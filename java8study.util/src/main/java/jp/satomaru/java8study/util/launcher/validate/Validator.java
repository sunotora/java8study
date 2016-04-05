package jp.satomaru.java8study.util.launcher.validate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

/**
 * リクエストのバリデーションを行います。
 * 
 * @param <K> 対象となる引数のキー
 * @param <V> 対象となる引数の型
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Validator<K, V> {

	/**
	 * インデックスに関連付けられた引数のバリデーションを開始します。
	 * 
	 * @param index 引数のインデックス
	 * @param type 引数の型
	 * @return バリデーター
	 */
	public static <V> Validator<Integer, V> forArgument(Integer index, Class<V> type) {
		ValidatorContext<Integer, V> context = new ValidatorContext<>();
		context.setKey(index);
		context.setType(type);
		context.setInvalidSupplier(InvalidArgument::new);
		return new Validator<>(context);
	}

	/**
	 * 名前に関連付けられた引数のバリデーションを開始します。
	 * 
	 * @param name 引数の名前
	 * @param type 引数の型
	 * @return バリデーター
	 */
	public static <V> Validator<String, V> forParameter(String name, Class<V> type) {
		ValidatorContext<String, V> context = new ValidatorContext<>();
		context.setKey(name);
		context.setType(type);
		context.setInvalidSupplier(InvalidParameter::new);
		return new Validator<>(context);
	}

	/** バリデーターコンテキスト。 */
	private final ValidatorContext<K, V> context;

	/** 引数取得時に発生した例外に、バリデーション例外のメッセージを割り当てる関数のリスト。 */
	private final List<Consumer<RuntimeException>> catchList = new ArrayList<>();

	/** バリデーション例外の標準メッセージを作成する関数。 */
	private Supplier<String> defaultMessageSupplier;

	/**
	 * 引数取得時に発生した例外に、バリデーション例外のメッセージを割り当てます。
	 * 
	 * @param condition メッセージを割り当てる条件
	 * @param messageSupplier 割り当てるメッセージを作成する関数
	 * @return このオブジェクト自身
	 */
	public Validator<K, V> when(Predicate<RuntimeException> condition, Supplier<String> messageSupplier) {
		catchList.add(exception -> {
			if (condition.test(exception)) {
				throw context.createInvalid(messageSupplier);
			}
		});

		return this;
	}

	/**
	 * 引数取得時に発生した例外に、バリデーション例外のメッセージを割り当てます。
	 * 
	 * @param target メッセージを割り当てる例外
	 * @param messageSupplier 割り当てるメッセージを作成する関数
	 * @return このオブジェクト自身
	 */
	public Validator<K, V> when(Class<? extends RuntimeException> target, Supplier<String> messageSupplier) {
		return when(target::isInstance, messageSupplier);
	}

	/**
	 * バリデーション例外の標準メッセージを設定します。
	 * 
	 * <p>
	 * {@link #when(Predicate, Supplier)}および{@link #when(Class, Supplier)}に該当しない場合は、
	 * このメッセージが割り当てられます。
	 * </p>
	 * 
	 * @param defaultMessageSupplier バリデーション例外の標準メッセージを作成する関数
	 * @return このオブジェクト自身
	 */
	public Validator<K, V> whenOthers(Supplier<String> defaultMessageSupplier) {
		this.defaultMessageSupplier = defaultMessageSupplier;
		return this;
	}

	/**
	 * リクエストから引数を取得して、単項目バリデーターを作成します。
	 * 
	 * <p>
	 * getterには{@code Request::get}を指定してください。
	 * これは、{@link jp.satomaru.java8study.util.launcher.message.Request#get(Class, int) Request#get(Class, int)}、
	 * あるいは{@link jp.satomaru.java8study.util.launcher.message.Request#get(Class, String) Request#get(Class, String)}を指します。
	 * </p>
	 * 
	 * <p>
	 * 引数を取得する際に例外が発生した場合は、
	 * {@link #when(Predicate, Supplier)}、{@link #when(Class, Supplier)}、{@link #whenOthers(Supplier)}
	 * を元に、バリデーション例外が送出されます。
	 * </p>
	 * 
	 * @param getter リクエストから引数を取得する関数
	 * @return 単項目バリデーター
	 */
	public UnitValidator<K, V> get(BiFunction<K, Class<V>, Optional<V>> getter) {
		try {
			return new UnitValidator<>(context, context.getValue(getter));
		} catch (Invalid e) {
			throw e;
		} catch (RuntimeException e) {
			catchList.forEach(catcher -> catcher.accept(e));
			throw context.createInvalid(defaultMessageSupplier);
		}
	}
}

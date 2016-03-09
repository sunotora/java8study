package jp.satomaru.java8study.util;

import java.util.Collections;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import jp.satomaru.java8study.util.variable.VariableBase;
import lombok.ToString;

/**
 * インデックス番号を割り振られた1次元配列の基底クラスです。
 *
 * @param <T> 配列要素の値
 * @param <E> 配列要素
 */
@ToString
public abstract class LineBase<T, E extends VariableBase<T, E> & Indexed> {

	/** 配列要素。 */
	private final Map<Integer, E> elements;

	/** 配列要素数。 */
	private final int size;

	/**
	 * コンストラクタ。
	 * 
	 * <p>
	 * 生成される要素のインデックス番号は、0から配列要素数-1となります。
	 * 第2引数は、配列要素の初期値生成に使用されます。
	 * </p>
	 * 
	 * @param size 配列要素数
	 * @param indexToValue インデックス番号を受け取り、 配列要素を返す関数
	 * @throws IllegalArgumentException 配列要素数がマイナスである場合
	 * @throws IllegalStateException インデックス番号が重複している場合
	 */
	public LineBase(int size, IntFunction<E> indexToValue) {
		this.size = Args.of("size", size).min(0).get();

		elements = IntStream.range(0, size)
			.mapToObj(indexToValue)
			.collect(Collectors.collectingAndThen(
				Collectors.toMap(Indexed::getIndex, element -> element),
				Collections::unmodifiableMap));
	}

	/**
	 * 配列要素数を取得します。
	 * 
	 * @return 配列要素数
	 */
	public final int size() {
		return size;
	}

	/**
	 * 配列要素を取得します。
	 * 
	 * @param index インデックス番号
	 * @return 配列要素
	 */
	public final Optional<E> optional(int index) {
		return Optional.ofNullable(elements.get(index));
	}

	/**
	 * 指定されたオブジェクトとインデックス番号が等しい配列要素を取得します。
	 * 
	 * @param index インデックス番号
	 * @return 配列要素
	 */
	public final Optional<E> optional(Indexed indexed) {
		return optional(indexed.getIndex());
	}

	/**
	 * 配列要素を取得します。
	 * 
	 * @param index インデックス番号
	 * @return 配列要素
	 * @throws NoSuchElementException 配列要素が存在しない場合。
	 */
	public final E get(int index) {
		return optional(index).get();
	}

	/**
	 * 指定されたオブジェクトとインデックス番号が等しい配列要素を取得します。
	 * 
	 * @param indexed インデックス番号を持つオブジェクト
	 * @return 配列要素
	 * @throws NoSuchElementException 配列要素が存在しない場合。
	 */
	public final E get(Indexed indexed) {
		return optional(indexed).get();
	}

	/**
	 * 全ての配列要素を含んだストリームを取得します。
	 * 
	 * @return ストリーム
	 */
	public final Stream<E> stream() {
		return elements.values().stream();
	}

	/**
	 * 全ての配列要素の値を含んだストリームを取得します。
	 * 
	 * @return ストリーム
	 */
	public final Stream<T> values() {
		return stream().map(VariableBase::getValue);
	}
}

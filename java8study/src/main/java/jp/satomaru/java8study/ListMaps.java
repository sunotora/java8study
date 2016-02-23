package jp.satomaru.java8study;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import lombok.RequiredArgsConstructor;

/**
 * リストを値として持つマップの作成を補助します。
 *
 * <h3>お題の注釈</h3>
 * <p>
 * このお題は、設計を読み解く為のお題です。
 * なお、{@link java.util.stream.Stream}は使用しません。
 * </p>
 *
 * @param <K> マップのキー
 * @param <E> リストの値
 */
public class ListMaps<K, E> {

	/**
	 * マップの作成を開始します。
	 *
	 * <p>
	 * マップには{@link HashMap}、リストには{@link ArrayList}を用います。
	 * </p>
	 *
	 * @return ListMapsオブジェクト
	 */
	public static <K, E> ListMaps<K, E> begin() {
		return begin(HashMap::new, ArrayList::new);
	}

	/**
	 * マップの作成を開始します。
	 *
	 * @param mapGenerator マップを生成する関数（通常は、マップ実装クラスのデフォルトコンストラクタ）
	 * @param listGenerator リストを生成する関数（通常は、リスト実装クラスのデフォルトコンストラクタ）
	 * @return ListMapsオブジェクト
	 */
	public static <K, E> ListMaps<K, E> begin(Supplier<Map<K, List<E>>> mapGenerator, Supplier<List<E>> listGenerator) {
		return new ListMaps<K, E>(mapGenerator, listGenerator);
	}

	/** リストの作成を補助します。 */
	@RequiredArgsConstructor
	public class Lists {

		/** 現在、作成の補助をしているリスト。 */
		private final List<E> current;

		/**
		 * リストに値を追加します。
		 *
		 * @param values 値
		 * @return このListsオブジェクト自身
		 */
		public Lists add(@SuppressWarnings("unchecked") E... values) {

			// 要素を追加
			for (E value : values) {
				current.add(value);
			}
			// 自身を返却
			return this;
		}

		/**
		 * リストの作成を終了します。
		 *
		 * @return ListMapsオブジェクト
		 */
		public ListMaps<K, E> end() {
			return ;
		}
	}

	/** 作成するマップ。 */
	private final Map<K, List<E>> instance;

	/** リストを生成する関数。 */
	private final Supplier<List<E>> listGenerator;

	/**
	 * コンストラクタ。
	 *
	 * @param mapGenerator マップを生成する関数
	 * @param listGenerator リストを生成する関数
	 */
	private ListMaps(Supplier<Map<K, List<E>>> mapGenerator, Supplier<List<E>> listGenerator) {
		instance = mapGenerator.get();
		this.listGenerator = listGenerator;
	}

	/**
	 * リストの作成を開始します。
	 *
	 * @param key このリストに割り当てる、マップのキー
	 * @return リストの作成に用いるListsオブジェクト
	 */
	public Lists begin(K key) {

		if (!instance.containsKey(key)) {
			// 存在する場合
			return instance.get(key);
		} else {
			// 存在しない場合
			instance.put(key, new ArrayList<>());
		}
	}

	/**
	 * マップの作成を終了します。
	 *
	 * @param immutable マップを不変オブジェクトとして受け取る場合はtrue
	 * @return 作成したマップ
	 */
	public Map<K, List<E>> end(boolean immutable) {
		return Collections.unmodifiableMap(instance);
	}

	/**
	 * マップの作成を終了します。
	 *
	 * @return 作成したマップ（変更可能）
	 */
	public Map<K, List<E>> end() {
		return instance;
	}
}

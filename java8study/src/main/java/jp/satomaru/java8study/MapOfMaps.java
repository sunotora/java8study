package jp.satomaru.java8study;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import lombok.RequiredArgsConstructor;

/**
 * マップを値として持つマップの作成を補助します。
 *
 * @param <K> 親マップのキー
 * @param <S> 子マップのキー
 * @param <V> 子マップの値
 */
public class MapOfMaps<K, S, V> {

	/**
	 * 親マップの作成を開始します。
	 *
	 * <p>
	 * 親・子マップには{@link HashMap}を用います。
	 * </p>
	 *
	 * @return MapOfMapsオブジェクト
	 */
	public static <K, S, V> MapOfMaps<K, S, V> begin() {
		return begin(HashMap::new, HashMap::new);
	}

	/**
	 * 親マップの作成を開始します。
	 *
	 * @param keyMapGenerator 親マップを生成する関数（通常は、マップ実装クラスのデフォルトコンストラクタ）
	 * @param valueMapGenerator 子マップを生成する関数（通常は、マップ実装クラスのデフォルトコンストラクタ）
	 * @return MapOfMapsオブジェクト
	 */
	public static <K, S, V> MapOfMaps<K, S, V> begin(Supplier<Map<K, Map<S, V>>> keyMapGenerator, Supplier<Map<S, V>> valueMapGenerator) {
		return new MapOfMaps<K, S, V>(keyMapGenerator, valueMapGenerator);
	}

	/** 子マップの作成を補助します。 */
	@RequiredArgsConstructor
	public class Maps {

		/** 現在、作成の補助をしている子マップ。 */
		private final Map<S, V> current;

		/**
		 * 子マップに値を追加します。
		 *
		 * @param key キー
		 * @param values 値
		 * @return このMapsオブジェクト自身
		 */
		public Maps add(S key, V value) {

			// 要素を追加
			current.put(key, value);

			// 自身を返却
			return this;
		}

		/**
		 * 子マップの作成を終了します。
		 *
		 * @return MapOfMapsオブジェクト
		 */
		public MapOfMaps<K, S, V> end() {

			// staticでない内部クラスから、その定義クラスのインスタンスを参照
			return MapOfMaps.this;
		}
	}

	/** 作成する親マップ。 */
	private final Map<K, Map<S, V>> instance;

	/** 子マップを生成する関数。 */
	private final Supplier<Map<S, V>> valueMapGenerator;

	/**
	 * コンストラクタ。
	 *
	 * @param keyMapGenerator 親マップを生成する関数
	 * @param valueMapGenerator 子マップを生成する関数
	 */
	private MapOfMaps(Supplier<Map<K, Map<S, V>>> keyMapGenerator, Supplier<Map<S, V>> valueMapGenerator) {

		// 親マップのインスタンスを生成
		instance = keyMapGenerator.get();
		// 子マップのジェネレーターを保持
		this.valueMapGenerator = valueMapGenerator;
	}

	/**
	 * 子マップの作成を開始します。
	 *
	 * @param key この子マップに割り当てる、マップのキー
	 * @return 子マップの作成に用いるMapsオブジェクト
	 */
	public Maps begin(K key) {

		if (instance.containsKey(key)) {
			// 存在する場合、親マップから子マップを取得してMapsに変換して返却
			return new Maps(instance.get(key));
		} else {
			// 存在しない場合、新たにMapsを作成して親マップにputし、Mapsを返却
			MapOfMaps<K, S, V>.Maps maps = new Maps(valueMapGenerator.get());
			instance.put(key, maps.current);
			return maps;
		}
	}

	/**
	 * 親マップの作成を終了します。
	 *
	 * @param immutable 親マップを不変オブジェクトとして受け取る場合はtrue
	 * @return 作成した親マップ
	 */
	public Map<K, Map<S, V>> end(boolean immutable) {

		// Map<S, V>をイミュータブルに変更
		instance.replaceAll((key, map) -> Collections.unmodifiableMap(map));

		return Collections.unmodifiableMap(instance);
	}

	/**
	 * 親マップの作成を終了します。
	 *
	 * @return 作成した親マップ（変更可能）
	 */
	public Map<K, Map<S, V>> end() {
		return instance;
	}
}

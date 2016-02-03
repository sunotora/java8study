package jp.satomaru.java8study;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import lombok.Value;

/**
 * 四則演算を行います。
 */
public class Calculator {

	/**
	 * メインメソッド。
	 *
	 * <p>
	 * 引数は以下の様に指定します。
	 * </p>
	 *
	 * <ul>
	 * <li>第1引数： 演算子<ul><li>加算： "add"</li><li>減算： "sub"</li><li>乗算： "mul"</li><li>除算： "div"</li></ul></li>
	 * <li>第2引数： 演算子の左項</li>
	 * <li>第3引数： 演算子の右項</li>
	 * </ul>
	 *
	 * @param args 引数
	 */
	public static void main(String[] args) {
		Items items = Items.of(new BigDecimal(args[1]), new BigDecimal(args[2]));
		BigDecimal result = new Calculator().execute(args[0], items);
		System.out.println(result);
	}

	/** 演算項目。 */
	@Value(staticConstructor="of")
	private static class Items {
		/** 左項。 */
		private final BigDecimal left;
		/** 右項。 */
		private final BigDecimal right;
	}

	/** 演算アクションマップ。キーは演算子。値は演算を行うFunction。 */
	private final Map<String, Function<Items, BigDecimal>> actionMap;
	// Function<T, R>はT型を受け取ってR型を返却する関数型インターフェース

	/**
	 * コンストラクタ。
	 */
	public Calculator() {
		// actionMapを初期化し、四則演算を行うFunctionを、演算子に関連付けて保存してください。
		actionMap = new HashMap<>();

		// if文のように１行のみとなる場合は{}やreturnが省略できる。
		// 加算
		actionMap.put("add", Items -> Items.left.add(Items.right));
		// 減算
		actionMap.put("sub", Items -> Items.left.subtract(Items.right));
		// 積算
		actionMap.put("mul", Items -> Items.left.multiply(Items.right));
		// 除算
		actionMap.put("div", Items -> Items.left.divide(Items.right));

		// ラムダ式が複数行になる場合はreturnが必要。
		//actionMap.put("add", (Items) -> {return Items.left.add(Items.right);});
		// ラムダ式のアロー演算子->の左辺が単項目の場合のみ、括弧が省略できる
		//actionMap.put("add", Items -> Items.left.add(Items.right));

		// 古の手法
//		actionMap.put("add", new Function<Calculator.Items, BigDecimal>() {
//			@Override
//			public BigDecimal apply(Items t) {
//				return t.left.add(t.right);
//			}
//		});
	}

	/**
	 * 演算を行います。
	 *
	 * @param operator 演算子
	 * @param items 演算項目
	 * @return 演算結果
	 */
	public BigDecimal execute(String operator, Items items) {
		// actionMapから演算子に関連付けられたFunctionを取得し、実行してその結果を返却してください。
		return actionMap.get(operator).apply(items);
	}

	/**
	 * テスト用Itemsビルダー
	 * @param left 左項
	 * @param right 右項
	 * @return
	 */
	Items itemsBuilder(BigDecimal left, BigDecimal right) {
		return Items.of(left, right);
	}
}

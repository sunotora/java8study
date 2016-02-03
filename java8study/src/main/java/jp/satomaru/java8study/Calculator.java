package jp.satomaru.java8study;

import java.math.BigDecimal;
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

	/**
	 * コンストラクタ。
	 */
	public Calculator() {
		// TODO actionMapを初期化し、四則演算を行うFunctionを、演算子に関連付けて保存してください。
	}

	/**
	 * 演算を行います。
	 * 
	 * @param operator 演算子
	 * @param items 演算項目
	 * @return 演算結果
	 */
	public BigDecimal execute(String operator, Items items) {
		// TODO actionMapから演算子に関連付けられたFunctionを取得し、実行してその結果を返却してください。
	}
}

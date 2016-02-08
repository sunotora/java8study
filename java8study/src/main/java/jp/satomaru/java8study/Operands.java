package jp.satomaru.java8study;

import java.math.BigDecimal;
import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Operands {

	private final BigDecimal left;
	private final BigDecimal right;

	public BigDecimal execute(BinaryOperator<BigDecimal> action) {
		return action.apply(left, right);
	}

	public BigDecimal executeLeft(UnaryOperator<BigDecimal> action) {
		return action.apply(left);
	}

	public BigDecimal executeRight(UnaryOperator<BigDecimal> action) {
		return action.apply(right);
	}

	public static void main(String[] args) {

		Operands me = new Operands(BigDecimal.valueOf(1), BigDecimal.valueOf(2));

		// 足し算をさせてみてください。

		// メソッド参照 (Class::staticMethod）
		System.out.println(me.execute(Operands::add));

		// メソッド参照（Class::instanceMethod）
		System.out.println(me.execute(BigDecimal::add));

		// ラムダ式
		System.out.println(me.execute((left, right) -> left.add(right)));

		// 関数インターフェース
		// BinaryOperator<T> extends BiFunction<T,T,T>
		// T apply(T t, T u)         R apply(T t, U u);
		// ラムダ式
		// (T, T) -> T
		// BinaryOperator<BigDecimal>
		// BigDecimal apply(BigDecimal t, BigDecimal u)
		// この関数インターフェースを用いる時、２引数を入力し、同じ型を返す
		// ラムダ式・関数インターフェース・メソッド参照が使用可能となる
		// BigDecimal::addは (x, y) -> x.add(y)と同様の処理が実施される

		// object::instanceMethod = (x, y, z) -> instanceMethod(x, y, z)     ex) System.out::println =  x     -> System.out.println(x);
		// Class::staticMethod    = (x, y, z) -> Class.staticMethod(x, y, z) ex) Math::pow           = (x, y) -> Math.pow(x, y);
		// Class::instanceMethod  = (x, y, z) -> x.instanceMethod(y, z)      ex) BigDecimal::add     = (x, y) -> x.add(y);
		// 三番目だけ動作が違うため注意する。
	}

	private static BigDecimal add(BigDecimal left, BigDecimal right) {
		return left.add(right);
	}
}

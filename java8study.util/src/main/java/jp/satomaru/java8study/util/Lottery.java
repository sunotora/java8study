package jp.satomaru.java8study.util;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 数字を重複せずにランダムに抽選します。
 */
public class Lottery implements Iterator<Integer> {

	/** 数字のリスト。 */
	private final LinkedList<Integer> list;

	/**
	 * コンストラクタ。
	 * 
	 * <p>
	 * 抽選対象となる数字は、1から引数で指定された数字までとなります。
	 * </p>
	 * 
	 * @param max 抽選に用いる最大の数字
	 */
	public Lottery(int max) {
		list = IntStream.range(1, max + 1).boxed().collect(Collectors.toCollection(LinkedList::new));
	}

	/**
	 * 抽選を行います。
	 * 
	 * <p>
	 * 抽選される数字は重複しません。
	 * 数字が残っていない場合は、{@link Optional#empty()}になります。
	 * </p>
	 * 
	 * @return 抽選した数字（数字がなくなった場合は{@link Optional#empty()}）
	 */
	public Optional<Integer> draw() {
		return (list.isEmpty())
				? Optional.empty()
				: Optional.of(list.remove(Numbers.randomInt(list.size())));
	}

	/**
	 * まだ抽選されていない数字が残っていることを判定します。
	 * 
	 * @return まだ抽選されていない数字が残っている場合はtrue
	 */
	@Override
	public boolean hasNext() {
		return !list.isEmpty();
	}

	/**
	 * 抽選を行います。
	 * 
	 * <p>
	 * これは、以下のコードと同値です。
	 * </p>
	 * 
	 * <p>
	 * {@code draw().get()}
	 * </p>
	 * 
	 * @return 抽選した数字
	 * @throws java.util.NoSuchElementException 数字が残っていない場合
	 * @see #draw()
	 */
	@Override
	public Integer next() {
		return draw().get();
	}
}

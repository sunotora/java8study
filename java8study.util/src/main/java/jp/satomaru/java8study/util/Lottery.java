package jp.satomaru.java8study.util;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 福引です。
 * 
 * <p>
 * いわゆる「ボックスガチャ」を実現します。
 * </p>
 */
public class Lottery<T> implements Iterator<T> {

	/** 要素（福引の中身）。 */
	private final LinkedList<T> elements;

	/**
	 * コンストラクタ
	 * 
	 * @param elements 要素（福引の中身）
	 */
	public Lottery(Collection<T> elements) {
		this.elements = new LinkedList<>(elements);
	}

	/**
	 * コンストラクタ
	 * 
	 * @param elements 要素（福引の中身）
	 */
	public Lottery(Stream<T> elements) {
		this.elements = elements.collect(Collectors.toCollection(LinkedList::new));
	}

	/**
	 * コンストラクタ
	 * 
	 * @param elements 要素（福引の中身）
	 */
	@SafeVarargs
	public Lottery(T... elements) {
		this.elements = new LinkedList<>();
		Collections.addAll(this.elements, elements);
	}

	/**
	 * 抽選を行います。
	 * 
	 * @return 抽選した要素（要素がなくなった場合は{@link Optional#empty()}）
	 */
	public Optional<T> draw() {
		return (elements.isEmpty())
				? Optional.empty()
				: Optional.of(elements.remove(Numbers.randomInt(elements.size())));
	}

	/**
	 * まだ抽選されていない要素が残っていることを判定します。
	 * 
	 * @return まだ抽選されていない要素が残っている場合はtrue
	 */
	@Override
	public boolean hasNext() {
		return !elements.isEmpty();
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
	 * @return 抽選した要素
	 * @throws java.util.NoSuchElementException 要素が残っていない場合
	 * @see #draw()
	 */
	@Override
	public T next() {
		return draw().get();
	}
}

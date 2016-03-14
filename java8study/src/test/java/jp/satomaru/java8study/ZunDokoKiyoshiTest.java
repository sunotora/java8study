package jp.satomaru.java8study;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.ArrayDeque;
import java.util.function.Supplier;

import jp.satomaru.java8study.ZunDokoKiyoshi.Word;

import org.junit.Before;
import org.junit.Test;

/**
 * うわさのズンドコキヨシ関数のテストケースです。
 */
public class ZunDokoKiyoshiTest {

	/** 「ズン」と「ドコ」を生成する関数。 */
	private Supplier<Word> generator;

	/**
	 * 前準備。
	 */
	@Before
	public void setUp() {
		generator = () -> (Math.random() < 0.5) ? Word.ZUN : Word.DOKO;
	}

	/**
	 * うわさのズンドコキヨシ関数の結果を検査します。
	 */
	@Test
	public void testExecute() {
		for (int i = 0; i < 1000; i++) {
			ArrayDeque<Word> actual = new ArrayDeque<>();
			ZunDokoKiyoshi.execute(generator, actual::add);

			assertThat(actual, hasSize(greaterThanOrEqualTo(6)));
			assertThat(actual.removeLast(), is(Word.KIYOSHI));
			assertThat(actual.removeLast(), is(Word.DOKO));
			assertThat(actual.removeLast(), is(Word.ZUN));
			assertThat(actual.removeLast(), is(Word.ZUN));
			assertThat(actual.removeLast(), is(Word.ZUN));
			assertThat(actual.removeLast(), is(Word.ZUN));

			int zunChain = 0;

			while (!actual.isEmpty()) {
				switch (actual.removeFirst()) {
					case ZUN:
						++zunChain;
						break;

					case DOKO:
						assertThat(zunChain, lessThan(4));
						zunChain = 0;
						break;

					default:
						fail();
				}
			}
		}
	}

	/**
	 * うわさのズンドコキヨシ関数の性能を測定します。
	 */
	@Test
	public void measureExecute() {
		int count = 1_000_000;
		long start = System.currentTimeMillis();

		for (int i = 0; i < count; i++) {
			ZunDokoKiyoshi.execute(generator, word -> {});
		}

		long end = System.currentTimeMillis();
		long total = end - start;
		System.out.println(String.format("%,d count : total %,d msec", count, total));
	}
}

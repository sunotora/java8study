package jp.satomaru.java8study.util;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;

import jp.satomaru.java8study.util.variable.TwoDimensionalInteger;

public class IntegerMatrixTest {

	/** マトリクスの幅。 */
	private static final int MATRIX_WIDTH = 4;

	/** マトリクスの高さ。 */
	private static final int MATRIX_HEIGHT = 5;

	MatrixBase<Integer, TwoDimensionalInteger> matrix;


	@Before
	public void before() {
		matrix = new IntegerMatrix(MATRIX_WIDTH, MATRIX_HEIGHT);

		int count = 1;
		for (int y = MATRIX_HEIGHT-1; y >= 0; y--) {
			for (int x = 0; x <= MATRIX_WIDTH-1; x++) {
				matrix.get(x, y).setValue(count);
				count++;
			}
		}
	}

	@Test
	public void isArroundTest() {

		/**
		 * 指定された座標の周囲の要素を含んだストリームを取得します。
		 * 例）  1,  2,  3,  4
		 *       5,  6,  7,  8
		 *       9, 10, 11, 12
		 *      13, 14, 15, 16
		 *      17, 18, 19, 20
		 *     入力が"7"の座標：返却ストリーム{2,3,4,6,8,10,11,12}
		 *     入力が"1"の座標：返却ストリーム{2,5,6}
		 *     入力が"20"の座標：返却ストリーム{15,16,19}
		 */
		//     入力が"7"の座標：返却ストリーム{2,3,4,6,8,10,11,12}
		List<Integer> list = matrix.arround(MATRIX_WIDTH -2, MATRIX_HEIGHT -2)
			.map(elem -> elem.getValue())
			.collect(Collectors.toList());

		assertThat(list, hasItems(toInt(2), toInt(3), toInt(4), toInt(6), toInt(8), toInt(10), toInt(11), toInt(12)));

		//     入力が"1"の座標：返却ストリーム{2,5,6}
		List<Integer> list2 = matrix.arround(0, MATRIX_HEIGHT -1)
				.map(elem -> elem.getValue())
				.collect(Collectors.toList());

		assertThat(list2, hasItems(toInt(2), toInt(5), toInt(6)));

		//     入力が"20"の座標：返却ストリーム{15,16,19}
		List<Integer> list3 = matrix.arround(MATRIX_WIDTH -1, 0)
				.map(elem -> elem.getValue())
				.collect(Collectors.toList());

		assertThat(list3, hasItems(toInt(15), toInt(16), toInt(19)));

	}

	private Integer toInt(int a) {
		return Integer.valueOf(a);
	}
}

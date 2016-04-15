package jp.co.satomaru.java8study.minesweeper;

import jp.co.satomaru.java8study.minesweeper.util.MINE_FIELD_MASS;
import jp.co.satomaru.java8study.minesweeper.util.MINE_FIELD_STATE;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * 地雷原のマス
 */
@AllArgsConstructor
@EqualsAndHashCode(of = {"mass", "state"}, callSuper = false)
public class MineField {

	@Getter
	@Setter
	private MINE_FIELD_MASS mass;

	@Getter
	@Setter
	private MINE_FIELD_STATE state;

	public String getMassString() {
		return mass.toString();
	}

	public String getStateString() {
		return state.toString();
	}

}
package jp.co.satomaru.java8study.minesweeper.util;

public enum MINE_FIELD_STATE {
	CLOSE(""),
	OPEN("O"),
	FLAG("F"),
	ON_HOLD("?"),
	;

	private String character;

	MINE_FIELD_STATE(String character) {
		this.character = character;
	}

	@Override
	public String toString() {
		return this.character;
	}
}

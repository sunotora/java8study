package jp.co.satomaru.java8study.minesweeper.util;

public enum MINE_FIELD {
	MINE("*"),
	ZERO("0"),
	ONE("1"),
	TWO("2"),
	THREE("3"),
	FOUR("4"),
	FIVE("5"),
	SIX("6"),
	SEVEN("7"),
	EIGHT("8"),
	;

	private String character;

	MINE_FIELD(String character) {
		this.character = character;
	}

	@Override
	public String toString() {
		return this.character;
	}

	public static String valueOf(long count) {
		switch (Long.valueOf(count).intValue()) {
			case 0:
				return ZERO.toString();
			case 1:
				return ONE.toString();
			case 2:
				return TWO.toString();
			case 3:
				return THREE.toString();
			case 4:
				return FOUR.toString();
			case 5:
				return FIVE.toString();
			case 6:
				return SIX.toString();
			case 7:
				return SEVEN.toString();
			case 8:
				return EIGHT.toString();
		}

		return "";
	}
}

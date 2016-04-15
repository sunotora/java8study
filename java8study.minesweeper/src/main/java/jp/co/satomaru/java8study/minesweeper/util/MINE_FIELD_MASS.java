package jp.co.satomaru.java8study.minesweeper.util;

public enum MINE_FIELD_MASS {
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

	MINE_FIELD_MASS(String character) {
		this.character = character;
	}

	@Override
	public String toString() {
		return this.character;
	}

	public static MINE_FIELD_MASS valueOf(long count) {
		switch (Long.valueOf(count).intValue()) {
			case 0:
				return ZERO;
			case 1:
				return ONE;
			case 2:
				return TWO;
			case 3:
				return THREE;
			case 4:
				return FOUR;
			case 5:
				return FIVE;
			case 6:
				return SIX;
			case 7:
				return SEVEN;
			case 8:
				return EIGHT;
		}

		return ZERO;
	}
}

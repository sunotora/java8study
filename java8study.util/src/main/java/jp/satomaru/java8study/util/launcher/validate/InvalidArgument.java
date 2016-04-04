package jp.satomaru.java8study.util.launcher.validate;

import lombok.Getter;

/**
 * インデックスに関連付けられた引数のバリデーション例外です。
 * 
 * @see Validator
 */
public class InvalidArgument extends Invalid {

	private static final long serialVersionUID = 1L;

	/** 例外が発生した引数のインデックス。 */
	@Getter private final Integer index;

	/**
	 * コンストラクタ。
	 * 
	 * @param index 例外が発生した引数のインデックス
	 * @param message メッセージ
	 */
	public InvalidArgument(Integer index, String message) {
		super(message);
		this.index = index;
	}

	/**
	 * 文字列表現を取得します。
	 * 
	 * @return 文字列表現
	 */
	@Override
	public String toString() {
 		return String.format("%s[%d]", super.toString(), index);
	}
}

package jp.satomaru.java8study.util.launcher.validate;

import lombok.Getter;

/**
 * 名前に関連付けられた引数のバリデーション例外です。
 * 
 * @see Validator
 */
public class InvalidParameter extends Invalid {

	private static final long serialVersionUID = 1L;

	/** 例外が発生した引数の名前。 */
	@Getter private final String name;

	/**
	 * コンストラクタ。
	 * 
	 * @param name 例外が発生した引数の名前
	 * @param message メッセージ
	 */
	public InvalidParameter(String name, String message) {
		super(message);
		this.name = name;
	}

	/**
	 * 文字列表現を取得します。
	 * 
	 * @return 文字列表現
	 */
	@Override
	public String toString() {
 		return String.format("%s[%s]", super.toString(), name);
	}
}

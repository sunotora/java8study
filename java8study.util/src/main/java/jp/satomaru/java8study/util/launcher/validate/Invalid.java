package jp.satomaru.java8study.util.launcher.validate;

/**
 * バリデーション例外です。
 * 
 * @see Validator
 */
public abstract class Invalid extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * コンストラクタ。
	 * 
	 * @param message メッセージ
	 */
	public Invalid(String message) {
		super(message);
	}
}

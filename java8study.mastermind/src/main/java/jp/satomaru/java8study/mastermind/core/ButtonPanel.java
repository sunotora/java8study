package jp.satomaru.java8study.mastermind.core;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

/**
 * ボタン群のパネルです。
 */
@SuppressWarnings("restriction")
public class ButtonPanel {

	/** JavaFXアンカーペイン。 */
	private final AnchorPane anchor = new AnchorPane();

	/** 決定ボタン。 */
	private final Button decideButton;

	/** 降参ボタン。 */
	private final Button giveupButton;

	/** リセットボタン。 */
	private final Button resetButton;

	/**
	 * コンストラクタ。
	 * 
	 * @param onDecide 決定ボタン押下時の処理
	 * @param onGiveup 降参ボタン押下時の処理
	 * @param onReset リセットボタン押下時の処理
	 */
	public ButtonPanel(
			Runnable onDecide,
			Runnable onGiveup,
			Runnable onReset) {

		decideButton = createButton("決定");
		decideButton.setOnAction(event -> onDecide.run());

		giveupButton = createButton("降参");
		giveupButton.setOnAction(event -> onGiveup.run());

		resetButton = createButton("リセット");
		resetButton.setOnAction(event -> onReset.run());

		VBox vBox = new VBox(decideButton, giveupButton, resetButton);
		vBox.setPadding(new Insets(0.0, 0.0, 0.0, 16.0));
		vBox.setSpacing(4.0);

		anchor.getChildren().add(vBox);
		AnchorPane.setBottomAnchor(vBox, 0.0);
		AnchorPane.setRightAnchor(vBox, 0.0);
	}

	/**
	 * このパネルのJavaFXペインを取得します。
	 * 
	 * @return JavaFXペイン
	 */
	public Pane getPane() {
		return anchor;
	}

	/**
	 * 決定ボタンの非活性状態を設定します。
	 * 
	 * @param disable 非活性にする場合はtrue
	 * @return このオブジェクト自身
	 */
	public ButtonPanel setDecideDisable(boolean disable) {
		decideButton.setDisable(disable);
		return this;
	}

	/**
	 * 降参ボタンの非活性状態を設定します。
	 * 
	 * @param disable 非活性にする場合はtrue
	 * @return このオブジェクト自身
	 */
	public ButtonPanel setGiveupDisable(boolean disable) {
		giveupButton.setDisable(disable);
		return this;
	}

	/**
	 * リセットボタンの非活性状態を設定します。
	 * 
	 * @param disable 非活性にする場合はtrue
	 * @return このオブジェクト自身
	 */
	public ButtonPanel setResetDisable(boolean disable) {
		resetButton.setDisable(disable);
		return this;
	}

	/**
	 * ボタンを作成します。
	 * 
	 * @param caption ボタンのキャプション
	 * @return ボタン
	 */
	private Button createButton(String caption) {
		Button button = new Button(caption);
		button.setPadding(new Insets(0.0));
		button.setFont(Font.font("Arial", 14.0));
		button.setMinSize(64.0, 24.0);
		return button;
	}
}

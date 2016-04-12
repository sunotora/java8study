package jp.co.satomaru.java8study.minesweeper.core;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * ツール群のパネルです。
 */
@SuppressWarnings("restriction")
public class ToolPanel {

	/** JavaFXアンカーペイン。 */
	private final AnchorPane anchor = new AnchorPane();

	/** 時間テキスト */
	private final Text timeText = createText("00:00");

	/** 残地雷テキスト */
	private final Text remainLandMinesText;

	/** リセットボタン。 */
	private final Button resetButton;

	/**
	 * コンストラクタ。
	 *
	 * @param onReset リセットボタン押下時の処理
	 * @param remainLandMinesNum 残地雷数
	 */
	public ToolPanel(Runnable onReset, int remainLandMinesNum) {

		resetButton = createButton("R");
		resetButton.setOnAction(event -> onReset.run());

		remainLandMinesText = createText(String.valueOf(remainLandMinesNum));

		HBox hBox = new HBox(timeText, resetButton, remainLandMinesText);
		hBox.setPadding(new Insets(0.0, 0.0, 10.0, 0.0));
		hBox.setSpacing(30.0);

		anchor.getChildren().add(hBox);
		AnchorPane.setTopAnchor(hBox, 0.0);
		AnchorPane.setLeftAnchor(hBox, 0.0);

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
	 * リセットボタンの非活性状態を設定します。
	 *
	 * @param disable 非活性にする場合はtrue
	 * @return このオブジェクト自身
	 */
	public ToolPanel setResetDisable(boolean disable) {
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
		button.setMinSize(32.0, 32.0);
		return button;
	}

	/**
	 * JavaFXテキストを作成します。
	 *
	 * @param string テキスト
	 * @return 作成したJavaFXテキスト
	 */
	private Text createText(String string) {
		Text text = new Text(string);
		text.setFont(Font.font("Arial", 14.0));
		return text;
	}

	/**
	 * JavaFXノードをJavaFXグリッドペインに配置します。
	 *
	 * @param node JavaFXノード
	 * @return 第1引数のJavaFXノード
	 */
	private <T extends Node> T setAnchorNode(T node) {

		VBox vBox = new VBox(node);
		vBox.setPadding(new Insets(0.0, 0.0, 0.0, 16.0));
		vBox.setSpacing(4.0);

		anchor.getChildren().add(vBox);
//		AnchorPane.setTopAnchor(vBox, 0.0);
//		AnchorPane.setLeftAnchor(vBox, 0.0);

		return node;
	}


}

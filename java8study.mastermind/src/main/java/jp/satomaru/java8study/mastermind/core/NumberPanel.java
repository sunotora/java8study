package jp.satomaru.java8study.mastermind.core;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import jp.satomaru.java8study.util.Matrix;
import jp.satomaru.java8study.util.TwoDimensional;
import jp.satomaru.java8study.util.variable.TwoDimensionalVariable;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

/**
 * 数字マトリクスのパネルです。
 */
public class NumberPanel {

	/** 数字マスへのアクセスを提供します。 */
	@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
	public final class NumberAdapter {

		/** 数字マスのボタン。 */
		private final Button button;

		/**
		 * 数字を設定します。
		 * 
		 * @param number 設定する数字
		 * @return このオブジェクト自身
		 */
		public NumberAdapter setNumber(Integer number) {
			button.setText((number != null) ? number.toString() : null);
			return this;
		}

		/**
		 * 非活性状態を設定します。
		 * 
		 * @param disable 非活性にする場合はtrue
		 * @return このオブジェクト自身
		 */
		public NumberAdapter setDisable(boolean disable) {
			button.setDisable(disable);
			return this;
		}
	}

	/** JavaFXグリッドペイン。 */
	private final GridPane grid = new GridPane();

	/** 数字マトリクス。 */
	private final Matrix<NumberAdapter> numbers;

	/** 行のヒット表示。 */
	private final List<Text> rowHits = new ArrayList<>();

	/** 行のブロー表示。 */
	private final List<Text> rowBlows = new ArrayList<>();

	/** 列のヒット表示。 */
	private final List<Text> colHits = new ArrayList<>();

	/** 列のブロー表示。 */
	private final List<Text> colBlows = new ArrayList<>();

	/**
	 * コンストラクタ。
	 * 
	 * @param width 数字マトリクスの幅
	 * @param height 数字マトリクスの高さ
	 * @param onLeftClick 数字マトリクスが左クリックされた時の処理
	 * @param onRightClick 数字マトリクスが右クリックされた時の処理
	 */
	public NumberPanel(
			int width,
			int height,
			Consumer<TwoDimensionalVariable<NumberAdapter>> onLeftClick,
			Consumer<TwoDimensionalVariable<NumberAdapter>> onRightClick) {

		grid.setHgap(2.0);
		grid.setVgap(2.0);

		setGridNode(createText("H"), width, height);
		setGridNode(createText("B"), width + 1, height + 1);

		numbers = new Matrix<>(width, height, (x, y) -> new NumberAdapter(createNumberButton(x, y, onLeftClick, onRightClick)));
		numbers.flat().forEach(item -> setGridNode(item.getValue().button, item.getX(), item.getY()));

		for (int i = 0; i < height; i++) {
			rowHits.add(setGridNode(createText(null), width, i));
			rowBlows.add(setGridNode(createText(null), width + 1, i));
		}

		for (int i = 0; i < width; i++) {
			colHits.add(setGridNode(createText(null), i, height));
			colBlows.add(setGridNode(createText(null), i, height + 1));
		}
	}

	/**
	 * このパネルのJavaFXペインを取得します。
	 * 
	 * @return JavaFXペイン
	 */
	public Pane getPane() {
		return grid;
	}

	/**
	 * 指定されたオブジェクトと2次元座標が等しい数字マスを取得します。
	 * 
	 * @param twoDimensional 2次元座標を持つオブジェクト
	 * @return 数字マス
	 */
	public TwoDimensionalVariable<NumberAdapter> getNumberNode(TwoDimensional twoDimensional) {
		return numbers.get(twoDimensional);
	}

	/**
	 * 数字マトリクスの非活性状態を設定します。
	 * 
	 * @param disable 非活性にする場合はtrue
	 * @return このオブジェクト自身
	 */
	public NumberPanel setNumberDisable(boolean disable) {
		numbers.flat().map(TwoDimensionalVariable::getValue).forEach(adapter -> adapter.setDisable(disable));
		return this;
	}

	/**
	 * 行のヒット表示を設定します。
	 * 
	 * @param y 行のY座標
	 * @param value 値
	 * @return このオブジェクト自身
	 */
	public NumberPanel setRowHit(int y, int value) {
		rowHits.get(y).setText(Integer.toString(value));
		return this;
	}

	/**
	 * 行のブロー表示を設定します。
	 * 
	 * @param y 行のY座標
	 * @param value 値
	 * @return このオブジェクト自身
	 */
	public NumberPanel setRowBlow(int y, int value) {
		rowBlows.get(y).setText(Integer.toString(value));
		return this;
	}

	/**
	 * 列のヒット表示を設定します。
	 * 
	 * @param x X座標
	 * @param value 値
	 * @return このオブジェクト自身
	 */
	public NumberPanel setColHit(int x, int value) {
		colHits.get(x).setText(Integer.toString(value));
		return this;
	}

	/**
	 * 列のブロー表示を設定します。
	 * 
	 * @param x X座標
	 * @param value 値
	 * @return このオブジェクト自身
	 */
	public NumberPanel setColBlow(int x, int value) {
		colBlows.get(x).setText(Integer.toString(value));
		return this;
	}

	/**
	 * 全てのヒット表示およびブロー表示を空欄にします。
	 * 
	 * @return このオブジェクト自身
	 */
	public NumberPanel clearAllHitsAndBlows() {
		rowHits.stream().forEach(node -> node.setText(null));
		rowBlows.stream().forEach(node -> node.setText(null));
		colHits.stream().forEach(node -> node.setText(null));
		colBlows.stream().forEach(node -> node.setText(null));
		return this;
	}

	/**
	 * JavaFXノードをJavaFXグリッドペインに配置します。
	 * 
	 * @param node JavaFXノード
	 * @param x 配置するX座標
	 * @param y 配置するY座標
	 * @return 第1引数のJavaFXノード
	 */
	private <T extends Node> T setGridNode(T node, int x, int y) {
		grid.getChildren().add(node);
		GridPane.setConstraints(node, x, y, 1, 1, HPos.CENTER, VPos.CENTER);
		return node;
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
	 * 数値マスのJavaFXボタンを作成します。
	 * 
	 * @param x X座標
	 * @param y Y座標
	 * @param onLeftClick 数字マトリクスが左クリックされた時の処理
	 * @param onRightClick 数字マトリクスが右クリックされた時の処理
	 * @return 作成したJavaFXボタン
	 */
	@SuppressWarnings("incomplete-switch")
	private Button createNumberButton(
			int x,
			int y,
			Consumer<TwoDimensionalVariable<NumberAdapter>> onLeftClick,
			Consumer<TwoDimensionalVariable<NumberAdapter>> onRightClick) {

		Button button = new Button();
		button.setPadding(new Insets(0.0));
		button.setFont(Font.font("Arial", 24.0));
		button.setMinSize(48.0, 48.0);
		button.setMaxSize(48.0, 48.0);
		button.setOnMouseClicked(event -> {
			switch (event.getButton()) {
				case PRIMARY:
					onLeftClick.accept(numbers.get(x, y));
					break;
				case SECONDARY:
					onRightClick.accept(numbers.get(x, y));
					break;
			}
		});

		return button;
	}
}

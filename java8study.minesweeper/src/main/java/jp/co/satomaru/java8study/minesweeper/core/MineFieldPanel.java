package jp.co.satomaru.java8study.minesweeper.core;

import java.util.function.Consumer;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import jp.satomaru.java8study.util.Matrix;
import jp.satomaru.java8study.util.TwoDimensional;
import jp.satomaru.java8study.util.variable.TwoDimensionalVariable;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

/**
 * 地雷原マトリクスのパネルです。
 */
@SuppressWarnings("restriction")
public class MineFieldPanel {

	/** 地雷原マスサイズ */
	private static double MINE_FIELD_SIZE = 24.0;

	/** 地雷原マスへのアクセスを提供します。 */
	@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
	public final class MineFieldAdapter {

		/** 地雷原マスのボタン。 */
		private final Button button;

		/**
		 * 地雷原を設定します。
		 *
		 * @param number 設定する地雷原
		 * @return このオブジェクト自身
		 */
		public MineFieldAdapter setOutputStr(String str) {
			button.setText((str != null) ? str : null);
			return this;
		}

		/**
		 * 非活性状態を設定します。
		 *
		 * @param disable 非活性にする場合はtrue
		 * @return このオブジェクト自身
		 */
		public MineFieldAdapter setDisable(boolean disable) {
			button.setDisable(disable);
			return this;
		}
	}

	/** JavaFXグリッドペイン。 */
	private final GridPane grid = new GridPane();

	/** 地雷原マトリクス。 */
	private final Matrix<MineFieldAdapter> mineField;


	/**
	 * コンストラクタ。
	 *
	 * @param width 地雷原マトリクスの幅
	 * @param height 地雷原マトリクスの高さ
	 * @param onLeftClick 地雷原マトリクスが左クリックされた時の処理
	 * @param onRightClick 地雷原マトリクスが右クリックされた時の処理
	 */
	public MineFieldPanel(
			int width,
			int height,
			Consumer<TwoDimensionalVariable<MineFieldAdapter>> onLeftClick,
			Consumer<TwoDimensionalVariable<MineFieldAdapter>> onRightClick) {

		grid.setHgap(2.0);
		grid.setVgap(2.0);

		mineField = new Matrix<>(width, height, (x, y) -> new MineFieldAdapter(createMineFieldButton(x, y, onLeftClick, onRightClick)));
		mineField.flat().forEach(item -> setGridNode(item.getValue().button, item.getX(), item.getY()));
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
	 * 指定されたオブジェクトと2次元座標が等しい地雷原マスを取得します。
	 *
	 * @param twoDimensional 2次元座標を持つオブジェクト
	 * @return 地雷原マス
	 */
	public TwoDimensionalVariable<MineFieldAdapter> getNumberNode(TwoDimensional twoDimensional) {
		return mineField.get(twoDimensional);
	}

	/**
	 * 地雷原マトリクスの非活性状態を設定します。
	 *
	 * @param disable 非活性にする場合はtrue
	 * @return このオブジェクト自身
	 */
	public MineFieldPanel setOutputStrDisable(boolean disable) {
		mineField.flat().map(TwoDimensionalVariable::getValue).forEach(adapter -> adapter.setDisable(disable));
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
	 * 地雷原マスのJavaFXボタンを作成します。
	 *
	 * @param x X座標
	 * @param y Y座標
	 * @param onLeftClick 地雷原マトリクスが左クリックされた時の処理
	 * @param onRightClick 地雷原マトリクスが右クリックされた時の処理
	 * @return 作成したJavaFXボタン
	 */
	@SuppressWarnings("incomplete-switch")
	private Button createMineFieldButton(
			int x,
			int y,
			Consumer<TwoDimensionalVariable<MineFieldAdapter>> onLeftClick,
			Consumer<TwoDimensionalVariable<MineFieldAdapter>> onRightClick) {

		Button button = new Button();
		button.setPadding(new Insets(0.0));
		button.setFont(Font.font("Arial", MINE_FIELD_SIZE/2));
		button.setMinSize(MINE_FIELD_SIZE, MINE_FIELD_SIZE);
		button.setMaxSize(MINE_FIELD_SIZE, MINE_FIELD_SIZE);
		button.setOnMouseClicked(event -> {
			switch (event.getButton()) {
				case PRIMARY:
					onLeftClick.accept(mineField.get(x, y));
					break;
				case SECONDARY:
					onRightClick.accept(mineField.get(x, y));
					break;
			}
		});

		return button;
	}
}

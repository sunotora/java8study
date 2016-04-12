package jp.co.satomaru.java8study.minesweeper;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import jp.co.satomaru.java8study.minesweeper.core.MineFieldPanel;
import jp.co.satomaru.java8study.minesweeper.core.MineFieldPanel.MineFieldAdapter;
import jp.co.satomaru.java8study.minesweeper.core.ToolPanel;
import jp.co.satomaru.java8study.minesweeper.util.MINE_FIELD;
import jp.co.satomaru.java8study.minesweeper.util.MINE_FIELD_STATE;
import jp.satomaru.java8study.util.Lottery;
import jp.satomaru.java8study.util.Matrix;
import jp.satomaru.java8study.util.variable.TwoDimensionalVariable;

/**
 * 地雷原から地雷を取り除くゲームです。
 *
 * <p>
 * いずれかの地雷原マトリクスをオープン（左クリック）するとゲームが開始状態となります。、
 * オープンした地雷原マトリクスには、数字が記載されており、周囲８マスに何個地雷が配置されているかを表しています。
 * 地雷の置かれていないマスを全て開ければ勝ち、いずれかの地雷をオープンすると負けです。
 * </p>
 *
 * <p>
 * オープンされていない地雷原マトリクスを右クリックすると旗を立てることができます。
 * 旗が立っている地雷原マトリクスを右クリックすると旗を消すことができます。
 * 旗が立っている地雷原マトリクスはオープンできません。
 * //TODO "?"の状態を実装するかは未定です。
 * </p>
 *
 * <p>
 * 最初からやり直す時はリセットボタンを押下してください。
 * </p>
 */
@SuppressWarnings("restriction")
public class MineSweeper extends Application {

	/** タイトル。 */
	private static final String TITLE = "MineSweeper";

	/** 地雷原マトリクスの幅。 */
	private static final int MATRIX_WIDTH = 9;

	/** 地雷原マトリクスの高さ。 */
	private static final int MATRIX_HEIGHT = 9;

	/** 地雷の数 */
	private static final int MAX_MINES = 10;

	/**
	 * メイン関数。
	 *
	 * @param args 使用しません
	 */
	public static void main(String... args) {
		launch(args);
	}

	/** 地雷原マトリクスのパネル */
	private MineFieldPanel mineFieldPanel;

	/** ツール群のパネル。 */
	private ToolPanel toolPanel;

	/** 地雷原マトリクス。 */
	private Matrix<String> mineFieldMatrix = new Matrix<String>(MATRIX_WIDTH, MATRIX_HEIGHT, MINE_FIELD.ZERO.toString());

	/** 地雷原状態マトリクス。 */
	private Matrix<String> mineFieldStateMatrix = new Matrix<String>(MATRIX_WIDTH, MATRIX_HEIGHT, MINE_FIELD_STATE.CLOSE.toString());

	/**
	 * JavaFXアプリケーションを起動します。
	 *
	 * <p>
	 * 地雷原マトリクスのパネル、およびツール群のパネルを生成し、プライマリ・ステージに表示します。
	 * </p>
	 *
	 * @param stage プライマリ・ステージ
	 * @throws Exception JavaFXアプリケーションの起動に失敗した場合
	 */
	@Override
	public void start(Stage stage) throws Exception {
		mineFieldPanel = new MineFieldPanel(MATRIX_WIDTH, MATRIX_WIDTH, this::onNumberLeftClick, this::onNumberRightClick);
		toolPanel = new ToolPanel(this::onReset, MAX_MINES);

		onReset();

		BorderPane pane = new BorderPane();
		pane.setPadding(new Insets(10.0 ,0.0 ,0.0 ,10.0));
		pane.setTop(toolPanel.getPane());
		pane.setCenter(mineFieldPanel.getPane());

		stage.setTitle(TITLE);
		stage.setFullScreen(false);
		stage.setResizable(false);
		stage.setScene(new Scene(pane));
		stage.show();
	}

	/**
	 * アラート・ダイアログを表示します。
	 *
	 * @param type アラートタイプ
	 * @param headerText ヘッダーメッセージ（概要メッセージ）
	 * @param contentText 内容メッセージ（詳細メッセージ）
	 */
	private void alert(AlertType type, String headerText, String contentText) {
		Alert alert = new Alert(type);
		alert.setTitle(TITLE);
		alert.getDialogPane().setHeaderText(headerText);
		alert.getDialogPane().setContentText(contentText);
		alert.show();
	}

	/**
	 * 地雷原マトリクスのパネルが左クリックされた時の処理を行います。
	 *
	 * <ol>
	 * <li>初めてマスが押下された場合は、地雷原マトリクスの初期化（押下されたマスに地雷は置かない）を行い、ゲームを開始状態とします。</li>
	 * <li>既にオープン状態になっている、または"旗"がセットされているマスは何もせず処理を終了します。</li>
	 * <li>オープン対象のマスに地雷が存在する場合は、ユーザの負けでゲームを終了します。（「地雷を踏んでしまいました・・・」という警告をアラート・ダイアログに表示します。）
	 *     その場合、全てのマスを開け、非活性にします。</li>
	 * <li>上記以外の場合は、押下されたマスをオープン状態に変更します。
	 *     オープン状態にしたマスの周囲地雷数が"0"だった場合は、周りのマスも連動してオープン状態になります。
	 *     オープンとなった周りのマスの周囲地雷数も"0"だった場合は、さらに連動して周りのマスをオープン状態にします。</li>
	 * <li>地雷が存在するマス以外のすべてのマスがオープン状態となったら、ユーザの勝ちでゲームを終了します。（「ユーザの勝ちです。」という情報をアラート・ダイアログに表示します。）
	 *     その際、全てのマスを非活性にします。</li>
	 * </ol>
	 *
	 * @param item クリックされた地雷原マス
	 */
	private void onNumberLeftClick(TwoDimensionalVariable<MineFieldAdapter> item) {

		if (isNotStarted()) {
			// 初期処理
			initMineField(item);
		}

		// 既にオープンになっている、または旗がセットされているマスは何もせずに処理を終了する
		String currentState = mineFieldStateMatrix.get(item).getValue();
		if (currentState.equals(MINE_FIELD_STATE.OPEN.toString())
				|| currentState.equals(MINE_FIELD_STATE.FLAG.toString())) {
			return;
		}

		String currentMass = mineFieldMatrix.get(item).getValue();

		// 地雷を踏んだら終わり。
		if (currentMass.equals(MINE_FIELD.MINE.toString())) {
			alert(AlertType.WARNING, "地雷を踏んでしまいました・・・", "GAME OVER!!!");

			// 全マスを非活性に変更
			mineFieldPanel.setMineFieldDisable(true);

			// 地雷・周囲地雷数を表示
			mineFieldMatrix.flat()
				.forEach(elem -> mineFieldPanel.getMineFieldNode(elem).getValue().setValue(elem.getValue()));

			return;
		}

		// 周囲地雷数を取得
		String arroundRemaiMines = mineFieldMatrix.get(item).getValue();
		// マスに設定
		mineFieldPanel.getMineFieldNode(item).getValue().setValue(arroundRemaiMines);

		// 0だったら回りも押下。（こなへんどうやってやろうかなぁ）
		// TODO

		// 押下したマスを非活性に変更
		item.getValue().setDisable(true);

		// 押下したマスの状態マトリックスをオープン状態に変更
		mineFieldStateMatrix.get(item).setValue(MINE_FIELD_STATE.OPEN.toString());

		// 終了判定（地雷じゃない全てのマスがオープンだったら終了）
		if (mineFieldMatrix.flat()
			.filter(elem -> !elem.getValue().equals(MINE_FIELD.MINE.toString()))
			.allMatch(elem -> mineFieldStateMatrix.get(elem).getValue().equals(MINE_FIELD_STATE.OPEN.toString()))) {

			// 全マスを非活性に変更
			mineFieldPanel.setMineFieldDisable(true);

			alert(AlertType.INFORMATION, "ユーザの勝ちです。", "GAME CLEAR!!!");
		}
	}

	/**
	 * 地雷原マトリックスの初期化
	 * @param targetItem
	 */
	private void initMineField(TwoDimensionalVariable<MineFieldAdapter> targetItem) {

		// 数値を生成
		Lottery<Integer> correctInitLottery = Lottery.ofInteger(1, MATRIX_WIDTH * MATRIX_HEIGHT);

		// 地雷原マトリックスに地雷を配置（押下マス以外）
		mineFieldMatrix.flat()
			.filter(item -> targetItem.isNotSamePosition(item))
			.forEach(item -> item.setValue(convertMine(correctInitLottery.draw().get())));

		// 周囲地雷数を更新（押下マス含む、地雷除く）
		mineFieldMatrix.flat()
			.filter(item -> !item.getValue().equals(MINE_FIELD.MINE.toString()))
			.forEach(item -> item.setValue(MINE_FIELD.valueOf(
												mineFieldMatrix
												.arround(item)
												.filter(item2 -> item2.getValue().equals(MINE_FIELD.MINE.toString()))
												.count()))
					);

////		テスト表示用
//		mineFieldMatrix.flat()
//			.forEach(item -> mineFieldPanel.getMineFieldNode(item).getValue().setValue(item.getValue()));
	}

	private String convertMine(Integer num) {
		return num <= MAX_MINES ? MINE_FIELD.MINE.toString() : MINE_FIELD.ZERO.toString();
	}

	/**
	 * 地雷原マトリクスのパネルが右クリックされた時の処理を行います。
	 *
	 * <ol>
	 * <li>ゲームが開始状態になっていない場合は何もせずに処理を終了します。</li>
	 * <li>既にオープン状態になっているマスは何もせずに処理を終了します。</li>
	 * <li>オープンでない場合、以下の処理を行います。
	 *      <ol>
	 *      <li>何もなし、かつ残地雷数が0の場合
	 *          旗に変更、残地雷数を１つ減らす</li>
	 *      <li>旗の場合
	 *          何もなしに変更、残地雷数を１つ増やす</li>
	 *      </ol>
	 * </li>
	 * </ol>
	 *
	 * @param item クリックされた数字マス
	 */
	private void onNumberRightClick(TwoDimensionalVariable<MineFieldAdapter> item) {

		if (isNotStarted()) return;

		TwoDimensionalVariable<String> currentMass = mineFieldStateMatrix.get(item);

		if (currentMass.getValue().equals(MINE_FIELD_STATE.OPEN.toString())) {
			// 既にオープンは何もしない
			return;
		} else if (currentMass.getValue().equals(MINE_FIELD_STATE.CLOSE.toString())){
			// 空いてない場合は旗を立てる
			currentMass.setValue(MINE_FIELD_STATE.FLAG.toString());
			mineFieldPanel.getMineFieldNode(item).getValue().setValue(MINE_FIELD_STATE.FLAG.toString());
		} else if (currentMass.getValue().equals(MINE_FIELD_STATE.FLAG.toString())){
			// 旗が立っている場合は旗を下げる
			currentMass.setValue(MINE_FIELD_STATE.CLOSE.toString());
			mineFieldPanel.getMineFieldNode(item).getValue().setValue(MINE_FIELD_STATE.CLOSE.toString());
		}
	}

	/**
	 * リセットボタン押下時の処理を行います。
	 *
	 * <ol>
	 * <li>地雷原マトリクスのパネルをすべて非オープン状態・活性に戻します。</li>
	 * <li>残地雷数をMAX_MINESに戻します。</li>
	 * <li>// TODO TIMEを初期化します。</li>
	 * </ol>
	 */
	private void onReset() {

		// 地雷原マトリックスのパネルを全て活性化
		mineFieldPanel.setMineFieldDisable(false);

		mineFieldMatrix.flat()
			.forEach(item -> {
				// パネルの初期化
				mineFieldPanel.getMineFieldNode(item).getValue().setValue(MINE_FIELD_STATE.CLOSE.toString());
				// 状態マトリックスの初期化
				mineFieldStateMatrix.get(item).setValue(MINE_FIELD_STATE.CLOSE.toString());
			});

		// 残地雷数を初期化
		toolPanel.setRemainLandMinesText(MAX_MINES);
	}

	private boolean isStarted() {
		return !isNotStarted();
	}

	private boolean isNotStarted() {
		return mineFieldStateMatrix.flat().allMatch(elem -> elem.getValue().equals(MINE_FIELD_STATE.CLOSE.toString()));
	}
}

package jp.satomaru.java8study.mastermind;

import java.util.TreeSet;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import jp.satomaru.java8study.mastermind.core.ButtonPanel;
import jp.satomaru.java8study.mastermind.core.NumberPanel;
import jp.satomaru.java8study.mastermind.core.NumberPanel.NumberAdapter;
import jp.satomaru.java8study.util.IntegerLine;
import jp.satomaru.java8study.util.IntegerMatrix;
import jp.satomaru.java8study.util.Lottery;
import jp.satomaru.java8study.util.MatrixBase;
import jp.satomaru.java8study.util.variable.TwoDimensionalInteger;
import jp.satomaru.java8study.util.variable.TwoDimensionalVariable;
import jp.satomaru.java8study.util.variable.VariableBase;

/**
 * ヒントを元に、3x3マスの数字を当てるゲームです。
 *
 * <p>
 * 数字マトリクスを全て数字で埋めて決定ボタンを押下すると、
 * 数字マトリクスの各列・各行に、「H:ヒット」「B:ブロー」というヒントが表示されます。
 * ヒットは「アタリ」という意味です。ブローは「ハズレだけど、その列（または行）の違うマスに数字を移動するとアタリになる」という意味です。
 * </p>
 *
 * <p>
 * 数字を移動する時は、移動元および移動先のマスを右クリックして、マスの数字を消します。
 * その後、マスを左クリックすると、現在使用されていない数字が、クリック毎に順番に表示されます。
 * </p>
 *
 * <p>
 * 降参する時は降参ボタン、最初からやり直す時はリセットボタンを押下してください。
 * </p>
 */
public class MasterMind extends Application {

	/** タイトル。 */
	private static final String TITLE = "MasterMind";

	/** 数字マトリクスの幅。 */
	private static final int MATRIX_WIDTH = 3;

	/** 数字マトリクスの高さ。 */
	private static final int MATRIX_HEIGHT = 3;

	/**
	 * メイン関数。
	 *
	 * @param args 使用しません
	 */
	public static void main(String... args) {
		launch(args);
	}

	/** 数字マトリクスのパネル。 */
	private NumberPanel numberPanel;

	/** ボタン群のパネル。 */
	private ButtonPanel buttonPanel;

	/** 正解。 */
	private MatrixBase<Integer, TwoDimensionalInteger> correctMatrix = new IntegerMatrix(MATRIX_WIDTH, MATRIX_HEIGHT);

	/** 解答。 */
	private MatrixBase<Integer, TwoDimensionalInteger> answerMatrix = new IntegerMatrix(MATRIX_WIDTH, MATRIX_HEIGHT);

	/** 現在使用されていない数字。 */
	private TreeSet<Integer> remains = new TreeSet<Integer>();

	/**
	 * JavaFXアプリケーションを起動します。
	 *
	 * <p>
	 * 数字マトリクスのパネル、およびボタン群のパネルを生成し、プライマリ・ステージに表示します。
	 * </p>
	 *
	 * @param stage プライマリ・ステージ
	 * @throws Exception JavaFXアプリケーションの起動に失敗した場合
	 */
	@Override
	public void start(Stage stage) throws Exception {
		numberPanel = new NumberPanel(MATRIX_WIDTH, MATRIX_WIDTH, this::onNumberLeftClick, this::onNumberRightClick);
		buttonPanel = new ButtonPanel(this::onDecide, this::onGiveup, this::onReset);

		onReset();

		BorderPane pane = new BorderPane();
		pane.setPadding(new Insets(8.0));
		pane.setCenter(numberPanel.getPane());
		pane.setRight(buttonPanel.getPane());

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
	 * 決定ボタン押下時の処理を行います。
	 *
	 * <ol>
	 * <li>解答が不完全である場合は、「空欄のままの数字があります。」「全ての数字を設定してください。」という警告をアラート・ダイアログに表示して、処理を中断します。</li>
	 * <li>解答と正解を比較して各ヒットおよび各ブローを割り出し、数字マトリクスのパネルに表示します。</LI>
	 * <li>解答が全て正解と一致している場合は、「おめでとうございます。」「全問正解です。」という情報をアラート・ダイアログに表示して、数字マトリクス、決定ボタン、降参ボタンを非活性にします。</li>
	 * </ol>
	 */
	private void onDecide() {

		if (answerMatrix.flat().anyMatch(VariableBase::isNull)) {
			// 解答不完全
			alert(AlertType.WARNING, "空欄のままの数字があります。", "全ての数字を設定してください。");
			return;
		}


		// HITの算出
		IntegerLine hHits = new IntegerLine(MATRIX_HEIGHT, 0);
		IntegerLine vHits = new IntegerLine(MATRIX_WIDTH, 0);

		answerMatrix.flat()
			.filter(answer -> correctMatrix.get(answer).isSameValue(answer))   // 座標と正解が一致するものを選択
			.forEach(answer -> {
				hHits.get(answer.getY()).increment();                          // 該当箇所をインクリメント
				vHits.get(answer.getX()).increment();
			});

		// HITの設定
		hHits.stream().forEach(item -> numberPanel.setRowHit(item.getIndex(), item.getValue()));
		vHits.stream().forEach(item -> numberPanel.setColHit(item.getIndex(), item.getValue()));

		// BLOWの算出
		IntegerLine hBlows = new IntegerLine(MATRIX_HEIGHT, 0);
		IntegerLine vBlows = new IntegerLine(MATRIX_WIDTH, 0);

		answerMatrix.flat()
			.forEach(answer -> {
				correctMatrix.row(answer.getY())
					.filter(hoge -> answer.isNotSamePosition(hoge) && answer.isSameValue(hoge)) // 座標が異なり、正解が一致する縦列を選択
					.forEach(piyo -> hBlows.get(piyo.getY()).increment());                      // 該当箇所をインクリメント

				correctMatrix.col(answer.getX())
					.filter(hoge -> answer.isNotSamePosition(hoge) && answer.isSameValue(hoge)) // 座標が異なり、正解が一致する横列を選択
					.forEach(piyo -> vBlows.get(piyo.getX()).increment());                      // 該当箇所をインクリメント
			});

		// BLOWの設定
		hBlows.stream().forEach(item -> numberPanel.setRowBlow(item.getIndex(), item.getValue()));
		vBlows.stream().forEach(item -> numberPanel.setColBlow(item.getIndex(), item.getValue()));

		// 全問正解
		if (hHits.values().allMatch(value -> value == MATRIX_WIDTH)) {
			// 全問正解
			alert(AlertType.INFORMATION, "おめでとうございます。", "全問正解です。");
			numberPanel.setNumberDisable(true);
			buttonPanel.setDecideDisable(true);
			buttonPanel.setGiveupDisable(true);
		}
	}

	/**
	 * 降参ボタン押下時の処理を行います。
	 *
	 * <ol>
	 * <li>数字マトリクスに正解を表示して、非活性にします。</li>
	 * <li>数字マトリクス、決定ボタン、降参ボタンを非活性にします。</li>
	 * </ol>
	 */
	private void onGiveup() {

		// 正解マトリクスをGUIに表示
		correctMatrix.flat().forEach(item -> numberPanel.getNumberNode(item).getValue().setNumber(item.getValue()));

		// 数字マトリクス、決定ボタン、降参ボタンを非活性に変更
		numberPanel.setNumberDisable(true);
		buttonPanel.setDecideDisable(true);
		buttonPanel.setGiveupDisable(true);
	}

	/**
	 * リセットボタン押下時の処理を行います。
	 *
	 * <ol>
	 * <li>正解をランダムで生成します。</li>
	 * <li>解答に、順番に数字を埋めます。</li>
	 * <li>数字マトリクスに解答を表示して、活性にします。</li>
	 * <li>全てのヒット表示およびブロー表示を空欄します。</li>
	 * <li>決定ボタン、降参ボタンを活性にします。</li>
	 * <li>現在使用されていない数字をリセットします。</li>
	 * </ol>
	 */
	private void onReset() {

		// 正解用数値を生成
		Lottery<Integer> correctInitLottery = Lottery.ofInteger(1, MATRIX_WIDTH * MATRIX_HEIGHT);

		// 正解マトリックスを作成、現在使用されていない数字のリセット
		correctMatrix.flat().forEach(item -> item.setValue(correctInitLottery.draw().get()));

		// 解答用数値を生成
		Lottery<Integer> answerInitLottery = Lottery.ofInteger(1, MATRIX_WIDTH * MATRIX_HEIGHT);

		// 解答に順番に数字を埋める、GUI表示
		answerMatrix.flat()
			.map(item -> item.setValue(answerInitLottery.draw().get()))
			.forEach(item -> numberPanel.getNumberNode(item).getValue().setNumber(item.getValue()));

		// 現在使用されていない数字をリセット
		remains.clear();

		// 全てのヒット表示およびブロー表示を空欄に変更
		numberPanel.clearAllHitsAndBlows();

		// 数字マトリックス、決定ボタン、降参ボタンを活性化
		numberPanel.setNumberDisable(false);
		buttonPanel.setDecideDisable(false);
		buttonPanel.setGiveupDisable(false);
	}

	/**
	 * 数字マトリクスが左クリックされた時の処理を行います。
	 *
	 * <ol>
	 * <li>現在使用されていない数字が存在しない場合は、処理を中断します。</li>
	 * <li>現在使用されていない数字の中から、クリックされた数字マスの解答よりも大きく、かつ最も近い数字を、解答に設定します。
	 * ただし、現在使用されていない数字の中に現在の解答よりも大きい数字が存在しない場合、または解答に数字が設定されていない場合は、
	 * 現在使用されていない数字の中から一番小さい数字を解答に設定します。</li>
	 * <li>解答に数字が設定されていた場合は、その数字を現在使用されていない数字に追加します。</li>
	 * <li>現在使用されていない数字から、解答に設定した数字を削除します。</li>
	 * <li>数字マスに新しい解答を表示します。</li>
	 * </ol>
	 *
	 * @param item クリックされた数字マス
	 */
	private void onNumberLeftClick(TwoDimensionalVariable<NumberAdapter> item) {

		if (remains.isEmpty()) {
			// remainsが空の時は何もしない
			return;
		}

		// 現在設定されている値を取得
		TwoDimensionalInteger currentAnswer = answerMatrix.get(item);

		Integer nextInteger = null;

		if (!currentAnswer.isNull()) {
			// 現在の解答が設定されていた場合、未使用数字へ追加・より大きい値を取得
			remains.add(currentAnswer.getValue());
			nextInteger = remains.higher(currentAnswer.getValue());
		}

		if (nextInteger == null) {
			// 次の設定値が取得できていない場合は、未使用数字の先頭を取得
			nextInteger = remains.first();
		}

		// 解答マトリックスとGUIに設定
		currentAnswer.setValue(nextInteger);
		item.getValue().setNumber(nextInteger);
		// 未使用数字から設定値をクリア
		remains.remove(nextInteger);
	}

	/**
	 * 数字マトリクスが右クリックされた時の処理を行います。
	 *
	 * <ol>
	 * <li>クリックされた数字マスの解答が設定されていない場合は、処理を中断します。</li>
	 * <li>クリックされた数字マスの表示を空欄にします。</li>
	 * <li>現在使用されていない数字に、クリックされた数字マスの解答を追加します。</li>
	 * <li>クリックされた数字マスの解答を消去します。</li>
	 * </ol>
	 *
	 * @param item クリックされた数字マス
	 */
	private void onNumberRightClick(TwoDimensionalVariable<NumberAdapter> item) {

		TwoDimensionalInteger selectedItem = answerMatrix.get(item);

		if (!selectedItem.isNull()) {

			// 現在値を未使用数字へ
			remains.add(selectedItem.getValue());

			// 正解マトリックスとGUIをクリア
			selectedItem.clear();
			item.getValue().setNumber(null);
		}
	}
}

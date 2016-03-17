package jp.satomaru.java8study.util.launcher;

import java.math.BigDecimal;

public class LauncherTest {

	public static class Model {

		public void add(Request request) {
			System.out.println(getFirst(request).add(getSecond(request)));
		}

		public void sub(Request request) {
			System.out.println(getFirst(request).subtract(getSecond(request)));
		}

		private BigDecimal getFirst(Request request) {
			return Validator.of("first")
				.whenNoSuchElement(name -> "数値は2つ指定してください。")
				.defaultMessage(name -> "第2引数には数値を指定してください。")
				.test(() -> request.get(BigDecimal.class, 0).get());
		}

		private BigDecimal getSecond(Request request) {
			return Validator.of("second")
				.whenNoSuchElement(name -> "数値は2つ指定してください。")
				.defaultMessage(name -> "第3引数には数値を指定してください。")
				.test(() -> request.get(BigDecimal.class, 1).get());
		}
	}

	public static void main(String[] args) {
		Launcher.of(Model.class)
			.setCommand("add", Model::add)
			.setCommand("sub", Model::sub)
			.ready()
			.launch(new Model(), new StringsRequest(args));
	}
}

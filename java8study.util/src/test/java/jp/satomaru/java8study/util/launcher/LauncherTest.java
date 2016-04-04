package jp.satomaru.java8study.util.launcher;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.stream.Stream;

import jp.satomaru.java8study.util.Numbers;
import jp.satomaru.java8study.util.Strings;
import jp.satomaru.java8study.util.launcher.message.Message;
import jp.satomaru.java8study.util.launcher.message.StringsRequest;
import jp.satomaru.java8study.util.launcher.message.SysoutResponse;
import jp.satomaru.java8study.util.launcher.validate.InvalidArgument;
import jp.satomaru.java8study.util.launcher.validate.InvalidParameter;
import jp.satomaru.java8study.util.launcher.validate.UnitValidator;
import jp.satomaru.java8study.util.launcher.validate.Validator;

public class LauncherTest {

	public static class Model {

		public void add(Message message) {
			BigDecimal first = validateFirst(message).end();
			BigDecimal second = validateSecond(message).end();
			BigDecimal result = first.add(second);
			message.outputAndClose(result);
		}

		public void sub(Message message) {
			BigDecimal first = validateFirst(message).end();
			BigDecimal second = validateSecond(message).end();
			BigDecimal result = first.subtract(second);
			message.outputAndClose(result);
		}

		public void mul(Message message) {
			BigDecimal first = validateFirst(message).end();
			BigDecimal second = validateSecond(message).end();
			BigDecimal result = first.multiply(second);
			message.outputAndClose(result);
		}

		public void div(Message message) {
			BigDecimal first = validateFirst(message).end();
			BigDecimal second = validateSecond(message)
					.when(Numbers::isZero, () -> "除算の場合、数値2に0を指定することはできません。")
					.end();

			BigDecimal result = first.divide(second, 16, RoundingMode.HALF_UP).stripTrailingZeros();
			String text = Strings.rightSuppress(result.toPlainString(), "0");
			message.outputAndClose(text);
		}

		public void whenNoCommand(Message message) {
			message.outputAndClose("引数を指定してください。");
		}

		public void whenIllegalCommand(Message message, String command) {
			message.outputAndClose(String.format("演算の指定が誤っています:%s", command));
		}

		public void whenInvalidArgument(Message message, InvalidArgument invalid) {
			message.errorAndClose(invalid);
		}

		public void whenInvalidParameter(Message message, InvalidParameter invalid) {
			message.errorAndClose(invalid);
		}

		public void whenError(Message message) {
			message.response(response ->
				Stream.of(
						"",
						"電卓",
						"　　引数: <演算> <数値1> <数値2>",
						"　　　　演算:",
						"　　　　　　add - 加算",
						"　　　　　　sub - 減算",
						"　　　　　　mul - 乗算",
						"　　　　　　div - 除算")
					.forEach(response::output));
		}

		private UnitValidator<Integer, BigDecimal> validateFirst(Message message) {
			return Validator.forArgument(0, BigDecimal.class)
				.whenOthers(() -> "数値1を正しく指定してください。")
				.get(message::get)
				.exists(() -> "数値は2つ指定してください。");
		}

		private UnitValidator<Integer, BigDecimal> validateSecond(Message message) {
			return Validator.forArgument(1, BigDecimal.class)
				.whenOthers(() -> "数値2を正しく指定してください。")
				.get(message::get)
				.exists(() -> "数値は2つ指定してください。");
		}
	}

	private static Launcher<Model> launcher = Launcher.of(Model.class)
		.setCommand("add", Model::add)
		.setCommand("sub", Model::sub)
		.setCommand("mul", Model::mul)
		.setCommand("div", Model::div)
		.whenNoCommand(Model::whenNoCommand)
		.whenIllegalCommand(Model::whenIllegalCommand)
		.whenInvalidArgument(Model::whenInvalidArgument)
		.whenInvalidParameter(Model::whenInvalidParameter)
		.whenError(Model::whenError)
		.ready();

	public static void main(String[] args) {
		launcher.launch(new Model(), new StringsRequest(args), new SysoutResponse());
	}
}

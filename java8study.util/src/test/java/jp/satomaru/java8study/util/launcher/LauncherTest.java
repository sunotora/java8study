package jp.satomaru.java8study.util.launcher;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.stream.Stream;

import jp.satomaru.java8study.util.Numbers;
import jp.satomaru.java8study.util.launcher.message.Message;
import jp.satomaru.java8study.util.launcher.message.StringsRequest;
import jp.satomaru.java8study.util.launcher.message.SysoutResponse;
import jp.satomaru.java8study.util.launcher.validate.InvalidArgument;
import jp.satomaru.java8study.util.launcher.validate.InvalidParameter;
import jp.satomaru.java8study.util.launcher.validate.UnitValidator;
import jp.satomaru.java8study.util.launcher.validate.Validator;

public class LauncherTest {

	public interface Model {
		void add(Message message);
		void sub(Message message);
		void mul(Message message);
		void div(Message message);
	}

	public static class ModelImpl implements Model, ErrorHandler {

		@Override
		public void add(Message message) {
			BigDecimal first = validateFirst(message).end();
			BigDecimal second = validateSecond(message).end();
			BigDecimal result = first.add(second);
			message.output(result);
		}

		@Override
		public void sub(Message message) {
			BigDecimal first = validateFirst(message).end();
			BigDecimal second = validateSecond(message).end();
			BigDecimal result = first.subtract(second);
			message.output(result);
		}

		@Override
		public void mul(Message message) {
			BigDecimal first = validateFirst(message).end();
			BigDecimal second = validateSecond(message).end();
			BigDecimal result = first.multiply(second);
			message.output(result);
		}

		@Override
		public void div(Message message) {
			BigDecimal first = validateFirst(message).end();
			BigDecimal second = validateSecond(message)
					.when(Numbers::isZero, () -> "除算の場合、数値2に0を指定することはできません。")
					.end();

			BigDecimal result = first.divide(second, 16, RoundingMode.HALF_UP).stripTrailingZeros();
			message.output(result);
		}

		@Override
		public void whenNoCommand(Message message) {
			message.output("引数を指定してください。");
		}

		@Override
		public void whenIllegalCommand(Message message, String command) {
			message.output(String.format("演算の指定が誤っています:%s", command));
		}

		@Override
		public void whenInvalidArgument(Message message, InvalidArgument invalid) {
			message.error(invalid);
		}

		@Override
		public void whenInvalidParameter(Message message, InvalidParameter invalid) {
			message.error(invalid);
		}

		@Override
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

	private static final Launcher<Model> launcher = Launcher.of(Model.class)
		.setCommand("add", Model::add)
		.setCommand("sub", Model::sub)
		.setCommand("mul", Model::mul)
		.setCommand("div", Model::div)
		.ready();

	public static void main(String[] args) {
		try {
			launcher.launch(new ModelImpl(), new StringsRequest(args), new SysoutResponse());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

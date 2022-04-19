package protocol;

import root.command.Command;
import root.communication.Translator;
import root.communication.parser.DataParser;
import root.model.event.ModelEventArg;

public class NamedWrapperTranslator implements Translator {

	private NameTypeResolver typeResolver;
	private DataParser parser;

	public NamedWrapperTranslator(
			DataParser parser,
			NameTypeResolver typeResolver) {

		this.parser = parser;
		this.typeResolver = typeResolver;

	}

	@Override
	public String toStrData(Command command) {
		var wrapper = new NamedWrapper(
				command.getName(),
				parser.ToString(command));

		return parser.ToString(wrapper);
	}

	@Override
	public byte[] toByteData(Command command) {
		return this.toStrData(command).getBytes();
	}

	@Override
	public Command toCommand(String strData) {
		var wrappedData = parser.FromString(strData, NamedWrapper.class);
		var commandType = typeResolver.resolve(wrappedData.name);
		if (commandType == null) {
			// TODO I guess this should be handled somehow
			// some exception maybe ...
			// debug
			System.out.println("Unknown command type received: " + wrappedData.name);
			return null;
		}

		return (Command) parser.FromString(wrappedData.payload, commandType);
	}

	@Override
	public Command toCommand(byte[] byteData) {
		return this.toCommand(new String(byteData));
	}

	@Override
	public String toStrData(ModelEventArg eventArg) {
		var wrapper = new NamedWrapper(
				eventArg.getEventName(),
				parser.ToString(eventArg));

		return parser.ToString(wrapper);
	}

	@Override
	public byte[] toByteData(ModelEventArg eventArg) {
		return this.toStrData(eventArg).getBytes();
	}

	@Override
	public ModelEventArg toModelEventArg(String strData) {
		var wrappedData = parser.FromString(strData, NamedWrapper.class);
		var eventArgType = typeResolver.resolve(wrappedData.name);

		return (ModelEventArg) parser.FromString(wrappedData.payload, eventArgType);
	}

	@Override
	public ModelEventArg toModelEventArg(byte[] byteData) {
		return this.toModelEventArg(new String(byteData));
	}

}

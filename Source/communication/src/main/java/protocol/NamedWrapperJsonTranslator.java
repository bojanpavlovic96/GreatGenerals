package protocol;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import root.command.Command;
import root.communication.Translator;
import root.communication.parser.DataParser;
import root.model.event.ModelEventArg;

// TODO since parser is external dependency this should me renamed to
// just NamedWrapperTranslator because some other parser (apart from json) 
// could be used

public class NamedWrapperJsonTranslator implements Translator {

	private NameTypeResolver typeResolver;
	// private Map<String, Class<?>> typeMap;
	private DataParser parser;

	public NamedWrapperJsonTranslator(
			DataParser parser,
			NameTypeResolver typeResolver
	// List<NameTypeMapping> typeMappings
	) {

		this.parser = parser;
		this.typeResolver = typeResolver;

		// this.typeMap = new HashMap<String, Class<?>>();
		// for (var mapping : typeMappings) {
		// this.typeMap.put(mapping.name, mapping.type);
		// }

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
		// var commandType = typeMap.get(wrappedData.name);
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
		// var eventArgType = typeMap.get(wrappedData.name);
		var eventArgType = typeResolver.resolve(wrappedData.name);

		return (ModelEventArg) parser.FromString(wrappedData.payload, eventArgType);
	}

	@Override
	public ModelEventArg toModelEventArg(byte[] byteData) {
		return this.toModelEventArg(new String(byteData));
	}

}

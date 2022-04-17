package root.communication;

import root.command.Command;
import root.model.event.ModelEventArg;

public interface Translator {

	// command translation

	String toStrData(Command command);

	byte[] toByteData(Command command);

	Command toCommand(String strData);

	Command toCommand(byte[] byteData);

	// model event translation

	String toStrData(ModelEventArg eventArg);

	byte[] toByteData(ModelEventArg eventArg);

	ModelEventArg toModelEventArg(String strData);

	ModelEventArg toModelEventArg(byte[] byteData);

}

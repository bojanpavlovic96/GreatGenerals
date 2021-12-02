package root.controller;

import root.command.Command;

public interface CommandStack {

	void push(Command newCommand);

	Command pop();

	Command get(int ind);

	Command remove(int ind);

	boolean isEmpty();

}

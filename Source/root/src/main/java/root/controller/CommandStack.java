package root.controller;

import java.util.function.Function;

import root.command.Command;

public interface CommandStack {

	void push(Command newCommand);

	Command pop();

	Command get(int ind);

	Command remove(int ind);

	boolean isEmpty();

	boolean removeFirstMatch(Function<Command, Boolean> matchMethod);

}

package controller.command;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import root.command.Command;
import root.controller.CommandStack;

public class UndoStack implements CommandStack {

	private List<Command> stack;

	public UndoStack() {
		this.stack = new ArrayList<Command>();
	}

	@Override
	public void push(Command newCommand) {
		this.stack.add(newCommand);
	}

	@Override
	public Command pop() {
		int count = this.stack.size();

		if (count > 0) {
			return this.stack.remove(count - 1);
		}

		return null;
	}

	@Override
	public Command get(int ind) {
		if (ind >= 0 && ind < this.stack.size()) {
			return stack.get((stack.size() - 1) - ind);
		}
		return null;
	}

	@Override
	public Command remove(int ind) {
		if (ind >= 0 && ind < this.stack.size()) {
			return stack.remove((stack.size() - 1) - ind);
		}
		return null;
	}

	@Override
	public boolean isEmpty() {
		return (this.stack.size() == 0);
	}

	@Override
	public boolean removeFirstMatch(Function<Command, Boolean> matchMethod) {

		int index = 0;

		Command currentCommand = null;
		while ((currentCommand = this.get(index)) != null) {
			if (matchMethod.apply(currentCommand)) {

				this.remove(index);

				return true;
			}
			index++;
		}

		return false;
	}

	public int getSize() {
		return this.stack.size();
	}

}

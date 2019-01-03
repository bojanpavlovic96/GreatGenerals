package view.command;

import java.util.List;

public class LoadBoardCommand extends ViewCommand {

	private List<Integer> fields;

	public LoadBoardCommand(List<Integer> fields) {

		this.fields = fields;

	}

	public void run() {

	}

}

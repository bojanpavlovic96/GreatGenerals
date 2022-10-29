package controller.command;

import root.command.Command;

public class CtrlServerErrorMessage extends Command {

	private String message;

	public CtrlServerErrorMessage(String message) {
		this.message = message;
	}

	@Override
	public void run() {
		System.out.println("SERVER ERROR COMMAND NOT IMPLEMENTED ... ");
	}

	@Override
	public Command getAntiCommand() {
		return null;
	}

}

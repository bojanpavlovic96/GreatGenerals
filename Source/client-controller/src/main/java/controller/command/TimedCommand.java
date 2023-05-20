package controller.command;

import java.util.Date;

import root.command.Command;

public class TimedCommand extends Command {

	public Command command;
	public Date timestamp;

	public TimedCommand(Command command, Date timestamp) {
		this.command = command;
		this.timestamp = timestamp;
	}

	@Override
	public void run() {
		command.run();
	}

	@Override
	public Command getAntiCommand() {
		return command.getAntiCommand();
	}
}

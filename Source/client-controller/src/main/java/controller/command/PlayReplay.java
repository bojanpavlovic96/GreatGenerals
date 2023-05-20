package controller.command;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import root.command.Command;

public class PlayReplay extends Command {

	private class DelayedCommand {
		public Command command;
		public long delay;

		public DelayedCommand() {
		}

		public DelayedCommand(Command command, long delay) {
			this.command = command;
			this.delay = delay;
		}
	}

	private Date startTimestamp;
	private List<TimedCommand> commands;

	private List<DelayedCommand> processedComms;

	public PlayReplay(List<TimedCommand> commands, Date startTimestamp) {
		this.commands = commands;
		this.startTimestamp = startTimestamp;

		this.processedComms = new ArrayList<DelayedCommand>();

		var initDelay = commands.get(0).timestamp.getTime() - startTimestamp.getTime();
		processedComms.add(new DelayedCommand(commands.get(0), initDelay));

		for (var i = 1; i < commands.size() - 1; i++) {
			var prevC = commands.get(i - 1);
			var delay = commands.get(i).timestamp.getTime() - prevC.timestamp.getTime();
			// var delayedC = new DelayedCommand(commands.get(i), processedComms.get(0).delay + delay);
			var delayedC = new DelayedCommand(commands.get(i), delay);
			processedComms.add(delayedC);
		}
	}

	@Override
	public void run() {
		while (!processedComms.isEmpty()) {
			var curr = processedComms.remove(0);
			try {
				Thread.sleep(curr.delay);
			} catch (InterruptedException e) {
				System.out.println("Exc while waiting on delayed command: " + curr.command.getClass().toString());
				e.printStackTrace();
			}
			curr.command.run();
		}
	}

	@Override
	public Command getAntiCommand() {
		return null;
	}

}

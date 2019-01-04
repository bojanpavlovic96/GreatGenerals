package controller;

import communication.Communicator;
import model.Model;
import view.CommandGenerator;
import view.View;
import view.command.CommandQueue;

public class GameBrain implements Controller {

	private Communicator communicator;

	private View view;
	private CommandQueue view_command_queue;

	private Model model;

	public GameBrain(Communicator communicator, View view, Model model) {

		super();

		this.communicator = communicator;
		this.view = view;
		this.model = model;

		this.view_command_queue = this.view.getCommandQueue();

		// testing

		CommandGenerator commandGenerator = new CommandGenerator(this.view_command_queue);
		commandGenerator.generateCommand();

		// testing

		this.initCommunicatorHandlers();

	}

	private void initCommunicatorHandlers() {

		// set handlers for message

	}

	public Communicator getCommunicator() {
		return this.communicator;
	}

	public void setCommunicator(Communicator communicator) {
		this.communicator = communicator;
	}

	public View getView() {
		return this.view;
	}

	public void setView(View view) {
		this.view = view;
	}

	public Model getModel() {
		return this.model;
	}

	public void setMode(Model model) {
		this.model = model;
	}

}

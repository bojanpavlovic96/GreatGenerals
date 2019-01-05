package controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

import communication.Communicator;
import controller.command.MoveCommand;
import controller.command.UpdateCommandQueue;
import javafx.geometry.Point2D;
import model.Model;
import model.component.Field;
import model.component.GameField;
import model.component.GameUnit;
import model.component.Terrain;
import view.View;
import view.ViewEvent;
import view.ViewEventHandler;
import view.command.CommandQueue;
import view.command.LoadBoardCommand;
import view.command.SelectFieldCommand;

public class GameBrain implements Controller {

	private ExecutorService update_reader;
	private UpdateCommandQueue update_command_queue;

	private Communicator communicator;

	private View view;
	private CommandQueue view_command_queue;

	private Model model;

	private Field selected_field;

	public GameBrain(Communicator communicator, View view, Model model_arg) {

		super();

		this.communicator = communicator;
		this.view = view;
		this.model = model_arg;
		this.initializeModel(); // somehow get list of models

		this.view_command_queue = this.view.getCommandQueue();

		LoadBoardCommand load_command = new LoadBoardCommand(this.model.getFields());
		this.view_command_queue.enqueue(load_command);

		// just for test ...

		this.view_command_queue.enqueue(new SelectFieldCommand(this.model.getField(new Point2D(10, 9))));

		try {
			Thread.sleep(1500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// test ...

		this.initViewEventHandlers();

		this.initCommunicatorHandlers();

	}

	private void initializeModel() {

		List<Field> models = new ArrayList<Field>();

		for (int i = 5; i < 15; i++) {
			models.add(new GameField(new Point2D(i, 1), null, new Terrain()));
		}

		for (int i = 1; i < 20; i++) {
			models.add(new GameField(new Point2D(i, 2), new GameUnit(), new Terrain()));
		}

		for (int i = 1; i < 20; i++) {
			models.add(new GameField(new Point2D(i, 3), new GameUnit(), new Terrain()));
		}

		for (int i = 3; i < 18; i++) {
			models.add(new GameField(new Point2D(i, 4), new GameUnit(), new Terrain()));
		}

		for (int i = 5; i < 15; i++) {
			models.add(new GameField(new Point2D(i, 5), new GameUnit(), new Terrain()));
		}

		for (int i = -2; i < 18; i++) {
			models.add(new GameField(new Point2D(i, 6), new GameUnit(), new Terrain()));
		}

		for (int i = -4; i < 19; i++) {
			models.add(new GameField(new Point2D(i, 7), new GameUnit(), new Terrain()));
		}

		for (int i = -3; i < 18; i++) {
			models.add(new GameField(new Point2D(i, 8), new GameUnit(), new Terrain()));
		}

		for (int i = -3; i < 17; i++) {
			models.add(new GameField(new Point2D(i, 9), new GameUnit(), new Terrain()));
		}

		for (int i = -4; i < 17; i++) {
			models.add(new GameField(new Point2D(i, 10), null, new Terrain()));
		}

		for (int i = 5; i < 15; i++) {
			models.add(new GameField(new Point2D(i, 11), new GameUnit(), new Terrain()));
		}

		for (int i = 5; i < 15; i++) {
			models.add(new GameField(new Point2D(i, 12), new GameUnit(), new Terrain()));
		}

		for (int i = 5; i < 15; i++) {
			models.add(new GameField(new Point2D(i, 13), new GameUnit(), new Terrain()));
		}

		this.model.initializeModel(models);

	}

	private void initCommunicatorHandlers() {

		// set handlers for message

	}

	private void initViewEventHandlers() {

		this.view.addEventHandler("left-mouse-click-event", new ViewEventHandler() {

			public void execute(ViewEvent arg) {

				if (selected_field != null) {

					System.out.println("Select is not null...");

					MoveCommand move = new MoveCommand(selected_field, model.getField(arg.getField_position()));
					move.setView_command_queue(view_command_queue);
					move.run();

					selected_field = null;

				} else {

					System.out.println("Select is null ...");

					selected_field = model.getField(arg.getField_position());
					
					System.out.println("Selecting ...");
					SelectFieldCommand select = new SelectFieldCommand(selected_field);
					view_command_queue.enqueue(select);

				}

			}

		});

		this.view.addEventHandler("right-mouse-click-event", new ViewEventHandler() {

			public void execute(ViewEvent arg) {
				System.out.println("Right click on: " + arg.getField_position());
			}
		});
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

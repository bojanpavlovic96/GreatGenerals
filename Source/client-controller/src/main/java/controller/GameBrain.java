package controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

import communication.Communicator;
import controller.command.CtrlMoveCommand;
import controller.command.CtrlCommandQueue;
import javafx.geometry.Point2D;
import model.DataModel;
import model.Model;
import model.component.Field;
import model.component.GameField;
import model.component.Terrain;
import model.component.unit.BasicUnit;
import model.component.unit.MoveEventHandler;
import model.component.unit.Unit;
import model.path.AStar;
import model.path.PathFinder;
import view.View;
import view.ViewEvent;
import view.ViewEventHandler;
import view.command.CommandQueue;
import view.command.LoadBoardCommand;
import view.command.SelectFieldCommand;

public class GameBrain implements Controller {

	// used for receiving updates from communicator
	private Communicator communicator;

	private ExecutorService server_update_reader;
	private CtrlCommandQueue server_update_queue;
	// queue of commands

	private View view;
	private CommandQueue view_command_queue;

	private Model model;

	private Field selected_field;

	private PathFinder path_finder;

	public GameBrain(Communicator communicator, View view, Model model_arg) {

		super();

		this.communicator = communicator;
		this.view = view;
		this.model = model_arg;
		this.initializeModel(); // somehow get list of models

		this.view_command_queue = this.view.getCommandQueue();
		LoadBoardCommand load_command = new LoadBoardCommand(this.model.getFields());
		this.view_command_queue.enqueue(load_command);

		this.initViewEventHandlers();

		this.initCommunicatorHandlers();

		this.path_finder = new AStar(this.model);

	}

	private void initializeModel() {

		List<Field> models = new ArrayList<Field>();

		int left = 3;
		int right = 17;

		for (int i = 1; i < 16; i++) {

			for (int j = left; j < right; j++) {
				models.add(new GameField(new Point2D(j, i), null, new Terrain()));
			}

			if (left > -3)
				left--;
		}

		this.model.initializeModel(models);

		((DataModel) this.model).default_move_event_handler = new MoveEventHandler() {

			public void execute(Field from, Field to) {
				CtrlMoveCommand move = new CtrlMoveCommand(from, to);
				move.setView_command_queue(view_command_queue);
				move.run();
			}
		};

		this.model.setUnit(new Point2D(10, 10), "basic-unit");

	}

	private void initCommunicatorHandlers() {

		// set handlers for message

	}

	private void initViewEventHandlers() {

		this.view.addEventHandler("left-mouse-click-event", new ViewEventHandler() {

			public void execute(ViewEvent arg) {

				if (selected_field != null) {

					System.out.println("Select is not null...");

					// MoveCommand move = new MoveCommand(selected_field,
					// model.getField(arg.getField_position()));
					// move.setView_command_queue(view_command_queue);
					// move.run();

					Field goal = model.getField(arg.getField_position());
					List<Field> path = path_finder.findPath(selected_field, goal);

					for (Field field : path) {
						view_command_queue.enqueue(new SelectFieldCommand(field));
					}

					selected_field.getUnit().getMoveType().addToPath(path);
					selected_field.getUnit().getMoveType().move();

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

	// private void initializeModel() {
	//
	// List<Field> models = new ArrayList<Field>();
	//
	// for (int i = 5; i < 15; i++) {
	// models.add(new GameField(new Point2D(i, 1), null, new Terrain()));
	// }
	//
	// for (int i = 1; i < 20; i++) {
	// Field field = new GameField(new Point2D(i, 2), null, new Terrain());
	// field.setUnit(new BasicUnit(field));
	// field.getUnit().getMoveType().setOnMoveHandler(new MoveEventHandler() {
	//
	// public void execute(Field from, Field to) {
	// CtrlMoveCommand move = new CtrlMoveCommand(from, to);
	// move.setView_command_queue(view_command_queue);
	// move.run();
	// }
	//
	// });
	// models.add(field);
	// }
	//
	// for (int i = 1; i < 20; i++) {
	// Field field = new GameField(new Point2D(i, 3), null, new Terrain());
	// field.setUnit(new BasicUnit(field));
	// models.add(field);
	// }
	//
	// for (int i = 3; i < 18; i++) {
	// Field field = new GameField(new Point2D(i, 4), null, new Terrain());
	// field.setUnit(new BasicUnit(field));
	// models.add(field);
	// }
	//
	// for (int i = 5; i < 15; i++) {
	// Field field = new GameField(new Point2D(i, 5), null, new Terrain());
	// field.setUnit(new BasicUnit(field));
	// models.add(field);
	// }
	//
	// for (int i = -2; i < 18; i++) {
	// Field field = new GameField(new Point2D(i, 6), null, new Terrain());
	// field.setUnit(new BasicUnit(field));
	// models.add(field);
	// }
	//
	// for (int i = -4; i < 19; i++) {
	// Field field = new GameField(new Point2D(i, 7), null, new Terrain());
	// field.setUnit(new BasicUnit(field));
	// models.add(field);
	// }
	//
	// for (int i = -3; i < 18; i++) {
	// Field field = new GameField(new Point2D(i, 8), null, new Terrain());
	// field.setUnit(new BasicUnit(field));
	// models.add(field);
	// }
	//
	// for (int i = -3; i < 17; i++) {
	// Field field = new GameField(new Point2D(i, 9), null, new Terrain());
	// field.setUnit(new BasicUnit(field));
	// models.add(field);
	// }
	//
	// for (int i = -4; i < 17; i++) {
	// models.add(new GameField(new Point2D(i, 10), null, new Terrain()));
	// }
	//
	// for (int i = 5; i < 15; i++) {
	// Field field = new GameField(new Point2D(i, 11), null, new Terrain());
	// field.setUnit(new BasicUnit(field));
	// models.add(field);
	// }
	//
	// for (int i = 5; i < 15; i++) {
	// Field field = new GameField(new Point2D(i, 12), null, new Terrain());
	// field.setUnit(new BasicUnit(field));
	// models.add(field);
	// }
	//
	// for (int i = 5; i < 15; i++) {
	// Field field = new GameField(new Point2D(i, 13), null, new Terrain());
	// field.setUnit(new BasicUnit(field));
	// models.add(field);
	// }
	//
	// this.model.initializeModel(models);
	//
	// }

}

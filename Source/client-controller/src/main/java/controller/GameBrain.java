package controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

import controller.command.CtrlCommandQueue;
import controller.command.CtrlMoveCommand;
import controller.communication.Communicator;
import controller.communication.ServerProxy;
import javafx.geometry.Point2D;
import model.Model;
import model.component.Terrain;
import model.component.field.Field;
import model.component.field.GameField;
import model.component.unit.MoveEventHandler;
import server.Server;
import view.ShouldBeShutdown;
import view.View;
import view.ViewEvent;
import view.ViewEventHandler;
import view.command.ClearTopLayerCommand;
import view.command.CommandQueue;
import view.command.DisplayFieldInfoCommand;
import view.command.LoadBoardCommand;
import view.command.SelectFieldCommand;
import view.command.ZoomInCommand;
import view.command.ZoomOutCommand;

public class GameBrain implements Controller {

	// connection with server (other players)
	private ServerProxy server_proxy;

	// implement create commands and communication protocol
	// implement missing serverUpdateReaderTask
	private ExecutorService server_update_reader;

	// provided in message translator
	private CtrlCommandQueue server_update_queue;

	private View view;
	private CommandQueue view_command_queue;

	private Model model;

	private Field selected_field;
	private Field info_displayed;

	// methods

	public GameBrain(ServerProxy server_proxy, View view, Model new_model) {
		super();

		this.server_proxy = server_proxy;
		this.server_update_queue = this.server_proxy.getCtrlQueue();

		this.view = view;
		this.model = new_model;

		if (!this.model.isInitialized())
			this.initializeModel(); // somehow get list of models

		// view initialization
		this.view_command_queue = this.view.getCommandQueue();

		// load board with fields from model
		LoadBoardCommand load_command = new LoadBoardCommand(this.model.getFields());
		this.view_command_queue.enqueue(load_command);

		this.initViewEventHandlers();

		this.view.show();

	}

	// attention fake initialization
	private void initializeModel() {

		List<Field> models = new ArrayList<Field>();

		int left = 3;
		int right = 17;

		for (int i = 1; i < 16; i++) {

			for (int j = left; j < right; j++) {
				if (i % 2 == 0 && j % 5 == 0)
					models.add(new GameField(new Point2D(j, i), null, new Terrain("mountains", 1)));
				else
					models.add(new GameField(new Point2D(j, i), null, new Terrain("water", 1)));
			}

			if (left > -3)
				left--;
		}

		this.model.initializeModel(models);

		this.model.setDefaultMoveEventHandler(new MoveEventHandler() {

			public void execute(Field from, Field to) {

				CtrlMoveCommand move = new CtrlMoveCommand(from, to);
				move.setView_command_queue(view_command_queue);
				move.run();

			}

		});

		this.model.setUnit(new Point2D(10, 10), "basic-unit");
		this.model.setUnit(new Point2D(5, 5), "basic-unit");
		this.model.setUnit(new Point2D(5, 10), "basic-unit");
		this.model.setUnit(new Point2D(4, 7), "basic-unit");

	}

	private void initViewEventHandlers() {

		this.view.addEventHandler("left-mouse-click-event", new ViewEventHandler() {

			public void execute(ViewEvent arg) {

				if (selected_field != null) {
					System.out.println("Some action from selected to goal ...");

					if (info_displayed != null) {
						ClearTopLayerCommand clear_command = new ClearTopLayerCommand();
						view_command_queue.enqueue(clear_command);
						info_displayed = null;
					}

					Field goal = model.getField(arg.getField_position());
					if (goal.getUnit() == null) {
						selected_field.getUnit().getMoveType().calculatePath(goal);

						for (Field field : selected_field.getUnit().getMoveType().getPath()) {
							view_command_queue.enqueue(new SelectFieldCommand(field));
						}

						selected_field.getUnit().getMoveType().move();

						selected_field = null;
					}
				} else {
					if (info_displayed != null) {
						ClearTopLayerCommand clear_command = new ClearTopLayerCommand();
						view_command_queue.enqueue(clear_command);
						info_displayed = null;
					}
					selected_field = model.getField(arg.getField_position());
					if (selected_field.getUnit() == null) {
						selected_field = null;
					} else {

						System.out.println("Selecting ...");
						SelectFieldCommand select = new SelectFieldCommand(selected_field);

						view_command_queue.enqueue(select);
					}
				}

			}

		});

		this.view.addEventHandler("right-mouse-click-event", new ViewEventHandler() {

			public void execute(ViewEvent arg) {

				if (info_displayed != null) {
					ClearTopLayerCommand clear_command = new ClearTopLayerCommand();
					view_command_queue.enqueue(clear_command);
					info_displayed = null;
				}

				Field field = model.getField(arg.getField_position());

				if (field != null) {

					info_displayed = field;

					DisplayFieldInfoCommand command = new DisplayFieldInfoCommand(field);

					view_command_queue.enqueue(command);
				}
			}
		});

		this.view.addEventHandler("key-event-char-1", new ViewEventHandler() {

			public void execute(ViewEvent arg) {

				ZoomInCommand command = new ZoomInCommand(model.getFields());
				command.setView(view);
				view_command_queue.enqueue(command);

			}
		});

		this.view.addEventHandler("key-event-char-2", new ViewEventHandler() {

			public void execute(ViewEvent arg) {

				ZoomOutCommand command = new ZoomOutCommand(model.getFields());
				command.setView(view);
				view_command_queue.enqueue(command);

			}
		});

	}

	// getters and setters

	public View getView() {
		return this.view;
	}

	public void setView(View view) {
		this.view = view;
	}

	public Model getModel() {
		return this.model;
	}

	public void setModel(Model model) {
		this.model = model;
	}

	public void shutdown() {

		if (this.server_update_reader != null && !this.server_update_reader.isShutdown()) {
			this.server_update_reader.shutdown();
		}

		if (this.view != null) {
			((ShouldBeShutdown) this.view).shutdown();
		}

		// TODO somehow cancel timer tasks

	}

	public Communicator getCommunicator() {
		return (Communicator) this.server_proxy;
	}

}

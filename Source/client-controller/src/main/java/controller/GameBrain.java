package controller;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import controller.command.CtrlCommandProcessor;
import controller.command.CtrlCommandQueue;
import controller.communication.Communicator;
import controller.communication.ServerProxy;
import model.Model;
import model.component.field.Field;
import view.ShouldBeShutdown;
import view.View;
import view.ViewEvent;
import view.ViewEventHandler;
import view.command.ClearTopLayerCommand;
import view.command.CommandQueue;
import view.command.DisplayFieldInfoCommand;
import view.command.SelectFieldCommand;
import view.command.ZoomInCommand;
import view.command.ZoomOutCommand;

public class GameBrain implements Controller {

	// connection with server (other players)
	private ServerProxy server_proxy;

	private ExecutorService ctrl_command_executor;
	private CtrlCommandQueue server_update_queue;

	private View view;
	private CommandQueue view_command_queue;

	private Model model;

	private Field selected_field;
	private Field info_displayed;

	// methods

	public GameBrain(ServerProxy server_proxy, View view, Model new_model) {
		super();

		this.ctrl_command_executor = Executors.newSingleThreadExecutor();

		this.server_proxy = server_proxy;
		this.server_update_queue = new CtrlCommandQueue();
		this.server_update_queue.setOnEnqueueEventHandler(new CtrlCommandProcessor(	this.ctrl_command_executor,
																					this));

		this.view = view;
		this.model = new_model;

		this.server_proxy.setCtrlQueue(this.server_update_queue);

		// TODO remove this, first server message is initialization message
		// if (!this.model.isInitialized())
		// this.initializeModel(); // somehow get list of models

		// view initialization
		this.view_command_queue = this.view.getCommandQueue();

		// TODO remove this, also called from initialization command after server
		// initialization message
		// LoadBoardCommand load_command = new LoadBoardCommand(this.model.getFields());
		// this.view_command_queue.enqueue(load_command);

		// view events, click, key ...
		this.initViewEventHandlers();

		this.view.show();

	}

	// attention fake initialization
	private void initializeModel() {

		// List<Field> models = new ArrayList<Field>();
		//
		// int left = 3;
		// int right = 17;
		//
		// for (int i = 1; i < 16; i++) {
		//
		// for (int j = left; j < right; j++) {
		// if (i % 2 == 0 && j % 5 == 0)
		// models.add(new GameField(new Point2D(j, i), true, null, new
		// Terrain("mountains", 1)));
		// else
		// models.add(new GameField(new Point2D(j, i), true, null, new Terrain("water",
		// 1)));
		// }
		//
		// if (left > -3)
		// left--;
		// }
		//
		// this.model.initializeModel(models);
		//
		// this.model.setDefaultMoveEventHandler(new MoveEventHandler() {
		//
		// public void execute(Field from, Field to) {
		//
		// CtrlMoveCommand move = new CtrlMoveCommand(from, to);
		// move.setViewCommandQueue(view_command_queue);
		// move.run();
		//
		// }
		//
		// });
		//
		// this.model.setUnit(new Point2D(10, 10), "basic-unit");
		// this.model.setUnit(new Point2D(5, 5), "basic-unit");
		// this.model.setUnit(new Point2D(5, 10), "basic-unit");
		// this.model.setUnit(new Point2D(4, 7), "basic-unit");

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

		if (this.ctrl_command_executor != null && !this.ctrl_command_executor.isShutdown()) {
			this.ctrl_command_executor.shutdown();
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

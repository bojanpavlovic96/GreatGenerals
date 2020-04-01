package controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import controller.action.DefaultModelEventHandler;
import controller.option.AddToPathFieldOption;
import controller.option.MoveFieldOption;
import controller.option.SelectPathFieldOption;

import root.ActiveComponent;
import root.command.BasicCommandProcessor;
import root.command.Command;
import root.command.CommandProcessor;
import root.command.CommandProducer;
import root.command.CommandQueue;
import root.communication.ServerProxy;
import root.controller.Controller;
import root.model.Model;
import root.model.component.Field;
import root.model.component.option.FieldOption;
import root.model.event.ModelEventArg;
import root.view.View;
import root.view.event.ViewEventArg;
import root.view.event.ViewEventHandler;

import view.command.SelectFieldCommand;
import view.command.ShowFieldInfoCommand;
import view.command.ZoomInCommand;
import view.command.ZoomOutCommand;

public class GameBrain implements Controller {

	private ServerProxy server_proxy;

	private ExecutorService serverCommandExecutor;
	private CommandProcessor serverCommandProcessor;
	private CommandQueue serverCommandQueue;

	private View view;
	private CommandQueue view_command_queue;

	private Model model;

	private Field selected_field;
	private List<Command> to_undo;

	private List<FieldOption> field_options;

	// constructors
	public GameBrain(ServerProxy server_proxy, View view, Model model) {
		super();

		this.view = view;
		this.model = model;
		this.server_proxy = server_proxy;

		this.to_undo = new ArrayList<Command>();

		// attention let's say that every controller implementations has its own
		// ModelEventHandler (maybe this isn't the best approach)
		this.model.setEventHandler(new DefaultModelEventHandler(this));

		this.initFieldOptions();

		// --- connect serverProxy and controller

		this.serverCommandQueue = ((CommandProducer) this.server_proxy).getConsumerQueue();

		this.serverCommandExecutor = Executors.newSingleThreadExecutor();
		this.serverCommandProcessor = new BasicCommandProcessor(this.serverCommandExecutor, this);
		this.serverCommandQueue.setCommandProcessor(this.serverCommandProcessor);

		// --- done with serverProxy

		this.view_command_queue = this.view.getCommandQueue();

		// view events, click, key press ...
		this.initViewEventHandlers();

		this.view.show();

	}

	// methods

	private void initViewEventHandlers() {

		this.view.addEventHandler("left-mouse-click-event", new ViewEventHandler() {

			public void execute(ViewEventArg arg) {

				Field focused_field = model.getField(arg.getFieldPosition());

				if (focused_field != null) {

					// undo all previous commands
					if (!to_undo.isEmpty()) {
						for (int i = (to_undo.size() - 1); i >= 0; i--) {
							view_command_queue.enqueue(to_undo.get(i).getAntiCommand());
						}
					}
					to_undo.clear();

					// execute new command
					Command select_command = new SelectFieldCommand(focused_field);
					view_command_queue.enqueue(select_command);

					to_undo.add(select_command);

					selected_field = focused_field;

				}

			}

		});

		this.view.addEventHandler("right-mouse-click-event", new ViewEventHandler() {

			public void execute(ViewEventArg arg) {

				Field focused_field = model.getField(arg.getFieldPosition());

				// valid click
				if (focused_field != null) {
					Command show_menu = new ShowFieldInfoCommand(selected_field, focused_field);
					view_command_queue.enqueue(show_menu);
					to_undo.add(show_menu);
				}

			}

		});

		this.view.addEventHandler("key-event-char-1", new ViewEventHandler() {

			public void execute(ViewEventArg arg) {

				ZoomInCommand command = new ZoomInCommand(model.getFields());
				view_command_queue.enqueue(command);

				// reset old state
				for (Command prev_command : to_undo) {
					view_command_queue.enqueue(prev_command);
				}

			}
		});

		this.view.addEventHandler("key-event-char-2", new ViewEventHandler() {

			public void execute(ViewEventArg arg) {

				ZoomOutCommand command = new ZoomOutCommand(model.getFields());
				view_command_queue.enqueue(command);

				// reset old state
				for (Command prev_command : to_undo) {
					view_command_queue.enqueue(prev_command);
				}

			}
		});

	}

	private void initFieldOptions() {

		this.field_options = new ArrayList<FieldOption>();

		this.field_options.add(new SelectPathFieldOption(this));
		this.field_options.add(new MoveFieldOption(this));
		this.field_options.add(new AddToPathFieldOption(this));

	}

	// getters and setters

	@Override
	public View getView() {
		return this.view;
	}

	@Override
	public void setView(View view) {
		this.view = view;
	}

	@Override
	public Model getModel() {
		return this.model;
	}

	@Override
	public void setModel(Model model) {
		this.model = model;
	}

	@Override
	public void shutdown() {

		if (this.serverCommandExecutor != null && !this.serverCommandExecutor.isShutdown()) {
			this.serverCommandExecutor.shutdown();
		}

		if (this.view != null) {
			((ActiveComponent) this.view).shutdown();
		}

		if (this.model != null) {
			this.model.shutdown();
		}

	}

	@Override
	public void execute(ModelEventArg event_argument) {
		this.server_proxy.sendIntention(event_argument);
	}

	@Override
	public ServerProxy getServerProxy() {
		return this.server_proxy;
	}

	@Override
	public void setServerProxy(root.communication.ServerProxy new_proxy) {
		this.server_proxy = new_proxy;
	}

	@Override
	public CommandQueue getCommandQueue() {
		return this.serverCommandQueue;
	}

	@Override
	public void setCommandQueue(CommandQueue new_queue) {
		this.serverCommandQueue = new_queue;
	}

	@Override
	public CommandProcessor getCommandProcessor() {
		return this.serverCommandProcessor;
	}

	public void setConsumerQueue(CommandQueue consumer_queue) {
		this.view_command_queue = consumer_queue;
	}

	@Override
	public CommandQueue getConsumerQueue() {
		return this.view_command_queue;
	}

	@Override
	public List<FieldOption> getFieldOptions() {
		return this.field_options;
	}

	@Override
	public void enqueueForUndone(Command new_command) {
		this.to_undo.add(new_command);
	}

}

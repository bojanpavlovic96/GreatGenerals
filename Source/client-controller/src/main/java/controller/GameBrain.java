package controller;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import controller.action.DefaultModelEventHandler;

import root.ActiveComponent;
import root.command.BasicCommandProcessor;
import root.command.CommandProcessor;
import root.command.CommandProducer;
import root.command.CommandQueue;
import root.communication.ServerProxy;
import root.controller.Controller;
import root.model.Model;
import root.model.component.Field;
import root.model.event.ModelEventArg;
import root.view.View;
import root.view.event.ViewEventArg;
import root.view.event.ViewEventHandler;

import view.command.DisplayFieldInfoCommand;
import view.command.ZoomInCommand;
import view.command.ZoomOutCommand;

public class GameBrain implements Controller {

	private ServerProxy server_proxy;

	private ExecutorService server_command_executor;
	private CommandProcessor server_command_processor;
	private CommandQueue server_command_queue;

	private View view;
	private CommandQueue view_command_queue;

	private Model model;

	private Field selected_field;
	private Field info_displayed;

	// methods

	public GameBrain(ServerProxy server_proxy, View view, Model model) {
		super();

		this.view = view;
		this.model = model;
		this.server_proxy = server_proxy;

		// attention let's say that every controller implementations has its own
		// ModelEventHandler (maybe this isn't the best approach)
		this.model.setEventHandler(new DefaultModelEventHandler(this));

		// --- connect serverProxy and controller

		this.server_command_queue = ((CommandProducer) this.server_proxy).getConsumerQueue();

		// server commands executor
		this.server_command_executor = Executors.newSingleThreadExecutor();
		this.server_command_processor = new BasicCommandProcessor(this.server_command_executor, this);
		this.server_command_queue.setCommandProcessor(this.server_command_processor);

		// --- done with serverProxy

		this.view_command_queue = this.view.getCommandQueue();

		// view events, click, key press ...
		this.initViewEventHandlers();

		this.view.show();

	}

	private void initViewEventHandlers() {

		this.view.addEventHandler("left-mouse-click-event", new ViewEventHandler() {

			public void execute(ViewEventArg arg) {

				// debug
				System.out.println("Handling view event ... " + arg.getEvent().getEventType().getName());
				Field focused_field = model.getField(arg.getFieldPosition());

				if (selected_field != null) {
					// debug
					System.out.println("\talready has selection @ GameBrain.left-mouse-click-event");

				} else {
					// debug
					System.out.println("\tselecting field @ GameBrain.left-mouse-click-event");

				}

			}
		});

		this.view.addEventHandler("right-mouse-click-event", new ViewEventHandler() {

			public void execute(ViewEventArg arg) {

				Field field = model.getField(arg.getFieldPosition());

				if (field != null) {
					
					info_displayed = field;

					DisplayFieldInfoCommand command = new DisplayFieldInfoCommand(field);

					view_command_queue.enqueue(command);
				}
			}
		});

		this.view.addEventHandler("key-event-char-1", new ViewEventHandler() {

			public void execute(ViewEventArg arg) {

				ZoomInCommand command = new ZoomInCommand(model.getFields());
				view_command_queue.enqueue(command);

			}
		});

		this.view.addEventHandler("key-event-char-2", new ViewEventHandler() {

			public void execute(ViewEventArg arg) {

				ZoomOutCommand command = new ZoomOutCommand(model.getFields());
				view_command_queue.enqueue(command);

			}
		});

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

		if (this.server_command_executor != null && !this.server_command_executor.isShutdown()) {
			this.server_command_executor.shutdown();
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
	public root.communication.ServerProxy getServerProxy() {
		return this.server_proxy;
	}

	@Override
	public void setServerProxy(root.communication.ServerProxy new_proxy) {
		this.server_proxy = new_proxy;
	}

	@Override
	public CommandQueue getCommandQueue() {
		return this.server_command_queue;
	}

	@Override
	public void setCommandQueue(CommandQueue new_queue) {
		this.server_command_queue = new_queue;
	}

	@Override
	public CommandProcessor getCommandProcessor() {
		return this.server_command_processor;
	}

	public void setConsumerQueue(CommandQueue consumer_queue) {
		this.view_command_queue = consumer_queue;
	}

	@Override
	public CommandQueue getConsumerQueue() {
		return this.view_command_queue;
	}

}

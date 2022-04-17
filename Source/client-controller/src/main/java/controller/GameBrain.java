package controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import controller.command.UndoStack;
import controller.option.AddToPathFieldOption;
import controller.option.MoveFieldOption;
import controller.option.SelectPathFieldOption;
import controller.option.StopMovingFieldOption;
import root.ActiveComponent;
import root.command.BasicCommandProcessor;
import root.command.Command;
import root.command.CommandDrivenComponent;
import root.command.CommandProcessor;
import root.command.CommandProducer;
import root.command.CommandQueue;
import root.communication.GameServerProxy;
import root.controller.CommandStack;
import root.controller.Controller;
import root.model.Model;
import root.model.component.Field;
import root.model.component.option.FieldOption;
import root.model.event.ModelEventArg;
import root.model.event.ModelEventHandler;
import root.view.View;
import root.view.event.ViewEventArg;
import root.view.event.ViewEventHandler;
import root.view.menu.Menu;
import view.command.ClearTopLayerCommand;
import view.command.PopulateMenuCommand;
import view.command.ShowFieldInfoCommand;
import view.command.SelectFieldCommand;
import view.command.ZoomInCommand;
import view.command.ZoomOutCommand;

public class GameBrain implements Controller {

	private GameServerProxy serverProxy;

	private CommandProcessor serverCommandProcessor;
	private CommandQueue serverCommandQueue;

	private View view;
	private CommandQueue viewCommandQueue;

	private Model model;

	// selected item is the highlighted one
	// focused item is the right-clicked one

	private Field selectedField;
	private Field focusedField;

	private UndoStack undoStack;
	// private List<Command> undoStack;

	private List<FieldOption> fieldOptions;

	// constructors
	public GameBrain(GameServerProxy serverProxy, View view, Model model) {
		super();

		this.view = view;
		this.model = model;
		this.serverProxy = serverProxy;

		// this.undoStack = new ArrayList<Command>();
		this.undoStack = new UndoStack();

		// attention let's say that every controller implementations has its own
		// ModelEventHandler (maybe this isn't the best approach)
		this.model.setEventHandler((ModelEventHandler) this);

		this.initFieldOptions();

		// connect serverProxy and controller
		this.serverCommandQueue = ((CommandProducer) serverProxy).getConsumerQueue();
		this.serverCommandProcessor = new BasicCommandProcessor(
				Executors.newSingleThreadExecutor(),
				(CommandDrivenComponent) this);
		this.serverCommandQueue.setCommandProcessor(this.serverCommandProcessor);

		this.viewCommandQueue = this.view.getCommandQueue();

		// view events, click, key press ...
		this.initViewEventHandlers();

		this.view.show();

	}

	// methods

	private void initViewEventHandlers() {

		this.view.addEventHandler("left-mouse-click-event", new ViewEventHandler() {

			public void execute(ViewEventArg arg) {

				Field clickedField = model.getField(arg.getFieldPosition());
				if (clickedField != null) {

					// undo all previous commands
					Command doneCommand = null;
					while ((doneCommand = undoStack.pop()) != null) {
						var antiCommand = doneCommand.getAntiCommand();
						viewCommandQueue.enqueue(antiCommand);
					}

					var selectField = new SelectFieldCommand(clickedField);

					viewCommandQueue.enqueue(selectField);
					undoStack.push(selectField);

					if (clickedField.getUnit() != null
							&& clickedField.getUnit().getMoveType() != null
							&& clickedField.getUnit().getMoveType().getPath() != null) {

						for (Field pathField : clickedField
								.getUnit()
								.getMoveType()
								.getPath()) {

							var selectPathField = new SelectFieldCommand(pathField);

							viewCommandQueue.enqueue(selectPathField);
							undoStack.push(selectPathField);
						}

					}

					// remove
					// var selectCommand = new ComplexSelectFieldCommand(clickedField);
					// viewCommandQueue.enqueue(selectCommand);

					selectedField = clickedField;
					focusedField = null;

				}

			}

		});

		this.view.addEventHandler("right-mouse-click-event", new ViewEventHandler() {

			public void execute(ViewEventArg arg) {

				focusedField = model.getField(arg.getFieldPosition());

				// valid click
				if (focusedField != null) {

					var clearCommand = new ClearTopLayerCommand();

					Command showMenuCommand;
					if (selectedField != null) {
						showMenuCommand = new ShowFieldInfoCommand(selectedField, focusedField);
					} else {
						showMenuCommand = new ShowFieldInfoCommand(focusedField, focusedField);
					}

					viewCommandQueue.enqueue(clearCommand);
					viewCommandQueue.enqueue(showMenuCommand);

					undoStack.push(showMenuCommand);

				}

			}

		});

		// i don't understand this comment 9.12.2021
		// TODO maybe for the purpose of redrawing path and similar options
		// add additional list of command which are "stateless" and which execution wont
		// do any damage to the current state if they are executed more than once

		this.view.addEventHandler("key-event-char-1", new ViewEventHandler() {

			public void execute(ViewEventArg arg) {

				ZoomInCommand command = new ZoomInCommand(model.getFields());
				viewCommandQueue.enqueue(command);

				// this wont be valid in situation when attack and build commands get
				// implemented
				// // reset old state
				// for (Command prev_command : toUndo) {
				// viewCommandQueue.enqueue(prev_command);
				// }

			}
		});

		this.view.addEventHandler("key-event-char-2", new ViewEventHandler() {

			public void execute(ViewEventArg arg) {

				ZoomOutCommand command = new ZoomOutCommand(model.getFields());
				viewCommandQueue.enqueue(command);

				// this wont be valid in situation when attack and build command get implemented
				// // reset old state
				// for (Command prev_command : toUndo) {
				// viewCommandQueue.enqueue(prev_command);
				// }

			}
		});

	}

	private void initFieldOptions() {

		this.fieldOptions = new ArrayList<FieldOption>();

		this.fieldOptions.add(new SelectPathFieldOption(this));
		this.fieldOptions.add(new MoveFieldOption(this));
		this.fieldOptions.add(new AddToPathFieldOption(this));
		this.fieldOptions.add(new StopMovingFieldOption(this));

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

		if (this.serverCommandProcessor != null) {
			((ActiveComponent) this.serverCommandProcessor).shutdown();
		}

		if (this.view != null) {
			((ActiveComponent) this.view).shutdown();
		}

		if (this.model != null) {
			this.model.shutdown();
		}

	}

	@Override
	public void handleModelEvent(ModelEventArg eventArg) {
		this.serverProxy.sendIntention(eventArg);
	}

	@Override
	public GameServerProxy getServerProxy() {
		return this.serverProxy;
	}

	@Override
	public void setServerProxy(GameServerProxy newProxy) {
		this.serverProxy = newProxy;
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

	@Override
	public CommandQueue getConsumerQueue() {
		return this.viewCommandQueue;
	}

	@Override
	public void setConsumerQueue(CommandQueue consumer_queue) {
		this.viewCommandQueue = consumer_queue;
	}

	@Override
	public List<FieldOption> getPossibleFieldOptions() {
		return this.fieldOptions;
	}

	@Override
	public Field getSelectedField() {
		return this.selectedField;
	}

	@Override
	public void setSelectedField(Field newField) {
		this.selectedField = newField;
	}

	// TODO this should be removed ...
	@Override
	public void selectField(Field fieldToSelect) {

		this.selectedField = fieldToSelect;

		var selectField = new SelectFieldCommand(this.selectedField);
		this.viewCommandQueue.enqueue(selectField);

		if (this.selectedField.getUnit() != null
				&& this.selectedField.getUnit().getMoveType() != null
				&& this.selectedField.getUnit().getMoveType().getPath() != null) {
			for (Field pathField : this.selectedField
					.getUnit()
					.getMoveType()
					.getPath()) {
				var selectPathField = new SelectFieldCommand(pathField);

				viewCommandQueue.enqueue(selectPathField);
				this.undoStack.equals(selectPathField);
			}
		}

		// TODO feel like this is useless
		Menu fieldMenu = view.getOptionMenu();
		if (fieldMenu.isDisplayed()) {
			selectedField.adjustOptionsFor(focusedField);
			viewCommandQueue.enqueue(new PopulateMenuCommand(selectedField.getEnabledOptions()));
		}

	}

	@Override
	public CommandStack getUndoStack() {
		return this.undoStack;
	}

}

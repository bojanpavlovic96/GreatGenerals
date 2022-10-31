package controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import controller.command.UndoStack;
import controller.option.AddToPathFieldOption;
import controller.option.MoveFieldOption;
import controller.option.SelectPathFieldOption;
import controller.option.StopMovingFieldOption;
import model.event.ReadyForInitEvent;
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
import view.command.ClearTopLayerCommand;
import view.command.ShowFieldMenuCommand;
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

	private List<FieldOption> fieldOptions;

	public GameBrain(GameServerProxy serverProxy, View view, Model model) {
		super();

		this.serverProxy = serverProxy;
		this.view = view;
		this.model = model;

		undoStack = new UndoStack();

		// attention let's say that every controller implementations has its own
		// ModelEventHandler (maybe this isn't the best approach)
		model.setEventHandler((ModelEventHandler) this);

		initFieldOptions();

		// connect serverProxy and controller
		serverCommandQueue = ((CommandProducer) serverProxy).getConsumerQueue();
		serverCommandProcessor = new BasicCommandProcessor(
				Executors.newSingleThreadExecutor(),
				(CommandDrivenComponent) this);
		serverCommandQueue.setCommandProcessor(this.serverCommandProcessor);

		viewCommandQueue = new CommandQueue();
		((CommandDrivenComponent) view).setCommandQueue(viewCommandQueue);

		// view events, click, key press ...
		initViewEventHandlers();

		// This may be a bit hacky way of doing it since getUsername/roomName methods
		// are added to the interface just for this case. 
		// Doesn't break anything I guess ... 
		var readyEvent = new ReadyForInitEvent(
				serverProxy.getUsername(),
				serverProxy.getRoomName());

		System.out.println("Sent ready for init event: ");
		System.out.println("\t" + serverProxy.getUsername());
		System.out.println("\t" + serverProxy.getRoomName());

		serverProxy.sendIntention(readyEvent);

		view.showView();
	}

	private void initViewEventHandlers() {

		// TODO maybe put this inside the ctrlInitializeCommand
		// if server is very slow you could be able to click on the map before
		// the fields are initialized (created)
		// other approach would be put some loading screen above everything 
		// until we get ctrlInitializeCommand ... 
		view.addEventHandler("left-mouse-click-event", (ViewEventArg arg) -> {

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

		});

		view.addEventHandler("right-mouse-click-event", (ViewEventArg arg) -> {

			focusedField = model.getField(arg.getFieldPosition());

			// valid click
			if (focusedField != null) {

				var clearCommand = new ClearTopLayerCommand();

				Command showMenuCommand;
				if (selectedField != null) {
					showMenuCommand = new ShowFieldMenuCommand(selectedField, focusedField);
				} else {
					showMenuCommand = new ShowFieldMenuCommand(focusedField, focusedField);
				}

				viewCommandQueue.enqueue(clearCommand);
				viewCommandQueue.enqueue(showMenuCommand);

				undoStack.push(showMenuCommand);

			}

		});

		// 25.7.2022 - understanding: drawing path 
		// i don't understand this comment 9.12.2021
		// TODO maybe for the purpose of redrawing path and similar options
		// add additional list of command which are "stateless" and which execution wont
		// do any damage to the current state if they are executed more than once

		view.addEventHandler("key-event-char-1", (ViewEventArg arg) -> {

			ZoomInCommand command = new ZoomInCommand(model.getFields());
			viewCommandQueue.enqueue(command);

			// this wont be valid in situation when attack and build commands get
			// implemented
			// // reset old state
			// for (Command prev_command : toUndo) {
			// viewCommandQueue.enqueue(prev_command);
			// }

		});

		view.addEventHandler("key-event-char-2", (ViewEventArg arg) -> {

			ZoomOutCommand command = new ZoomOutCommand(model.getFields());
			viewCommandQueue.enqueue(command);

			// this wont be valid in situation when attack and build command get implemented
			// // reset old state
			// for (Command prev_command : toUndo) {
			// viewCommandQueue.enqueue(prev_command);
			// }

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

		if (this.serverProxy != null
				&& (this.serverProxy instanceof ActiveComponent)) {
			((ActiveComponent) this.serverProxy).shutdown();
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

	@Override
	public CommandStack getUndoStack() {
		return this.undoStack;
	}

}

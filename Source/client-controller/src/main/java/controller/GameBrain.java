package controller;

import java.util.ArrayList;
import java.util.List;

import controller.command.UndoStack;
import controller.option.AbortAttackFieldOption;
import controller.option.AddToPathFieldOption;
import controller.option.AttacksSubmenuFieldOption;
import controller.option.ClearPathFieldOption;
import controller.option.MoveFieldOption;
import controller.option.RetreatFieldOption;
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
import root.model.PlayerData;
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

	private PlayerData player;

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

	public GameBrain(PlayerData player, GameServerProxy serverProxy, View view, Model model) {

		this.player = player;

		this.serverProxy = serverProxy;
		this.view = view;
		this.model = model;

		undoStack = new UndoStack();

		model.setModelEventHandler((ModelEventHandler) this);

		initFieldOptions();

		// connect serverProxy and controller
		serverCommandQueue = ((CommandProducer) serverProxy).getConsumerQueue();
		serverCommandProcessor = new BasicCommandProcessor((CommandDrivenComponent) this);
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

		view.addEventHandler("left-mouse-click-event", (ViewEventArg arg) -> {

			Field clickedField = model.getField(arg.getFieldPosition());

			if (clickedField == null) {
				return;
			}

			// undo all previous commands
			Command doneCommand = null;
			while ((doneCommand = undoStack.pop()) != null) {
				var antiCommand = doneCommand.getAntiCommand();
				viewCommandQueue.enqueue(antiCommand);
			}

			var selectCmd = new SelectFieldCommand(clickedField);

			viewCommandQueue.enqueue(selectCmd);
			undoStack.push(selectCmd);

			if (clickedField.getUnit() != null
					&& clickedField.getUnit().getMove() != null
					&& clickedField.getUnit().getMove().getPath() != null) {

				var path = clickedField.getUnit().getMove().getPath();
				path = path.subList(1, path.size());

				for (Field pathField : path) {

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

		});

		view.addEventHandler("right-mouse-click-event", (ViewEventArg arg) -> {

			focusedField = model.getField(arg.getFieldPosition());

			// valid click
			if (focusedField == null) {
				return;
			}

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

		});

		// 25.7.2022 - understanding: drawing path 
		// I don't understand this comment 9.12.2021
		// TODO maybe for the purpose of redrawing path and similar options
		// add additional list of commands which are "stateless" and which execution wont
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

		fieldOptions = new ArrayList<FieldOption>();

		fieldOptions.add(new SelectPathFieldOption(this));
		fieldOptions.add(new MoveFieldOption(this));
		fieldOptions.add(new AddToPathFieldOption(this));
		fieldOptions.add(new StopMovingFieldOption(this));
		fieldOptions.add(new ClearPathFieldOption(this));

		fieldOptions.add(new AttacksSubmenuFieldOption(this));
		fieldOptions.add(new AbortAttackFieldOption(this));
		fieldOptions.add(new RetreatFieldOption(this));
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

	@Override
	public PlayerData getPlayer() {
		return player;
	}

	@Override
	public boolean isOwner(String name) {
		return player.getUsername().equals(name);
	}

}

package controller;

import java.util.ArrayList;
import java.util.List;

import controller.command.SingleThreadedQueue;
import controller.command.UndoStack;
import controller.option.AbortAttackFieldOption;
import controller.option.AddToPathFieldOption;
import controller.option.ShowAttacksFieldOption;
import controller.option.ShowUnitsFieldOption;
import controller.option.ClearPathFieldOption;
import controller.option.MoveFieldOption;
import controller.option.SelectPathFieldOption;
import controller.option.StopMovingFieldOption;
import model.intention.LeaveGameIntention;
import model.intention.ReadyForInitIntention;
import model.intention.ReadyForReplayIntention;
import root.ActiveComponent;
import root.command.CommandQueue;
import root.command.Command;
import root.communication.GameServerProxy;
import root.controller.CommandStack;
import root.controller.Controller;
import root.model.Model;
import root.model.component.Field;
import root.model.component.option.FieldOption;
import root.model.event.ClientIntention;
import root.model.event.ModelEventHandler;
import root.view.View;
import root.view.event.ViewEventArg;
import view.command.ClearTopLayerCommand;
import view.command.ShowFieldMenuCommand;
import view.command.SelectFieldCommand;
import view.command.ShowFieldDescription;
import view.command.ZoomInCommand;
import view.command.ZoomOutCommand;

public class GameController implements Controller, ActiveComponent {

	private GameDoneHandler onGameDone;

	private GameServerProxy serverProxy;

	// private CommandProcessor serverCmdProcessor;
	// private CommandQueue serverCommandQueue;
	private CommandQueue commandQueue;

	private View view;
	private CommandQueue consumerQueue;

	private Model model;

	// selected item is the highlighted one
	// focused item is the right-clicked one

	private Field selectedField;
	private Field focusedField;

	private UndoStack undoStack;

	private List<FieldOption> fieldOptions;

	public GameController(GameServerProxy serverProxy,
			View view,
			Model model,
			GameDoneHandler onGameDone,
			boolean asReplay) {

		this.onGameDone = onGameDone;

		this.serverProxy = serverProxy;
		this.view = view;
		this.model = model;

		this.commandQueue = new SingleThreadedQueue(this);

		undoStack = new UndoStack();

		model.setModelEventHandler((ModelEventHandler) this);

		fieldOptions = new ArrayList<FieldOption>();
		if (!asReplay) {
			populateFieldOptions();
		}

		// connect serverProxy and controller
		// serverCommandQueue = ((CommandProducer) serverProxy).getConsumerQueue();
		// serverCmdProcessor = new BasicCommandProcessor((CommandDrivenComponent) this);
		// serverCommandQueue.setCommandProcessor(this.serverCmdProcessor);
		serverProxy.setConsumer(this.getCommandQueue());

		// viewCommandQueue = new CommandQueue();
		// ((CommandDrivenComponent) view).setCommandQueue(viewCommandQueue);

		this.setConsumer(view.getCommandQueue());

		// view events, click, key press ...
		initViewEventHandlers();

		// This may be a bit hacky way of doing it since getUsername/roomName methods
		// are added to the interface just for this case. 
		// Doesn't break anything I guess ... 
		ClientIntention readyEvent = null;
		if (asReplay) {
			readyEvent = new ReadyForReplayIntention(
					serverProxy.getUsername(),
					serverProxy.getRoomName());
		} else {
			readyEvent = new ReadyForInitIntention(
					serverProxy.getUsername(),
					serverProxy.getRoomName());
		}

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
				consumerQueue.enqueue(antiCommand);
			}

			var selectCmd = new SelectFieldCommand(clickedField);

			System.out.println("Clicked on -> x:" + clickedField.getStoragePosition().getX()
					+ " y:" + clickedField.getStoragePosition().getY());

			// var neighs = clickedField.getNeighbours(1);
			// for (var n : neighs) {
			// 	var comm = new SelectFieldCommand(model.getField(n));
			// 	viewCommandQueue.enqueue(comm);
			// }

			consumerQueue.enqueue(selectCmd);
			undoStack.push(selectCmd);

			if (clickedField.getUnit() != null
					&& clickedField.getUnit().getMove() != null
					&& clickedField.getUnit().getMove().getPath() != null) {

				var path = clickedField.getUnit().getMove().getPath();
				path = path.subList(1, path.size());

				for (Field pathField : path) {

					var selectPathField = new SelectFieldCommand(pathField);

					consumerQueue.enqueue(selectPathField);
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

			// var modelDist = model.distance(selectedField, focusedField);
			// var ax = selectedField.getStoragePosition().x;
			// var ay = selectedField.getStoragePosition().y;

			// var bx = focusedField.getStoragePosition().x;
			// var by = focusedField.getStoragePosition().y;

			// var xDist = Math.abs(ax - bx);
			// var yDist = Math.abs(ay - by);

			// var myDist = Math.sqrt(Math.pow(xDist, 2) + Math.pow(yDist, 2));

			// System.out.println("ModelDist: " + modelDist + " MyDist: " + myDist);

			var clearCommand = new ClearTopLayerCommand();

			Command showMenuCommand;
			if (selectedField != null) {
				showMenuCommand = new ShowFieldMenuCommand(selectedField, focusedField);
			} else {
				showMenuCommand = new ShowFieldMenuCommand(focusedField, focusedField);
			}

			var showDescriptionCommand = new ShowFieldDescription(focusedField);

			consumerQueue.enqueue(clearCommand);
			consumerQueue.enqueue(showMenuCommand);
			consumerQueue.enqueue(showDescriptionCommand);

			undoStack.push(showMenuCommand);
			undoStack.push(showDescriptionCommand);

		});

		// 25.7.2022 - understanding: drawing path 
		// I don't understand this comment 9.12.2021
		// . TODO maybe for the purpose of redrawing path and similar options
		// add additional list of commands which are "stateless" and which execution wont
		// do any damage to the current state if they are executed more than once

		view.addEventHandler("key-event-char-1", (ViewEventArg arg) -> {

			ZoomInCommand command = new ZoomInCommand(model.getFields());
			consumerQueue.enqueue(command);

			// this wont be valid in situation when attack and build commands get
			// implemented
			// // reset old state
			// for (Command prev_command : toUndo) {
			// viewCommandQueue.enqueue(prev_command);
			// }

		});

		view.addEventHandler("key-event-char-2", (ViewEventArg arg) -> {

			ZoomOutCommand command = new ZoomOutCommand(model.getFields());
			consumerQueue.enqueue(command);

			// this wont be valid in situation when attack and build command get implemented
			// // reset old state
			// for (Command prev_command : toUndo) {
			// viewCommandQueue.enqueue(prev_command);
			// }

		});

		view.addEventHandler("key-event-char-q", (ViewEventArg arg) -> {

			System.out.println("Handling Q event ... ");

			var leaveIntention = new LeaveGameIntention(model.getOwner().getUsername());
			serverProxy.sendIntention(leaveIntention);

			// view.hideView();
			// onGameDone.handleGameDone();

			// // ??? should I ... ? 
			// shutdown();
		});

	}

	private void populateFieldOptions() {

		fieldOptions.add(new SelectPathFieldOption(this));
		fieldOptions.add(new MoveFieldOption(this));
		fieldOptions.add(new AddToPathFieldOption(this));
		fieldOptions.add(new StopMovingFieldOption(this));
		fieldOptions.add(new ClearPathFieldOption(this));

		fieldOptions.add(new AbortAttackFieldOption(this));
		fieldOptions.add(new ShowAttacksFieldOption(this));

		fieldOptions.add(new ShowUnitsFieldOption(this));
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

		// Injected  dependencies should be destroyed/shutdown by the creator
		// which in this case is Launcher ... ? 

		System.out.println("Shutting down command queue ... ");
		// if (serverCmdProcessor != null && serverCmdProcessor instanceof ActiveComponent) {
		// 	((ActiveComponent) serverCmdProcessor).shutdown();
		// }
		if (commandQueue != null && commandQueue instanceof ActiveComponent) {
			((ActiveComponent) commandQueue).shutdown();
		}

		System.out.println("Shutting down view ... ");
		if (view != null) {
			if (view instanceof ActiveComponent) {
				((ActiveComponent) view).shutdown();
			}
			view.hideView();
		}

		System.out.println("Shutting down model ... ");
		if (model != null && model instanceof ActiveComponent) {
			((ActiveComponent) model).shutdown();
		}

		System.out.println("Shutting down server proxy ... ");
		if (serverProxy != null && serverProxy instanceof ActiveComponent) {
			((ActiveComponent) serverProxy).shutdown();
		}

		onGameDone.handleGameDone();
	}

	@Override
	public void handle(ClientIntention eventArg) {
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
		return commandQueue;
	}

	// @Override
	// public void setCommandQueue(CmdQueue newQueue) {
	// 	this.serverCommandQueue = newQueue;
	// }

	// @Override
	// public CommandProcessor getCommandProcessor() {
	// 	return this.serverCmdProcessor;
	// }

	@Override
	public CommandQueue getConsumerQueue() {
		return this.consumerQueue;
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
	public Field getFocusedField() {
		return this.focusedField;
	}

	@Override
	public CommandStack getUndoStack() {
		return this.undoStack;
	}

	@Override
	public boolean isOwner(String name) {
		return model.getOwner().getUsername().equals(name);
	}

	@Override
	public void setConsumer(CommandQueue queue) {
		this.consumerQueue = queue;
	}

}

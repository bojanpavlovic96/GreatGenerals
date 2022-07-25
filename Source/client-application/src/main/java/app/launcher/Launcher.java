package app.launcher;

import java.util.ArrayList;
import java.util.List;

import app.form.StartForm;
import app.resource_manager.AppConfig;
import app.server.MockupGameServerProxy;
import app.server.MockupMsgInterpreter;

import controller.GameBrain;
import controller.command.CtrlInitializeCommand;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

import model.DataModel;
import model.PlayerModelData;
import model.component.field.ModelField;

import protocol.NamedWrapperTranslator;

import proxy.RabbitRoomServerProxy;
import proxy.RestLoginServerProxy;

import root.ActiveComponent;
import root.Point2D;
import root.command.Command;
import root.communication.GameServerProxy;
import root.communication.parser.GsonJsonParser;
import root.controller.Controller;
import root.model.Model;
import root.model.PlayerData;
import root.model.component.Field;
import root.model.component.Terrain;
import root.view.Color;
import root.view.View;
import view.DrawingStage;
import view.component.HexFieldManager;

public class Launcher extends Application {

	private Thread connectionThread;
	private RabbitConnectionTask connectionTask;

	private StartPageController startPageController;

	private Controller gameController;

	// init() is not on main (UI) thread
	// constructor -> init() -> start()
	@Override
	public void init() throws Exception {

	}

	// running on main (UI) thread
	@Override
	public void start(Stage primaryStage) throws Exception {
		// ignore generated primaryStage

		var brokerConfig = AppConfig.getInstance().brokerConfig.getActive();
		System.out.println("Using broker config: \n" + brokerConfig.toString());

		connectionTask = new RabbitConnectionTask(brokerConfig);

		var msgInterpreter = new MockupMsgInterpreter();

		var loginProxy = new RestLoginServerProxy(
				AppConfig.getInstance().restLoginServerConfig.getActive(),
				new GsonJsonParser());

		var protocolTranslator = new NamedWrapperTranslator(
				new GsonJsonParser(),
				new StupidStaticTypeResolver());

		var roomProxy = new RabbitRoomServerProxy(
				AppConfig.getInstance().rabbitRoomServerProxyConfig,
				connectionTask,
				protocolTranslator);

		startPageController = new StartPageController(new StartForm(),
				loginProxy,
				roomProxy,
				this::startTheGame);

		connectionThread = new Thread(this.connectionTask);
		connectionThread.start();

		startPageController.showInitialPage();
	}

	private void startTheGame(String username, String roomName) {

		// after the login replace username and roomName with some token 
		// provided by the loginServer

		System.out.println("Game ready handler called ... ");
		// GameServerProxy serverProxy = new RabbitGameServerProxy(
		// 		AppConfig.getInstance().rabbitServerProxyConfig,
		// 		connectionTask.getChannel(),
		// 		translator,
		// 		username,
		// 		roomName);

		var protocolTranslator = new NamedWrapperTranslator(
				new GsonJsonParser(),
				new StupidStaticTypeResolver());

		var msgInterpreter = new MockupMsgInterpreter();

		GameServerProxy serverProxy = new MockupGameServerProxy(
				AppConfig.getInstance().rabbitGameServerProxyConfig,
				protocolTranslator,
				msgInterpreter,
				username,
				roomName);

		var initCommand = getFakeInitCommand();
		serverProxy.getConsumerQueue().enqueue(initCommand);

		var viewConfig = AppConfig.getInstance().viewConfig;
		View view = new DrawingStage(
				new HexFieldManager(viewConfig.fieldHeight,
						viewConfig.fieldWidth,
						viewConfig.fieldBorderWidth),
				viewConfig);

		// attention controller still null at this moment
		// modelEventHandler is set from controller constructor
		// this is empty model (only timer and unit creator)
		Model model = new DataModel();

		gameController = new GameBrain(serverProxy, view, model);

		startPageController.hideInitialPage();
		gameController.getView().showView();

	}

	private Command getFakeInitCommand() {
		List<PlayerData> players = new ArrayList<PlayerData>();
		players.add(new PlayerModelData("user 1", Color.RED));
		players.add(new PlayerModelData("user 2", Color.GREEN));
		players.add(new PlayerModelData("user 3", Color.BLACK));

		List<Field> fieldModels = new ArrayList<Field>();

		int left = 3;
		int right = 17;

		int playerCounter = 0;

		for (int i = 1; i < 16; i++) {

			for (int j = left; j < right; j++) {
				if (i % 2 == 0 && j % 5 == 0) {
					fieldModels.add(new ModelField(
							new Point2D(j, i),
							players.get(playerCounter),
							true,
							null,
							new Terrain("mountains", 1)));
				} else {
					fieldModels.add(new ModelField(
							new Point2D(j, i),
							players.get(playerCounter),
							true,
							null,
							new Terrain("water", 1)));
				}

				playerCounter++;
				playerCounter %= 3;

			}

			if (left > -3)
				left--;
		}

		var cmd = new CtrlInitializeCommand(players, null);
		cmd.fields = fieldModels;
		return cmd;
	}

	@Override
	public void stop() throws Exception {
		super.stop();

		System.out.println("Calling application stop ... @ Launcher.start");

		// not every implementation of this components has to be activeCompoenent
		// that is why I am doing check with every component

		// close connection on shutdown
		if (connectionTask != null && connectionTask instanceof ActiveComponent) {
			System.out.println("Closing connection ... @ Launcher.stop");
			((ActiveComponent) this.connectionTask).shutdown();
		}

		if (gameController != null && gameController instanceof ActiveComponent) {
			System.out.println("Shutting down controller ... @ Launcher.stop");
			((ActiveComponent) this.gameController).shutdown();
		}

		if (startPageController != null && startPageController instanceof ActiveComponent) {
			((ActiveComponent) startPageController).shutdown();
		}

	}

	public static void main(String[] args) {
		Application.launch(args);
	}

}

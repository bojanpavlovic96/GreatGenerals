package app.launcher;

import app.form.StartForm;
import app.resource_manager.AppConfig;
import controller.GameBrain;
import controller.command.SwitchCaseMsgInterpreter;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

import model.DataModel;

import protocol.NamedWrapperTranslator;
import protocol.SwitchCaseTypeResolver;
import proxy.RabbitGameServerProxy;
import proxy.RabbitRoomServerProxy;
import proxy.RestLoginServerProxy;

import root.ActiveComponent;
import root.communication.GameServerProxy;
import root.communication.parser.GsonJsonParser;
import root.controller.Controller;
import root.model.Model;
import root.view.View;
import view.DrawingStage;
import view.component.HexFieldManager;

public class Launcher extends Application {

	private Thread connectionThread;
	private RabbitConnectionTask connectionTask;

	private StartPageController startPageController;

	private Controller gameController;

	@Override
	public void init() throws Exception {
		// init() is not on main (UI) thread
		// constructor -> init() -> start()
	}

	// running on main (UI) thread
	@Override
	public void start(Stage primaryStage) throws Exception {
		// ignore generated primaryStage

		var brokerConfig = AppConfig.getInstance().brokerConfig.getActive();
		System.out.println("Using broker config: \n" + brokerConfig.toString());

		connectionTask = new RabbitConnectionTask(brokerConfig);

		var loginProxy = new RestLoginServerProxy(
				AppConfig.getInstance().restLoginServerConfig.getActive(),
				new GsonJsonParser());

		var protocolTranslator = new NamedWrapperTranslator(
				new GsonJsonParser(),
				new SwitchCaseTypeResolver());

		var roomProxy = new RabbitRoomServerProxy(
				AppConfig.getInstance().rabbitRoomServerProxyConfig,
				connectionTask,
				protocolTranslator);

		startPageController = new StartPageController(new StartForm(),
				loginProxy,
				roomProxy,
				this::startTheGame);

		connectionThread = new Thread(this.connectionTask);
		connectionThread.setName("RabbitConnection thread ... ");
		connectionThread.start();

		startPageController.showInitialPage();
	}

	private void startTheGame(String username, String roomName) {

		System.out.println("Game ready handler called ... ");

		var protocolTranslator = new NamedWrapperTranslator(
				new GsonJsonParser(),
				new SwitchCaseTypeResolver());

		var msgInterpreter = new SwitchCaseMsgInterpreter();

		GameServerProxy serverProxy = new RabbitGameServerProxy(
				AppConfig.getInstance().rabbitGameServerProxyConfig,
				connectionTask,
				protocolTranslator,
				msgInterpreter,
				username,
				roomName);

		System.out.println("Game proxy initialized ... ");

		Model model = new DataModel();

		var viewConfig = AppConfig.getInstance().viewConfig;

		var fieldManager = new HexFieldManager(viewConfig.fieldHeight,
				viewConfig.fieldWidth,
				viewConfig.fieldBorderWidth);

		System.out.println("Creating drawing stage ... ");

		Platform.runLater(() -> {
			View view = new DrawingStage(fieldManager, viewConfig);
			System.out.println("Initialized View ... ");

			gameController = new GameBrain(serverProxy, view, model);
			System.out.println("Initialized Controller ... ");

			startPageController.hideInitialPage();
			gameController.getView().showView();
		});

	}

	// private Command getFakeInitCommand() {
	// 	List<PlayerData> players = new ArrayList<PlayerData>();
	// 	players.add(new PlayerModelData("user 1", Color.RED, 128, 256));
	// 	players.add(new PlayerModelData("user 2", Color.GREEN, 128, 256));
	// 	players.add(new PlayerModelData("user 3", Color.BLACK, 128, 256));

	// 	List<Field> fieldModels = new ArrayList<Field>();

	// 	int left = 3;
	// 	int right = 17;

	// 	int playerCounter = 0;

	// 	for (int i = 1; i < 16; i++) {

	// 		for (int j = left; j < right; j++) {
	// 			if (i % 2 == 0 && j % 5 == 0) {
	// 				fieldModels.add(new ModelField(
	// 						new Point2D(j, i),
	// 						players.get(playerCounter),
	// 						true,
	// 						null,
	// 						new Terrain("mountains", 1)));
	// 			} else {
	// 				fieldModels.add(new ModelField(
	// 						new Point2D(j, i),
	// 						players.get(playerCounter),
	// 						true,
	// 						null,
	// 						new Terrain("water", 1)));
	// 			}

	// 			playerCounter++;
	// 			playerCounter %= 3;

	// 		}

	// 		if (left > -3)
	// 			left--;
	// 	}

	// 	var cmd = new CtrlInitializeCommand(players, null);
	// 	cmd.fields = fieldModels;
	// 	return cmd;
	// }

	@Override
	public void stop() throws Exception {
		super.stop();

		System.out.println("Calling application stop ... @ Launcher.start");

		// not every implementation of this components has to be activeComponent
		// that is why I am doing check with every component

		// close connection on shutdown
		if (gameController != null && gameController instanceof ActiveComponent) {
			System.out.println("Shutting down controller ... @ Launcher.stop");
			((ActiveComponent) this.gameController).shutdown();
		}

		if (startPageController != null && startPageController instanceof ActiveComponent) {
			System.out.println("Shutting down start page controller ... @ Launcher.stop");
			((ActiveComponent) startPageController).shutdown();
		}

		if (connectionTask != null && connectionTask instanceof ActiveComponent) {
			System.out.println("Closing connection ... @ Launcher.stop");
			((ActiveComponent) this.connectionTask).shutdown();
		}

	}

	public static void main(String[] args) {
		Application.launch(args);
	}

}

package app.launcher;

import app.form.StartForm;
import app.resource_manager.AppConfig;
import app.resource_manager.StringRegistry;
import controller.GameBrain;
import controller.command.SwitchCaseMsgInterpreter;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

import model.GameModel;
import model.component.field.HexagonField;
import protocol.NamedWrapperTranslator;
import protocol.SwitchCaseTypeResolver;
import proxy.RabbitGameServerProxy;
import proxy.RabbitRoomServerProxy;
import proxy.RestLoginServerProxy;

import root.ActiveComponent;
import root.communication.parser.GsonJsonParser;
import root.controller.Controller;
import root.model.Model;
import root.model.PlayerData;
import root.model.event.ConcurrentExecutorTimer;
import root.view.View;
import view.DrawingStage;
import view.component.HexFieldManager;

public class Launcher extends Application {

	private Thread connectionThread;
	private RabbitConnectionTask connectionTask;

	private StartPageController startPageController;

	private Controller gameController;

	// @Override
	// public void init() throws Exception {
	// 	// init() is not on main (UI) thread
	// 	// constructor -> init() -> start()
	// }

	@Override
	public void start(Stage primaryStage) throws Exception {
		// Running on the main (UI) thread
		// ignore generated primaryStage

		var brokerConfig = AppConfig.getInstance().brokerConfig.getActive();

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

		var formConfig = AppConfig.getInstance().formConfig;
		var langConfig = AppConfig.getInstance().langConfig;

		var stringRegistry = new StringRegistry(langConfig);

		var startForm = new StartForm(formConfig, langConfig, stringRegistry);

		startPageController = new StartPageController(startForm,
				loginProxy,
				roomProxy,
				this::startTheGame);

		connectionThread = new Thread(this.connectionTask);
		connectionThread.setName("RabbitConnection thread ... ");
		connectionThread.start();

		startPageController.showInitialPage();
	}

	private void startTheGame(PlayerData player, String roomName) {

		System.out.println("Game ready handler called ... ");

		var protocolTranslator = new NamedWrapperTranslator(
				new GsonJsonParser(),
				new SwitchCaseTypeResolver());

		var msgInterpreter = new SwitchCaseMsgInterpreter();

		var serverProxy = new RabbitGameServerProxy(
				AppConfig.getInstance().rabbitGameServerProxyConfig,
				connectionTask,
				protocolTranslator,
				msgInterpreter,
				player.getUsername(),
				roomName);

		System.out.println("Game proxy initialized ... ");
		// Not sure if 3 is gonna be enough if separate thread is required for
		// each schedule call ... 
		// Then again currently I have only 3 "scheduleable" actions: move, build
		// and attack/defend
		var timer = new ConcurrentExecutorTimer(3);
		var fieldFactory = HexagonField.getFactory();
		Model model = new GameModel(player.getUsername(), timer, fieldFactory);

		System.out.println("Model created ... ");

		var viewConfig = AppConfig.getInstance().viewConfig;

		var fieldManager = new HexFieldManager(viewConfig.fieldHeight,
				viewConfig.fieldWidth,
				viewConfig.fieldBorderWidth);

		Platform.runLater(() -> {
			var view = new DrawingStage(fieldManager, viewConfig);
			System.out.println("View created ... ");

			gameController = new GameBrain(player,
					serverProxy,
					view,
					model,
					this::gameDoneHandler);

			System.out.println("Initialized Controller ... ");

			startPageController.hideInitialPage();
			if (startPageController instanceof ActiveComponent) {
				((ActiveComponent) startPageController).shutdown();
			}
			gameController.getView().showView();
		});

	}

	private void gameDoneHandler() {
		System.out.println("Game is done");
		System.out.println("Controller passed controll to Launcher");
		var room = startPageController.getRoomName();
		var pass = " "; // this should not be important
		startPageController.leaveRoomActionHandler(room, pass);
		// startPageController.getInitialPage().showRoomForm();
		startPageController.showInitialPage();
	}

	@Override
	public void stop() throws Exception {
		super.stop();

		System.out.println("Calling application stop ... @ Launcher.start");

		// Tot every implementation of this components has to be activeComponent.

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

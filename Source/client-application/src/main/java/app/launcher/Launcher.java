package app.launcher;

import app.form.StartForm;
import app.resource_manager.AppConfig;
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
import root.model.event.ConcExecutorTimer;
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

		var serverProxy = new RabbitGameServerProxy(
				AppConfig.getInstance().rabbitGameServerProxyConfig,
				connectionTask,
				protocolTranslator,
				msgInterpreter,
				username,
				roomName);

		System.out.println("Game proxy initialized ... ");
		// TODO not sure if 3 is gonna be enough if separate thread is required for
		// each schedule call ... 
		var timer = new ConcExecutorTimer(3);
		var fieldFactory = HexagonField.getFactory();
		Model model = new GameModel(timer, fieldFactory);

		System.out.println("Model created ... ");

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
			if (startPageController instanceof ActiveComponent) {
				((ActiveComponent) startPageController).shutdown();
			}
			gameController.getView().showView();
		});

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

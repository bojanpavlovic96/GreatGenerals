package app.launcher;

import java.util.ArrayList;
import java.util.List;

import app.form.StartForm;
import app.resource_manager.AppConfig;
import app.server.MockupGameServerProxy;
import app.server.MockupLoginServerProxy;
import controller.GameBrain;
import controller.command.CtrlInitializeCommand;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.DataModel;
import model.PlayerModelData;
import model.component.field.ModelField;
import protocol.NamedWrapperTranslator;
import proxy.RabbitGameServerProxy;
import root.ActiveComponent;
import root.command.Command;
import root.communication.GameServerProxy;
import root.communication.Translator;
import root.communication.parser.GsonJsonParser;
import root.communication.parser.StaticParser;
import root.controller.Controller;
import root.model.Model;
import root.model.PlayerData;
import root.model.component.Field;
import root.model.component.Terrain;
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
		if (brokerConfig != null) {
			System.out.println("Using broker config: \n" + brokerConfig.toString());
		} else {
			System.out.println("Exiting ... ");

			return;
		}

		connectionTask = new RabbitConnectionTask(brokerConfig);

		startPageController = new StartPageController(
				new StartForm(),
				new MockupLoginServerProxy(),
				// new RabbitLoginServerProxy(this.connectionTask, brokerConfig.queues),
				(String username, String roomName) -> { // game ready handler

					// TODO why am I subscribeing here ... ?
					// maybe to shutdown app in case of an error ... ?
					// if (!connectionTask.isConnected()) {
					// connectionTask.subscribeForEvents((connTask, eventType) -> {

					// });
					// }

					Translator translator = new NamedWrapperTranslator(
							new GsonJsonParser(),
							new StupidStaticTypeResolver());

					// TODO replace username and roomName with some token/key
					// obtained from the login server
					// GameServerProxy serverProxy = new RabbitGameServerProxy(
					// 		AppConfig.getInstance().rabbitServerProxyConfig,
					// 		connectionTask.getChannel(),
					// 		translator,
					// 		username,
					// 		roomName);

					GameServerProxy serverProxy = new MockupGameServerProxy(
							AppConfig.getInstance().rabbitServerProxyConfig,
							translator,
							username,
							roomName);

					// TODO remove when real gameServerProxy gets implemented
					var initCommand = getFakeInitCommand();
					serverProxy.getConsumerQueue().enqueue(initCommand);

					// TODO somehow initialize resource manager
					// resources could be obtained from the server
					// values passed to the hexFieldManager should also be extracted from
					// configuration
					View view = new DrawingStage(
							new HexFieldManager(80, 30, 2),
							AppConfig.getInstance().viewConfig);

					// attention controller still null at this moment
					// modelEventHandler is set from controller constructor
					// this is empty model (only timer and unit creator)
					Model model = new DataModel();

					gameController = new GameBrain(serverProxy, view, model);

					startPageController.hideInitialPage();
					gameController.getView().show();

				});

		this.connectionThread = new Thread(this.connectionTask);
		this.connectionThread.start();

		this.startPageController.showInitialPage();
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

		return new CtrlInitializeCommand(players, fieldModels);
	}

	@Override
	public void stop() throws Exception {
		super.stop();

		System.out.println("Calling application stop ... @ Launcher.start");

		// close connection on shutdown
		if (this.connectionTask != null) {
			System.out.println("Closing connection ... @ Launcher.stop");
			((ActiveComponent) this.connectionTask).shutdown();
		}

		if (this.gameController != null) {
			System.out.println("Shutting down controller ... @ Launcher.stop");
			((ActiveComponent) this.gameController).shutdown();
		}

		// startPageController is not active compontent
		// since the loginServerProxy is implemented
		// if (this.startPageController != null) {
		// System.out.println("Shutting down initial controller ... @ Launcher.stop");
		// ((ActiveComponent) this.startPageController).shutdown();
		// }

	}

	public static void main(String[] args) {
		Application.launch(args);
	}

}

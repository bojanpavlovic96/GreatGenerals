package app.form;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.rabbitmq.client.Channel;

import app.event.GameReadyEvent;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class InitialPage extends Stage {

	// set with setChannel when connection is established
	private Channel channel;

	// TODO height may be unused
	private double WIDTH = 200;
	private double HEIGHT = 400;

	private VBox main_containter;
	private Scene main_scene;

	private HeaderForm header_form;
	private UserForm user_form;
	private RoomForm room_form;

	private GameReadyEvent on_game_ready;

	private ExecutorService executor;

	private Runnable initChannelTask;

	// methods

	public InitialPage() {

		this.executor = Executors.newSingleThreadExecutor();

		this.initStage();

		this.initHeader();

		this.initUserForm();

		this.initRoomForm();

		// hide room form, only user form
		this.room_form.setVisible(false);

		this.setScene(this.main_scene);
	}

	private void initStage() {

		this.main_containter = new VBox();
		this.main_scene = new Scene(this.main_containter);

		// TODO commented just for testing, uncomment
		// next line removes title bar
		// this.initStyle(StageStyle.UNDECORATED);

		this.setResizable(false);

		this.main_containter.setAlignment(Pos.TOP_CENTER);
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();

		this.setWidth(this.WIDTH);
		// this.setHeight(this.HEIGHT);

		this.setX(dimension.getWidth() / 2 - this.WIDTH / 2);
		// TODO find better value
		this.setY(200);

	}

	private void initHeader() {

		this.header_form = new HeaderForm(this.WIDTH, 125);
		this.main_containter.getChildren().add(this.header_form);
		this.header_form.managedProperty();

	}

	private void initUserForm() {
		this.user_form = new UserForm();

		// both handlers only call appropriate method from initial page (show info/status message)
		FormMessageProducer messageProducer = (FormMessageProducer) this.user_form;
		messageProducer.setInfoMessageHandler(new FormMessageHandler() {

			public void execute(String message_name) {

				showInfoMessage(message_name);

			}

		});

		messageProducer.setStatusMessageHandler(new FormMessageHandler() {

			public void execute(String message_name) {

				showStatusMessage(message_name);

			}
		});

		this.user_form.setOnLoginHandler(new UserFormActionHandler() {

			public void execute(String username, String password) {

				if (channel != null) {

					executor.submit(new LoginRequestTask(channel, username, password));

				} else {
					showInfoMessage("please-wait-for-connection");
				}

			}
		});

		this.user_form.setOnRegisterHandler(new UserFormActionHandler() {

			public void execute(String username, String password) {

				if (channel != null) {

					executor.submit(new RegisterRequestTask(channel, username, password));

				} else {

					showInfoMessage("please-wait-for-connection");

				}

			}
		});

		this.main_containter.getChildren().add(this.user_form);
	}

	private void showInfoMessage(String message_name) {
		((MessageDisplay) this.header_form).showInfoMessage(message_name);
	}

	private void showStatusMessage(String message_name) {
		((MessageDisplay) this.header_form).showStatusMessage(message_name);
	}

	private void initRoomForm() {
		this.room_form = new RoomForm();
		this.main_containter.getChildren().add(this.room_form);
	}

	// public methods

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(final Channel new_channel) {

		channel = new_channel;

		// this method is called from connection thread
		Platform.runLater(new Runnable() {

			public void run() {

			}

		});

	}

	public GameReadyEvent getOnGameReady() {
		return on_game_ready;
	}

	public void setOnGameReady(GameReadyEvent on_game_ready) {
		this.on_game_ready = on_game_ready;
	}

}

package app.form;

import java.awt.Dimension;
import java.awt.Toolkit;

import app.event.FormMessageHandler;
import app.event.LanguageEvent;
import app.event.RoomFormActionHandler;
import app.event.UserFormActionHandler;
import app.resource_manager.StringResourceManager;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class StartForm extends Stage implements InitialPage {

	// attention height may be unused
	private double WIDTH = 200;
	// attention hard-coded, find better solution for height
	private double HEIGHT = 640;

	private VBox main_containter;
	private Scene main_scene;

	// page forms

	// messages, image and title
	private HeaderForm header_form;

	// username, password, login, register
	private UserForm user_form;

	// logout, roomName, roomPassword, createRoom, joinRoom, startGame,
	// listOfPlayers
	private RoomForm room_form;
	// version number, language
	private BottomForm bottom_form;

	// methods

	public StartForm() {

		this.initStage();

		this.initHeader();

		this.initUserForm();

		this.initRoomForm();

		this.initBottomForm();

		this.room_form.setVisible(false);
		// this.user_form.setVisible(false);

		this.setScene(this.main_scene);

	}

	private void initStage() {

		this.main_containter = new VBox();
		this.main_scene = new Scene(this.main_containter);

		// TODO commented just for testing, uncomment
		this.setTitle("Great generals");
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

		VBox.setVgrow(this.main_containter, Priority.NEVER);

	}

	private void initHeader() {

		this.header_form = new HeaderForm(this.WIDTH, 125);
		this.main_containter.getChildren().add(this.header_form);
		this.header_form.managedProperty();

	}

	private void initUserForm() {
		this.user_form = new UserForm();

		// both handlers only call appropriate method from initial page (show
		// info/status message)
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

		this.main_containter.getChildren().add(this.user_form);
	}

	private void initRoomForm() {
		this.room_form = new RoomForm();

		this.main_containter.getChildren().add(this.room_form);
	}

	private void initBottomForm() {

		this.bottom_form = new BottomForm();

		this.bottom_form.setLanguageEventHandler(new LanguageEvent() {

			public void execute(String new_language) {

				StringResourceManager.setLanguage(new_language);

				// TODO platform.runLater maybe

				((HasLabels) header_form).reloadLabels();
				((HasLabels) user_form).reloadLabels();
				((HasLabels) room_form).reloadLabels();
				((HasLabels) bottom_form).reloadLabels();

			}
		});

		this.main_containter.getChildren().add(this.bottom_form);

	}

	// public methods

	// messageDisplay interface

	public void showInfoMessage(final String message_name) {

		Platform.runLater(new Runnable() {

			public void run() {

				((MessageDisplay) header_form).showInfoMessage(message_name);

			}
		});

	}

	public void showStatusMessage(final String message_name) {

		Platform.runLater(new Runnable() {

			public void run() {

				((MessageDisplay) header_form).showStatusMessage(message_name);

			}
		});

	}

	public String getStatusMessage() {
		return ((MessageDisplay) this.header_form).getStatusMessage();
	}

	public String getInfoMessage() {
		return ((MessageDisplay) this.header_form).getInfoMessage();
	}

	// initial page interface

	// implement
	public void setOnLogoutHandler() {

	}

	public void setOnLoginHandler(UserFormActionHandler handler) {
		this.user_form.setOnLoginHandler(handler);
	}

	public void setOnRegisterHandler(UserFormActionHandler handler) {
		this.user_form.setOnRegisterHandler(handler);
	}

	public void setOnCreateRoomHandler(RoomFormActionHandler handler) {
		this.room_form.setOnCreateGroupHandler(handler);
	}

	public void setOnJoinRoomHandler(RoomFormActionHandler handler) {
		this.room_form.setOnJoinGroupHandler(handler);
	}

	// implement
	public void setOnStartGameHandler() {

	}

	public void showUserForm() {
		this.user_form.setVisible(true);
	}

	public void hideUserForm() {
		this.user_form.setVisible(false);
	}

	public void showRoomForm() {
		this.room_form.setVisible(true);
	}

	public void hideRoomForm() {
		this.room_form.setVisible(false);
	}

	public void setUsername(String username) {
		this.user_form.setUsername(username);
	}

	public void setUserPassword(String password) {
		this.user_form.setUserPassword(password);
	}

	@Override
	public String getUsername() {
		return this.user_form.getUsername();
	}

	@Override
	public String getPassword() {
		return this.user_form.getPassword();
	}

}

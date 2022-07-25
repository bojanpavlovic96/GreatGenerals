package app.form;

import java.awt.Dimension;
import java.awt.Toolkit;

import app.event.RoomFormActionHandler;
import app.event.UserFormActionHandler;
import app.resource_manager.Language;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class StartForm extends Stage implements InitialPage {

	private double WIDTH = 200;
	// attention height may be unused
	// attention hard-coded, find better solution for height
	private double HEIGHT = 640;

	private VBox mainContainer;
	private Scene mainScene;

	// page forms

	// messages, image and title
	private HeaderForm headerForm;

	// username, password, login, register
	private UserForm userForm;

	// logout, roomName, roomPassword, createRoom, joinRoom, startGame,
	// listOfPlayers
	private RoomForm roomForm;
	// version number, language
	private BottomForm bottomForm;

	// methods

	public StartForm() {

		this.initStage();

		this.initHeader();

		this.initUserForm();

		this.initRoomForm();

		this.initBottomForm();

		this.roomForm.setVisible(false);
		// this.user_form.setVisible(false);

		this.setScene(this.mainScene);

	}

	private void initStage() {

		this.mainContainer = new VBox();
		this.mainScene = new Scene(this.mainContainer);

		this.setTitle("Great generals");
		// next line removes title bar
		// this.initStyle(StageStyle.UNDECORATED);

		this.setResizable(false);

		this.mainContainer.setAlignment(Pos.TOP_CENTER);
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();

		System.out.println("Display dimensions: \n"
				+ "width: " + dimension.getWidth() + "\n"
				+ "height: " + dimension.getHeight() + "\n");

		this.setWidth(this.WIDTH);
		// this.setHeight(this.HEIGHT);

		this.setX(dimension.getWidth() / 2 - this.WIDTH / 2);
		// using dimension.getHeight() / 2 wont really work great
		// i don't know which display/monitor is gonna be used
		// sometimes it will uses dimensions of one and display on another ...
		this.setY(200);

		VBox.setVgrow(this.mainContainer, Priority.NEVER);

	}

	private void initHeader() {

		this.headerForm = new HeaderForm(this.WIDTH, 125);
		this.mainContainer.getChildren().add(this.headerForm);
		this.headerForm.managedProperty();

	}

	private void initUserForm() {
		this.userForm = new UserForm();

		// both handlers only call appropriate method from initial page
		// (show info/status message)
		this.userForm.setInfoMessageHandler((Language.MessageType message) -> {
			showInfoMessage(message);
		});

		this.userForm.setStatusMessageHandler((Language.MessageType message) -> {
			showStatusMessage(message);
		});

		this.mainContainer.getChildren().add(this.userForm);
	}

	private void initRoomForm() {
		this.roomForm = new RoomForm();

		this.mainContainer.getChildren().add(this.roomForm);
	}

	private void initBottomForm() {

		this.bottomForm = new BottomForm();

		this.mainContainer.getChildren().add(this.bottomForm);

	}

	// public methods

	// messageDisplay interface

	@Override
	public void showInfoMessage(Language.MessageType messageName) {
		((MessageDisplay) headerForm).showInfoMessage(messageName);
	}

	@Override
	public void showStatusMessage(Language.MessageType messageName) {
		((MessageDisplay) headerForm).showStatusMessage(messageName);
	}

	@Override
	public String getCurrentStatusMessage() {
		return ((MessageDisplay) this.headerForm).getCurrentStatusMessage();
	}

	@Override
	public String getCurrentInfoMessage() {
		return ((MessageDisplay) this.headerForm).getCurrentInfoMessage();
	}

	// initial page interface

	// implement
	@Override
	public void setOnLogoutHandler(UserFormActionHandler handler) {

	}

	@Override
	public void setOnLoginHandler(UserFormActionHandler handler) {
		this.userForm.setOnLoginHandler(handler);
	}

	@Override
	public void setOnRegisterHandler(UserFormActionHandler handler) {
		this.userForm.setOnRegisterHandler(handler);
	}

	@Override
	public void setOnCreateRoomHandler(RoomFormActionHandler handler) {
		this.roomForm.setOnCreateRoomHandler(handler);
	}

	@Override
	public void setOnJoinRoomHandler(RoomFormActionHandler handler) {
		this.roomForm.setOnJoinRoomHandler(handler);
	}

	// implement
	@Override
	public void setOnStartGameHandler() {

	}

	@Override
	public void showUserForm() {
		Platform.runLater(() -> {
			userForm.setVisible(true);
		});
	}

	@Override
	public void hideUserForm() {
		Platform.runLater(() -> {
			userForm.setVisible(false);
		});
	}

	@Override
	public void showRoomForm() {
		Platform.runLater(() -> {
			roomForm.setVisible(true);
		});
	}

	@Override
	public void hideRoomForm() {
		Platform.runLater(() -> {
			roomForm.setVisible(false);
		});
	}

	@Override
	public void setUsername(String username) {
		this.userForm.setUsername(username);
	}

	@Override
	public void setUserPassword(String password) {
		this.userForm.setUserPassword(password);
	}

	@Override
	public String getUsername() {
		return this.userForm.getUsername();
	}

	@Override
	public String getPassword() {
		return this.userForm.getPassword();
	}

	@Override
	public void showPage() {
		Platform.runLater(() -> {
			super.show();
		});
	}

	@Override
	public void hidePage() {
		Platform.runLater(() -> {
			super.hide();
		});
	}

}

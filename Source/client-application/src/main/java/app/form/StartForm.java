package app.form;

import java.awt.Dimension;
import java.awt.Toolkit;

import app.event.RoomFormActionHandler;
import app.event.StartGameEventHandler;
import app.event.UserFormActionHandler;
import app.resource_manager.LangConfig;
import app.resource_manager.Language;
import app.resource_manager.StringRegistry;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import root.communication.PlayerDescription;
import root.view.FormConfig;

public class StartForm extends Stage implements InitialPage, HasLabels {

	// width being hardcoded is semi-ok but height ... doesn't look good ... 
	private double WIDTH = 200;
	private double HEIGHT = 640;

	private FormConfig config;
	private LangConfig langConfig;

	private StringRegistry stringRegistry;

	private VBox mainContainer;
	private Scene mainScene;

	// messages, image and title
	private HeaderForm headerForm;

	// username, password, login, register
	private UserForm userForm;

	// logout, roomName, roomPassword, createRoom, joinRoom, listOfPlayers
	private RoomForm roomForm;

	private Language language;
	private Button startButton;
	private StartGameEventHandler onStartGame;

	// version number, language
	private BottomForm bottomForm;

	// methods

	public StartForm(FormConfig config,
			LangConfig langConfig,
			StringRegistry stringRegistry) {

		this.config = config;
		this.langConfig = langConfig;

		this.stringRegistry = stringRegistry;

		initStage();

		initHeader();

		initUserForm();
		userForm.setVisible(true);

		initRoomForm();
		roomForm.setVisible(false);

		initStartButton();

		initBottomForm();

		setScene(this.mainScene);
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
		this.setHeight(this.HEIGHT);

		this.setX(dimension.getWidth() / 2 - this.WIDTH / 2);
		// using dimension.getHeight() / 2 wont really work great
		// i don't know which display/monitor is gonna be used
		// sometimes it will uses dimensions of one and display on another ...
		this.setY(200);

		VBox.setVgrow(this.mainContainer, Priority.NEVER);

	}

	private void initHeader() {

		this.headerForm = new HeaderForm(config, stringRegistry, this.WIDTH, 125);
		this.mainContainer.getChildren().add(this.headerForm);
		this.headerForm.managedProperty();

	}

	private void initUserForm() {
		this.userForm = new UserForm(stringRegistry);

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
		this.roomForm = new RoomForm(stringRegistry);

		this.mainContainer.getChildren().add(this.roomForm);
	}

	private void initStartButton() {

		language = stringRegistry.getLanguage();
		startButton = new Button(language.startGame);
		// font = new Font(this.FONT_NAME, this.FONT_SIZE);
		// startButton.setFont(font);
		startButton.managedProperty().bind(startButton.visibleProperty());

		mainContainer.getChildren().add(startButton);

		VBox.setMargin(startButton, new Insets(10, 0, 0, 5));

		disableGameStart();
	}

	private void initBottomForm() {

		this.bottomForm = new BottomForm(config, langConfig, stringRegistry);

		this.mainContainer.getChildren().add(this.bottomForm);

	}

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

	@Override
	public void setOnLeaveRoomHandler(RoomFormActionHandler handler) {
		roomForm.setOnLeaveRoomHandler(handler);
	}

	@Override
	public void setOnStartGameHandler(StartGameEventHandler handler) {
		this.onStartGame = handler;
		startButton.setOnAction((ActionEvent event) -> {
			if (onStartGame != null) {
				onStartGame.handle();
			}
		});
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

	@Override
	public void addPlayer(PlayerDescription playerDesc) {
		roomForm.addPlayer(playerDesc);
	}

	@Override
	public void removePlayer(String player) {
		roomForm.removePlayer(player);
	}

	@Override
	public void clearPlayers() {
		roomForm.clearPlayers();
	}

	@Override
	public void showPlayers() {
		roomForm.showPlayers();
	}

	@Override
	public void hidePlayers() {
		roomForm.hidePlayers();
	}

	@Override
	public void enableGameStart() {
		startButton.setVisible(true);
	}

	@Override
	public void disableGameStart() {
		startButton.setVisible(false);
	}

	@Override
	public void loadLabels(Language newLanguage) {
		this.language = newLanguage;

		this.startButton.setText(this.language.startGame);
	}

	@Override
	public void disableCreateRoom() {
		roomForm.disableCreateRoom();
	}

	@Override
	public void enableCreateRoom() {
		roomForm.enableCreateRoom();
	}

	@Override
	public void disableJoinRoom() {
		roomForm.disableJoinRoom();
	}

	@Override
	public void enableJoinRoom() {
		roomForm.enableJoinRoom();
	}

	@Override
	public void enableLeaveRoom() {
		roomForm.enableLeaveRoom();
	}

	@Override
	public void disableLeaveRoom() {
		roomForm.disableLeaveRoom();
	}

}

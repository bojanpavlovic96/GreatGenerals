package app.form;

import app.event.FormMessageHandler;
import app.event.RoomFormActionHandler;
import app.resource_manager.Language;
import app.resource_manager.StringRegistry;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import root.communication.PlayerDescription;

public class RoomForm extends VBox implements FormMessageProducer, HasLabels {

	// Noto Sans CJK TC Black
	// Un Dotum Bold
	// Tlwg Typewriter Bold Oblique
	// Purisa Oblique
	// Latin Modern Mono 10 Italic
	// Laksaman
	// Latin Modern Roman 10 Italic
	// Norasi Italic
	// Un Pilgi

	private final String FONT_NAME = "Tlwg Typewriter Bold";
	private final int FONT_SIZE = 15;

	private StringRegistry stringRegistry;

	private Language language;

	private Button logoutBtn;

	private Label roomNameLb;
	private TextField roomNameTf;

	private Label roomPasswordLb;
	private PasswordField roomPasswordPf;

	private Button replayBtn;

	private Button createRoomBtn;
	private Button joinRoomBtn;
	private Button leaveRoomBtn;

	private Label playersLb;

	private ObservableList<String> players;
	private ListView<String> playersLW;

	// private VBox playersVb;
	// private ScrollPane playersScrollPane;

	// private Button startGameBtn;

	private FormMessageHandler onStatusMessage;
	private FormMessageHandler onInfoMessage;

	private Font font;

	private RoomFormActionHandler onReplay;

	private RoomFormActionHandler onCreateRoom;
	private RoomFormActionHandler onJoinRoom;
	private RoomFormActionHandler onLeaveRoom;

	// private GameReadyHandler onStartGame;

	public RoomForm(StringRegistry stringRegistry) {

		this.stringRegistry = stringRegistry;
		this.language = stringRegistry.getLanguage();

		this.setAlignment(Pos.TOP_CENTER);

		this.initForm();

		this.setHandlers();

	}

	private void initForm() {

		font = new Font(this.FONT_NAME, this.FONT_SIZE);

		// up-right-down-left
		this.setPadding(new Insets(10, 5, 10, 5));

		this.managedProperty().bind(this.visibleProperty());

		// buttons & inputs specific

		this.logoutBtn = new Button(this.language.logout);
		this.logoutBtn.setFont(this.font);
		// this will allow other components to rearrange when logoutBtn is hidden 
		this.logoutBtn.managedProperty().bind(logoutBtn.visibleProperty());

		this.replayBtn = new Button(this.language.replay);
		this.replayBtn.setFont(this.font);
		this.replayBtn.managedProperty().bind(replayBtn.visibleProperty());

		this.roomNameLb = new Label(this.language.roomName);
		this.roomNameLb.setFont(this.font);
		this.roomNameTf = new TextField();
		this.roomNameTf.setText("some"); // TODO remove

		this.roomPasswordLb = new Label(this.language.roomPassword);
		this.roomPasswordLb.setFont(this.font);
		this.roomPasswordPf = new PasswordField();
		this.roomPasswordPf.setText("room"); // TODO remove

		this.createRoomBtn = new Button(this.language.createRoom);
		this.createRoomBtn.setFont(this.font);
		this.createRoomBtn.managedProperty().bind(createRoomBtn.visibleProperty());

		this.joinRoomBtn = new Button(this.language.joinRoom);
		this.joinRoomBtn.setFont(this.font);
		this.joinRoomBtn.managedProperty().bind(joinRoomBtn.visibleProperty());

		this.leaveRoomBtn = new Button(this.language.leaveRoom);
		this.leaveRoomBtn.setFont(this.font);

		this.playersLb = new Label(this.language.playersInRoom);
		this.playersLb.setFont(this.font);

		// this.playersVb = new VBox();
		// this.playersVb.setVisible(false);
		// // attention next line is ignored
		// this.playersVb.setAlignment(Pos.TOP_CENTER);
		// this.playersVb.setPadding(new Insets(5, 0, 0, 10));
		// this.playersVb.setMinWidth(100);
		// this.playersVb.setMinHeight(100);

		// this.playersScrollPane = new ScrollPane(this.playersVb);
		// // next line actually aligns content horizontally
		// this.playersScrollPane.setFitToWidth(true);
		// this.playersScrollPane.setMinHeight(80);
		// this.playersScrollPane.setMaxHeight(70);

		this.players = FXCollections.observableArrayList();
		this.playersLW = new ListView<String>(players);
		this.playersLW.setVisible(false);
		this.playersLW.setEditable(true);
		this.playersLW.setPrefSize(100, 80);
		// this.playersLW.setMinWidth(20);
		// this.playersLW.setMinHeight(20);
		// this.playersLW.setStyle("-fx-background-color: #332299 ;");

		// this.startGameBtn = new Button(language.startGame);
		// this.startGameBtn.setFont(font);
		// this.startGameBtn.managedProperty().bind(startGameBtn.visibleProperty());

		// add components to container in the right order

		this.getChildren().add(this.logoutBtn);
		this.getChildren().add(this.replayBtn);

		this.getChildren().add(this.roomNameLb);
		this.getChildren().add(this.roomNameTf);

		this.getChildren().add(this.roomPasswordLb);
		this.getChildren().add(this.roomPasswordPf);

		this.getChildren().add(this.createRoomBtn);
		this.getChildren().add(this.joinRoomBtn);
		this.getChildren().add(this.leaveRoomBtn);

		this.getChildren().add(this.playersLb);

		// this.getChildren().add(this.playersScrollPane);
		this.getChildren().add(playersLW);

		// this.getChildren().add(this.startGameBtn);

		// disable starting game
		// enable it after you requirements are met 
		// this.disableStartingGame();

		// elements margins (up, right, down, left)
		VBox.setMargin(this.replayBtn, new Insets(10, 0, 10, 0));

		VBox.setMargin(this.roomNameTf, new Insets(2, 0, 5, 0));
		VBox.setMargin(this.roomPasswordPf, new Insets(2, 0, 5, 0));

		VBox.setMargin(this.createRoomBtn, new Insets(5, 0, 5, 0));
		VBox.setMargin(this.leaveRoomBtn, new Insets(10, 0, 0, 0));
		VBox.setMargin(this.playersLb, new Insets(10, 0, 0, 0));
		VBox.setMargin(this.playersLW, new Insets(10, 0, 0, 0));

		// VBox.setMargin(this.startGameBtn, new Insets(10, 0, 0, 5));

		stringRegistry.subscribeForLanguageChange(this);
	}

	private void setHandlers() {

		this.replayBtn.setOnAction((ActionEvent event) -> {
			if (onReplay != null) {
				onReplay.handleFormAction(getRoomName(), getRoomPassword());
			}
		});

		this.createRoomBtn.setOnAction((ActionEvent event) -> {
			if (onCreateRoom != null) {
				onCreateRoom.handleFormAction(getRoomName(), getRoomPassword());
			}
		});

		this.joinRoomBtn.setOnAction((ActionEvent event) -> {
			if (onJoinRoom != null) {
				onJoinRoom.handleFormAction(getRoomName(), getRoomPassword());
			}
		});

		this.leaveRoomBtn.setOnAction((ActionEvent event) -> {
			if (onLeaveRoom != null) {
				onLeaveRoom.handleFormAction(getRoomName(), "");
			}
		});

		// this.startGameBtn.setOnAction((ActionEvent event) -> {
		// 	if (onStartGame != null) {
		// 		onStartGame.execute(username, roomName);
		// 	}
		// });
	}

	// this is not "wrapped" in interface because startForm already extends
	// javafx.Stage
	// (startForm contains user and room form)
	// so it is already bound to specific implementation (javafx) ...
	// this high abstraction is not really required

	// public methods
	public String getRoomName() {
		return this.roomNameTf.getText();
	}

	public String getRoomPassword() {
		return this.roomPasswordPf.getText();
	}

	public void addPlayer(PlayerDescription playerDesc) {
		Platform.runLater(() -> {
			players.add(playerDesc.getUsername());
			playersLW.setItems(players);

			System.out.println("Player added to the list: " + playerDesc.getUsername());
		});

		// playersVb.getChildren().add(newLabel);
	}

	public void removePlayer(final String player) {
		Platform.runLater(() -> {
			players.remove(player);
		});
	}

	// this.playersVb.getChildren().removeIf(new Predicate<Node>() {
	// 	public boolean test(Node single_label) {
	// 		return ((Label) single_label).getText().equals(player);
	// 	}
	// });

	public void clearPlayers() {
		Platform.runLater(() -> {
			this.players.clear();
		});

		// this.playersVb.getChildren().clear();
	}

	public void showPlayers() {
		this.playersLb.setVisible(true);
		this.playersLW.setVisible(true);
	}

	public void hidePlayers() {
		this.playersLb.setVisible(false);
		this.playersLW.setVisible(false);
	}

	// public void disableStartingGame() {
	// 	this.startGameBtn.setVisible(false);
	// }

	// public void enableStartingGame() {
	// 	this.startGameBtn.setVisible(true);
	// }

	// implement

	public void setOnLogoutHandler() {

	}

	public void setOnReplayHandler(RoomFormActionHandler handler) {
		this.onReplay = handler;
	}

	public void setOnCreateRoomHandler(RoomFormActionHandler handler) {
		this.onCreateRoom = handler;
	}

	public void setOnJoinRoomHandler(RoomFormActionHandler handler) {
		this.onJoinRoom = handler;
	}

	public void setOnLeaveRoomHandler(RoomFormActionHandler handler) {
		this.onLeaveRoom = handler;
	}

	// public void setOnStartGameHandler(GameReadyHandler handler) {
	// 	this.onStartGame = handler;
	// }

	// hasLabels interface

	public void loadLabels(Language newLanguage) {

		this.language = newLanguage;

		this.logoutBtn.setText(this.language.logout);
		this.replayBtn.setText(this.language.replay);
		this.roomNameLb.setText(this.language.roomName);
		this.roomPasswordLb.setText(this.language.roomPassword);
		this.createRoomBtn.setText(this.language.createRoom);
		this.joinRoomBtn.setText(this.language.joinRoom);
		this.playersLb.setText(this.language.playersInRoom);
		// this.startGameBtn.setText(this.language.startGame);

	}

	// FormMessageProducer interface

	public void setStatusMessageHandler(FormMessageHandler handler) {
		onStatusMessage = handler;
	}

	public void setInfoMessageHandler(FormMessageHandler handler) {
		onInfoMessage = handler;
	}

	public FormMessageHandler getStatusMessageHandler() {
		return this.onStatusMessage;
	}

	public FormMessageHandler getInfoMessageHandler() {
		return this.onInfoMessage;
	}

	public void disableReplay() {
		this.replayBtn.setVisible(false);
	}

	public void enableReplay() {
		this.replayBtn.setVisible(true);
	}

	public void disableCreateRoom() {
		this.createRoomBtn.setVisible(false);
	}

	public void enableCreateRoom() {
		this.createRoomBtn.setVisible(true);
	}

	public void disableJoinRoom() {
		this.joinRoomBtn.setVisible(false);
	}

	public void enableJoinRoom() {
		this.joinRoomBtn.setVisible(true);
	}

	public void enableLeaveRoom() {
		this.leaveRoomBtn.setVisible(true);
	}

	public void disableLeaveRoom() {
		this.leaveRoomBtn.setVisible(false);
	}

}
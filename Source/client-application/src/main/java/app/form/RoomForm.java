package app.form;

import java.util.function.Predicate;

import app.event.FormMessageHandler;
import app.event.RoomFormActionHandler;
import app.resource_manager.Language;
import app.resource_manager.StringResourceManager;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class RoomForm
		extends VBox
		implements FormMessageProducer, HasLabels {

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

	private Language language;

	private Button logoutBtn;

	private Label toomNameLb;
	private TextField room_name_tf;

	private Label roomPasswordLb;
	private PasswordField room_password_pf;

	private Button createRoomBtn;
	private Button joinRoomBtn;

	private Label playersLb;

	private ScrollPane playersScrollPane;
	private VBox playersVb;

	private Button startGameBtn;

	private FormMessageHandler onStatusMessage;
	private FormMessageHandler onInfoMessage;

	private Font font;

	private RoomFormActionHandler on_create_room;
	private RoomFormActionHandler on_join_room;

	public RoomForm() {

		this.language = StringResourceManager.getLanguage();

		this.setAlignment(Pos.TOP_CENTER);

		this.initForm();

		this.setHandlers();

	}

	private void initForm() {

		this.font = new Font(this.FONT_NAME, this.FONT_SIZE);

		// up-right-down-left
		this.setPadding(new Insets(10, 5, 10, 5));

		this.managedProperty().bind(this.visibleProperty());

		// buttons & inputs specific

		this.logoutBtn = new Button(this.language.logout);
		this.logoutBtn.setFont(this.font);

		this.toomNameLb = new Label(this.language.roomName);
		this.toomNameLb.setFont(this.font);
		this.room_name_tf = new TextField();

		this.roomPasswordLb = new Label(this.language.roomPassword);
		this.roomPasswordLb.setFont(this.font);
		this.room_password_pf = new PasswordField();

		this.createRoomBtn = new Button(this.language.createRoom);
		this.createRoomBtn.setFont(this.font);

		this.joinRoomBtn = new Button(this.language.joinRoom);
		this.joinRoomBtn.setFont(this.font);

		this.playersLb = new Label(this.language.playersInRoom);
		this.playersLb.setFont(this.font);

		this.playersVb = new VBox();
		// attention next line is ignored
		this.playersVb.setAlignment(Pos.TOP_CENTER);
		this.playersVb.setPadding(new Insets(5, 0, 0, 10));

		this.playersScrollPane = new ScrollPane(this.playersVb);
		// next line actually aligns content horizontally
		this.playersScrollPane.setFitToWidth(true);
		this.playersScrollPane.setMinHeight(70);
		this.playersScrollPane.setMaxHeight(70);

		this.startGameBtn = new Button(this.language.startGame);
		this.startGameBtn.setFont(this.font);
		this.startGameBtn.managedProperty().bind(this.startGameBtn.visibleProperty());

		// test labels (start)

		// this.addPlayer("player 1");
		// this.addPlayer("player 2");
		// this.addPlayer("player 3");
		// this.addPlayer("player 4");
		// this.addPlayer("player 5");

		// test labels (end)

		// add components to container in the right order

		this.getChildren().add(this.logoutBtn);

		this.getChildren().add(this.toomNameLb);
		this.getChildren().add(this.room_name_tf);

		this.getChildren().add(this.roomPasswordLb);
		this.getChildren().add(this.room_password_pf);

		this.getChildren().add(this.createRoomBtn);
		this.getChildren().add(this.joinRoomBtn);

		this.getChildren().add(this.playersLb);

		this.getChildren().add(this.playersScrollPane);

		this.getChildren().add(this.startGameBtn);

		// disable starting game
		// enable it after server response about creating new room
		this.disableStartingGame();

		// elements margins (up, right, down, left)
		VBox.setMargin(this.room_name_tf, new Insets(2, 0, 5, 0));

		VBox.setMargin(this.room_password_pf, new Insets(2, 0, 5, 0));

		VBox.setMargin(this.createRoomBtn, new Insets(5, 0, 5, 0));

		VBox.setMargin(this.playersLb, new Insets(10, 0, 0, 0));

		VBox.setMargin(this.startGameBtn, new Insets(10, 0, 0, 5));
		
		StringResourceManager.subscribeForLanguageChange(this);
	}

	private void setHandlers() {
		this.createRoomBtn.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent event) {

				if (on_create_room != null) {
					on_create_room.handleFormAction(getRoomName(), getRoomPassword());
				}

			}

		});

		this.joinRoomBtn.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent event) {

				if (on_join_room != null) {
					on_join_room.handleFormAction(getRoomName(), getRoomPassword());
				}

			}
		});
	}

	// this is not "wrapped" in interface because startForm already extends
	// javafx.Stage
	// (startForm contains user and room form)
	// so it is already bound to specific implementation (javafx) ...
	// this high abstraction is not really required

	// public methods
	public String getRoomName() {
		return this.room_name_tf.getText();
	}

	public String getRoomPassword() {
		return this.room_password_pf.getText();
	}

	public void addPlayer(String new_player) {
		Label new_label = new Label(new_player);
		new_label.setFont(this.font);

		this.playersVb.getChildren().add(new_label);

	}

	public void removePlayer(final String player) {
		this.playersVb.getChildren().removeIf(new Predicate<Node>() {
			public boolean test(Node single_label) {
				return ((Label) single_label).getText().equals(player);
			}
		});
	}

	public void disableStartingGame() {
		this.startGameBtn.setVisible(false);
	}

	public void enableStartingGame() {
		this.startGameBtn.setVisible(true);
	}

	// action handlers
	// implement

	public void setOnLogoutHandler() {

	}

	public void setOnCreateGroupHandler(RoomFormActionHandler handler) {
		this.on_create_room = handler;
	}

	public void setOnJoinGroupHandler(RoomFormActionHandler handler) {
		this.on_join_room = handler;
	}

	// implement

	public void setOnStartGameHandler() {

	}

	// hasLabels interface

	public void loadLabels(Language newLanguage) {

		this.language = newLanguage;

		this.logoutBtn.setText(this.language.logout);
		this.toomNameLb.setText(this.language.roomName);
		this.roomPasswordLb.setText(this.language.roomPassword);
		this.createRoomBtn.setText(this.language.createRoom);
		this.joinRoomBtn.setText(this.language.joinRoom);
		this.playersLb.setText(this.language.playersInRoom);
		this.startGameBtn.setText(this.language.startGame);

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

	public FormMessageHandler getInfoMessageHanlder() {
		return this.onInfoMessage;
	}

}
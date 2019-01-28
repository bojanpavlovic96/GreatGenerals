package app.form;

import java.util.function.Predicate;

import app.event.FormMessageHandler;
import app.event.RoomFormActionHandler;
import app.resource_manager.StringResourceManager;
import javafx.application.Preloader.PreloaderNotification;
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

	private StringResourceManager string_manager;

	private Button logout_btn;

	private Label room_name_lb;
	private TextField room_name_tf;

	private Label room_password_lb;
	private PasswordField room_password_pf;

	private Button create_room_btn;
	private Button join_room_btn;

	private Label players_lb;

	private ScrollPane players_scroll_pane;
	private VBox players_vb;

	private Button start_game_btn;

	private FormMessageHandler on_status_message;
	private FormMessageHandler on_info_message;

	private Font font;

	private RoomFormActionHandler on_create_room;
	private RoomFormActionHandler on_join_room;

	public RoomForm() {

		this.string_manager = StringResourceManager.getInstance();

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

		this.logout_btn = new Button(this.string_manager.getString("logout"));
		this.logout_btn.setFont(this.font);

		this.room_name_lb = new Label(this.string_manager.getString("room-name"));
		this.room_name_lb.setFont(this.font);
		this.room_name_tf = new TextField();

		this.room_password_lb = new Label(this.string_manager.getString("room-password"));
		this.room_password_lb.setFont(this.font);
		this.room_password_pf = new PasswordField();

		this.create_room_btn = new Button(this.string_manager.getString("create-room"));
		this.create_room_btn.setFont(this.font);

		this.join_room_btn = new Button(this.string_manager.getString("join-room"));
		this.join_room_btn.setFont(this.font);

		this.players_lb = new Label(this.string_manager.getString("players"));
		this.players_lb.setFont(this.font);

		this.players_vb = new VBox();
		// attention next line is ignored
		this.players_vb.setAlignment(Pos.TOP_CENTER);
		this.players_vb.setPadding(new Insets(5, 0, 0, 10));

		this.players_scroll_pane = new ScrollPane(this.players_vb);
		// next line actually aligns content horizontally
		this.players_scroll_pane.setFitToWidth(true);
		this.players_scroll_pane.setMinHeight(70);
		this.players_scroll_pane.setMaxHeight(70);

		this.start_game_btn = new Button(this.string_manager.getString("start-game"));
		this.start_game_btn.setFont(this.font);
		this.start_game_btn.managedProperty().bind(this.start_game_btn.visibleProperty());

		// test labels (start)

		// this.addPlayer("player 1");
		// this.addPlayer("player 2");
		// this.addPlayer("player 3");
		// this.addPlayer("player 4");
		// this.addPlayer("player 5");

		// test labels (end)

		// add components to container in the right order

		this.getChildren().add(this.logout_btn);

		this.getChildren().add(this.room_name_lb);
		this.getChildren().add(this.room_name_tf);

		this.getChildren().add(this.room_password_lb);
		this.getChildren().add(this.room_password_pf);

		this.getChildren().add(this.create_room_btn);
		this.getChildren().add(this.join_room_btn);

		this.getChildren().add(this.players_lb);

		this.getChildren().add(this.players_scroll_pane);

		this.getChildren().add(this.start_game_btn);

		// disable starting game
		// enable it after server response about creating new room
		this.disableStartingGame();

		// elements margins (up, right, down, left)
		VBox.setMargin(this.room_name_tf, new Insets(2, 0, 5, 0));

		VBox.setMargin(this.room_password_pf, new Insets(2, 0, 5, 0));

		VBox.setMargin(this.create_room_btn, new Insets(5, 0, 5, 0));

		VBox.setMargin(this.players_lb, new Insets(10, 0, 0, 0));

		VBox.setMargin(this.start_game_btn, new Insets(10, 0, 0, 5));
	}

	private void setHandlers() {
		this.create_room_btn.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent event) {

				if (on_create_room != null) {
					on_create_room.execute(getRoomName(), getRoomPassword());
				}

			}

		});

		this.join_room_btn.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent event) {

				if (on_join_room != null) {
					on_join_room.execute(getRoomName(), getRoomPassword());
				}

			}
		});
	}

	// public methods
	// TODO wrap in interface maybe

	public String getRoomName() {
		return this.room_name_tf.getText();
	}

	public String getRoomPassword() {
		return this.room_password_pf.getText();
	}

	public void addPlayer(String new_player) {
		Label new_label = new Label(new_player);
		new_label.setFont(this.font);

		this.players_vb.getChildren().add(new_label);

	}

	public void removePlayer(final String player) {
		this.players_vb.getChildren().removeIf(new Predicate<Node>() {
			public boolean test(Node single_label) {
				return ((Label) single_label).getText().equals(player);
			}
		});
	}

	public void disableStartingGame() {
		this.start_game_btn.setVisible(false);
	}

	public void enableStartingGame() {
		this.start_game_btn.setVisible(true);
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

	public void reloadLabels() {

		this.string_manager = StringResourceManager.getInstance();

		this.logout_btn.setText(this.string_manager.getString("logout"));

		this.room_name_lb.setText(this.string_manager.getString("room-name"));

		this.room_password_lb.setText(this.string_manager.getString("room-password"));

		this.create_room_btn.setText(this.string_manager.getString("create-room"));

		this.join_room_btn.setText(this.string_manager.getString("join-room"));

		this.players_lb.setText(this.string_manager.getString("players"));

		this.start_game_btn.setText(this.string_manager.getString("start-game"));

	}

	// FormMessageProducer interface

	public void setStatusMessageHandler(FormMessageHandler handler) {
		on_status_message = handler;
	}

	public void setInfoMessageHandler(FormMessageHandler handler) {
		on_info_message = handler;
	}

	public FormMessageHandler getStatusMessageHandler() {
		return this.on_status_message;
	}

	public FormMessageHandler getInfoMessageHanlder() {
		return this.on_info_message;
	}

}

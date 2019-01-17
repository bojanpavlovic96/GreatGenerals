package app.form;

import java.util.function.Predicate;

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

public class RoomForm extends VBox implements FormMessageProducer, HasLabels {

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

	/*
	 * fields for handlers
	 * 
	 */

	public RoomForm() {

		this.string_manager = StringResourceManager.getInstance();

		this.setAlignment(Pos.TOP_CENTER);

		this.initForm();

		this.setHandlers();
	}

	private void initForm() {

		// stage specific
		this.setStyle("-fx-border-color: blue;\n" + "-fx-border-width: 2;");

		// up-right-down-left
		this.setPadding(new Insets(10, 5, 10, 5));

		this.managedProperty().bind(this.visibleProperty());

		// buttons & inputs specific

		this.logout_btn = new Button(this.string_manager.getString("logout"));

		this.room_name_lb = new Label(this.string_manager.getString("room-name"));
		this.room_name_tf = new TextField();

		this.room_password_lb = new Label(this.string_manager.getString("room-password"));
		this.room_password_pf = new PasswordField();

		this.create_room_btn = new Button(this.string_manager.getString("create-room"));

		this.join_room_btn = new Button(this.string_manager.getString("join-room"));

		this.players_lb = new Label(this.string_manager.getString("players"));

		this.players_vb = new VBox();
		// TODO next line is ignored
		this.players_vb.setAlignment(Pos.TOP_CENTER);
		this.players_vb.setPadding(new Insets(5, 0, 0, 10));

		this.players_scroll_pane = new ScrollPane(this.players_vb);
		// next line actually aligns content horizontally
		this.players_scroll_pane.setFitToWidth(true);
		this.players_scroll_pane.setMaxHeight(70);

		this.start_game_btn = new Button(this.string_manager.getString("start-game"));
		this.start_game_btn.managedProperty().bind(this.start_game_btn.visibleProperty());

		// test labels (start)

		Label label1 = new Label("label 1");
		Label label2 = new Label("label 2");
		Label label3 = new Label("label 3");
		Label label4 = new Label("label 4");
		Label label5 = new Label("label 5");
		Label label6 = new Label("label 6");

		this.players_vb.getChildren().add(label1);
		this.players_vb.getChildren().add(label2);
		this.players_vb.getChildren().add(label3);
		this.players_vb.getChildren().add(label4);
		this.players_vb.getChildren().add(label5);
		this.players_vb.getChildren().add(label6);

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
				// check does handler exists and call if it does

			}

		});

		this.join_room_btn.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent event) {
				// same as above
			}
		});
	}

	// public methods

	public String getRoomName() {
		return this.room_name_tf.getText();
	}

	public String getRoomPassword() {
		return this.room_password_pf.getText();
	}

	public void setOnCreateGroupHandler() {

	}

	public void setOnJoinGroupHandler() {

	}

	public void setOnStartGameHandler() {

	}

	public void addPlayer(String new_player) {

	}

	public void removePlayer(final String player) {
		this.players_vb.getChildren().removeIf(new Predicate<Node>() {
			public boolean test(Node single_label) {
				return ((Label) single_label).getText().equals(player);
			}
		});
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

package app.form;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class RoomForm extends VBox {

	StringResourceManager resource_manager;

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

	/*
	 * fields for handlers
	 * 
	 */

	public RoomForm() {

		this.resource_manager = StringResourceManager.getInstance();

		this.setAlignment(Pos.TOP_CENTER);

		this.initForm();

		this.setHandlers();
	}

	private void initForm() {

		this.logout_btn = new Button(this.resource_manager.getString("logout"));

		this.room_name_lb = new Label(this.resource_manager.getString("room-name"));
		this.room_name_tf = new TextField();

		this.room_password_lb = new Label(this.resource_manager.getString("room-password"));
		this.room_password_pf = new PasswordField();

		this.create_room_btn = new Button(this.resource_manager.getString("create-room"));
		this.join_room_btn = new Button(this.resource_manager.getString("join-room"));

		this.players_lb = new Label(this.resource_manager.getString("players"));

		this.players_vb = new VBox();
		this.players_vb.setAlignment(Pos.TOP_CENTER);
		this.players_vb.setPadding(new Insets(5, 0, 0, 10));

		this.players_scroll_pane = new ScrollPane(this.players_vb);
		this.players_scroll_pane.setFitToWidth(true);
		// this line actually aligns content horizontally

		// test labels

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

		this.players_scroll_pane.setMaxHeight(70);

		// test

		// add components to container

		this.getChildren().add(this.logout_btn);

		this.getChildren().add(this.room_name_lb);
		this.getChildren().add(this.room_name_tf);

		this.getChildren().add(this.room_password_lb);
		this.getChildren().add(this.room_password_pf);

		this.getChildren().add(this.create_room_btn);
		this.getChildren().add(this.join_room_btn);

		this.getChildren().add(this.players_lb);

		this.getChildren().add(this.players_scroll_pane);

		// up-right-down-left
		this.setPadding(new Insets(10, 5, 10, 5));

		VBox.setMargin(this.room_name_tf, new Insets(2, 0, 5, 0));
		VBox.setMargin(this.room_password_pf, new Insets(2, 0, 5, 0));

		VBox.setMargin(this.create_room_btn, new Insets(5, 0, 5, 0));

		VBox.setMargin(this.players_lb, new Insets(10, 0, 0, 0));
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
		// TODO Auto-generated method stub

	}

	public void setOnJoinGroupHandler() {
		// TODO Auto-generated method stub

	}

	public void setOnStartGameHandler() {

	}

	public void addPlayer(String new_user) {

	}

	public void removeUser(String user) {

	}

}

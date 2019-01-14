package app.form;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class UserForm extends VBox {

	StringResourceManager resource_manager;

	private Label username_lb;
	private TextField username_tf;

	private Label password_lb;
	private PasswordField password_pf;

	private Button login_btn;
	private Button register_btn;

	/*
	 * fields for handlers
	 * 
	 */

	public UserForm() {

		this.resource_manager = StringResourceManager.getInstance();

		this.setAlignment(Pos.TOP_CENTER);

		this.initForm();

		this.setHandlers();
	}

	private void initForm() {

		this.managedProperty().bind(this.visibleProperty());

		this.username_lb = new Label(this.resource_manager.getString("username"));
		this.username_tf = new TextField();

		this.password_lb = new Label(this.resource_manager.getString("password"));
		this.password_pf = new PasswordField();

		this.login_btn = new Button(this.resource_manager.getString("login"));
		this.register_btn = new Button(this.resource_manager.getString("register"));

		this.getChildren().add(this.username_lb);
		this.getChildren().add(this.username_tf);

		this.getChildren().add(this.password_lb);
		this.getChildren().add(this.password_pf);

		this.getChildren().add(this.login_btn);
		this.getChildren().add(this.register_btn);

		// up-right-down-left
		this.setPadding(new Insets(10, 5, 10, 5));

		VBox.setMargin(this.username_tf, new Insets(2, 0, 5, 0));
		VBox.setMargin(this.password_pf, new Insets(2, 0, 5, 0));

		VBox.setMargin(this.login_btn, new Insets(10, 0, 5, 0));

	}

	private void setHandlers() {
		this.login_btn.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent event) {
				// check does handler exists and call if it does

			}
		});

		this.register_btn.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent event) {
				// same as above
			}
		});
	}

	public String getUsername() {
		return this.username_tf.getText();
	}

	public String getPassword() {
		return this.password_pf.getText();
	}

	public void setOnLoginHandler() {
		// TODO Auto-generated method stub

	}

	public void setOnRegisterHandler() {
		// TODO Auto-generated method stub

	}

}

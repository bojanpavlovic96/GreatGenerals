package app.form;

import app.event.FormMessageHandler;
import app.event.UserFormActionHandler;
import app.resource_manager.StringResourceManager;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class UserForm
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

	private StringResourceManager string_manager;

	private Label username_lb;
	private TextField username_tf;

	private Label password_lb;
	private PasswordField password_pf;

	private Button loginBtn;
	private Button registerBtn;

	private UserFormActionHandler onLogin;
	private UserFormActionHandler onRegister;

	private FormMessageHandler onInfoMessage;
	private FormMessageHandler onStatusMessage;

	private Font font;

	// methods

	public UserForm() {

		this.string_manager = StringResourceManager.getInstance();

		this.setAlignment(Pos.TOP_CENTER);

		this.initForm();

		this.setHandlers();
	}

	private void initForm() {

		this.font = new Font(this.FONT_NAME, this.FONT_SIZE);

		this.managedProperty().bind(this.visibleProperty());

		this.username_lb = new Label(this.string_manager.getString("username"));
		this.username_lb.setFont(this.font);
		this.username_tf = new TextField();

		this.password_lb = new Label(this.string_manager.getString("password"));
		this.password_lb.setFont(this.font);
		this.password_pf = new PasswordField();

		this.loginBtn = new Button(this.string_manager.getString("login"));
		this.loginBtn.setFont(this.font);

		this.registerBtn = new Button(this.string_manager.getString("register"));
		this.registerBtn.setFont(this.font);

		this.getChildren().add(this.username_lb);
		this.getChildren().add(this.username_tf);

		this.getChildren().add(this.password_lb);
		this.getChildren().add(this.password_pf);

		this.getChildren().add(this.loginBtn);
		this.getChildren().add(this.registerBtn);

		// up-right-down-left
		this.setPadding(new Insets(10, 5, 10, 5));

		VBox.setMargin(this.username_tf, new Insets(2, 0, 5, 0));
		VBox.setMargin(this.password_pf, new Insets(2, 0, 5, 0));

		VBox.setMargin(this.loginBtn, new Insets(10, 0, 5, 0));

	}

	private void setHandlers() {
		this.loginBtn.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent event) {
				if (onLogin != null) {
					onLogin.handleFormAction(
							username_tf.getText(),
							password_pf.getText());
				} else {
					System.out.println("on_login handler is not set ..."
							+ "\t@ UserForm.loginBtnAction ...");
				}
			}

		});

		this.registerBtn.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent event) {
				if (onRegister != null) {
					onRegister.handleFormAction(
							username_tf.getText(),
							password_pf.getText());
				} else {
					System.out.println("on_register handler is not set ..."
							+ "\t@ UserForm.registerBtnAction ...");
				}

			}
		});
	}

	public String getUsername() {
		return this.username_tf.getText();
	}

	public String getPassword() {
		return this.password_pf.getText();
	}

	public void setUsername(String username) {
		this.username_tf.setText(username);
	}

	public void setUserPassword(String password) {
		this.password_pf.setText(password);
	}

	// login/register handlers

	public void setOnLoginHandler(UserFormActionHandler handler) {
		this.onLogin = handler;
	}

	public void setOnRegisterHandler(UserFormActionHandler handler) {
		onRegister = handler;

	}

	// messages handlers

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

	public void reloadLabels() {

		this.string_manager = StringResourceManager.getInstance();

		this.username_lb.setText(this.string_manager.getString("username"));

		this.password_lb.setText(this.string_manager.getString("password"));

		this.loginBtn.setText(this.string_manager.getString("login"));

		this.registerBtn.setText(this.string_manager.getString("register"));

	}

}

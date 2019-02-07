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

public class UserForm extends VBox implements FormMessageProducer, HasLabels {

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

	private Button login_btn;
	private Button register_btn;

	private UserFormActionHandler on_login;
	private UserFormActionHandler on_register;

	private FormMessageHandler on_info_message;
	private FormMessageHandler on_status_message;

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

		this.login_btn = new Button(this.string_manager.getString("login"));
		this.login_btn.setFont(this.font);

		this.register_btn = new Button(this.string_manager.getString("register"));
		this.register_btn.setFont(this.font);

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
				if (on_login != null) {
					on_login.execute(username_tf.getText(), password_pf.getText());
				} else {
					System.out.println("on_login handler is not set ..." + "\t@ UserForm.loginBtnAction ...");
				}
			}

		});

		this.register_btn.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent event) {
				if (on_register != null) {
					on_register.execute(username_tf.getText(), password_pf.getText());
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
		this.on_login = handler;
	}

	public void setOnRegisterHandler(UserFormActionHandler handler) {
		on_register = handler;

	}

	// messages handlers

	public void setStatusMessageHandler(FormMessageHandler hanlder) {
		on_status_message = hanlder;
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

	public void reloadLabels() {

		this.string_manager = StringResourceManager.getInstance();

		this.username_lb.setText(this.string_manager.getString("username"));

		this.password_lb.setText(this.string_manager.getString("password"));

		this.login_btn.setText(this.string_manager.getString("login"));

		this.register_btn.setText(this.string_manager.getString("register"));

	}

}

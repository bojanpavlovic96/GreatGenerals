package app.form;

import app.resource_manager.StringResourceManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class HeaderForm extends VBox implements MessageDisplay {

	// path relative to resources
	// TODO load path from some configuration file (or something like that) somehow
	private final String IMG_PATH = "/main_pic.jpg";

	private StringResourceManager string_manager;

	private Label status_message;

	private Label info_message;

	private double image_width;
	private double image_height;
	private ImageView image;

	private Label title;

	// methods

	public HeaderForm(double img_width, double img_height) {
		super();

		image_width = img_width;
		image_height = img_height;

		this.string_manager = StringResourceManager.getInstance();

		this.initForm();
	}

	private void initForm() {

		this.managedProperty().bind(this.visibleProperty());

		this.setAlignment(Pos.TOP_CENTER);

		// up-right-down-left
		this.setPadding(new Insets(5, 5, 0, 5));

		FormMessage first_message = this.string_manager.getMessage("waiting-for-server");

		this.status_message = new Label(first_message.getMessage());
		this.status_message.setStyle("-fx-background-color: " + first_message.getColor());

		this.info_message = new Label();

		this.image = new ImageView(new Image(this.IMG_PATH));
		this.image.setFitWidth(this.image_width);
		this.image.setFitHeight(this.image_height);

		// TODO change title bar font
		this.title = new Label(this.string_manager.getString("title"));
		this.title.setFont(new Font("Purisa Bold", 20));

		this.getChildren().add(this.status_message);
		this.getChildren().add(this.info_message);
		this.getChildren().add(this.image);
		this.getChildren().add(this.title);

	}

	public String getStatusMessage() {
		return this.status_message.getText();
	}

	public String getInfoMessage() {
		return this.info_message.getText();
	}

	public void showStatusMessage(String status_message_name) {
		FormMessage message = this.string_manager.getMessage(status_message_name);

		if (message != null) {
			this.status_message.setText(message.getMessage());
			this.status_message.setStyle("-fx-backround-color: " + message.getColor());
		} else {
			// just passed message with white background
			this.status_message.setText(status_message_name);
			this.status_message.setStyle("-fx-background-color: #111111;\n");
		}
	}

	public void showInfoMessage(String info_message_name) {
		FormMessage message = this.string_manager.getMessage(info_message_name);

		if (message != null) {
			this.info_message.setText(message.getMessage());
			this.info_message.setStyle("-fx-background-color: " + message.getColor());
		} else {
			// just passed message with white background
			this.info_message.setText(info_message_name);
			this.info_message.setStyle("-fx-backround-color: #111111");
		}
	}

}

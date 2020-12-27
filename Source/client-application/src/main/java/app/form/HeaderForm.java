package app.form;

import app.resource_manager.StringResourceManager;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.util.Duration;

public class HeaderForm extends VBox implements MessageDisplay, HasLabels {

	// path relative to resources
	// TODO load path from some configuration file (or something like that) somehow
	private final String IMG_PATH = "/battle_draw.jpg";
	private final int TITLE_FONT_SIZE = 20;
	private final String TITLE_FONT_NAME = "Tlwg Typewriter Bold";

	private final int MESSAGE_FONT_SIZE = 14;
	private final String MESSAGE_FONT_NAME = "Tlwg Typewriter Bold Oblique";

	private final String ALPHA_VALUE = "75";

	// Noto Sans CJK TC Black
	// Un Dotum Bold
	// Tlwg Typewriter Bold Oblique
	// Purisa Oblique
	// Latin Modern Mono 10 Italic
	// Laksaman
	// Latin Modern Roman 10 Italic
	// Norasi Italic
	// Un Pilgi

	private StringResourceManager stringManager;

	private Label statusMessage;
	private String status_name;

	private Label infoMessage;
	private String info_name;
	private PauseTransition info_message_timer;

	private double imageWidth;
	private double imageHeight;
	private ImageView image;

	private Label title;

	private Font titleFont;
	private Font message_font;

	// methods

	public HeaderForm(double img_width, double img_height) {
		super();

		imageWidth = img_width;
		imageHeight = img_height;

		this.stringManager = StringResourceManager.getInstance();

		this.info_message_timer = new PauseTransition(Duration.seconds(2));
		this.info_message_timer.setOnFinished(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent event) {
				infoMessage.setVisible(false);
				info_name = null;
			}

		});

		this.titleFont = new Font(this.TITLE_FONT_NAME, this.TITLE_FONT_SIZE);
		this.message_font = new Font(this.MESSAGE_FONT_NAME, this.MESSAGE_FONT_SIZE);

		this.initForm();
	}

	private void initForm() {

		this.managedProperty().bind(this.visibleProperty());

		this.setAlignment(Pos.TOP_CENTER);

		// up-right-down-left
		this.setPadding(new Insets(5, 5, 5, 5));

		FormMessage firstMessage = this.stringManager.getMessage("waiting-for-server");

		this.statusMessage = new Label(firstMessage.getMessage());
		this.statusMessage.setStyle("-fx-background-color: "
				+ firstMessage.getColor());
		this.statusMessage.setFont(this.message_font);

		this.infoMessage = new Label();
		this.infoMessage.setFont(this.message_font);

		this.image = new ImageView(
				new Image(this.IMG_PATH,
						this.imageWidth,
						this.imageHeight,
						false,
						false));
		this.image.setFitWidth(this.imageWidth);
		this.image.setFitHeight(this.imageHeight);

		this.title = new Label(this.stringManager.getString("title"));
		this.title.setFont(this.titleFont);

		this.getChildren().add(this.statusMessage);
		this.getChildren().add(this.infoMessage);
		this.getChildren().add(this.image);
		this.getChildren().add(this.title);

		VBox.setMargin(this.infoMessage, new Insets(2, 0, 0, 0));

		VBox.setMargin(this.image, new Insets(5, 0, 0, 0));

	}

	// MessageDisplay interface

	public String getCurrentStatusMessage() {
		return this.statusMessage.getText();
	}

	public String getCurrentInfoMessage() {
		return this.infoMessage.getText();
	}

	public void showStatusMessage(String status_message_name) {

		this.status_name = status_message_name;

		FormMessage message = this.stringManager.getMessage(status_message_name);

		if (message != null) {
			this.statusMessage.setText(message.getMessage());
			this.statusMessage.setStyle("-fx-background-color: " + message.getColor() + this.ALPHA_VALUE);
		} else {
			// just passed message with white background
			this.statusMessage.setText(status_message_name);
			this.statusMessage.setStyle("-fx-background-color: #111111" + this.ALPHA_VALUE + ";\n");
		}

	}

	public void showInfoMessage(String info_message_name) {

		this.info_name = info_message_name;

		FormMessage message = this.stringManager.getMessage(info_message_name);

		if (!this.infoMessage.isVisible()) {
			this.infoMessage.setVisible(true);
		}

		if (message != null) {
			this.infoMessage.setText(message.getMessage());
			this.infoMessage.setStyle("-fx-background-color: " + message.getColor() + this.ALPHA_VALUE);
		} else {
			// just passed message with white background
			this.infoMessage.setText("Unknown: " + info_message_name);
			this.infoMessage.setStyle("-fx-background-color: #aacc91" + this.ALPHA_VALUE);
		}

		this.info_message_timer.stop();
		this.info_message_timer.play();

	}

	// HasLabels interface

	public void reloadLabels() {

		this.stringManager = StringResourceManager.getInstance();

		this.statusMessage.setText(this.status_name);

		// start new 2s with info in new language
		if (this.infoMessage.isVisible()) {
			this.showInfoMessage(this.info_name);
		}

	}

}

package app.form;

import app.resource_manager.Language;
import app.resource_manager.MainConfig;
import app.resource_manager.StringResourceManager;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
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

	// Noto Sans CJK TC Black
	// Un Dotum Bold
	// Tlwg Typewriter Bold Oblique
	// Purisa Oblique
	// Latin Modern Mono 10 Italic
	// Laksaman
	// Latin Modern Roman 10 Italic
	// Norasi Italic
	// Un Pilgi

	private Language language;

	private Label statusMessage;
	private Language.MessageType statusName;

	private Label infoMessage;
	private Language.MessageType infoName;
	private PauseTransition infoMessageTimer;

	private double imageWidth;
	private double imageHeight;
	private ImageView image;

	private Label title;

	private Font titleFont;
	private Font messageFont;

	// methods

	public HeaderForm(double img_width, double img_height) {
		super();

		imageWidth = img_width;
		imageHeight = img_height;

		this.language = StringResourceManager.getLanguage();

		this.infoMessageTimer = new PauseTransition(Duration.seconds(2));
		this.infoMessageTimer.setOnFinished(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent event) {
				infoMessage.setVisible(false);
				infoName = null;
			}

		});
		var config = MainConfig.getInstance();
		this.titleFont = new Font(config.titleFont, config.titleFontSize);
		this.messageFont = new Font(config.messageFont, config.messageFontSize);

		this.initForm();
	}

	private void initForm() {

		this.managedProperty().bind(this.visibleProperty());

		this.setAlignment(Pos.TOP_CENTER);

		// up-right-down-left
		this.setPadding(new Insets(5, 5, 5, 5));

		this.statusMessage = new Label();
		this.statusMessage.setFont(this.messageFont);

		this.showStatusMessage(Language.MessageType.WaitingForServer);

		this.infoMessage = new Label();
		this.infoMessage.setFont(this.messageFont);

		var imagePath = MainConfig.getInstance().headerImagePath;
		this.image = new ImageView(
				new Image(imagePath,
						this.imageWidth,
						this.imageHeight,
						false,
						false));
		this.image.setFitWidth(this.imageWidth);
		this.image.setFitHeight(this.imageHeight);

		this.title = new Label(this.language.title);
		this.title.setFont(this.titleFont);

		this.getChildren().add(this.statusMessage);
		this.getChildren().add(this.infoMessage);
		this.getChildren().add(this.image);
		this.getChildren().add(this.title);

		VBox.setMargin(this.infoMessage, new Insets(2, 0, 0, 0));

		VBox.setMargin(this.image, new Insets(5, 0, 0, 0));

		StringResourceManager.subscribeForLanguageChange(this);
	}

	// MessageDisplay interface

	public String getCurrentStatusMessage() {
		return this.statusMessage.getText();
	}

	public String getCurrentInfoMessage() {
		return this.infoMessage.getText();
	}

	public void showStatusMessage(Language.MessageType statMessage) {

		this.statusName = statMessage;

		var messageObj = this.language.getMessage(statMessage);

		Platform.runLater(() -> {
			if (messageObj != null) {
				this.statusMessage.setText(messageObj.message);
				this.statusMessage.setStyle("-fx-background-color: "
						+ messageObj.color
						+ MainConfig.getInstance().headerAlphaValue);
			} else {
				// just passed message with white background
				this.statusMessage.setText("unknown - " + this.statusName);
				this.statusMessage.setStyle("-fx-background-color: #111111"
						+ MainConfig.getInstance().headerAlphaValue
						+ ";\n");
			}
		});

	}

	public void showInfoMessage(Language.MessageType infoMessageName) {

		this.infoName = infoMessageName;
		var messageObj = this.language.getMessage(infoMessageName);

		Platform.runLater(() -> {
			if (!this.infoMessage.isVisible()) {
				this.infoMessage.setVisible(true);
			}

			if (messageObj != null) {
				this.infoMessage.setText(messageObj.message);
				this.infoMessage.setStyle("-fx-background-color: "
						+ messageObj.color
						+ MainConfig.getInstance().headerAlphaValue);
			} else {
				// just passed message with white background
				this.infoMessage.setText("unknown - " + this.infoName);
				this.infoMessage.setStyle("-fx-background-color: #aacc91"
						+ MainConfig.getInstance().headerAlphaValue);
			}

			this.infoMessageTimer.stop();
			this.infoMessageTimer.play();

		});
	}

	@Override
	public void loadLabels(Language newLanguage) {

		this.language = newLanguage;

		Platform.runLater(() -> {
			if (this.statusName != null) {
				this.showStatusMessage(this.statusName);
			}

			// start new 2s with info in new language
			if (this.infoMessage.isVisible() && this.infoName != null) {
				this.showInfoMessage(this.infoName);
			}

		});

	}

}

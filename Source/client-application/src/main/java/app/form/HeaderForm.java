package app.form;

import app.resource_manager.Language;
import app.resource_manager.AppConfig;
import app.resource_manager.StringRegistry;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.util.Duration;
import root.view.FormConfig;

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

	private FormConfig config;

	private StringRegistry stringRegistry;

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

	public HeaderForm(FormConfig config,
			StringRegistry stringRegistry,
			double imgWidth,
			double imgHeight) {
		super();

		this.config = config;
		this.stringRegistry = stringRegistry;

		imageWidth = imgWidth;
		imageHeight = imgHeight;

		this.language = stringRegistry.getLanguage();

		var duration = Duration.seconds(config.infoMessageDuration);
		this.infoMessageTimer = new PauseTransition(duration);
		this.infoMessageTimer.setOnFinished((event) -> {
			infoMessage.setVisible(false);
			infoName = null;
		});
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

		// should not be set from here and current model is not really transparent 
		// about when connection is established 
		// this.showStatusMessage(Language.MessageType.WaitingForServer);

		this.infoMessage = new Label();
		this.infoMessage.setFont(this.messageFont);

		this.image = new ImageView(
				new Image(config.headerImagePath,
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

		stringRegistry.subscribeForLanguageChange(this);
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
						+ config.headerAlphaValue);
			} else {
				// just passed message with white background
				this.statusMessage.setText("unknown - " + this.statusName);
				this.statusMessage.setStyle("-fx-background-color: #111111"
						+ config.headerAlphaValue
						+ ";\n");
			}
		});

	}

	public void showInfoMessage(Language.MessageType infoMessageName) {

		this.infoName = infoMessageName;
		var messageObj = this.language.getMessage(infoMessageName);

		Platform.runLater(() -> {
			if (!infoMessage.isVisible()) {
				infoMessage.setVisible(true);
			}

			if (messageObj != null) {
				infoMessage.setText(messageObj.message);
				infoMessage.setStyle("-fx-background-color: "
						+ messageObj.color
						+ config.headerAlphaValue);
			} else {
				// just passed message with white background
				infoMessage.setText("unknown - " + this.infoName);
				infoMessage.setStyle("-fx-background-color: #aacc91"
						+ config.headerAlphaValue);
			}

			infoMessageTimer.stop();
			infoMessageTimer.play();

		});
	}

	@Override
	public void loadLabels(Language newLanguage) {

		this.language = newLanguage;

		// I don't think this should be wrapped inside the runLater
		// language change can only be triggered from the dropbox
		// that action is executed on the main thread so ... yeah 

		// Platform.runLater(() -> {
		if (this.statusName != null) {
			this.showStatusMessage(this.statusName);
		}

		// start new 2s with info in new language
		if (this.infoMessage.isVisible() && this.infoName != null) {
			this.showInfoMessage(this.infoName);
		}

		// });

	}

}

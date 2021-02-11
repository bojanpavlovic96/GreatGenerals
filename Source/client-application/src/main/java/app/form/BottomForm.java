package app.form;

import app.event.LanguageEvent;
import app.resource_manager.StringResourceManager;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class BottomForm extends HBox implements LanguageSwitcher {

	private final String ALPHA_VALUE = "a2";

	private final double LANG_MARGIN = 5;

	private final String color = "#993300";

	private StringResourceManager stringManager;

	private Label text;

	private ComboBox<String> langBox;

	private LanguageEvent on_language_change;

	public BottomForm() {

		this.stringManager = StringResourceManager.getInstance();

		this.initForm();

	}

	private void initForm() {

		this.setAlignment(Pos.BASELINE_LEFT);
		this.setPadding(new Insets(2, 2, 5, 2));
		this.setStyle("-fx-background-color:" + this.color + this.ALPHA_VALUE);

		this.text = new Label(this.stringManager.getString("bottom-text"));

		this.langBox = new ComboBox<String>();

		this.getChildren().add(this.text);
		this.getChildren().add(this.langBox);

		// before stage.show() all dimensions are 0 ...
		HBox.setMargin(this.langBox, new Insets(0, 0, 0, this.LANG_MARGIN));

		// if this is not wrapped with runLater text is not displayed properly ...
		Platform.runLater(() -> {
			text.setPrefWidth(getWidth() - langBox.getWidth() - LANG_MARGIN * 3);
		});

		// TODO set handler on language switch

	}

	public void setLanguageSwitchHandler(LanguageEvent handler) {
		this.on_language_change = handler;
	}

}

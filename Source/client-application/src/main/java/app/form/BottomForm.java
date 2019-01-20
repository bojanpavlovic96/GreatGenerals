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

	private StringResourceManager string_manager;

	private Label text;

	private ComboBox<String> lang_box;

	private LanguageEvent on_language_change;

	public BottomForm() {

		this.string_manager = StringResourceManager.getInstance();

		this.initForm();

	}

	private void initForm() {

		this.setAlignment(Pos.BASELINE_LEFT);
		this.setPadding(new Insets(2, 2, 5, 2));
		this.setStyle("-fx-background-color:" + this.color + this.ALPHA_VALUE);

		this.text = new Label(this.string_manager.getString("bottom-text"));

		this.lang_box = new ComboBox<String>();

		this.getChildren().add(this.text);
		this.getChildren().add(this.lang_box);

		HBox.setMargin(this.lang_box, new Insets(0, 0, 0, this.LANG_MARGIN));

		// before stage.show() all dimensions are 0 ...
		Platform.runLater(new Runnable() {

			public void run() {
				text.setPrefWidth(getWidth() - lang_box.getWidth() - LANG_MARGIN * 3);
			}

		});

		// TODO set handler on language change
		/**
		 * {if(this.onlanguage!=null) this.onlanguage.execute(language) }
		 *
		 */

	}

	public void setLanguageEventHandler(LanguageEvent handler) {
		this.on_language_change = handler;
	}

}

package app.form;

import app.resource_manager.Language;
import app.resource_manager.LangConfig;
import app.resource_manager.StringRegistry;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import root.view.FormConfig;

public class BottomForm extends HBox implements HasLabels {

	private final String ALPHA_VALUE = "a2";

	private final double LANG_MARGIN = 5;

	private final String color = "#993300";

	private FormConfig formConfig;
	private LangConfig langConfig;
	private StringRegistry stringRegistry;

	private Label versionLabel;
	private ComboBox<String> langBox;

	public BottomForm(FormConfig formConfig, 
		LangConfig langConfig, 
		StringRegistry stringRegistry) {

		this.formConfig = formConfig;
		this.langConfig = langConfig;
		this.stringRegistry = stringRegistry;

		this.initForm();
	}

	private void initForm() {

		this.setAlignment(Pos.BASELINE_LEFT);
		this.setPadding(new Insets(2, 2, 5, 2));
		this.setStyle("-fx-background-color:"
				+ this.color
				+ this.ALPHA_VALUE);

		this.langBox = new ComboBox<String>();

		this.versionLabel = new Label("");
		this.getChildren().add(this.versionLabel);
		this.getChildren().add(this.langBox);

		// before stage.show() all dimensions are 0 ...
		HBox.setMargin(this.langBox, new Insets(0, 0, 0, this.LANG_MARGIN));

		// if this is not wrapped with runLater text is not displayed properly ...
		Platform.runLater(() -> {
			versionLabel.setPrefWidth(getWidth() - langBox.getWidth() - LANG_MARGIN * 3);
		});

		// this is the only way to set onMousePressed handler ...
		this.langBox.setCellFactory(listView -> {
			var cell = new ListCell<String>() {
				@Override
				protected void updateItem(String item, boolean empty) {
					super.updateItem(item, empty);

					setText(empty ? null : item);
				}
			};

			cell.setOnMousePressed(e -> {
				if (!cell.isEmpty()) {
					System.out.println("Language switched to: " + cell.getText());
					stringRegistry.setLanguage(cell.getText());
				}
			});

			return cell;
		});

		this.langBox.getItems().addAll(langConfig.languages);
		this.langBox.setValue(langConfig.defaultLanguage);

		this.loadLabels(stringRegistry.getLanguage());

		stringRegistry.subscribeForLanguageChange(this);
	}

	@Override
	public void loadLabels(Language newLanguage) {
		Platform.runLater(() -> {
			this.versionLabel.setText(newLanguage.bottomText);
		});
	}

}

package app.form;

import java.util.List;

import app.resource_manager.Language;
import app.resource_manager.StringRegistry;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import root.communication.messages.GameDetails;
import root.view.FormConfig;

public class ReplayForm extends Stage implements HasLabels {

	private FormConfig config;
	private Language language;

	private VBox mainContainer;
	private Scene mainScene;

	private Label topLabel;
	private ObservableList<ReplayItem> items;
	private ListView<ReplayItem> itemsView;

	private ReplaySelectHandler onSelectHandler;
	private ReplayClosedHandler onCloseHandler;

	public ReplayForm(FormConfig config, StringRegistry stringReg) {
		this.config = config;
		language = stringReg.getLanguage();

		mainContainer = new VBox();
		mainContainer.setStyle("-fx-background-color: #ffffff");
		mainScene = new Scene(mainContainer);

		topLabel = new Label();
		itemsView = new ListView<ReplayItem>();

		mainContainer.getChildren().add(topLabel);
		mainContainer.getChildren().add(itemsView);

		loadLabels(language);

		this.setScene(mainScene);
	}

	@Override
	public void loadLabels(Language newLanguage) {
		if (items != null) {
			topLabel.setText(newLanguage.replays);
		} else {
			topLabel.setText(newLanguage.loadingReplays);
		}
	}

	public void populate(List<GameDetails> games) {
		Platform.runLater(() -> {
			topLabel.setText(language.replays);
			items = FXCollections.observableArrayList();

			for (var game : games) {
				System.out.println("Addding: " + game.gameId);
				items.add(new ReplayItem(language, game, onSelectHandler));
			}

			itemsView.setItems(items);
		});
	}

	@Override
	public void hide() {
		super.hide();
		if (items != null) {
			items.clear();
		}
		items = null;
	}

	public void close() {
		hide();
		if (onCloseHandler != null) {
			onCloseHandler.handle();
		}
	}

	public void setOnSelectHandler(ReplaySelectHandler handler) {
		this.onSelectHandler = handler;
	}

	public void setOnCloseHandler(ReplayClosedHandler handler) {
		this.onCloseHandler = handler;
	}

}

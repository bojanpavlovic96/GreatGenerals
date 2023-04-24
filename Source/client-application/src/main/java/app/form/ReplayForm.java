package app.form;

import java.util.List;

import app.resource_manager.Language;
import app.resource_manager.StringRegistry;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import root.communication.messages.GameDetails;
import root.view.FormConfig;

public class ReplayForm extends Stage implements HasLabels {

	private FormConfig config;
	private Language language;
	private Label topLabel;
	private ObservableList<ReplayItem> items;
	private ListView<ReplayItem> itemsView;

	private ReplaySelectHandler onSelectHandler;
	private ReplayClosedHandler onCloseHandler;

	public ReplayForm(FormConfig config, StringRegistry stringReg) {
		this.config = config;
		language = stringReg.getLanguage();

		topLabel = new Label();
		itemsView = new ListView<ReplayItem>();
		loadLabels(language);
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
		topLabel.setText(language.replays);
		items = FXCollections.observableArrayList();

		for (var game : games) {
			items.add(new ReplayItem(language, game, onSelectHandler));
		}

		itemsView.setItems(items);
	}

	@Override
	public void hide() {
		super.hide();
		items.clear();
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

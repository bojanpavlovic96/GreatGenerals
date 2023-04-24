package app.form;

import app.resource_manager.Language;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import root.communication.messages.GameDetails;

public class ReplayItem extends HBox implements HasLabels {

	private static final int MS_IN_MIN = 60000;
	private static final int MS_IN_SEC = 1000;
	private Language lang;
	private ReplaySelectHandler handler;
	private GameDetails details;

	public ReplayItem(Language language, GameDetails details, ReplaySelectHandler handler) {
		this.lang = language;
		this.details = details;
		this.handler = handler;

		var roomNameL = new Label(details.roomName);
		var masterL = new Label(details.master);
		var leftBox = new VBox(roomNameL, masterL);

		var durationL = new Label(formatDuration(details.msDuration));
		var winnerL = new Label(details.winner);
		var rightBox = new VBox(durationL, winnerL);

		getChildren().add(leftBox);
		getChildren().add(rightBox);

		setOnMouseClicked((MouseEvent e) -> {
			if (handler == null) {
				System.out.println("ReplaySelectHandler in replayItem not set ... ");
				return;
			}

			handler.handle(details.gameId);
		});

	}

	private String formatDuration(double msDur) {
		var mins = msDur / MS_IN_MIN;
		var secs = (msDur / MS_IN_MIN) / MS_IN_SEC;
		return mins + ":" + secs;
	}

	@Override
	public void loadLabels(Language newLanguage) {
		this.lang = newLanguage;
		// implement once labels next to the values are added 
	}

}

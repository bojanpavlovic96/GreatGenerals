package root.communication.messages;

import root.view.FormConfig;

public class GameDetails {

	public String gameId;

	public String roomName;
	public String master;
	public String winner;
	public double msDuration;
	public int pointsGain;

	public GameDetails() {
	}

	public GameDetails(String gameId,
			String roomName,
			String master,
			String winner,
			double msDuration,
			int pointsGain) {

		this.gameId = gameId;
		this.roomName = roomName;
		this.master = master;
		this.winner = winner;
		this.msDuration = msDuration;
		this.pointsGain = pointsGain;
	}

}

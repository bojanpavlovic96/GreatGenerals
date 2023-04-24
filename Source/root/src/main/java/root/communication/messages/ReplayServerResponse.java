package root.communication.messages;

import java.util.List;

public class ReplayServerResponse {

	public ReplayResponseStatus status;

	public List<GameDetails> games;

	public ReplayServerResponse(ReplayResponseStatus status) {
		this.status = status;
		this.games = null;
	}

	public ReplayServerResponse(ReplayResponseStatus status, List<GameDetails> games) {
		this.status = status;
		this.games = games;
	}

}

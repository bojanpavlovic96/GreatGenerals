package root.communication.messages;

import root.communication.PlayerDescription;

public class PlayerServerResponse {

	private PlayerServerResponseStatus status;

	private PlayerDescription player;

	public PlayerServerResponse(PlayerServerResponseStatus status,
			PlayerDescription player) {
		this.status = status;
		this.player = player;
	}

	public PlayerServerResponseStatus getStatus() {
		return status;
	}

	public PlayerDescription getPlayer() {
		return player;
	}

	public void setStatus(PlayerServerResponseStatus status) {
		this.status = status;
	}

	public void setPlayer(PlayerDescription player) {
		this.player = player;
	}

	public static PlayerServerResponse success(PlayerDescription data) {
		return new PlayerServerResponse(PlayerServerResponseStatus.SUCCESS, data);
	}

	public static PlayerServerResponse invalidUsername() {
		return new PlayerServerResponse(PlayerServerResponseStatus.UNKNOWN_USER, null);
	}

	public static PlayerServerResponse invalidToken() {
		return new PlayerServerResponse(PlayerServerResponseStatus.INVALID_TOKEN, null);
	}

	public static PlayerServerResponse failure() {
		return new PlayerServerResponse(PlayerServerResponseStatus.UNKNOWN_USER, null);
	}

}

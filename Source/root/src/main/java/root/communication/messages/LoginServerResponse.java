package root.communication.messages;

import root.communication.PlayerDescription;

public class LoginServerResponse {

	private LoginServerResponseStatus status;

	private PlayerDescription player;

	public LoginServerResponse(PlayerDescription playerData,
			LoginServerResponseStatus status) {

		this.player = playerData;
		this.status = status;
	}

	public PlayerDescription getPlayer() {
		return player;
	}

	public void setPlayer(PlayerDescription player) {
		this.player = player;
	}

	public LoginServerResponseStatus getStatus() {
		return this.status;
	}

	public void setStatus(LoginServerResponseStatus status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "{" +
				" username='" + player.getUsername() + "'" +
				", level='" + player.getLevel() + "'" +
				", points='" + player.getCoins() + "'" +
				", status='" + getStatus() + "'" +
				"}";
	}

	public static LoginServerResponse failed() {
		return new LoginServerResponse(null, LoginServerResponseStatus.SERVER_ERROR);
	}

}

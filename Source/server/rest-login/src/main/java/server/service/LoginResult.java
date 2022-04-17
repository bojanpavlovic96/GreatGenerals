package server.service;

import server.model.Player;

public class LoginResult {

	private LoginStatus loginStatus;

	private Player player;

	public LoginResult(LoginStatus loginStatus, Player player) {
		this.loginStatus = loginStatus;
		this.player = player;
	}

	public LoginStatus getLoginStatus() {
		return this.loginStatus;
	}

	public Player getPlayer() {
		return this.player;
	}

}

package server.service;

import server.model.Player;

public interface LoginHandler {
	void handle(LoginResult result, Player username);
}

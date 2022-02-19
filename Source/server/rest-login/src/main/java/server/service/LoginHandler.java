package server.service;

import server.model.Player;

public interface LoginHandler {
	void handle(LoginResponse result, Player username);
}

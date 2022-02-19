package server.service;

import org.springframework.beans.factory.annotation.Autowired;

import server.model.Player;
import server.model.PlayerRepository;

public class MockupLoginService implements LoginService {

	@Autowired
	private final PlayerRepository playerRepo;

	public MockupLoginService(PlayerRepository playerRepo) {
		this.playerRepo = playerRepo;
	}

	@Override
	public void validateUsername(String username, ValidationHandler handler) {
		handler.handle(ValidationResult.Valid);
	}

	@Override
	public void validatePassword(String password, ValidationHandler handler) {
		handler.handle(ValidationResult.Valid);
	}

	@Override
	public void register(String username, String password, LoginHandler handler) {
		handler.handle(LoginResult.Valid, new Player(username, "", 14, 10));
	}

	@Override
	public void login(String username, String password, LoginHandler handler) {
		handler.handle(LoginResult.Valid, new Player(username, "", 14, 10));
	}

}
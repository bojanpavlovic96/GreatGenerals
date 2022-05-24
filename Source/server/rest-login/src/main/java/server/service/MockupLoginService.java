package server.service;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import server.model.Player;
import server.model.PlayerRepository;

@Service(value = "MockupLoginService")
public class MockupLoginService implements LoginService {

	private final PlayerRepository playerRepo;

	// @Autowired
	// public MockupLoginService(
	// @Qualifier("MockupPlayerRepository") PlayerRepository playerRepo) {

	@Autowired
	public MockupLoginService(PlayerRepository playerRepo) {
		this.playerRepo = playerRepo;
	}

	@Override
	public CompletableFuture<ValidationResult> validateUsername(String username) {
		// var retValue = new CompletableFuture<ValidationResult>();
		// retValue.complete(ValidationResult.Valid);
		
		return CompletableFuture.completedFuture(ValidationResult.Valid);
	}

	@Override
	public CompletableFuture<ValidationResult> validatePassword(String password) {
		return CompletableFuture.completedFuture(ValidationResult.Valid);
	}

	@Override
	public CompletableFuture<LoginResult> register(String username, String password) {
		return CompletableFuture.completedFuture(new LoginResult(
				LoginStatus.Valid,
				new Player("", "", 10, 20)));
	}

	@Override
	public CompletableFuture<LoginResult> login(String username, String password) {
		return CompletableFuture.completedFuture(new LoginResult(
				LoginStatus.Valid,
				new Player("", "", 10, 20)));
	}

}
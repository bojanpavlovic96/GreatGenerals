package server.service;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
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

		if (playerRepo == null) {
			System.out.println("Player repo is null in mockup login service ... ");
		} else {
			System.out.println("Autowired playerRepo obtanied ... ");
		}
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
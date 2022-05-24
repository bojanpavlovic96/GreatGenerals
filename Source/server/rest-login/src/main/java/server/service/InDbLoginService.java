package server.service;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import server.model.Player;
import server.model.PlayerRepository;

@Primary
@Service(value = "InDbLoginService")
public class InDbLoginService implements LoginService {

	private PlayerRepository playerRepo;

	public InDbLoginService(@Autowired PlayerRepository playerRepo) {
		this.playerRepo = playerRepo;
	}

	@Override
	public CompletableFuture<ValidationResult> validateUsername(String username) {
		if (!hasGoodChars(username)) {
			return CompletableFuture.completedFuture(ValidationResult.InvalidCharacters);
		}

		if (!hasGoodLength(username)) {
			return CompletableFuture.completedFuture(ValidationResult.InvalidLength);
		}

		Player player = playerRepo.getByName(username);

		if (player == null) {
			return CompletableFuture.completedFuture(ValidationResult.Valid);
		} else {
			return CompletableFuture.completedFuture(ValidationResult.AlreadyInUse);
		}
	}

	@Override
	public CompletableFuture<ValidationResult> validatePassword(String password) {
		if (!hasGoodChars(password)) {
			return CompletableFuture.completedFuture(ValidationResult.InvalidCharacters);
		}

		if (!hasGoodLength(password)) {
			return CompletableFuture.completedFuture(ValidationResult.InvalidLength);
		}

		return CompletableFuture.completedFuture(ValidationResult.Valid);
	}

	@Override
	public CompletableFuture<LoginResult> login(String username, String password) {

		LoginResult result = null;

		if (!hasGoodChars(username) || !hasGoodLength(username)) {
			result = new LoginResult(LoginStatus.InvalidUsername, null);
		} else if (!hasGoodChars(password) || !hasGoodLength(password)) {
			result = new LoginResult(LoginStatus.InvalidPassword, null);
		} else {

			Player player = playerRepo.getByName(username);

			if (player == null) {
				result = new LoginResult(LoginStatus.NoSuchUser, null);
			} else {
				if (player.getPassword().equals(password)) {
					result = new LoginResult(LoginStatus.Valid, player);
				} else {
					result = new LoginResult(LoginStatus.WrongPassword, player);
				}
			}
		}

		return CompletableFuture.completedFuture(result);

	}

	@Override
	public CompletableFuture<LoginResult> register(String username, String password) {

		LoginResult result = null;

		if (!hasGoodChars(username) || !hasGoodLength(username)) {
			result = new LoginResult(LoginStatus.InvalidUsername, null);
		} else if (!hasGoodChars(password) || !hasGoodLength(password)) {
			result = new LoginResult(LoginStatus.InvalidPassword, null);
		} else {

			Player player = playerRepo.getByName(username);

			if (player != null) {
				result = new LoginResult(LoginStatus.UserAlreadyExists, null);
			} else {

				player = new Player(username, password, 0, 0);
				player = playerRepo.save(player);

				result = new LoginResult(LoginStatus.Valid, player);
			}
		}

		return CompletableFuture.completedFuture(result);
	}

	private boolean hasGoodChars(String username) {
		// TODO implement
		return true;
	}

	private boolean hasGoodLength(String password) {
		// TODO implement
		return true;
	}

}

package server.service;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import server.model.Player;
import server.model.PlayerRepository;

@Primary
@Service(value = "MysqlLoginService")
public class MysqlLoginService implements LoginService {

	private PlayerRepository playerRepo;

	public MysqlLoginService(@Autowired PlayerRepository playerRepo) {
		this.playerRepo = playerRepo;
	}

	@Override
	public CompletableFuture<ValidationResult> validateUsername(String username) {
		Player player = playerRepo.getByName(username);
		// TODO before returning, check if username is valid, length and used chars
		if (player == null) {
			return CompletableFuture.completedFuture(ValidationResult.Valid);
		} else {
			return CompletableFuture.completedFuture(ValidationResult.AlreadyInUse);
		}
	}

	@Override
	public CompletableFuture<ValidationResult> validatePassword(String password) {
		// TODO validate length and used chars
		return CompletableFuture.completedFuture(ValidationResult.Valid);
	}

	@Override
	public CompletableFuture<LoginResult> login(String username, String password) {

		Player player = playerRepo.getByName(username);

		LoginResult result = null;

		if (player == null) {
			result = new LoginResult(LoginStatus.NoSuchUser, null);
		} else {
			if (player.getPassword().equals(password)) {
				result = new LoginResult(LoginStatus.Valid, player);
			} else {
				result = new LoginResult(LoginStatus.InvalidPassword, player);
			}
		}

		return CompletableFuture.completedFuture(result);

	}

	@Override
	public CompletableFuture<LoginResult> register(String username, String password) {

		Player player = playerRepo.getByName(username);

		LoginResult result = null;
		if (player != null) {
			result = new LoginResult(LoginStatus.UserAlreadyExists, null);
		} else {

			// TODO validate username and password ...

			player = new Player(username, password, 0, 0);
			player = playerRepo.save(player);

			result = new LoginResult(LoginStatus.Valid, player);
		}

		return CompletableFuture.completedFuture(result);
	}

}

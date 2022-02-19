package server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import root.communication.messages.LoginRequest;
import root.communication.messages.LoginServerResponse;
import root.communication.messages.LoginServerResponseStatus;
import root.communication.messages.RegisterRequest;
import server.model.Player;
import server.model.PlayerRepository;
import server.service.LoginResult;
import server.service.LoginService;

public class LoginController {

	// @Autowired
	// private final PlayerRepository playerRepo;

	@Autowired
	private final LoginService loginService;

	// public LoginController(PlayerRepository playerRepo) {
	// this.playerRepo = playerRepo;
	// }

	public LoginController(LoginService loginService) {
		this.loginService = loginService;
	}

	@PostMapping("/login")
	public LoginServerResponse checkCredentials(@RequestBody LoginRequest request) {

		LoginServerResponse response = new LoginServerResponse();
		response.setUsername(request.getUsername());

		loginService.login(
				request.getUsername(),
				request.getPassword(),
				(result, player) -> {
					if (result == LoginResult.Valid) {
						response.setStatus(mapServiceResult(result));

						response.setLevel(player.getLevel());
						response.setPoints(player.getPoints());

					} else {

					}

					return response;
				});

		if (record == null) {
			response.setStatus(LoginServerResponseStatus.INVALID_NAME);
			response.setMessage("Player with this username does not exist ... ");
			// TODO read this string from configuration

			return response;
		}

		if (!request.getPassword().equals(record.getPassword())) {
			response.setStatus(LoginServerResponseStatus.INVALID_PASSWORD);
			response.setMessage("Wrong password ... ");
			// TODO read this string from configuration

			return response;
		}

		// user exists and the password is correct

		response.setLevel(record.getLevel());
		response.setPoints(record.getPoints());

		response.setStatus(LoginServerResponseStatus.SUCCESS);

		return response;
	}

	@PostMapping("/register")
	public LoginServerResponse registerPlayer(@RequestBody RegisterRequest request) {

		LoginServerResponse response = new LoginServerResponse();
		response.setUsername(request.getUsername());

		System.out.println("Handling request: " + request.toString());

		Player record = this.playerRepo.getByName(request.getUsername());

		if (record != null) {
			response.setStatus(LoginServerResponseStatus.INVALID_NAME);
			response.setMessage("This username is already taken ... ");
			// TODO read this string from configuration

			return response;
		}

		Player newPlayer = new Player(
				request.getUsername(),
				request.getPassword(),
				Player.translateToLevel(0),
				0);

		this.playerRepo.save(newPlayer);

		response.setStatus(LoginServerResponseStatus.SUCCESS);
		response.setLevel(newPlayer.getLevel());
		response.setPoints(newPlayer.getPoints());

		return response;
	}

	private LoginServerResponseStatus mapServiceResult(LoginResult loginResult) {
		switch (loginResult) {
		case Valid:
			return LoginServerResponseStatus.SUCCESS;
		case InvalidPassword:
			return LoginServerResponseStatus.INVALID_PASSWORD;
		case NoSuchUser:
			return LoginServerResponseStatus.INVALID_NAME;
		default:
			return LoginServerResponseStatus.SERVER_ERROR;

		}
	}

	// @PostMapping("/login")
	// public LoginServerResponse checkCredentials(@RequestBody LoginRequest
	// request) {

	// LoginServerResponse response = new LoginServerResponse();
	// response.setUsername(request.getUsername());

	// System.out.println("Handling request for: " + request.getUsername());

	// Player record = this.playerRepo.getByName(request.getUsername());

	// if (record == null) {
	// response.setStatus(LoginServerResponseStatus.INVALID_NAME);
	// response.setMessage("Player with this username does not exist ... ");
	// // TODO read this string from configuration

	// return response;
	// }

	// if (!request.getPassword().equals(record.getPassword())) {
	// response.setStatus(LoginServerResponseStatus.INVALID_PASSWORD);
	// response.setMessage("Wrong password ... ");
	// // TODO read this string from configuration

	// return response;
	// }

	// // user exists and the password is correct

	// response.setLevel(record.getLevel());
	// response.setPoints(record.getPoints());

	// response.setStatus(LoginServerResponseStatus.SUCCESS);

	// return response;
	// }

	// @PostMapping("/register")
	// public LoginServerResponse registerPlayer(@RequestBody RegisterRequest
	// request) {

	// LoginServerResponse response = new LoginServerResponse();
	// response.setUsername(request.getUsername());

	// System.out.println("Handling request: " + request.toString());

	// Player record = this.playerRepo.getByName(request.getUsername());

	// if (record != null) {
	// response.setStatus(LoginServerResponseStatus.INVALID_NAME);
	// response.setMessage("This username is already taken ... ");
	// // TODO read this string from configuration

	// return response;
	// }

	// Player newPlayer = new Player(
	// request.getUsername(),
	// request.getPassword(),
	// Player.translateToLevel(0),
	// 0);

	// this.playerRepo.save(newPlayer);

	// response.setStatus(LoginServerResponseStatus.SUCCESS);
	// response.setLevel(newPlayer.getLevel());
	// response.setPoints(newPlayer.getPoints());

	// return response;
	// }

}

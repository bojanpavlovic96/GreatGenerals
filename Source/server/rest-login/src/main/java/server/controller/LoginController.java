package server.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import root.communication.PlayerDescription;
import root.communication.messages.LoginRequest;
import root.communication.messages.LoginServerResponse;
import root.communication.messages.LoginServerResponseStatus;
import root.communication.messages.RegisterRequest;
import server.model.Player;
import server.model.PlayerRepository;
import server.service.LoginResult;
import server.service.LoginService;
import server.service.LoginStatus;

@RestController
public class LoginController {

	private final LoginService loginService;

	// if @Primary annotation is not used @Qualifier is necessary
	// this is the only way to inject the bean if multiple impl. are provided
	// also there is a xml way of doing this but ... it is xml
	// @Autowired
	// public LoginController(
	// @Qualifier("MockupLoginService") LoginService loginService,
	// @Qualifier("JpaPlayerRepository") PlayerRepository playerRepo) {

	public LoginController(LoginService loginService) {

		this.loginService = loginService;
	}

	@GetMapping("/ping")
	public LoginServerResponse ping() {
		return new LoginServerResponse(
				new PlayerDescription("ping_username", 128, 256),
				LoginServerResponseStatus.SUCCESS);
	}

	@PostMapping("/login")
	public LoginServerResponse login(@RequestBody LoginRequest request) {

		var username = request.getUsername();
		var password = request.getPassword();

		LoginServerResponse response = null;

		LoginResult loginResult = null;
		try {
			loginResult = loginService.login(username, password).get();
		} catch (Exception e) {
			// debug
			System.out.println("Exception while using login service... ");
			System.out.println(e.getMessage());
		}

		if (loginResult == null) {
			response = LoginServerResponse.failed();
		} else if (loginResult.getLoginStatus() != LoginStatus.Valid) {
			response = new LoginServerResponse(
					null,
					mapStatus(loginResult.getLoginStatus()));
		} else {
			response = new LoginServerResponse(
					mapPlayerData(loginResult.getPlayer()),
					mapStatus(LoginStatus.Valid));
		}

		return response;

	}

	@PostMapping("/register")
	public LoginServerResponse registerPlayer(@RequestBody RegisterRequest request) {

		LoginServerResponse response = null;

		LoginResult result = null;

		try {
			result = loginService.register(
					request.getUsername(),
					request.getPassword())
					.get();

		} catch (Exception e) {
			System.out.println("Exception while using login service... ");
			System.out.println(e.getMessage());
		}

		if (result == null) {
			response = LoginServerResponse.failed();
		} else if (result.getLoginStatus() != LoginStatus.Valid) {
			response = new LoginServerResponse(
					null,
					mapStatus(result.getLoginStatus()));
		} else {

			response = new LoginServerResponse(
					mapPlayerData(result.getPlayer()),
					mapStatus(LoginStatus.Valid));
		}

		return response;
	}

	private PlayerDescription mapPlayerData(Player dbPlayer) {
		return new PlayerDescription(dbPlayer.getName(),
				dbPlayer.getLevel(),
				dbPlayer.getPoints());
	}

	private LoginServerResponseStatus mapStatus(LoginStatus loginStatus) {
		switch (loginStatus) {
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

}

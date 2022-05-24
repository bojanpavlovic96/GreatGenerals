package server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import root.communication.messages.LoginRequest;
import root.communication.messages.LoginServerResponse;
import root.communication.messages.LoginServerResponseStatus;
import root.communication.messages.RegisterRequest;
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

	@Autowired
	public LoginController(LoginService loginService) {

		this.loginService = loginService;
	}

	@PostMapping("/login")
	public LoginServerResponse login(@RequestBody LoginRequest request) {

		var username = request.getUsername();
		var password = request.getPassword();

		var response = new LoginServerResponse();
		response.setUsername(username);

		LoginResult loginResult = null;
		try {
			loginResult = loginService.login(username, password).get();
		} catch (Exception e) {
			// debug
			System.out.println("Exception while using login service... ");
			System.out.println(e.getMessage());
		}

		if (loginResult == null || loginResult.getLoginStatus() != LoginStatus.Valid) {
			response.setStatus(mapStatus(loginResult.getLoginStatus()));
		} else {
			// success
			response.setStatus(mapStatus(loginResult.getLoginStatus()));

			response.setLevel(loginResult.getPlayer().getLevel());
			response.setPoints(loginResult.getPlayer().getPoints());
		}

		return response;

	}

	@PostMapping("/register")
	public LoginServerResponse registerPlayer(@RequestBody RegisterRequest request) {

		LoginServerResponse response = new LoginServerResponse();
		response.setUsername(request.getUsername());

		LoginResult result = null;

		try {
			result = loginService.register(
					request.getUsername(),
					request.getPassword())
					.get();
		} catch (Exception e) {
			System.out.println("Exception while useing login service... ");
			System.out.println(e.getMessage());
		}

		if (result == null || result.getLoginStatus() != LoginStatus.Valid) {
			response.setStatus(mapStatus(result.getLoginStatus()));
		} else {
			response.setStatus(mapStatus(result.getLoginStatus()));

			response.setLevel(result.getPlayer().getLevel());
			response.setPoints(result.getPlayer().getPoints());
		}

		return response;
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

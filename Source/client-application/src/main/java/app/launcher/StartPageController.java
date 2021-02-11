package app.launcher;

import java.util.List;
import java.util.Random;

import app.event.GameReadyHandler;
import app.event.RoomFormActionHandler;
import app.event.UserFormActionHandler;
import app.form.GameReadyEventProducer;
import app.form.InitialPage;
import app.form.MessageDisplay;
import root.ActiveComponent;
import root.communication.LoginServerProxy;
import root.communication.LoginServerResponseHandler;
import root.communication.messages.LoginRequest;
import root.communication.messages.LoginServerResponse;
import root.communication.messages.LoginServerResponseStatus;
import root.communication.messages.RegisterRequest;

/* Used for handling user actions from login/register/createRoom/joinRoom form */
public class StartPageController implements GameReadyEventProducer {

	private InitialPage initialPage;

	private GameReadyHandler onGameReady;

	// game data
	// user
	private String username;
	private String password;
	private boolean loggedIn;
	// room
	private String roomName;
	private String roomPassword;
	// also room
	private List<String> players;

	private LoginServerProxy loginServer;

	// methods

	public StartPageController(InitialPage initial_page,
			LoginServerProxy loginServer,
			GameReadyHandler onGameReady) {

		this.initialPage = initial_page;
		this.loginServer = loginServer;
		this.onGameReady = onGameReady;

		this.showInitialPage();

		this.initialPage.setOnLoginHandler(this::loginActionHandler);
		this.initialPage.setOnRegisterHandler(this::registerActionHandler);

		// TODO implement handlers for other actions

	}

	private void loginActionHandler(String _username, String _password) {
		System.out.println("Handling login action ... @ InitialController");

		username = _username;
		password = _password;

		if (loginServer != null && loginServer.isReady()) {
			loginServer.login(
					new LoginRequest(username, password),
					(response) -> {
						if (response.getStatus() == LoginServerResponseStatus.SUCCESS) {
							if (onGameReady != null) {

								// this should just switch to roomForm
								onGameReady.execute(
										response.getUsername(),
										"randomRoomName");
							}
						}
					});
			((MessageDisplay) initialPage).showInfoMessage("login-request-sent");
		} else {
			((MessageDisplay) initialPage).showInfoMessage("please-wait-for-connection");
		}

	}

	private void registerActionHandler(String username, String password) {
		System.out.println("Handling register action ... @ InitialController");

		if (loginServer != null && loginServer.isReady()) {
			loginServer.register(
					new RegisterRequest(username, password),
					new LoginServerResponseHandler() {

						@Override
						public void handleResponse(LoginServerResponse response) {
							if (response.getStatus() == LoginServerResponseStatus.SUCCESS) {
								if (onGameReady != null) {

									// this should just switch back to login form
									onGameReady.execute(
											response.getUsername(),
											"randomRoomName");
								}
							}
						}

					});
			((MessageDisplay) initialPage).showInfoMessage("register-request-sent");
		} else {
			((MessageDisplay) initialPage).showInfoMessage("please-wait-for-connection");
		}

	}

	public void hideInitialPage() {
		this.initialPage.hide();
	}

	public void showInitialPage() {
		this.initialPage.show();
	}

	// GameReadyEventProducer interface
	@Override
	public void setOnGameReadyHandler(GameReadyHandler handler) {
		this.onGameReady = handler;
	}

	@Override
	public GameReadyHandler getGameReadyHandler() {
		return this.onGameReady;
	}

}

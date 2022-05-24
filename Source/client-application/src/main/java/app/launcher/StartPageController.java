package app.launcher;

import java.util.List;

import app.event.GameReadyHandler;
import app.form.GameReadyEventProducer;
import app.form.InitialPage;
import app.form.MessageDisplay;
import app.resource_manager.Language;
import root.ActiveComponent;
import root.communication.LoginServerProxy;
import root.communication.messages.LoginRequest;
import root.communication.messages.LoginServerResponseStatus;
import root.communication.messages.RegisterRequest;

// Used for handling user actions from login/register/createRoom/joinRoom form
public class StartPageController implements GameReadyEventProducer, ActiveComponent {

	private InitialPage initialPage;

	private String username;
	private String password;
	private boolean loggedIn;

	private String roomName;
	private String roomPassword;

	private List<String> players;

	private LoginServerProxy loginServer;

	private GameReadyHandler onGameReady;

	public StartPageController(InitialPage initialPage,
			LoginServerProxy loginServer,
			GameReadyHandler onGameReady) {

		this.initialPage = initialPage;
		this.loginServer = loginServer;
		this.onGameReady = onGameReady;

		this.showInitialPage();

		this.initialPage.setOnLoginHandler(this::loginActionHandler);
		this.initialPage.setOnRegisterHandler(this::registerActionHandler);

	}

	private void loginActionHandler(String _username, String _password) {
		System.out.println("Handling login action ... @ InitialController");

		this.username = _username;
		this.password = _password;

		if (loginServer == null || !loginServer.isReady()) {
			showInfoMessage(Language.MessageType.PleaseWaitForConnection);
		}

		loginServer.login(new LoginRequest(username, password),
				(response) -> {
					if (response.getStatus() == LoginServerResponseStatus.SUCCESS) {
						if (onGameReady != null) {
							// onGameReady.execute("randomUsername", "randomRoomName");
							initialPage.hideUserForm();
							initialPage.showRoomForm();
						}
					} else {
						showInfoMessage(Language.MessageType.LoginFailed);
					}
				});

		showInfoMessage(Language.MessageType.LoginRequestSent);
	}

	private void registerActionHandler(String username, String password) {
		System.out.println("Handling register action ... @ InitialController");

		if (loginServer != null && loginServer.isReady()) {
			loginServer.register(
					new RegisterRequest(username, password),
					(response) -> {
						if (response.getStatus() == LoginServerResponseStatus.SUCCESS) {

							if (onGameReady != null) {

								// this should just switch back to login form
								onGameReady.execute(response.getUsername(),
										"randomRoomName");
							}
						}
					});
			showInfoMessage(Language.MessageType.RegisterRequestSent);
		} else {
			showInfoMessage(Language.MessageType.PleaseWaitForConnection);
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

	private void showInfoMessage(Language.MessageType infoName) {
		((MessageDisplay) initialPage).showInfoMessage(infoName);
	}

	@Override
	public void shutdown() {
		// because of the bug with the httpClient where client will hold thread 
		// pool alive even after the last window is closed 
		// shuttingDown custom threadPool doesn't work (but should be) anyways 
		// but ... if I (anyone) somehow find solution how to shutdown client this
		// interface implementation and this call propagation will be used 
		if (loginServer != null && loginServer instanceof ActiveComponent) {
			((ActiveComponent) loginServer).shutdown();
		}

	}

}

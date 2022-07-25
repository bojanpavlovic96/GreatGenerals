package app.launcher;

import java.util.List;

import app.event.GameReadyHandler;
import app.form.GameReadyEventProducer;
import app.form.InitialPage;
import app.form.MessageDisplay;
import app.resource_manager.Language;
import root.ActiveComponent;
import root.communication.LoginServerProxy;
import root.communication.RoomServerProxy;
import root.communication.messages.JoinResponseMsg;
import root.communication.messages.JoinResponseType;
import root.communication.messages.LoginRequest;
import root.communication.messages.LoginServerResponse;
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
	private RoomServerProxy roomServer;

	private GameReadyHandler onGameReady;

	public StartPageController(InitialPage initialPage,
			LoginServerProxy loginServer,
			RoomServerProxy roomServer,
			GameReadyHandler onGameReady) {

		this.initialPage = initialPage;
		this.loginServer = loginServer;
		this.roomServer = roomServer;
		this.onGameReady = onGameReady;

		this.showInitialPage();

		this.initialPage.setOnLoginHandler(this::loginActionHandler);
		this.initialPage.setOnRegisterHandler(this::registerActionHandler);

		this.initialPage.setOnCreateRoomHandler(this::createRoomActionHandler);
		this.initialPage.setOnJoinRoomHandler(this::joinRoomActionHandler);

	}

	private void loginActionHandler(String _username, String _password) {
		System.out.println("Handling login action ... @ InitialController");

		this.username = _username;
		this.password = _password;

		if (loginServer == null || !loginServer.isReady()) {
			System.out.println("Login server not available at the moment ... ");
			showInfoMessage(Language.MessageType.PleaseWaitForConnection);
			return;
		}

		loginServer.login(new LoginRequest(username, password),
				(response) -> {
					if (response.getStatus() == LoginServerResponseStatus.SUCCESS) {
						System.out.println("Login successful ... ");
						initialPage.hideUserForm();
						initialPage.showRoomForm();
					} else {
						System.out.println("Login failed ... ");
						showInfoMessage(Language.MessageType.LoginFailed);
					}
				});

		showInfoMessage(Language.MessageType.LoginRequestSent);

		return;
	}

	private void registerActionHandler(String username, String password) {
		System.out.println("Handling register action ... @ InitialController");

		if (loginServer == null || !loginServer.isReady()) {
			System.out.println("Login server not available at the moment ... ");
			showInfoMessage(Language.MessageType.PleaseWaitForConnection);
			return;
		}

		loginServer.register(
				new RegisterRequest(username, password),
				(LoginServerResponse response) -> {
					if (response.getStatus() == LoginServerResponseStatus.SUCCESS) {
						System.out.println("Successful registration ... ");
						// when you get registered you automatically get loggedin
						// because why not ... 
						// next page should be the room page

						initialPage.hideUserForm();
						initialPage.showRoomForm();

					} else {
						System.out.println("Failed to register: "
								+ response.getStatus().toString());
						return;
					}
				});

		showInfoMessage(Language.MessageType.RegisterRequestSent);

		return;
	}

	private void createRoomActionHandler(String roomName, String roomPassword) {
		if (roomServer == null || !roomServer.isReady()) {
			System.out.println("RoomServerProxy is not ready or already in use ... ");
			showInfoMessage(Language.MessageType.PleaseWaitForRoomServer);
			return;
		}

		System.out.println("room server is ready ... will try to create room ... ");

		roomServer.CreateRoom(roomName, roomPassword, username,
				(JoinResponseMsg response) -> {
					System.out.println("Handling room response ... ");
					if (response.responseType == JoinResponseType.Success) {
						System.out.println("Create room successful ... ");
						showInfoMessage(Language.MessageType.RoomCreated);

						System.out.println("Staring game ... ");
						onGameReady.execute(username, roomName);

					} else if (response.responseType == JoinResponseType.InvalidRoom) {
						System.out.println("Requested room already exists ... ");
						showInfoMessage(Language.MessageType.RoomExists);

					} else if (response.responseType == JoinResponseType.AlreadyIn) {
						System.out.println("You are already in this room ... ");
						showInfoMessage(Language.MessageType.AlreadyInRoom);
					}

				});

		showInfoMessage(Language.MessageType.CreateRequestSent);
	}

	private void joinRoomActionHandler(String roomName, String roomPassword) {

	}

	public void hideInitialPage() {
		this.initialPage.hidePage();
	}

	public void showInitialPage() {
		this.initialPage.showPage();
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

		if (roomServer != null && roomServer instanceof ActiveComponent) {
			((ActiveComponent) roomServer).shutdown();
		}

	}

}

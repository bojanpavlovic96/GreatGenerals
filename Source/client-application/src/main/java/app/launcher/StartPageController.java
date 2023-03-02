package app.launcher;

import java.util.ArrayList;
import java.util.List;

import app.event.GameReadyHandler;
import app.form.GameReadyEventProducer;
import app.form.InitialPage;
import app.form.MessageDisplay;
import app.resource_manager.Language;
import app.resource_manager.Language.MessageType;
import root.ActiveComponent;
import root.communication.LoginServerProxy;
import root.communication.PlayerDescription;
import root.communication.RoomServerProxy;
import root.communication.messages.RoomResponseMsg;
import root.communication.messages.RoomResponseType;
import root.communication.messages.LoginRequest;
import root.communication.messages.LoginServerResponse;
import root.communication.messages.LoginServerResponseStatus;
import root.communication.messages.RegisterRequest;

// Used for handling user actions from login/register/createRoom/joinRoom form
public class StartPageController implements GameReadyEventProducer, ActiveComponent {

	private InitialPage initialPage;

	private PlayerDescription player;

	private String roomName;

	private List<PlayerDescription> players;

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

		this.players = new ArrayList<PlayerDescription>();

		this.showInitialPage();

		initialPage.setOnLoginHandler(this::loginActionHandler);
		initialPage.setOnRegisterHandler(this::registerActionHandler);

		initialPage.setOnCreateRoomHandler(this::createRoomActionHandler);
		initialPage.setOnJoinRoomHandler(this::joinRoomActionHandler);

		initialPage.setOnStartGameHandler(this::startGameActionHandler);

		initialPage.setOnLeaveRoomHandler(this::leaveRoomActionHandler);

	}

	private void loginActionHandler(String username, String password) {
		System.out.println("Handling login action ... @ InitialController");

		if (loginServer == null || !loginServer.isReady()) {
			System.out.println("Login server not available at the moment ... ");
			showInfoMessage(Language.MessageType.PleaseWaitForConnection);
			return;
		}

		loginServer.login(new LoginRequest(username, password),
				(LoginServerResponse response) -> {
					if (response.getStatus() == LoginServerResponseStatus.SUCCESS) {
						System.out.println("Login successful ... ");

						player = response.getPlayer();

						initialPage.hideUserForm();
						initialPage.showRoomForm();
						initialPage.disableLeaveRoom();

						showInfoMessage(Language.MessageType.LoginSuccessful);
						showStatusMessage(Language.MessageType.LoginSuccessful);

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
						// When you get registered you automatically get logged in.
						// Next page should be the room page

						player = response.getPlayer();

						initialPage.hideUserForm();
						initialPage.showRoomForm();

						showInfoMessage(MessageType.RegisterSuccessful);
						showStatusMessage(MessageType.LoginSuccessful);

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

		roomServer.CreateRoom(roomName,
				roomPassword,
				player.getUsername(),
				this::createRoomResponseHandler);

		showInfoMessage(Language.MessageType.CreateRequestSent);
	}

	private void createRoomResponseHandler(RoomResponseMsg response) {
		System.out.println("Handling room response ... ");
		if (response.responseType == RoomResponseType.Success) {
			System.out.println("Create room successful ... ");

			initialPage.disableCreateRoom();
			initialPage.disableJoinRoom();

			initialPage.enableLeaveRoom();

			this.roomName = response.roomName;
			this.players.add(response.players.get(0));

			initialPage.showPlayers();
			System.out.println("Will add player to the list ... ");
			initialPage.addPlayer(response.players.get(0));

			initialPage.enableGameStart();

			roomServer.SubscribeForRoomUpdates(roomName, player.getUsername(), this::roomUpdateHandler);

			showStatusMessage(Language.MessageType.RoomCreated);

		} else if (response.responseType == RoomResponseType.InvalidRoom) {
			System.out.println("Requested room already exists ... ");
			showInfoMessage(Language.MessageType.RoomExists);

		} else if (response.responseType == RoomResponseType.AlreadyIn) {
			System.out.println("You are already in this room ... ");
			showInfoMessage(Language.MessageType.AlreadyInRoom);
		}
	}

	private void roomUpdateHandler(RoomResponseMsg response) {
		System.out.println("Handling room update ... ");

		if (response.responseType == RoomResponseType.PlayerJoined) {
			System.out.println("Player joined update ... ");

			showInfoMessage(Language.MessageType.NewPlayerJoined);

			// var newPlayer = filterNewPlayer(response.players);
			var newPlayer = filterNewPlayer(response.players, response.username);

			if (newPlayer == null) {
				System.out.println("ERROR, new player has null as a username ... ");
				return;
			}

			players.add(newPlayer);

			initialPage.addPlayer(newPlayer);
			initialPage.enableGameStart();

		} else if (response.responseType == RoomResponseType.PlayerLeft) {
			System.out.println("Player left update ... ");

			showInfoMessage(Language.MessageType.PlayerLeft);

			var whoLeft = response.username;

			players.removeIf((player) -> player.getUsername().equals(whoLeft));

			initialPage.removePlayer(whoLeft);

			if (this.players.size() < 2) {
				initialPage.disableGameStart();
			}

		} else if (response.responseType == RoomResponseType.RoomDestroyed) {
			System.out.println("Room destroyed update ... ");
			showInfoMessage(Language.MessageType.RoomDestroyed);

			players.clear();

			roomServer.UnsubFromRoomUpdates();

			initialPage.clearPlayers();
			initialPage.hidePlayers();
			initialPage.disableGameStart();
			initialPage.disableLeaveRoom();
			initialPage.enableCreateRoom();
			initialPage.enableJoinRoom();
		} else if (response.responseType == RoomResponseType.GameStarted) {

			System.out.println("Successfully started game ... ");
			showInfoMessage(Language.MessageType.GameStarted);

			roomServer.UnsubFromRoomUpdates();

			onGameReady.execute(player, response.roomName);

		} else {
			System.out.println("Inappropriate message received as an roomUpdate ... ");
			System.out.println("Type: " + response.responseType.toString());
		}

	}

	private PlayerDescription filterNewPlayer(List<PlayerDescription> newPlayers, String name) {
		for (var player : newPlayers) {
			if (player.getUsername().equals(name)) {
				return player;
			}
		}

		return null;
	}

	private void joinRoomActionHandler(String roomName, String roomPassword) {
		if (roomServer == null || !roomServer.isReady()) {
			System.out.println("RoomServerProxy is not ready or already in use ... ");
			showInfoMessage(Language.MessageType.PleaseWaitForRoomServer);
			return;
		}

		System.out.println("room server is ready ... will try to join room ... ");
		roomServer.JoinRoom(roomName, roomPassword, player.getUsername(),
				(RoomResponseMsg response) -> {
					System.out.println("Handling join response ... ");

					if (response.responseType == RoomResponseType.Success) {
						System.out.println("Join room successful ... ");

						showInfoMessage(Language.MessageType.SuccessfulJoin);

						initialPage.disableCreateRoom();
						initialPage.disableJoinRoom();

						initialPage.enableLeaveRoom();

						initialPage.showPlayers();

						for (var player : response.players) {
							initialPage.addPlayer(player);
						}

						roomServer.SubscribeForRoomUpdates(response.roomName,
								response.username,
								this::roomUpdateHandler);

					} else if (response.responseType == RoomResponseType.WrongPassword) {
						System.out.println("Wrong room password ... ");
						showInfoMessage(Language.MessageType.WrongRoomPassword);

					} else if (response.responseType == RoomResponseType.InvalidRoom) {
						System.out.println("This room does not exist ... ");
						showInfoMessage(Language.MessageType.RoomDoesntExists);

					} else if (response.responseType == RoomResponseType.AlreadyIn) {
						System.out.println("Already in the room ... ");
						showInfoMessage(Language.MessageType.AlreadyInRoom);
					}

				});

		showInfoMessage(Language.MessageType.JoinRequestSent);
	}

	public void leaveRoomActionHandler(String roomName, String roomPassword) {
		System.out.println("Attempt to leave room ... ");
		if (roomServer == null || !roomServer.isReady()) {
			System.out.println("RoomServerProxy is not ready or already in use ... ");
			showInfoMessage(Language.MessageType.PleaseWaitForRoomServer);
			return;
		}

		roomServer.LeaveRoom(roomName, player.getUsername(),
				(RoomResponseMsg response) -> {
					System.out.println("Handling leave room response ... ");

					if (response.responseType == RoomResponseType.Success) {
						System.out.println("Successful left the room ... ");
						showInfoMessage(Language.MessageType.SuccessfulLeft);

						roomServer.UnsubFromRoomUpdates();

						players.clear();

						initialPage.clearPlayers();
						initialPage.hidePlayers();
						initialPage.disableLeaveRoom();
						initialPage.disableGameStart();

						initialPage.enableCreateRoom();
						initialPage.enableJoinRoom();

					} else {
						System.out.println("There was an error while trying to leave the room ... ");
						System.out.println("Message intentionally omitted ... ");

					}
				});

	}

	public void startGameActionHandler() {
		System.out.println("Attempt to start the game ... ");

		roomServer.StartGame(roomName, player.getUsername(),
				(RoomResponseMsg response) -> {
					System.out.println("Received start game response ... ");

					if (response.responseType == RoomResponseType.Success) {
						System.out.println("Successfully started game ... ");
						showInfoMessage(Language.MessageType.GameStarted);

						roomServer.UnsubFromRoomUpdates();

						onGameReady.execute(player, response.roomName);
					} else {
						// Game doesn't exists and not enough players are possible
						// errors but due to client side validations they can't really
						// happen. 
						System.out.println("Error while starting game ... ");
						System.out.println("Actual error: " + response.type.toString());

						showInfoMessage(Language.MessageType.UnknownError);
					}

				});

	}

	public void hideInitialPage() {
		this.initialPage.hidePage();
	}

	public void showInitialPage() {
		this.initialPage.showPage();
	}

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

	private void showStatusMessage(Language.MessageType message) {
		((MessageDisplay) initialPage).showStatusMessage(message);
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

	public String getRoomName() {
		return this.roomName;
	}

}

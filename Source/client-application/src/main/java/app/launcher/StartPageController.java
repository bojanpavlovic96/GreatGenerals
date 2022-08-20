package app.launcher;

import java.util.ArrayList;
import java.util.List;

import app.event.GameReadyHandler;
import app.form.GameReadyEventProducer;
import app.form.InitialPage;
import app.form.MessageDisplay;
import app.resource_manager.Language;
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

	private String username;
	private String password;

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

		this.players = new ArrayList<String>();

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
				(response) -> {
					if (response.getStatus() == LoginServerResponseStatus.SUCCESS) {
						System.out.println("Login successful ... ");
						showInfoMessage(Language.MessageType.LoginSuccessful);

						this.username = username;
						this.password = password;

						initialPage.hideUserForm();
						initialPage.showRoomForm();

					} else {
						System.out.println("Login failed ... ");
						showInfoMessage(Language.MessageType.LoginFailed);
					}
				});

		// showInfoMessage(Language.MessageType.LoginRequestSent);

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
						// when you get registered you automatically get logged in 
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
				this::createRoomResponseHandler);

		showInfoMessage(Language.MessageType.CreateRequestSent);
	}

	private void createRoomResponseHandler(RoomResponseMsg response) {
		System.out.println("Handling room response ... ");
		if (response.responseType == RoomResponseType.Success) {
			System.out.println("Create room successful ... ");
			showInfoMessage(Language.MessageType.RoomCreated);

			initialPage.disableCreateRoom();
			initialPage.disableJoinRoom();

			initialPage.enableLeaveRoom();

			this.roomName = response.roomName;
			this.players.add(username);
			System.out.println("Message displayed ... ");

			initialPage.showPlayers();
			System.out.println("Will add player to the list ... ");
			initialPage.addPlayer(response.players.get(0));

			// initialPage.enableGameStart();

			roomServer.SubscribeForRoomUpdates(roomName, username, this::roomUpdateHandler);

		} else if (response.responseType == RoomResponseType.InvalidRoom) {
			System.out.println("Requested room already exists ... ");
			showInfoMessage(Language.MessageType.RoomExists);

		} else if (response.responseType == RoomResponseType.AlreadyIn) {
			System.out.println("You are already in this room ... ");
			showInfoMessage(Language.MessageType.AlreadyInRoom);
		}
	}

	private void roomUpdateHandler(RoomResponseMsg response) {
		System.out.println("New room update received ... ");

		if (response.responseType == RoomResponseType.PlayerJoined) {

			showInfoMessage(Language.MessageType.NewPlayerJoined);

			var newPlayer = filterNewPlayer(response.players);

			if (newPlayer == null) {
				System.out.println("ERROR, new player has null as a username ... ");
				return;
			}

			players.add(newPlayer.getUsername());

			initialPage.addPlayer(newPlayer);
			initialPage.enableGameStart();

		} else if (response.responseType == RoomResponseType.PlayerLeft) {
			System.out.println("Player left update ... ");

			showInfoMessage(Language.MessageType.PlayerLeft);

			var whoLeft = filterWhoLeft(response.players);

			initialPage.removePlayer(whoLeft);

			if (this.players.size() < 2) {
				initialPage.disableGameStart();
			}

		} else if (response.responseType == RoomResponseType.RoomDestroyed) {
			// this will be called with the leaveRoomResponse 
			// nothing bad will happen ... but ... but 
			showInfoMessage(Language.MessageType.RoomDestoryed);
			System.out.println("Room destroyed update ... ");

			players.clear();

			roomServer.UnsubFromRoomUpdates();

			initialPage.clearPlayers();
			initialPage.disableGameStart();
			initialPage.enableCreateRoom();
			initialPage.enableJoinRoom();
		} else {
			System.out.println("Inappropriate message received as an roomUpdate ... ");
			System.out.println("Type: " + response.responseType.toString());
		}

	}

	private PlayerDescription filterNewPlayer(List<PlayerDescription> newPlayers) {
		for (var newPlayer : newPlayers) {
			if (!players.contains(newPlayer.getUsername())) {
				return newPlayer;
			}
		}

		return null;
	}

	private String filterWhoLeft(List<PlayerDescription> currPlayers) {
		var updatedNames = currPlayers.stream().map((player) -> player.getUsername()).toList();

		for (var name : this.players) {
			if (!updatedNames.contains(name)) {
				return name;
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
		roomServer.JoinRoom(roomName, roomPassword, username,
				(RoomResponseMsg response) -> {
					System.out.println("Handling join response ... ");
					showInfoMessage(Language.MessageType.SuccessfulJoin);
					initialPage.disableCreateRoom();
					initialPage.disableJoinRoom();

					initialPage.enableLeaveRoom();

					if (response.responseType == RoomResponseType.Success) {
						System.out.println("Join room successful ... ");

						initialPage.showPlayers();
						for (var player : response.players) {
							initialPage.addPlayer(player);
						}

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

		roomServer.LeaveRoom(roomName, username,
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
		System.out.println("Assuming everything is ok and starting the game ... ");
		if (onGameReady != null) {
			onGameReady.execute(username, roomName);
		}
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

	public String getUsername() {
		return this.username;
	}

	public String getRoomName() {
		return this.roomName;
	}

}

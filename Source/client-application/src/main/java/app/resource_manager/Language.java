package app.resource_manager;

public class Language {

	public enum MessageType {
		WaitingForServer,
		ConnectionEstablished,
		ServerUnreachable,
		PleaseWaitForConnection,
		LoginRequestSent,
		RegisterRequestSent,
		CreateRequestSent,
		JoinRequestSent,
		LoginSuccessful,
		LoginFailed,
		RegisterSuccessful,
		RegisterFailed,
		PleaseWaitForRoomServer,
		RoomCreated,
		SuccessfulJoin,
		SuccessfulLeft,
		AlreadyInRoom,
		RoomExists,
		WrongRoomPassword,
		RoomDoesntExists,
		NewPlayerJoined,
		PlayerLeft,
		RoomDestroyed,
		GameStarted,
		UnknownError
	}

	public static class Message {
		public String name;
		public String message;
		public String color;
	}

	public String langIconPath;
	public String title;
	public String username;
	public String password;
	public String login;
	public String register;
	public String logout;
	public String roomName;
	public String roomPassword;
	public String createRoom;
	public String joinRoom;
	public String leaveRoom;
	public String startGame;
	public String playersInRoom;
	public String bottomText;

	public Language.Message waitingForServer;
	public Language.Message connectionEstablished;
	public Language.Message serverUnreachable;
	public Language.Message pleaseWaitForConnection;
	public Language.Message loginRequestSent;
	public Language.Message registerRequestSent;
	public Language.Message createRequestSent;
	public Language.Message joinRequestSent;
	public Language.Message loginSuccessful;
	public Language.Message loginFailed;
	public Language.Message registerSuccessful;
	public Language.Message registerFailed;

	public Language.Message pleaseWaitForRoomServer;

	public Language.Message roomCreated;
	public Language.Message successfulJoin;
	public Language.Message successfulLeft;
	public Language.Message alreadyInRoom;
	public Language.Message roomExists;
	public Language.Message roomDoesntExists;

	public Language.Message wrongRoomPassword;

	public Language.Message newPlayerJoined;
	public Language.Message playerLeft;
	public Language.Message roomDestroyed;

	public Language.Message gameStarted;

	public Language.Message unknownError;

	public Language() {

	}

	public Language.Message getMessage(Language.MessageType message) {
		switch (message) {
			case WaitingForServer:
				return this.waitingForServer;
			case ConnectionEstablished:
				return this.connectionEstablished;
			case ServerUnreachable:
				return this.serverUnreachable;
			case PleaseWaitForConnection:
				return this.pleaseWaitForConnection;
			case LoginRequestSent:
				return this.loginRequestSent;
			case RegisterRequestSent:
				return this.registerRequestSent;
			case CreateRequestSent:
				return this.createRequestSent;
			case JoinRequestSent:
				return this.joinRequestSent;
			case LoginFailed:
				return this.loginFailed;
			case LoginSuccessful:
				return this.loginSuccessful;
			case RegisterFailed:
				return this.registerFailed;
			case RegisterSuccessful:
				return this.registerSuccessful;
			case PleaseWaitForRoomServer:
				return this.pleaseWaitForRoomServer;
			case RoomCreated:
				return this.roomCreated;
			case AlreadyInRoom:
				return this.alreadyInRoom;
			case RoomExists:
				return this.roomExists;
			case WrongRoomPassword:
				return this.wrongRoomPassword;
			case RoomDoesntExists:
				return this.roomDoesntExists;
			case SuccessfulJoin:
				return this.successfulJoin;
			case SuccessfulLeft:
				return this.successfulLeft;
			case NewPlayerJoined:
				return this.newPlayerJoined;
			case PlayerLeft:
				return this.playerLeft;
			case RoomDestroyed:
				return this.roomDestroyed;
			case GameStarted:
				return this.gameStarted;
			case UnknownError:
				return this.unknownError;
			default:
				break;
		}

		return null;
	}

}

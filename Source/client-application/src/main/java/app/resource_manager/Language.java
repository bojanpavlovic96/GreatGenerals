package app.resource_manager;

public class Language {

	public enum MessageType {
		WaitingForServer, ConectionEstablished, ServerUnreachable, PleaseWaitForConnection, LoginRequestSent, RegisterRequestSent, CreateRequestSent, JoinRequestSent
	}

	public static class Message {
		public String name;
		public String message;
		public String color;
	}

	public String langIconPath; // lang-icon
	public String title;
	public String username;
	public String password;
	public String login;
	public String register;
	public String logout;
	public String roomName; // room-name
	public String roomPassword; // room-password
	public String createRoom; // create-room
	public String joinRoom; // join-room
	public String startGame; // start-game
	public String playersInRoom;
	public String bottomText; // bottom-text

	public Language.Message waitingForServer;
	public Language.Message connectionEstablished;
	public Language.Message serverUnreachable;
	public Language.Message pleaseWaitForConnection;
	public Language.Message loginRequestSent;
	public Language.Message registerRequestSent;
	public Language.Message createRequestSent;
	public Language.Message joinRequestSent;

	public Language() {

	}

	public Language.Message getMessage(Language.MessageType message) {
		switch (message) {
		case WaitingForServer:
			return this.waitingForServer;
		case ConectionEstablished:
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
		}

		return null;
	}

}

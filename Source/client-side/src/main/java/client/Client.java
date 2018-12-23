package client;

import board.BoardManager;
import chat.BasicChatController;
import chat.ChatController;
import rabbit.api.Messenger;
import rabbit.api.Pigeon;
import ui.CliInterface;
import ui.UserInterface;

public class Client {

	private Messenger messenger;

	private ChatController chat_controller;

	private BoardManager board_manager;

	private GameData game_data;

	private UserInterface user_interface;

	public Client(Messenger messenger, ChatController chat_controller, BoardManager board_manager,
			GameData game_data, UserInterface user_interface) {

		this.messenger = messenger;
		this.chat_controller = chat_controller;
		this.board_manager = board_manager;
		this.game_data = game_data;
		this.user_interface = user_interface;

		// set handlers to all available events
		this.connectWithUserInterface();

		this.user_interface.startGameLoop();

		System.out.println("Client created...");

	}

	// used to set all event handlers to the user interface instance
	private void connectWithUserInterface() {

	}

	public static void main(String[] args) {

		Messenger messenger = new Pigeon("amqp://guest:guest@cera.ddns.net");
		ChatController chat_controller = new BasicChatController();
		BoardManager board_manager = null;
		GameData game_data = new GameData();
		UserInterface user_interface = new CliInterface();

		Client client = new Client(messenger, chat_controller, board_manager, game_data, user_interface);

	}

}

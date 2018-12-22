package client;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import board.BoardManager;
import rabbit.api.Messenger;
import rabbit.api.Pigeon;

public class CliClient {

	private Messenger messenger;

	private BoardManager board_manager;

	private GameData game_data;

	public CliClient(Messenger messenger, BoardManager board_manager, GameData game_data) {

		this.messenger = messenger;
		this.board_manager = board_manager;
		this.game_data = game_data;
		
		this.gameLoop();
		
	}

	private void gameLoop() {

		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

		String user_input = "";

		try {

			while (!user_input.equals("cancel")) {

				user_input = reader.readLine();

				System.out.println("Processing: " + user_input);

			}

		} catch (Exception e) {
			System.out.println("Exception in game loop...");
			System.out.println(e);
		}

	}

	public static void main(String[] args) {
		
		CliClient client=new CliClient(null, null, null);
		
		
	}

}

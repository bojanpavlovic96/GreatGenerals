package client;

import board.Board;
import rabbit.api.Messenger;
import rabbit.api.Pigeon;

public class CliClient {

	private Messenger messenger;

	private Board board;

	private String username;
	private String password;

	private String room_id;
	private String room_name;
	private String room_key;

	public CliClient() {

		this.messenger = new Pigeon();

	}

}

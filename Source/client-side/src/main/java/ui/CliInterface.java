package ui;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class CliInterface implements UserInterface, Runnable {

	private boolean running;

	private BufferedReader reader;

	private Thread game_thread;

	public CliInterface() {

		this.running = false;

		this.reader = new BufferedReader(new InputStreamReader(System.in));

	}

	public void run() {

		String user_input = "";

		while (this.running) {

			System.out.print("Waiting for user input: ");

			user_input = this.reader.readLine();

		}

	}

	public void startGameLoop() {
		this.running = true;

		this.game_thread = new Thread(this);

		this.game_thread.start();
	}

	public void stopGameLoop() {
		if (this.running) {
			this.running = false;
			System.out.println("Game loop should stop now...");
		} else {
			System.out.println("Game loop is not started yet...");
		}
	}

	public void setClickHandler() {
		// TODO Auto-generated method stub

	}

}

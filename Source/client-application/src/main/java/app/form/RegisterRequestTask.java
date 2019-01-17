package app.form;

import com.rabbitmq.client.Channel;

public class RegisterRequestTask implements Runnable {

	private Channel channel;

	private String username;
	private String password;

	public RegisterRequestTask(Channel channel, String username, String password) {
		super();

		this.channel = channel;
		this.username = username;
		this.password = password;
	}

	// TODO implement
	public void run() {
		// TODO same as with the login request task
		// leave debug message
		System.out.println("Sending register request ... @ LoginRequstTask");

	}

}

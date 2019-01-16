package app.form;

import com.rabbitmq.client.Channel;

public class LoginRequestTask implements Runnable {

	private Channel channel;

	private String username;
	private String password;

	public LoginRequestTask(Channel channel, String username, String password) {
		this.channel = channel;
		this.username = username;
		this.password = password;
	}

	// TODO implement
	public void run() {

	}

}

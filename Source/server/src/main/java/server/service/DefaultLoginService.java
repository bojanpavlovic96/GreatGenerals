package server.service;

import com.rabbitmq.client.Channel;

import server.data.UserData;
import server.database.Database;

public class DefaultLoginService implements LoginService {

	private Channel channel;
	private Database database;

	public DefaultLoginService(Channel channel, Database database) {
		this.channel = channel;
		this.database = database;

		this.initializeChannel();

	}

	private String generateQueryFor(String username) {
		return "select all from users where username=" + username;
	}

	private void initializeChannel() {

	}

	@Override
	public UserData checkUser(String username, String password) {

		return null;
	}

	@Override
	public void setCommunicationChannel(Channel channel) {
		this.channel = channel;
		this.initializeChannel();
	}

	@Override
	public Channel getCommunicationChannel() {
		return this.channel;
	}

}

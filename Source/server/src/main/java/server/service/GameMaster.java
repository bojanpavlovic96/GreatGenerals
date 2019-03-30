package server.service;

import com.rabbitmq.client.Channel;

import server.database.Database;

public class GameMaster {

	private String room_name;
	private Channel channel;
	private Database database;

	public GameMaster(String room_name, Channel channel, Database database) {
		super();
		this.room_name = room_name;
		this.channel = channel;
		this.database = database;

		this.initChannel();

	}

	private void initChannel() {

	}

	public String getRoom_name() {
		return room_name;
	}

	public void setRoom_name(String room_name) {
		this.room_name = room_name;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public Database getDatabase() {
		return database;
	}

	public void setDatabase(Database database) {
		this.database = database;
	}

}

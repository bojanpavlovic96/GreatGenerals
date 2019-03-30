package server;

import java.util.HashMap;
import java.util.Map;

import com.rabbitmq.client.Channel;

import server.connection.ConnectionTask;
import server.connection.ConnectionUnit;
import server.database.Database;
import server.service.DefaultLoginService;
import server.service.GameMaster;
import server.service.LoginService;

public class Server {

	private String connection_uri;

	private ConnectionTask connection_task;
	private Thread connection_thread;

	private Database database;
	private LoginService login_service;

	private Channel room_request_channel;

	private Map<String, GameMaster> room_controllers;

	public Server(String connection_uri) {

		this.connection_uri = connection_uri;

		this.connection_task = new ConnectionUnit(this.connection_uri, new Runnable() {

			@Override
			public void run() {

				// after connection establishment

				// used for accepting room creating requests
				room_request_channel = connection_task.getChannel();
				initRoomRequestChannel();

				// attention maybe pass connection uri or something like that ... ?
				database = new Database();
				// debug
				System.out.println("Database created ... @ ConnectionTask.onConnectionReady");

				login_service = new DefaultLoginService(connection_task.getChannel(), database);
				// debug
				System.out.println("Login service created ... @ ConnectionTask.onConnectionReady");

			}

		});

		this.connection_thread = new Thread(this.connection_task);
		this.connection_thread.start();

		this.room_controllers = new HashMap<String, GameMaster>();

	}

	private void initRoomRequestChannel() {

		// set listeners

		// create room message

		// check does room with same name exists
		// if not create one
		// send response

	}

	public void shutdown() {
		// TODO shutdown all other components also

		this.connection_task.shutdown();
	}

}

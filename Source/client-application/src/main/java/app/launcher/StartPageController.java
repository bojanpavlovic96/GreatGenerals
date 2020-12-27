package app.launcher;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import org.json.JSONObject;

import com.rabbitmq.client.Channel;

import app.event.GameReadyHandler;
import app.event.RoomFormActionHandler;
import app.event.UserFormActionHandler;
import app.form.ConnectionUser;
import app.form.GameReadyEventProducer;
import app.form.InitialPage;
import app.form.MessageDisplay;
import app.resource_manager.QueuesConfig;
import root.ActiveComponent;

public class StartPageController
		implements GameReadyEventProducer,
		ConnectionUser,
		ActiveComponent {

	private int id;

	private QueuesConfig queuesConfig;

	private Channel channel;

	private InitialPage initialPage;

	private GameReadyHandler onGameReady;

	private String responseQueue;

	// game data
	// user
	private String username;
	private String password;
	private boolean loggedIn;
	// room
	private String room_name;
	private String roomPassword;
	// also room
	private List<String> players;

	// methods

	public StartPageController(InitialPage initial_page,
			QueuesConfig queuesConfig,
			GameReadyHandler onGameReady) {

		this.queuesConfig = queuesConfig;
		this.onGameReady = onGameReady;

		// TODO something better than this, maybe mac-address
		this.id = (new Random()).nextInt();

		this.initialPage = initial_page;

		// get default queue naming manager
		// this.naming_manager = CommunicationNamingManager.getInstance("default");

		this.showInitialPage();

		this.initPageActionHandlers();

	}

	private void initPageActionHandlers() {

		this.initialPage.setOnLoginHandler(
				new UserFormActionHandler() {
					public void handleFormAction(String _username, String _password) {

						// debug
						System.out.println("Handling login event ... @ InitialController");

						username = _username;
						password = _password;

						if (channel != null) {

							try {

								// TODO replace this 'justJson' with proper class
								JSONObject json_request = new JSONObject();
								json_request.put("id", id);
								json_request.put("username", username);
								json_request.put("password", password);

								// debug
								System.out.println("Publishing on: "
										+ queuesConfig.loginExchange
										+ " for: "
										+ queuesConfig.loginRoutingKey);

								channel.basicPublish(
										queuesConfig.loginExchange,
										queuesConfig.loginRoutingKey,
										null,
										(json_request.toString()).getBytes());

								// debug
								System.out.println("Calling on_game_ready event handler ... ");

								// attention. called here just for testing purpose
								onGameReady.execute(username, room_name);

							} catch (IOException e) {
								// Auto-generated catch block
								e.printStackTrace();
							}

							((MessageDisplay) initialPage).showInfoMessage("login-request-sent");

						} else {
							((MessageDisplay) initialPage).showInfoMessage("please-wait-for-connection");
						}

					}

				});

		this.initialPage.setOnRegisterHandler(new UserFormActionHandler() {

			public void handleFormAction(String username, String password) {

				// debug
				System.out.println("Handling register event ... @ InitialController");

				if (channel != null) {
					// TODO send register request using channel

					((MessageDisplay) initialPage).showInfoMessage("register-request-sent");

				} else {
					((MessageDisplay) initialPage).showInfoMessage("please-wait-for-connection");
				}

			}

		});

		// implement
		this.initialPage.setOnLogoutHandler();
		// just send logout request ... no need for response i gues

		this.initialPage.setOnCreateRoomHandler(new RoomFormActionHandler() {

			public void handleFormAction(String room_name, String room_password) {

				// debug
				System.out.println("Handling create room event ... @ InitialController");

				if (channel != null) {

					((MessageDisplay) initialPage).showInfoMessage("create-request-sent");

				} else {
					((MessageDisplay) initialPage).showInfoMessage("please-wait-for-connection");
				}

			}

		});

		this.initialPage.setOnJoinRoomHandler(new RoomFormActionHandler() {

			public void handleFormAction(String room_name, String room_password) {

				// debug
				System.out.println("Handling join room event ... @ InitialController");

				if (channel != null) {
					// TODO send join room request

					((MessageDisplay) initialPage).showInfoMessage("join-request-sent");

				} else {
					((MessageDisplay) initialPage).showInfoMessage("please-wait-for-connection");
				}

			}

		});

		this.initialPage.setOnStartGameHandler();
		// check number of players
		// (there should be 3 of them)

	}

	public void hideInitialPage() {
		this.initialPage.hide();
	}

	public void showInitialPage() {
		this.initialPage.show();
	}

	public void setCommunicationChannel(Channel channel) {

		this.channel = channel;

		this.initialPage.showStatusMessage("connection-established");

		this.initChannel();

	}

	// implement
	private void initChannel() {

		try {

			this.channel.exchangeDeclare(this.queuesConfig.loginExchange, "topic");

			this.responseQueue = this.channel.queueDeclare().getQueue();

		} catch (IOException e) {
			// Auto-generated catch block
			e.printStackTrace();
		}

		/**
		 * implement declare queues&exchanges
		 * 
		 * implement set adequate handlers for :
		 * 
		 * login response show adequate message fill this.username and this.password
		 * show roomForm
		 * 
		 * register response show adequate message fill initialPage.username and
		 * initialPage.password
		 * 
		 * room create response show adequate message
		 * 
		 * room join response show adequate message fill playerList
		 * 
		 * startGame response show adequate message call gameReadyHandler
		 * 
		 */

	}

	@Override
	public Channel getCommunicationChannel() {
		return this.channel;
	}

	@Override
	public void shutdown() {

		if (this.channel != null && this.channel.isOpen()) {
			try {

				this.channel.close();

			} catch (IOException e) {
				// Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	// game initiator interface

	public void setOnGameReadyHandler(GameReadyHandler handler) {
		this.onGameReady = handler;
	}

	public GameReadyHandler getGameReadyHandler() {
		return this.onGameReady;
	}

	// from connection user
	@Override
	public void handleConnectionFailure() {

		if (this.channel != null && this.channel.isOpen()) {
			try {
				this.channel.close();
			} catch (IOException e) {
				// Auto-generated catch block
				e.printStackTrace();
			}
		}

		this.initialPage.showStatusMessage("server-unreachable");

	}

}

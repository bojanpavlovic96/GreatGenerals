package app.launcher;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import org.json.JSONObject;

import com.rabbitmq.client.Channel;
import com.rabbitmq.tools.json.JSONReader;

import app.event.GameReadyHandler;
import app.event.RoomFormActionHandler;
import app.event.UserFormActionHandler;
import app.form.ConnectionUser;
import app.form.GameReadyEventProducer;
import app.form.InitialPage;
import app.form.MessageDisplay;
import app.resource_manager.CommunicationNamingManager;
import root.ActiveComponent;

public class InitialController implements GameReadyEventProducer, ConnectionUser, ActiveComponent {

	private int id;

	private Channel channel;

	private CommunicationNamingManager naming_manager;

	private InitialPage initial_page;

	private GameReadyHandler on_game_ready;

	private String response_queue;

	// game data
	// user
	private String username;
	private String password;
	private boolean logged_in;
	// room
	private String room_name;
	private String room_password;
	// also room
	private List<String> players;

	// methods

	public InitialController(InitialPage initial_page) {

		// TODO something better than this, maybe mac-address
		this.id = (new Random()).nextInt();

		this.initial_page = initial_page;

		// get default queue naming manager
		this.naming_manager = CommunicationNamingManager.getInstance("default");

		this.showInitialPage();

		this.initPageActionHandlers();

	}

	private void initPageActionHandlers() {

		this.initial_page.setOnLoginHandler(new UserFormActionHandler() {

			public void execute(String username, String password) {

				// debug
				System.out.println("Handling login event ... @ InitialController");

				username = initial_page.getUsername();
				password = initial_page.getPassword();

				if (channel != null) {

					try {

						JSONObject json_request = new JSONObject();
						json_request.put("id", id);
						json_request.put("username", username);
						json_request.put("password", password);

						// debug
						System.out.println("Publishing on: "
											+ naming_manager.getConfig("login-exchange-name")
											+ "\nfor: "
											+ naming_manager.getConfig("login-routing-key"));

						channel.basicPublish(	naming_manager.getConfig("login-exchange-name"),
												naming_manager.getConfig("login-routing-key"),
												null,
												(json_request.toString()).getBytes());

						// debug
						System.out.println("Calling on_game_ready event handler ... ");
						on_game_ready.execute();

					} catch (IOException e) {
						// Auto-generated catch block
						e.printStackTrace();
					}

					((MessageDisplay) initial_page).showInfoMessage("login-request-sent");
					// on_game_ready.execute();

				} else {
					((MessageDisplay) initial_page).showInfoMessage("please-wait-for-connection");
				}

			}

		});

		this.initial_page.setOnRegisterHandler(new UserFormActionHandler() {

			public void execute(String username, String password) {

				// debug
				System.out.println("Handling register event ... @ InitialController");

				if (channel != null) {
					// TODO send register request using channel

					((MessageDisplay) initial_page).showInfoMessage("register-request-sent");

				} else {
					((MessageDisplay) initial_page).showInfoMessage("please-wait-for-connection");
				}

			}

		});

		// implement
		this.initial_page.setOnLogoutHandler();
		// just send logout request ... no need for response i gues

		this.initial_page.setOnCreateRoomHandler(new RoomFormActionHandler() {

			public void execute(String room_name, String room_password) {

				// debug
				System.out.println("Handling create room event ... @ InitialController");

				if (channel != null) {
					// TODO send create room request

					((MessageDisplay) initial_page).showInfoMessage("create-request-sent");

				} else {
					((MessageDisplay) initial_page).showInfoMessage("please-wait-for-connection");
				}

			}

		});

		this.initial_page.setOnJoinRoomHandler(new RoomFormActionHandler() {

			public void execute(String room_name, String room_password) {

				// debug
				System.out.println("Handling join room event ... @ InitialController");

				if (channel != null) {
					// TODO send join room request

					((MessageDisplay) initial_page).showInfoMessage("join-request-sent");

				} else {
					((MessageDisplay) initial_page).showInfoMessage("please-wait-for-connection");
				}

			}

		});

		this.initial_page.setOnStartGameHandler();
		// check number of players (there should be 3 of them because of the map
		// dividing)

	}

	public void hideInitialPage() {
		this.initial_page.hide();
	}

	public void showInitialPage() {
		this.initial_page.show();
	}

	// connection user interface

	public void setCommunicationChannel(Channel channel) {
		this.channel = channel;

		this.initial_page.showStatusMessage("connection-established");

		this.initChannel();

	}

	// implement
	private void initChannel() {

		try {

			this.channel.exchangeDeclare(	this.naming_manager.getConfig("login-exchange-name"),
											"topic");

			this.response_queue = this.channel.queueDeclare().getQueue();

		} catch (IOException e) {
			// Auto-generated catch block
			e.printStackTrace();
		}

		/**
		 * implement 
		 * declare queues&exchanges
		 * 
		 * implement
		 * set adequate handlers for :
		 * 
		 *  login response
		 *  	show adequate message
		 *  	fill this.username and this.password
		 *  	show roomForm	
		 *  
		 *  register response
		 *  	show adequate message
		 *  	fill initialPage.username and initialPage.password
		 *  
		 *  room create response
				show adequate message
			
		 *  room join response
		 *  	show adequate message
		 *  	fill playerList
		 *  
		 *  startGame response
		 *  	show adequate message
		 *  	call gameReadyHandler
		 * 	
		 *  TODO use this.naming_manager for queue naming 
		 *  TODO fill /resources/config/queue/default-queue-config.json with appropriate queue names
		 */

	}

	public Channel getCommunicationChannel() {
		return this.channel;
	}

	// should be shutdown interface

	public void shutdown() {

	}

	// game initiator interface

	public void setOnGameReadyHandler(GameReadyHandler handler) {
		on_game_ready = handler;
	}

	public GameReadyHandler getGameReadyHandler() {
		return this.on_game_ready;
	}

}

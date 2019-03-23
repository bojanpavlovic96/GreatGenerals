package app.launcher;

import java.util.List;

import com.rabbitmq.client.Channel;

import app.event.GameReadyHandler;
import app.event.RoomFormActionHandler;
import app.event.UserFormActionHandler;
import app.form.ConnectionUser;
import app.form.GameReadyEventProducer;
import app.form.InitialPage;
import app.form.MessageDisplay;
import app.resource_manager.QueueNamingManager;
import root.ActiveComponent;

public class InitialController implements GameReadyEventProducer, ConnectionUser, ActiveComponent {

	private Channel channel;

	private QueueNamingManager naming_manager;

	private InitialPage initial_page;

	private GameReadyHandler on_game_ready;

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

		this.initial_page = initial_page;

		// get default queue naming manager
		this.naming_manager = QueueNamingManager.getInstance("default");

		this.showInitialPage();

		this.initPageActionHandlers();

	}

	private void initPageActionHandlers() {

		this.initial_page.setOnLoginHandler(new UserFormActionHandler() {

			public void execute(String username, String password) {

				System.out.println("Handling login event ... @ InitialController");

				if (channel != null) {

					// TODO send login request using this.channel

					((MessageDisplay) initial_page).showInfoMessage("login-request-sent");

					on_game_ready.execute();

				} else {
					((MessageDisplay) initial_page).showInfoMessage("please-wait-for-connection");
				}

			}

		});

		this.initial_page.setOnRegisterHandler(new UserFormActionHandler() {

			public void execute(String username, String password) {

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

				System.out.println("Handling join room event ... @ InitialController");

				if (channel != null) {
					// TODO send join room request

					((MessageDisplay) initial_page).showInfoMessage("join-request-sent");

				} else {
					((MessageDisplay) initial_page).showInfoMessage("please-wait-for-connection");
				}

			}

		});

		// implement
		this.initial_page.setOnStartGameHandler();
		// check number of players (there should be 3 of them because of the map
		// dividing)

	}

	// public methods
	// TODO maybe wrap with some interface, not necessary

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

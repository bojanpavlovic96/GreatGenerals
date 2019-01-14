package app.form;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;
import java.util.Random;

import com.rabbitmq.client.Channel;

import app.event.GameReadyEvent;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class InitialPage extends Stage {

	private String app_id;

	private Channel channel;

	private String user_response_queue;
	private String room_response_queue;

	private String user_request_queue = "user-request-queue";
	private String room_request_queue = "room-request-queue";

	private String user_response_exchange = "user-response-exchange";
	private String room_response_exchange = "room-response-exchange";

	// TODO may be unused
	private String main_queue;

	private double WIDTH = 200;
	private double HEIGHT = 400;

	private VBox main_containter;
	private Scene main_scene;

	private HeaderForm header;

	private UserForm user_form;

	private RoomForm room_form;

	private Button start_game_btn;

	private VBox players;

	private GameReadyEvent on_game_ready;

	// methods

	public InitialPage(String main_queue) {

		this.resolveId();

		// TODO not used
		this.main_queue = main_queue;

		this.initStage();

		this.initHeader();

		this.initUserForm();

		this.user_form.setVisible(false);
		
		this.initRoomForm();

		this.setScene(this.main_scene);
	}

	private void resolveId() {
		Random gen = new Random();
		this.app_id = Integer.toString(gen.nextInt(Integer.MAX_VALUE));
	}

	private void initStage() {

		this.main_containter = new VBox();
		this.main_scene = new Scene(this.main_containter);

		// TODO commented just for testing, uncomment
		// this.initStyle(StageStyle.UNDECORATED);

		this.setResizable(false);

		this.main_containter.setAlignment(Pos.TOP_CENTER);
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();

		this.setWidth(this.WIDTH);
//		this.setHeight(this.HEIGHT);

		this.setX(dimension.getWidth() / 2 - this.WIDTH / 2);
		// TODO find better value
		this.setY(200);

	}

	private void initHeader() {
		this.header = new HeaderForm(this.WIDTH, 125);
		this.main_containter.getChildren().add(this.header);
		this.header.managedProperty();
	}

	private void initUserForm() {
		this.user_form = new UserForm();

		this.main_containter.getChildren().add(this.user_form);
	}

	private void initRoomForm() {
		this.room_form = new RoomForm();
		this.main_containter.getChildren().add(this.room_form);
	}


	private void setButtonHandlers() {

		// this.login_btn.setOnAction(new EventHandler<ActionEvent>() {
		//
		// public void handle(ActionEvent event) {
		//
		// if (channel != null) {
		// if (checkInput()) {
		// username = username_tf.getText();
		// tryToLogin();
		// }
		// } else {
		// (new Alert(AlertType.INFORMATION, "Connection is not established yet ...
		// :\\")).show();
		// }
		//
		// }
		//
		// });
		//
		// this.logout_btn.setOnAction(new EventHandler<ActionEvent>() {
		//
		// public void handle(ActionEvent event) {
		//
		// hideRoomForm();
		// hideLogoutButton();
		// // logout from server
		// showLoginForm();
		//
		// }
		// });
		//
		// this.create_room_btn.setOnAction(new EventHandler<ActionEvent>() {
		//
		// public void handle(ActionEvent event) {
		//
		// // if room name ok .. bla bla
		//
		// try {
		// String request = app_id + "#" + "create" + "#" + room_name_tf.getText() + "#"
		// + room_passwd_tf.getText() + "#" + username;
		// channel.basicPublish("", room_request_queue, null, request.getBytes());
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		//
		// }
		// });
		//
		// this.join_room_btn.setOnAction(new EventHandler<ActionEvent>() {
		//
		// public void handle(ActionEvent event) {
		//
		// String request = app_id + "#" + "join" + "#" + room_name_tf.getText() + "#" +
		// room_passwd_tf.getText()
		// + "#" + username;
		//
		// try {
		// channel.basicPublish("", room_request_queue, null, request.getBytes());
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		//
		// }
		//
		// });

	}

	private void tryToLogin() {

		// String user_data = this.app_id + "#" + "login" + "#" +
		// this.username_tf.getText() + "#"
		// + this.password_tf.getText();
		//
		// try {
		// this.channel.basicPublish("", this.user_request_queue, null,
		// user_data.getBytes());
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

	}

	private boolean checkInput() {

		// if (this.username_tf.getText() != null &&
		// !this.username_tf.getText().isEmpty()) {
		// System.out.println("username ok");
		//
		// if (this.password_tf.getText() != null &&
		// !this.password_tf.getText().isEmpty()) {
		// System.out.println("passw ok");
		//
		// return true;
		//
		// } else {
		//
		// System.out.println("invalid passw");
		//
		// (new Alert(AlertType.ERROR, "Invalid password :)")).show();
		// return false;
		// }
		//
		// } else {
		// System.out.println("invalid username");
		// (new Alert(AlertType.ERROR, "Invalid username :)")).show();
		// return false;
		// }

		return false;

	}

	private void initChannel() {

		// TODO second false should be true "autodelete" (maybe)
		try {

			// login and register queue
			this.channel.queueDeclare(this.user_request_queue, true, false, false, null);

			this.channel.exchangeDeclare(this.user_response_exchange, "direct");
			this.user_response_queue = this.channel.queueDeclare().getQueue();
			this.channel.queueBind(this.user_response_queue, this.user_response_exchange, this.app_id);
			this.setUserResponseConsumer();

			// create and join room queue
			this.channel.queueDeclare(this.room_request_queue, true, false, false, null);

			this.channel.exchangeDeclare(this.room_response_exchange, "direct");
			this.room_response_queue = this.channel.queueDeclare().getQueue();
			this.channel.queueBind(this.room_response_queue, this.room_response_exchange, this.app_id);
			this.setRoomResponseConsumer();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void setUserResponseConsumer() {
//		try {
//			this.channel.basicConsume(this.user_response_queue, true, new DefaultConsumer(channel) {
//
//				@Override
//				public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties,
//						byte[] body) throws IOException {
//
//					String[] args = (new String(body)).split("#");
//
//					if (args[0].equalsIgnoreCase("ok")) {
//
//						Platform.runLater(new Runnable() {
//
//							public void run() {
//
//								(new Alert(AlertType.INFORMATION, "You are logegd in ... :)")).show();
//
//								hideLoginForm();
//								showLogoutButton();
//								showRoomForm();
//
//							}
//						});
//
//					} else {
//						(new Alert(AlertType.ERROR, "Bad username or password ... :\\")).show();
//					}
//
//				}
//
//			});
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

	private void setRoomResponseConsumer() {

		// try {
		// this.channel.basicConsume(this.room_response_queue, true, new
		// DefaultConsumer(channel) {
		// @Override
		// public void handleDelivery(String consumerTag, Envelope envelope,
		// BasicProperties properties,
		// byte[] body) throws IOException {
		//
		// String message = new String(body);
		//
		// final String[] args = message.split("#");
		// // ok || error # ...
		//
		// System.out.println("Received from room-rsponse: ");
		//
		// if (args[0].equals("create-ok")) {
		// // create ack
		//
		// Platform.runLater(new Runnable() {
		//
		// public void run() {
		//
		// showGameButton();
		// addPlayer(username);
		// showPlayersList();
		//
		// }
		// });
		//
		// } else if (args[0].equals("join-ok")) {
		// // join ack
		//
		// Platform.runLater(new Runnable() {
		//
		// public void run() {
		// // TODO Auto-generated method stub
		//
		// for (int i = 1; i < args.length; i++) {
		// addPlayer(args[i]);
		// }
		//
		// addPlayer(username);
		// showPlayersList();
		//
		// }
		// });
		//
		// } else if (args[0].equals("user-join")) {
		// // update users
		//
		// Platform.runLater(new Runnable() {
		//
		// public void run() {
		//
		// addPlayer(args[1]);
		//
		// }
		// });
		//
		// } else {
		//
		// Platform.runLater(new Runnable() {
		//
		// public void run() {
		// (new Alert(AlertType.ERROR, args[1])).show();
		// }
		// });
		//
		// }
		//
		// }
		// });
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

	}

	// public methods

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(final Channel new_channel) {

		// this method is called from connection thread
		Platform.runLater(new Runnable() {

			public void run() {
				channel = new_channel;

				// showInfoLabel();

				// message with green background
				// info_label.setText("Connection established");
				// info_label.setStyle("-fx-background-color: #10ff10");

				initChannel();

			}
		});

	}

	public GameReadyEvent getOnGameReady() {
		return on_game_ready;
	}

	public void setOnGameReady(GameReadyEvent on_game_ready) {
		this.on_game_ready = on_game_ready;
	}

}

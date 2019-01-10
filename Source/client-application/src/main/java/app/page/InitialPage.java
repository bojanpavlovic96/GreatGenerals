package app.page;

import java.io.IOException;
import java.util.Random;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import app.event.GameReadyEvent;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class InitialPage extends Stage {

	private String app_id;

	private Channel channel;

	private String user_response_queue;
	private String room_response_queue;

	private String user_request_queue = "user-request-queue";
	private String room_request_queue = "room-request-queue";

	private String user_response_exchange = "user-response-exchange";
	private String room_response_exchange = "room-response-exchange";

	private String main_queue;

	private double WIDTH = 200;
	private double HEIGHT = 600;

	private VBox main_containter;
	private Scene main_scene;

	private Image main_pic;

	private Label title;

	private Label username_lb;
	private TextField username_tf;
	private String username;

	private Label password_lb;
	private PasswordField password_tf;

	private Button login_btn;
	private Button register_btn;

	private Button logout_btn;

	private Label room_name_lb;
	private TextField room_name_tf;

	private Label room_passwd_lb;
	private PasswordField room_passwd_tf;

	private Button create_room_btn;
	private Button join_room_btn;

	private Button start_game_btn;

	private Label info_label;

	private VBox players;

	private GameReadyEvent on_game_ready;

	// methods

	public InitialPage(String main_queue) {

		this.resolveId();

		// TODO not used
		this.main_queue = main_queue;

		this.initStage();

		this.createLoginForm();

		this.createRoomForm();

		this.createGameButton();

		this.createPlayersList();

		this.createInfoLabel();

		this.setButtonHandlers();

		// components set

		this.hideLogoutButton();

		this.hideRoomForm();

		this.hideGameButton();

		this.showInfoLabel();

		this.hidePlayersList();

		this.setScene(this.main_scene);
	}

	private void resolveId() {
		Random gen = new Random();
		this.app_id = Integer.toString(gen.nextInt(Integer.MAX_VALUE));
	}

	private void initStage() {
		this.main_containter = new VBox();
		this.main_scene = new Scene(this.main_containter);

		this.initStyle(StageStyle.UNDECORATED);

		this.main_containter.setAlignment(Pos.TOP_CENTER);

		this.setX(200);
		this.setY(200);

		this.setWidth(this.WIDTH);
		this.setHeight(this.HEIGHT);

		this.main_pic = new Image("/main_pic.jpg");
		ImageView image_v = new ImageView(this.main_pic);
		image_v.setFitWidth(this.WIDTH);
		image_v.setFitHeight(125);

		this.main_containter.getChildren().add(image_v);

		this.title = new Label("Great Generals");
		this.title.setFont(new Font("Purisa Bold", 20));

		this.main_containter.getChildren().add(this.title);

	}

	private void createLoginForm() {

		this.username_lb = new Label("Username");
		this.username_tf = new TextField("Username");
		this.username_lb.managedProperty().bindBidirectional(this.username_lb.visibleProperty());
		this.username_tf.managedProperty().bindBidirectional(this.username_tf.visibleProperty());

		this.main_containter.getChildren().add(this.username_lb);
		this.main_containter.getChildren().add(this.username_tf);

		this.password_lb = new Label("Password");
		this.password_tf = new PasswordField();
		this.password_lb.managedProperty().bindBidirectional(this.password_lb.visibleProperty());
		this.password_tf.managedProperty().bindBidirectional(this.password_tf.visibleProperty());

		this.main_containter.getChildren().add(this.password_lb);
		this.main_containter.getChildren().add(this.password_tf);

		this.login_btn = new Button("Log in");
		this.login_btn.setStyle("-fx-background-color: #99b3ff");

		this.register_btn = new Button("Register");
		this.register_btn.setStyle("-fx-background-color: #ffcccc");

		this.logout_btn = new Button("Logout");
		this.logout_btn.setStyle("-fx-background-color: #cc0000");

		this.main_containter.getChildren().add(this.login_btn);
		this.main_containter.getChildren().add(this.register_btn);
		this.main_containter.getChildren().add(this.logout_btn);

		this.login_btn.managedProperty().bindBidirectional(this.login_btn.visibleProperty());
		this.login_btn.setManaged(true);

		this.register_btn.setManaged(true);
		this.register_btn.managedProperty().bindBidirectional(this.register_btn.visibleProperty());

		this.logout_btn.setManaged(true);
		this.logout_btn.managedProperty().bindBidirectional(this.logout_btn.visibleProperty());

		VBox.setMargin(this.title, new Insets(5, 0, 20, 0));
		VBox.setMargin(this.login_btn, new Insets(10, 0, 5, 0));
		VBox.setMargin(this.logout_btn, new Insets(10, 0, 5, 0));

		this.main_containter.setPadding(new Insets(0, 5, 10, 5));

	}

	private void createRoomForm() {

		this.room_name_lb = new Label("Room name");
		this.room_name_tf = new TextField("Room name");
		this.room_name_lb.managedProperty().bindBidirectional(this.room_name_lb.visibleProperty());

		this.room_passwd_lb = new Label("Room password");
		this.room_passwd_tf = new PasswordField();
		this.room_passwd_lb.managedProperty().bindBidirectional(this.room_passwd_lb.visibleProperty());

		this.create_room_btn = new Button("Create room");
		this.create_room_btn.managedProperty().bindBidirectional(this.create_room_btn.visibleProperty());
		this.create_room_btn.setStyle("-fx-background-color: #4d94ff");

		this.join_room_btn = new Button("Join room");
		this.join_room_btn.managedProperty().bindBidirectional(this.join_room_btn.visibleProperty());
		this.join_room_btn.setStyle("-fx-background-color: #ffc299");

		this.main_containter.getChildren().add(this.room_name_lb);
		this.main_containter.getChildren().add(this.room_name_tf);

		this.main_containter.getChildren().add(this.room_passwd_lb);
		this.main_containter.getChildren().add(this.room_passwd_tf);

		this.main_containter.getChildren().add(this.create_room_btn);
		this.main_containter.getChildren().add(this.join_room_btn);

		VBox.setMargin(this.create_room_btn, new Insets(10, 0, 5, 0));

	}

	private void createGameButton() {

		this.start_game_btn = new Button("Start game");
		this.start_game_btn.managedProperty().bind(this.start_game_btn.visibleProperty());
		this.start_game_btn.setStyle("-fx-background-color: #33cc33");

		VBox.setMargin(this.start_game_btn, new Insets(10, 0, 10, 0));

		this.main_containter.getChildren().add(this.start_game_btn);
	}

	private void createPlayersList() {

		ScrollPane scroll_pane = new ScrollPane();

		this.players = new VBox();

		this.players.setAlignment(Pos.TOP_CENTER);

		scroll_pane.setContent(this.players);
		scroll_pane.setPannable(true);

		this.players.managedProperty().bind(this.players.visibleProperty());

		this.main_containter.getChildren().add(this.players);

		Label p_title = new Label("Players");

		VBox.setMargin(p_title, new Insets(0, 0, 10, 0));

		this.players.getChildren().add(p_title);

	}

	private void hidePlayersList() {
		this.players.setVisible(false);
	}

	private void showPlayersList() {
		this.players.setVisible(true);
	}

	private void addPlayer(String username) {

		Label player = new Label(username);
		this.players.getChildren().add(player);

	}

	private void hideGameButton() {
		this.start_game_btn.setVisible(false);
	}

	private void showGameButton() {
		this.start_game_btn.setVisible(true);
	}

	private void createInfoLabel() {

		this.info_label = new Label();
		this.info_label.managedProperty().bind(this.info_label.visibleProperty());
		this.info_label.setText("Waiting for connection ...");
		this.info_label.setStyle("-fx-background-color: #ffd11a");

		this.main_containter.getChildren().add(this.info_label);

		VBox.setMargin(this.info_label, new Insets(10, 0, 5, 0));

	}

	private void hideInfoLabel() {
		this.info_label.setVisible(false);
	}

	private void showInfoLabel() {
		this.info_label.setVisible(true);
	}

	private void showLoginForm() {

		this.login_btn.setVisible(true);
		this.register_btn.setVisible(true);
		this.username_lb.setVisible(true);
		this.username_tf.setVisible(true);
		this.password_lb.setVisible(true);
		this.password_tf.setVisible(true);

	}

	private void hideLoginForm() {

		this.login_btn.setVisible(false);
		this.register_btn.setVisible(false);
		this.username_lb.setVisible(false);
		this.username_tf.setVisible(false);
		this.password_lb.setVisible(false);
		this.password_tf.setVisible(false);

	}

	private void showLogoutButton() {

		this.logout_btn.setVisible(true);

	}

	private void hideLogoutButton() {

		this.logout_btn.setVisible(false);

	}

	private void showRoomForm() {

		this.room_name_lb.setVisible(true);
		this.room_name_tf.setVisible(true);
		this.room_passwd_lb.setVisible(true);
		this.room_passwd_tf.setVisible(true);
		this.create_room_btn.setVisible(true);
		this.join_room_btn.setVisible(true);

	}

	private void hideRoomForm() {

		this.room_name_lb.setVisible(false);
		this.room_name_tf.setVisible(false);
		this.room_passwd_lb.setVisible(false);
		this.room_passwd_tf.setVisible(false);
		this.create_room_btn.setVisible(false);
		this.join_room_btn.setVisible(false);

	}

	private void setButtonHandlers() {

		this.login_btn.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent event) {

				if (channel != null) {
					if (checkInput()) {
						username = username_tf.getText();
						tryToLogin();
					}
				} else {
					(new Alert(AlertType.INFORMATION, "Connection is not established yet ... :\\")).show();
				}

			}

		});

		this.logout_btn.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent event) {

				hideRoomForm();
				hideLogoutButton();
				// logout from server
				showLoginForm();

			}
		});

		this.create_room_btn.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent event) {

				// if room name ok .. bla bla

				try {
					String request = app_id + "#" + "create" + "#" + room_name_tf.getText() + "#"
							+ room_passwd_tf.getText() + "#" + username;
					channel.basicPublish("", room_request_queue, null, request.getBytes());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});

		this.join_room_btn.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent event) {

				String request = app_id + "#" + "join" + "#" + room_name_tf.getText() + "#" + room_passwd_tf.getText()
						+ "#" + username;

				try {
					channel.basicPublish("", room_request_queue, null, request.getBytes());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		});

	}

	private void tryToLogin() {

		String user_data = this.app_id + "#" + "login" + "#" + this.username_tf.getText() + "#"
				+ this.password_tf.getText();

		try {
			this.channel.basicPublish("", this.user_request_queue, null, user_data.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private boolean checkInput() {
		if (this.username_tf.getText() != null && !this.username_tf.getText().isEmpty()) {
			System.out.println("username ok");

			if (this.password_tf.getText() != null && !this.password_tf.getText().isEmpty()) {
				System.out.println("passw ok");

				return true;

			} else {

				System.out.println("invalid passw");

				(new Alert(AlertType.ERROR, "Invalid password :)")).show();
				return false;
			}

		} else {
			System.out.println("invalid username");
			(new Alert(AlertType.ERROR, "Invalid username :)")).show();
			return false;
		}

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
		try {
			this.channel.basicConsume(this.user_response_queue, true, new DefaultConsumer(channel) {

				@Override
				public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties,
						byte[] body) throws IOException {

					String[] args = (new String(body)).split("#");

					if (args[0].equalsIgnoreCase("ok")) {

						Platform.runLater(new Runnable() {

							public void run() {

								(new Alert(AlertType.INFORMATION, "You are logegd in ... :)")).show();

								hideLoginForm();
								showLogoutButton();
								showRoomForm();

							}
						});

					} else {
						(new Alert(AlertType.ERROR, "Bad username or password ... :\\")).show();
					}

				}

			});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void setRoomResponseConsumer() {

		try {
			this.channel.basicConsume(this.room_response_queue, true, new DefaultConsumer(channel) {
				@Override
				public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties,
						byte[] body) throws IOException {

					String message = new String(body);

					final String[] args = message.split("#");
					// ok || error # ...

					System.out.println("Received from room-rsponse: ");

					if (args[0].equals("create-ok")) {
						// create ack

						Platform.runLater(new Runnable() {

							public void run() {

								showGameButton();
								addPlayer(username);
								showPlayersList();

							}
						});

					} else if (args[0].equals("join-ok")) {
						// join ack

						Platform.runLater(new Runnable() {

							public void run() {
								// TODO Auto-generated method stub

								for (int i = 1; i < args.length; i++) {
									addPlayer(args[i]);
								}

								addPlayer(username);
								showPlayersList();

							}
						});

					} else if (args[0].equals("user-join")) {
						// update users

						Platform.runLater(new Runnable() {

							public void run() {

								addPlayer(args[1]);

							}
						});

					} else {

						Platform.runLater(new Runnable() {

							public void run() {
								(new Alert(AlertType.ERROR, args[1])).show();
							}
						});

					}

				}
			});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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

				showInfoLabel();

				// message with green background
				info_label.setText("Connection established");
				info_label.setStyle("-fx-background-color: #10ff10");

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

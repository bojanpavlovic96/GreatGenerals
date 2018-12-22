package rabbit.api;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.impl.AMQImpl.Queue.DeclareOk;

import actions.Move;
import board.Field;
import figures.Figure;

/**
 * @author pereca
 *
 *         messenger implementations
 */
public class Pigeon implements Messenger {

	// private communication protocl

	private String mq_uri;

	private ConnectionFactory conn_factory;
	private Connection connection;

	private Map<String, Channel> channels;

	public Pigeon() {

	}

	public Pigeon(String mq_uri) {

		this.mq_uri = mq_uri;

		this.createConnection();

	}

	private void createConnection() {
		if (this.mq_uri != null) {
			this.conn_factory = new ConnectionFactory();
			try {

				this.conn_factory.setUri(this.mq_uri);
				this.conn_factory.setConnectionTimeout(30000);

				this.connection = this.conn_factory.newConnection();

			} catch (KeyManagementException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void declareChannel(String channel_name) {
		try {
		
			Channel new_channel=this.connection.createChannel();
		
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void broadcastMove(Move move, List<Integer> userIds) {
		// TODO Auto-generated method stub

	}

	public List<Field> getBoardFields(List<Figure> units) {
		// TODO Auto-generated method stub
		return null;
	}

}

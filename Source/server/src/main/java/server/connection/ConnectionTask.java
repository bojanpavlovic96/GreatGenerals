package server.connection;

import com.rabbitmq.client.Channel;

import root.ActiveComponent;

public interface ConnectionTask extends Runnable, ActiveComponent {

	Channel getChannel();

	boolean connectionEstablished();

}

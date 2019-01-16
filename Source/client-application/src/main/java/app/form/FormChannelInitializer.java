package app.form;

import com.rabbitmq.client.Channel;

import app.resource_manager.QueueNamingManager;

public class FormChannelInitializer implements Runnable {

	private Channel channel;

	private QueueNamingManager naming_manager;

	public FormChannelInitializer(Channel channel) {
		super();

		this.channel = channel;

		// TODO may be changed to debug
		this.naming_manager = QueueNamingManager.getInstance("default");

	}

	public void run() {

		// declare all necessary queues

	}

}

package app.resource_manager;

import utils.JsonUtil;

public class BrokerConfigFields {
	public String address;
	public int port;
	public String username;
	public String password;
	public String vhost;

	public QueuesConfig queues;

	public BrokerConfigFields(String address,
			int port,
			String username,
			String password,
			String vhost,
			QueuesConfig queues) {
		this.address = address;
		this.port = port;
		this.username = username;
		this.password = password;
		this.vhost = vhost;
		this.queues = queues;
	}

	public String toString() {
		return JsonUtil.ToString(this);
	}

}

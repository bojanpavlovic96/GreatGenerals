package app.resource_manager;

import root.communication.parser.StaticParser;

public class BrokerConfigFields {

	public String address;
	public int port;
	public String username;
	public String password;
	public String vhost;

	public BrokerConfigFields() {

	}

	public BrokerConfigFields(String address,
			int port,
			String username,
			String password,
			String vhost) {
		this.address = address;
		this.port = port;
		this.username = username;
		this.password = password;
		this.vhost = vhost;
	}

	@Override
	public String toString() {
		return StaticParser.ToString(this);
	}

}

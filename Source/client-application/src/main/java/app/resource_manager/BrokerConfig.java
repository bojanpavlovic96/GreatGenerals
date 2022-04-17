package app.resource_manager;

import root.communication.parser.StaticParser;

public class BrokerConfig {

	private static final String PROD_STAGE = "prod";
	private static final String DEV_STAGE = "dev";

	public String stage; // development || production

	public BrokerConfigFields development;
	public BrokerConfigFields production;

	public BrokerConfig() {

	}

	public String constructUri() {
		var config = this.getActive();
		return "amqp://" + config.username + ":" + config.password
				+ "@" + config.address + ":" + config.port
				+ "/" + config.vhost;
	}

	public BrokerConfigFields getActive() {
		if (this.stage.equals(PROD_STAGE)) {
			return this.production;
		} else if (this.stage.equals(DEV_STAGE)) {
			return this.development;
		} else {
			System.out.println("Broker config has wrong stage value ... ");

			return null;
		}
	}

	@Override
	public String toString() {
		return StaticParser.ToString(this.getActive());
	}

}

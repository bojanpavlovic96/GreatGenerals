package app.resource_manager;

import utils.JsonUtil;

public class BrokerConfig {

	private static final String CONFIG_PATH = "config/broker-config.json";

	private static final String PROD_STAGE = "prod";
	private static final String DEV_STAGE = "dev";

	private static BrokerConfig cache;

	public static BrokerConfigFields getConfig() {

		if (BrokerConfig.cache == null) {
			var config = ConfigLoader.load(CONFIG_PATH, BrokerConfig.class);
			if (config != null) {
				BrokerConfig.cache = config;
			} else {
				System.out.println("Failed to load broker config ... ");

				return null;
			}
		}

		return BrokerConfig.cache.getTargetConfig();
	}

	// json mapped configuration fields

	private String stage; // development || production

	private BrokerConfigFields development;
	private BrokerConfigFields production;

	public String toString() {
		return JsonUtil.ToString(this);
	}

	private BrokerConfigFields getTargetConfig() {
		if (this.stage.equals(PROD_STAGE)) {
			return this.production;
		} else if (this.stage.equals(DEV_STAGE)) {
			return this.development;
		} else {
			System.out.println("Broker config has wrong stage value ... ");

			return null;
		}
	}

	public String constructUri() {
		var config = this.getTargetConfig();
		return "amqp://" + config.username + ":" + config.password
				+ "@" + config.address + ":" + config.port
				+ "/" + config.vhost;
	}

}

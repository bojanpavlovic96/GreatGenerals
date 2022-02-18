package app.resource_manager;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;

import root.ConfigLoader;

public class RestLoginServerConfig {

	private static final String CONFIG_PATH = "config/rest-login-config.json";

	private static final Object PROD_STAGE = "prod";
	private static final Object DEV_STAGE = "dev";

	private static RestLoginServerConfig cache;

	public static RestLoginServerConfigFields getConfig() {
		if (RestLoginServerConfig.cache == null) {
			var config = ConfigLoader.load(CONFIG_PATH, RestLoginServerConfig.class);
			if (config != null) {
				RestLoginServerConfig.cache = config;
			} else {
				System.out.println("Failed to load RestLoginServer configuration ... ");

				return null;
			}
		}

		return RestLoginServerConfig.cache.getTargetConfig();
	}

	// mapped fields

	private String stage;

	private RestLoginServerConfigFields production;
	private RestLoginServerConfigFields development;

	private RestLoginServerConfigFields getTargetConfig() {
		if (this.stage.equals(PROD_STAGE)) {
			return this.production;
		} else if (this.stage.equals(DEV_STAGE)) {
			return this.development;
		} else {
			System.out.println("Broker config has wrong stage value ... ");

			return null;
		}
	}

	public String toString() {

		String txtObj = "";
		try {
			JsonMapper mapper = new JsonMapper();
			txtObj = mapper.writeValueAsString(this);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		return txtObj;
	}

}

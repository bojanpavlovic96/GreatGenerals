package app.resource_manager;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.google.common.io.Resources;

public class RestLoginServerConfig {

	private static final String CONFIG_PATH = "config/rest-login-config.json";
	private static final String STAGE_FIELD = "stage";

	private static RestLoginServerConfig configCache;

	public static RestLoginServerConfig loadConfig() {

		if (RestLoginServerConfig.configCache == null) {

			URL url = Resources.getResource(CONFIG_PATH);
			String txtConfig = "";
			try {
				txtConfig = Resources.toString(url, StandardCharsets.UTF_8);
			} catch (IOException e1) {
				System.out.println("Error in loading configuration ... ");
				e1.printStackTrace();
				return null;
			}

			if (txtConfig != null && !txtConfig.isEmpty()) {

				JsonMapper mapper = new JsonMapper();
				try {

					JsonNode rawJsonConfig = mapper.readTree(txtConfig);

					String targetStage = rawJsonConfig
							.get(RestLoginServerConfig.STAGE_FIELD)
							.asText();

					JsonNode stageConfig = rawJsonConfig.get(targetStage);
					RestLoginServerConfig.configCache = mapper.treeToValue(
							stageConfig,
							RestLoginServerConfig.class);

					RestLoginServerConfig.configCache.stage = targetStage;

				} catch (JsonProcessingException e) {
					System.out.println("Error in parsing broker config file ... ");
					System.out.println("EXC: " + e.toString());
					e.printStackTrace();
					return null;
				}

			} else {
				System.out.println("ERROR: Found empty config file on path:" +
						RestLoginServerConfig.CONFIG_PATH + " ... ");
				return null;
			}

		}

		return RestLoginServerConfig.configCache;
	}

	// mapped fields

	public String stage;

	public String address;
	public String port;
	public String loginPath;
	public String registerPath;

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

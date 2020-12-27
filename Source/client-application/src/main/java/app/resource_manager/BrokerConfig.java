package app.resource_manager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.json.JsonMapper;

public class BrokerConfig {

	private static final String CONFIG_PATH = "config/broker/broker-config.json";
	private static final String STAGE_FIELD = "stage";

	private static BrokerConfig ConfigCache;

	public static BrokerConfig loadConfig() {

		if (BrokerConfig.ConfigCache == null) {

			String txtConfig = BrokerConfig.loadTextFile(CONFIG_PATH);
			if (txtConfig != null && !txtConfig.isEmpty()) {

				JsonMapper mapper = new JsonMapper();
				try {

					JsonNode rawJsonConfig = mapper.readTree(txtConfig);

					String targetStage = rawJsonConfig
							.get(BrokerConfig.STAGE_FIELD)
							.asText();

					JsonNode stageConfig = rawJsonConfig.get(targetStage);
					BrokerConfig.ConfigCache = mapper.treeToValue(stageConfig, BrokerConfig.class);

					BrokerConfig.ConfigCache.stage = targetStage;

				} catch (JsonProcessingException e) {
					System.out.println("Error in parsing broker config file ... ");
					System.out.println("EXC: " + e.toString());
					e.printStackTrace();
					return null;
				}

			} else {
				System.out.println("ERROR: Found empty config file on path:" +
						BrokerConfig.CONFIG_PATH + " ... ");
				return null;
			}

		}

		return BrokerConfig.ConfigCache;
	}

	// json mapped configuration fields

	public String stage; // development || production

	public String address;
	public int port;
	public String username;
	public String password;
	public String vhost;

	public QueuesConfig queues;

	// methods

	private static String loadTextFile(String path) {

		StringBuilder sBuilder = new StringBuilder();

		ClassLoader cLoader = BrokerConfig.class.getClassLoader();

		try (
				InputStream inStream = cLoader.getResourceAsStream(path);
				InputStreamReader strReader = new InputStreamReader(inStream);
				BufferedReader buffReader = new BufferedReader(strReader);) {

			String line = "";
			while ((line = buffReader.readLine()) != null) {
				sBuilder.append(line);
			}

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		return sBuilder.toString();
	}

	public String toString() {

		String txtObj = "";
		try {
			JsonMapper mapper = new JsonMapper();
			txtObj = mapper.writeValueAsString(this);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return txtObj;
	}

	public String constructUri() {
		return "amqp://" + this.username + ":" + password
				+ "@" + address + ":" + this.port
				+ "/" + vhost;
	}

}

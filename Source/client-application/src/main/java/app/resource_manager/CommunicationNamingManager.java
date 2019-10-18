package app.resource_manager;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

public class CommunicationNamingManager {

	static private final String RESOURCE_PREFIX = "config/queue/";
	private String resource_type = "default";
	static private final String RESOURCE_SUFFIX = "-queue-config.json";

	static private Map<String, CommunicationNamingManager> instances;

	private JSONObject config_resource;

	/***
	 * 
	 * @param config_type
	 *            - configuration type ( "default" == null | "debug" )
	 * 
	 * @return QueueNamingManager instance for given type
	 * 
	 */
	static public CommunicationNamingManager getInstance(String config_type) {

		// resolve configuration type
		if (config_type == null || config_type.isEmpty())
			config_type = "default";

		config_type.toLowerCase();

		if (CommunicationNamingManager.instances == null) {
			CommunicationNamingManager.instances = new HashMap<String, CommunicationNamingManager>();
		}

		CommunicationNamingManager manager = CommunicationNamingManager.instances.get(config_type);

		if (manager == null) {

			manager = new CommunicationNamingManager(config_type);

			// save configuration in to the hash map for later reuse
			CommunicationNamingManager.instances.put(config_type, manager);

		}

		return manager;

	}

	private CommunicationNamingManager(String config_type) {

		this.resource_type = config_type;

		this.loadResource();

	}

	private void loadResource() {

		try {

			String resource_path = CommunicationNamingManager.RESOURCE_PREFIX + this.resource_type
									+ CommunicationNamingManager.RESOURCE_SUFFIX;

			ClassLoader loader = CommunicationNamingManager.class.getClassLoader();

			InputStream input_stream = loader.getResourceAsStream(resource_path);
			InputStreamReader stream_reader = new InputStreamReader(input_stream);
			BufferedReader buff_reader = new BufferedReader(stream_reader);

			StringBuilder content = new StringBuilder();
			String line = "";
			while ((line = buff_reader.readLine()) != null) {
				content.append(line);
			}

			buff_reader.close();
			stream_reader.close();
			input_stream.close();

			this.config_resource = new JSONObject(content.toString());

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public String getConfig(String queue) {
		return this.config_resource.getString(queue);
	}

	
	public String getConfigType() {
		return this.resource_type;
	}

}

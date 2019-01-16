package app.resource_manager;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

import org.json.JSONObject;

public class QueueNamingManager {

	static private final String RESOURCE_PREFIX = "config/queue/";
	private String resource_type = "default";
	static private final String RESOURCE_SUFFIX = "-queue-config.json";

	static private Map<String, QueueNamingManager> instances;

	private JSONObject config_resource;

	/***
	 * 
	 * @param config_type
	 *            - configuration type ( "default" == null | "debug" )
	 * 
	 * @return QueueNamingManager instance for given type
	 * 
	 */
	static public QueueNamingManager getInstance(String config_type) {

		if (config_type == null)
			config_type = "default";

		config_type.toLowerCase();

		QueueNamingManager manager = QueueNamingManager.instances.get(config_type);

		if (manager == null) {

			manager = new QueueNamingManager(config_type);
			QueueNamingManager.instances.put(config_type, manager);

		}

		return manager;

	}

	private QueueNamingManager(String config_type) {

		this.resource_type = config_type;

		this.loadResource();

	}

	private void loadResource() {

		try {

			String resource_path = QueueNamingManager.RESOURCE_PREFIX +
									this.resource_type +
									QueueNamingManager.RESOURCE_SUFFIX;

			ClassLoader loader = QueueNamingManager.class.getClassLoader();
			FileReader reader = new FileReader(loader.getResource(resource_path).getPath());
			BufferedReader buff_reader = new BufferedReader(reader);

			StringBuilder content = new StringBuilder();
			String line = "";
			while ((line = buff_reader.readLine()) != null) {
				content.append(line);
			}

			buff_reader.close();
			reader.close();

			this.config_resource = new JSONObject(content.toString());

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public String getQueueName(String queue) {
		return this.config_resource.getString(queue);
	}

	public String getConfigName() {
		return this.resource_type;
	}

}

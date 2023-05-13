package app.resource_manager;

import proxy.RabbitGameServerProxyConfig;
import proxy.RabbitRoomServerProxyConfig;
import proxy.RestLoginServerConfig;
import proxy.RestReplayProxyConfig;
import root.JsonResLoader;
import root.view.FormConfig;
import root.view.ViewConfig;

public class AppConfig {

	static private final String CONFIG_PATH = "config/app-config.json";

	static private AppConfig currentConfig;

	static public AppConfig getInstance() {
		if (currentConfig == null) {
			currentConfig = JsonResLoader.load(CONFIG_PATH, AppConfig.class);
		}

		return currentConfig;
	}

	// this has to be public in order to be serializable
	public AppConfig() {

	}

	// \\\\\\\\\\\\\\\\\\\\\\\\\\
	//
	// FIELDS
	//
	// ///////////////////////////

	// public List<String> languages;

	// public String defaultLanguage;

	public LangConfig langConfig;

	public FormConfig formConfig;

	public ViewConfig viewConfig;

	public BrokerConfig brokerConfig;

	public RabbitGameServerProxyConfig rabbitGameServerProxyConfig;

	public RabbitRoomServerProxyConfig rabbitRoomServerProxyConfig;

	public RestLoginServerConfig restLoginServerConfig;

	public RestReplayProxyConfig restReplayServerConfig;

}

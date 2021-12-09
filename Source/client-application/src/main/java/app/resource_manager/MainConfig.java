package app.resource_manager;

import java.util.List;

public class MainConfig {

	static private final String CONFIG_PATH = "config/main-config.json";

	static private MainConfig currentConfig;

	static public MainConfig getInstance() {
		if (currentConfig == null) {
			currentConfig = ConfigLoader.load(CONFIG_PATH, MainConfig.class);
		}

		return currentConfig;
	}

	// this has to be public in order to be srializable
	public MainConfig() {

	}

	// \\\\\\\\\\\\\\\\\\\\\\\\\\
	//
	// FIELDS
	//
	// ///////////////////////////

	public List<String> languages;

	public String defaultLanguage;

	public String headerImagePath;

	public int titleFontSize;
	public String titleFont;
	public int messageFontSize;
	public String messageFont;

	public int headerAlphaValue;

}

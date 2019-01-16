package app.resource_manager;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;

import app.form.FormMessage;

public class StringResourceManager {

	static private final String RESOURCE_SUFFIX = "-lang.json";
	static private final String RESOURCE_PREFIX = "lang/";

	static private StringResourceManager instance;

	private String language;

	private JSONObject resources;

	private JSONArray messages;

	// methods
	public static StringResourceManager getInstance() {

		if (StringResourceManager.instance == null)
			StringResourceManager.instance = new StringResourceManager();

		return StringResourceManager.instance;
	}

	private StringResourceManager() {

		this.setLanguage("en");

		this.loadResources();

	}

	private void loadResources() {

		try {

			String file_name = StringResourceManager.RESOURCE_PREFIX +
								this.language +
								StringResourceManager.RESOURCE_SUFFIX;

			ClassLoader loader = getClass().getClassLoader();
			FileReader reader = new FileReader(loader.getResource(file_name).getPath());
			BufferedReader buff_reader = new BufferedReader(reader);

			StringBuilder content = new StringBuilder();
			String line = "";
			try {
				while ((line = buff_reader.readLine()) != null) {
					content.append(line + "\n");

				}

				buff_reader.close();
				reader.close();

				// transform plain text in JSONObject
				this.resources = new JSONObject(content.toString());

				// extract array of messages from JSONObject
				this.messages = this.resources.getJSONArray("messages");

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void setLanguage(String language) {
		this.language = language;
		this.loadResources();
	}

	public String getString(String name) {
		return this.resources.getString(name);
	}

	public FormMessage getMessage(String message) {

		JSONObject json_message = null;
		for (int i = 0; i < messages.length(); i++) {
			json_message = this.messages.getJSONObject(i);
			if (json_message.getString("name").equals(message))
				return new FormMessage(json_message.getString("message"), json_message.getString("color"));
		}

		return null;
	}

}

package root;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import com.google.common.io.Resources;

import root.communication.parser.DataParser;
import root.communication.parser.GsonJsonParser;

public class ConfigLoader {

	private static DataParser configParser;

	public static <T> T load(String configPath, Class<T> configType) {

		if (configParser == null) {
			configParser = new GsonJsonParser();
		}

		URL url = Resources.getResource(configPath);
		String txtConfig = "";
		try {
			txtConfig = Resources.toString(url, StandardCharsets.UTF_8);
		} catch (IOException e1) {
			System.out.println("Error in loading configuration -> "
					+ e1.getMessage());

			return null;
		}

		if (txtConfig != null && !txtConfig.isEmpty()) {

			return configParser.FromString(txtConfig, configType);

		} else {
			System.out.println("ERROR: Found empty config file on path:" +
					configPath + " ... ");

			return null;
		}

	}

}

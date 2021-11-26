package app.resource_manager;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import com.google.common.io.Resources;

import utils.JsonUtil;

public class ConfigLoader {

	public static <T> T load(String configPath, Class<T> configType) {

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

			return JsonUtil.FromString(txtConfig, configType);

		} else {
			System.out.println("ERROR: Found empty config file on path:" +
					configPath + " ... ");

			return null;
		}

	}

}

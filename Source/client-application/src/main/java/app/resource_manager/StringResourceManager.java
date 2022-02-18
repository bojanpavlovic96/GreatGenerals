package app.resource_manager;

import java.util.ArrayList;
import java.util.List;

import app.form.HasLabels;
import root.ConfigLoader;

public class StringResourceManager {

	static private final String RESOURCE_PREFIX = "lang/";
	static private final String RESOURCE_SUFFIX = "-lang.json";

	static private String currentLangName = "";
	static private Language currentLanguage;

	private static List<HasLabels> languageConsumers;

	public static Language getLanguage() {

		if (StringResourceManager.currentLanguage == null) {
			if (currentLangName.equals("")) {
				currentLangName = AppConfig.getInstance().defaultLanguage;
			}

			var langPath = getLanguagePath(currentLangName);
			currentLanguage = ConfigLoader.load(langPath, Language.class);
		}

		return StringResourceManager.currentLanguage;

	}

	private static String getLanguagePath(String lang) {
		return StringResourceManager.RESOURCE_PREFIX
				+ lang
				+ StringResourceManager.RESOURCE_SUFFIX;
	}

	static public void setLanguage(String language) {

		var newLangPath = getLanguagePath(language);
		currentLanguage = ConfigLoader.load(newLangPath, Language.class);

		// notify consumers
		if (languageConsumers != null) {
			for (var consumer : languageConsumers) {
				consumer.loadLabels(currentLanguage);
			}
		}

	}

	static public void subscribeForLanguageChange(HasLabels newConsumer) {
		if (languageConsumers == null) {
			languageConsumers = new ArrayList<HasLabels>();
		}

		languageConsumers.add(newConsumer);
	}

}

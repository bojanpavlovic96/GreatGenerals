package app.resource_manager;

import java.util.ArrayList;
import java.util.List;

import app.form.HasLabels;

import root.JsonResLoader;

public class StringRegistry {

	static private final String RESOURCE_PREFIX = "lang/";
	static private final String RESOURCE_SUFFIX = "-lang.json";

	private LangConfig config;

	private String currentLangName = "";
	private Language currentLanguage;

	private List<HasLabels> languageConsumers;

	public StringRegistry(LangConfig config) {
		this.languageConsumers = new ArrayList<HasLabels>();
		this.config = config;
	}

	public Language getLanguage() {

		if (currentLanguage == null) {
			if (currentLangName.equals("")) {
				currentLangName = config.defaultLanguage;
			}

			var langPath = getLanguagePath(currentLangName);
			currentLanguage = JsonResLoader.load(langPath, Language.class);
			
		}

		return currentLanguage;
	}

	private String getLanguagePath(String lang) {
		return StringRegistry.RESOURCE_PREFIX
				+ lang
				+ StringRegistry.RESOURCE_SUFFIX;
	}

	public void setLanguage(String language) {

		var newLangPath = getLanguagePath(language);
		currentLanguage = JsonResLoader.load(newLangPath, Language.class);

		// notify consumers
		if (languageConsumers != null) {
			for (var consumer : languageConsumers) {
				consumer.loadLabels(currentLanguage);
			}
		}

	}

	public void subscribeForLanguageChange(HasLabels newConsumer) {
		languageConsumers.add(newConsumer);
	}

}

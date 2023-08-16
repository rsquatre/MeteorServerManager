package fr.rsquatre.Meteor.service.translation;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.apache.commons.io.FileUtils;

import fr.rsquatre.Meteor.Meteor;
import fr.rsquatre.Meteor.system.Service;

public class TranslationFactory extends Service {

	private HashMap<String, HashMap<String, String>> locales = new HashMap<>();

	public TranslationFactory() {

		try {

			getTranslationsFolder().mkdirs();

			String enGB = Meteor.getInstance().getEmbbed("lang/en_GB.json");
			File enGBFile = getTranslationsFile("en_GB");

			if (!enGBFile.exists())
				FileUtils.write(enGBFile, enGB, "UTF-8");

			String frFR = Meteor.getInstance().getEmbbed("lang/fr_FR.json");
			File frFRFile = getTranslationsFile("fr_FR");

			if (!frFRFile.exists())
				FileUtils.write(frFRFile, frFR, "UTF-8");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public boolean isLocaleAvailable(String locale) {

		return locales.containsKey(locale);
	}

	public boolean isTranslationAvailable(String locale, String key) {

		return isLocaleAvailable(locale) ? locales.get(locale).containsKey(key) : false;
	}

	public String translate(String key) {

		return translate(Meteor.getContext().getDefaultLocale(), key);
	}

	public String translate(String locale, String key) {

		return isTranslationAvailable(locale, key) ? locales.get(locale).get(key) : null;
	}

	public static File getTranslationsFolder() {

		return new File(Meteor.getInstance().getDataFolder(), "locales");
	}

	public static File getTranslationsFile(String locale) {

		return new File(getTranslationsFolder(),
				locale.toLowerCase().endsWith(".json") ? locale : locale.concat(".json"));
	}

}

package fr.rsquatre.Meteor.service.translation;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import fr.rsquatre.Meteor.Meteor;
import fr.rsquatre.Meteor.system.Logger;
import fr.rsquatre.Meteor.system.Service;

public class TranslationFactory extends Service {

	public static final String[] DEFAULT_LOCALES = { "en_GB", "fr_FR" };

	private HashMap<String, HashMap<String, String>> locales = new HashMap<>();

	public TranslationFactory() {

		try {

			getTranslationsFolder().mkdirs();

			// TODO check for missing translations in current in local files

			File[] localeFiles = getTranslationsFolder().listFiles(file -> file.getName().toLowerCase().matches("^\\w{2}_\\w{2}\\.json$"));
			Logger.info(String.format("Found %d locale files. Checking for updates...", localeFiles.length));

			int updated = 0;
			HashMap<String, HashMap<String, String>> embeddedLocales = new HashMap<>();

			// Fetch embedded locales and create local files if missing
			for (String locale : DEFAULT_LOCALES) {

				String translations = Meteor.readEmbedded(locale.concat(".json"));
				embeddedLocales.put(locale, new Gson().fromJson(translations, TypeToken.getParameterized(HashMap.class, String.class, String.class).getType()));

				if (!getTranslationsFile(locale).exists()) { FileUtils.write(getTranslationsFile(locale), translations, StandardCharsets.UTF_8); }
			}

			for (File localeFile : localeFiles) {

				HashMap<String, String> translations = new Gson().fromJson(FileUtils.readFileToString(localeFile, StandardCharsets.UTF_8),
						TypeToken.getParameterized(HashMap.class, String.class, String.class).getType());

				// get embedded entries from the locale or the English ones if this is a user
				// provided locale and add missing translations
				boolean dirty = false;
				String locale = localeFile.getName().substring(0, 5);
				for (Entry<String, String> entry : embeddedLocales.getOrDefault(locale, embeddedLocales.get("en_GB")).entrySet()) {

					if (!translations.containsKey(entry.getKey())) {
						translations.put(entry.getKey(), entry.getValue());
						dirty = true;

						// TODO if enabled in config print added key to a file for convenient editing if
						// English was used as
						// fallback and log
					}
				}

				// persist updated translations
				if (dirty) {
					FileUtils.write(localeFile, new GsonBuilder().setPrettyPrinting().create().toJson(translations), StandardCharsets.UTF_8);
					updated++;
				}

				locales.put(locale, translations);
			}

			if (updated > 0) {

				Logger.log(String.format("Successfuly updated %d locale files.", updated));
			} else {

				Logger.log("All locales are up to date.");
			}

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

		return translate(Meteor.getContext().getSystemLocale(), key);
	}

	public String translate(String key, String... args) {

		return translateTo(Meteor.getContext().getSystemLocale(), key, args);
	}

	public String translateTo(String locale, String key) {

		return isTranslationAvailable(locale, key) ? locales.get(locale).get(key)
				: String.format("Translation not found for locale %s and key %s", locale, key);
	}

	public String translateTo(String locale, String key, String... args) {

		return isTranslationAvailable(locale, key) ? String.format(locales.get(locale).get(key), (Object[]) args)
				: String.format("Translation not found for locale %s and key %s", locale, key);
	}

	public static File getTranslationsFolder() {

		return new File(Meteor.getInstance().getDataFolder(), "locales");
	}

	public static File getTranslationsFile(String locale) {

		return new File(getTranslationsFolder(), locale.toLowerCase().endsWith(".json") ? locale : locale.concat(".json"));
	}

}

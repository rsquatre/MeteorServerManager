package fr.rsquatre.Meteor.system;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;

import fr.rsquatre.Meteor.Meteor;

public class ConfigurationContext {

	private String defaultLocale = "en_GB";

	public String getDefaultLocale() {
		return defaultLocale;
	}

	public ConfigurationContext setDefaultLocale(String locale) {

		defaultLocale = locale;
		return this;
	}

	public boolean save() {

		try {

			File configFile = new File(Meteor.getInstance().getDataFolder(), "config.json");

			if (!configFile.exists()) {
				configFile.createNewFile();
			}

			FileUtils.write(configFile, new Gson().toJson(this), Charset.forName("UTF-8"));
			return true;

		} catch (IOException e) {
			// TODO log + warn staff
		}
		return false;
	}

}

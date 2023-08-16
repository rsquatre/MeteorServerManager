package fr.rsquatre.Meteor;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

import org.apache.commons.io.FileUtils;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import fr.rsquatre.Meteor.service.translation.TranslationFactory;
import fr.rsquatre.Meteor.system.ConfigurationContext;
import fr.rsquatre.Meteor.system.Service;

public class Meteor extends JavaPlugin {

	private static Meteor instance = null;

	private ConfigurationContext config = null;

	private HashMap<Class<? extends Service>, Service> services = new HashMap<>();

	public Meteor() {

		if (instance != null) {
			throw new IllegalStateException(getService(TranslationFactory.class).translate(getName()));
		}

		instance = this;

		File configFile = new File(Meteor.getInstance().getDataFolder(), "config.json");

		if (!configFile.exists() || configFile.isDirectory()) {

			if (getDataFolder().isDirectory()) {
				getDataFolder().mkdirs();
			}

			config = new ConfigurationContext();
			config.save();

		} else {

			try {

				config = new Gson().fromJson(FileUtils.readFileToString(configFile, "UTF-8"),
						ConfigurationContext.class);
			} catch (JsonSyntaxException | IOException e) {

				e.printStackTrace(); // TODO close server
			}
		}

	}

	@SuppressWarnings("unchecked")
	public <X extends Service> X getService(Class<X> service) {

		if (!services.containsKey(service)) {
			throw new IllegalStateException(getService(TranslationFactory.class).translate(getName(), getName()));
		}

		return (X) services.get(service);
	}

	public boolean isOnline(Class<? extends Service> service) {

		return services.containsKey(service);
	}

	public static Meteor getInstance() {

		return instance;
	}

	public static ConfigurationContext getContext() {

		return instance.config;
	}

	public String getEmbbed(String path) throws IOException {

		BufferedReader buffer = new BufferedReader(
				new InputStreamReader(Thread.currentThread().getContextClassLoader().getResourceAsStream(path)));

		StringBuilder str = new StringBuilder();

		String line;
		while ((line = buffer.readLine()) != null) {
			str.append(line);
		}

		return str.toString();

	}
}

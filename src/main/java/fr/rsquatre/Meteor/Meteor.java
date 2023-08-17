package fr.rsquatre.Meteor;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerKickEvent.Cause;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import fr.rsquatre.Meteor.service.translation.TranslationFactory;
import fr.rsquatre.Meteor.system.ConfigurationContext;
import fr.rsquatre.Meteor.system.Logger;
import fr.rsquatre.Meteor.system.Service;
import net.kyori.adventure.text.Component;

public class Meteor extends JavaPlugin {

	private static Meteor instance = null;

	private ConfigurationContext context = null;

	private HashMap<Class<? extends Service>, Service> services = new HashMap<>();

	public Meteor() {

		if (instance != null)
			throw new IllegalStateException(getService(TranslationFactory.class).translate(getName()));

		instance = this;

		Logger.log("Hello Universe! The translation service is still offline, Meteor will output logs in English for now.");

		File configFile = new File(Meteor.getInstance().getDataFolder(), "config.json");

		if (!configFile.exists() || configFile.isDirectory()) {

			getLogger().info("Configuration context not found. Generating default...");

			if (!getDataFolder().isDirectory()) { getDataFolder().mkdirs(); }

			context = new ConfigurationContext();
			context.save();

		} else {

			try {

				context = new Gson().fromJson(FileUtils.readFileToString(configFile, StandardCharsets.UTF_8), ConfigurationContext.class);

			} catch (JsonSyntaxException | IOException e) {

				Logger.fatal(
						"An unhandled exception occurred while reading the configuration. Please fix the issue, delete config.json or seek help if you're stuck.");
				e.printStackTrace();
			}
		}

		Logger.log("Successfuly loaded configuration context.");

		load(TranslationFactory.class);

		getLogger().info(getService(TranslationFactory.class).translate("system.current_locale", context.getSystemLocale()));

	}

	public static boolean load(Class<? extends Service> service) {

		return load(service, new Class[0], new Object[0]);
	}

	public static boolean load(Class<? extends Service> service, Class<?>[] argTypes, Object[] args) {

		try {

			if (instance.services.containsKey(service.getClass()))
				throw new IllegalStateException(
						Meteor.getService(TranslationFactory.class).translate("system.error.service_already_online", service.getName()));

			instance.services.put(service, service.getConstructor(argTypes).newInstance(args));
			Logger.log(Meteor.getService(TranslationFactory.class).translate("system.service_online", service.getName()));

		} catch (Exception e) {

			Logger.error(Meteor.isOnline(TranslationFactory.class)
					? Meteor.getService(TranslationFactory.class).translate("system.error.service_loading", service.getName())
					: String.format("An exception occurred while trying to lead service %s.", service.getName()));
			e.printStackTrace();

			return false;
		}

		return true;
	}

	@SuppressWarnings("unchecked")
	public static <X extends Service> X getService(Class<X> service) {

		if (!instance.services.containsKey(service))
			throw new IllegalStateException(getService(TranslationFactory.class).translate("system.error.service_offline", service.getName()));

		return (X) instance.services.get(service);
	}

	public static boolean isOnline(Class<? extends Service> service) {

		return instance.services.containsKey(service);
	}

	public static Meteor getInstance() {

		return instance;
	}

	public static ConfigurationContext getContext() {

		return instance.context;
	}

	public static String readEmbedded(String path) throws IOException {

		BufferedReader buffer = new BufferedReader(new InputStreamReader(Meteor.class.getClassLoader().getResourceAsStream(path), StandardCharsets.UTF_8));

		StringBuilder str = new StringBuilder();

		String line;
		while ((line = buffer.readLine()) != null) { str.append(line).append(System.lineSeparator()); }

		return str.substring(0, str.length() - 2).toString();

	}

	public static void ESTOP() {

		for (Player p : Bukkit.getOnlinePlayers()) {

			p.kick(Component.text(Meteor.isOnline(TranslationFactory.class) ? Meteor.getService(TranslationFactory.class).translate("system.error.fatal")
					: "A FATAL ERROR HAS OCCURRED. THE SERVER WILL SHUTDOWN TO PROTECT DATA INTEGRITY. CHECK THE LOGS."), Cause.PLUGIN);
		}

		Bukkit.shutdown();

	}
}

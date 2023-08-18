package fr.rsquatre.Meteor;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;

import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerKickEvent.Cause;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import fr.rsquatre.Meteor.service.translation.TranslationFactory;
import fr.rsquatre.Meteor.system.ConfigurationContext;
import fr.rsquatre.Meteor.system.ICriticalService;
import fr.rsquatre.Meteor.system.Logger;
import fr.rsquatre.Meteor.system.Service;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class Meteor extends JavaPlugin {

	private static Meteor instance = null;
	private static boolean loaded = false;

	private ConfigurationContext context = null;

	private HashSet<Class<? extends Service>> registeredServices = new HashSet<>();
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

	@Override
	public void onLoad() {

		loaded = true;
	}

	@Override
	public void onEnable() {

	}

	@Override
	public void onDisable() {

	}

	public static boolean register(Class<? extends Service> service) {

		// TODO

		instance.registeredServices.add(service);
		return true;
	}

	public static boolean load(Class<? extends Service> service) {

		return load(service, new Class[0], new Object[0]);
	}

	public static boolean load(Class<? extends Service> service, Class<?>[] argTypes, Object[] args) {

		try {

			if (!instance.registeredServices.contains(service))
				throw new IllegalArgumentException(
						Meteor.getService(TranslationFactory.class).translate("system.error.service_not_registered", service.getName()));

			if (instance.services.containsKey(service.getClass()))
				throw new IllegalStateException(
						Meteor.getService(TranslationFactory.class).translate("system.error.service_already_online", service.getName()));

			Service srv = service.getConstructor(argTypes).newInstance(args);

			if (srv.hasFailed())
				throw new IllegalStateException(Meteor.isOnline(TranslationFactory.class)
						? Meteor.getService(TranslationFactory.class).translate("system.error.service_faillure", service.getName())
						: String.format(
								"An instance of %s was successfuly created but the service is marked as failled and will not be accessible. Check the logs for errors.",
								service.getName()));

			instance.services.put(service, srv);
			Logger.log(Meteor.getService(TranslationFactory.class).translate("system.service_online", service.getName()));

		} catch (Exception e) {

			if (ICriticalService.class.isAssignableFrom(service)) {

				e.printStackTrace();
				Logger.fatal(Meteor.isOnline(TranslationFactory.class)
						? Meteor.getService(TranslationFactory.class).translate("system.error.critical_service_loading", service.getName())
						: String.format("An exception occurred while trying to load service %s. Meteor cannot operate properly without this service.",
								service.getName()));

			} else {

				Logger.error(Meteor.isOnline(TranslationFactory.class)
						? Meteor.getService(TranslationFactory.class).translate("system.error.service_loading", service.getName())
						: String.format("An exception occurred while trying to load service %s.", service.getName()));
				e.printStackTrace();
			}
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
					: "A FATAL ERROR HAS OCCURRED. THE SERVER WILL SHUTDOWN TO PROTECT DATA INTEGRITY. CHECK THE LOGS.", TextColor.color(200, 0, 0)),
					Cause.PLUGIN);
		}

		// If possible, get time to print the stacktrace in case it's called after
		// Logger#fatal
		if (loaded) {

			Bukkit.getScheduler().runTask(instance, () -> { Bukkit.shutdown(); });

		} else {
			Bukkit.shutdown();
		}

	}
}

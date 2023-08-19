package fr.rsquatre.Meteor.system;

import org.bukkit.plugin.java.JavaPlugin;

import fr.rsquatre.Meteor.Meteor;
import fr.rsquatre.Meteor.service.translation.TranslationFactory;

public abstract class Service {

	protected boolean failed = false;

	public Service() {

		if (Meteor.isOnline(getClass()))
			throw new IllegalStateException(Meteor.getService(TranslationFactory.class).translate("system.error.service_already_online_manual"));
	}

	public void load() {}

	public void unload() {}

	public boolean hasFailed() {
		return failed;
	}

	public String getName() {

		return Meteor.class.equals(getOwner()) ? "Meteor:".concat(getClass().getSimpleName()) : "Unkown:".concat(getClass().getSimpleName());
	}

	public abstract Class<? extends JavaPlugin> getOwner();
}

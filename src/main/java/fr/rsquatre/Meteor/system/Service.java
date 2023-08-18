package fr.rsquatre.Meteor.system;

import fr.rsquatre.Meteor.Meteor;
import fr.rsquatre.Meteor.service.translation.TranslationFactory;

public abstract class Service {

	protected boolean failed = false;

	public Service() {

		if (Meteor.isOnline(getClass()))
			throw new IllegalStateException(Meteor.getService(TranslationFactory.class).translate("system.error.service_already_online_manual"));
	}

	public boolean hasFailed() {
		return failed;
	}

	public String getName() {

		return "Meteor:".concat(getClass().getSimpleName());
	}
}

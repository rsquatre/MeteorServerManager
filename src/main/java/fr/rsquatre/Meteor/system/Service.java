package fr.rsquatre.Meteor.system;

import fr.rsquatre.Meteor.Meteor;
import fr.rsquatre.Meteor.service.translation.TranslationFactory;

public abstract class Service {

	public Service() {

		if (Meteor.getInstance().isOnline(getClass())) {

			throw new IllegalStateException(Meteor.getInstance().getService(TranslationFactory.class)
					.translate("system.error.serviceAlreadyOnline"));
		}
	}
}

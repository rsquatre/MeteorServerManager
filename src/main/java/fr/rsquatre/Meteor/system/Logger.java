/**
 *
 */
package fr.rsquatre.Meteor.system;

import fr.rsquatre.Meteor.Meteor;
import fr.rsquatre.Meteor.service.translation.TranslationFactory;

/**
 * @author <a href="https://github.com/rsquatre">rsquatre</a>
 *
 *         © All rights reserved, unless specified otherwise
 *
 */
public abstract class Logger {

	public static String log(Object str) {

		return info(str);
	}

	public static String info(Object str) {

		Meteor.getInstance().getLogger().info(str.toString());
		return str.toString();
	}

	public static String warn(Object str) {

		Meteor.getInstance().getLogger().warning(str.toString());
		return str.toString();
	}

	public static String error(Object str) {

		Meteor.getInstance().getLogger().severe(str.toString());
		return str.toString();
	}

	public static String fatal(Object str) {

		java.util.logging.Logger logger = Meteor.getInstance().getLogger();

		logger.severe("");
		logger.severe("");
		logger.severe(Meteor.isOnline(TranslationFactory.class) ? Meteor.getService(TranslationFactory.class).translate("system.error.fatal")
				: "A FATAL ERROR HAS OCCURRED. THE SERVER WILL SHUTDOWN TO PROTECT DATA INTEGRITY. CHECK THE LOGS.");
		logger.severe(str.toString());
		logger.severe("");
		logger.severe("");

		Meteor.ESTOP();

		return str.toString();
	}

}

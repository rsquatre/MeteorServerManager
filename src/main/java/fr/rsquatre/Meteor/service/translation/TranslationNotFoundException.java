package fr.rsquatre.Meteor.service.translation;

public class TranslationNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -395915497402249165L;
	
	private String locale;
	private String key;
	
	public TranslationNotFoundException(String locale, String key) {
		
		this.locale = locale;
		this.key = key;
	}
	
	@Override
	public String toString() {

		return String.format("Missing translation for locale %s with key %s", locale, key);
	}

}

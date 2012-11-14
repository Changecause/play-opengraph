package de.tfelix.play.opengraph;

import play.i18n.Lang;
import play.i18n.Messages;

/**
 * Stores a translatable string (translated via the i18n API of Play!
 * Framework). Objects an be added as well to fill in placeholder in the
 * translation string as documented in
 * http://www.playframework.org/documentation/2.0.2/JavaI18N 
 */
public class MetaTagValue {

	private String value;
	private Object[] args = null;

	public MetaTagValue(String str) {
		if(str == null || str.isEmpty()) {
			throw new IllegalArgumentException("Value can not be null or empty.");
		}
		
		this.value = str;
	}


	/**
	 * Bind new args to a possible translation. So the meta tags translation can
	 * later be changed.
	 * 
	 * @param args
	 */
	public void bindArgs(Object... args) {
		this.args = args;
	}

	@Override
	public String toString() {
		Lang lang;
		try {
			lang = OpengraphLanguage.getLanguage();
		} catch(RuntimeException ex) {
			// Request was not aviable. Mostly because we are in testing environment.
			// @TODO Look if this can not be handled by a advanced testcase rather then here in
			// code.
			lang = new Lang(Lang.defaultLang());
		}
		
		if (args == null) {
			return Messages.get(lang, value);
		} else {
			return Messages.get(lang, value, args);
		}
	}
	
	@Override
	public int hashCode() {
		return value.hashCode();
	}
	
	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || !(o instanceof MetaTagValue)) return false;
		
		MetaTagValue rhs = (MetaTagValue) o;
		return value.equals(rhs.value);
	}

}

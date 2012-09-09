package de.tfelix.play.opengraph;

import play.i18n.Messages;

import de.tfelix.play.opengraph.actions.OpengraphLanguage;

/**
 * Stores a translatable string (translated via the i18n API of Play!
 * Framework). Objects an be added as well to fill in placeholder in the
 * translation string as documented in
 * http://www.playframework.org/documentation/2.0.2/JavaI18N
 * 
 * @author Thomas Felix <thomas.felix@tfelix.de>
 * 
 */
public class MetaTagValue {

	private String value;
	private Object[] args = null;

	public MetaTagValue(String str) {
		this.value = str;
	}

	public MetaTagValue(String str, Object... args) {
		this.value = str;
		this.args = args;
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
		if (args == null) {
			return Messages.get(OpengraphLanguage.getLanguage(), value);
		} else {
			return Messages.get(OpengraphLanguage.getLanguage(), value, args);
		}
	}

}

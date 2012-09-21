package de.tfelix.play.opengraph;

import play.i18n.Lang;

/**
 * Used to store the information about the language and the original
 * requested language code inside the arguments of a request.
 *
 */
class LanguageStore {
	public Lang playLanguage;
	public String fbQueriedLanguage;
}

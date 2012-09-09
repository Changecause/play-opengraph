package de.tfelix.play.opengraph.actions;

import play.Logger;
import play.i18n.Lang;
import play.mvc.Http.Context;
import play.mvc.Http;

/**
 * This action retrieves the language which Facebook wants to get via a HTTP
 * Header and sets the appropriate Lang string for translating the meta tags
 * inside view.tags.facebookMeta.scala.html Notice: It sets just the string. The
 * creation of the lang object must be handled by the scala template. Since this
 * is the place where the supported facebook languages are stored too. So if new
 * languages are added later, the only changes which must be made are inside the
 * scala template.
 * 
 * @author Thomas Felix <thomas.felix@tfelix.de>
 * 
 */
public class OpengraphLanguage {

	static final private String LANGUAGE_KEY = "facebook-lang";

	/**
	 * Returns the Lang object which is requested by Facebook. Either the one
	 * requested by Facebook is delivered or the one which is in the language
	 * most appropriate for the current HTTP request.
	 * 
	 * @return
	 */
	public static Lang getLanguage() {
		Context ctx = Http.Context.current();
		if(ctx.args.containsKey(LANGUAGE_KEY)) {
			return (Lang) ctx.args.get(LANGUAGE_KEY);
		}
		
		String langCode = ctx.request().getHeader("X-Facebook-Locale");
		if (langCode != null && !langCode.isEmpty()) {
			// Strange behavior of Play i18n framework. If .de message file is
			// present it works if the browser defaults to de-DE but if I
			// explicitly call a message with de-DE language it fails. I have
			// to call especially for de as language code. So facebook language
			// codes must be shortened to the primary part as I don't user
			// country codes in my language files. This may be a bug in play.
			langCode = langCode.split("_")[0];
		}
		
		// If no explicit language request by Facebook, so give the language
		// dependent on the browser of the user.
		//String langCode = (String) Http.Context.current().args
		//		.get(LANGUAGE_KEY);
		Lang language;
		if (langCode == null || langCode.isEmpty()) {
			// Stupid workaround. play.i18n.defaultLang() returns a
			// play.api.i18n.Lang object, but a play.i18n.Lang is needed.
			language = Lang.forCode(Lang.defaultLang().language());
			Logger.debug("Test: " + language.code());
		} else {
			language = Lang.forCode(langCode);
		}

		ctx.args.put(LANGUAGE_KEY, language);
		return language;
	}
}

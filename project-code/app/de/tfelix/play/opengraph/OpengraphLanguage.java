package de.tfelix.play.opengraph;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import play.Logger;
import play.i18n.Lang;
import play.mvc.Http.Context;
import play.mvc.Http;

/**
 * This action retrieves the language which Facebook wants to get via a HTTP
 * header and sets the appropriate Lang object for translating the meta tags
 * inside view.opengraph.scala.html.
 */
class OpengraphLanguage {

	static final private String LANGUAGE_KEY = "facebook-lang";

	static final private Pattern LANGUAGE_REGEX = Pattern.compile("fb_locale=(\\w{2}_\\w{2})");

	private static String extractFacebookLanguageCode() {
		Context ctx = Http.Context.current();

		String langCode = ctx.request().getHeader("X-Facebook-Locale");
		if (langCode != null && !langCode.isEmpty()) {
			Logger.debug("X-Facebook-Locale Header found: " + langCode);
			return langCode;
		}

		// If code was not found, try to get it from the URL tag.
		Matcher matcher = LANGUAGE_REGEX.matcher(ctx.request().uri());
		while (matcher.find()) {
			langCode = matcher.group(1);
			Logger.debug("fb_locale found: " + langCode);
			return langCode;
		}
		return "";
	}

	/**
	 * Returns the Lang object which is requested by Facebook. Either the one
	 * requested by Facebook is delivered or the one which is in the language
	 * most appropriate for the current HTTP request (the language of the user
	 * visiting the page).
	 * 
	 * @return
	 */
	static Lang getLanguage() {
		Context ctx = Http.Context.current();
		if (!ctx.args.containsKey(LANGUAGE_KEY)) {
			createLanguage();
		}
		return ((LanguageStore) ctx.args.get(LANGUAGE_KEY)).playLanguage;
	}

	/**
	 * Tries to detect the language requested by Facebook and saves it in a cache.
	 */
	private static void createLanguage() {
		Context ctx = Http.Context.current();
		String facebookCode = extractFacebookLanguageCode();

		// If no explicit language request by Facebook, so give the language
		// dependent on the browser of the user.
		Lang language;
		if (!facebookCode.isEmpty()) {
			// Strange behavior of Play i18n framework. If .de message file is
			// present it works if the browser defaults to de-DE but if I
			// explicitly call a message with de-DE language it fails. I have
			// to call especially for de as language code. So Facebook language
			// codes must be shortened to the primary part as I don't user
			// country codes in my language files. This may be a bug in play.
			language = Lang.forCode(facebookCode.split("_")[0]);
		} else {
			// When no language from Facebook is requested just deliver the
			// default language of this website.
			language = new Lang(Lang.defaultLang());
		}

		// Create storage.
		LanguageStore store = new LanguageStore();
		store.playLanguage = language;
		store.fbQueriedLanguage = facebookCode;
		// Save it for later use in the same request, by the message translation
		// objects.
		ctx.args.put(LANGUAGE_KEY, store);
	}

	/**
	 * Returns the language code which was originally requested by Facebook or
	 * null otherwise.
	 * 
	 * @return
	 */
	static String getFacebookRequestCode() {
		// Check if the request language code was already cached. If not create the
		// cache.
		Context ctx;
		try {
			ctx = Http.Context.current();
		} catch(RuntimeException ex) {
			// No Context aviable. Return null.
			return null;
		}
		if (!ctx.args.containsKey(LANGUAGE_KEY)) {
			createLanguage();
		}
		return ((LanguageStore) ctx.args.get(LANGUAGE_KEY)).fbQueriedLanguage;
	}

}

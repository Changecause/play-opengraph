package de.tfelix.play.opengraph;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
 */
public class OpengraphLanguage {

	static final private String LANGUAGE_KEY = "facebook-lang";

	static final private Pattern LANGUAGE_REGEX = Pattern.compile("fb_locale=(\\w{2}_\\w{2})");

	private static String extractFacebookLanguageCode() {
		Context ctx = Http.Context.current();

		String langCode = ctx.request().getHeader("X-Facebook-Locale");
		if (langCode != null && !langCode.isEmpty()) {
			Logger.debug("Found X-Facebook-Locale Header! "+langCode);
			return langCode;
		}

		// If code was not found, try to get it from the URL tag.
		Matcher matcher = LANGUAGE_REGEX.matcher(ctx.request().uri());
		while (matcher.find()) {
			langCode = matcher.group(1);
			Logger.debug("Facebook Header 2nd: " + langCode);
			return langCode;
		}
		return "";
	}

	/**
	 * Returns the Lang object which is requested by Facebook. Either the one
	 * requested by Facebook is delivered or the one which is in the language
	 * most appropriate for the current HTTP request.
	 * 
	 * @return
	 */
	public static Lang getLanguage() {
		Context ctx = Http.Context.current();
		if (!ctx.args.containsKey(LANGUAGE_KEY)) {
			createLanguage();
		}
		return ((LanguageStore) ctx.args.get(LANGUAGE_KEY)).playLanguage;
	}

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
			// Stupid workaround. play.i18n.defaultLang() returns a
			// play.api.i18n.Lang object, but a play.i18n.Lang is needed.
			// This way we get the language of the current user visiting the
			// website.
			// language = Lang.preferred(ctx.request().acceptLanguages());

			// When no language from Facebook is requested just deliver the
			// default
			// Language of this website.
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
	public static String getFacebookRequestCode() {
		Context ctx = Http.Context.current();
		if (!ctx.args.containsKey(LANGUAGE_KEY)) {
			createLanguage();
		}
		return ((LanguageStore) ctx.args.get(LANGUAGE_KEY)).fbQueriedLanguage;
	}

}

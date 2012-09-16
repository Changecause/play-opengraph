package de.tfelix.play.opengraph;

import java.util.Arrays;
import java.util.Map;
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

	static final private Pattern LANGUAGE_REGEX = Pattern.compile("\fb_locale=(\\w{2}_\\w{2})");
	
	/**
	 * Returns the Lang object which is requested by Facebook. Either the one
	 * requested by Facebook is delivered or the one which is in the language
	 * most appropriate for the current HTTP request.
	 * 
	 * @return
	 */
	public static Lang getLanguage() {
		Context ctx = Http.Context.current();
		
		// DEBUG: Print the Header.
		//Logger.info("Path: "+ctx.request().path());
		//Logger.info("URI: "+ctx.request().uri());
		
		/*for(Map.Entry<String, String[]> entry : ctx.request().headers().entrySet()) {
			Logger.info(entry.getKey()+": "+Arrays.asList(entry.getValue()).toString());			
		}*/

		Logger.info("Facebook Header: "+ctx.request().getHeader("X-Facebook-Locale"));
		
		// See if the language was already parsed and cached for this request.
		if(ctx.args.containsKey(LANGUAGE_KEY)) {
			//Logger.info("Facebook language detected: "+ ctx.args.get(LANGUAGE_KEY));
			return (Lang) ctx.args.get(LANGUAGE_KEY);
		}
		
		String langCode = ctx.request().getHeader("X-Facebook-Locale");		
		// If code was not found, try to get it from the URL tag.
		if(langCode == null || langCode.isEmpty()) {
			Matcher matcher = LANGUAGE_REGEX.matcher(ctx.request().uri());
			while(matcher.find()) {
				String match = matcher.group(1);
				Logger.info(match);
				langCode = match;
				
				Logger.info("Facebook Header 2nd: "+langCode);
				break;
			}
		}
		
		// If no explicit language request by Facebook, so give the language
		// dependent on the browser of the user.
		Lang language;
		if (langCode != null && !langCode.isEmpty()) {
			// Strange behavior of Play i18n framework. If .de message file is
			// present it works if the browser defaults to de-DE but if I
			// explicitly call a message with de-DE language it fails. I have
			// to call especially for de as language code. So facebook language
			// codes must be shortened to the primary part as I don't user
			// country codes in my language files. This may be a bug in play.
			langCode = langCode.split("_")[0];
			language = Lang.forCode(langCode);
		} else {
			// Stupid workaround. play.i18n.defaultLang() returns a
			// play.api.i18n.Lang object, but a play.i18n.Lang is needed.
			language = new Lang(Lang.defaultLang());
			Logger.info("First language: "+language.toString());
			//language = Lang.preferred(Lang.availables());
			Logger.info("Second language: "+Lang.preferred(Lang.availables()));
			//Lang.forCode(Lang.defaultLang().language());
		}

		//Logger.info("Facebook language detected: "+language.toString());
		ctx.args.put(LANGUAGE_KEY, language);
		return language;
	}
}

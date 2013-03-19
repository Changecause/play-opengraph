import de.tfelix.play.opengraph.MetaTag;
import de.tfelix.play.opengraph.MetaTagSet;
import de.tfelix.play.opengraph.Opengraph;
import play.Application;
import play.GlobalSettings;

public class Global extends GlobalSettings {

	@Override
	public void onStart(Application app) {
		// Opengraph Tags are initialized at the app startup.

		// Insert permanent MetaTags which will be displayed on ALL websites.
		// They can be overridden by page tags which define a special route.
		Opengraph.insertPermanentTag(new MetaTag("og:site_name", "My Website"));
		Opengraph.insertPermanentTag(new MetaTag("fb:app_id", "1234567"));
		Opengraph.insertPermanentTag(new MetaTag("og:image", "http://url.to.your.site/picture.png"));
		Opengraph.insertPermanentTag(new MetaTag("og:url", "http://url.to.your.site"));
		Opengraph.insertPermanentTag(new MetaTag("og:title", "facebook.title"));
		Opengraph.insertPermanentTag(new MetaTag("og:type", "website"));
		// Notice: facebook.pageDescription is linking to a translation in a message file.
		Opengraph.insertPermanentTag(new MetaTag("og:description", "facebook.pageDescription"));
		Opengraph.insertPermanentTag(new MetaTag("og:locale", "en_us"));
		// More then one alternate local can be added for multilanguage support.
		Opengraph.insertPermanentTag(new MetaTag("og:locale:alternate", "de_de"));
		Opengraph.insertPermanentTag(new MetaTag("og:locale:alternate", "en_us"));

		MetaTagSet mts = new MetaTagSet();
		mts.add(new MetaTag("og:description", "facebook.pageDescription2"));
		Opengraph.insertTag("/page", mts);
	}

}

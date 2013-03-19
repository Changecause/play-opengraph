package de.tfelix.play.opengraph.views;

import static play.test.Helpers.running;
import static play.test.Helpers.testServer;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import play.libs.WS;
import de.tfelix.play.opengraph.MetaTag;
import de.tfelix.play.opengraph.MetaTagSet;
import de.tfelix.play.opengraph.Opengraph;

public class OpengraphTest {

	@BeforeClass
	public static void oneTimeSetUp() {
		Opengraph.clearAllTags();
		Opengraph.insertPermanentTag(new MetaTag("og:title", "Title"));
		MetaTagSet tagset = new MetaTagSet();
		tagset.add(new MetaTag("og:description", "This is a small description of the site."));
		Opengraph.insertTag("", tagset);
	}

	@Test
	public void renderTemplateInServer() {
		running(testServer(3333), new Runnable() {

			@Override
			public void run() {
				Assert.assertTrue(WS.url("http://localhost:3333").get().get().getBody().contains("og:description"));
			}
		});
	}
}

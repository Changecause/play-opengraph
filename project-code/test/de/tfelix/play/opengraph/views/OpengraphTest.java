package de.tfelix.play.opengraph.views;

import java.util.HashMap;

import org.junit.BeforeClass;
import org.junit.Test;

import de.tfelix.play.opengraph.MetaTag;
import de.tfelix.play.opengraph.MetaTagSet;
import de.tfelix.play.opengraph.Opengraph;
import de.tfelix.play.opengraph.views.html.opengraph;

import play.mvc.Content;
import play.mvc.Http.Context;
import static play.test.Helpers.*;
import static org.fest.assertions.Assertions.*;

public class OpengraphTest {

	@BeforeClass
	public static void oneTimeSetUp() {
		Opengraph.clearAllTags();
		Opengraph.insertPermanentTag(new MetaTag("og:title", "Title"));
		MetaTagSet tagset = new MetaTagSet();
		tagset.add(new MetaTag("og:description", "This is a small description of the site."));
		Opengraph.insertTag("/index", tagset);
	}

	/* @Test
	 Removed until i figure out a way to call a single template with  context.
	public void renderTemplate() {
		running(fakeApplication(), new Runnable() {
			public void run() {
				Context.current.set(new Context(null,
						new HashMap<String, String>(), 
						new HashMap<String, String>()));
				
				
				Content html = opengraph.render();
				assertThat(contentType(html)).isEqualTo("text/html");
				assertThat(contentAsString(html)).contains("og:title");
				assertThat(contentAsString(html)).contains("od:description");
			}
		});

	}*/
}

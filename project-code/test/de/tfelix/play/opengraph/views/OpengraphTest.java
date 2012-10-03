package de.tfelix.play.opengraph.views;

import org.junit.BeforeClass;
import org.junit.Test;

import de.tfelix.play.opengraph.MetaTag;
import de.tfelix.play.opengraph.Opengraph;
import de.tfelix.play.opengraph.views.html.opengraph;

import play.mvc.Content;
import static play.test.Helpers.*;
import static org.fest.assertions.Assertions.*;

public class OpengraphTest {
	
	 @BeforeClass
     public static void oneTimeSetUp() {
         Opengraph.clearAllTags();
         Opengraph.insertPermanentTag(new MetaTag("og:title", "Title"));
         Opengraph.insertTag("/index", new MetaTag("og:description", "This is a small description of the site."));
     }
	
	@Test
	public void renderTemplate() {
	    Content html = opengraph.render();
	    assertThat(contentType(html)).isEqualTo("text/html");
	    assertThat(contentAsString(html)).contains("og:title");
	    assertThat(contentAsString(html)).contains("od:description");
	}
}

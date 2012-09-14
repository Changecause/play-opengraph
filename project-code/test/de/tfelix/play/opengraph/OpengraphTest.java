package de.tfelix.play.opengraph;

import org.junit.Assert;
import org.junit.Test;

public class OpengraphTest {

	@Test
	public void Adding_Tag() {
		Assert.assertFalse("Openpgraph should contain no tags.", Opengraph.getTags().iterator().hasNext());
		Opengraph.insertPermanentTag(new MetaTag("og:title", "Test"));
		Assert.assertTrue("Openpraph should contain added tag.", Opengraph.getTags().iterator().hasNext());
	}
	
	@Test
	public void Special_Tag_Hides_Permanent() {
		MetaTag insertTag =  new MetaTag("og:title", "HelloWorld");
		Opengraph.insertTag("/index", insertTag);
		MetaTag tag = Opengraph.getTags("/index").iterator().next();
		Assert.assertTrue("MetaTags should be identical.", insertTag.equals(tag));
	}
	
	@Test
	public void Adding_Tag_Twice_Different_Content() {
		MetaTag tag2 = new MetaTag("og:title", "HelloWorld2");
		Opengraph.insertTag("/index", tag2);
		boolean found = false;
		for(MetaTag tag : Opengraph.getTags("/index")) {
			if(tag.equals(tag2)) {
				found = true;
				break;
			}
		}
		Assert.assertTrue("Tag was not found.", found);
	}
}

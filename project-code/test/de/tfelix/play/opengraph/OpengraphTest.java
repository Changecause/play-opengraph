package de.tfelix.play.opengraph;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class OpengraphTest {

	@Before
	public void ClearOpengraph() {
		Opengraph.clearAllTags();
	}

	@Test
	public void Adding_Tag() {
		Assert.assertTrue("Openpgraph should contain no tags.", Opengraph.getTags().size() == 0);
		MetaTag tag = new MetaTag("og:title", "Test");
		Opengraph.insertPermanentTag(tag);
		Assert.assertTrue("Openpraph should contain added tag.", Opengraph.getTags().contains(tag));
	}
	
	/*
	 * Beheaviour of locale is not 100% clear I will add unit tests and finalize behaviour when I am 
	 * sure about this.
	@Test
	public void Adding_Locale_Permanent() {
		MetaTag tag = new MetaTag("og:locale", "de_DE");
		Opengraph.insertPermanentTag(tag);
		List<MetaTag> tags = Opengraph.getTags();
		Assert.assertTrue(tags.contains(tag) && tags.size() == 1);
	}
	
	@Test
	public void Adding_Locale_Pagespecific() {
		MetaTag tag1 = new MetaTag("og:locale", "de_DE");
		MetaTag tag1 = new MetaTag("og:locale", "de_DE");
		Opengraph.insertPermanentTag(tag);
		List<MetaTag> tags = Opengraph.getTags();
		Assert.assertTrue(tags.contains(tag) && tags.size() == 1);
	}
	
	@Test
	public void Adding_Locale_Twice() {
		
	}*/

	@Test
	public void Special_Tag_Hides_Permanent() {
		MetaTag insertTag = new MetaTag("og:title", "HelloWorld");
		Opengraph.insertTag("/index", insertTag);
		List<MetaTag> tags = Opengraph.getTags("/index");
		Assert.assertTrue("MetaTags should be identical.", tags.contains(insertTag));
	}

	@Test
	public void Adding_Non_Allowed_Permanent_Tag_Twice() {
		MetaTag tag1 = new MetaTag("og:title", "Test 1");
		MetaTag tag2 = new MetaTag("og:title", "Test 2");
		Opengraph.insertPermanentTag(tag1);
		Opengraph.insertPermanentTag(tag2);
		List<MetaTag> tags = Opengraph.getTags();
		// One can not use .contains since MetaTag equal each other only by the content.
		// Check the length of the returned list.
		Assert.assertTrue(tags.contains(tag1)  && tags.size() == 1);
	}

	@Test
	public void Adding_Allowed_Tag_Twice_Permanent() {
		MetaTag tag2 = new MetaTag("og:locale:alternate", "de_DE");
		MetaTag tag1 = new MetaTag("og:locale:alternate", "en_US");
		Opengraph.insertPermanentTag(tag1);
		Opengraph.insertPermanentTag(tag2);
		List<MetaTag> tags = Opengraph.getTags();
		Assert.assertTrue(tags.contains(tag1) && tags.contains(tag2));
	}

	@Test
	public void Adding_Allowed_Tag_Twice_Page() {
		MetaTag tag2 = new MetaTag("og:locale:alternate", "de_DE");
		MetaTag tag1 = new MetaTag("og:locale:alternate", "en_US");
		Opengraph.insertTag("/index", tag1);
		Opengraph.insertTag("/index", tag2);
		List<MetaTag> tags = Opengraph.getTags("/index");
		Assert.assertTrue("Both tags should be included.", tags.contains(tag1) && tags.contains(tag2));
	}

	@Test
	public void Getting_Tags_Specific_Page() {
		MetaTag tag1 = new MetaTag("og:locale:alternate", "en_US");
		MetaTag tag2 = new MetaTag("og:locale:alternate", "de_DE");
		MetaTag tag3 = new MetaTag("og:title", "Testtitle");
		Opengraph.insertTag("/index", tag1);
		Opengraph.insertTag("/page", tag2);
		Opengraph.insertPermanentTag(tag3);
		List<MetaTag> tags = Opengraph.getTags("/page");
		// Should contain only page specific and permanent tags.
		Assert.assertTrue("Should contain only page specific tags.", tags.contains(tag2) && tags.contains(tag3));
		
		tags = Opengraph.getTags("/index");
		Assert.assertTrue("Should contain only /index page tags.", tags.contains(tag1) && tags.contains(tag3));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void Getting_Page_Tags_Null() {
		Opengraph.getTags(null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void Getting_Page_Tags_Empty() {
		Opengraph.getTags("");
	}

	@Test
	public void Getting_All_Tags() {
		MetaTag tag1 = new MetaTag("og:title", "Test");
		MetaTag tag2 = new MetaTag("og:description", "Test Description");
		Opengraph.insertTag("/index", tag1);
		Opengraph.insertPermanentTag(tag2);
		List<MetaTag> tags = Opengraph.getTags();
		Assert.assertTrue("Only permanent tag should be included.", tags.contains(tag2) && !tags.contains(tag1));
		tags = Opengraph.getTags("/index");
		Assert.assertTrue("Both tags should be included.", tags.contains(tag1) && tags.contains(tag2));
	}
}

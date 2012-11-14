package de.tfelix.play.opengraph;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import play.Logger;


public class OpengraphTest {

	@Before
	public void ClearOpengraph() {
		Opengraph.clearAllTags();
	}

	@Test
	public void Adding_Tag() {
		Assert.assertTrue("Openpgraph should contain no tags.", Opengraph.getTags().toString().isEmpty());
		MetaTag tag = new MetaTag("og:title", "Test");
		Opengraph.insertPermanentTag(tag);
		Assert.assertTrue("Openpraph should contain added tag.", Opengraph.getTags().toString().contains(tag.getProperty()));
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
		MetaTagSet tags = Opengraph.getTags("/index");
		Assert.assertTrue("MetaTags should be identical.", tags.toString().contains(insertTag.getProperty()));
	}

	@Test
	public void Adding_Non_Allowed_Permanent_Tag_Twice() {
		MetaTag tag1 = new MetaTag("og:title", "Test 1");
		MetaTag tag2 = new MetaTag("og:title", "Test 2");
		Opengraph.insertPermanentTag(tag1);
		Opengraph.insertPermanentTag(tag2);
		MetaTagSet tags = Opengraph.getTags();
		// One can not use .contains since MetaTag equal each other only by the content.
		// Check the length of the returned list.
		Logger.debug(tags.toString());
		Assert.assertTrue(tags.toString().contains(tag2.getContent().toString()));
	}

	@Test
	public void Adding_Allowed_Tag_Twice_Permanent() {
		MetaTag tag2 = new MetaTag("og:locale:alternate", "de_DE");
		MetaTag tag1 = new MetaTag("og:locale:alternate", "en_US");
		Opengraph.insertPermanentTag(tag1);
		Opengraph.insertPermanentTag(tag2);
		MetaTagSet tags = Opengraph.getTags();
		Assert.assertTrue(tags.toString().contains(tag1.getProperty()) && tags.toString().contains(tag2.getProperty()));
	}

	@Test
	public void Adding_Allowed_Tag_Twice_Page() {
		MetaTag tag2 = new MetaTag("og:locale:alternate", "de_DE");
		MetaTag tag1 = new MetaTag("og:locale:alternate", "en_US");
		Opengraph.insertTag("/index", tag1);
		Opengraph.insertTag("/index", tag2);
		MetaTagSet tags = Opengraph.getTags("/index");
		Assert.assertTrue("Both tags should be included.", 
				tags.toString().contains(tag1.getProperty()) && 
				tags.toString().contains(tag2.getProperty()));
	}
	
	@Test
	public void Adding_Permenant_Overwriting_With_Single_Page() {
		Opengraph.insertPermanentTag(new MetaTag("og:description", "Small Test"));
		Opengraph.insertPermanentTag(new MetaTag("og:site_name", "Website"));
		Opengraph.insertPermanentTag(new MetaTag("og:app_id", "123456"));
		Opengraph.insertPermanentTag(new MetaTag("og:locale:alternate", "de_DE"));
		
		MetaTagSet set = new MetaTagSet();
		set.add(new MetaTag("og:site_name", "OVERWRITE"));
		set.add(new MetaTag("og:locale:alternate", "en_US"));
		Opengraph.insertTag("/index", set);
		
		MetaTagSet tags = Opengraph.getTags("/index");
		// Single tags should be overwritten.
		String result = tags.toString();
		Assert.assertFalse("og:site_name should have been overridden.", result.contains("Website"));
		Assert.assertTrue("og:site_name should be OVERWRITE", result.contains("OVERWRITE"));
		// Should contain both languages.
		Assert.assertTrue("Should contain both alt. languages.", result.contains("de_DE") && result.contains("en_US"));
		
	}

	@Test
	public void Getting_Tags_Specific_Page() {
		MetaTag tag1 = new MetaTag("og:locale:alternate", "en_US");
		MetaTag tag2 = new MetaTag("og:locale:alternate", "de_DE");
		MetaTag tag3 = new MetaTag("og:title", "Testtitle");
		Opengraph.insertTag("/index", tag1);
		Opengraph.insertTag("/page", tag2);
		Opengraph.insertPermanentTag(tag3);
		MetaTagSet tags = Opengraph.getTags("/page");
		// Should contain only page specific and permanent tags.
		Assert.assertTrue("Should contain only page specific tags.", 
				tags.toString().contains(tag2.getProperty()) &&
				tags.toString().contains(tag3.getProperty()));
		
		tags = Opengraph.getTags("/index");
		Assert.assertTrue("Should contain only /index page tags.", 
				tags.toString().contains(tag1.getProperty()) && 
				tags.toString().contains(tag3.getProperty()));
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
		MetaTagSet tags = Opengraph.getTags();
		Assert.assertTrue("Only permanent tag should be included.", 
				tags.toString().contains(tag2.getProperty()) && 
				!tags.toString().contains(tag1.getProperty()));
		tags = Opengraph.getTags("/index");
		Assert.assertTrue("Both tags should be included.", 
				tags.toString().contains(tag1.toString()) && 
				tags.toString().contains(tag2.toString()));
	}
	
	@Test
	public void Add_MetaTagSet() {
		MetaTagSet set = new MetaTagSet();
		set.add(new MetaTag("og:title", "Test1"));
		Opengraph.insertTag("/pages/view", set);
		set = Opengraph.getTags("example.com/pages/view/1");
		Assert.assertTrue(set.toString().contains("og:title"));
	}
	
	/**
	 * This test will check if direct URLs like http://example.com/MYID will work as required
	 * for a webproject of mine.
	 */
	@Test
	public void Regex_Changing_Short_Url() {
		MetaTagSet set = new MetaTagSet();
		set.add(new MetaTag("og:title", "Test1"));
		Opengraph.insertTag("/\\w+", set);
		set = new MetaTagSet();
		set.add(new MetaTag("og:description", "Test description"));
		Opengraph.insertTag("/images/id", set);
		set = Opengraph.getTags("example.com/TEST123");
		Assert.assertTrue("Only the title tag should be found",
				set.toString().contains("og:title") && !set.toString().contains("og:description"));
	}
}

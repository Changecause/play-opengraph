package de.tfelix.play.opengraph;

import org.junit.Assert;
import org.junit.Test;

public class MetaTagSetTest {

	/**
	 * Both tests should be combined. If a single tag is added the second time,
	 * the first one should be overwritten.
	 */
	@Test
	public void Combine_Two_Sets() {

	}

	@Test
	public void Add_Tag() {
		MetaTagSet mts = new MetaTagSet();
		Assert.assertTrue(mts.size() == 0);
		mts.add(new MetaTag("og:url", "www.google.de"));
		Assert.assertTrue(mts.toString().contains("www.google.de") && mts.size() == 1);
		
	}

	@Test
	public void Add_Single_Tag_Twice() {
		MetaTagSet mts = new MetaTagSet();
		Assert.assertTrue(mts.size() == 0);
		mts.add(new MetaTag("og:url", "www.google.de"));
		mts.add(new MetaTag("og:url", "www.facebook.com"));
		Assert.assertFalse("First tag was not overwritten.", mts.toString().contains("www.google.de"));
		Assert.assertTrue(mts.toString().contains("www.facebook.com") && mts.size() == 1);
	}
	
	@Test
	public void Add_Multi_Tag_Twice() {
		MetaTagSet mts = new MetaTagSet();
		Assert.assertTrue(mts.size() == 0);
		mts.add(new MetaTag("og:locale:alternate", "en_US"));
		mts.add(new MetaTag("og:locale:alternate", "de_DE"));
		Assert.assertTrue("Should contain two tags.", mts.size() == 2);
		Assert.assertTrue(mts.toString().contains("en_US") && mts.toString().contains("de_DE"));
	}

}

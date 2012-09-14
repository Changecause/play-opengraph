package de.tfelix.play.opengraph;

import org.junit.Test;

@SuppressWarnings("unused")
public class MetaTagTest {
	
	@Test(expected=IllegalArgumentException.class)
	public void Empty_Property() {
		MetaTag tag = new MetaTag("", "Test");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void Empty_Content() {
		MetaTag tag = new MetaTag("Test", "");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void Null_Property() {
		MetaTag tag = new MetaTag(null, "Test");
	}
	
	@Test
	public void Normal_Creation() {
		MetaTag tag = new MetaTag("test", "test");
	}
}

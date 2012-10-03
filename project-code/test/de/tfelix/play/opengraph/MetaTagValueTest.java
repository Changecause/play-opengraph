package de.tfelix.play.opengraph;

import org.junit.Assert;
import org.junit.Test;

@SuppressWarnings("unused")
public class MetaTagValueTest {
	
	@Test(expected=IllegalArgumentException.class)
	public void Empty_Name() {
		MetaTagValue val = new MetaTagValue("");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void Null_Value() {
		MetaTagValue val = new MetaTagValue(null);
	}
	
	public void Check_Equal() {
		MetaTagValue val1 = new MetaTagValue("test");
		MetaTagValue val2 = new MetaTagValue("test");
		Assert.assertTrue(val1.equals(val1));
		Assert.assertTrue(val1.equals(val2));
	}
	
	
}

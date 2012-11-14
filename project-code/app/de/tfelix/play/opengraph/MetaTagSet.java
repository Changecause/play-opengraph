package de.tfelix.play.opengraph;

import java.util.HashSet;
import java.util.LinkedList;
import play.Logger;

/**
 * The MetaTagSet manages the OpenGraph metatags. Some tags can be included more
 * then once, some must be unique. The MetaTagSet will enforce this constraints.
 * You have to add all of your tags to this managment class and register it at
 * the static Opengraph manager.
 * 
 */
public class MetaTagSet {
	/**
	 * Tags properties which are allowed to be added twice.
	 */
	private static final HashSet<String> ALLOWED_DOUBLE_TAGS = new HashSet<String>();
	static {
		ALLOWED_DOUBLE_TAGS.add("og:locale:alternate");
	}

	/**
	 * Caches the tags which where already added to the MetaTag set in order to
	 * have near constant look up which is important to check if a tag was
	 * already included.
	 */
	private HashSet<String> metaTagsCache = new HashSet<String>();
	private LinkedList<MetaTag> metaTags = new LinkedList<MetaTag>();

	/**
	 * Std. Ctor.
	 * Creates an empty MetaTagSet.
	 */
	public MetaTagSet() {
		
	}
	
	/**
	 * Copy Ctor.
	 * Will copy the content of the rhs to the current new MetaTagSet.
	 * @param rhs
	 */
	public MetaTagSet(MetaTagSet rhs) {
		if(rhs == null) {
			throw new IllegalArgumentException("Rhs can not be null.");
		}
		
		// Copy the important stuff.
		metaTags.addAll(rhs.metaTags);
		metaTagsCache.addAll(rhs.metaTagsCache);
		
	}

	/**
	 * Binds new args to the MetaTags which are chosen with a key and a property
	 * value. These arguments will be used for filling variable placeholder in
	 * the translation string.<br>
	 * ATTENTION: Also the permanentMetaTags are also tried to bind to a new
	 * value, if the match the given property.
	 * 
	 * @param page
	 * @param propertie
	 * @param args
	 */
	public void bindArgs(String property, Object... args) {
		for (MetaTag tag : metaTags) {
			if (tag.getProperty().contains(property)) {
				tag.getContent().bindArgs(args);
			}
		}
	}

	/**
	 * Prints the meta tags of this tag set in standard HTML. So it can directly
	 * included in a template.
	 */
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (MetaTag tag : metaTags) {
			builder.append(tag.toString()).append("\n");
		}
		return builder.toString();
	}

	/**
	 * Adds all MetaTags from the set to the current set. This method will
	 * combine the sets in a safe way. Allowed multi tags will be included
	 * multiple times while single tags will overwrite the current tags-
	 * 
	 * @param set
	 *            Adds all parameter from the set to the current set.
	 */
	public void add(MetaTagSet rhs) {		
		// Now iterate over the given rhs set and see if the tags
		// can be added.
		for(MetaTag tag : rhs.metaTags) {
			add(tag);			
		}
	}
	
	public void add(MetaTag tag) {
		if (tag == null) {
			throw new IllegalArgumentException("tag can not be null");
		}

		// If a tag which can not be added multiple times overwrite it with the
		// new version.
		if (!ALLOWED_DOUBLE_TAGS.contains(tag.getProperty()) && metaTagsCache.contains(tag.getProperty())) {
			Logger.debug("MetaTag " + tag.toString() + " was already included. Will be overwritten.");
			metaTags.remove(tag);
		}

		metaTagsCache.add(tag.getProperty());
		metaTags.add(tag);
	}

	/**
	 * Lists the number of MetaTags which are currently inside this set.
	 * 
	 * @return
	 */
	public int size() {
		return metaTags.size();
	}
}

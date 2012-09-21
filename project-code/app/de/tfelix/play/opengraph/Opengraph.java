package de.tfelix.play.opengraph;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import play.Logger;

/**
 * Saves sets of MetaTags indexed via a String key. The tag collection can later
 * be requested and printed to the desired set of meta HTML tags.
 */
public class Opengraph {
	
	/**
	 * Private ctor. This class should not be instantiated but used in a static
	 * way.
	 */
	private Opengraph() {
		// no op
	}

	/**
	 * Keyword for the MetaTags which are selected when no specific key is
	 * requested.
	 */
	private static final String DEFAULT_KEY = "default";
	
	//private static final 
	private static final HashSet<String> ALLOWED_DOUBLE_TAGS
		= new HashSet<String>();
	static {
		ALLOWED_DOUBLE_TAGS.add("og:locale:alternate");
	}
	
	/**
	 * Tag which holds the locale will be saved as a standalone to be replacable
	 * if a special language is requested from Facebook.
	 */
	private static MetaTag localeTag = null;

	private static Map<String, HashSet<MetaTag>> metaTagsCache 
		= new HashMap<String, HashSet<MetaTag>>();

	/**
	 * The permanent meta tags are always added to the requested tags list. Some
	 * meta tags are mandatory by Facebook. Tags which should be included in ALL
	 * pages regardless of a key can be added to this list. They will be
	 * delivered every time a collection of tags is requested.
	 */
	private static HashSet<MetaTag> permanentMetaTags
		= new HashSet<MetaTag>();

	/**
	 * Permanent tags will be included in ALL requests of the meta tags.
	 * Adding twice the came tag will not work. A tag is considered equal
	 * if its property and its value are identical.
	 * 
	 * @param tag
	 */
	public static void insertPermanentTag(MetaTag tag) {
		if(tag == null) {
			throw new IllegalArgumentException("tag can not be null");
		}
		if(tag.getProperty().equals("og:locale")) {
			localeTag = tag;
		} else {
			permanentMetaTags.add(tag);
		}
	}

	/**
	 * Inserts a tag for the given page key. With this key all tags can later be
	 * retrieved. Tags with the same property but a specialized page url will override
	 * permanent tags.
	 * 
	 * @param page
	 * @param tag
	 */
	public static void insertTag(String page, MetaTag tag) {		
		if(page == null) {
			throw new IllegalArgumentException("Page can not be null.");
		}
		if(tag == null) {
			throw new IllegalArgumentException("Metatag can not be null.");
		}
		
		if (metaTagsCache.get(page) == null) {
			// Empty list. Create a new.
			metaTagsCache.put(page, new HashSet<MetaTag>());
		}
		metaTagsCache.get(page).add(tag);
	}

	/**
	 * Binds new args to the MetaTags which are chosen with a key and a
	 * property value.<br>
	 * ATTENTION: Also the permanentMetaTags are also tried to bind to a new
	 * value, if the match the given property.
	 * 
	 * @param page
	 * @param propertie
	 * @param args
	 */
	public static void bindArgs(String page, String propertie, Object... args) {
		HashSet<MetaTag> tags = metaTagsCache.get(page);
		for(MetaTag tag : tags) {
			if(tag.getProperty().contains(propertie)) {
				tag.getContent().bindArgs(args);
			}
		}
	}

	/**
	 * Returns the same as getTags(String key) but only the default tags, saved
	 * under the default key.
	 * 
	 * @return
	 * @throws Exception
	 */
	public static Iterable<MetaTag> getTags() {
		return getTags(DEFAULT_KEY);
	}

	/**
	 * Returns a unmodifiable list (read only) of the requested tags in addition
	 * to the required tags.
	 * 
	 * @param page
	 * @return
	 */
	public static Iterable<MetaTag> getTags(String page) {
		if(page == null) {
			throw new IllegalArgumentException("Page can not be null.");
		}
		Logger.debug("Get tags for key: "+page);
		
		HashSet<String> hits = new HashSet<String>();
		List<MetaTag> tmp = new LinkedList<MetaTag>();

		// Iterate over all other tags and try to find one which matches the given key.
		// The problem is the Framework does not give us the current path without parameter.
		// So we have to iterate over and try to match.
		Iterator<Entry<String, HashSet<MetaTag>>> it = metaTagsCache.entrySet().iterator();
	    Set<MetaTag> foundTags = null;
		while (it.hasNext()) {
	        Map.Entry<String, HashSet<MetaTag>> pairs = it.next();
	        if(page.contains(pairs.getKey())) {
	        	foundTags = pairs.getValue();
	        	break;
	        }
	    }
		
		// Get the current language and add a locale tag for it.
		String facebookLanguageCode = OpengraphLanguage.getFacebookRequestCode();
		if(facebookLanguageCode != null && !facebookLanguageCode.isEmpty()) {
			// Add this code to the taglist.
			tmp.add(new MetaTag("og:locale", OpengraphLanguage.getFacebookRequestCode()));
		} else {
			// otherwise add the default tag to the list.
			tmp.add(localeTag);
		}
		// Locale is always added.
		hits.add("og:locale");
		
		if(foundTags != null) {
			// Add all of the found matching tags.
			tmp.addAll(foundTags);
			for(MetaTag metaTag : foundTags) {
				hits.add(metaTag.getProperty());
			}
		}
		
		for (MetaTag metaTag : permanentMetaTags) {
			// If this meta tag was already included in the result, do not add it
			// again UNTIL it is a tag for which double inclusion is allowed.
			if(hits.contains(metaTag.getProperty()) && !isMultiTag(metaTag)) {
				continue;
			}
			tmp.add(metaTag);
		}
		
		return Collections.unmodifiableList(tmp);
	}
	
	private static boolean isMultiTag(MetaTag metaTag) {
		return ALLOWED_DOUBLE_TAGS.contains(metaTag.getProperty());
	}
}

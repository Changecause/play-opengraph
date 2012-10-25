package de.tfelix.play.opengraph;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Saves and manages sets of MetaTags indexed via a String key. The tag
 * collection can later be requested and printed to the desired set of meta HTML
 * tags. This class is the entry point for registering meta tags which should
 * show up on the different pages.
 */
public final class Opengraph {

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

	/**
	 * Tag which holds the locale will be saved as a standalone to be
	 * replaceable if a special language is requested from Facebook.
	 */
	private static MetaTag localeTag = null;

	/**
	 * MetaTagSets are saved in respect to a specific route.
	 */
	private static Map<String, MetaTagSet> metaTagsCache = new HashMap<String, MetaTagSet>();

	/**
	 * The permanent meta tags are always added to the requested tags list. Some
	 * meta tags are mandatory by Facebook. Tags which should be included in ALL
	 * pages regardless of a key can be added to this list. They will be
	 * delivered every time a collection of tags is requested.
	 */
	private static MetaTagSet permanentMetaTags = new MetaTagSet();

	/**
	 * Permanent tags will be included in ALL requests of the meta tags. Adding
	 * twice the came tag will not work. A tag is considered equal if its
	 * property and its value are identical.
	 * 
	 * @param tag
	 */
	public static void insertPermanentTag(MetaTag tag) {
		permanentMetaTags.add(tag);
	}

	/**
	 * Inserts a tag for the given page key. With this key all tags can later be
	 * retrieved. Tags with the same property but a specialized page url will
	 * override permanent tags.
	 * 
	 * @param page
	 * @param tag
	 */
	public static void insertTag(String page, MetaTag tag) {
		if (page == null || page.isEmpty()) {
			throw new IllegalArgumentException("Page can not be null or empty.");
		}

		if (metaTagsCache.get(page) == null) {
			// Empty list. Create a new.
			metaTagsCache.put(page, new MetaTagSet());
		}

		metaTagsCache.get(page).add(tag);
	}

	/**
	 * Returns the same as getTags(String key) but only the default tags, saved
	 * under the default key.
	 * 
	 * @return
	 * @throws Exception
	 */
	public static MetaTagSet getTags() {
		return getTags(DEFAULT_KEY);
	}

	/**
	 * Returns a unmodifiable list (read only) of the requested tags in addition
	 * to the required tags.
	 * 
	 * @param page
	 * @return
	 */
	public static MetaTagSet getTags(String page) {
		if (page == null || page.isEmpty()) {
			throw new IllegalArgumentException("Page can not be null or empty.");
		}
		
		MetaTagSet result = new MetaTagSet();
		// Add all permanent tags to the result set.
		result.add(permanentMetaTags);

		// Iterate over all other tags and try to find one which matches the
		// given key.
		// The problem is the Framework does not give us the current path
		// without parameter. So we have to iterate over and try to match.
		Iterator<Entry<String, MetaTagSet>> it = metaTagsCache.entrySet().iterator();
		MetaTagSet foundTags = null;
		while (it.hasNext()) {
			Map.Entry<String, MetaTagSet> pairs = it.next();
			if (page.contains(pairs.getKey())) {
				foundTags = pairs.getValue();
				break;
			}
		}

		// Get the current language and add a locale tag for it.
		String facebookLanguageCode = OpengraphLanguage.getFacebookRequestCode();
		if (facebookLanguageCode != null && !facebookLanguageCode.isEmpty()) {
			// Add this code to the tag list.
			result.add(new MetaTag("og:locale", OpengraphLanguage.getFacebookRequestCode()));
		} else {
			if (localeTag != null) {
				// otherwise add the default tag to the list.
				result.add(localeTag);
				//hits.add("og:locale");
			}
		}

		if (foundTags != null) {
			// Add all of the found matching tags.
			result.add(foundTags);
		}

		return result;
	}

	/**
	 * Clears ALL the tags which are saved. Permanent tags aswell the page
	 * specific tags.
	 */
	public static void clearAllTags() {
		permanentMetaTags = new MetaTagSet();
		metaTagsCache.clear();
	}
}

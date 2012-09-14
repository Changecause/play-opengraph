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
 * 
 * @author Thomas Felix <thomas.felix@tfelix.de>
 * 
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
	 * List of tags which are required by the Open Graph protocol.
	 */
	@SuppressWarnings("unused")
	private static final Set<String> REQUIERED_TAGS;
	static {
		HashSet<String> reqSet = new HashSet<String>();
		reqSet.add("og:title");
		reqSet.add("og:type");
		reqSet.add("og:image");
		reqSet.add("og:url");
		reqSet.add("og:site_name");
		reqSet.add("fb:app_id");
		REQUIERED_TAGS = Collections.unmodifiableSet(reqSet);
	}
	
	/**
	 * Checks if the MetaTag is a alternate language indicator, if so
	 * it will be added. And a boolean value returned indicating this.
	 * @param tag
	 * @return
	 */
	private static boolean checkLanguageMeta(MetaTag tag) {
		if(tag.getProperty().equals("og:locale:alternate")) {
			return true;
		}
		return false;
	}

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
		permanentMetaTags.add(tag);
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
		
		HashSet<MetaTag> tmp = new HashSet<MetaTag>();
		
		//tmp.addAll(permanentMetaTags);

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
		
		if(foundTags != null) {
			// No Matching tags found. Add nothing.
			tmp.addAll(foundTags);
		}
		
		tmp.addAll(permanentMetaTags);
		return Collections.unmodifiableSet(tmp);
	}
	
	/**
	 * Checks if all metatags which Facebook requires are set in the given output.
	 * If not issue a log entry as a notice.
	 * 
	 * @TODO Not yet implemented because of speed concerns.
	 * 
	 * @param tags
	 * @return
	 */
	@SuppressWarnings("unused")
	private boolean containsAllNeededKeys(Set<MetaTag> tags) {
		return true;
	}
}

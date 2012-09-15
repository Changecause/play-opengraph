package de.tfelix.play.opengraph;

public class MetaTag {
	
	private String property;
	private MetaTagValue content;
	
	/**
	 * Performs error checking and should be called from the various ctors.
	 * 
	 * @param property
	 * @param content
	 */
	private void initialize(String property, MetaTagValue content) {
		if(property == null || property.isEmpty()) {
			throw new IllegalArgumentException("Property can not be null or empty.");
		}
		if(content == null) {
			throw new IllegalArgumentException("Content can not be null.");
		}
		this.property = property.toLowerCase();
		this.content = content;
	}

	public MetaTag(String property, String content) {
		initialize(property, new MetaTagValue(content));
	}
	
	public MetaTag(String property, MetaTagValue content) {
		initialize(property, content);
	}
	

	/**
	 * Returns the property.
	 * 
	 * @return
	 */
	public String getProperty() {
		return property;
	}

	/**
	 * Two MetaTags are considered equal if they match their property String
	 * regardless of content (content may be translated into different
	 * languages, nether the less the objects should be considered equal).
	 */
	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || !(o instanceof MetaTag)) return false;
		
		MetaTag rhs = (MetaTag) o;
		return property.equals(rhs.property) && content.equals(rhs.content);
	}
	
	/**
	 * Like in equals method, only the property is used to determine hashCode.
	 */
	@Override
	public int hashCode() {
		return property.hashCode() + content.hashCode();
	}

	/**
	 * Creates the string representation of the meta-tag.
	 * 
	 * @return Metatag string as it can included in HTML.
	 */
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("<meta property=\"");
		builder.append(property).append("\" content=\"");
		builder.append(content.toString()).append("\" />");
		return builder.toString();
	}

	/**
	 * Gets the translatable value of the MetaTag.
	 * 
	 * @return Translatable value of the MetaTag.
	 */
	public MetaTagValue getContent() {
		return content;
	}
}

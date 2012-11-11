Changelog
=====================================

## Changelog ##

### 0.1.1 - 11.11.2012 ###
 * Introduced MetaTagSet, a collection of MetTags, this class will now do the verification of new tags and checks for not allowed duplicates of tags.
 * The search for the current page is done via regex (currently a bit inefficent). But routes can now be matched against a regex string. E.g: Openpragh.insertTag("/\\w+", TAGS) will match all page URLS like "/123" or "/1" etc.

#### API Changes ####
 * Opengraph.bindArgs() was now moved to MetaTagSet.bindArgs(). To use it the MetaTagSet for a specific page must be requested and then a bindArgs() can be issued.
 * Opengraph.insertTag(String, MetaTagSet) is now the prefered way to add a set of tags to a website.


### 0.1.0 ###
 * Initial Version.

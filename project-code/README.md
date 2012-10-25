Changelog
=====================================

Version 0.1.1

* Introduced MetaTagSet, a collection of MetTags, this class will now do the verification of new tags and checks for not allowed duplicates of tags.

API Changes:
* Opengraph.bindArgs() was now moved to MetaTagSet.bindArgs(). To use it the MetaTagSet for a specific page must be requested and then a bindArgs() can be issued.

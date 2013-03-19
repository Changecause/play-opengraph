play-opengraph - A opengraph metatag manager
============================================

The play-opengraph module is a Play! 2.x Framework plugin which makes it easier to handle the Facebook opengraph metatags. They are needed by the scraper to pull usefull informations from your website. The plugin should improve translation of these tags by hooking into Play!s native translation system.

Installation
---------------------------------------------

Since there is no reposity for Play modules this module is (like some others) hosted by github.com as a project specific page. To include it in your project edit your Build.scala file:

Dependency declaration is:

```
"de.tfelix"	%% "opengraph-module" % "0.1.1"
```

The complete Build.scala looks like this:

```scala
object ApplicationBuild extends Build {

	val appName = "opengraph-usage"
	val appVersion      = "1.0-SNAPSHOT"

	 val appDependencies = Seq(
      javaCore,
	  "de.tfelix"             %% "opengraph-module"           % "0.1.3-SNAPSHOT"
    )

	val main = play.Project(appName, appVersion, appDependencies).settings(
		// Add your own project settings here
		resolvers += Resolver.url("Opengraph-Module Play Repository", url("http://tfelix.github.com/play-opengraph/releases/"))(Resolver.ivyStylePatterns),
		checksums := Nil 
	)

}
```

**Notice:** I dont know why, but somehow SBT/Ivy keeps telling that the SHA1 checksum is corrupted adding the setting checksums := Nil works around this problem. Any help why this is the case is appreciated.

Usage
---------------------------------------------

First of all you can check out the example app inside the /sample/ directory.

There are two kind of meta tags which can be used:

* Permanent Metatags
* Page specific Metatags

The permanent tags will be included on ALL pages. This can be handy for tags like the app_id which will not change.
The page specific metatags will just be included if a visitor requests this very page. The page will be determined by looking to the route which is currently requested.
It is recomended to configure all tags when the app starts. To do this one can add a Global.java file with a onStart handler.
Inside this onStart method you define permanent tags like so:

```java
Opengraph.insertPermanentTag(new MetaTag("og:site_name", config.getString("facebook.site_name")));
```

Page specific tags must be first added to a `MetaTagSet`. This agregates all tags which belong to a single page. To add them seperatly was removed because of performance concerns. So first create a `MetaTagSet` and add all tags to this object. Include the `MetaTagSet` afterwards to the `Opengraph` object like so:

```java
MetaTagSet mts = new MetaTagSet();
mts.add(new MetaTag("og:title", "facebook.quoteTitle"));
Opengraph.insertTag("/quotes/view", );
```

This tag will be added just when visiting the /quotes/view URL.

**Notice:** Page specific tags will be treated with a higher priority: Which means if a permanent tag and a page specific tag exist at the same time, the page specific tag will be included.

Normaly a tag can not be added twice. However there are exception. For example the og:locale:alternate tag can occure more then once. This is possible. (Actually there are self defined metatags which can also occure more then once but they are currently not supported. Maybe in the future.)

Translation
---------------------------------------------

The MetaTag values can (and should) be feeded with a string which is also found in the conf/messages files. If Facebook requests a special language this will be detected and the tags will be translated according to. Please make sure when you define og:locale:alternate tags that the translations are existing.

**Notice:** Currently this seems to be a bit buggy. But I am looking into this if its a problem on my side or if something is wrong with Facebook.

Bugs & Limitations
---------------------------------------------

### Locale Tags

Currently there seems to be an issue with Facebook which forces all locale values to be in LOWERCASE. E.g: 

```java
Opengraph.insertPermanentTag(new MetaTag("og:locale:alternate", "en_us")); 
```
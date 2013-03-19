import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "graphmodule-usage"
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

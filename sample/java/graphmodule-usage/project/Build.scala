import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "graphmodule-usage"
    val appVersion      = "1.0-SNAPSHOT"

    val appDependencies = Seq(
      "de.tfelix"             %% "opengraph-module"           % "0.1.0"
    )

    val main = PlayProject(appName, appVersion, appDependencies, mainLang = JAVA).settings(
      // Add your own project settings here
      resolvers += Resolver.url("Opengraph-Module Play Repository", url("http://tfelix.github.com/play-opengraph/releases/"))(Resolver.ivyStylePatterns),
      checksums := Nil    
    )

}

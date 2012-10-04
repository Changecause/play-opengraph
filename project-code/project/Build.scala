import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "opengraph-module"
    val appVersion      = "0.1.1-SNAPSHOT"

    val appDependencies = Seq(
      // Add your project dependencies here,
    )

    val main = PlayProject(appName, appVersion, appDependencies, mainLang = JAVA).settings(
    	organization := "de.tfelix",
		javacOptions ++= Seq("-target", "1.6") ++ Seq("-source", "1.6"),
		publishTo := Some(Resolver.file("file",  new File( "C:/test" )) )
    )

}

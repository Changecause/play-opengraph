import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "opengraph-module"
    val appVersion      = "0.1.3"

    val appDependencies = Seq(
    	javaCore
    )

    val main = play.Project(appName, appVersion, appDependencies).settings(
    	organization := "de.tfelix",
		javacOptions ++= Seq("-target", "1.6") ++ Seq("-source", "1.6"),
		publishTo := Some(Resolver.file("file",  new File( "C:/test" )) )
    )

}

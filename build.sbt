name := "Mongo Export"

version := "0.1"

scalaVersion := "2.11.4"

libraryDependencies ++= Seq(
	"org.mongodb" %% "casbah" % "2.8.2",
	"net.liftweb" %% "lift-json" % "2.6+",
	"com.typesafe" % "config" % "1.2.1",
	"com.github.nscala-time" %% "nscala-time" % "2.0.0"
	)
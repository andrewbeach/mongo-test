package mongo.export

import scala.collection.mutable.Builder

import com.mongodb.casbah.Imports._ 
import com.mongodb.casbah.commons.conversions.scala._
import com.mongodb.casbah.commons.{MongoDBObject, MongoDBObjectBuilder}

import com.github.nscala_time.time.Imports._

class QueryBuilder {
	
	val self = this

	val builder = MongoDBObject.newBuilder

	def +(field: (String, Any)): this.type = {
		builder += field
		return this
	}

	def +(start: DateTime, end: DateTime): this.type = {
		RegisterJodaTimeConversionHelpers()
		builder += ("created" -> MongoDBObject("$gte" -> start.toString, "$lt" -> end.toString))
		return this
	}

	def result: DBObject = builder.result

}
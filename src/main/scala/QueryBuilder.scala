package QueryBuilder

import scala.collection.mutable.Builder

import com.mongodb.casbah.Imports._ 
import com.mongodb.casbah.commons.conversions.scala._
import com.mongodb.casbah.commons.{MongoDBObject, MongoDBObjectBuilder}

import com.github.nscala_time.time.Imports._

class QueryBuilder {
	
	val self = this

	val builder = MongoDBObject.newBuilder

	def addField(field: (String, Any)): this.type = {
		builder += field
		return this
	}

	def addDateRange(start: String, end: String): this.type = {
		RegisterJodaTimeConversionHelpers()
		val start_date = DateTime.parse(start)
		val end_date = DateTime.parse(end)
		builder += "created" -> MongoDBObject("$gte" -> start_date, "$lt" -> end_date)
		return this
	}

	def result: DBObject = builder.result

}
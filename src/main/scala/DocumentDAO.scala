package mongo.export

import com.mongodb.casbah.Imports._ 
import com.mongodb.casbah.commons.conversions.scala._

case class DAOConfig (uri: String, 
					db: String, 
					coll: String, 
					proj: DBObject = {
						val builder = new QueryBuilder
						val projection = Receipt.default_projection.foreach(f =>
							builder + f
						)
						builder.result
					})

abstract class DocumentDAO(conf: DAOConfig) {

	def mongoClient = MongoClient(MongoClientURI(conf.uri))
	def coll: MongoCollection = mongoClient(conf.db)(conf.coll) 
	def projection = conf.proj

	def find(selection: DBObject): List[String] = {
		coll.find(selection, projection).map(doc => doc.toString) toList
	}

}

class ReceiptDAO(conf: DAOConfig) extends DocumentDAO(conf) {

	def findReceiptsById(id: Int): List[Receipt] = {
		val builder = new QueryBuilder
		val selection: DBObject = builder + ("account_id" -> id) + ("type" -> "receipt") result
		val results: List[String] = find(selection)

		Receipt.parse(results)
	}	

	def count(selection: DBObject): Int = {
		coll.count(selection)
	}

	def countByAccountId(ids: List[Int]): List[Account] = {
		ids map {id => 
			val builder = new QueryBuilder
			val selection: DBObject = { builder + ("account_id" -> id) result }
			new Account(id, count(selection))
		}

	}

}

	
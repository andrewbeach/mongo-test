package DocumentDAO

import com.mongodb.casbah.Imports._ 
import com.mongodb.casbah.commons.conversions.scala._

import Document._
import Account._
import QueryBuilder._

case class DAOConfig (uri: String, 
					db: String, 
					coll: String, 
					proj: DBObject = {
						val builder = new QueryBuilder
						val projection = Receipt.default_projection.foreach(f =>
							builder.addField(f)
						)
						builder.result
					}
					)

class DocumentDAO(conf: DAOConfig) {

	val mongoClient = MongoClient(MongoClientURI(conf.uri))
	val coll: MongoCollection = mongoClient(conf.db)(conf.coll) 
	val projection = conf.proj

	def find(selection: DBObject): Iterator[String] = {
		coll.find(selection, projection).map(doc => doc.toString)
	}

	def findReceipts(id: Int): List[Receipt] = {
		val builder = new QueryBuilder
		val selection: DBObject = builder.addField("account_id" -> id).addField("type" -> "receipt").result
		val results: Iterator[String] = find(selection)

		Receipt.parseList(results)
	}	

	def count(selection: DBObject): Int = {
		coll.count(selection)
	}

	def countByAccountId(ids: Iterator[Int]): Iterator[(Int, Int)] = {
		ids map {id =>
			val builder = new QueryBuilder
			val selection = builder.addField("account_id" -> id).result
			val num = count(selection)
			(id, num)
		}
	}

	def countByAccountId2(ids: Iterator[Int]): List[Account] = {
		ids map {id =>
			val builder = new QueryBuilder
			val selection = builder.addField("account_id" -> id).result
			new Account(id, count(selection))
		} toList

	}

}

	
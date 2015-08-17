package DocumentDAO

import com.mongodb.casbah.Imports._ 

import Document._

case class DAOConfig (uri: String, 
					db: String, 
					coll: String, 
					proj: Seq[(String, Int)] = Receipts.default_projection
					)

class DocumentDAO(conf: DAOConfig) {

	val mongoClient = MongoClient(MongoClientURI(conf.uri))
	val coll: MongoCollection = mongoClient(conf.db)(conf.coll) 
	val projection = conf.proj

	def mdbObject(seq: Seq[(String,Any)]): MongoDBObject = {
		val builder = MongoDBObject.newBuilder
		seq.foreach(f => builder += f)
		builder.result
	}

	def find(selection: Seq[(String,Any)]): Iterator[String] = {
		coll.find(mdbObject(selection), mdbObject(projection)).map(doc => doc.toString)
	}

	def findReceiptsByAccountId(id: Int): List[Receipt] = {
		val selection = Seq("account_id" -> id, 
							"type" -> "receipt")
		val receipts: Iterator[String] = find(selection)
		Receipts.parseList(receipts)
	}	

	def count(selection: Seq[(String,Any)]): Int = {
		coll.count(mdbObject(selection))
	}

	def countByAccountId(ids: Iterator[Int]): Iterator[(Int, Int)] = {
		ids map {id =>
			val selection = Seq("account_id" -> id)
			val num = count(selection)
			(id, num)
		}
	}

}

	
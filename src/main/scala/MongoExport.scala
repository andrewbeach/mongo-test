package mongo.export

import com.mongodb.casbah.Imports._
import com.typesafe.config._
import net.liftweb.json._

import Document._

object Main extends App {

	// Connect to documents collection in Shoeboxed MongoDB
	val conf = ConfigFactory.load()
	val mongoClient = MongoClient(MongoClientURI(conf.getString("main.mongo.uri")))
	val coll: MongoCollection = mongoClient("shoeboxed_documents")("documents")

	// Set up query criteria (selection: query parameters, projection: fields to return)
	val selection = MongoDBObject("account_id" -> 680886300) ++
					MongoDBObject("type" -> "receipt")
	val projection = MongoDBObject("account_id" -> 1, "source" -> 1, "fields.categories" -> 1, "fields.seller" -> 1, "fields.payment_type" -> 1, "fields.total" -> 1, "fields.note" -> 1, "fields.currency" -> 1, "_id" -> 0)
	
	// Run query
	val docs: MongoCursor = coll.find(selection, projection)

	// Parse query response as JSON
	implicit val formats = DefaultFormats // No customization of parser
	val parsed: Iterator[JValue] = docs.map(doc => parse(doc.toString))
	val receipt_list: List[JValue] = parsed.toList

	// Map parsed results to Receipt data type
	val receipts: List[Receipt] = receipt_list.map(doc => doc.extract[Receipt])

	// EXPERIMENTS:
	// Count by vendor
	// val vendorCount: List[(String, Int)] = Receipts.countByVendor(receipts).toList sortWith {_._2 > _._2}
	// vendorCount.foreach({ case (vendor, count) => println(vendor + " -> " + count)})

	// Source string
	// receipts.foreach(r => println(r.getSourceString))

}
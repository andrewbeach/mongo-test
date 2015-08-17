package mongo.export

import com.typesafe.config._

import Document._
import DocumentDAO._
import IO._

object Main extends App {

	// Connect to documents collection in Shoeboxed MongoDB
	val conf = ConfigFactory.load()
	val daoConfig = new DAOConfig(conf.getString("main.mongo.uri"),
								"shoeboxed_documents",
								"documents")
	val documents = new DocumentDAO(daoConfig)

	val account_ids = IO.Import.fromCSV("data/input.csv")

	documents.countById(account_ids).foreach(println)
	val receipts = documents.findReceiptsById(680886300)
	Receipts.printList(receipts)

}
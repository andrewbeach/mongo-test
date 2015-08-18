package mongo.export

import com.typesafe.config._

import Document._
import Account._
import DocumentDAO._
import Data._

object Main extends App {

	// Connect to documents collection in Shoeboxed MongoDB
	val conf = ConfigFactory.load()
	val daoConfig = new DAOConfig(conf.getString("main.mongo.uri"),
								"shoeboxed_documents",
								"documents")
	val documents = new DocumentDAO(daoConfig)

	val account_ids = Import.fromCSV("data/input.csv")

	// documents.countByAccountId(account_ids).foreach(println)

	// val receipts = documents.findReceiptsByAccountId(680886300)
	
	// Receipt.groupByVendor(receipts).foreach { case (v, lr) =>
	// 	println(v + ": ")
	// 	Receipt.printList(lr)
	// }

	val count_list: List[Account] = documents.countByAccountId2(account_ids)
	count_list.foreach(println)

}
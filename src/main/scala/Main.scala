package mongo.export

import com.typesafe.config._

object Main extends App {

	// Connect to documents collection in Shoeboxed MongoDB
	val conf = ConfigFactory.load()
	val daoConfig = new DAOConfig(conf.getString("main.mongo.uri"),
								"shoeboxed_documents",
								"documents")
	val documents = new ReceiptDAO(daoConfig)

	val ids: List[Int] = Load.fromCSV("data/input.csv").toList

	// documents.countByAccountId(account_ids).foreach(println)

	// val receipts = documents.findReceiptsByAccountId(680886300)
	
	// Receipt.groupByVendor(receipts).foreach { case (v, lr) =>
	// 	println(v + ": ")
	// 	Receipt.printList(lr)
	// }

	val counts: List[Account] = documents.countByAccountId(ids)
	counts.foreach(println)

}
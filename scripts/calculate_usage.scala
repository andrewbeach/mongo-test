import mongo.export._
import com.typesafe.config._
import com.github.nscala_time.time.Imports._

object Usage {
	
	def connect: ReceiptDAO = {
		val conf = ConfigFactory.load()
		val daoConfig = new DAOConfig(conf.getString("main.mongo.uri"),
									"shoeboxed_documents",
									"documents")
		new ReceiptDAO(daoConfig)
	}

	def count(id: Int, start: String, end: String): Int = {

		val documents: ReceiptDAO = connect
		val query = new QueryBuilder
		val selection = query + ("account_id" -> id) + (DateTime.parse(start), DateTime.parse(end)) result

		return documents.count(selection)
	}

	def count(id: Int, start_date: String, months: Int): List[(String,Int)] = {

		val documents: ReceiptDAO = connect
		val query = new QueryBuilder
		(1 to months).toList.map(m => {
			val start = DateTime.parse(start_date) + (m-1).months
			val end = start + 1.months
			val selection = query + ("account_id" -> id) + (start, end) result
			val count = documents.count(selection)
			(start.toString.slice(0,10) + " to " + end.toString.slice(0,10), count)
		}) 
		
	}

	def average(id: Int, months: Int): Int = {
		val start = DateTime.now - months.months
		val end = DateTime.now
		val num = count(id, start.toString, end.toString)
		num / months
	}

	def average(id: Int, start: String, months: Int): Int = {
		val end = DateTime.parse(start) + months.months
		val num = count(id, start, end.toString)
		num / months
	}

	// val account_ids = Load.fromCSV("data/input.csv")

}
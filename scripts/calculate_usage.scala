import DocumentDAO._
import QueryBuilder._ 

import com.typesafe.config._
import com.github.nscala_time.time.Imports._

object Usage {
	
	def connect: DocumentDAO = {
		val conf = ConfigFactory.load()
		val daoConfig = new DAOConfig(conf.getString("main.mongo.uri"),
									"shoeboxed_documents",
									"documents")
		new DocumentDAO(daoConfig)
	}

	def calculate(id: Int, start: String, end: String): Int = {

		val documents: DocumentDAO = connect
		val query = new QueryBuilder
		val selection = query.addField("account_id" -> id).addDateRange(start, end).result

		documents.count(selection)
	}

	def calculate(id: Int, start_date: String, months: Int): List[(String,Int)] = {

		val documents: DocumentDAO = connect
		val query = new QueryBuilder
		(1 to months).toList.map(m => {
			val start = DateTime.parse(start_date) + (m-1).months
			val end = start + 1.months
			val selection: DBObject = query.addField("account_id" -> id)
								 .addDateRange(start.toString, end.toString)
								 .result
			val count = documents.count(selection)
			(start.toString.slice(0,10) + " to " + end.toString.slice(0,10), count)
		}) 
		
	}

	// val account_ids = Load.fromCSV("data/input.csv")

}
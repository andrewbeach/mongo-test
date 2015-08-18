import Account._
import Document._
import DocumentDAO._
import Data._

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

	def calculate(id: Int, start: String, end: String, documents: DocumentDAO): Int = {
		val start_date = new DateTime(start)
		val end_date = new DateTime(end)
		val selection = Seq("account_id" -> id)
		documents.count(selection)
	}

	val account_ids = Import.fromCSV("data/input.csv")

}
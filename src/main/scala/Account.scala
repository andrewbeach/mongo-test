package Account

import net.liftweb.json.{parse => parseJSON, _ => _} 

case class Account(id: Int, doc_count: Int)

object Account {

	def parse(d: String): Account = {
		implicit val formats = DefaultFormats // No customization of JSON parser
		parseJSON(d).extract[Account]
	}

	// def sort(l: List[Account]): List[Account = {

		
	// }

}
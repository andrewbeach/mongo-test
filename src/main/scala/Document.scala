package Document

import net.liftweb.json.{parse => parseJSON, _ => _} 

abstract class Document {

}

/* RECEIPT REPRESENTATION */
case class Receipt(account_id: Int, 
			fields: Fields, 
			source: Source) extends Document {

	// Playing around with getter methods, practicing with options and defauls
	def getSeller: String = fields.seller getOrElse "No vendor"
	def getLast4: String = fields.payment_type.last4 getOrElse "No last 4"
	def getCategories: List[String] = fields.categories getOrElse List()
	def getCategoryString: String = (fields.categories getOrElse List()) mkString ", "
	def getTotal: Int = fields.total.on_document getOrElse 0
	def getSourceString: String = source.name match {
		case Some(s) if s == "mail in" => s + ": " + (this.source.envelope getOrElse "")
		case Some(s) if s == "integration" => s + ": " + (this.source.api_app_name getOrElse "")
		case Some(s) if s == "email" => s + ": " + (this.source.email_address getOrElse "")
		case Some(s) => s + ""
		case _ => ""
	}
}

// Receipt fields
case class Fields(categories: Option[List[String]], 
			payment_type: PaymentType, 
			seller: Option[String], 
			total: Total, 
			note: Option[String], 
			currency: Option[String])

// Receipt source fields
case class Source(name: Option[String],
			envelope: Option[String],
			api_app_name: Option[String],
			email_address: Option[String])

// Receipt payment type fields
case class PaymentType(`type`: Option[String], 
			last4: Option[String])

// Receipt total fields
case class Total(on_document: Option[Int], 
			in_account_currency: Option[Int])

/* RECEIPT COMPANION OBJECT */
object Receipt {

	def print(r: Receipt) = {
		val print_string = r.getSeller + " for $" + r.getTotal + " | " + r.getCategoryString
		println(print_string)
	}

	def printList(ls: List[Receipt]) = {
		ls.foreach(r => print(r))
	}

	def parse(d: String): Receipt = {
		implicit val formats = DefaultFormats // No customization of JSON parser
		parseJSON(d).extract[Receipt]
	}

	def parseList[T <: Iterator[String]](ls: T): List[Receipt] = {

		ls.toList.map(li => parse(li))
	}

	val default_projection = Seq("account_id" -> 1, 
								"source" -> 1, 
								"fields.categories" -> 1, 
								"fields.seller" -> 1, 
								"fields.payment_type" -> 1, 
								"fields.total" -> 1, 
								"fields.note" -> 1, 
								"fields.currency" -> 1, 
								"_id" -> 0)

	def total(ls: List[Receipt]): Int = {	
		ls.foldLeft[Int](0)((acc, r) => acc + (r.fields.total.on_document getOrElse 0))
	}

	def groupByVendor(ls: List[Receipt]): Map[String, List[Receipt]] = {
		ls.groupBy[String](r => r.fields.seller getOrElse "")
	}

	def count(ls: List[Receipt]): Int = ls.length

	def countByVendor(ls: List[Receipt]): Map[String, Int] = {
		groupByVendor(ls).map(p => (p._1, p._2.length))
	}

	// Receipts.countByVendor(receipts).toList sortWith {_._2 > _._2}
	// vendorCount.foreach({ case (vendor, count) => println(vendor + " -> " + count)})
}
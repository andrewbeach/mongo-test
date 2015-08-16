package Document

trait Document {

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
object Receipts {
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
}
package IO

object Import {

	def fromCSV(f: String): Iterator[Int] = {

		val src = scala.io.Source.fromFile(f).getLines
		src.map(l => l.toInt)

	}
      	
}

// object Export {

// }

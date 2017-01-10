package zamblauskas.csv.parser.util

import au.com.bytecode.opencsv.CSVReader
import ImplicitExtensions._

import scala.annotation.tailrec

object CsvReaderUtil {

  implicit final class CSVReaderExtensions(val reader: CSVReader) extends AnyVal {
    @tailrec
    def next: Option[Array[String]] = {
      Option(reader.readNext) match {
        case Some(line) if isEmpty(line) => next
        case s @ Some(_) => s
        case None => None
      }
    }

    private def isEmpty(line: Array[String]): Boolean =
      line.isEmpty || line.length === 1 && line(0).trim === ""
  }
}

package zamblauskas.csv.parser.util

import com.opencsv.CSVReader

import scala.annotation.tailrec

object CsvReaderUtil:

  extension (reader: CSVReader)
    @tailrec
    def next: Option[Array[String]] =
      Option(reader.readNext) match
        case Some(line) if isEmpty(line) => next
        case s @ Some(_) => s
        case None => None

    private def isEmpty(line: Array[String]): Boolean =
      line.isEmpty || (line.length == 1 && line(0).trim == "")

package zamblauskas.csv.parser.util

import au.com.bytecode.opencsv.CSVReader

object CsvReaderUtil {

  @SuppressWarnings(Array("org.brianmckenna.wartremover.warts.Null"))
  implicit class CSVReaderExtensions(reader: CSVReader) {
    def next: Option[Array[String]] = {
      val line = reader.readNext
      if(line == null) None
      else if(isEmpty(line)) next
      else Some(line)
    }

    private def isEmpty(line: Array[String]): Boolean =
      line.isEmpty || line.length == 1 && line(0).trim == ""
  }

}

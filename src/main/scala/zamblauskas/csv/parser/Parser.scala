package zamblauskas.csv.parser

import com.opencsv.{CSVParser, CSVParserBuilder, CSVReader, CSVReaderBuilder, ICSVParser}
import com.opencsv.validators.{LineValidatorAggregator, RowValidatorAggregator}
import zamblauskas.csv.parser.ReadResult._
import zamblauskas.csv.parser.util.CsvReaderUtil._

import java.io.{Reader, StringReader}
import java.util.Locale
import scala.annotation.tailrec

object Parser:

  val DefaultSeparator = ','

  final case class Failure(lineNum: Int, line: String, message: String)

  /**
   * Check if header is valid using default separator.
   */
  def isHeaderValid[T](str: String)(implicit cr: ColumnReads[T]): Boolean = isHeaderValidWithSeparator(str, DefaultSeparator)

  def isHeaderValidWithSeparator[T](str: String, separator: Char)(implicit cr: ColumnReads[T]): Boolean =
    val (_, header) = readerAndHeader(str, separator)
    cr.isHeaderValid(header.toIndexedSeq)

  /**
    * Parse using default separator.
    */
  def parse[T](str: String)(implicit cr: ColumnReads[T]): Either[Failure, Seq[T]] = parseWithSeparator(str, DefaultSeparator)

  def parseWithSeparator[T](str: String, separator: Char)(implicit cr: ColumnReads[T]): Either[Failure, Seq[T]] =
    val (reader, header) = readerAndHeader(str, separator)

    def parseLine(line: Array[String]): ReadResult[T] =
      val columns = header.zip(line).map { case (c, h) => Column(c, h) }
      cr.read(columns.toIndexedSeq)

    @tailrec
    def read(lineNum: Int, acc: Seq[T]): Either[Failure, Seq[T]] =
      reader.next match {
        case Some(line) => parseLine(line) match {
          case ReadSuccess(value) => read(lineNum + 1, acc :+ value)
          case ReadFailure(msg)   => Left[Failure, Seq[T]](Failure(lineNum, line.mkString(","), msg))
        }
        case None => Right[Failure, Seq[T]](acc)
      }

    read(0, Seq.empty[T])


  private def readerAndHeader(str: String, separator: Char): (CSVReader, Array[String]) =
    val reader = new CSVReaderBuilder(new StringReader(str))
      .withCSVParser(
        new CSVParserBuilder()
          .withSeparator(separator)
          .build()
      )
      .build()
    val header = reader.next.getOrElse(Array.empty[String])
    (reader, header)

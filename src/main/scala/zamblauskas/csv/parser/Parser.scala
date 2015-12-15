package zamblauskas.csv.parser

import java.io.StringReader

import au.com.bytecode.opencsv.CSVReader

import scala.annotation.tailrec

object Parser {

  final case class Failure(lineNum: Int, line: String, message: String)

  def parse[T](str: String)(implicit cr: ColumnReads[T]): Either[Failure, Seq[T]] = parse(str, separator = ',')

  def parse[T](str: String, separator: Char)(implicit cr: ColumnReads[T]): Either[Failure, Seq[T]] = {
    val csv = new CSVReader(new StringReader(str), separator)
    val header = csv.next.getOrElse(Array.empty)

    def parseLine(line: Array[String]): ReadResult[T] = {
      val columns = header.zip(line).map { case (c, h) => Column(c, h) }
      cr.read(columns)
    }

    @tailrec
    def read(lineNum: Int, acc: Seq[T]): Either[Failure, Seq[T]] = {
      csv.next match {
        case Some(line) => parseLine(line) match {
          case ReadSuccess(value) => read(lineNum + 1, acc :+ value)
          case ReadFailure(msg)   => Left[Failure, Seq[T]](Failure(lineNum, line.mkString(","), msg))
        }
        case None => Right[Failure, Seq[T]](acc)
      }
    }

    read(0, Seq.empty[T])
  }
}

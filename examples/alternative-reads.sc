import zamblauskas.csv.parser._
import zamblauskas.functional._
import Parser.parse

case class Person(age: Int, city: String)

val englishPersonReads: ColumnReads[Person] = (
  column("age").as[Int] and
  column("city").as[String]
)(Person)

val germanPersonReads: ColumnReads[Person] = (
  column("alter").as[Int] and
  column("stadt").as[String]
)(Person)

implicit val personReads = englishPersonReads or germanPersonReads

val englishCsv =
  """
    |age,city
    |33,London
  """.stripMargin

val germanCsv =
  """
    |alter,stadt
    |33,London
  """.stripMargin

parse[Person](englishCsv) == parse[Person](germanCsv)

import zamblauskas.csv.parser._
import zamblauskas.functional._
import Parser.parse

case class Person(age: Int, city: String)

implicit val personReads: ColumnReads[Person] = (
  (column("age").as[Int] or column("alter").as[Int]) and
  (column("city").as[String] or column("stadt").as[String])
)(Person)

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

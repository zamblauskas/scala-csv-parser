import zamblauskas.csv.parser._

case class Person(name: String, age: Int, city: Option[String])

val csv = """
            |name,age,height,city
            |Emily,33,169,London
            |Thomas,25,,
          """.stripMargin

Parser.parse[Person](csv)

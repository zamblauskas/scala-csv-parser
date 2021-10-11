![Github Action](https://github.com/zamblauskas/scala-csv-parser/actions/workflows/release.yml/badge.svg)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.github.zamblauskas/scala-csv-parser_2.13/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.github.zamblauskas/scala-csv-parser_2.13)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/359702f057414b42b9df9728c2c18094)](https://www.codacy.com/gh/zamblauskas/scala-csv-parser/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=zamblauskas/scala-csv-parser&amp;utm_campaign=Badge_Grade)
[![Codacy Badge](https://app.codacy.com/project/badge/Coverage/359702f057414b42b9df9728c2c18094)](https://www.codacy.com/gh/zamblauskas/scala-csv-parser/dashboard?utm_source=github.com&utm_medium=referral&utm_content=zamblauskas/scala-csv-parser&utm_campaign=Badge_Coverage)


About
==============================
CSV parser library for Scala.
Easiest way to convert CSV string representation into a case class.

Usage
==============================

``` scala
import zamblauskas.csv.parser._

case class Person(name: String, age: Int, city: Option[String])

val csv = """
            |name,age,height,city
            |Emily,33,169,London
            |Thomas,25,,
          """.stripMargin

val result = Parser.parse[Person](csv)

result shouldBe Right(List(Person("Emily",33,Some("London")), Person("Thomas",25,None)))
```

ColumnReads[T]
==============================

Example above used a macro generated `ColumnReads[Person]`.
You can define one manually if the generated one does not fit your use case
(e.g. column names differ from case class parameter names).

This is identical to what the macro generates for a `Person` case class:
``` scala
import zamblauskas.csv.parser._
import zamblauskas.functional._

case class Person(name: String, age: Int, city: Option[String])

implicit val personReads: ColumnReads[Person] = (
  column("name").as[String]    and
  column("age").as[Int]        and
  column("city").asOpt[String]
)(Person.apply)

val csv = """
            |name,age,height,city
            |Emily,33,169,London
            |Thomas,25,,
          """.stripMargin

val result = Parser.parse[Person](csv)

result shouldBe Right(List(Person("Emily",33,Some("London")), Person("Thomas",25,None)))
```

Alternative column names
==============================

If columns have two or more alternative names (e.g. in different languages),
you can use an `or` combinator.

``` scala
import zamblauskas.csv.parser._
import zamblauskas.functional._
import Parser.parse

case class Person(age: Int, city: String)

implicit val personReads: ColumnReads[Person] = (
  (column("age").as[Int] or column("alter").as[Int]) and
  (column("city").as[String] or column("stadt").as[String])
)(Person.apply)

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

parse[Person](englishCsv) shouldBe parse[Person](germanCsv)
```

Alternative reads
==============================

Example above can be rewritten to use alternative `ColumnReads` instead of alternative column names.

``` scala
import zamblauskas.csv.parser._
import zamblauskas.functional._
import Parser.parse

case class Person(age: Int, city: String)

val englishPersonReads: ColumnReads[Person] = (
  column("age").as[Int] and
  column("city").as[String]
)(Person.apply)

val germanPersonReads: ColumnReads[Person] = (
  column("alter").as[Int] and
  column("stadt").as[String]
)(Person.apply)

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

parse[Person](englishCsv) shouldBe parse[Person](germanCsv)
```

SBT dependency
==============================

Package is available on [Maven Central](https://mvnrepository.com/artifact/io.github.zamblauskas/scala-csv-parser).
Check the link for the latest version and add to your `build.sbt`:

```
libraryDependencies += "io.github.zamblauskas" %% "scala-csv-parser" % "<latest_version>"
```

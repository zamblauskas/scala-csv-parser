[![Build Status](https://travis-ci.org/zamblauskas/scala-csv-parser.svg?branch=master)](https://travis-ci.org/zamblauskas/scala-csv-parser)
[![Bintray Download](https://api.bintray.com/packages/zamblauskas/maven/scala-csv-parser/images/download.svg) ](https://bintray.com/zamblauskas/maven/scala-csv-parser/_latestVersion)
[![Codacy Grade Badge](https://api.codacy.com/project/badge/Grade/7a2742cce08742939453f5cf86b1f1a9)](https://www.codacy.com/app/zamblauskas/scala-csv-parser/dashboard)
[![Codacy Coverage Badge](https://api.codacy.com/project/badge/Coverage/7a2742cce08742939453f5cf86b1f1a9)](https://www.codacy.com/app/zamblauskas/scala-csv-parser/dashboard)

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
)(Person)

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

parse[Person](englishCsv) shouldBe parse[Person](germanCsv)
```

Alternative reads
==============================

Example above can be rewritten to use alternative `ColumnReads` instead of alternative column names on single `ColumnReads`.

``` scala
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

parse[Person](englishCsv) shouldBe parse[Person](germanCsv)
```

SBT dependency
==============================

Package is available at [Bintray](https://bintray.com/zamblauskas/maven/scala-csv-parser).
Check for the latest version and add to your `build.sbt`:

```
resolvers += Resolver.bintrayRepo("zamblauskas", "maven")

libraryDependencies += "zamblauskas" %% "scala-csv-parser" % "<latest_version>"
```

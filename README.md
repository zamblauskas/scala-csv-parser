[![Build Status](https://travis-ci.org/zamblauskas/scala-csv-parser.svg?branch=master)](https://travis-ci.org/zamblauskas/scala-csv-parser)

About
==============================
CSV parser library for Scala.
Easiest way to convert CSV string representation into a case class.

Usage
==============================

``` scala
  import zamblauskas.csv.parser._

  val csv = """
    |name,age,height,city
    |Emily,33,169,London
    |Thomas,25,,
  """.stripMargin

  case class Person(name: String, age: Int, city: Option[String])

  val result = Parser.parse[Person](csv)

  println(result)
```

This will print:
```
Right(List(Person(Emily,33,Some(London)), Person(Thomas,25,None)))
```

ColumnReads[T]
==============================

Example above used a macro generated `ColumnReads[Person]`.
You can define one manually if the generated one does not fit your use case
(e.g. column names differ from case class parameter names).

This is identical to what macro generates for a `Person` case class:
``` scala
import zamblauskas.functional._

implicit val personReads: ColumnReads[Person] = (
  column("name").as[String]    and
  column("age").as[Int]        and
  column("city").asOpt[String]
)(Person)

```

SBT dependency
==============================

Add to your `build.sbt`:

``` scala
resolvers += Resolver.bintrayRepo("zamblauskas", "maven")

libraryDependencies += "zamblauskas" %% "scala-csv-parser" % "0.7.0"
```

About
==============================
CSV parser library for Scala.
Best suited to convert a string representation into a collection of objects.

Usage
==============================

Input CSV string:
```
val csv = """
|name,age,height,city
|Emily,33,195,London
|Thomas,25,,
""".stripMargin
```
Case class we want to convert to:
```
case class Person(name: String, age: Int, city: Option[String])
```

Step 1: define an implicit `ColumnReads[Person]`:
```
implicit val personReads: ColumnReads[Person] = (
  column("name").as[String]    |@|
  column("age").as[Int]        |@|
  column("city").asOpt[String]
)(Person)
```

Step 2: get the result:
```
val result: Either[Failure, Seq[Person]] = parse(csv)
println(result)
//Right(List(Person(Emily,33,Some(London)), Person(Thomas,25,None)))
```
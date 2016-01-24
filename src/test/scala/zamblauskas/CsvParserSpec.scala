package zamblauskas

import org.scalatest.{FunSpec, Matchers}
import zamblauskas.csv.parser.Parser._
import zamblauskas.csv.parser.ColumnBuilder._

import zamblauskas.functional._

class CsvParserSpec extends FunSpec with Matchers {

  case class Person(name: String, age: Int, city: Option[String])

  implicit val personReads = (
    column("name").as[String] and
    column("age").as[Int] and
    column("city").asOpt[String]
  )(Person)

  describe("success parsing") {
    it("empty string") {
      val csv = ""
      parse(csv) shouldBe Right(Seq.empty[Person])
    }

    it("multiple lines") {
      val csv = """
        |name,age,city
        |john,33,london
        |smith,15,birmingham
      """.stripMargin

      parse(csv) shouldBe Right(Seq(
        Person("john", 33, Some("london")),
        Person("smith", 15, Some("birmingham"))
      ))
    }

    it("inversed columns") {
      val csv = """
        |city,age,name
        |london,33,john
      """.stripMargin

      parse(csv) shouldBe Right(Seq(
        Person("john", 33, Some("london"))
      ))
    }

    it("ignore empty lines") {
      val csv = """
        |name,age,city
        |
        |john,33,london
        |
      """.stripMargin

      parse(csv) shouldBe Right(Seq(
        Person("john", 33, Some("london"))
      ))
    }

    it("ignore unused columns") {
      val csv = """
        |name,x,age,city,y
        |john,x,33,london,y
        """.stripMargin

      parse(csv) shouldBe Right(Seq(
        Person("john", 33, Some("london"))
      ))
    }

    it("optional column does not exist") {
      val csv = """
        |name,age
        |john,33
      """.stripMargin

      parse(csv) shouldBe Right(Seq(
        Person("john", 33, None)
      ))
    }

    it("optional column is empty") {
      val csv = """
        |name,age,city
        |john,33,
      """.stripMargin

      parse(csv) shouldBe Right(Seq(
        Person("john", 33, None)
      ))
    }

    it("';' as a separator") {
      val csv = """
        |name;age;city
        |john;33;london
      """.stripMargin

      parse(csv, ';') shouldBe Right(Seq(
        Person("john", 33, Some("london"))
      ))
    }
  }

  describe("failure parsing") {
    it("required column does not exist") {
      val csv = """
        |name,city
        |john,london
      """.stripMargin

      val result = parse(csv)
      result shouldBe a[Left[_, _]]

      val failure = result.left.get
      failure.lineNum shouldBe 0
      failure.line shouldBe "john,london"
      failure.message should include("age")
    }

    it("cannot convert column to required type") {
      val csv = """
        |name,age,city
        |john,x,london
      """.stripMargin

      val result = parse(csv)
      result shouldBe a[Left[_, _]]

      val failure = result.left.get
      failure.lineNum shouldBe 0
      failure.line shouldBe "john,x,london"
      failure.message should include("age")
    }

    it("more than one required column is missing") {
      val csv = """
        |city
        |london
      """.stripMargin

      val result = parse(csv)
      result shouldBe a[Left[_, _]]

      val failure = result.left.get
      failure.lineNum shouldBe 0
      failure.line shouldBe "london"
      failure.message should (include("name") and include("age"))
    }

    it("failure on second line") {
      val csv = """
        |name,age,city
        |john,15,london
        |smith
      """.stripMargin

      val result = parse(csv)
      result shouldBe a[Left[_, _]]

      val failure = result.left.get
      failure.lineNum shouldBe 1
      failure.line shouldBe "smith"
      failure.message should include("age")
    }
  }
}

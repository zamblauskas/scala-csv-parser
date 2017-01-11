package zamblauskas

import org.scalatest.{FunSpec, Matchers}
import zamblauskas.csv.parser.Parser._
import zamblauskas.csv.parser._
import zamblauskas.functional._


class CsvParserSpec extends FunSpec with Matchers {

  case class Person(name: String, age: Int, city: Option[String])

  describe("manually defined reads") {
    val personReads: ColumnReads[Person] = (
      column("name").as[String] and
      column("age").as[Int] and
      column("city").asOpt[String]
    )(Person)

    testReads(personReads)
  }

  describe("macro generated reads") {
    val personReads: ColumnReads[Person] = implicitly[ColumnReads[Person]]

    testReads(personReads)
  }

  describe("alternative column names") {
    implicit val personReads: ColumnReads[Person] = (
      column("n").as[String] or column("name").as[String] and
      (column("a").as[Int] or column("age").as[Int]) and
      // XXX: support for alternative optional columns could be improved,
      //      if both columns 'c' and 'city' are alternative and optional,
      //      we need to handle `None` for the first one as invalid value so it would
      //      try to pick second.
      (column("c").as[String].map(Some(_)) or column("city").asOpt[String])
    )(Person)

    it("read with short column names") {
      val csv = """
                  |n,a,c
                  |john,21,madrid
                """.stripMargin
      parse(csv) shouldBe Right(Seq(Person("john", 21, Some("madrid"))))
      isHeaderValid(csv) shouldBe true
    }

    it("read with long column names") {
      val csv = """
                  |name,age,city
                  |john,21,madrid
                """.stripMargin
      parse(csv) shouldBe Right(Seq(Person("john", 21, Some("madrid"))))
      isHeaderValid(csv) shouldBe true
    }

    it("none of the columns exist") {
      val csv = """
                  |no-name,no-age,no-city
                  |john,21,madrid
                """.stripMargin

      val result = parse(csv)

      val failure = expectFailure(result)
      failure.lineNum shouldBe 0
      failure.line shouldBe "john,21,madrid"
      failure.message shouldBe
        "Column 'a' does not exist., " +
        "Column 'age' does not exist., " +
        "Column 'n' does not exist., " +
        "Column 'name' does not exist."
        // XXX: 'city' field is optional, therefore should not be in the error message.

      isHeaderValid(csv) shouldBe false
    }
  }

  describe("alternative reads") {
    val defaultReads: ColumnReads[Person] = implicitly[ColumnReads[Person]]
    val shortNameReads: ColumnReads[Person] = (
      column("n").as[String] and
      column("a").as[Int] and
      column("c").asOpt[String]
    )(Person)

    implicit val personReads = defaultReads or shortNameReads

    it("read with short column names") {
      val csv = """
                  |n,a,c
                  |john,21,madrid
                """.stripMargin

      parse(csv) shouldBe Right(Seq(Person("john", 21, Some("madrid"))))

      isHeaderValid(csv) shouldBe true
    }

    it("read with default column names") {
      val csv = """
                  |name,age,city
                  |john,21,madrid
                """.stripMargin

      parse(csv) shouldBe Right(Seq(Person("john", 21, Some("madrid"))))

      isHeaderValid(csv) shouldBe true
    }

    it("fail on mixed column names") {
      val csv = """
                  |name,a,city
                  |john,21,madrid
                """.stripMargin

      val result = parse(csv)

      val failure = expectFailure(result)
      failure.lineNum shouldBe 0
      failure.line shouldBe "john,21,madrid"
      failure.message shouldBe
        "Column 'age' does not exist., " +
        "Column 'n' does not exist."

      isHeaderValid(csv) shouldBe false
    }
  }

  describe("maximum size case class") {
    it("parse 22 params case class") {
      case class Maximum(p1: Int, p2: Int, p3: Int, p4: Int, p5: Int, p6: Int, p7: Int, p8: Int, p9: Int, p10: Int,
        p11: Int, p12: Int, p13: Int, p14: Int, p15: Int, p16: Int, p17: Int, p18: Int, p19: Int, p20: Int, p21: Int, p22: Int)

      val csv =
        """
        |p1,p2,p3,p4,p5,p6,p7,p8,p9,p10,p11,p12,p13,p14,p15,p16,p17,p18,p19,p20,p21,p22
        |1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22
      """.stripMargin

      parse[Maximum](csv) shouldBe Right(Seq(
        Maximum(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22)
      ))
    }
  }

  def testReads(implicit cr: ColumnReads[Person]): Unit = {
    describe("success parsing") {
      it("empty string") {
        val csv = ""
        parse(csv) shouldBe Right(Seq.empty[Person])
        isHeaderValid(csv) shouldBe false
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
        isHeaderValid(csv) shouldBe true
      }

      it("inversed columns") {
        val csv = """
                    |city,age,name
                    |london,33,john
                  """.stripMargin

        parse(csv) shouldBe Right(Seq(
          Person("john", 33, Some("london"))
        ))
        isHeaderValid(csv) shouldBe true
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
        isHeaderValid(csv) shouldBe true
      }

      it("ignore unused columns") {
        val csv = """
                    |name,x,age,city,y
                    |john,x,33,london,y
                  """.stripMargin

        parse(csv) shouldBe Right(Seq(
          Person("john", 33, Some("london"))
        ))
        isHeaderValid(csv) shouldBe true
      }

      it("optional column does not exist") {
        val csv = """
                    |name,age
                    |john,33
                  """.stripMargin

        parse(csv) shouldBe Right(Seq(
          Person("john", 33, None)
        ))
        isHeaderValid(csv) shouldBe true
      }

      it("optional column is empty") {
        val csv = """
                    |name,age,city
                    |john,33,
                  """.stripMargin

        parse(csv) shouldBe Right(Seq(
          Person("john", 33, None)
        ))
        isHeaderValid(csv) shouldBe true
      }

      it("';' as a separator") {
        val csv = """
                    |name;age;city
                    |john;33;london
                  """.stripMargin

        parseWithSeparator(csv, ';') shouldBe Right(Seq(
          Person("john", 33, Some("london"))
        ))
        isHeaderValidWithSeparator(csv, ';') shouldBe true
      }
    }

    describe("failure parsing") {
      it("required column does not exist") {
        val csv = """
                    |name,city
                    |john,london
                  """.stripMargin

        val result = parse(csv)

        val failure = expectFailure(result)
        failure.lineNum shouldBe 0
        failure.line shouldBe "john,london"
        failure.message should include("age")

        isHeaderValid(csv) shouldBe false
      }

      it("cannot convert column to required type") {
        val csv = """
                    |name,age,city
                    |john,x,london
                  """.stripMargin

        val result = parse(csv)

        val failure = expectFailure(result)
        failure.lineNum shouldBe 0
        failure.line shouldBe "john,x,london"
        failure.message should include("age")

        isHeaderValid(csv) shouldBe true
      }

      it("more than one required column is missing") {
        val csv = """
                    |city
                    |london
                  """.stripMargin

        val result = parse(csv)

        val failure = expectFailure(result)
        failure.lineNum shouldBe 0
        failure.line shouldBe "london"
        failure.message should (include("name") and include("age"))

        isHeaderValid(csv) shouldBe false
      }

      it("failure on second line") {
        val csv = """
                    |name,age,city
                    |john,15,london
                    |smith
                  """.stripMargin

        val result = parse(csv)

        val failure = expectFailure(result)
        failure.lineNum shouldBe 1
        failure.line shouldBe "smith"
        failure.message should include("age")

        isHeaderValid(csv) shouldBe true
      }
    }
  }

  private def expectFailure[A](result: Either[A, _]): A =
    result.left.getOrElse(fail(s"Expected failure, but got $result"))
}

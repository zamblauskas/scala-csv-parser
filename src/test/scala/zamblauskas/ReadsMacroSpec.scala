package zamblauskas

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers
import zamblauskas.csv.parser.Parser._
import zamblauskas.csv.parser._


class ReadsMacroSpec extends AnyFunSpec with Matchers {
  it("generate reads for single param case class") {
    case class SingleParam(param: String)

    val csv =
      """
        |param
        |value
      """.stripMargin

    parse[SingleParam](csv) shouldBe Right(Seq(SingleParam("value")))
  }

  it("generate reads for multi param case class") {
    case class MultiParam(param1: String, param2: String)

    val csv =
      """
        |param1,param2
        |value1,value2
      """.stripMargin

    parse[MultiParam](csv) shouldBe Right(Seq(MultiParam("value1", "value2")))
  }

  it("does not generate reads for empty param case class") {
    case class EmptyParam()

    """parse[EmptyParam]("")""" shouldNot compile
  }
}

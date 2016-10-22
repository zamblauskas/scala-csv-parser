package zamblauskas

import org.scalatest.{FunSpec, Matchers}

import zamblauskas.csv.parser.Parser._
import zamblauskas.csv.parser._
import zamblauskas.functional._


class ReadsMacroSpec extends FunSpec with Matchers {

  case class SingleParam(param: String)
  case class EmptyParam()
  class NotACaseClass(param: String)

  it("generate reads for single param case class") {
    val csv =
      """
        |param
        |value
      """.stripMargin

    parse[SingleParam](csv) shouldBe Right(Seq(SingleParam("value")))
  }

  it("does not generate reads for empty param case class") {
    """parse[EmptyParam]("")""" shouldNot compile
  }

  it("does not generate reads for non case class") {
    """parse[NotACaseClass]("")""" shouldNot compile
  }

}

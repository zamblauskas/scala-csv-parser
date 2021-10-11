package zamblauskas

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers
import zamblauskas.csv.parser.ReadResult._

class ReadsSpec extends AnyFunSpec with Matchers {

  describe("default Reads") {
    it("read String") {
      read[String]("string") shouldBe ReadSuccess("string")
    }

    it("read Int") {
      read[Int]("1") shouldBe ReadSuccess(1)
      read[Int]("1.0") shouldBe a[ReadFailure]
      read[Int]("not an Int") shouldBe a[ReadFailure]
    }

    it("read Float") {
      read[Float]("1") shouldBe ReadSuccess(1f)
      read[Float]("1.0") shouldBe ReadSuccess(1f)
      read[Float]("not a Float") shouldBe a[ReadFailure]
    }

    it("read Double") {
      read[Float]("1") shouldBe ReadSuccess(1d)
      read[Float]("1.0") shouldBe ReadSuccess(1d)
      read[Float]("not a Double") shouldBe a[ReadFailure]
    }

    it("read Boolean") {
      read[Boolean]("true") shouldBe ReadSuccess(true)
      read[Boolean]("True") shouldBe ReadSuccess(true)
      read[Boolean]("TrUe") shouldBe ReadSuccess(true)
      read[Boolean]("TRUE") shouldBe ReadSuccess(true)
      read[Boolean]("false") shouldBe ReadSuccess(false)
      read[Boolean]("False") shouldBe ReadSuccess(false)
      read[Boolean]("FalSe") shouldBe ReadSuccess(false)
      read[Boolean]("FALSE") shouldBe ReadSuccess(false)
      read[Boolean]("not a Boolean") shouldBe a[ReadFailure]
    }
  }

  def read[T](value: String)(implicit r: Reads[T]): ReadResult[T] = r.read(Column("test", value))
}

package zamblauskas.csv.parser

import zamblauskas.functional._

import scala.reflect.ClassTag
import scala.util.Try

trait Reads[T] {
  def read(column: Column): ReadResult[T]
}

object Reads {

  implicit val stringReads: Reads[String] = new Reads[String] {
    def read(column: Column): ReadResult[String] = ReadSuccess(column.value)
  }

  implicit val intReads: Reads[Int] = tryRead(_.toInt)

  implicit val longReads: Reads[Long] = tryRead(_.toLong)

  implicit val floatReads: Reads[Float] = tryRead(_.toFloat)

  implicit val doubleReads: Reads[Double] = tryRead(_.toDouble)

  implicit val booleanReads: Reads[Boolean] = tryRead(_.toBoolean)

  private[this] def tryRead[T : ClassTag](f: String => T): Reads[T] = new Reads[T] {
    def read(column: Column): ReadResult[T] = Try[ReadResult[T]] {
      ReadSuccess(f(column.value))
    }.getOrElse {
      ReadFailure(cannotConvertMsg(column, implicitly[ClassTag[T]].runtimeClass.getSimpleName))
    }
  }

  private[this] def cannotConvertMsg(column: Column, typeName: String) =
    s"Cannot convert '${column.value}' to '$typeName' for column '${column.name}'"
}

final case class Column(name: String, value: String)

trait ColumnReads[T] {
  def isHeaderValid(names: Seq[String]): Boolean

  def read(line: Seq[Column]): ReadResult[T]
}

object ColumnReads {

  implicit val columnReadsIsApplicative: Applicative[ColumnReads] = new Applicative[ColumnReads] {
    override def unit[A](a: => A): ColumnReads[A] = new ColumnReads[A] {
      override def isHeaderValid(names: Seq[String]): Boolean = true
      override def read(line: Seq[Column]): ReadResult[A] = ReadSuccess(a)
    }
    override def apply[A, B](f: => ColumnReads[(A) => B])(fa: => ColumnReads[A]): ColumnReads[B] = new ColumnReads[B] {
      override def isHeaderValid(names: Seq[String]): Boolean = fa.isHeaderValid(names) && f.isHeaderValid(names)
      override def read(line: Seq[Column]): ReadResult[B] = (fa.read(line), f.read(line)) match {
        case (ReadSuccess(fav), ReadSuccess(fv)) => ReadSuccess(fv(fav))
        case (f1: ReadFailure, f2: ReadFailure) => f1 |+| f2
        case (f: ReadFailure, _) => f
        case (_, f: ReadFailure) => f
      }
    }
  }

  implicit val columnReadsIsAlternative: Alternative[ColumnReads] = new Alternative[ColumnReads] {
    override def or[A, B >: A](alt1: ColumnReads[A], alt2: ColumnReads[B]): ColumnReads[B] = new ColumnReads[B] {
      override def isHeaderValid(names: Seq[String]): Boolean = alt1.isHeaderValid(names) || alt2.isHeaderValid(names)
      override def read(line: Seq[Column]): ReadResult[B] = alt1.read(line) match {
        case s1 @ ReadSuccess(_) => s1
        case f1 @ ReadFailure(_) => alt2.read(line) match {
          case s2 @ ReadSuccess(_) => s2
          case f2 @ ReadFailure(_) => f1 |+| f2
        }
      }
    }
  }
}

package zamblauskas.csv.parser

import scala.reflect.ClassTag
import scala.util.Try
import scalaz.Applicative
import scalaz.syntax.semigroup._


trait Reads[T] {
  def read(column: Column): ReadResult[T]
}

object Reads {

  implicit val stringReads: Reads[String] = new Reads[String] {
    def read(column: Column): ReadResult[String] = ReadSuccess(column.value)
  }

  implicit val intReads: Reads[Int] = tryRead(_.toInt)

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
  def read(line: Seq[Column]): ReadResult[T]
}

object ColumnReads {

  implicit val columnReadsIsApplicative: Applicative[ColumnReads] = new Applicative[ColumnReads] {
    override def point[A](a: => A): ColumnReads[A] = new ColumnReads[A] {
      override def read(line: Seq[Column]): ReadResult[A] = ReadSuccess(a)
    }
    override def ap[A, B](fa: => ColumnReads[A])(f: => ColumnReads[(A) => B]): ColumnReads[B] = new ColumnReads[B] {
      override def read(line: Seq[Column]): ReadResult[B] = (fa.read(line), f.read(line)) match {
        case (ReadSuccess(fav), ReadSuccess(fv)) => ReadSuccess(fv(fav))
        case (f1: ReadFailure, f2: ReadFailure) => f1 |+| f2
        case (f: ReadFailure, _) => f
        case (_, f: ReadFailure) => f
      }
    }
  }
}
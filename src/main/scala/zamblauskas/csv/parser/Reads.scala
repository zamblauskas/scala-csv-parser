package zamblauskas.csv.parser

import zamblauskas.csv.parser.ReadResult._
import zamblauskas.functional._

import scala.reflect.ClassTag
import scala.util.Try
import scala.deriving.Mirror
import scala.compiletime.{erasedValue, summonInline}
import scala.compiletime.constValue

trait Reads[T]:
  def read(column: Column): ReadResult[T]

object Reads:

  given Reads[String] = (column: Column) => ReadSuccess(column.value)

  given Reads[Int] = tryRead(_.toInt)

  given Reads[Long] = tryRead(_.toLong)

  given Reads[Float] = tryRead(_.toFloat)

  given Reads[Double] = tryRead(_.toDouble)

  given Reads[Boolean] = tryRead(_.toBoolean)

  private def tryRead[T](f: String => T)(using ct: ClassTag[T]): Reads[T] = (column: Column) =>
    Try(
      ReadSuccess(f(column.value))
    ).getOrElse(
      ReadFailure(cannotConvertMsg(column, ct.runtimeClass.getSimpleName))
    )


  private def cannotConvertMsg(column: Column, typeName: String) =
    s"Cannot convert '${column.value}' to '$typeName' for column '${column.name}'"


final case class Column(name: String, value: String)


trait ColumnReads[T]:
  def isHeaderValid(names: Seq[String]): Boolean
  def read(line: Seq[Column]): ReadResult[T]

object ColumnReads:

  given Applicative[ColumnReads] with
    def unit[A](a: A): ColumnReads[A] = new ColumnReads[A]:
      def isHeaderValid(names: Seq[String]): Boolean = true
      def read(line: Seq[Column]): ReadResult[A] = ReadSuccess(a)

    extension [A](fa: ColumnReads[A]) def apply[B](f: ColumnReads[A => B]): ColumnReads[B] = new ColumnReads[B]:
      def isHeaderValid(names: Seq[String]): Boolean = fa.isHeaderValid(names) && f.isHeaderValid(names)
      def read(line: Seq[Column]): ReadResult[B] = (fa.read(line), f.read(line)) match
          case (ReadSuccess(fav), ReadSuccess(fv)) => ReadSuccess(fv(fav))
          case (f1: ReadFailure, f2: ReadFailure) => f1 |+| f2
          case (f: ReadFailure, _) => f
          case (_, f: ReadFailure) => f


  given Alternative[ColumnReads] with
    extension [A, B >: A](alt1: ColumnReads[A]) def or(alt2: => ColumnReads[B]): ColumnReads[B] = new ColumnReads[B]:
      def isHeaderValid(names: Seq[String]): Boolean = alt1.isHeaderValid(names) || alt2.isHeaderValid(names)
      def read(line: Seq[Column]): ReadResult[B] = alt1.read(line) match
        case s1 @ ReadSuccess(_) => s1
        case f1 @ ReadFailure(_) => alt2.read(line) match
          case s2 @ ReadSuccess(_) => s2
          case f2 @ ReadFailure(_) => f1 |+| f2

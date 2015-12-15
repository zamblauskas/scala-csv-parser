package zamblauskas.csv.parser

import scalaz.{Functor, Semigroup}

trait ReadResult[+A]
final case class ReadSuccess[A](value: A) extends ReadResult[A]
final case class ReadFailure(msg: String) extends ReadResult[Nothing]

object ReadResult {

  implicit val failureIsSemigroup: Semigroup[ReadFailure] = new Semigroup[ReadFailure] {
    override def append(f1: ReadFailure, f2: => ReadFailure): ReadFailure = {
      ReadFailure(f1.msg + ", " + f2.msg)
    }
  }

  implicit val readResultIsFunctor: Functor[ReadResult] = new Functor[ReadResult] {
    override def map[A, B](fa: ReadResult[A])(f: (A) => B): ReadResult[B] = fa match {
      case ReadSuccess(v) => ReadSuccess(f(v))
      case f: ReadFailure => f
    }
  }
}

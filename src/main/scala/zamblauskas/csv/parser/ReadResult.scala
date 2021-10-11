package zamblauskas.csv.parser

import zamblauskas.functional.{Functor, Semigroup}


enum ReadResult[+A]:
  case ReadSuccess[A](value: A) extends ReadResult[A]
  case ReadFailure(msg: String) extends ReadResult[Nothing]

object ReadResult:

  given Semigroup[ReadFailure] with
    extension (f1: ReadFailure) def append(f2: => ReadFailure) = ReadFailure(f1.msg + ", " + f2.msg)

  given Functor[ReadResult] with
    extension [A](fa: ReadResult[A]) def map[B](f: A => B): ReadResult[B] =
      fa match
        case ReadSuccess(v) => ReadSuccess(f(v))
        case f: ReadFailure => f


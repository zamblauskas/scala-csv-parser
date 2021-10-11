package zamblauskas.functional

trait Alternative[F[_]]:
  extension [A, B >: A](alt1: F[A]) def or(alt2: => F[B]): F[B]

object Alternative:
  def apply[F[_]](using a: Alternative[F]) = a

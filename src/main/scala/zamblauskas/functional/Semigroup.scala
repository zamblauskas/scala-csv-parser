package zamblauskas.functional

trait Semigroup[F]:
  extension (f1: F) def append(f2: => F): F
  extension (f1: F) def |+|(f2: => F): F = append(f1)(f2)

object Semigroup:
  def apply[F](using a: Semigroup[F]) = a

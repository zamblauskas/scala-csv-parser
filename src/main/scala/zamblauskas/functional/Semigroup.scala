package zamblauskas.functional

trait Semigroup[F] {
  def append(f1: F, f2: => F): F
}

class SemigroupOps[F](val self: F)(implicit val F: Semigroup[F]) {
  def |+|(other: => F): F = F.append(self, other)
}

trait ToSemigroupOps {
  implicit def toSemigroupOps[F](v: F)(implicit F0: Semigroup[F]): SemigroupOps[F] = new SemigroupOps[F](v)
}

object ToSemigroupOps extends ToSemigroupOps
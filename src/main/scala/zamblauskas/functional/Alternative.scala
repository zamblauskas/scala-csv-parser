package zamblauskas.functional

trait Alternative[F[_]] {
  def or[A, B >: A](alt1: F[A], alt2: F[B]): F[B]
}

class AlternativeOps[F[_], A](val self: F[A])(implicit F: Alternative[F]) {
  def or[B >: A](other: F[B]): F[B] = F.or(self, other)
}

trait ToAlternativeOps {
  implicit def toAlternativeOps[F[_], A](v: F[A])(implicit F0: Alternative[F]): AlternativeOps[F, A] = new AlternativeOps[F, A](v)
}

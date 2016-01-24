package zamblauskas.functional

trait Functor[F[_]] {

  def map[A, B](fa: => F[A])(f: A => B): F[B]

}

class FunctorOps[F[_], A](val self: F[A])(implicit F: Functor[F]) {

  def map[B](f: A => B): F[B] = F.map(self)(f)

}

trait ToFunctorOps {
  implicit def toFunctorOps[F[_], A](v: F[A])(implicit F0: Functor[F]): FunctorOps[F, A] = new FunctorOps[F, A](v)
}

object ToFunctorOps extends ToFunctorOps

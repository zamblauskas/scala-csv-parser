package zamblauskas.functional

trait Applicative[F[_]] extends Functor[F] {

  def unit[A](a: => A): F[A]
  def apply[A, B](fab: => F[A => B])(fa: => F[A]): F[B]

  // derived methods
  def map[A, B](fa: => F[A])(f: A => B): F[B] = apply(unit(f))(fa)
  def map2[A, B, C](fa: => F[A], fb: => F[B])(f: (A, B) => C): F[C] = apply(map(fa)(f.curried))(fb)
  def map3[A, B, C, D](fa: => F[A], fb: => F[B], fc: => F[C])(f: (A, B, C) => D) =
    apply(map2(fa, fb)((a:A, b:B) => f.curried (a)(b)))(fc)
}

trait ApplicativeBuilder[M[_], A, B] {
  val a: M[A]
  val b: M[B]
  def apply[C](f: (A, B) => C)(implicit ap: Applicative[M]): M[C] = ap.map2(a, b)(f)
  def and[C](cc: M[C]): ApplicativeBuilder3[C] = new ApplicativeBuilder3[C] {
    val c = cc
  }

  trait ApplicativeBuilder3[C] {
    val c: M[C]
    def apply[D](f: (A, B, C) => D)(implicit ap: Applicative[M]): M[D] = ap.map3(a, b, c)(f)
  }
}

class ApplicativeOps[F[_], A](val self: F[A])(implicit val F: Applicative[F]) {

  def and[B](fb: F[B]): ApplicativeBuilder[F, A, B] = new ApplicativeBuilder[F, A, B] {
    val a = self
    val b = fb
  }

}

trait ToApplicativeOps {
  implicit def toApplicativeOps[F[_], A](v: F[A])(implicit F0: Applicative[F]): ApplicativeOps[F, A] = new ApplicativeOps[F, A](v)
}

object ToApplicativeOps extends ToApplicativeOps

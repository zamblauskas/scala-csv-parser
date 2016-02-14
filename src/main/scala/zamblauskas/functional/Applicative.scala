package zamblauskas.functional

trait Applicative[F[_]] extends Functor[F] {

  def unit[A](a: => A): F[A]
  def apply[A, B](fab: => F[A => B])(fa: => F[A]): F[B]

  // derived methods
  def map[A, B](fa: => F[A])(f: A => B): F[B] = apply(unit(f))(fa)
  def map2[A, B, C](fa: => F[A], fb: => F[B])(f: (A, B) => C): F[C] = apply(map(fa)(f.curried))(fb)
  def map3[A, B, C, D](fa: => F[A], fb: => F[B], fc: => F[C])(f: (A, B, C) => D): F[D] =
    apply(map2(fa, fb)((a:A, b:B) => f.curried (a)(b)))(fc)
  def map4[A, B, C, D, E](fa: => F[A], fb: => F[B], fc: => F[C], fd: => F[D])(f: (A, B, C, D) => E): F[E] =
    apply(map3(fa, fb, fc)((a:A, b:B, c:C) => f.curried (a)(b)(c)))(fd)
  def map5[A, B, C, D, E, G](fa: => F[A], fb: => F[B], fc: => F[C], fd: => F[D], fe: => F[E])(f: (A, B, C, D, E) => G): F[G] =
    apply(map4(fa, fb, fc, fd)((a:A, b:B, c:C, d:D) => f.curried (a)(b)(c)(d)))(fe)
  def map6[A, B, C, D, E, G, H](fa: => F[A], fb: => F[B], fc: => F[C], fd: => F[D], fe: => F[E], fg: => F[G])(f: (A, B, C, D, E, G) => H): F[H] =
    apply(map5(fa, fb, fc, fd, fe)((a:A, b:B, c:C, d:D, e: E) => f.curried (a)(b)(c)(d)(e)))(fg)
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
    def and[D](dd: M[D]): ApplicativeBuilder4[D] = new ApplicativeBuilder4[D] {
      val d = dd
    }

    trait ApplicativeBuilder4[D] {
      val d: M[D]
      def apply[E](f: (A, B, C, D) => E)(implicit ap: Applicative[M]): M[E] = ap.map4(a, b, c, d)(f)
      def and[E](ee: M[E]): ApplicativeBuilder5[E] = new ApplicativeBuilder5[E] {
        val e = ee
      }

      trait ApplicativeBuilder5[E] {
        val e: M[E]
        def apply[G](f: (A, B, C, D, E) => G)(implicit ap: Applicative[M]): M[G] = ap.map5(a, b, c, d, e)(f)
        def and[G](gg: M[G]): ApplicativeBuilder6[G] = new ApplicativeBuilder6[G] {
          val g = gg
        }

        trait ApplicativeBuilder6[G] {
          val g: M[G]
          def apply[H](f: (A, B, C, D, E, G) => H)(implicit ap: Applicative[M]): M[H] = ap.map6(a, b, c, d, e, g)(f)
        }
      }
    }
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

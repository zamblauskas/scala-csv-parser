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
  def map7[A, B, C, D, E, G, H, I](fa: => F[A], fb: => F[B], fc: => F[C], fd: => F[D], fe: => F[E], fg: => F[G], fh: => F[H])(f: (A, B, C, D, E, G, H) => I): F[I] =
    apply(map6(fa, fb, fc, fd, fe, fg)((a:A, b:B, c:C, d:D, e: E, g: G) => f.curried (a)(b)(c)(d)(e)(g)))(fh)
  def map8[A, B, C, D, E, G, H, I, J](fa: => F[A], fb: => F[B], fc: => F[C], fd: => F[D], fe: => F[E], fg: => F[G], fh: => F[H], fi: => F[I])(f: (A, B, C, D, E, G, H, I) => J): F[J] =
    apply(map7(fa, fb, fc, fd, fe, fg, fh)((a:A, b:B, c:C, d:D, e: E, g: G, h: H) => f.curried (a)(b)(c)(d)(e)(g)(h)))(fi)
  def map9[A, B, C, D, E, G, H, I, J, K](fa: => F[A], fb: => F[B], fc: => F[C], fd: => F[D], fe: => F[E], fg: => F[G], fh: => F[H], fi: => F[I], fj: => F[J])(f: (A, B, C, D, E, G, H, I, J) => K): F[K] =
    apply(map8(fa, fb, fc, fd, fe, fg, fh, fi)((a:A, b:B, c:C, d:D, e: E, g: G, h: H, i: I) => f.curried (a)(b)(c)(d)(e)(g)(h)(i)))(fj)
  def map10[A, B, C, D, E, G, H, I, J, K, L](fa: => F[A], fb: => F[B], fc: => F[C], fd: => F[D], fe: => F[E], fg: => F[G], fh: => F[H], fi: => F[I], fj: => F[J], fk: => F[K])(f: (A, B, C, D, E, G, H, I, J, K) => L): F[L] =
    apply(map9(fa, fb, fc, fd, fe, fg, fh, fi, fj)((a:A, b:B, c:C, d:D, e: E, g: G, h: H, i: I, j: J) => f.curried (a)(b)(c)(d)(e)(g)(h)(i)(j)))(fk)
  def map11[A, B, C, D, E, G, H, I, J, K, L, M](fa: => F[A], fb: => F[B], fc: => F[C], fd: => F[D], fe: => F[E], fg: => F[G], fh: => F[H], fi: => F[I], fj: => F[J], fk: => F[K], fl: => F[L])(f: (A, B, C, D, E, G, H, I, J, K, L) => M): F[M] =
    apply(map10(fa, fb, fc, fd, fe, fg, fh, fi, fj, fk)((a:A, b:B, c:C, d:D, e: E, g: G, h: H, i: I, j: J, k: K) => f.curried (a)(b)(c)(d)(e)(g)(h)(i)(j)(k)))(fl)
  def map12[A, B, C, D, E, G, H, I, J, K, L, M, N](fa: => F[A], fb: => F[B], fc: => F[C], fd: => F[D], fe: => F[E], fg: => F[G], fh: => F[H], fi: => F[I], fj: => F[J], fk: => F[K], fl: => F[L], fm: => F[M])(f: (A, B, C, D, E, G, H, I, J, K, L, M) => N): F[N] =
    apply(map11(fa, fb, fc, fd, fe, fg, fh, fi, fj, fk, fl)((a:A, b:B, c:C, d:D, e: E, g: G, h: H, i: I, j: J, k: K, l: L) => f.curried (a)(b)(c)(d)(e)(g)(h)(i)(j)(k)(l)))(fm)
  def map13[A, B, C, D, E, G, H, I, J, K, L, M, N, O](fa: => F[A], fb: => F[B], fc: => F[C], fd: => F[D], fe: => F[E], fg: => F[G], fh: => F[H], fi: => F[I], fj: => F[J], fk: => F[K], fl: => F[L], fm: => F[M], fn: => F[N])(f: (A, B, C, D, E, G, H, I, J, K, L, M, N) => O): F[O] =
    apply(map12(fa, fb, fc, fd, fe, fg, fh, fi, fj, fk, fl, fm)((a:A, b:B, c:C, d:D, e: E, g: G, h: H, i: I, j: J, k: K, l: L, m: M) => f.curried (a)(b)(c)(d)(e)(g)(h)(i)(j)(k)(l)(m)))(fn)
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

          def and[H](hh: M[H]): ApplicativeBuilder7[H] = new ApplicativeBuilder7[H] {
            val h = hh
          }

          trait ApplicativeBuilder7[H] {
            val h: M[H]

            def apply[I](f: (A, B, C, D, E, G, H) => I)(implicit ap: Applicative[M]): M[I] = ap.map7(a, b, c, d, e, g, h)(f)

            def and[I](ii: M[I]): ApplicativeBuilder8[I] = new ApplicativeBuilder8[I] {
              val i = ii
            }

            trait ApplicativeBuilder8[I] {
              val i: M[I]

              def apply[J](f: (A, B, C, D, E, G, H, I) => J)(implicit ap: Applicative[M]): M[J] = ap.map8(a, b, c, d, e, g, h, i)(f)

              def and[J](jj: M[J]): ApplicativeBuilder9[J] = new ApplicativeBuilder9[J] {
                val j = jj
              }

              trait ApplicativeBuilder9[J] {
                val j: M[J]

                def apply[K](f: (A, B, C, D, E, G, H, I, J) => K)(implicit ap: Applicative[M]): M[K] = ap.map9(a, b, c, d, e, g, h, i, j)(f)

                def and[K](kk: M[K]): ApplicativeBuilder10[K] = new ApplicativeBuilder10[K] {
                  val k = kk
                }

                trait ApplicativeBuilder10[K] {
                  val k: M[K]

                  def apply[L](f: (A, B, C, D, E, G, H, I, J, K) => L)(implicit ap: Applicative[M]): M[L] = ap.map10(a, b, c, d, e, g, h, i, j, k)(f)

                  def and[L](ll: M[L]): ApplicativeBuilder11[L] = new ApplicativeBuilder11[L] {
                    val l = ll
                  }

                  trait ApplicativeBuilder11[L] {
                    val l: M[L]

                    def apply[N](f: (A, B, C, D, E, G, H, I, J, K, L) => N)(implicit ap: Applicative[M]): M[N] = ap.map11(a, b, c, d, e, g, h, i, j, k, l)(f)

                    def and[N](nn: M[N]): ApplicativeBuilder12[N] = new ApplicativeBuilder12[N] {
                      val n = nn
                    }

                    trait ApplicativeBuilder12[N] {
                      val n: M[N]

                      def apply[O](f: (A, B, C, D, E, G, H, I, J, K, L, N) => O)(implicit ap: Applicative[M]): M[O] = ap.map12(a, b, c, d, e, g, h, i, j, k, l, n)(f)

                      def and[O](oo: M[O]): ApplicativeBuilder13[O] = new ApplicativeBuilder13[O] {
                        val o = oo
                      }

                      trait ApplicativeBuilder13[O] {
                        val o: M[O]

                        def apply[P](f: (A, B, C, D, E, G, H, I, J, K, L, N, O) => P)(implicit ap: Applicative[M]): M[P] = ap.map13(a, b, c, d, e, g, h, i, j, k, l, n, o)(f)
                      }
                    }
                  }
                }
              }
            }
          }
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

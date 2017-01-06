package zamblauskas.functional

class ApplicativeOps[F[_], A](val self: F[A])(implicit val F: Applicative[F]) {
  def and[B](fb: F[B]): ApplicativeBuilder2[F, A, B] = new ApplicativeBuilder2[F, A, B] {
    val p1: F[A] = self
    val p2: F[B] = fb
  }
}

trait ToApplicativeOps {
  implicit def toApplicativeOps[F[_], A](v: F[A])(implicit F0: Applicative[F]): ApplicativeOps[F, A] = new ApplicativeOps[F, A](v)
}

object ToApplicativeOps extends ToApplicativeOps

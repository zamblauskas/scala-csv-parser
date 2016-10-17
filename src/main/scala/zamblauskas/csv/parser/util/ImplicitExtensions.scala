package zamblauskas.csv.parser.util

object ImplicitExtensions {

  /**
    * Ripped from https://github.com/puffnfresh/wartremover#equals
    */
  @SuppressWarnings(Array("org.wartremover.warts.Equals"))
  implicit final class AnyOps[A](self: A) {
    def ===(other: A): Boolean = self == other
  }

}

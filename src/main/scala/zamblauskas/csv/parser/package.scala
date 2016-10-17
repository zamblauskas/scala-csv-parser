package zamblauskas.csv

import scala.language.experimental.macros

package object parser {

  def column(name: String): ColumnBuilder = ColumnBuilder(name)

  implicit def materializeColumnReads[T]: ColumnReads[T] = macro ReadsMacro.materializeColumnReadsImpl[T]

}

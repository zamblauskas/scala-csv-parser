package zamblauskas.csv.parser

import scala.reflect.macros.blackbox

object ReadsMacro {

  def materializeColumnReadsImpl[T: c.WeakTypeTag](c: blackbox.Context): c.Expr[ColumnReads[T]] = {
    import c.universe._

    val typeOfT = weakTypeOf[T]
    val fullNameOfT = typeOfT.typeSymbol.fullName

    def abort(cause: String): Nothing = {
      val name = classOf[ColumnReads[_]].getName
      val msg = cause + "\n" +
        s"Error while trying to generate $name[$fullNameOfT].\n" +
        s"Either fix the error or provide $name[$fullNameOfT]."
      c.abort(c.enclosingPosition, msg)
    }

    val fields = typeOfT.decls.collectFirst {
      case m: MethodSymbol if m.isPrimaryConstructor ⇒ m
    }.flatMap(_.paramLists.headOption).getOrElse(
      abort(s"Couldn't find $fullNameOfT constructor.")
    )

    val fieldsColumnBuilders = fields.map { field =>
      val nameType = field.name
      val returnType = typeOfT.decl(nameType).typeSignature.resultType

      val columnName = nameType.decodedName.toString

      val isOption = returnType.typeConstructor <:< typeOf[Option[_]].typeConstructor

      if(isOption) {
        val innerType = returnType.typeArgs.headOption.getOrElse(
          abort("Option must have a concrete type argument.")
        )
        q"ColumnBuilder($columnName).asOpt[$innerType]"
      } else {
        q"ColumnBuilder($columnName).as[$returnType]"
      }
    }

    val functionalPkg = q"import zamblauskas.functional._"
    val newTypeOfT = c.parse(s"new $typeOfT(${fields.map(_ => "_").mkString(",")})")

    val columnBuilderOfT = fieldsColumnBuilders match {
      case Nil =>
        abort(s"$fullNameOfT constructor must have at least one parameter.")
      case x :: Nil =>
        q"""
          $functionalPkg
          $x.map($newTypeOfT)
          """
      case _ =>
        val applicativeBuilder = fieldsColumnBuilders.reduceLeft { (acc, r) =>
          q"""
            $functionalPkg
            $acc.and($r)
            """
        }
        q"$applicativeBuilder.apply($newTypeOfT)"
    }

    c.Expr[ColumnReads[T]](columnBuilderOfT)
  }
}

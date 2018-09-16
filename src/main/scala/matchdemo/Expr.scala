package matchdemo

/**
  * @program: workjob
  * @description: ${description}
  * @author: 刘文强 kingcall
  * @create: 2018-08-04 16:40
  **/
abstract class Expr
case class Var(anme:String) extends Expr
case class Number(num:Double) extends Expr
case class UnOP(operrator:String,arg:Expr) extends Expr
case class BinOp(operrator:String,left:Expr,right: Expr) extends Expr
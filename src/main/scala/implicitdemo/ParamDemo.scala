package scala.implicitdemo

/**
  * 隐式参数,当没有的时候就在当前域中或者伴生对象中找或者从外部引入的对象中找
  * 隐式参数往往是一个对象，基础数据类型用默认参数就可以了,隐式参数一个所以用对象可以尽可能的多容纳信息（特殊的对象可以避免二意性）
  * 隐式参数最好一个，即使是多个数据类型也必须不一致，否则会出现二意性
  * 隐式implicit在一个函数参数中只能出现一次，并且只能出现在最前面，代表着该参数列表全部是隐式参数
  *
  * @param left
  * @param right
  */
case class Delimiters(left: String, right: String)
case class Delimiters2(left: String, right: Int)
class PreferredPrompt(val preferrence: String)
class PreferredDrink(val preferrence: String)
object ParamDemo {
    implicit val ps = Delimiters("<<<", ">>>")
    implicit val le: String = "《《《《"
    implicit val le2: Int = 10
    def quote(what: String)(implicit delimiters: Delimiters) = println(delimiters.left + what + delimiters.right)

    def quote2(what: String)(implicit left: String, right: Int) = println(left + what + "\t" + right)

    def quote3(what: String)(ps: Array[String] = Array("<<<", ">>>")) = println(ps(0) + what + "\t" + ps(1))
    // 会找一个隐式的字符串参数，但不会根据名字去找
    def person(implicit name : String) = name
    // implict 必须出现在参数列表最前面，同时意味着所有的参数是隐式的
    def quote4(implicit what:String,left: String,right:String)=println(left + what + "\t" + right)
    implicit val prompt=new PreferredPrompt("prmop")
    implicit val drink=new PreferredDrink("drink")
    def greete(name:String)(implicit prompt:PreferredPrompt,drink: PreferredDrink): Unit ={
        println(s"hello ${name},${prompt.preferrence},${drink.preferrence}")
    }
    def main(args: Array[String]): Unit = {
        quote("I'm kingcall")(Delimiters("《", "》"))
        quote("I'm kingcall")
        quote2("I'm kingcall")
        quote3("I'm kingcall")()
        println(person)
        quote4
        greete("kingcall")
    }
}
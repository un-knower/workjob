package matchdemo.extract

object Twice{
    def apply(s:String): String = s+s

    def unapply(arg: String): Option[String] ={
        val length=arg.length/2
        val half=arg.substring(0,length)
        if(half==arg.substring(length)) Some(half) else None
    }
}

object Uppercase{
    def unapply(arg: String): Boolean =arg.toUpperCase==arg
}
object Domain{
    def apply(parts:String*): String = parts.mkString(".")
    // 在mkString() 方法中参数就是一个字符串，而在split 方法中参数是一个正则表达式
    def unapplySeq(arg: String): Option[Seq[String]] = Some(arg.split("\\.").reverse)
}

object Email {
    def main(args: Array[String]): Unit = {
        println(changeLenth("tom@sun.com"))
        changeLength_test
        userTwiceUpper_test
    }

    def test_base(): Unit ={
        val s=apply("kingcall","hotmail.com")
        println(unapply(s))
        println(unapply("kingcall hotmail.com"))
        println(getResult(s))
    }

    def changeLength_test(): Unit ={
        println(Domain.apply("com","baidu","www"))
        println(Domain.unapplySeq(Domain.apply("com","baidu","www")))
        println(Domain.unapplySeq("tom@sun.com"))
    }

    /**
      * 可变长度的匹配
      */
    def changeLenth(s:String)=s match {
        case Email("tom",Domain("com",_*))=>true
        case _ => false
    }
    def userTwiceUpper_test(): Unit ={
        userTwiceUpper("KiKi@hotmail.com")
    }

    /**
      * 可以看出匹配是从外到里进行匹配的
      * 对于变量绑定我们是可以将其写成提取器格式的，也就是说变量绑定的绑定对象可以使提取器
      * @param s
      */
    def userTwiceUpper(s:String)=s match {
        case Email(Twice(x @ Uppercase()),domain)=>println("重复大写匹配上了")
        case Email(Twice(x),domain)=>println("重复匹配上了")
        case _ => println("没有匹配上")
    }
    def getResult(email: String)=email match {
            // email 怎么匹配上 Email
            case Email(user,domain)=>println("匹配上了")
            case _=>None
        }
    def getResult(email: Any)=email match {
        // email 怎么匹配上 Email
        case Email(user,domain)=>println("匹配上了aa")
        case _=>None
    }

    def apply(user:String,domain:String)=user+"@"+domain

    def unapply(arg: String): Option[(String,String)] ={
        val parts=arg split "@"
        if (parts.length==2)Some(parts(0),parts(1)) else None
    }

}

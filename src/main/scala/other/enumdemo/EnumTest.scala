package scala.other.enumdemo

/*
*   在Scala中并没有枚举类型，但在标准类库中提供了Enumeration类来产出枚举。扩展Enumeration类后，调用Value方法来初始化枚举中的可能值。
*   Value 提供了枚举值比较以及id的获取
*   Scala中的枚举其实都是Enumeration.Value这个对象
*
* */
object EnumTest {

    object WeekDay extends Enumeration {
        /*values 实质上就是枚举类型变量的集合(更准确的来说是个方法)，来源于继承*/
        type WeekDay = Value //声明枚举对外暴露的变量类型
        val Mon = Value("1")
        val Tue = Value("2")
        val Wed = Value("3")
        val Thu = Value("4")
        val Fri = Value("5")
        val Sat = Value("6")
        val Sun = Value("7")

        def checkExists(day: String) = this.values.exists(_.toString == day) //检测是否存在此枚举值
        def isWorkingDay(day: WeekDay) = !(day == Sat || day == Sun) //判断是否是工作日
        def showAll = this.values.foreach(println) // 打印所有的枚举值

    }

    def main(args: Array[String]): Unit = {
        test1()
    }

    def test1(): Unit = {
        println(WeekDay.values)
        println(WeekDay.Fri.id)
        println(WeekDay.checkExists("8")) //检测是否存在
        println(WeekDay.Sun == WeekDay.withName("7")) //正确的使用方法
        println(WeekDay.Sun == "7") //错误的使用方法
        WeekDay.showAll //打印所有的枚举值
        println(WeekDay.isWorkingDay(WeekDay.Sun)) //是否是工作日
        val weekDay = WeekDay.Sun
        weekDay match {
            case WeekDay.Mon => println("星期一")
            case WeekDay.Tue => println("星期二")
            case WeekDay.Wed => println("星期三")
            case WeekDay.Thu => println("星期四")
            case WeekDay.Fri => println("星期五")
            case WeekDay.Sat => println("星期六")
            case WeekDay.Sun => println("星期日")
        }
    }

    def test_collection(): Unit = {
        val s = Set("a", "b", "c")
        println(s.exists("a" == _))
        println(s.contains("a"))
    }


}

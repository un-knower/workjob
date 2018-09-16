package scala.Book.mydone.scala2.ch02

import redis.clients.jedis.Tuple


object test {

    def main(args: Array[String]): Unit = {
        te()
    }

    def ifResult(): Unit = {
        val x: Any = if (1 > 0) "dfd" else 1
        println(x.getClass.getSimpleName)

        if (1 > 0) {
            println("hello ()")
            println("hello ()")
            println("hello ()")
        }
    }

    def abouteq(): Unit = {
        var y = 1
        val x = y = 1
        println(x)
        println(y)

    }

    /**
      * 字符串插值
      * {} 有是一定对的，没有可能结果是错误的
      */
    def aboutformat(): Unit = {
        val name = "刘文强"
        val p = "world"
        println(s"hello $name,hello ${p}")

        val age = 10
        // 这里你用 s 没错，但是不能起到你想要格式化字符串的效果
        println(f"${age + 0.5}%7.2f")
        //和python中的 r 有点像，拒绝转义
        println(raw"hello \n world")
        //获取美元符号
        println(s"hello $name,hello ${p}，hello $$")

    }

    def aobutrange(): Unit = {
        val x = 1 to 10
        println(x)

        val x1 = (1, 2, 3, "a", "b")
        for (iv <- List(x1)) {
            println(iv + "\t" + iv.getClass.getSimpleName)
        }
        //推导式的特殊之处循环体没有大括号,也就是说只能有一句
        val y = for (i <- 1 to 10 if i % 2 == 0) yield i

        println(y.mkString("\t"))
    }

    /**
      * return 唯一的作用就是立即退出
      */
    def aboutReturn(): Unit = {
        val x = {}
        println(x)
        println(x.getClass.getSimpleName)
        val y = Unit
        println(y)
        println(y.getClass.getSimpleName)
        return ()
        println("hello")
    }

    def te(): Unit = {
        for (x <- (1 to 10).reverse) {
            println(x)
        }
    }

    def strji(): Unit = {
        val s = "Hello"

    }


}

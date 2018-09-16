package scala.Book.mydone.scala2.ch02


object after {
    def main(args: Array[String]): Unit = {
        println(signum(10))
        countdown(10)
        println(strfabs("Hello"))

        println(fabs(10, 3))
    }

    def signum(x: Int): Int = {
        if (x >= 0)
            1
        else
            -1
    }

    def countdown(n: Int): Unit = {
        val x = (0 to n).reverse
        for (i <- x) {
            println(i)
        }
    }

    /**
      * 注意一下 if 里面不能是 0
      *
      * @param str
      * @return
      */
    def strfabs(str: String): Long = {
        if (str.length == 0)
            1
        else
            str.head.toLong * strfabs(str.tail)
    }

    def fabs(x: Int, n: Int): Int = {
        if (n > 0 && n % 2 == 0)
            fabs(x, n / 2) * fabs(x, n / 2)
        else if (n > 0 && n % 2 == 1)
            x * fabs(x, n - 1)
        else if (n == 0)
            1
        else
            fabs(x, math.abs(n))
    }


}

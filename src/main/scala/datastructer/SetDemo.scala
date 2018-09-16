package scala.collection


object SetDemo {
    def contain_collection(): Unit = {
        val s = Set("a", "b", "c")
        println(s.exists("a" == _))
        println(s.contains("a"))
    }

}

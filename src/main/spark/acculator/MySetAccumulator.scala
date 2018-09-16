package acculator

import org.apache.spark.util.AccumulatorV2

/**
  * 态度是 已经提供很多现成的累加器可供我们使用 例如 CollectionAccumulator 不过它的存储是使用集合来存储的
  */
class MySetAccumulator extends AccumulatorV2[String, java.util.Set[String]] {
    private val _logArray: java.util.Set[String] = new java.util.HashSet[String]()

    override def isZero: Boolean = {
        _logArray.isEmpty
    }

    override def reset(): Unit = {
        _logArray.clear()
    }

    // 这里是自定义的方法 添加成功后返回true 添加失败后返回false
    def myadd(v: String): Boolean = {
        return _logArray.add(v)
    }

    // 这里是默认要重写的方法
    override def add(v: String): Unit = {
        return _logArray.add(v)
    }

    override def merge(other: AccumulatorV2[String, java.util.Set[String]]): Unit = {
        other match {
            case o: MySetAccumulator => _logArray.addAll(o.value)
        }

    }

    override def value: java.util.Set[String] = {
        java.util.Collections.unmodifiableSet(_logArray)
    }

    // 这个方法才是关键
    override def copy(): MySetAccumulator = {
        val newAcc = new MySetAccumulator()
        _logArray.synchronized {
            newAcc._logArray.addAll(_logArray)
        }
        newAcc
    }
}
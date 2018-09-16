package acculator

import org.apache.spark.util.AccumulatorV2

import scala.collection.JavaConversions._

class MyMapAccumulator extends AccumulatorV2[String, java.util.Map[String, Int]] {
    private val _countmap: java.util.HashMap[String, Int] = new java.util.HashMap[String, Int]()

    override def isZero: Boolean = {
        _countmap.isEmpty
    }

    override def reset(): Unit = {
        _countmap.clear()
    }

    def myadd(v: (String, Int)): Unit = {
        val key = v._1
        val value = v._2
        if (_countmap.containsKey(key)) {
            _countmap.put(key, _countmap.get(key) + value)
        } else {
            _countmap.put(key, value)
        }
    }

    override def add(v: String): Unit = {

    }

    override def merge(other: AccumulatorV2[String, java.util.Map[String, Int]]): Unit = {
        other match {
            case o: MyMapAccumulator => other.value.foreach(num => {
                val key = num._1
                val value = num._2
                if (_countmap.containsKey(key)) {
                    _countmap.put(key, _countmap.get(key) + value)
                } else {
                    _countmap.put(key, value)
                }
            })
        }
    }

    override def value: java.util.Map[String, Int] = {
        java.util.Collections.unmodifiableMap(_countmap)
    }

    // 这个方法才是关键
    override def copy(): MyMapAccumulator = {
        val newAcc = new MyMapAccumulator()
        _countmap.synchronized {
            // newAcc._countmap.addAll(_countmap)
            _countmap.map(entry => newAcc._countmap += entry)

        }
        newAcc
    }


}

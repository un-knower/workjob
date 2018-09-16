package sparkdemo.complexjson

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types._
import org.apache.spark.sql.functions._

/**
  * 关键是理解每种方法的使用场景
  * structtype的定义
  *
  */
object ParseJson {
    val sparksession = SparkSession.builder().master("local[2]").appName("kingcall").getOrCreate()

    import sparksession.implicits._

    def main(args: Array[String]): Unit = {
        readJson()
    }

    def readJson(): Unit = {
        case class DeviceData(id: Int, device: String)
        val jsonSchema = new StructType().add("battery_level", LongType).add("c02_level", LongType).add("cca3", StringType).add("cn", StringType).add("device_id", LongType).add("device_type", StringType).add("signal", LongType).add("ip", StringType).add("temp", LongType).add("timestamp", TimestampType)
        val eventDS = Seq(
            (0, """{"device_id": 0, "device_type": "sensor-ipad", "ip": "68.161.225.1", "cca3": "USA", "cn": "United States", "temp": 25, "signal": 23, "battery_level": 8, "c02_level": 917, "timestamp" :1475600496 }"""),
            (1, """{"device_id": 1, "device_type": "sensor-igauge", "ip": "213.161.254.1", "cca3": "NOR", "cn": "Norway", "temp": 30, "signal": 18, "battery_level": 6, "c02_level": 1413, "timestamp" :1475600498 }"""),
            (19, """{"device_id": 19, "device_type": "sensor-ipad", "ip": "193.200.142.254", "cca3": "AUT", "cn": "Austria", "temp": 32, "signal": 27, "battery_level": 5, "c02_level": 1282, "timestamp" :1475600536 }""")).toDF("id", "device")
        /*从一个json字符串中，抽出来的列按照 Schema 组成 DF 的列*/
        val devicesDF = eventDS.select(from_json($"device", jsonSchema) as "devices").select($"devices.*").filter($"devices.temp" > 10 and $"devices.signal" > 15)
        devicesDF.show()

    }

    def readJson_from_json(): Unit = {
        case class DeviceData(id: Int, device: String)
        val jsonSchema = new StructType().add("battery_level", LongType).add("c02_level", LongType).add("cca3", StringType).add("cn", StringType).add("device_id", LongType).add("device_type", StringType).add("signal", LongType).add("ip", StringType).add("temp", LongType).add("timestamp", TimestampType)
        val eventDS = Seq(
            (0, """{"device_id": 0, "device_type": "sensor-ipad", "ip": "68.161.225.1", "cca3": "USA", "cn": "United States", "temp": 25, "signal": 23, "battery_level": 8, "c02_level": 917, "timestamp" :1475600496 }"""),
            (1, """{"device_id": 1, "device_type": "sensor-igauge", "ip": "213.161.254.1", "cca3": "NOR", "cn": "Norway", "temp": 30, "signal": 18, "battery_level": 6, "c02_level": 1413, "timestamp" :1475600498 }"""),
            (19, """{"device_id": 19, "device_type": "sensor-ipad", "ip": "193.200.142.254", "cca3": "AUT", "cn": "Austria", "temp": 32, "signal": 27, "battery_level": 5, "c02_level": 1282, "timestamp" :1475600536 }""")).toDF("id", "device")
        /*从一个json字符串中，抽出来的列按照 Schema 组成 DF 的列*/
        val devicesDF = eventDS.select(from_json($"device", jsonSchema) as "devices").select($"devices.*").filter($"devices.temp" > 10 and $"devices.signal" > 15)
        devicesDF.show()
    }

    def readJson_get_json_object(): Unit = {
        val eventsFromJSONDF = Seq(
            (0, """{"device_id": 0, "device_type": "sensor-ipad", "ip": "68.161.225.1", "cca3": "USA", "cn": "United States", "temp": 25, "signal": 23, "battery_level": 8, "c02_level": 917, "timestamp" :1475600496 }"""),
            (1, """{"device_id": 1, "device_type": "sensor-igauge", "ip": "213.161.254.1", "cca3": "NOR", "cn": "Norway", "temp": 30, "signal": 18, "battery_level": 6, "c02_level": 1413, "timestamp" :1475600498 }"""),
            (19, """{"device_id": 19, "device_type": "sensor-ipad", "ip": "193.200.142.254", "cca3": "AUT", "cn": "Austria", "temp": 32, "signal": 27, "battery_level": 5, "c02_level": 1282, "timestamp" :1475600536 }""")).toDF("id", "device")
        eventsFromJSONDF.printSchema()
        /* 这个方法的参数 第一个是带解析的列但必须是json字符串  第二个参数是带解析的字段也就是要从解析字符串中要解析出来的内容  */
        val jsDF = eventsFromJSONDF.select($"id", get_json_object($"device", "$.device_type").alias("device_type"), get_json_object($"device", "$.ip").alias("ip"), get_json_object($"device", "$.cca3").alias("cca3"))
        jsDF.printSchema
        jsDF.show()
    }

    /**
      * 演示了两种自定义schema 的方法
      * 解析了json 数组
      */
    def readJson_explode(): Unit = {
        /*spark 有自带的 schema 当然在比较复杂的时候我们可以自定义，下面的定义方式说明了他有一个构造函数，使用序列的structfiled 完成的*/
        val struct = StructType(
            StructField("partner_code", StringType, true) ::
                    StructField("app_name", StringType, true) ::
                    StructField("person_info", MapType(StringType, StringType, true)) ::
                    StructField("items", ArrayType(MapType(StringType, StringType, true))) ::
                    Nil)
        val DF3 = sparksession.read.schema(struct).json(sparksession.sparkContext.textFile("src/main/resources/externalfile/complex2.json"))
        DF3.show()
        DF3.printSchema()

        val struct2 = new StructType().add(StructField("partner_code", StringType, true)).add(StructField("app_name", StringType, true)).add(
            StructField("person_info", MapType(StringType, StringType, true))
        ).add(StructField("items", ArrayType(MapType(StringType, StringType, true))))
        val DF4 = sparksession.read.schema(struct2).json(sparksession.sparkContext.textFile("src/main/resources/externalfile/complex2.json").toDS())
        DF4.show()
        DF4.printSchema()
        DF4.select($"partner_code", $"app_name", $"person_info.name", $"person_info.age", explode($"items").alias("items")).select(
            $"partner_code", $"app_name", $"name", $"age", $"items.item_id", $"items.item_name", $"items.group", get_json_object($"items.item_detail", "$.platform_count").alias("platform")
        ).show()
    }

    /**
      * explode 也可以用在map 上
      */
    def othershow(): Unit = {
        val schema = new StructType()
                .add("dc_id", StringType) // data center where data was posted to Kafka cluster
                .add("source", // info about the source of alarm
            MapType( // define this as a Map(Key->value)
                StringType,
                new StructType()
                        .add("description", StringType)
                        .add("ip", StringType)
                        .add("id", LongType)
                        .add("temp", LongType)
                        .add("c02_level", LongType)
                        .add("geo", //MapType(StringType,StringType,true) 这个也行
                            new StructType()
                                    .add("lat", DoubleType)
                                    .add("long", DoubleType)
                        )
            )
        )
        val dataDS = Seq(
            """
    {
      "dc_id": "dc-101",
      "source": {
          "sensor-igauge": {
            "id": 10,
            "ip": "68.28.91.22",
            "description": "Sensor attached to the container ceilings",
            "temp":35,
            "c02_level": 1475,
            "geo": {"lat":38.00, "long":97.00}
          },
          "sensor-ipad": {
            "id": 13,
            "ip": "67.185.72.1",
            "description": "Sensor ipad attached to carbon cylinders",
            "temp": 34,
            "c02_level": 1370,
            "geo": {"lat":47.41, "long":-122.00}
          },
          "sensor-inest": {
            "id": 8,
            "ip": "208.109.163.218",
            "description": "Sensor attached to the factory ceilings",
            "temp": 40,
            "c02_level": 1346,
            "geo": {"lat":33.61, "long":-111.89}
          },
          "sensor-istick": {
            "id": 5,
            "ip": "204.116.105.67",
            "description": "Sensor embedded in exhaust pipes in the ceilings",
            "temp": 40,
            "c02_level": 1574,
            "geo": {"lat":35.93, "long":-85.46}
          }
        }
    }""").toDS()
        val df = sparksession.read.schema(schema).json(dataDS)
        df.printSchema()
        val df2 = df.select($"dc_id", explode($"source")).select($"dc_id", $"key", $"value.id", $"value.ip", $"value.description", $"value.geo.lat", $"value.geo.long")
        df2.show()
        df2.printSchema()
        val df3 = df.select($"dc_id", $"source.sensor-igauge.description", $"source.sensor-igauge.ip", $"source.sensor-igauge.id", $"source.sensor-igauge.temp", $"source.sensor-igauge.c02_level",
            $"source.sensor-igauge.geo.lat", $"source.sensor-igauge.geo.long",
            $"source.sensor-ipad.description", $"source.sensor-ipad.ip", $"source.sensor-ipad.id", $"source.sensor-ipad.temp", $"source.sensor-ipad.c02_level",
            $"source.sensor-ipad.geo.lat", $"source.sensor-ipad.geo.long",
            $"source.sensor-inest.description", $"source.sensor-inest.ip", $"source.sensor-inest.id", $"source.sensor-inest.temp", $"source.sensor-inest.c02_level",
            $"source.sensor-inest.geo.lat", $"source.sensor-inest.geo.long",
            $"source.sensor-istick.description", $"source.sensor-istick.ip", $"source.sensor-istick.id", $"source.sensor-istick.temp", $"source.sensor-istick.c02_level",
            $"source.sensor-istick.geo.lat", $"source.sensor-istick.geo.long"
        )
        df3.show()
        df3.printSchema()


    }


}

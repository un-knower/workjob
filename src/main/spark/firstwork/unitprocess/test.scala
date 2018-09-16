package sparkdemo.firstwork.unitprocess

import com.alibaba.fastjson.JSON


object test {
    def main(args: Array[String]): Unit = {
        val s =
            """
              |_v,aid,an,av,cd,cds,chn,cid,city,cms,country,cts,dmm,dmt,dmv,dpv,ds,flt,guid,isp,lat,mt,ns_ts,province,pt,s_hn,s_ts,s_uip,sbt,sid,so,sr,t,tid,ua,uid,ul,uno,unt,uuid,v
            """.stripMargin
        println(s.split(",").length)
    }

}

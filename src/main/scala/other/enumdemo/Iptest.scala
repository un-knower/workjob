package scala.other.enumdemo

import java.net.InetAddress

import scala.other.enumdemo.IpEnum._


object Iptest {
    def getIP: String = {
        InetAddress.getLocalHost.getHostAddress
    }

    def initConf(localConf: String, remoteConf: String) = {
        val ip: String = "10.52.7.88"
        println(ip)
        val configFile = ip match {
            case _ if ip.startsWith(DevelopEnv.toString) => localConf
            case _ if ip.startsWith(TestEnv.toString) => remoteConf
            case _ if ip.startsWith(RemoteEnv.toString) => remoteConf
            case _ if ip.startsWith(TestCLusterEnv.toString) => remoteConf
            case _ if ip.startsWith(M.toString) => "kingcall"
        }
        configFile
    }

    def main(args: Array[String]): Unit = {
        println(initConf("local", "remote"))
    }

}

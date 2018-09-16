package scala.other.enumdemo

object IpEnum extends Enumeration {
    type IpEnum = Value
    val DevelopEnv = Value("10.200.202.")
    val TestEnv = Value("192.168.10.")
    val RemoteEnv = Value("10.52.7.")
    val TestCLusterEnv = Value("192.168.9.")
    val M = Value("192.168.131.")
}
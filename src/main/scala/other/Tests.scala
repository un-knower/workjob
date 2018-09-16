package scala.other

import java.time.LocalDate

case class Dates(createdAt: LocalDate, updatedAt: LocalDate, startDate: LocalDate, endDate: LocalDate)

object Tests extends App {
    println(List(Dates))
    val fields = Dates.getClass.getDeclaredFields()
    println(fields.mkString("="))
}

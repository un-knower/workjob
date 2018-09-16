package classdemo.abstractdemo

class Food
abstract class Aniamal{
  def eat(food:Food)
}
abstract class Aniamal2{
  type suitefood <: Food
  def eat(food:suitefood)
}
class Grass extends Food
class Cow extends Aniamal{
  override def eat(food: Food): Unit = {
    println("牛的事物类型必须是 Food 不能是 Grass")
  }
}
class  Cow2 extends Aniamal2{
  override type suitefood = Grass
  override def eat(food: suitefood): Unit = {
    println("这个牛是吃草的")
  }
}
class Fish extends Food
object Demo2 {
  def main(args: Array[String]): Unit = {
    new Cow2().eat(new Grass)

  }

}

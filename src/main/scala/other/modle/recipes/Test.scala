package other.modle.recipes

import other.modle.recipes.Entity.Apple

object Test {

    def main(args: Array[String]): Unit = {
        println(SimpleDatabase.foodNamed("Apple"))
        println(SimpleBrowser.recipesUsing(Apple))
    }

}

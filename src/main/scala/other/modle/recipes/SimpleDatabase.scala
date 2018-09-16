package other.modle.recipes

import other.modle.recipes.Entity._

object SimpleDatabase {
    def allFoods=List(Apple,Orange,Cream,Sugar)
    def foodNamed(name:String):Option[Food]={
        allFoods.find(_.name==name)
    }
    def allRecipes:List[Recipe]=List(FruitSalad)
    case class FoodCategory(name:String,foods:List[Food])
    private var categories=List(
        FoodCategory("fruits",List(Apple,Orange)),
        FoodCategory("misc",List(Cream,Sugar))
    )
    def allCategories=categories
}
object SimpleBrowser{
    /**
      * 用了该食材的菜
      * @param food
      */
    def recipesUsing(food: Food) ={
        SimpleDatabase.allRecipes.filter(receip=>{
            receip.ingredents.contains(food)
        })
    }
    def dispalyCategory(category:SimpleDatabase.FoodCategory)={
        println(category)
    }
}

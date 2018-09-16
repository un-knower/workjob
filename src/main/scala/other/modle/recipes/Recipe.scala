package other.modle.recipes

class Recipe(val name:String,val ingredents:List[Food],val instructions:String) {
    override def toString: String = name
}

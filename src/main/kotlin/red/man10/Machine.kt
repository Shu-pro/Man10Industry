package red.man10

data class Machine (
        var name: String,
        var imageName: String?,
        var recipes: MutableList<Recipe>
)
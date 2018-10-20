package red.man10.models

data class Machine (
        var name: String,
        var imageName: String?,
        var recipes: MutableList<Recipe>
)
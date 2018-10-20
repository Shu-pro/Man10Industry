package red.man10.models

data class ChanceSet(
        var req: Int, //必須レベル
        var chances: MutableMap<Int, Double> //レベル, 確率
)
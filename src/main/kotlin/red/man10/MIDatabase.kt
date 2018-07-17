package red.man10

class MIDatabase {

    companion object {
        var pl: MIPlugin? = null
    }

    fun initialize(plugin: MIPlugin): MIDatabase {
        pl = plugin
        return this
    }

}
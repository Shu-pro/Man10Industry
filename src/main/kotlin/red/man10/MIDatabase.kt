package red.man10

class MIDatabase {

    companion object {
        var pl: MIPlugin? = null
        var mysql: MySQLManager? = null
    }

    fun initialize(plugin: MIPlugin): MIDatabase {
        pl = plugin
        mysql = MySQLManager(pl, "mIndustry")

        return this
    }

}
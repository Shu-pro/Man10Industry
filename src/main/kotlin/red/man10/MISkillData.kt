package red.man10

import org.bukkit.Bukkit
import java.util.*

class MISkillData {

    var mysql: MySQLManager? = null

    companion object {
        var pl: MIPlugin? = null
    }

    fun initialize(plugin: MIPlugin): MISkillData {
        pl = plugin
        mysql = MySQLManager(plugin, "mIndustry")

        return this
    }

    fun loadPlayerDataFromDB(uuid: UUID) {
        var playerData: MutableMap<Int, Int> = mutableMapOf()

        for (skillId in 1..pl!!.skills.count()) {
            val skillData = mysql!!.query("select * from player_data where player_uuid = '$uuid' AND skill_id = '$skillId'")
            if (!(skillData!!.next())) {
                mysql!!.execute("insert into player_data values (0, '" + uuid.toString() + "', " + skillId.toString() + ", 0)")
                playerData.put(skillId, 0)
            } else {
                print(skillData.getInt("level"))
                playerData.put(skillId, skillData.getInt("level"))
            }
        }
        mysql!!.close()

        pl!!.currentPlayerData[uuid] = playerData
    }

    fun savePlayerDataToDB(uuid: UUID) {
        val playerData = pl!!.currentPlayerData[uuid]!!
        for (skillId in 1..pl!!.skills.count()) {
            setPlayerData(uuid, skillId, playerData[skillId]!!)
        }
        pl!!.currentPlayerData.remove(uuid)
    }

    fun setPlayerData(uuid: UUID, skillId: Int, level: Int) {
        mysql!!.execute("update player_data set level = ${level} where player_uuid = '$uuid' and skill_id = $skillId")
        if (Bukkit.getPlayer(uuid) != null) {
            pl!!.currentPlayerData[uuid]!![skillId] = level
        }
    }

}
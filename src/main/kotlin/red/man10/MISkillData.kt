package red.man10

import org.bukkit.Bukkit
import java.util.*

class MISkillData {

    var currentPlayerData: MutableMap<UUID, PlayerSkillData> = mutableMapOf()

    companion object {
        var pl: MIPlugin? = null
    }

    fun initialize(plugin: MIPlugin): MISkillData {
        pl = plugin
        //mysql = pl!!.mysql

        return this
    }

//    fun loadAllDataFromPlayer(uuid: UUID, mysql: MySQLManager) { //OnPlayerLogin
//        var playerData: PlayerSkillData = mutableMapOf()
//        var allSkillData = mysql.query("select * from player_data where player_uuid = '$uuid'")
//        var idCount = 1
//        if (allSkillData.isLast) {
//            mysql.execute("insert into player_data values (0, '" + uuid.toString() + "', " + 1 + ", 0)")
//        }
//        while (allSkillData!!.next()) {
//            if (allSkillData.getInt("skill_id") == idCount) {
//                playerData.put(idCount, allSkillData.getInt("level"))
//            } else { //データーがなかったら
//                mysql.execute("insert into player_data values (0, '" + uuid.toString() + "', " + idCount.toString() + ", 0)")
//                playerData.put(idCount, 0)
//            }
//            idCount++
//        }
////        for (skillId in 1..pl!!.skills.count()) {
////            val skillData = mysql.query("select * from player_data where player_uuid = '$uuid' AND skill_id = '$skillId'")
////            if (!(skillData!!.next())) { //データーなかったら:
////                mysql.execute("insert into player_data values (0, '" + uuid.toString() + "', " + skillId.toString() + ", 0)")
////                playerData.put(skillId, 0)
////            } else { //あったら:
////                print(skillData.getInt("level"))
////                playerData.put(skillId, skillData.getInt("level"))
////            }
////        }
//
//        mysql.close()
//
//        pl!!.currentPlayerData[uuid] = playerData
//    }
//
//    fun saveAllDataFromPlayer(uuid: UUID, mysql: MySQLManager) { //OnPLayerLogout
//        val playerData = pl!!.currentPlayerData[uuid]!!
//
//        for (skillId in 1..pl!!.skills.count()) {
//            val level = playerData[skillId]!!
//            setPlayerData(uuid, level, mysql, skillId)
//        }
//
//        pl!!.currentPlayerData.remove(uuid)
//    }

    fun getPlayerData(uuid: UUID, skillId: Int): Int {
        //if (currentPlayerData[uuid] != null) {
            if (currentPlayerData[uuid]!![skillId] == null) {
                //No Cache
                val mysql = MySQLManager(pl!!, "MIGET_" + uuid.toString() + "_" + skillId)

                var skillValue = mysql.query("select level from player_data where player_uuid = '$uuid' and skill_id = $skillId")
                if (!(skillValue.isBeforeFirst)) {
                    //No Data In DB, Create New
                    mysql.execute("insert into player_data values (0, '" + uuid.toString() + "', " + skillId.toString() + ", 0) ")
                    currentPlayerData[uuid]!![skillId] = 0
                    mysql.close()
                    return 0
                } else {
                    //Update cache from DB
                    //wrong: mysql.query("update player_data set level = 0 where uuid = '" + uuid.toString() + "' and skill_id = " + skillId.toString())
                    skillValue.next()
                    val level = skillValue.getInt("level")
                    currentPlayerData[uuid]!![skillId] = level
                    mysql.close()
                    return level
                }
            }
        //}
        //Cache exists
        return currentPlayerData[uuid]!![skillId]!!
    }

    fun setPlayerData(uuid: UUID, level: Int, skillId: Int) { //On Update
        currentPlayerData[uuid]!![skillId] = level

        val mysql = MySQLManager(pl!!, "MISET_" + uuid.toString() + "_" + skillId)
        var skillValue = mysql.query("select level from player_data where player_uuid = '$uuid' and skill_id = '$skillId'")
        if (!(skillValue.isBeforeFirst)) {
            //No Data In DB, Create New
            mysql.execute("insert into player_data values (0, '" + uuid.toString() + "', " + skillId.toString() + ", " + level + ") ")
        } else {
            //Data ready
            mysql.execute("update player_data set level = " + level + " where player_uuid = '" + uuid.toString() + "' and skill_id = " + skillId.toString())
        }
        mysql.close()
    }


}
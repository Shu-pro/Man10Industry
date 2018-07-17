package red.man10

import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.Material
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent


class MIListener: Listener {

    companion object {
        var pl: MIPlugin? = null
    }

    fun initialize(plugin: MIPlugin): MIListener {
        pl = plugin
        return this
    }

    @EventHandler
    fun onItemClick(e: PlayerInteractEvent) {

    }

    @EventHandler
    fun onPlayerJoin(e: PlayerJoinEvent) {
        Bukkit.getScheduler().runTaskAsynchronously(MIConfig.pl!!, {
            pl!!.skill.loadPlayerDataFromDB(e.player.uniqueId)
        })
    }

    @EventHandler
    fun onPlayerQuit(e: PlayerQuitEvent) {
        Bukkit.getScheduler().runTaskAsynchronously(MIConfig.pl!!, {
            pl!!.skill.savePlayerDataToDB(e.player.uniqueId)
        })
    }
}
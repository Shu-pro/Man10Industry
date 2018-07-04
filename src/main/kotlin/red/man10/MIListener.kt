package red.man10

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.Material


class MIListener: Listener {

    companion object {
        var pl: MIPlugin? = null
    }

    fun initialize(plugin: MIPlugin): MIListener {
        pl = plugin
        return MIListener()
    }

    @EventHandler
    fun onItemClick(e: PlayerInteractEvent) {
//        var p = e.player
//        var itemInHand = p.inventory.itemInMainHand
//        if (itemInHand != null && itemInHand.type !== Material.AIR) {
//            //if (e.action == e.action.RIGHT_CLICK_AIR || e.action == e.action.RIGHT_CLICK_BLOCK) {
//                val cPlayer = p as CraftPlayer
//                val eat = PacketPlayOutEntityStatus(cPlayer.getHandle(), 9.toByte())
//                cPlayer.handle.playerConnection.sendPacket(eat)
//            //}
//        }
    }
}
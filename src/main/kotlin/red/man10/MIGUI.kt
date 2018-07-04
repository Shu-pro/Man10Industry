package red.man10

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Item
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import red.man10.MIPlugin

class MIGUI {

    companion object {
        var pl: MIPlugin? = null
    }

    fun initialize(plugin: MIPlugin): MIGUI {
        pl = plugin
        return MIGUI()
    }

    fun openProcessingView(p: Player) {
        val inv = Bukkit.getServer().createInventory(null, 54, pl!!.prefix + "§0加工メニュー")

        val blackGlass = createItem(Material.STAINED_GLASS_PANE, 15.toShort(), "", mutableListOf())
        placeItem(blackGlass, inv, mutableListOf(0,1,2,3,4,9,18,27,36,45,46,47,48,49,40,13))

        val redGlass = createItem(Material.STAINED_GLASS_PANE, 14.toShort(), "", mutableListOf())
        placeItem(redGlass, inv, mutableListOf(5,6,7,8,17,26,35,44,53,52,51,50,41,14))

        val rightArrow = createDHItem("§b§lクリックで§e§l加工!", mutableListOf(), 963.toShort())
        placeItem(rightArrow, inv, mutableListOf(22,23,31,32))
        p.openInventory(inv)
    }

    fun createItem(material: Material, itemtype: Short?, itemName: String, loreList: MutableList<String>): ItemStack {
        val CIitemStack = ItemStack(material, 1, itemtype!!)
        val CIitemMeta = CIitemStack.itemMeta
        CIitemMeta.displayName = itemName
        CIitemMeta.lore = loreList
        CIitemStack.itemMeta = CIitemMeta
        return CIitemStack
    }

    fun createDHItem(itemName: String, loreList: MutableList<String>, durability: Short): ItemStack {
        val CIItemStack = ItemStack(Material.DIAMOND_HOE, 1, durability)
        val CIItemMeta = CIItemStack.itemMeta
        CIItemMeta.displayName = itemName
        CIItemMeta.isUnbreakable = true
        CIItemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE)
        CIItemStack.itemMeta = CIItemMeta
        return CIItemStack
    }

    fun placeItem(item: ItemStack, inv: Inventory, places: MutableList<Int>) {
        for (place in places) {
            inv.setItem(place, item)
        }
    }

}
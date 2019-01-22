package red.man10

import org.apache.commons.lang.mutable.Mutable
import org.bukkit.Material
import org.bukkit.entity.Item
import org.bukkit.inventory.ItemStack
import red.man10.models.Machine
import red.man10.models.Recipe
import java.util.Collections.addAll
import java.awt.Graphics2D
import org.bukkit.entity.Player
import java.awt.image.ImageObserver
import java.io.File
import javax.imageio.ImageIO
import org.bukkit.Bukkit
import java.awt.image.BufferedImage




class MIMachine {

    companion object {
        var pl: MIPlugin? = null
    }

    fun initialize(plugin: MIPlugin): MIMachine {
        pl = plugin
        return this
    }

    fun process(p: PlayerSkillData, machine: Machine, inputs: MutableList<ItemStack>): Pair<MutableList<ItemStack>,MutableList<ItemStack>> {
        print(inputs)
        var usableRecieps = mutableListOf<Recipe>()
        for (recipe in machine.recipes) {
            var result = checkIfAContainsAllItemsOfB(recipe.inputs, inputs)
            print(result)
            if (result.first) {
                usableRecieps.add(recipe)
                print(result.second)
            }
        }
        print(usableRecieps)

        usableRecieps.filter {
            for (chanceSetWithSkill in it.chanceSets) {
                if (p[chanceSetWithSkill.key]!!.toInt() < chanceSetWithSkill.value.req) {
                    return@filter true
                }
            }
            return@filter false
        }

        print(":::")
        return Pair(mutableListOf(ItemStack(Material.APPLE, 10)),)
    }

    fun compressItemList(list: MutableList<ItemStack>): MutableList<ItemStack> {
        var compressedList: MutableList<ItemStack> = mutableListOf()
        for (item in list) {
            var didSameItemExisted = false
            for (compressedItem in compressedList) {
                if (item.isSimilar(compressedItem)) {
                    compressedItem.amount += item.amount
                    didSameItemExisted = true
                }
            }
            if (!didSameItemExisted) {
                compressedList.add(item)
            }
        }
        return pl!!.util.cloneMutableItemStackList(compressedList)
    }

    fun checkIfAContainsAllItemsOfB(listA: MutableList<ItemStack>, listB: MutableList<ItemStack>): Pair<Boolean, MutableList<ItemStack>> { //Bの方がでかい
        val compressedListA = compressItemList(listA) //ここでclone
        val compressedListB = compressItemList(listB)
        print(compressedListA)
        print(compressedListB)
        for (itemA in compressedListA) {
            for (itemB in compressedListB) {
                if (itemA.isSimilar(itemB)) {
                    if (itemA.amount <= itemB.amount) {
                        itemB.amount -= itemA.amount
                        itemA.amount = 0
                    } else {
                        itemA.amount -= itemB.amount
                        itemB.amount = 0
                    }
                }
            }
        }
        if (compressedListA.filter { it.amount != 0 }.isEmpty()) {
            return Pair(true, compressedListB)
        }
        return Pair(false, mutableListOf())
    }

    fun createMapItem(machineKey: String): ItemStack {
        var map = MappRenderer.getMapItem(pl!!, machineKey)
        map.itemMeta.displayName = "§b§l" + pl!!.machines[machineKey]!!.name + "§r§7(100/100)"
        return map
    }

    fun createAllMachineMapp(){
        for (machine in pl!!.machines) {
            MappRenderer.draw(machine.key, 0) { key: String, mapId: Int, g: Graphics2D ->
                //      画面更新をする
                val result = drawImage(g, machine.value.imageName, 0, 0, 128, 128)
                if (!result) {
                    g.drawString("No Image Found", 10, 10)
                }
                true
            }
            MappRenderer.displayTouchEvent("machine") { key: String, mapId: Int, player: Player, x: Int, y: Int ->
                player.chat("/mi usemachine $key")
                true
            }
        }
    }

    fun drawImage(g: Graphics2D, imageKey: String, x: Int, y: Int, w: Int, h: Int): Boolean {
        val image = MappRenderer.image(imageKey)
        if (image == null) {
            Bukkit.getLogger().warning("no image:$imageKey")
            return false
        }

        g.drawImage(image, x, y, w, h, null)

        return true
    }
}
package red.man10

import org.apache.commons.lang.mutable.Mutable
import org.bukkit.Material
import org.bukkit.entity.Item
import org.bukkit.inventory.ItemStack
import red.man10.models.Machine
import red.man10.models.Recipe

class MIMachine {

    companion object {
        var pl: MIPlugin? = null
    }

    fun initialize(plugin: MIPlugin): MIMachine {
        pl = plugin
        return this
    }

    fun process(p: PlayerSkillData, machine: Machine, inputs: MutableList<ItemStack>): MutableList<ItemStack> {
        print(inputs)
        var usableRecieps = mutableListOf<Recipe>()
        for (recipe in machine.recipes) {
            var inputsCopy = inputs
            for (recipeInput in recipe.inputs) {
                var result = listContainsItem(inputsCopy, recipeInput)
                if (result.first) {
                    inputsCopy[result.second].amount -= recipeInput.amount//レシピに該当するので減らす
                    if (inputsCopy[result.second].amount == 0) {
                        inputsCopy.removeAt(result.second) //0個なので削除
                    }
                }
            }
            if (inputsCopy.isEmpty()) {
                usableRecieps.add(recipe)
            }
        }
        print(usableRecieps)
        return mutableListOf(ItemStack(Material.APPLE, 10))
    }

    fun listContainsItem(list: MutableList<ItemStack>, item: ItemStack): Pair<Boolean, Int> { //Ignoring amount
        var count = 0
        for (listItem in list) {
            if (compareItemWithoutAmount(listItem, item)) {
                if (listItem.amount <= item.amount) {
                    return Pair(true, count)
                }
            }
            count++
        }
        return Pair(false, 0)
    }

    fun compareItemWithoutAmount(first: ItemStack, second: ItemStack): Boolean {
        first.amount = 1
        second.amount = 1
        return (first == second)
    }
}
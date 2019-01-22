package red.man10

import org.apache.commons.lang.mutable.Mutable
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Item
import java.io.File
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder
import sun.plugin2.liveconnect.ArgumentHelper.writeObject
import org.bukkit.util.io.BukkitObjectOutputStream
import java.io.ByteArrayOutputStream
import org.bukkit.inventory.ItemStack
import java.io.IOException
import sun.plugin2.liveconnect.ArgumentHelper.readObject
import org.bukkit.util.io.BukkitObjectInputStream
import java.io.ByteArrayInputStream

class MIUtility {

    companion object {
        var pl: MIPlugin? = null
    }

    fun initialize(plugin: MIPlugin): MIUtility {
        pl = plugin
        return this
    }

    // Below made by @takatronix

    @Throws(IllegalStateException::class)
    fun itemStackArrayToBase64(items: Array<ItemStack>): String {
        try {
            val outputStream = ByteArrayOutputStream()
            val dataOutput = BukkitObjectOutputStream(outputStream)

            // Write the size of the inventory
            dataOutput.writeInt(items.size)

            // Save every element in the list
            for (i in items.indices) {
                dataOutput.writeObject(items[i])
            }

            // Serialize that array
            dataOutput.close()
            return Base64Coder.encodeLines(outputStream.toByteArray())
        } catch (e: Exception) {
            throw IllegalStateException("Unable to save item stacks.", e)
        }
    }

    @Throws(IOException::class)
    fun itemStackArrayFromBase64(data: String): MutableList<ItemStack> {
        try {
            val inputStream = ByteArrayInputStream(Base64Coder.decodeLines(data))
            val dataInput = BukkitObjectInputStream(inputStream)
            val items = arrayOfNulls<ItemStack>(dataInput.readInt())

            // Read the serialized inventory
            for (i in items.indices) {
                items[i] = dataInput.readObject() as ItemStack
            }

            dataInput.close()
            return unwrapItemStackMutableList(items.toMutableList())
        } catch (e: ClassNotFoundException) {
            throw IOException("Unable to decode class type.", e)
        }

    }

    fun testF() {
        print("hello!!!")
    }

    fun unwrapItemStackMutableList(list: MutableList<ItemStack?>): MutableList<ItemStack>{
        val unwrappedList = mutableListOf<ItemStack>()
        for (item in list) {
            if (item != null) {
                unwrappedList.add(item)
            }
        }
        return unwrappedList
    }

    fun cloneMutableItemStackList(list: MutableList<ItemStack>): MutableList<ItemStack> {
        var newList = mutableListOf<ItemStack>()
        for (item in list) {
            newList.add(item.clone())
        }
        return newList
    }
}
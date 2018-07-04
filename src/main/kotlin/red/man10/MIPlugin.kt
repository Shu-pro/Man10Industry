package red.man10

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import red.man10.MIGUI
import sun.misc.resources.Messages

class MIPlugin: JavaPlugin() {

    val prefix = "§a[§bm§3Industry§a] §b"

    val listener = MIListener().initialize(this)
    val db = MIDatabase().initialize(this)
    val config = MIConfig().initialize(this)
    val util = MIUtility().initialize(this)
    val gui = MIGUI().initialize(this)

    var chanceSets = mutableMapOf<String, ChanceSet>()
    var recipies = mutableMapOf<String, Recipe>()
    var skills = mutableListOf<Skill>()

    override fun onEnable() {
        server.pluginManager.registerEvents(listener, this)
        config.loadAll(Bukkit.getConsoleSender())
    }

    override fun onDisable() {

    }

    override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<String>): Boolean {
        if (sender is Player) {
            if (sender.hasPermission("mi.op")) {
                when (args.size) {
                    0 -> {
                        showHelp(sender)
                        return true
                    }
                    1 -> {
                        when (args[0]) {
                            "help" -> {
                                showHelp(sender)
                                return true
                            }
                            "reload" -> {
                                config.loadAll(sender)
                                return true
                            }
                            "list" -> {
                                sender.sendMessage(prefix + "§bChanceSets: ")
                                sender.sendMessage(prefix + "§7" + chanceSets.keys)
                                sender.sendMessage(prefix + "§bSkills: ")

                                val skillStringList = mutableListOf<String>()
                                skills.onEach { skillStringList.add(it.name) }
                                sender.sendMessage(prefix + "§7" + skillStringList)

                                sender.sendMessage(prefix + "§bRecipes: ")
                                sender.sendMessage(prefix + "§7" + recipies.keys)
                                return true
                            }
                            "0711" -> {
                                for (i in 1..100) {
                                    sender.sendMessage(prefix + ":;(∩´﹏`∩);:")
                                }
                                return true
                            }
                            "usemachine" -> {
                                gui.openProcessingView(sender)
                            }
                            else -> {
                                warnWrongCommand(sender)
                                return true
                            }
                        }
                    }
                    2 -> {
                        when (args[0]) {
                            else -> {
                                warnWrongCommand(sender)
                                return true
                            }
                        }
                    }
                    3 -> {
                        when(args[0]) {
                            "info" -> {
                                when (args[1]) {
                                    "c" -> {
                                        if (chanceSets[args[2]] != null) {
                                            sender.sendMessage(prefix + "§bData of ChanceSet: " + args[2])
                                            sender.sendMessage(prefix + "§7" + chanceSets[args[2]])
                                        } else {
                                            sender.sendMessage(prefix + "§bChanceSet: " + args[2] + " doesn't exist")
                                        }
                                        return true
                                    }
                                    else -> {
                                        warnWrongCommand(sender)
                                        return true
                                    }
                                }
                            }
                            else -> {
                                warnWrongCommand(sender)
                                return true
                            }
                        }
                    }
                }
            }
            if (sender.hasPermission("mi.use")){
                when (args[0]) {
                    else -> {
                        showHelp(sender)
                        return true
                    }
                }
            }
        } else {
            sender.sendMessage(prefix + "コンソールからは実行できません。")
            return true
        }
        return false
    }

    fun showHelp(sender: CommandSender) {
        sender.sendMessage("§a******** §b§lm§3§lIndustry §a********")
        sender.sendMessage("§bData Managements")
        sender.sendMessage("§3/mi reload §7Load all data")
        sender.sendMessage("§3/mi list §7View all machines/recipes/skills")
        sender.sendMessage("§3/mi info [c/m/r/s] [id] §7View specific data")
        sender.sendMessage("§3/mi setinput [recipeId] §7Set an input for recipe")
        sender.sendMessage("§3/mi setoutput [recipeId] §7Set an output for recipe")
        sender.sendMessage("§bVer 1.0 : by Shupro")
        sender.sendMessage("§a***************************")
    }

    fun warnWrongCommand(sender: CommandSender) {
        sender.sendMessage(prefix + "Wrong Command. /mi help")
    }
}

package red.man10

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import red.man10.MIGUI
import sun.misc.resources.Messages
import java.util.*

class MIPlugin: JavaPlugin() {

    val prefix = "§a[§bm§3Industry§a] §b"

    val listener = MIListener().initialize(this)
    val db = MIDatabase().initialize(this)
    val config = MIConfig().initialize(this)
    val util = MIUtility().initialize(this)
    val gui = MIGUI().initialize(this)
    val skill = MISkillData().initialize(this)
    val chat = MIChat().initialize(this)

    var chanceSets = mutableMapOf<String, ChanceSet>()
    var recipies = mutableMapOf<String, Recipe>()
    var skills = mutableListOf<Skill>()
    var machines = mutableMapOf<String, Machine>()

    var currentPlayerData: MutableMap<UUID, MutableMap<Int, Int>> = mutableMapOf()//スキルid, スキルレベル

    override fun onEnable() {
        server.pluginManager.registerEvents(listener, this)
        server.pluginManager.registerEvents(gui, this)

        for (player in Bukkit.getOnlinePlayers()) {
            skill.loadPlayerDataFromDB(player.uniqueId)
        }

        config.loadAll(Bukkit.getConsoleSender())
    }

    override fun onDisable() {
        for (player in Bukkit.getOnlinePlayers()) {
            skill.savePlayerDataToDB(player.uniqueId)
        }
    }

    override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<String>): Boolean {
        if (sender is Player) {
            if (sender.hasPermission("mi.use")){
                when (args.size) {
                    0 -> {
                        chat.showTopMenu(sender)
                        return true
                    }
                    1 -> {
                        when (args[0]) {
                            "myskill" -> {
                                var skillId = 1
                                for (skill in skills) {
                                    when (skillId) {
                                        1 -> {
                                            sender.sendMessage(prefix + "§e加工スキル:")
                                        }
                                        5 -> {
                                            sender.sendMessage(prefix + "§e魔法スキル:")
                                        }
                                        9 -> {
                                            sender.sendMessage(prefix + "§e学問スキル:")
                                        }
                                        13 -> {
                                            sender.sendMessage(prefix + "§eスペシャルスキル:")
                                        }
                                    }
                                    val skillArrow = "§8 " + "〉".repeat(7 - skill.name.length)
                                    val level = currentPlayerData[sender.uniqueId]!![skillId]!!
                                    sender.sendMessage(prefix +
                                            " §b" + skill.name +
                                            skillArrow +
                                            " §a" + String.format("%3s", level.toString() + "Lv " +
                                            chat.returnProgressBar((level.toDouble()/100))))
                                    //sender.sendMessage(prefix + "§b" + skill.name + " §a" + currentPlayerData[sender.uniqueId]!![skillId] + "Lv - " + chat.returnProgressBar(0.3))
                                    //sender.sendMessage(prefix + "§b" + chat.returnProgressBar(0.3) + " §b" + skill.name + " §a" + currentPlayerData[sender.uniqueId]!![skillId] + "Lv ")

                                    skillId++
                                }
                                return true
                            }
                        }
                    }
                }
            }
            if (sender.hasPermission("mi.op")) {
                when (args.size) {
                    0 -> {
                    }
                    1 -> {
                        when (args[0]) {
                            "help" -> {
                                showHelp(sender)
                                skill.loadPlayerDataFromDB(sender.uniqueId)
                                return true
                            }
                            "reload" -> {
                                config.loadAll(sender)
                                return true
                            }
                            "list" -> {
                                showList(sender)
                                return true
                            }
                            "0711" -> {
                                for (i in 1..100) {
                                    sender.sendMessage(prefix + ":;(∩´﹏`∩);:")
                                }
                                return true
                            }
                        }
                    }
                    2 -> {
                        when (args[0]) {
                            "usemachine" -> {
                                gui.openProcessingView(sender, args[1])
                                return true
                            }
                        }
                    }
                    3 -> {
                        when(args[0]) {
                            "info" -> {
                                when (args[1]) {
                                    "c" -> {
                                        if (chanceSets[args[2]]  != null) {
                                            sender.sendMessage(prefix + "§bData of ChanceSet: " + args[2])
                                            sender.sendMessage(prefix + "§7" + chanceSets[args[2]])
                                        } else {
                                            sender.sendMessage(prefix + "§bChanceSet: " + args[2] + " doesn't exist")
                                        }
                                        return true
                                    }
                                    "m" -> {
                                        if (machines[args[2]] != null) {
                                            sender.sendMessage(prefix + "§bData of Machine: " + args[2])
                                            sender.sendMessage(prefix + "§7" + machines[args[2]])
                                        } else {
                                            sender.sendMessage(prefix + "§bMachine: " + args[2] + " doesn't exist")
                                        }
                                        return true
                                    }
                                    "r" -> {
                                        if (recipies[args[2]] != null) {
                                            sender.sendMessage(prefix + "§bData of Recipe: " + args[2])
                                            sender.sendMessage(prefix + "§7" + recipies[args[2]])
                                        } else {
                                            sender.sendMessage(prefix + "§bRecipe: " + args[2] + " doesn't exist")
                                        }
                                        return true
                                    }
                                }
                            }

                        }
                    }
                    4 -> {
                        when(args[0]) {
                            "setlevel" -> {
                                val targetPlayer = Bukkit.getPlayer(args[1])
                                if (targetPlayer == null) {
                                    sender.sendMessage(prefix + "§bPlayer doesn't exist")
                                    return true
                                }
                                val targetSkill = skills[args[2].toInt() + 1]
                                if (targetSkill == null) {
                                    sender.sendMessage(prefix + "§bSkill doesn't exist")
                                    return true
                                }
                                val level = args[3].toInt()
                                if (level < 0 || level > 100) {
                                    sender.sendMessage(prefix + "§bLevel value is invalid")
                                    return true
                                }
                                skill.setPlayerData(targetPlayer.uniqueId, args[2].toInt(), level)
                                sender.sendMessage(prefix + "§bLevel set")
                                return true
                            }
                        }
                    }
                }
            }
            warnWrongCommand(sender)
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
        sender.sendMessage("§3/mi list §7View all CS, Machines, Recipes, Skills")
        sender.sendMessage("§3/mi info [c/m/r] [key] §7View specific data of CS/ Machine/ Recipe")
        sender.sendMessage("§3/mi setinput [recipeKey] §7Set an input for recipe")
        sender.sendMessage("§3/mi setoutput [recipeKey] §7Set an output for recipe")
        sender.sendMessage("§3/mi usemachine [machineKey] §7Use a machine")
        sender.sendMessage("§3/mi setlevel [playerId] [skillId] [level] §7Set a level of player")
        sender.sendMessage("§bVer 1.0 : by Shupro")
        sender.sendMessage("§a***************************")
    }

    fun showList(sender: CommandSender) {
        sender.sendMessage(prefix + "§bChanceSets: ")
        sender.sendMessage(prefix + "§7" + chanceSets.keys)

        sender.sendMessage(prefix + "§bSkills: ")
        val skillStringList = mutableListOf<String>()
        skills.onEach { skillStringList.add(it.name) }
        sender.sendMessage(prefix + "§7" + skillStringList)

        sender.sendMessage(prefix + "§bRecipes: ")
        sender.sendMessage(prefix + "§7" + recipies.keys)

        sender.sendMessage(prefix + "§bMachines: ")
        sender.sendMessage(prefix + "§7" + machines.keys)
    }

    fun warnWrongCommand(sender: CommandSender) {
        sender.sendMessage(prefix + "Wrong Command. /mi help")
    }
}

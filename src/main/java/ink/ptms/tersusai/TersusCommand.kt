package ink.ptms.tersusai

import com.google.common.collect.Maps
import ink.ptms.tersusai.inject.TersusInjector
import ink.ptms.tersusai.rule.CollectTask
import ink.ptms.tersusai.rule.Collector
import org.bukkit.Bukkit
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerQuitEvent
import taboolib.common.platform.command.CommandBody
import taboolib.common.platform.command.CommandHeader
import taboolib.common.platform.command.mainCommand
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.adaptCommandSender
import taboolib.common.platform.function.adaptPlayer
import taboolib.common.platform.function.submitAsync
import taboolib.common5.Baffle
import taboolib.common5.Coerce
import taboolib.expansion.createHelper
import taboolib.module.chat.Components
import taboolib.module.chat.colored
import taboolib.module.nms.getI18nName
import java.util.concurrent.TimeUnit
import kotlin.math.floor

/**
 * TersusCommand
 * ink.ptms.tersusai
 *
 * @author sky
 * @since 2021/7/17 20:44
 */
@CommandHeader("tersusai", aliases = ["ta", "tersus"], permission = "tersusai.access")
object TersusCommand {

    private val baffle = Baffle.of(100, TimeUnit.MILLISECONDS)
    private const val normal = "§7§l[§f§lTersus§7§l] §7"
    private const val error = "§c§l[§4§lTersus§c§l] §c"

    @CommandBody(optional = true)
    val status = subCommand {
        execute<CommandSender> { sender, _, _ ->
            sender.info("§7§m                                   ")
            sender.info("状态: §f" + TersusInjector.getTps().joinToString(separator = ", ") { Coerce.format(it).toString() })
            submitAsync {
                Bukkit.getWorlds().forEach { world ->
                    val livingEntities = world.livingEntities
                    val map: MutableMap<String, Int> = Maps.newTreeMap()
                    val mapAffect: MutableMap<String, Int> = Maps.newHashMap()
                    livingEntities.forEach { entity ->
                        val name = entity.getI18nName(sender as? Player)
                        map[name] = map.getOrDefault(name, 0) + 1
                        if (TersusInjector.isInsentient(entity) && TersusInjector.isInjected(entity)) {
                            mapAffect[name] = mapAffect.getOrDefault(name, 0) + 1
                        }
                    }
                    val entitiesSorted = map.entries.sortedByDescending { it.value }.associateBy ({ it.key }, { it.value })
                    sender.info("世界: &f" + world.name)
                    sender.info("&8- &7区块: &f" + world.loadedChunks.size)
                    Components.empty()
                        .append("§7§l[§f§lTersus§7§l] §8- §7实体: §f" + livingEntities.size + " §a(" + mapAffect.values.sum() + ") ")
                        .append("§8(详细)").hoverText("§7详细信息↓\n" + entitiesSorted.keys.joinToString("\n") { " §f$it §8-> §f" + map[it] + " §a(" + mapAffect.getOrDefault(it, 0) + ")" })
                        .sendTo(adaptCommandSender(sender))
                }
                sender.info("§7§m                                   ")
            }
        }
    }

    @CommandBody(optional = true)
    val collect = subCommand {
        execute<Player> { sender, _, _ ->
            val time1 = System.currentTimeMillis()
            sender.info("§7§m                                   ")
            sender.info("获取有效实体.")
            val affectEntity = Collector.getAffectEntity(sender.world)
            sender.info("创建工作线程.")
            submitAsync {
                val time2 = System.currentTimeMillis()
                val collectTask = CollectTask(affectEntity).collect()
                sender.info("实体分组.")
                var i = 1
                collectTask.groups.forEach { group ->
                    Components.empty()
                        .append("§7§l[§f§lTersus§7§l] §8  ")
                        .append(i++.toString() + " §7-> §f" + group.entities.size + " §8(" + floor(Math.PI * group.range * group.range) + "m^2) (ρ=" + group.density + ")")
                        .hoverText("点击传送至附近")
                        .clickRunCommand("/tp " + group.center.x + " " + group.center.y + " " + group.center.z)
                        .sendTo(adaptPlayer(sender))
                    sender.spawnParticle(Particle.END_ROD, group.maxDistanceCache[0].location, 1000, 0.0, 100.0, 0.0, 0.0)
                    sender.spawnParticle(Particle.END_ROD, group.maxDistanceCache[1].location, 1000, 0.0, 100.0, 0.0, 0.0)
                }
                sender.info("§7耗时 &f${System.currentTimeMillis() - time1}ms &8(async: ${time2 - time1}ms)")
                sender.info("§7§m                                   ")
            }
        }
    }

    @CommandBody(optional = true)
    val reload = subCommand {
        execute<CommandSender> { sender, _, _ ->
            TersusAI.reload()
            sender.info("§7重载完成.")
        }
    }

    @CommandBody
    val main = mainCommand {
        createHelper()
    }

    private fun CommandSender.info(args: String) {
        sendMessage(normal + args.colored())
        if (this is Player && baffle.hasNext(name)) {
            playSound(location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 2f)
        }
    }

    private fun CommandSender.error(args: String) {
        sendMessage(error + args.colored())
        if (this is Player && baffle.hasNext(name)) {
            playSound(location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 2f)
        }
    }

    @SubscribeEvent
    fun onQuit(e: PlayerQuitEvent) {
        baffle.reset(e.player.name)
    }
}
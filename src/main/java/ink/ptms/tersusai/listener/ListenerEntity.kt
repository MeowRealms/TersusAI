package ink.ptms.tersusai.listener

import ink.ptms.tersusai.TersusAI
import ink.ptms.tersusai.inject.TersusInjector
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.LivingEntity
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.inventory.EquipmentSlot
import taboolib.common.platform.Schedule
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.event.SubscribeEvent
import taboolib.module.configuration.ConfigNode

/**
 * @author sky
 * @since 2019-08-23 1:08
 */
object ListenerEntity {

    @ConfigNode("EntitySpawnLimit.range")
    var range = -1

    @ConfigNode("EntitySpawnLimit.count")
    var count = -1

    @SubscribeEvent(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun onEntitySpawn(e: CreatureSpawnEvent) {
        val entity = e.entity
        if (TersusAI.isAffected(entity)) {
            val range = range.toDouble()
            if (range > 0 && count > 0 && entity.getNearbyEntities(range, range, range).count { it.type == entity.type } > count) {
                e.isCancelled = true
            } else {
                TersusInjector.inject(entity)
            }
        }
    }

    @SubscribeEvent
    fun onInteract(e: PlayerInteractEntityEvent) {
        if (e.rightClicked is LivingEntity
                // 管理
                && e.player.isOp
                // 潜行
                && e.player.isSneaking
                // 创造
                && e.player.gameMode == GameMode.CREATIVE
                // 物品
                && e.player.inventory.itemInMainHand.type == Material.BLAZE_ROD
                // 调试
                && TersusAI.conf.getBoolean("Rules.debug-item")
                // 有效实体
                && TersusInjector.isInsentient(e.rightClicked as LivingEntity)
                // 主手点击
                && e.hand == EquipmentSlot.HAND) {
            e.isCancelled = true
            e.player.sendMessage("§7§l[§f§lTersus§7§l] §7AI-Level (Goal): §f" + (TersusInjector.getGoal(e.rightClicked as LivingEntity)?.level ?: "Unknown"))
            e.player.sendMessage("§7§l[§f§lTersus§7§l] §7AI-Level (Target): §f" + (TersusInjector.getTarget(e.rightClicked as LivingEntity)?.level ?: "Unknown"))
            e.player.playSound(e.player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1F, 2F)
        }
    }

    @Schedule(async = true, period = 200)
    fun inject() {
        Bukkit.getWorlds().flatMap { it.livingEntities }
            .filter { TersusAI.isAffected(it) && !TersusInjector.isInjected(it) }
            .forEach { TersusInjector.inject(it) }
    }
}
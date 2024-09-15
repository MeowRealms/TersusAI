package ink.ptms.tersusai.rule

import com.google.common.collect.Maps
import ink.ptms.tersusai.TersusAI
import ink.ptms.tersusai.TersusAI.conf
import ink.ptms.tersusai.inject.TersusInjector.Companion.level
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World
import taboolib.common.platform.Schedule
import taboolib.common5.Baffle

/**
 * @Author sky
 * @Since 2019-08-24 7:57
 */
object Collector {

    private val worldTask: Map<String, CollectTask> = Maps.newConcurrentMap()
    private val filter: MutableMap<Coordinate, Baffle> = Maps.newConcurrentMap()

    @Schedule(async = true, period = 40)
    fun run() {
        Bukkit.getWorlds().forEach { world ->
            val collectTask = CollectTask(getAffectEntity(world)).collect()
            var inhibit = 0
            var global = false
            for (method in TersusAI.methods) {
                if (method.check(collectTask.entities.size, 0.0, RuleAffect.GLOBAL)) {
                    global = true
                    inhibit = method.inhibit
                    collectTask.entities.forEach { entity -> level(entity.livingEntity, method.inhibit) }
                    printLog(method, world.spawnLocation, collectTask.entities.size, 0.0)
                    break
                }
            }
            for (collectGroup in collectTask.groups) {
                for (method in TersusAI.methods) {
                    if (method.check(collectGroup.entities.size, collectGroup.density, RuleAffect.GROUP) && method.inhibit > inhibit) {
                        collectGroup.entities.forEach { entity -> level(entity.livingEntity, method.inhibit) }
                        printLog(method, collectGroup.center, collectGroup.entities.size, collectGroup.density)
                        break
                    }
                }
            }
            if (!global) {
                collectTask.single.forEach { entity ->
                    TersusAI.methods.firstOrNull { rule -> rule.check(0, 0.0, RuleAffect.SINGLE) }?.let { rule ->
                        level(entity.livingEntity, rule.inhibit)
                    }
                }
            }
        }
    }

    @JvmStatic
    val collectRange: Int
        get() = conf.getInt("Rules.collect-range")

    @JvmStatic
    val collectSize: Int
        get() = conf.getInt("Rules.collect-size")

    @JvmStatic
    fun getWorldTask(): Map<String, CollectTask> {
        return worldTask
    }

    fun getAffectEntity(world: World): List<CollectEntity> {
        return world.livingEntities.filter { TersusAI.isAffected(it) }.map { CollectEntity(it) }
    }

    private fun printLog(method: Rule, location: Location, size: Int, i: Double) {
        if (filter.computeIfAbsent(Coordinate(location)) { Baffle.of(conf.getInt("Rules.logger-level", 10)) }.hasNext()) {
            method.log(size, i, location)
        }
    }
}
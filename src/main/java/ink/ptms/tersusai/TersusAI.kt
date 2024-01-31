package ink.ptms.tersusai

import ink.ptms.tersusai.inject.TersusInjector
import ink.ptms.tersusai.rule.Rule
import org.bukkit.entity.LivingEntity
import taboolib.common.platform.Plugin
import taboolib.common.platform.function.info
import taboolib.common.platform.function.warning
import taboolib.common5.Coerce
import taboolib.module.configuration.Config
import taboolib.module.configuration.Configuration

/**
 * @author sky
 * @since 2019-08-23 0:48
 */
object TersusAI : Plugin() {

    @Config
    lateinit var conf: Configuration
        private set

    val methods = mutableListOf<Rule>()

    /**
     * 判断生物是否受本插件效果影响
     * 判断内容：
     * - 世界禁用
     * - 实体类型
     * - 实体名称
     */
    fun isAffected(entity: LivingEntity): Boolean {
        return (TersusInjector.isInsentient(entity)
                && !conf.getStringList("DisableWorlds").contains(entity.world.name)
                && !conf.getStringList("DisableEntities").contains(entity.type.name.lowercase())
                )
    }

    override fun onEnable() {
        conf.onReload { reload() }
        reload()
    }

    fun reload() {
        methods.clear()
        conf.getList("Rules.methods")!!.forEach {
            if (it is Map<*, *>) {
                if (it.containsKey("logger") && it["logger"] is List<*>) {
                    methods.add(Rule(it["condition"].toString(), Coerce.toInteger(it["inhibit"]), it["affect"].toString(), it["logger"] as List<String>))
                } else {
                    methods.add(Rule(it["condition"].toString(), Coerce.toInteger(it["inhibit"]), it["affect"].toString()))
                }
            } else {
                warning("Invalid control rule format: $it")
            }
        }
        info("Loaded " + methods.size + " control rules.")
    }

}

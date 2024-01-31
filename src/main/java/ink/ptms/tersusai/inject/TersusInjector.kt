package ink.ptms.tersusai.inject

import org.bukkit.entity.LivingEntity
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.module.nms.MinecraftVersion
import taboolib.module.nms.nmsProxy

/**
 * @Author sky
 * @Since 2019-08-23 11:00
 */
abstract class TersusInjector {

    abstract fun getGoal0(entity: LivingEntity): TersusSelector?

    abstract fun getTarget0(entity: LivingEntity): TersusSelector?

    abstract fun setGoal0(entity: LivingEntity, selector: TersusSelector)

    abstract fun setTarget0(entity: LivingEntity, selector: TersusSelector)

    abstract fun isInsentient0(entity: LivingEntity): Boolean

    abstract fun getTps(): DoubleArray

    companion object {

        private val injector = nmsProxy<TersusInjector>()
        private lateinit var generator: TersusSelector

        @Awake(LifeCycle.LOAD)
        fun init() {
            generator = when (MinecraftVersion.major) {
                -1 -> error("TersusAI doesn't support the current version \"${MinecraftVersion.minecraftVersion}\".")

                else -> nmsProxy("ink.ptms.tersusai.inject.impl.Injector12000")
            }
        }

        fun level(entity: LivingEntity, level: Int) {
            injector.getGoal0(entity)?.level = level
            injector.getTarget0(entity)?.level = level
        }

        fun inject(entity: LivingEntity) {
            injectGoal(entity)
            injectTarget(entity)
        }

        private fun injectGoal(entity: LivingEntity) {
            try {
                injector.setGoal0(entity, generator.generateGoal(entity))
            } catch (ignored: NullPointerException) {
            } catch (t: Throwable) {
                t.printStackTrace()
            }
        }

        private fun injectTarget(entity: LivingEntity) {
            try {
                injector.setTarget0(entity, generator.generateTarget(entity))
            } catch (ignored: NullPointerException) {
            } catch (t: Throwable) {
                t.printStackTrace()
            }
        }

        fun getGoal(entity: LivingEntity): TersusSelector? {
            return injector.getGoal0(entity)
        }

        fun getTarget(entity: LivingEntity): TersusSelector? {
            return injector.getTarget0(entity)
        }

        fun isInsentient(entity: LivingEntity): Boolean {
            return injector.isInsentient0(entity)
        }

        fun isInjected(entity: LivingEntity): Boolean {
            return injector.getGoal0(entity) != null && injector.getTarget0(entity) != null
        }

        fun getTps(): DoubleArray {
            return injector.getTps()
        }
    }
}
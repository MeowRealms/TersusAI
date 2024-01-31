package ink.ptms.tersusai.inject

import org.bukkit.entity.LivingEntity

/**
 * @author sky
 * @since 2019-08-23 13:58
 */
interface TersusSelector {

    var level: Int

    fun generateGoal(entity: LivingEntity): TersusSelector

    fun generateTarget(entity: LivingEntity): TersusSelector

}

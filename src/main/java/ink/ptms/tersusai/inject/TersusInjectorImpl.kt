package ink.ptms.tersusai.inject

import net.minecraft.world.entity.EntityInsentient
import net.minecraft.world.entity.ai.goal.PathfinderGoalSelector
import org.bukkit.Bukkit
import org.bukkit.craftbukkit.v1_20_R3.CraftServer
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftLivingEntity
import org.bukkit.entity.LivingEntity

/**
 * @author sky
 * @since 2019-08-23 14:31
 */
class TersusInjectorImpl : TersusInjector() {

//    private val goalField = EntityInsentient::class.java
//        .declaredFields
//        .first { it.type == PathfinderGoalSelector::class.java }
//        .apply { isAccessible = true }
//
//    private val targetField = EntityInsentient::class.java
//        .declaredFields
//        .last { it.type == PathfinderGoalSelector::class.java }
//        .apply { isAccessible = true }

    override fun getGoal0(entity: LivingEntity): TersusSelector? {
        val pathfinder = ((entity as CraftLivingEntity).handle as EntityInsentient).bO
        return pathfinder as? TersusSelector
    }

    override fun getTarget0(entity: LivingEntity): TersusSelector? {
        val pathfinder = ((entity as CraftLivingEntity).handle as EntityInsentient).bP
        return pathfinder as? TersusSelector
    }

    override fun setGoal0(entity: LivingEntity, selector: TersusSelector) {
        ((entity as CraftLivingEntity).handle as EntityInsentient).bO = selector as PathfinderGoalSelector
    }

    override fun setTarget0(entity: LivingEntity, selector: TersusSelector) {
        ((entity as CraftLivingEntity).handle as EntityInsentient).bP = selector as PathfinderGoalSelector
    }

    override fun isInsentient0(entity: LivingEntity): Boolean {
        return (entity as CraftLivingEntity).handle is EntityInsentient
    }

//    override fun getLivingEntities(world: World): List<LivingEntity> {
//        return (world as CraftWorld).handle
//            .getProperty<EntityLookup>("entityLookup")!!
//            .a() // invokeMethod<Iterable<Entity>>("a")!!
//            .mapNotNull { entity ->
//                val bukkitEntity = entity?.bukkitEntity // val bukkitEntity = entity.invokeMethod<CraftEntity>("getBukkitEntity")
//                if (bukkitEntity != null && bukkitEntity is LivingEntity && bukkitEntity.isValid()) {
//                    bukkitEntity
//                } else {
//                    null
//                }
//            }
//    }

    override fun getTps(): DoubleArray {
        return (Bukkit.getServer() as CraftServer).tps
    }
}
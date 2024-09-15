package ink.ptms.tersusai.inject.impl;

import ink.ptms.tersusai.inject.TersusSelector;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import org.bukkit.craftbukkit.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import taboolib.common.util.RandomKt;

import java.util.Set;
import java.util.function.Predicate;

/**
 * v1.21
 */
public class Injector12100 extends GoalSelector implements TersusSelector {

    private GoalSelector selector;
    private int level = 0;

    public Injector12100() {
        super();
    }

    public Injector12100(GoalSelector selector) {
        super();
        this.selector = selector;
    }

    @Override
    public void addGoal(int priority, Goal goal) {
        selector.addGoal(priority, goal);
    }

    @Override
    public void removeAllGoals(Predicate<Goal> predicate) {
        selector.removeAllGoals(predicate);
    }

    @Override
    public boolean inactiveTick(int tickRate, boolean inactive) {
        return selector.inactiveTick(tickRate, inactive);
    }

    @Override
    public boolean hasTasks() {
        return selector.hasTasks();
    }

    @Override
    public void removeGoal(Goal goal) {
        selector.removeGoal(goal);
    }

    @Override
    public void tick() {
        if (level == 0 || RandomKt.random(100) >= level) {
            selector.tick();
        }
    }

    @Override
    public void tickRunningGoals(boolean tickAll) {
        if (level == 0 || RandomKt.random(100) >= level) {
            selector.tickRunningGoals(tickAll);
        }
    }

    @NotNull
    @Override
    public Set<WrappedGoal> getAvailableGoals() {
        return selector.getAvailableGoals();
    }

    @Override
    public void disableControlFlag(Goal.Flag control) {
        selector.disableControlFlag(control);
    }

    @Override
    public void enableControlFlag(Goal.Flag control) {
        selector.enableControlFlag(control);
    }

    @Override
    public void setControlFlag(Goal.Flag control, boolean enabled) {
        selector.setControlFlag(control, enabled);
    }

    @Override
    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public int getLevel() {
        return level;
    }

    @NotNull
    @Override
    public TersusSelector generateGoal(@NotNull LivingEntity entity) {
        return new Injector12100(((Mob) ((CraftLivingEntity) entity).getHandle()).goalSelector);
    }

    @NotNull
    @Override
    public TersusSelector generateTarget(@NotNull LivingEntity entity) {
        return new Injector12100(((Mob) ((CraftLivingEntity) entity).getHandle()).targetSelector);
    }
}

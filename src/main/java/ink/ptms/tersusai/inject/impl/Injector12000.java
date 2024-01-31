package ink.ptms.tersusai.inject.impl;

import ink.ptms.tersusai.inject.TersusSelector;
import net.minecraft.world.entity.EntityInsentient;
import net.minecraft.world.entity.ai.goal.PathfinderGoal;
import net.minecraft.world.entity.ai.goal.PathfinderGoalSelector;
import net.minecraft.world.entity.ai.goal.PathfinderGoalWrapped;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import taboolib.common.util.RandomKt;

import java.util.Set;
import java.util.stream.Stream;

/**
 * v1.20
 */
public class Injector12000 extends PathfinderGoalSelector implements TersusSelector {

    private PathfinderGoalSelector selector;
    private int level = 0;

    public Injector12000() {
        super();
    }

    public Injector12000(PathfinderGoalSelector selector) {
        super();
        this.selector = selector;
    }

    @Override
    public void a(int var0, PathfinderGoal var1) {
        selector.a(var0, var1);
    }

    @Override
    public boolean hasTasks() {
        return selector.hasTasks();
    }

    @Override
    public void a(PathfinderGoal var0) {
        selector.a(var0);
    }

    @Override
    public void a() {
        if (level == 0 || RandomKt.random(100) >= level) {
            selector.a();
        }
    }

    @Override
    public void a(boolean tickAll) {
        if (level == 0 || RandomKt.random(100) >= level) {
            selector.a(tickAll);
        }
    }

    @NotNull
    @Override
    public Set<PathfinderGoalWrapped> b() {
        return selector.b();
    }

    @NotNull
    @Override
    public Stream<PathfinderGoalWrapped> c() {
        return selector.c();
    }

    @Override
    public void a(int var0) {
        selector.a(var0);
    }

    @Override
    public void a(PathfinderGoal.Type var0) {
        selector.a(var0);
    }

    @Override
    public void b(PathfinderGoal.Type var0) {
        selector.b(var0);
    }

    @Override
    public void a(PathfinderGoal.Type var0, boolean var1) {
        selector.a(var0, var1);
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
        return new Injector12000(((EntityInsentient) ((CraftLivingEntity) entity).getHandle()).bO);
    }

    @NotNull
    @Override
    public TersusSelector generateTarget(@NotNull LivingEntity entity) {
        return new Injector12000(((EntityInsentient) ((CraftLivingEntity) entity).getHandle()).bP);
    }
}

package com.bobmowzie.mowziesmobs.server.ai;

import com.bobmowzie.mowziesmobs.server.entity.grottol.EntityGrottol;
import com.google.common.base.Predicate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.vehicle.MinecartEntity;

import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;

public final class EntityAIGrottolFindMinecart extends Goal {
    private final EntityGrottol grottol;

    private final Comparator<Entity> sorter;

    private final Predicate<MinecartEntity> predicate;

    private MinecartEntity minecart;

    private int time;

    public EntityAIGrottolFindMinecart(EntityGrottol grottol) {
        this.grottol = grottol;
        this.sorter = Comparator.comparing(grottol::squaredDistanceTo);
        this.predicate = minecart -> minecart != null && minecart.isAlive() && !minecart.hasPassengers() && EntityGrottol.isBlockRail(minecart.getWorld().getBlockState(minecart.getBlockPos()).getBlock());
        this.setControls(EnumSet.of(Control.LOOK, Control.MOVE));
    }

    @Override
    public boolean canStart() {
        if (this.grottol.fleeTime <= 1) return false;
        List<MinecartEntity> minecarts = this.grottol.getWorld().getEntitiesByClass(MinecartEntity.class, this.grottol.getBoundingBox().expand(8.0D, 4.0D, 8.0D), this.predicate);
        minecarts.sort(this.sorter);
        if (minecarts.isEmpty()) {
            return false;
        }
        this.minecart = minecarts.get(0);
        return true;
    }

    @Override
    public boolean shouldContinue() {
        return this.predicate.test(this.minecart) && this.time < 1200 && !this.grottol.isInMinecart();
    }

    @Override
    public void start() {
        this.time = 0;
        this.grottol.getNavigation().startMovingTo(this.minecart, 0.5D);
    }

    @Override
    public void stop() {
        this.grottol.getNavigation().stop();
    }

    @Override
    public void tick() {
        if (this.grottol.squaredDistanceTo(this.minecart) > 1.45D * 1.45D) {
            this.grottol.getLookControl().lookAt(this.minecart, 10.0F, this.grottol.getMaxLookPitchChange());
            if (++this.time % 40 == 0) {
                this.grottol.getNavigation().startMovingTo(this.minecart, 0.5D);
            }
        } else {
            this.grottol.startRiding(this.minecart, true);
            if (this.minecart.getDamageWobbleTicks() == 0) {
                this.minecart.setDamageWobbleSide(-this.minecart.getDamageWobbleSide());
                this.minecart.setDamageWobbleTicks(10);
                this.minecart.setDamageWobbleStrength(50.0F);
            }
        }
    }
}

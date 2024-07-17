package com.bobmowzie.mowziesmobs.server.entity.frostmaw;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;

/**
 * Created by BobMowzie on 7/20/2017.
 */
public class EntityFrozenController extends Entity {
    public EntityFrozenController(EntityType<? extends EntityFrozenController> type, World world) {
        super(type, world);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.getWorld().isClient && this.age >= 70 && !this.hasPassengers()) this.discard();
//        List<Entity> passengers = getPassengers();
//        for (Entity passenger : passengers) {
//            if (passenger instanceof LivingEntity) {
//                LivingEntity livingEntity = (LivingEntity)passenger;
//                if (!livingEntity.isPotionActive(PotionHandler.FROZEN)) discard() ;
//            }
//        }
    }

    @Override
    protected void initDataTracker() {
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound compound) {
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound compound) {
    }

    @Override
    public double getMountedHeightOffset() {
        return 0;
    }

    @Override
    public void updatePassengerPosition(Entity passenger, PositionUpdater moveFunction) {
        if (this.hasPassenger(passenger)) {
            if (passenger instanceof PlayerEntity) passenger.setPosition(this.getX(), this.getY(), this.getZ());
            else
                passenger.updatePositionAndAngles(this.getX(), this.getY(), this.getZ(), this.getYaw(), this.getPitch());
        }
    }
}

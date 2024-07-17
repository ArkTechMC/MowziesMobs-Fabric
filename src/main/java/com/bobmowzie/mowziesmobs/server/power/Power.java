package com.bobmowzie.mowziesmobs.server.power;

import com.bobmowzie.mowziesmobs.server.capability.PlayerCapability;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

import java.util.List;

public abstract class Power {

    private final PlayerCapability.PlayerCapabilityImp capability;

    public Power(PlayerCapability.PlayerCapabilityImp capability) {
        this.capability = capability;
    }

    public void tick(TickEvent.PlayerTickEvent event) {

    }

    public void onRightClickEmpty(PlayerInteractEvent.RightClickEmpty event) {

    }

    public void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {

    }

    public void onRightClickWithItem(PlayerInteractEvent.RightClickItem event) {

    }

    public void onRightClickEntity(PlayerInteractEvent.EntityInteract event) {

    }

    public void onLeftClickEmpty(PlayerInteractEvent.LeftClickEmpty event) {

    }

    public void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {

    }

    public void onLeftClickEntity(AttackEntityEvent event) {

    }

    public void onTakeDamage(LivingHurtEvent event) {

    }

    public void onJump(LivingEvent.LivingJumpEvent event) {

    }

    public void onRightMouseDown(PlayerEntity player) {

    }

    public void onLeftMouseDown(PlayerEntity player) {

    }

    public void onRightMouseUp(PlayerEntity player) {

    }

    public void onLeftMouseUp(PlayerEntity player) {

    }

    public void onSneakDown(PlayerEntity player) {

    }

    public void onSneakUp(PlayerEntity player) {

    }

    public boolean canUse(PlayerEntity player) {
        return true;
    }

    public PlayerCapability.PlayerCapabilityImp getProperties() {
        return this.capability;
    }

    public List<LivingEntity> getEntityLivingBaseNearby(LivingEntity player, double distanceX, double distanceY, double distanceZ, double radius) {
        return this.getEntitiesNearby(player, LivingEntity.class, distanceX, distanceY, distanceZ, radius);
    }

    public <T extends Entity> List<T> getEntitiesNearby(LivingEntity player, Class<T> entityClass, double r) {
        return player.getWorld().getEntitiesByClass(entityClass, player.getBoundingBox().expand(r, r, r), e -> e != player && player.distanceTo(e) <= r);
    }

    public <T extends Entity> List<T> getEntitiesNearby(LivingEntity player, Class<T> entityClass, double dX, double dY, double dZ, double r) {
        return player.getWorld().getEntitiesByClass(entityClass, player.getBoundingBox().expand(dX, dY, dZ), e -> e != player && player.distanceTo(e) <= r);
    }
}

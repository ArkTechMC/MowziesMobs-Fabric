package com.bobmowzie.mowziesmobs.server.power;

import com.bobmowzie.mowziesmobs.server.capability.PlayerCapability;
import io.github.fabricators_of_create.porting_lib.entity.events.LivingEntityEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class Power {

    private final PlayerCapability.PlayerCapabilityImp capability;

    public Power(PlayerCapability.PlayerCapabilityImp capability) {
        this.capability = capability;
    }

    public void tick(PlayerEntity player) {

    }

    public void onRightClickEmpty(PlayerInteractEvent.RightClickEmpty event) {

    }

    public void onRightClickBlock(PlayerEntity player, World world, Hand hand, BlockHitResult hitResult) {

    }

    public void onRightClickWithItem(PlayerEntity player, World world, Hand hand) {

    }

    public void onRightClickEntity(PlayerEntity player, World world, Hand hand, Entity entity, @Nullable EntityHitResult hitResult) {

    }

    public void onLeftClickEmpty(PlayerInteractEvent.LeftClickEmpty event) {

    }

    public void onLeftClickBlock(PlayerEntity player, World world, Hand hand, BlockPos pos, Direction direction) {

    }

    public void onLeftClickEntity(PlayerEntity player, World world, Hand hand, Entity entity, @Nullable EntityHitResult hitResult) {

    }

    public void onTakeDamage(LivingEntity livingEntity, DamageSource source, float damage) {

    }

    public void onJump(LivingEntityEvents.LivingJumpEvent event) {

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

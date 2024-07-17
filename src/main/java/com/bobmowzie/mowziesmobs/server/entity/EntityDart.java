package com.bobmowzie.mowziesmobs.server.entity;

import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.umvuthana.EntityUmvuthana;
import com.bobmowzie.mowziesmobs.server.entity.umvuthana.EntityUmvuthanaFollowerToPlayer;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

public class EntityDart extends ArrowEntity {
    public EntityDart(EntityType<? extends EntityDart> type, World world) {
        super(type, world);
    }

    public EntityDart(EntityType<? extends EntityDart> type, World worldIn, double x, double y, double z) {
        this(type, worldIn);
        this.setPosition(x, y, z);
        this.setDamage(ConfigHandler.COMMON.TOOLS_AND_ABILITIES.BLOW_GUN.attackDamage);
    }

    public EntityDart(EntityType<? extends EntityDart> type, World world, LivingEntity shooter) {
        this(type, world, shooter.getX(), shooter.getY() + (double) shooter.getStandingEyeHeight() - (double) 0.1F, shooter.getZ());
        this.setOwner(shooter);
        if (shooter instanceof PlayerEntity) {
            this.pickupType = PickupPermission.ALLOWED;
        }
    }

    @Override
    protected ItemStack asItemStack() {
        return new ItemStack(ItemHandler.DART);
    }

    @Override
    protected void onHit(LivingEntity living) {
        super.onHit(living);
        if (this.getOwner() instanceof PlayerEntity)
            living.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, ConfigHandler.COMMON.TOOLS_AND_ABILITIES.BLOW_GUN.poisonDuration, 3, false, true));
        else living.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 30, 1, false, true));
        living.setStuckArrowCount(living.getStuckArrowCount() - 1);
    }

    @Override
    protected void onEntityHit(EntityHitResult raytraceResultIn) {
        if (raytraceResultIn.getType() == HitResult.Type.ENTITY) {
            Entity hit = raytraceResultIn.getEntity();
            Entity shooter = this.getOwner();
            if (hit instanceof LivingEntity living) {
                if (
                        this.getWorld().isClient ||
                                (shooter == hit) ||
                                (shooter instanceof EntityUmvuthana && living instanceof EntityUmvuthana && ((EntityUmvuthana) shooter).isUmvuthiDevoted() == ((EntityUmvuthana) living).isUmvuthiDevoted()) ||
                                (shooter instanceof EntityUmvuthanaFollowerToPlayer && living == ((EntityUmvuthanaFollowerToPlayer) shooter).getLeader())
                )
                    return;
            }
        }
        super.onEntityHit(raytraceResultIn);
    }
}
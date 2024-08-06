package com.bobmowzie.mowziesmobs.server.entity.effects;

import com.bobmowzie.mowziesmobs.server.capability.PlayerCapability;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.ParrotEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by BobMowzie on 7/15/2017.
 */
public class EntityAxeAttack extends EntityMagicEffect {
    private static final TrackedData<Boolean> VERTICAL = DataTracker.registerData(EntityAxeAttack.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<ItemStack> AXE_STACK = DataTracker.registerData(EntityAxeAttack.class, TrackedDataHandlerRegistry.ITEM_STACK);

    public static int SWING_DURATION_HOR = 24;
    public static int SWING_DURATION_VER = 30;
    private float quakeAngle = 0;
    private Box quakeBB = new Box(0, 0, 0, 1, 1, 1);

    public EntityAxeAttack(EntityType<? extends EntityAxeAttack> type, World world) {
        super(type, world);
    }

    public EntityAxeAttack(EntityType<? extends EntityAxeAttack> type, World world, LivingEntity caster, boolean vertical) {
        super(type, world, caster);
        this.setVertical(vertical);
        this.setAxeStack(caster.getMainHandStack());
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.getDataTracker().startTracking(VERTICAL, false);
        this.getDataTracker().startTracking(AXE_STACK, ItemHandler.WROUGHT_AXE.getDefaultStack());
    }

    @Override
    public void tick() {
        super.tick();
        if (this.caster != null) {
            if (!this.caster.isAlive()) this.discard();
            this.updatePositionAndAngles(this.caster.getX(), this.caster.getY() + this.caster.getStandingEyeHeight(), this.caster.getZ(), this.caster.getYaw(), this.caster.getPitch());
        }
        if (!this.getWorld().isClient && this.age == 7) this.playSound(MMSounds.ENTITY_WROUGHT_WHOOSH, 0.7F, 1.1f);
        if (!this.getWorld().isClient && this.caster != null) {
            if (!this.getVertical() && this.age == SWING_DURATION_HOR / 2 - 1)
                this.dealDamage((float) (7 * ConfigHandler.COMMON.TOOLS_AND_ABILITIES.AXE_OF_A_THOUSAND_METALS.toolConfig.attackDamage / 9F), 4f, 160, 1.2f);
            else if (this.getVertical() && this.age == SWING_DURATION_VER / 2 - 1) {
                this.dealDamage((float) ConfigHandler.COMMON.TOOLS_AND_ABILITIES.AXE_OF_A_THOUSAND_METALS.toolConfig.attackDamage, 4.5f, 40, 0.8f);
                this.quakeAngle = this.getYaw();
                this.quakeBB = this.getBoundingBox().offset(0, -this.caster.getStandingEyeHeight(), 0);
                this.playSound(MMSounds.ENTITY_WROUGHT_AXE_LAND, 0.3F, 0.5F);
                this.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 2, 0.9F + this.random.nextFloat() * 0.1F);
            } else if (this.getVertical() && this.age == SWING_DURATION_VER / 2 + 1) {
                EntityCameraShake.cameraShake(this.getWorld(), this.getPos(), 10, 0.05f, 0, 10);
            }
        }

        if (this.getVertical() && this.caster != null) {
            if (this.age >= SWING_DURATION_VER / 2) {
                int maxDistance = 16;
                double perpFacing = this.quakeAngle * (Math.PI / 180);
                double facingAngle = perpFacing + Math.PI / 2;
                int hitY = MathHelper.floor(this.quakeBB.minY - 0.5);
                int distance = this.age - 15;
                double spread = Math.PI * 0.35F;
                int arcLen = MathHelper.ceil(distance * spread);
                double minY = this.quakeBB.minY;
                double maxY = this.quakeBB.maxY;
                for (int i = 0; i < arcLen; i++) {
                    double theta = (i / (arcLen - 1.0) - 0.5) * spread + facingAngle;
                    double vx = Math.cos(theta);
                    double vz = Math.sin(theta);
                    double px = this.getX() + vx * distance;
                    double pz = this.getZ() + vz * distance;
                    float factor = 1 - distance / (float) maxDistance;
                    Box selection = new Box(px - 1.5, minY, pz - 1.5, px + 1.5, maxY, pz + 1.5);
                    List<Entity> hit = this.getWorld().getNonSpectatingEntities(Entity.class, selection);
                    for (Entity entity : hit) {
                        if (entity.isOnGround()) {
                            if (entity == this || entity instanceof FallingBlockEntity || entity == this.caster) {
                                continue;
                            }
                            float applyKnockbackResistance = 0;
                            boolean hitEntity = false;
                            if (!this.raytraceCheckEntity(entity)) continue;

                            if (this.caster instanceof PlayerEntity)
                                hitEntity = entity.damage(this.getDamageSources().playerAttack((PlayerEntity) this.caster), (float) ((factor * 5 + 1) * (ConfigHandler.COMMON.TOOLS_AND_ABILITIES.AXE_OF_A_THOUSAND_METALS.toolConfig.attackDamage / 9.0f)));
                            else
                                hitEntity = entity.damage(this.getDamageSources().mobAttack(this.caster), (float) ((factor * 5 + 1) * (ConfigHandler.COMMON.TOOLS_AND_ABILITIES.AXE_OF_A_THOUSAND_METALS.toolConfig.attackDamage / 9.0f)));
                            if (entity instanceof LivingEntity) {
                                applyKnockbackResistance = (float) ((LivingEntity) entity).getAttributeInstance(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE).getValue();
                            }
                            if (hitEntity) {
                                double magnitude = -4;
                                double x = vx * (1 - factor) * magnitude * (1 - applyKnockbackResistance);
                                double y = 0;
                                if (entity.isOnGround()) {
                                    y += 0.15 * (1 - applyKnockbackResistance);
                                }
                                double z = vz * (1 - factor) * magnitude * (1 - applyKnockbackResistance);
                                entity.setVelocity(entity.getVelocity().add(x, y, z));
                                if (entity instanceof ServerPlayerEntity) {
                                    ((ServerPlayerEntity) entity).networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(entity));
                                }
                            }
                        }
                    }
                    if (this.getWorld().random.nextBoolean()) {
                        int hitX = MathHelper.floor(px);
                        int hitZ = MathHelper.floor(pz);
                        BlockPos pos = new BlockPos(hitX, hitY, hitZ);
                        BlockPos abovePos = new BlockPos(pos).up();
                        BlockState block = this.getWorld().getBlockState(pos);
                        BlockState blockAbove = this.getWorld().getBlockState(abovePos);
                        if (!block.isAir() && block.isSolidBlock(this.getWorld(), pos) && !block.hasBlockEntity() && !blockAbove.blocksMovement()) {
                            EntityFallingBlock fallingBlock = new EntityFallingBlock(EntityHandler.FALLING_BLOCK, this.getWorld(), block, 0.3f);
                            fallingBlock.setPosition(hitX + 0.5, hitY + 1, hitZ + 0.5);
                            this.getWorld().spawnEntity(fallingBlock);
                        }
                    }
                }
            }
        }
        if (this.age > SWING_DURATION_HOR) this.discard();
    }

    private void dealDamage(float damage, float range, float arc, float applyKnockback) {
        boolean hit = false;
        List<Entity> entitiesHit = this.getEntitiesNearby(range, 2, range, range);
        for (Entity entityHit : entitiesHit) {
            float entityHitAngle = (float) ((Math.atan2(entityHit.getZ() - this.getZ(), entityHit.getX() - this.getX()) * (180 / Math.PI) - 90) % 360);
            float entityAttackingAngle = this.getYaw() % 360;
            if (entityHitAngle < 0) {
                entityHitAngle += 360;
            }
            if (entityAttackingAngle < 0) {
                entityAttackingAngle += 360;
            }
            float entityRelativeAngle = entityHitAngle - entityAttackingAngle;
            float entityHitDistance = (float) Math.sqrt((entityHit.getZ() - this.getZ()) * (entityHit.getZ() - this.getZ()) + (entityHit.getX() - this.getX()) * (entityHit.getX() - this.getX())) - entityHit.getWidth() / 2f;
            if (entityHit != this.caster && (!(entityHit instanceof ParrotEntity) || entityHit.getVehicle() != this.caster) && entityHitDistance <= range && entityRelativeAngle <= arc / 2 && entityRelativeAngle >= -arc / 2 || entityRelativeAngle >= 360 - arc / 2 || entityRelativeAngle <= -360 + arc / 2) {
                // Do raycast check to prevent damaging through walls
                if (!this.raytraceCheckEntity(entityHit)) continue;

                if (this.caster instanceof PlayerEntity player) {
                    PlayerCapability.IPlayerCapability playerCapability = PlayerCapability.get(player);
                    if (playerCapability != null) {
                        playerCapability.setAxeCanAttack(true);
                        this.attackTargetEntityWithCurrentItem(entityHit, player, damage / ItemHandler.WROUGHT_AXE.getAttackDamage(), applyKnockback);
                        playerCapability.setAxeCanAttack(false);
                    }
                } else {
                    entityHit.damage(this.getDamageSources().mobAttack(this.caster), damage);
                    entityHit.setVelocity(entityHit.getVelocity().x * applyKnockback, entityHit.getVelocity().y, entityHit.getVelocity().z * applyKnockback);
                }
                hit = true;
            }
        }
        if (hit) {
            this.playSound(MMSounds.ENTITY_WROUGHT_AXE_HIT, 0.3F, 0.5F);
        }
    }

    public boolean getVertical() {
        return this.getDataTracker().get(VERTICAL);
    }

    public void setVertical(boolean vertical) {
        this.getDataTracker().set(VERTICAL, vertical);
    }

    private List<Entity> getEntitiesNearby(double distanceX, double distanceY, double distanceZ, double radius) {
        List<Entity> list = this.getWorld().getOtherEntities(this, this.getBoundingBox().expand(distanceX, distanceY, distanceZ));
        ArrayList<Entity> nearEntities = list.stream().filter(entityNeighbor -> entityNeighbor != null && this.distanceTo(entityNeighbor) <= radius + entityNeighbor.getWidth() / 2f).collect(Collectors.toCollection(ArrayList::new));
        return nearEntities;
    }

    public LivingEntity getCaster() {
        return this.caster;
    }

    public ItemStack getAxeStack() {
        return this.getDataTracker().get(AXE_STACK);
    }

    public void setAxeStack(ItemStack axeStack) {
        this.getDataTracker().set(AXE_STACK, axeStack);
    }

    /**
     * Copied from player entity, with modification
     */
    public void attackTargetEntityWithCurrentItem(Entity targetEntity, PlayerEntity player, float damageMult, float knockbackMult) {
        ItemStack oldStack = player.getMainHandStack();
        ItemStack newStack = this.getAxeStack();
        player.setStackInHand(Hand.MAIN_HAND, newStack);
        player.getAttributes().addTemporaryModifiers(newStack.getAttributeModifiers(EquipmentSlot.MAINHAND));

        if (targetEntity.isAttackable()) {
            if (!targetEntity.handleAttack(player)) {
                float f = (float) player.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE) * damageMult;
                float f1;
                if (targetEntity instanceof LivingEntity) {
                    f1 = EnchantmentHelper.getAttackDamage(player.getMainHandStack(), ((LivingEntity) targetEntity).getGroup());
                } else {
                    f1 = EnchantmentHelper.getAttackDamage(player.getMainHandStack(), EntityGroup.DEFAULT);
                }

                float f2 = 1.0f;
                f = f * (0.2F + f2 * f2 * 0.8F);
                f1 = f1 * f2;
                if (f > 0.0F || f1 > 0.0F) {
                    boolean flag = f2 > 0.9F;
                    boolean flag1 = false;
                    int i = 0;
                    i = i + EnchantmentHelper.getKnockback(player);
                    if (player.isSprinting() && flag) {
                        player.getWorld().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_PLAYER_ATTACK_KNOCKBACK, player.getSoundCategory(), 1.0F, 1.0F);
                        ++i;
                        flag1 = true;
                    }

                    f = f + f1;
                    boolean flag3 = false;

                    float f4 = 0.0F;
                    boolean flag4 = false;
                    int j = EnchantmentHelper.getFireAspect(player);
                    if (targetEntity instanceof LivingEntity) {
                        f4 = ((LivingEntity) targetEntity).getHealth();
                        if (j > 0 && !targetEntity.isOnFire()) {
                            flag4 = true;
                            targetEntity.setOnFireFor(1);
                        }
                    }

                    Vec3d vector3d = targetEntity.getVelocity();
                    boolean flag5 = targetEntity.damage(this.getDamageSources().playerAttack(player), f);
                    if (flag5) {
                        if (i > 0) {
                            if (targetEntity instanceof LivingEntity) {
                                ((LivingEntity) targetEntity).takeKnockback((float) i * 0.5F * knockbackMult, MathHelper.sin(player.getYaw() * ((float) Math.PI / 180F)), -MathHelper.cos(player.getYaw() * ((float) Math.PI / 180F)));
                            } else {
                                targetEntity.addVelocity(-MathHelper.sin(player.getYaw() * ((float) Math.PI / 180F)) * (float) i * 0.5F * knockbackMult, 0.1D, MathHelper.cos(player.getYaw() * ((float) Math.PI / 180F)) * (float) i * 0.5F * knockbackMult);
                            }

                            player.setVelocity(player.getVelocity().multiply(0.6D, 1.0D, 0.6D));
                            player.setSprinting(false);
                        }

                        if (targetEntity instanceof ServerPlayerEntity && targetEntity.velocityModified) {
                            ((ServerPlayerEntity) targetEntity).networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(targetEntity));
                            targetEntity.velocityModified = false;
                            targetEntity.setVelocity(vector3d);
                        }

                        if (flag) {
                            player.getWorld().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_PLAYER_ATTACK_STRONG, player.getSoundCategory(), 1.0F, 1.0F);
                        } else {
                            player.getWorld().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_PLAYER_ATTACK_WEAK, player.getSoundCategory(), 1.0F, 1.0F);
                        }

                        if (f1 > 0.0F) {
                            player.addEnchantedHitParticles(targetEntity);
                        }

                        player.onAttacking(targetEntity);
                        if (targetEntity instanceof LivingEntity) {
                            EnchantmentHelper.onUserDamaged((LivingEntity) targetEntity, player);
                        }

                        EnchantmentHelper.onTargetDamaged(player, targetEntity);
                        ItemStack itemstack1 = player.getMainHandStack();
                        Entity entity = targetEntity;
//                        if (targetEntity instanceof net.minecraftforge.entity.PartEntity) {
//                            entity = ((net.minecraftforge.entity.PartEntity<?>) targetEntity).getParent();
//                        }

                        if (!player.getWorld().isClient && !itemstack1.isEmpty() && entity instanceof LivingEntity) {
                            ItemStack copy = itemstack1.copy();
                            itemstack1.postHit((LivingEntity) entity, player);
                            if (itemstack1.isEmpty()) {
                                player.setStackInHand(Hand.MAIN_HAND, ItemStack.EMPTY);
                            }
                        }

                        if (targetEntity instanceof LivingEntity) {
                            float f5 = f4 - ((LivingEntity) targetEntity).getHealth();
                            player.increaseStat(Stats.DAMAGE_DEALT, Math.round(f5 * 10.0F));
                            if (j > 0) {
                                targetEntity.setOnFireFor(j * 4);
                            }

                            if (player.getWorld() instanceof ServerWorld && f5 > 2.0F) {
                                int k = (int) ((double) f5 * 0.5D);
                                ((ServerWorld) player.getWorld()).spawnParticles(ParticleTypes.DAMAGE_INDICATOR, targetEntity.getX(), targetEntity.getBodyY(0.5D), targetEntity.getZ(), k, 0.1D, 0.0D, 0.1D, 0.2D);
                            }
                        }

                        player.addExhaustion(0.1F);
                    } else {
                        player.getWorld().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_PLAYER_ATTACK_NODAMAGE, player.getSoundCategory(), 1.0F, 1.0F);
                        if (flag4) {
                            targetEntity.extinguish();
                        }
                    }
                }

            }
        }
        player.setStackInHand(Hand.MAIN_HAND, oldStack);
        player.getAttributes().addTemporaryModifiers(oldStack.getAttributeModifiers(EquipmentSlot.MAINHAND));
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound compound) {
        super.readCustomDataFromNbt(compound);
        this.setAxeStack(ItemStack.fromNbt(compound.getCompound("axe_stack")));
        this.setVertical(compound.getBoolean("vertical"));
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound compound) {
        super.writeCustomDataToNbt(compound);
        compound.put("axe_stack", this.getAxeStack().writeNbt(new NbtCompound()));
        compound.putBoolean("vertical", this.getVertical());
    }
}

package com.bobmowzie.mowziesmobs.server.ai.animation;

import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntityCameraShake;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntityFallingBlock;
import com.bobmowzie.mowziesmobs.server.entity.wroughtnaut.EntityWroughtnaut;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import com.iafenvoy.uranus.animation.Animation;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;

import java.util.List;

public class AnimationFWNStompAttackAI extends SimpleAnimationAI<EntityWroughtnaut> {
    public AnimationFWNStompAttackAI(EntityWroughtnaut entity, Animation animation) {
        super(entity, animation, true);
    }

    @Override
    public void tick() {
        this.entity.setVelocity(0, this.entity.getVelocity().y, 0);
        double perpFacing = this.entity.bodyYaw * (Math.PI / 180);
        double facingAngle = perpFacing + Math.PI / 2;
        int hitY = MathHelper.floor(this.entity.getBoundingBox().minY - 0.5);
        int tick = this.entity.getAnimationTick();
        final int maxDistance = 6;
        ServerWorld world = (ServerWorld) this.entity.getWorld();
        if (tick == 6) {
            this.entity.playSound(MMSounds.ENTITY_WROUGHT_SHOUT_2, 1, 1);
        } else if (tick > 9 && tick < 17) {
            if (tick == 10) {
                this.entity.playSound(MMSounds.ENTITY_WROUGHT_STEP, 1.2F, 0.5F + this.entity.getRandom().nextFloat() * 0.1F);
            } else if (tick == 12) {
                this.entity.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 2, 1F + this.entity.getRandom().nextFloat() * 0.1F);
                EntityCameraShake.cameraShake(this.entity.getWorld(), this.entity.getPos(), 25, 0.1f, 0, 20);
            }
            if (tick % 2 == 0) {
                int distance = tick / 2 - 2;
                double spread = Math.PI * 2;
                int arcLen = MathHelper.ceil(distance * spread);
                double minY = this.entity.getBoundingBox().minY;
                double maxY = this.entity.getBoundingBox().maxY;
                for (int i = 0; i < arcLen; i++) {
                    double theta = (i / (arcLen - 1.0) - 0.5) * spread + facingAngle;
                    double vx = Math.cos(theta);
                    double vz = Math.sin(theta);
                    double px = this.entity.getX() + vx * distance;
                    double pz = this.entity.getZ() + vz * distance;
                    float factor = 1 - distance / (float) maxDistance;
                    Box selection = new Box(px - 1.5, minY, pz - 1.5, px + 1.5, maxY, pz + 1.5);
                    List<Entity> hit = world.getNonSpectatingEntities(Entity.class, selection);
                    for (Entity entity : hit) {
                        if (entity.isOnGround()) {
                            if (entity == this.entity || entity instanceof EntityFallingBlock) {
                                continue;
                            }
                            float applyKnockbackResistance = 0;
                            if (entity instanceof LivingEntity) {
                                entity.damage(entity.getDamageSources().mobAttack(this.entity), (factor * 5 + 1) * ConfigHandler.COMMON.MOBS.FERROUS_WROUGHTNAUT.combatConfig.attackMultiplier);
                                applyKnockbackResistance = (float) ((LivingEntity) entity).getAttributeInstance(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE).getValue();
                            }
                            double magnitude = world.random.nextDouble() * 0.15 + 0.1;
                            float x = 0, y = 0, z = 0;
                            x += vx * factor * magnitude * (1 - applyKnockbackResistance);
                            y += 0.1 * (1 - applyKnockbackResistance) + factor * 0.15 * (1 - applyKnockbackResistance);
                            z += vz * factor * magnitude * (1 - applyKnockbackResistance);
                            entity.setVelocity(entity.getVelocity().add(x, y, z));
                            if (entity instanceof ServerPlayerEntity) {
                                ((ServerPlayerEntity) entity).networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(entity));
                            }
                        }
                    }
                    if (world.random.nextBoolean()) {
                        int hitX = MathHelper.floor(px);
                        int hitZ = MathHelper.floor(pz);
                        BlockPos pos = new BlockPos(hitX, hitY, hitZ);
                        BlockPos abovePos = new BlockPos(pos).up();
                        BlockState block = world.getBlockState(pos);
                        BlockState blockAbove = world.getBlockState(abovePos);
                        if (!block.isAir() && block.isSolidBlock(world, pos) && !block.hasBlockEntity() && !blockAbove.blocksMovement()) {
                            EntityFallingBlock fallingBlock = new EntityFallingBlock(EntityHandler.FALLING_BLOCK, world, block, (float) (0.4 + factor * 0.2));
                            fallingBlock.setPosition(hitX + 0.5, hitY + 1, hitZ + 0.5);
                            world.spawnEntity(fallingBlock);
                        }
                    }
                }
            }
        }
    }
}

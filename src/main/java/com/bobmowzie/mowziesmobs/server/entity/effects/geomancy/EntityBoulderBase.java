package com.bobmowzie.mowziesmobs.server.entity.effects.geomancy;

import com.bobmowzie.mowziesmobs.client.particle.ParticleHandler;
import com.bobmowzie.mowziesmobs.client.particle.util.AdvancedParticleBase;
import com.bobmowzie.mowziesmobs.client.particle.util.ParticleComponent;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntityCameraShake;
import com.bobmowzie.mowziesmobs.server.entity.sculptor.EntitySculptor;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;

/**
 * Created by BobMowzie on 4/14/2017.
 */
public class EntityBoulderBase extends EntityGeomancyBase {
    public static final HashMap<GeomancyTier, EntityDimensions> SIZE_MAP = new HashMap<>();
    private static final byte ACTIVATE_ID = 67;

    static {
        SIZE_MAP.put(GeomancyTier.NONE, EntityDimensions.changing(1, 1));
        SIZE_MAP.put(GeomancyTier.SMALL, EntityDimensions.changing(1, 1));
        SIZE_MAP.put(GeomancyTier.MEDIUM, EntityDimensions.changing(2, 1.5f));
        SIZE_MAP.put(GeomancyTier.LARGE, EntityDimensions.changing(3, 2.5f));
        SIZE_MAP.put(GeomancyTier.HUGE, EntityDimensions.changing(4, 3.5f));
    }

    public BlockState storedBlock;
    public float animationOffset = 0;
    public GeomancyTier boulderSize = GeomancyTier.SMALL;
    public int risingTick = 0;
    public boolean active = false;
    protected int finishedRisingTick = 4;

    public EntityBoulderBase(EntityType<? extends EntityBoulderBase> type, World world) {
        super(type, world);
        this.finishedRisingTick = 4;
        this.animationOffset = this.random.nextFloat() * 8;
        this.setTier(GeomancyTier.SMALL);
        this.setBoundingBox(this.calculateBoundingBox());
    }

    public EntityBoulderBase(EntityType<? extends EntityBoulderBase> type, World world, LivingEntity caster, BlockState blockState, BlockPos pos, GeomancyTier tier) {
        super(type, world, caster, blockState, pos);
        this.finishedRisingTick = 4;
        this.animationOffset = this.random.nextFloat() * 8;
        this.setTier(tier);
        this.setSizeParams();
        this.setBoundingBox(this.calculateBoundingBox());
    }

    @Override
    public boolean isCollidable() {
        return this.active;
    }

    public boolean checkCanSpawn() {
        if (!this.getWorld().getNonSpectatingEntities(EntityBoulderBase.class, this.getBoundingBox().contract(0.01)).isEmpty())
            return false;
        return this.getWorld().isSpaceEmpty(this, this.getBoundingBox().contract(0.01));
    }

    public void setSizeParams() {
        GeomancyTier size = this.getTier();
        if (size == GeomancyTier.MEDIUM) {
            this.finishedRisingTick = 8;
        } else if (size == GeomancyTier.LARGE) {
            this.finishedRisingTick = 12;
        } else if (size == GeomancyTier.HUGE) {
            this.finishedRisingTick = 90;
        }
    }

    @Override
    protected @NotNull Box calculateBoundingBox() {
        EntityDimensions dim = EntityBoulderBase.SIZE_MAP.get(this.getTier());
        this.dimensions = dim;
        return dim.getBoxAt(this.getPos());
    }

    public void activate() {
        this.active = true;
        this.getWorld().sendEntityStatus(this, ACTIVATE_ID);
    }

    @Override
    public void handleStatus(byte id) {
        super.handleStatus(id);
        if (id == ACTIVATE_ID) {
            this.active = true;
        }
    }

    public boolean isFinishedRising() {
        return this.risingTick >= this.finishedRisingTick;
    }

    @Override
    public void tick() {
        if (this.firstUpdate) {
            this.setSizeParams();
            this.boulderSize = this.getTier();
        }
        if (this.storedBlock == null) this.storedBlock = this.getBlock();

        super.tick();
        this.setBoundingBox(this.calculateBoundingBox());
        this.move(MovementType.SELF, this.getVelocity());

        if (this.boulderSize == GeomancyTier.HUGE && this.risingTick < this.finishedRisingTick) {
            float f = this.getWidth() / 2.0F;
            Box aabb = new Box(this.getX() - (double) f, this.getY() - 0.5, this.getZ() - (double) f, this.getX() + (double) f, this.getY() + Math.min(this.risingTick / (float) this.finishedRisingTick * 3.5f, 3.5f), this.getZ() + (double) f);
            this.setBoundingBox(aabb);
        }

        if (this.risingTick < this.finishedRisingTick + 2) {
            List<Entity> popUpEntities = this.getWorld().getOtherEntities(this, this.getBoundingBox());
            for (Entity entity : popUpEntities) {
                if (entity.canHit() && !(entity instanceof EntityBoulderBase)) {
                    if (this.boulderSize != GeomancyTier.HUGE)
                        entity.move(MovementType.SHULKER_BOX, new Vec3d(0, 2 * (Math.pow(2, -this.risingTick * (0.6 - 0.1 * this.boulderSize.ordinal()))), 0));
                    else entity.move(MovementType.SHULKER_BOX, new Vec3d(0, 0.6f, 0));
                }
            }
        }

        if (this.risingTick == 1) {
            for (int i = 0; i < 20 * this.getWidth(); i++) {
                Vec3d particlePos = new Vec3d(this.random.nextFloat() * 1.3 * this.getWidth(), 0, 0);
                particlePos = particlePos.rotateY((float) (this.random.nextFloat() * 2 * Math.PI));
                this.getWorld().addParticle(new BlockStateParticleEffect(ParticleTypes.BLOCK, this.storedBlock), this.getX() + particlePos.x, this.getY() - 1, this.getZ() + particlePos.z, particlePos.x, 2, particlePos.z);
            }
            if (this.boulderSize == GeomancyTier.SMALL) {
                this.playSound(MMSounds.EFFECT_GEOMANCY_SMALL_CRASH, 1.5f, 1.3f);
                this.playSound(MMSounds.EFFECT_GEOMANCY_MAGIC_SMALL, 1.5f, 1f);
            } else if (this.boulderSize == GeomancyTier.MEDIUM) {
                this.playSound(MMSounds.EFFECT_GEOMANCY_HIT_MEDIUM_2, 1.5f, 1.5f);
                this.playSound(MMSounds.EFFECT_GEOMANCY_MAGIC_SMALL, 1.5f, 0.8f);
            } else if (this.boulderSize == GeomancyTier.LARGE) {
                this.playSound(MMSounds.EFFECT_GEOMANCY_HIT_MEDIUM_1, 1.5f, 0.9f);
                this.playSound(MMSounds.EFFECT_GEOMANCY_MAGIC_BIG, 1.5f, 1.5f);
                EntityCameraShake.cameraShake(this.getWorld(), this.getPos(), 10, 0.05f, 0, 20);
            } else if (this.boulderSize == GeomancyTier.HUGE) {
                this.playSound(MMSounds.EFFECT_GEOMANCY_MAGIC_BIG, 2f, 0.5f);
                this.playSound(MMSounds.EFFECT_GEOMANCY_RUMBLE_1, 2, 0.8f);
                EntityCameraShake.cameraShake(this.getWorld(), this.getPos(), 15, 0.05f, 50, 30);
            }
            if (this.getWorld().isClient) {
                AdvancedParticleBase.spawnAlwaysVisibleParticle(this.getWorld(), ParticleHandler.RING2, 64, this.getX(), this.getY() - 0.9f, this.getZ(), 0, 0, 0, false, 0, Math.PI / 2f, 0, 0, 3.5F, 0.83f, 1, 0.39f, 1, 1, (int) (5 + 2 * this.getWidth()), true, true, new ParticleComponent[]{
                        new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.ALPHA, ParticleComponent.KeyTrack.startAndEnd(1f, 0f), false),
                        new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.SCALE, ParticleComponent.KeyTrack.startAndEnd(0f, (1.0f + 0.5f * this.getWidth()) * 10f), false)
                });
            }
        }
        if (this.risingTick == 30 && this.boulderSize == GeomancyTier.HUGE) {
            this.playSound(MMSounds.EFFECT_GEOMANCY_RUMBLE_2, 2, 0.7f);
        }

        int dripTick = this.risingTick - 2;
        if (this.boulderSize == GeomancyTier.HUGE) dripTick -= 20;
        int dripNumber = (int) (this.getWidth() * 6 * Math.pow(1.03 + 0.04 * 1 / this.getWidth(), -(dripTick)));
        if (dripNumber >= 1 && dripTick > 0) {
            dripNumber *= this.random.nextFloat();
            for (int i = 0; i < dripNumber; i++) {
                Vec3d particlePos = new Vec3d(this.random.nextFloat() * 0.6 * this.getWidth(), 0, 0);
                particlePos = particlePos.rotateY((float) (this.random.nextFloat() * 2 * Math.PI));
                float offsetY;
                if (this.boulderSize == GeomancyTier.HUGE && this.risingTick < this.finishedRisingTick)
                    offsetY = this.random.nextFloat() * (this.getHeight() - 1) - this.getHeight() * (this.finishedRisingTick - this.risingTick) / this.finishedRisingTick;
                else offsetY = this.random.nextFloat() * (this.getHeight() - 1);
                this.getWorld().addParticle(new BlockStateParticleEffect(ParticleTypes.BLOCK, this.storedBlock), this.getX() + particlePos.x, this.getY() + offsetY, this.getZ() + particlePos.z, 0, -1, 0);
            }
        }
        if (this.active) {
            this.risingTick++;
        }
    }

    @Override
    protected void explode() {
        if (this.active) super.explode();
        else this.discard();
    }

    @Override
    public void remove(RemovalReason p_146834_) {
        super.remove(p_146834_);
        if (this.caster instanceof EntitySculptor sculptor) {
            sculptor.boulders.remove(this);
        }
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound compound) {
        super.writeCustomDataToNbt(compound);
        compound.putInt("risingTick", this.risingTick);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound compound) {
        super.readCustomDataFromNbt(compound);
        this.risingTick = compound.getInt("risingTick");
    }
}

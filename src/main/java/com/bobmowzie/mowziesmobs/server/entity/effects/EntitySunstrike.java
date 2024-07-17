package com.bobmowzie.mowziesmobs.server.entity.effects;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.particle.ParticleOrb;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.LeaderSunstrikeImmune;
import com.bobmowzie.mowziesmobs.server.entity.umvuthana.EntityUmvuthi;
import net.minecraft.block.BlockState;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.enums.SlabType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.EntityPositionS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;

import java.util.List;

public class EntitySunstrike extends Entity implements IEntityAdditionalSpawnData {
    public static final int STRIKE_EXPLOSION = 35;

    private static final int STRIKE_LENGTH = 43;
    // 1 minute past strike end
    private static final int STRIKE_LINGER = STRIKE_LENGTH + 20 * 60;
    private static final TrackedData<Integer> VARIANT_LEAST = DataTracker.registerData(EntitySunstrike.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> VARIANT_MOST = DataTracker.registerData(EntitySunstrike.class, TrackedDataHandlerRegistry.INTEGER);
    private int prevStrikeTime;
    private int strikeTime;
    private LivingEntity caster;

    public EntitySunstrike(EntityType<? extends EntitySunstrike> type, World world) {
        super(type, world);
        this.ignoreCameraFrustum = true;
    }

    public EntitySunstrike(EntityType<? extends EntitySunstrike> type, World world, LivingEntity caster, int x, int y, int z) {
        this(type, world);
        this.caster = caster;
        this.setPosition(x + 0.5F, y + 1.0625F, z + 0.5F);
    }

    @Override
    protected void initDataTracker() {
        this.getDataTracker().startTracking(VARIANT_LEAST, 0);
        this.getDataTracker().startTracking(VARIANT_MOST, 0);
    }

    public float getStrikeTime(float delta) {
        return this.getActualStrikeTime(delta) / STRIKE_LENGTH;
    }

    public float getStrikeDrawTime(float delta) {
        return this.getActualStrikeTime(delta) / STRIKE_EXPLOSION;
    }

    public float getStrikeDamageTime(float delta) {
        return (this.getActualStrikeTime(delta) - STRIKE_EXPLOSION) / (STRIKE_LENGTH - STRIKE_EXPLOSION);
    }

    public boolean isStrikeDrawing(float delta) {
        return this.getActualStrikeTime(delta) < STRIKE_EXPLOSION;
    }

    public boolean isLingering(float delta) {
        return this.getActualStrikeTime(delta) > STRIKE_EXPLOSION + 5;
    }

    public boolean isStriking(float delta) {
        return this.getActualStrikeTime(delta) < STRIKE_LENGTH;
    }

    private float getActualStrikeTime(float delta) {
        return this.prevStrikeTime + (this.strikeTime - this.prevStrikeTime) * delta;
    }

    private void setStrikeTime(int strikeTime) {
        this.prevStrikeTime = this.strikeTime = strikeTime;
    }

    public boolean isStriking() {
        return this.isStriking(1);
    }

    public long getVariant() {
        return (((long) this.getDataTracker().get(VARIANT_MOST)) << 32) | (this.getDataTracker().get(VARIANT_LEAST) & 0xFFFFFFFFL);
    }

    private void setVariant(long variant) {
        this.getDataTracker().set(VARIANT_MOST, (int) (variant >> 32));
        this.getDataTracker().set(VARIANT_LEAST, (int) variant);
    }

    @Override
    public boolean canHit() {
        return false;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean shouldRender(double distance) {
        return distance < 1024;
    }

    @Override
    public void tick() {
        super.tick();
        this.prevStrikeTime = this.strikeTime;

        if (this.getWorld().isClient) {
            if (this.strikeTime == 0) {
                MowziesMobs.PROXY.playSunstrikeSound(this);
            } else if (this.strikeTime < STRIKE_EXPLOSION - 10) {
                float time = this.getStrikeTime(1);
                int timeBonus = (int) (time * 5);
                int orbCount = this.random.nextInt(4 + timeBonus) + timeBonus + 1;
                while (orbCount-- > 0) {
                    float theta = this.random.nextFloat() * MathUtils.TAU;
                    final float min = 0.2F, max = 1.9F;
                    float r = this.random.nextFloat() * (max - min) + min;
                    float ox = r * MathHelper.cos(theta);
                    float oz = r * MathHelper.sin(theta);
                    final float minY = 0.1F;
                    float oy = this.random.nextFloat() * (time * 6 - minY) + minY;
                    this.getWorld().addParticle(new ParticleOrb.OrbData((float) this.getX(), (float) this.getZ()), this.getX() + ox, this.getY() + oy, this.getZ() + oz, 0, 0, 0);
                }
            } else if (this.strikeTime > STRIKE_EXPLOSION) {
                this.smolder();
            } else if (this.strikeTime == STRIKE_EXPLOSION) {
                this.spawnExplosionParticles(10);
            }
        } else {
            this.moveDownToGround();
            if (this.strikeTime >= STRIKE_LINGER || !this.getWorld().isSkyVisibleAllowingSea(this.getBlockPos())) {
                this.discard();
            } else if (this.strikeTime == STRIKE_EXPLOSION) {
                this.damageEntityLivingBaseNearby(3);
            }
        }
        this.strikeTime++;
    }

    public void moveDownToGround() {
        HitResult rayTrace = this.rayTrace(this);
        if (rayTrace.getType() == HitResult.Type.BLOCK) {
            BlockHitResult hitResult = (BlockHitResult) rayTrace;
            if (hitResult.getSide() == Direction.UP) {
                BlockState hitBlock = this.getWorld().getBlockState(hitResult.getBlockPos());
                if (this.strikeTime > STRIKE_LENGTH && hitBlock != this.getWorld().getBlockState(this.getBlockPos().down())) {
                    this.discard();
                }
                if (hitBlock.getBlock() instanceof SlabBlock && hitBlock.get(Properties.SLAB_TYPE) == SlabType.BOTTOM) {
                    this.setPosition(this.getX(), hitResult.getBlockPos().getY() + 1.0625F - 0.5f, this.getZ());
                } else {
                    this.setPosition(this.getX(), hitResult.getBlockPos().getY() + 1.0625F, this.getZ());
                }
                if (this.getWorld() instanceof ServerWorld) {
                    ((ServerWorld) this.getWorld()).getChunkManager().sendToOtherNearbyPlayers(this, new EntityPositionS2CPacket(this));
                }
            }
        }
    }

    public void damageEntityLivingBaseNearby(double radius) {
        Box region = new Box(this.getX() - radius, this.getY() - 0.5, this.getZ() - radius, this.getX() + radius, this.getWorld().getTopY() + 20, this.getZ() + radius);
        List<Entity> entities = this.getWorld().getOtherEntities(this, region);
        double radiusSq = radius * radius;
        for (Entity entity : entities) {
            if (this.getDistanceSqXZToEntity(entity) < radiusSq) {
                if (this.caster instanceof EntityUmvuthi && (entity instanceof LeaderSunstrikeImmune)) {
                    continue;
                }
                if (this.caster instanceof PlayerEntity && entity == this.caster) {
                    continue;
                }
                float damageFire = 2f;
                float damageMob = 2f;
                if (this.caster instanceof EntityUmvuthi) {
                    damageFire *= (float) ConfigHandler.COMMON.MOBS.UMVUTHI.combatConfig.attackMultiplier;
                    damageMob *= (float) ConfigHandler.COMMON.MOBS.UMVUTHI.combatConfig.attackMultiplier;
                }
                if (this.caster instanceof PlayerEntity) {
                    damageFire *= (float) ConfigHandler.COMMON.TOOLS_AND_ABILITIES.SUNS_BLESSING.sunsBlessingAttackMultiplier;
                    damageMob *= (float) ConfigHandler.COMMON.TOOLS_AND_ABILITIES.SUNS_BLESSING.sunsBlessingAttackMultiplier;
                }
                if (entity.damage(this.getDamageSources().mobProjectile(this, this.caster), damageMob)) entity.timeUntilRegen = 0;
                if (entity.damage(this.getDamageSources().onFire(), damageFire)) {
                    entity.setOnFireFor(3);
                }
            }
        }
    }

    public double getDistanceSqXZToEntity(Entity entityIn) {
        double d0 = this.getX() - entityIn.getX();
        double d2 = this.getZ() - entityIn.getZ();
        return d0 * d0 + d2 * d2;
    }

    private void smolder() {
        if (this.random.nextFloat() < 0.1F) {
            int amount = this.random.nextInt(2) + 1;
            while (amount-- > 0) {
                float theta = this.random.nextFloat() * MathUtils.TAU;
                float r = this.random.nextFloat() * 0.7F;
                float x = r * MathHelper.cos(theta);
                float z = r * MathHelper.sin(theta);
                this.getWorld().addParticle(ParticleTypes.LARGE_SMOKE, this.getX() + x, this.getY() + 0.1, this.getZ() + z, 0, 0, 0);
            }
        }
    }

    private void spawnExplosionParticles(int amount) {
        for (int i = 0; i < amount; i++) {
            final float velocity = 0.1F;
            float yaw = i * (MathUtils.TAU / amount);
            float vy = this.random.nextFloat() * 0.08F;
            float vx = velocity * MathHelper.cos(yaw);
            float vz = velocity * MathHelper.sin(yaw);
            this.getWorld().addParticle(ParticleTypes.FLAME, this.getX(), this.getY() + 0.1, this.getZ(), vx, vy, vz);
        }
        for (int i = 0; i < amount / 2; i++) {
            this.getWorld().addParticle(ParticleTypes.LAVA, this.getX(), this.getY() + 0.1, this.getZ(), 0, 0, 0);
        }
    }

    public void onSummon() {
        this.setVariant(this.random.nextLong());
    }

    private HitResult rayTrace(EntitySunstrike entity) {
        Vec3d startPos = new Vec3d(entity.getX(), entity.getY(), entity.getZ());
        Vec3d endPos = new Vec3d(entity.getX(), this.getWorld().getBottomY(), entity.getZ());
        return entity.getWorld().raycast(new RaycastContext(startPos, endPos, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, this));
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound compound) {
        compound.putInt("strikeTime", this.strikeTime);
        compound.putLong("variant", this.getVariant());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound compound) {
        this.setStrikeTime(compound.getInt("strikeTime"));
        this.setVariant(compound.getLong("variant"));
    }

    @Override
    public void writeSpawnData(PacketByteBuf buffer) {
        buffer.writeInt(this.strikeTime);
    }

    @Override
    public void readSpawnData(PacketByteBuf buffer) {
        this.setStrikeTime(buffer.readInt());
    }
}

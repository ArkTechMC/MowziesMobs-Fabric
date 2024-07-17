package com.bobmowzie.mowziesmobs.server.entity.effects.geomancy;

import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.sculptor.EntitySculptor;
import com.google.common.collect.Iterables;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.*;
import net.minecraft.world.World;

import java.util.List;

public class EntityBoulderSculptor extends EntityBoulderProjectile {
    private static final float MAX_DIST_HORIZONTAL = 4.0f;
    private static final float MAX_DIST_VERTICAL = 2.4f;
    private static final int MAX_TRIES = 10;
    protected boolean isMainPath = false;
    protected boolean descending = false;
    private EntityBoulderSculptor nextBoulder;
    private EntitySculptor sculptor;
    private EntityPillar pillar;
    private boolean replacementBoulder = false;
    private boolean spawnedNextBoulders = false;

    private int timeUntilActivation = -1;

    private float orbitSpeed = 0.0f;

    public EntityBoulderSculptor(EntityType<? extends EntityBoulderSculptor> type, World world) {
        super(type, world);
    }

    public EntityBoulderSculptor(EntityType<? extends EntityBoulderSculptor> type, World world, LivingEntity caster, BlockState blockState, BlockPos pos, GeomancyTier tier) {
        super(type, world, caster, blockState, pos, tier);
    }

    public EntityBoulderSculptor(EntityType<? extends EntityBoulderSculptor> type, EntityBoulderSculptor other) {
        super(type, other.getWorld(), other.caster, other.storedBlock, other.getBlockPos(), other.getTier());
    }

    public void descend() {
        this.descending = true;
    }

    @Override
    public boolean doRemoveTimer() {
        return false;
    }

    // When a boulder is fired, it replaces itself with a delayed boulder
    public void delayActivation(int delay) {
        this.timeUntilActivation = delay;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.sculptor == null || this.pillar == null) {
            if (this.caster instanceof EntitySculptor) {
                this.sculptor = (EntitySculptor) this.caster;
                this.sculptor.boulders.add(this);
                this.pillar = this.sculptor.getPillar();
            }
        }

        // Orbit around sculptor pillar
        if (!this.isTravelling() && this.sculptor != null && this.sculptor.getTarget() != null) {
            if (this.orbitSpeed < 0.02) this.orbitSpeed += 0.001;
            Vec3d between = this.getPos().subtract(this.sculptor.getPos());
            between = between.rotateY(this.orbitSpeed);
            this.setPosition(between.add(this.sculptor.getPos()));
        } else {
            if (this.orbitSpeed > 0) this.orbitSpeed -= 0.001;
        }

        if (!this.getWorld().isClient() && this.age > 2 && this.hasSyncedCaster && (this.sculptor == null || this.sculptor.isRemoved() || this.pillar == null || this.pillar.isRemoved() || (this.pillar.isFalling() && !this.descending))) {
            this.explode();
            return;
        }

        if (!this.replacementBoulder && this.age >= 2 && !this.spawnedNextBoulders && this.hasSyncedCaster) {
            this.nextBoulders();
        }

        if (!this.replacementBoulder && this.pillar != null && !this.getWorld().isClient()) {
            if (this.pillar.getY() + this.pillar.getHeight() >= this.getY() && !this.active) this.activate();
        }

        if (this.descending) {
            this.move(MovementType.SELF, new Vec3d(0, -EntityPillar.RISING_SPEED, 0));
            if (Iterables.size(this.getWorld().getBlockCollisions(this, this.getBoundingBox().expand(0.1))) > 0) {
                this.discard();
                return;
            }
        }

        if (!this.getWorld().isClient() && this.replacementBoulder) {
            if (this.timeUntilActivation > 0) {
                this.timeUntilActivation--;
            } else if (this.timeUntilActivation == 0) {
                this.activate();
            }
        }
    }

    public void nextBoulders() {
        if (this.caster == null || this.sculptor == null || this.pillar == null) return;
        this.spawnedNextBoulders = true;
        if (this.getWorld().isClient()) return;

        // If it's not the main path, path has a random chance of ending. Chance is weighted by the number of live paths.
        if (!this.isMainPath) {
            if (this.random.nextFloat() < MathUtils.fit(this.sculptor.numLivePaths, 3, 7, 0.0, 0.4)) {
                this.sculptor.numLivePaths--;
                return;
            }
        }

        // Path has a random chance of branching. Chance is weighted by the number of live paths.
        int numNextBoulders = 1;
        if (this.random.nextFloat() < MathUtils.fit(this.sculptor.numLivePaths, 1, 5, 0.2, 0.0)) {
            numNextBoulders = 2;
        }

        for (int i = 0; i < numNextBoulders; i++) {
            boolean success = this.nextSingleBoulder();
            if (success && i > 0) this.sculptor.numLivePaths++;
        }

//        this.activate();
    }

    @Override
    protected boolean startActive() {
        return false;
    }

    public boolean nextSingleBoulder() {
        int whichTierIndex = (int) (Math.pow(this.random.nextFloat(), 2) * (GeomancyTier.values().length - 2) + 1);
        GeomancyTier nextTier = GeomancyTier.values()[whichTierIndex];
        EntityBoulderSculptor nextBoulder = new EntityBoulderSculptor(EntityType.get(), this.getWorld(), this.caster, this.getBlock(), this.getBlockPos(), nextTier);

        // Try many times to find a good placement for the next boulder
        for (int j = 0; j < MAX_TRIES; j++) {
            Vec3d randomPos;
            if (this.getHeightFrac() < 1f) {
                randomPos = this.chooseRandomLocation(nextBoulder);
            }
            // If the platform is already at max height, next platform should move towards sculptor
            else if (this.getPos().multiply(1, 0, 1).distanceTo(this.sculptor.getPos().multiply(1, 0, 1)) > MAX_DIST_HORIZONTAL) {
                randomPos = this.chooseTowardsSculptorLocation(nextBoulder);
            } else return false;
            nextBoulder.setPosition(randomPos);

            // Make sure boulder has no collision, even with the future fully-grown pillar
            if (
                    this.getWorld().getEntitiesByClass(EntityBoulderSculptor.class, nextBoulder.getBoundingBox(), (b) -> b != this).isEmpty()
                            && Iterables.size(this.getWorld().getBlockCollisions(nextBoulder, nextBoulder.getBoundingBox())) == 0
                            && !this.pillar.getBoundingBox().withMaxY(this.pillar.getY() + EntitySculptor.TEST_HEIGHT).intersects(nextBoulder.getBoundingBox())
            ) {
                // Check nearby boulders below to make sure this boulder doesn't block jumping path
                Box toCheck = nextBoulder.getBoundingBox().expand(MAX_DIST_HORIZONTAL, MAX_DIST_VERTICAL / 2f + 1.5f, MAX_DIST_HORIZONTAL).offset(0, -MAX_DIST_VERTICAL / 2f - 1.5f, 0);
                List<EntityBoulderSculptor> platforms = this.getWorld().getNonSpectatingEntities(EntityBoulderSculptor.class, toCheck);
                boolean obstructsPath = false;
                for (EntityBoulderSculptor platform : platforms) {
                    if (platform != nextBoulder && !nextBoulder.checkJumpPath(platform)) {
                        obstructsPath = true;
                        break;
                    }
                }
                if (!obstructsPath) {
                    this.getWorld().spawnEntity(nextBoulder);
                    if (this.isMainPath && this.nextBoulder == null) {
                        this.nextBoulder = nextBoulder;
                        this.nextBoulder.setMainPath();
                    }
                    return true;
                }
            }
        }
        // If the main path can't find a good placement, it needs to try again next tick. Branched paths can end.
        if (this.isMainPath) this.spawnedNextBoulders = false;
        return false;
    }

    protected Vec3d chooseRandomLocation(EntityBoulderSculptor nextBoulder) {
        EntityDimensions thisDims = SIZE_MAP.get(this.getTier());
        EntityDimensions nextDims = SIZE_MAP.get(nextBoulder.getTier());
        Vec3d startLocation = this.getPos();
        Vec2f fromPillarPos = new Vec2f((float) (this.caster.getX() - startLocation.x), (float) (this.caster.getZ() - startLocation.z));
        float horizontalOffset = net.minecraft.util.math.MathHelper.nextFloat(this.random, 1, MAX_DIST_HORIZONTAL) + thisDims.width / 2f + nextDims.width / 2f;
        float verticalOffset = net.minecraft.util.math.MathHelper.nextFloat(this.random, 0, MAX_DIST_VERTICAL) - (nextDims.height - thisDims.height);

        float baseAngle = (float) -Math.toDegrees(Math.atan2(fromPillarPos.y, fromPillarPos.x));
        // Minimum and maximum angles force the angle to approach 90 degrees as it gets too close or too far from the pillar
        float minRandomAngle = (float) (Math.min(Math.pow(3f, -fromPillarPos.length() + 3), 1f) * 90f);
        double radius = EntitySculptor.testRadiusAtHeight(startLocation.y + verticalOffset + nextDims.height - this.pillar.getY());
        float maxRandomAngle = 180f - (float) (Math.min(Math.pow(3f, fromPillarPos.length() - radius), 1f) * 90f);
        float randomAngle = net.minecraft.util.math.MathHelper.nextFloat(this.random, minRandomAngle, maxRandomAngle);
        if (this.random.nextBoolean()) randomAngle *= -1;
        // random angle tends towards center as the platforms reach higher up
        randomAngle *= 1f - Math.pow(this.getHeightFrac(), 5f) * 0.75;
        Vec3d offset = new Vec3d(horizontalOffset, verticalOffset, 0);
        float finalAngle = (float) Math.toRadians(MathHelper.wrapDegrees(baseAngle + randomAngle));
        offset = offset.rotateY(finalAngle);
        Vec3d nextLocation = startLocation.add(offset);
        if (nextLocation.getY() + nextDims.height > this.pillar.getY() + EntitySculptor.TEST_HEIGHT) {
            nextLocation = new Vec3d(nextLocation.getX(), this.pillar.getY() + EntitySculptor.TEST_HEIGHT - nextDims.height, nextLocation.getZ());
        }

        return nextLocation;
    }

    // For when the platforms have reached max height and just need to head towards sculptor
    protected Vec3d chooseTowardsSculptorLocation(EntityBoulderSculptor nextBoulder) {
        EntityDimensions thisDims = SIZE_MAP.get(this.getTier());
        EntityDimensions nextDims = SIZE_MAP.get(nextBoulder.getTier());
        Vec3d startLocation = this.getPos();
        Vec2f fromPillarPos = new Vec2f((float) (this.caster.getX() - startLocation.x), (float) (this.caster.getZ() - startLocation.z));
        float horizontalOffset = net.minecraft.util.math.MathHelper.nextFloat(this.random, 1, MAX_DIST_HORIZONTAL) + thisDims.width / 2f + nextDims.width / 2f;

        float baseAngle = (float) -Math.toDegrees(Math.atan2(fromPillarPos.y, fromPillarPos.x));
        Vec3d offset = new Vec3d(horizontalOffset, 0, 0);
        float finalAngle = (float) Math.toRadians(MathHelper.wrapDegrees(baseAngle));
        offset = offset.rotateY(finalAngle);

        return startLocation.add(offset);
    }

    public EntityBoulderSculptor getNextBoulder() {
        return this.nextBoulder;
    }

    public boolean checkJumpPath(EntityBoulderSculptor platform) {
        EntityBoulderSculptor next = platform.getNextBoulder();
        if (next == null) return true;
        EntityDimensions platDims = SIZE_MAP.get(platform.getTier());
        EntityDimensions nextDims = SIZE_MAP.get(next.getTier());

        Vec3d toNext = next.getPos().subtract(platform.getPos());
        Vec3d startPos = platform.getPos().add(0, platDims.height, 0).add(toNext.multiply(1, 0, 1).normalize().multiply(platDims.width / 2f));
        Vec3d endPos = next.getPos().add(0, nextDims.height, 0).add(toNext.multiply(1, 0, 1).normalize().multiply(-nextDims.width / 2f));

        double gravity = -net.minecraftforge.common.ForgeMod.ENTITY_GRAVITY.get().getDefaultValue();
        double jumpVelY = 1D; // Player y jump speed with jump boost II
        double heightDiff = endPos.getY() - startPos.getY();
        // Quadratic formula to solve for time it takes to complete jump
        double totalTime = (-jumpVelY - Math.sqrt(jumpVelY * jumpVelY - 4 * gravity * -heightDiff)) / (2 * gravity);
        // Use time to get needed x and z velocities
        double jumpVelX = (endPos.getX() - startPos.getX()) / totalTime;
        double jumpVelZ = (endPos.getZ() - startPos.getZ()) / totalTime;
        Vec3d jumpVel = new Vec3d(jumpVelX, jumpVelY, jumpVelZ);

        Box thisBounds = SIZE_MAP.get(this.getTier()).getBoxAt(this.getPos());
        int substeps = 5;
        for (int i = 0; i < substeps; i++) {
            double time = (totalTime / (double) substeps) * i;
            Vec3d jumpPosition = new Vec3d(0, gravity * time * time, 0).add(jumpVel.multiply(time)).add(startPos);
            Box playerBounds = EntityType.PLAYER.getDimensions().getBoxAt(jumpPosition);
            if (thisBounds.intersects(playerBounds)) return false;
        }

        return true;
    }

    public void setMainPath() {
        this.isMainPath = true;
    }

    public float getHeightFrac() {
        if (this.caster instanceof EntitySculptor sculptor) {
            EntityPillar pillar = sculptor.getPillar();
            if (pillar != null) {
                return (float) (this.getPos().getY() + this.getHeight() - pillar.getY()) / EntitySculptor.TEST_HEIGHT;
            }
        }
        return -1;
    }

    @Override
    public void remove(RemovalReason p_146834_) {
        super.remove(p_146834_);
        if (this.sculptor != null) {
            this.sculptor.boulders.remove(this);
        }
    }

    @Override
    protected boolean travellingBlockedBy(Entity entity) {
        return super.travellingBlockedBy(entity) && !(entity instanceof EntityBoulderSculptor);
    }

    @Override
    public boolean collidesWith(Entity entity) {
        return super.collidesWith(entity) && !(entity instanceof EntityBoulderSculptor);
    }

    @Override
    protected float getShootRingParticleScale() {
        return super.getShootRingParticleScale() * 4;
    }

    @Override
    public void shoot(Vec3d shootDirection) {
        super.shoot(shootDirection);
        EntityBoulderSculptor boulderSculptor = new EntityBoulderSculptor(EntityHandler.BOULDER_SCULPTOR, this);
        boulderSculptor.setPosition(this.getPos());
        boulderSculptor.replacementBoulder = true;
        boulderSculptor.delayActivation(40);
        this.getWorld().spawnEntity(boulderSculptor);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound compound) {
        super.writeCustomDataToNbt(compound);
        compound.putBoolean("SpawnedNext", this.spawnedNextBoulders);
        compound.putBoolean("Descending", this.descending);
        compound.putBoolean("MainPath", this.isMainPath);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound compound) {
        super.readCustomDataFromNbt(compound);
        this.spawnedNextBoulders = compound.getBoolean("SpawnedNext");
        this.descending = compound.getBoolean("Descending");
        this.isMainPath = compound.getBoolean("MainPath");
    }
}

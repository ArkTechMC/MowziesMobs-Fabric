package com.bobmowzie.mowziesmobs.server.entity.effects.geomancy;

import com.bobmowzie.mowziesmobs.server.block.ICopiedBlockProperties;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntityCameraShake;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntityFallingBlock;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntityMagicEffect;
import com.bobmowzie.mowziesmobs.server.potion.EffectGeomancy;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import com.bobmowzie.mowziesmobs.server.tag.TagHandler;
import io.github.fabricators_of_create.porting_lib.tags.Tags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

public abstract class EntityGeomancyBase extends EntityMagicEffect implements GeoEntity {
    protected static final TrackedData<BlockState> BLOCK_STATE = DataTracker.registerData(EntityGeomancyBase.class, TrackedDataHandlerRegistry.BLOCK_STATE);
    private static final byte EXPLOSION_PARTICLES_ID = 69;
    private static final TrackedData<Integer> TIER = DataTracker.registerData(EntityGeomancyBase.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> DEATH_TIME = DataTracker.registerData(EntityGeomancyBase.class, TrackedDataHandlerRegistry.INTEGER);
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private boolean doRemoveTimer = true;

    public EntityGeomancyBase(EntityType<? extends EntityMagicEffect> type, World worldIn) {
        super(type, worldIn);
    }

    public EntityGeomancyBase(EntityType<? extends EntityMagicEffect> type, World worldIn, LivingEntity caster, BlockState blockState, BlockPos pos) {
        super(type, worldIn, caster);
        if (!worldIn.isClient && blockState != null && EffectGeomancy.isBlockUseable(blockState)) {
            BlockState newBlock = this.changeBlock(blockState);
            this.setBlock(newBlock);
        }
    }

    // Change the specified block to its geomancy version. I.E. Grass blocks turn to dirt, stairs and slabs turn to base versions.
    public BlockState changeBlock(BlockState blockState) {
        if (!blockState.isIn(TagHandler.GEOMANCY_USEABLE)) {
            ICopiedBlockProperties properties = (ICopiedBlockProperties) blockState.getBlock().settings;
            Block baseBlock = properties.getBaseBlock();
            if (baseBlock != null) {
                blockState = baseBlock.getDefaultState();
            }
        }

        if (
                blockState.getBlock() == Blocks.GRASS_BLOCK ||
                        blockState.getBlock() == Blocks.MYCELIUM ||
                        blockState.getBlock() == Blocks.PODZOL ||
                        blockState.getBlock() == Blocks.DIRT_PATH
        ) {
            blockState = Blocks.DIRT.getDefaultState();
        } else if (blockState.isIn(Tags.Blocks.ORES_IN_GROUND_DEEPSLATE))
            blockState = Blocks.DEEPSLATE.getDefaultState();
        else if (blockState.isIn(BlockTags.NYLIUM)) blockState = Blocks.NETHERRACK.getDefaultState();
        else if (blockState.isIn(Tags.Blocks.ORES_IN_GROUND_NETHERRACK))
            blockState = Blocks.NETHERRACK.getDefaultState();
        else if (blockState.isIn(Tags.Blocks.ORES_IN_GROUND_STONE)) blockState = Blocks.STONE.getDefaultState();
        else if (blockState.isIn(Tags.Blocks.SAND_RED)) blockState = Blocks.RED_SANDSTONE.getDefaultState();
        else if (blockState.isIn(Tags.Blocks.SAND_COLORLESS)) blockState = Blocks.SANDSTONE.getDefaultState();
        else if (blockState.getBlock() == Blocks.SOUL_SAND) blockState = Blocks.SOUL_SOIL.getDefaultState();

        return blockState;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.doRemoveTimer()) {
            int newDeathTime = this.getDeathTime() - 1;
            this.setDeathTime(newDeathTime);
            if (newDeathTime < 0) this.explode();
        }
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.getDataTracker().startTracking(BLOCK_STATE, Blocks.DIRT.getDefaultState());
        this.getDataTracker().startTracking(DEATH_TIME, 1200);
        this.getDataTracker().startTracking(TIER, 0);
    }

    @Override
    public boolean isCollidable() {
        return true;
    }

    @Override
    public boolean doesRenderOnFire() {
        return false;
    }

    @Override
    public PistonBehavior getPistonBehavior() {
        return PistonBehavior.BLOCK;
    }

    @Override
    public boolean isSilent() {
        return false;
    }

    @Override
    public void playSound(SoundEvent soundIn, float volume, float pitch) {
        super.playSound(soundIn, volume, pitch + this.random.nextFloat() * 0.25f - 0.125f);
    }

    @Override
    public boolean isImmuneToExplosion() {
        return true;
    }

    protected void explode() {
        this.getWorld().sendEntityStatus(this, EXPLOSION_PARTICLES_ID);
        GeomancyTier tier = this.getTier();
        if (tier == GeomancyTier.SMALL) {
            this.playSound(MMSounds.EFFECT_GEOMANCY_MAGIC_SMALL, 1.5f, 0.9f);
            this.playSound(MMSounds.EFFECT_GEOMANCY_BREAK, 1.5f, 1f);
        } else if (tier == GeomancyTier.MEDIUM) {
            this.playSound(MMSounds.EFFECT_GEOMANCY_MAGIC_SMALL, 1.5f, 0.7f);
            this.playSound(MMSounds.EFFECT_GEOMANCY_BREAK_MEDIUM_3, 1.5f, 1.5f);
        } else if (tier == GeomancyTier.LARGE) {
            this.playSound(MMSounds.EFFECT_GEOMANCY_MAGIC_BIG, 1.5f, 1f);
            this.playSound(MMSounds.EFFECT_GEOMANCY_BREAK_MEDIUM_1, 1.5f, 0.9f);
            EntityCameraShake.cameraShake(this.getWorld(), this.getPos(), 15, 0.05f, 0, 20);

            for (int i = 0; i < 5; i++) {
                Vec3d particlePos = new Vec3d(this.random.nextFloat() * 2, 0, 0);
                particlePos = particlePos.rotateY((float) (this.random.nextFloat() * 2 * Math.PI));
                particlePos = particlePos.rotateX((float) (this.random.nextFloat() * 2 * Math.PI));
                particlePos = particlePos.add(new Vec3d(0, this.getHeight() / 4, 0));
                EntityFallingBlock fallingBlock = new EntityFallingBlock(EntityHandler.FALLING_BLOCK, this.getWorld(), 70, this.getBlock());
                fallingBlock.setPosition(this.getX() + particlePos.x, this.getY() + 0.5 + particlePos.y, this.getZ() + particlePos.z);
                fallingBlock.setVelocity((float) particlePos.x * 0.3f, 0.2f + this.random.nextFloat() * 0.6f, (float) particlePos.z * 0.3f);
                this.getWorld().spawnEntity(fallingBlock);
            }
        } else if (tier == GeomancyTier.HUGE) {
            this.playSound(MMSounds.EFFECT_GEOMANCY_MAGIC_BIG, 1.5f, 0.5f);
            this.playSound(MMSounds.EFFECT_GEOMANCY_BREAK_LARGE_1, 1.5f, 0.5f);
            EntityCameraShake.cameraShake(this.getWorld(), this.getPos(), 20, 0.05f, 0, 20);

            for (int i = 0; i < 7; i++) {
                Vec3d particlePos = new Vec3d(this.random.nextFloat() * 2.5f, 0, 0);
                particlePos = particlePos.rotateY((float) (this.random.nextFloat() * 2 * Math.PI));
                particlePos = particlePos.rotateX((float) (this.random.nextFloat() * 2 * Math.PI));
                particlePos = particlePos.add(new Vec3d(0, this.getHeight() / 4, 0));
                EntityFallingBlock fallingBlock = new EntityFallingBlock(EntityHandler.FALLING_BLOCK, this.getWorld(), 70, this.getBlock());
                fallingBlock.setPosition(this.getX() + particlePos.x, this.getY() + 0.5 + particlePos.y, this.getZ() + particlePos.z);
                fallingBlock.setVelocity((float) particlePos.x * 0.3f, 0.2f + this.random.nextFloat() * 0.6f, (float) particlePos.z * 0.3f);
                this.getWorld().spawnEntity(fallingBlock);
            }
        }
        this.discard();
    }

    private void spawnExplosionParticles() {
        for (int i = 0; i < 40 * this.getWidth(); i++) {
            Vec3d particlePos = new Vec3d(this.random.nextFloat() * 0.7 * this.getWidth(), 0, 0);
            particlePos = particlePos.rotateY((float) (this.random.nextFloat() * 2 * Math.PI));
            particlePos = particlePos.rotateX((float) (this.random.nextFloat() * 2 * Math.PI));
            particlePos.add(0, this.getHeight() / 2.0, 0);
            Camera camera = MinecraftClient.getInstance().gameRenderer.getCamera();
            boolean overrideLimiter = camera.getPos().squaredDistanceTo(this.getX(), this.getY(), this.getZ()) < 64 * 64;
            this.getWorld().addImportantParticle(new BlockStateParticleEffect(ParticleTypes.BLOCK, this.getBlock()), overrideLimiter, this.getX() + particlePos.x, this.getY() + 0.5 + particlePos.y, this.getZ() + particlePos.z, particlePos.x, particlePos.y, particlePos.z);
        }
    }

    @Override
    public void handleStatus(byte id) {
        if (id == EXPLOSION_PARTICLES_ID) {
            this.spawnExplosionParticles();
        } else super.handleStatus(id);
    }

    public BlockState getBlock() {
        return this.getDataTracker().get(BLOCK_STATE);
    }

    public void setBlock(BlockState block) {
        this.getDataTracker().set(BLOCK_STATE, block);
    }

    public GeomancyTier getTier() {
        return GeomancyTier.values()[this.dataTracker.get(TIER)];
    }

    public void setTier(GeomancyTier size) {
        this.dataTracker.set(TIER, size.ordinal());
    }

    public int getDeathTime() {
        return this.dataTracker.get(DEATH_TIME);
    }

    public void setDeathTime(int deathTime) {
        this.dataTracker.set(DEATH_TIME, deathTime);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound compound) {
        super.writeCustomDataToNbt(compound);
        BlockState blockState = this.getBlock();
        if (blockState != null) compound.put("block", NbtHelper.fromBlockState(blockState));
        if (this.doRemoveTimer()) compound.putInt("deathTime", this.getDeathTime());
        compound.putInt("tier", this.getTier().ordinal());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound compound) {
        super.readCustomDataFromNbt(compound);
        NbtElement blockStateCompound = compound.get("block");
        if (blockStateCompound != null) {
            BlockState blockState = NbtHelper.toBlockState(this.getWorld().createCommandRegistryWrapper(RegistryKeys.BLOCK), (NbtCompound) blockStateCompound);
            this.setBlock(blockState);
        }
        if (compound.contains("deathTime")) {
            this.doRemoveTimer = true;
            this.setDeathTime(compound.getInt("deathTime"));
        } else {
            this.doRemoveTimer = false;
        }
        this.setTier(GeomancyTier.values()[compound.getInt("tier")]);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }

    public boolean doRemoveTimer() {
        return this.doRemoveTimer;
    }

    public void setDoRemoveTimer(boolean doRemoveTimer) {
        this.doRemoveTimer = doRemoveTimer;
    }

    public enum GeomancyTier {
        NONE,
        SMALL,
        MEDIUM,
        LARGE,
        HUGE
    }
}

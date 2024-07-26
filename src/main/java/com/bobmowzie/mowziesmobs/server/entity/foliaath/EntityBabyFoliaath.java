package com.bobmowzie.mowziesmobs.server.entity.foliaath;

import com.bobmowzie.mowziesmobs.server.ai.animation.AnimationBabyFoliaathEatAI;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import com.bobmowzie.mowziesmobs.server.entity.MowzieLLibraryEntity;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import com.iafenvoy.uranus.animation.Animation;
import com.iafenvoy.uranus.animation.AnimationHandler;
import com.iafenvoy.uranus.client.model.tools.ControlledAnimation;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import java.util.ArrayList;
import java.util.List;

public class EntityBabyFoliaath extends MowzieLLibraryEntity {
    public static final Animation EAT_ANIMATION = Animation.create(20);
    private static final TrackedData<Integer> GROWTH = DataTracker.registerData(EntityBabyFoliaath.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Boolean> INFANT = DataTracker.registerData(EntityBabyFoliaath.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> HUNGRY = DataTracker.registerData(EntityBabyFoliaath.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<ItemStack> EATING = DataTracker.registerData(EntityBabyFoliaath.class, TrackedDataHandlerRegistry.ITEM_STACK);
    public ControlledAnimation activate = new ControlledAnimation(5);
    private double prevActivate;

    public EntityBabyFoliaath(EntityType<? extends EntityBabyFoliaath> type, World world) {
        super(type, world);
        this.setInfant(true);
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MowzieEntity.createAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 1)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 1);
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(1, new AnimationBabyFoliaathEatAI<EntityBabyFoliaath>(this, EAT_ANIMATION));
    }

    protected boolean isMovementNoisy() {
        return false;
    }

    @Override
    public boolean isPushedByFluids() {
        return false;
    }

    @Override
    public void addVelocity(double x, double y, double z) {
        super.addVelocity(0, y, 0);
    }

    @Override
    protected void tickCramming() {

    }

    @Override
    public void tick() {
        super.tick();
        this.setVelocity(0, this.getVelocity().y, 0);
        this.bodyYaw = 0;

        if (this.arePlayersCarryingMeat(this.getPlayersNearby(3, 3, 3, 3)) && this.getAnimation() == NO_ANIMATION && this.getHungry()) {
            this.activate.increaseTimer();
        } else {
            this.activate.decreaseTimer();
        }

        if (this.activate.getTimer() == 1 && this.prevActivate - this.activate.getTimer() < 0) {
            this.playSound(MMSounds.ENTITY_FOLIAATH_GRUNT, 0.5F, 1.5F);
        }
        this.prevActivate = this.activate.getTimer();

        if (!this.getWorld().isClient && this.getHungry() && this.getAnimation() == NO_ANIMATION) {
            for (ItemEntity meat : this.getMeatsNearby(0.4, 0.2, 0.4, 0.4)) {
                ItemStack stack = meat.getStack().split(1);
                if (!stack.isEmpty()) {
                    this.setEating(stack);
                    AnimationHandler.INSTANCE.sendAnimationMessage(this, EAT_ANIMATION);
                    this.playSound(MMSounds.ENTITY_FOLIAATH_BABY_EAT, 0.5F, 1.2F);
                    this.incrementGrowth();
                    this.setHungry(false);
                    break;
                }
            }
        }
        if (this.getWorld().isClient && this.getAnimation() == EAT_ANIMATION && (this.getAnimationTick() == 3 || this.getAnimationTick() == 7 || this.getAnimationTick() == 11 || this.getAnimationTick() == 15 || this.getAnimationTick() == 19)) {
            for (int i = 0; i <= 5; i++) {
                this.getWorld().addParticle(new ItemStackParticleEffect(ParticleTypes.ITEM, this.getEating()), this.getX(), this.getY() + 0.2, this.getZ(), this.random.nextFloat() * 0.2 - 0.1, this.random.nextFloat() * 0.2, this.random.nextFloat() * 0.2 - 0.1);
            }
        }

        //Growing
        if (!this.getWorld().isClient) {
            if (this.age % 20 == 0 && !this.getHungry()) {
                this.incrementGrowth();
            }
            // TODO: cleanup this poor logic
            this.setInfant(this.getGrowth() < 600);
            if (this.getInfant()) {
                this.setHungry(false);
            }
            if (this.getGrowth() == 600) {
                this.setHungry(true);
            }
            if (this.getGrowth() == 1200) {
                this.setHungry(true);
            }
            if (this.getGrowth() == 1800) {
                this.setHungry(true);
            }
            if (this.getGrowth() == 2400) {
                EntityFoliaath adultFoliaath = new EntityFoliaath(EntityHandler.FOLIAATH, this.getWorld());
                adultFoliaath.setPosition(this.getX(), this.getY(), this.getZ());
                adultFoliaath.setCanDespawn(false);
                this.getWorld().spawnEntity(adultFoliaath);
                this.discard();
            }
        }
    }

    @Override
    public Animation getDeathAnimation() {
        return null;
    }

    @Override
    public Animation getHurtAnimation() {
        return null;
    }

    private boolean arePlayersCarryingMeat(List<PlayerEntity> players) {
        if (players.size() > 0) {
            for (PlayerEntity player : players) {
                FoodComponent food = player.getMainHandStack().getItem().getFoodComponent();
                if (food != null && food.isMeat()) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void onDeath(DamageSource source) {
        super.onDeath(source);
        for (int i = 0; i < 10; i++) {
            this.getWorld().addParticle(new BlockStateParticleEffect(ParticleTypes.BLOCK, Blocks.JUNGLE_LEAVES.getDefaultState()), this.getX(), this.getY() + 0.2, this.getZ(), 0, 0, 0);
        }
        this.discard();
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public void pushAwayFrom(Entity collider) {
        this.setVelocity(0, this.getVelocity().y, 0);
    }

    @Override
    protected SoundEvent getDeathSound() {
        this.playSound(SoundEvents.BLOCK_GRASS_BREAK, 1, 0.8F);
        return null;
    }

    @Override
    public boolean canSpawn(WorldAccess world, SpawnReason reason) {
        if (world.doesNotIntersectEntities(this) && world.isSpaceEmpty(this) && !world.containsFluid(this.getBoundingBox())) {
            BlockPos ground = new BlockPos(
                    MathHelper.floor(this.getX()),
                    MathHelper.floor(this.getBoundingBox().minY) - 1,
                    MathHelper.floor(this.getZ())
            );

            BlockState block = world.getBlockState(ground);

            if (block.getBlock() == Blocks.GRASS_BLOCK || block.isIn(BlockTags.DIRT) || block.isIn(BlockTags.LEAVES)) {
                this.playSound(SoundEvents.BLOCK_GRASS_HIT, 1, 0.8F);
                return true;
            }
        }
        return false;
    }

    public List<ItemEntity> getMeatsNearby(double distanceX, double distanceY, double distanceZ, double radius) {
        List<Entity> list = this.getWorld().getOtherEntities(this, this.getBoundingBox().expand(distanceX, distanceY, distanceZ));
        ArrayList<ItemEntity> listEntityItem = new ArrayList<>();
        for (Entity entityNeighbor : list) {
            if (entityNeighbor instanceof ItemEntity && this.distanceTo(entityNeighbor) <= radius) {
                FoodComponent food = ((ItemEntity) entityNeighbor).getStack().getItem().getFoodComponent();
                if (food != null && food.isMeat()) {
                    listEntityItem.add((ItemEntity) entityNeighbor);
                }
            }
        }
        return listEntityItem;
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound compound) {
        super.writeCustomDataToNbt(compound);
        compound.putInt("tickGrowth", this.getGrowth());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound compound) {
        super.readCustomDataFromNbt(compound);
        this.setGrowth(compound.getInt("tickGrowth"));
    }

    @Override
    public boolean cannotDespawn() {
        return true;
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.getDataTracker().startTracking(GROWTH, 0);
        this.getDataTracker().startTracking(INFANT, false);
        this.getDataTracker().startTracking(HUNGRY, false);
        this.getDataTracker().startTracking(EATING, ItemStack.EMPTY);
    }

    public int getGrowth() {
        return this.getDataTracker().get(GROWTH);
    }

    public void setGrowth(int growth) {
        this.getDataTracker().set(GROWTH, growth);
    }

    public void incrementGrowth() {
        this.setGrowth(this.getGrowth() + 1);
    }

    public boolean getInfant() {
        return this.getDataTracker().get(INFANT);
    }

    public void setInfant(boolean infant) {
        this.getDataTracker().set(INFANT, infant);
    }

    public boolean getHungry() {
        return this.getDataTracker().get(HUNGRY);
    }

    public void setHungry(boolean hungry) {
        this.getDataTracker().set(HUNGRY, hungry);
    }

    public ItemStack getEating() {
        return this.getDataTracker().get(EATING);
    }

    public void setEating(ItemStack stack) {
        this.getDataTracker().set(EATING, stack);
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[]{EAT_ANIMATION};
    }
}
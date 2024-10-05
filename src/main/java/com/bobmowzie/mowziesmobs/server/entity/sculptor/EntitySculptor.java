package com.bobmowzie.mowziesmobs.server.entity.sculptor;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.particle.ParticleHandler;
import com.bobmowzie.mowziesmobs.client.particle.util.AdvancedParticleBase;
import com.bobmowzie.mowziesmobs.client.particle.util.ParticleComponent;
import com.bobmowzie.mowziesmobs.client.particle.util.ParticleRotation;
import com.bobmowzie.mowziesmobs.server.ability.Ability;
import com.bobmowzie.mowziesmobs.server.ability.AbilityHandler;
import com.bobmowzie.mowziesmobs.server.ability.AbilitySection;
import com.bobmowzie.mowziesmobs.server.ability.AbilityType;
import com.bobmowzie.mowziesmobs.server.ability.abilities.mob.DieAbility;
import com.bobmowzie.mowziesmobs.server.ability.abilities.mob.HurtAbility;
import com.bobmowzie.mowziesmobs.server.ai.UseAbilityAI;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import com.bobmowzie.mowziesmobs.server.entity.MowzieGeckoEntity;
import com.bobmowzie.mowziesmobs.server.entity.effects.geomancy.EntityBoulderSculptor;
import com.bobmowzie.mowziesmobs.server.entity.effects.geomancy.EntityGeomancyBase;
import com.bobmowzie.mowziesmobs.server.entity.effects.geomancy.EntityPillar;
import com.bobmowzie.mowziesmobs.server.inventory.ContainerSculptorTrade;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import com.bobmowzie.mowziesmobs.server.potion.EffectGeomancy;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.scoreboard.AbstractTeam;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.Animation;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;

import java.util.*;

public class EntitySculptor extends MowzieGeckoEntity {
    public static final AbilityType<EntitySculptor, HurtAbility<EntitySculptor>> HURT_ABILITY = new AbilityType<>("sculptor_hurt", (type, entity) -> new HurtAbility<>(type, entity, RawAnimation.begin().thenPlay("hurt"), 17, 0));
    public static final AbilityType<EntitySculptor, DieAbility<EntitySculptor>> DIE_ABILITY = new AbilityType<>("sculptor_die", (type, entity) -> new DieAbility<>(type, entity, RawAnimation.begin().thenPlay("die"), 70));
    public static final AbilityType<EntitySculptor, StartTestAbility> START_TEST = new AbilityType<>("testStart", StartTestAbility::new);
    public static final AbilityType<EntitySculptor, FailTestAbility> FAIL_TEST = new AbilityType<>("testFail", FailTestAbility::new);
    public static final AbilityType<EntitySculptor, PassTestAbility> PASS_TEST = new AbilityType<>("testPass", PassTestAbility::new);
    public static final AbilityType<EntitySculptor, AttackAbility> ATTACK_ABILITY = new AbilityType<>("attack", AttackAbility::new);
    private static final TrackedData<ItemStack> DESIRES = DataTracker.registerData(EntitySculptor.class, TrackedDataHandlerRegistry.ITEM_STACK);
    private static final TrackedData<Boolean> IS_TRADING = DataTracker.registerData(EntitySculptor.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Optional<UUID>> TESTING_PLAYER = DataTracker.registerData(EntitySculptor.class, TrackedDataHandlerRegistry.OPTIONAL_UUID);
    public static int TEST_HEIGHT = 50;
    public static int TEST_RADIUS_BOTTOM = 6;
    public static int TEST_RADIUS = 16;
    public static int TEST_MAX_RADIUS_HEIGHT = 10;
    public static double TEST_RADIUS_FALLOFF = 5;
    private static final RawAnimation HURT = RawAnimation.begin().thenPlay("hurt");
    private static final RawAnimation TEST_OBSTRUCTED = RawAnimation.begin().thenLoop("test_obstructed");
    public boolean handLOpen = true;
    public boolean handROpen = true;
    public int numLivePaths = 0;
    public List<EntityBoulderSculptor> boulders = new ArrayList<>();
    private PlayerEntity customer;
    private PlayerEntity testingPlayer;
    private Optional<Double> prevPlayerVelY;
    private Optional<Vec3d> prevPlayerPosition;
    private int ticksAcceleratingUpward;
    private boolean testing;
    private boolean isTestObstructed;
    private boolean isTestObstructedSoFar;
    private int obstructionTestHeight;
    private EntityPillar.EntityPillarSculptor pillar;
    private RevengeGoal hurtByTargetAI;

    public EntitySculptor(EntityType<? extends MowzieEntity> type, World world) {
        super(type, world);
        this.experiencePoints = 30;
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MowzieEntity.createAttributes().add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 10)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 130)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 1)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 40);
    }

    public static double testRadiusAtHeight(double height) {
        return TEST_RADIUS_BOTTOM + Math.pow(Math.min(height / (double) TEST_MAX_RADIUS_HEIGHT, 1), TEST_RADIUS_FALLOFF) * (TEST_RADIUS - TEST_RADIUS_BOTTOM);
    }

    private static boolean canPayFor(ItemStack stack, ItemStack worth) {
        return stack.getItem() == worth.getItem() && stack.getCount() >= worth.getCount();
    }

    @Override
    public AbilityType getHurtAbility() {
        return HURT_ABILITY;
    }

    @Override
    public AbilityType getDeathAbility() {
        return DIE_ABILITY;
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(8, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(2, new UseAbilityAI<>(this, START_TEST, false));
        this.goalSelector.add(1, new UseAbilityAI<>(this, DIE_ABILITY));
        this.goalSelector.add(2, new UseAbilityAI<>(this, HURT_ABILITY, false));
        this.goalSelector.add(4, new RunTestGoal(this));
        this.goalSelector.add(3, new CombatBehaviorGoal(this));
        this.hurtByTargetAI = new RevengeGoal(this) {

            @Override
            public void start() {
                super.start();
                if (this.mob instanceof EntitySculptor sculptor) {
                    sculptor.setTestingPlayer(null);
                    sculptor.setCustomer(null);
                }
            }

            @Override
            public boolean canStart() {
                return super.canStart() && EntitySculptor.this.getHealthRatio() < 0.7;
            }

            @Override
            public boolean shouldContinue() {
                LivingEntity livingentity = this.mob.getTarget();
                if (livingentity == null) {
                    livingentity = this.target;
                }

                if (livingentity == null) {
                    return false;
                } else if (!this.mob.canTarget(livingentity)) {
                    return false;
                } else {
                    AbstractTeam team = this.mob.getScoreboardTeam();
                    AbstractTeam team1 = livingentity.getScoreboardTeam();
                    if (team != null && team1 == team) {
                        return false;
                    } else {
                        double d0 = this.getFollowRange();
                        double yDistMax = 20;
                        double yBase = this.mob.getY();
                        EntitySculptor sculptor = (EntitySculptor) this.mob;
                        if (sculptor.getPillar() != null) {
                            yBase = sculptor.getPillar().getY();
                        }
                        if (
                                this.mob.getPos().multiply(1, 0, 1).squaredDistanceTo(livingentity.getPos().multiply(1, 0, 1)) > d0 * d0 ||
                                        livingentity.getY() < yBase - yDistMax ||
                                        livingentity.getY() > this.mob.getY() + yDistMax
                        ) {
                            return false;
                        } else {
                            this.mob.setTarget(livingentity);
                            return true;
                        }
                    }
                }
            }
        };
        this.targetSelector.add(3, this.hurtByTargetAI);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        Item tradeItem = Registries.ITEM.get(new Identifier(ConfigHandler.COMMON.MOBS.UMVUTHI.whichItem));
        this.getDataTracker().startTracking(DESIRES, new ItemStack(tradeItem, ConfigHandler.COMMON.MOBS.UMVUTHI.howMany));
        this.getDataTracker().startTracking(IS_TRADING, false);
        this.getDataTracker().startTracking(TESTING_PLAYER, Optional.empty());
    }

    @Override
    protected <E extends GeoEntity> void loopingAnimations(AnimationState<E> event) {
        event.getController().transitionLength(10);
        if (this.isTestObstructed) {
            this.controller.setAnimation(TEST_OBSTRUCTED);
        } else {
            super.loopingAnimations(event);
        }
//        if (event.getController() instanceof MowzieAnimationController mowzieAnimationController) {
//            mowzieAnimationController.checkAndReloadAnims();
//        }
//        event.getController().setAnimation(RawAnimation.begin().thenLoop("attack_1"));
    }

    @Override
    public boolean handleFallDamage(float p_147187_, float p_147188_, DamageSource p_147189_) {
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
    public boolean canBePushedByEntity(Entity entity) {
        return false;
    }

    public ItemStack getDesires() {
        return this.getDataTracker().get(DESIRES);
    }

    public void setDesires(ItemStack stack) {
        this.getDataTracker().set(DESIRES, stack);
    }

    public boolean fulfillDesire(Slot input) {
        ItemStack desires = this.getDesires();
        if (canPayFor(input.getStack(), desires)) {
            input.takeStack(desires.getCount());
            return true;
        }
        return false;
    }

    @Override
    public boolean cannotDespawn() {
        return true;
    }

    @Override
    public void tick() {
        this.setVelocity(0, this.getVelocity().y, 0);
        super.tick();
        if (this.testingPlayer == null && this.getTestingPlayerID().isPresent()) {
            this.testingPlayer = this.getWorld().getPlayerByUuid(this.getTestingPlayerID().get());
        }

        if (this.testingPlayer != null) {
            this.getLookControl().lookAt(this.testingPlayer);
        } else if (this.customer != null) {
            this.getLookControl().lookAt(this.customer);
        }

        if (!this.testing) {
            this.checkTestObstructedAtHeight(this.obstructionTestHeight);

            int height = EntitySculptor.TEST_HEIGHT + 3;
            this.obstructionTestHeight = (this.obstructionTestHeight + 1) % height;
            if (this.obstructionTestHeight == 0) {
                this.isTestObstructed = this.isTestObstructedSoFar;
                this.isTestObstructedSoFar = false;
            }
        }
    }

    public boolean checkTestObstructed() {
        int height = EntitySculptor.TEST_HEIGHT + 3;
        for (int i = 0; i < height; i++) {
            this.checkTestObstructedAtHeight(i);
            if (this.isTestObstructed) return true;
        }
        return false;
    }

    private void checkTestObstructedAtHeight(int height) {
        BlockPos pos = this.getBlockPos();
        int radius = EntitySculptor.TEST_RADIUS;
        for (int i = -radius; i < radius; i++) {
            for (int j = -radius; j < radius; j++) {
                Vec2f offset = new Vec2f(i, j);
                BlockPos checkPos = pos.add((int) offset.x, height, (int) offset.y);
                double testRadius = testRadiusAtHeight(height);
                if (offset.lengthSquared() < testRadius * testRadius) {
                    if (!this.getWorld().getBlockState(checkPos).isAir()) {
                        this.isTestObstructed = true;
                        this.isTestObstructedSoFar = true;
                        if (this.getWorld().isClient())
                            this.executeClientCode(checkPos);
                    }
                }
            }
        }
    }

    @Environment(EnvType.CLIENT)
    private void executeClientCode(BlockPos checkPos) {
        if (this.isPlayerInTestZone(MinecraftClient.getInstance().player) && this.blockHasExposedSide(checkPos)) {
            MowziesMobs.PROXY.sculptorMarkBlock(this.getId(), checkPos);
            ParticleRotation.FaceCamera faceCamera = new ParticleRotation.FaceCamera(0);
            AdvancedParticleBase.spawnAlwaysVisibleParticle(this.getWorld(), ParticleHandler.RING2, 64, checkPos.getX() + 0.5, checkPos.getY() + 0.5, checkPos.getZ() + 0.5, 0, 0, 0, faceCamera, 3.5F, 0.83f, 1, 0.39f, 1, 1, 20, true, false, new ParticleComponent[]{
                    new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.ALPHA, ParticleComponent.KeyTrack.startAndEnd(0.7f, 0f), false),
                    new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.SCALE, ParticleComponent.KeyTrack.startAndEnd(0f, 16.0f), false)
            });
        }
    }

    private boolean blockHasExposedSide(BlockPos pos) {
        return !this.getWorld().getBlockState(pos.north()).isOpaque() ||
                !this.getWorld().getBlockState(pos.south()).isOpaque() ||
                !this.getWorld().getBlockState(pos.east()).isOpaque() ||
                !this.getWorld().getBlockState(pos.west()).isOpaque() ||
                !this.getWorld().getBlockState(pos.up()).isOpaque() ||
                !this.getWorld().getBlockState(pos.down()).isOpaque();
    }

    private void checkIfPlayerCheats() {
        if (this.testingPlayer == null) return;
        this.prevPlayerPosition = Optional.of(this.testingPlayer.getPos());
        if (!this.isTesting() || this.testingPlayer.isCreative()) return;

        // Check if player moved too far away
        if (this.testingPlayer != null && this.testingPlayer.getPos().multiply(1, 0, 1).distanceTo(this.getPos().multiply(1, 0, 1)) > TEST_RADIUS + 3) {
            this.playerCheated();
            return;
        }
        if (this.testingPlayer != null && this.pillar != null && this.testingPlayer.getY() < this.pillar.getY() - 10) {
            this.playerCheated();
            return;
        }

        // Check if testing player is flying
        if (this.testingPlayer != null && this.testingPlayer.getAbilities().flying) {
            this.playerCheated();
            return;
        }
        if (this.testingPlayer != null && !this.testingPlayer.isOnGround()) {
            double playerVelY = this.testingPlayer.getVelocity().getY();
            if (this.prevPlayerVelY != null && this.prevPlayerVelY.isPresent()) {
                double acceleration = playerVelY - this.prevPlayerVelY.get();
                if (acceleration >= 0.0) {
                    this.ticksAcceleratingUpward++;
                } else if (this.ticksAcceleratingUpward > 0) {
                    this.ticksAcceleratingUpward--;
                }
                if (this.ticksAcceleratingUpward > 5) {
                    this.playerCheated();
                    return;
                }
            }
            this.prevPlayerVelY = Optional.of(playerVelY);
        } else {
            this.ticksAcceleratingUpward = 0;
            this.prevPlayerVelY = Optional.empty();
        }

        // Check if testing player teleported
        if (this.testingPlayer != null) {
            Vec3d currPosition = this.testingPlayer.getPos();
            if (this.prevPlayerPosition != null && this.prevPlayerPosition.isPresent()) {
                if (currPosition.distanceTo(this.prevPlayerPosition.get()) > 3.0) {
                    this.playerCheated();
                }
            }
        }
    }

    public void playerCheated() {
        if (this.isTesting() && this.testingPlayer != null) {
            this.sendAbilityMessage(FAIL_TEST);
        }
    }

    public boolean isTrading() {
        return this.dataTracker.get(IS_TRADING);
    }

    public void setTrading(boolean trading) {
        this.dataTracker.set(IS_TRADING, trading);
    }

    public PlayerEntity getCustomer() {
        return this.customer;
    }

    public void setCustomer(PlayerEntity customer) {
        this.setTrading(customer != null);
        this.customer = customer;
    }

    public boolean isTesting() {
        return this.testing;
    }

    public Optional<UUID> getTestingPlayerID() {
        return this.getDataTracker().get(TESTING_PLAYER);
    }

    public void setTestingPlayerID(UUID playerID) {
        if (playerID == null) this.getDataTracker().set(TESTING_PLAYER, Optional.empty());
        else this.getDataTracker().set(TESTING_PLAYER, Optional.of(playerID));
    }

    public void setTestingPlayer(PlayerEntity testingPlayer) {
        this.testingPlayer = testingPlayer;
        this.setTestingPlayerID(testingPlayer == null ? null : testingPlayer.getUuid());
    }

    public void openGUI(PlayerEntity playerEntity) {
        this.setCustomer(playerEntity);
        MowziesMobs.PROXY.setReferencedMob(this);
        if (!this.getWorld().isClient && this.getTarget() == null && this.isAlive()) {
            playerEntity.openHandledScreen(new NamedScreenHandlerFactory() {
                @Override
                public ScreenHandler createMenu(int id, PlayerInventory playerInventory, PlayerEntity player) {
                    return new ContainerSculptorTrade(id, EntitySculptor.this, playerInventory);
                }

                @Override
                public Text getDisplayName() {
                    return EntitySculptor.this.getDisplayName();
                }
            });
        }
    }

    @Override
    protected ActionResult interactMob(PlayerEntity player, Hand hand) {
        if (this.isTesting()) {
            if (player == this.testingPlayer) this.sendAbilityMessage(PASS_TEST);
        } else {
            if (this.canTradeWith(player) && this.getTarget() == null && this.isAlive()) {
                this.openGUI(player);
                return ActionResult.SUCCESS;
            }
        }
        return ActionResult.PASS;
    }

    public boolean canTradeWith(PlayerEntity player) {
        return !this.isTrading() && !(this.getHealth() <= 0) && !this.testing;
    }

    public boolean doesItemSatisfyDesire(ItemStack stack) {
        return canPayFor(stack, this.getDesires());
    }

    public EntityPillar.EntityPillarSculptor getPillar() {
        return this.pillar;
    }

    public void setPillar(EntityPillar.EntityPillarSculptor pillar) {
        this.pillar = pillar;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        super.registerControllers(controllers);
    }

    @Override
    public AbilityType<?, ?>[] getAbilities() {
        return new AbilityType[]{START_TEST, FAIL_TEST, PASS_TEST, HURT_ABILITY, DIE_ABILITY, ATTACK_ABILITY};
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound compound) {
        super.writeCustomDataToNbt(compound);
        if (this.testingPlayer != null && this.getTestingPlayerID().isPresent()) {
            compound.putUuid("TestingPlayer", this.getTestingPlayerID().get());
            compound.putInt("NumLivePaths", this.numLivePaths);
        }
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound compound) {
        super.readCustomDataFromNbt(compound);
        if (compound.contains("TestingPlayer")) {
            this.testing = true;
            this.setTestingPlayerID(compound.getUuid("TestingPlayer"));
            this.numLivePaths = compound.getInt("NumLivePaths");
        }
    }

    public boolean isPlayerInTestZone(PlayerEntity player) {
        double yDistMax = 12;
        double yBase = this.getY();
        if (this.getPillar() != null) {
            yBase = this.getPillar().getY();
        }
        return player.getPos().multiply(1, 0, 1).squaredDistanceTo(this.getPos().multiply(1, 0, 1)) < (TEST_RADIUS * TEST_RADIUS + 9) &&
                player.getY() > yBase - yDistMax &&
                player.getY() < yBase + TEST_HEIGHT + yDistMax;
    }

    public static class StartTestAbility extends Ability<EntitySculptor> {
        private static final RawAnimation TEST_START_ANIM = RawAnimation.begin().then("testStart", Animation.LoopType.PLAY_ONCE);
        private static final int MAX_RANGE_TO_GROUND = 12;
        private BlockPos spawnPillarPos;
        private BlockState spawnPillarBlock;

        public StartTestAbility(AbilityType<EntitySculptor, StartTestAbility> abilityType, EntitySculptor user) {
            super(abilityType, user, new AbilitySection[]{
                    new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.STARTUP, 18),
                    new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.ACTIVE, 34)
            });
        }

        public static void placeStartingBoulders(EntitySculptor sculptor) {
            Random rand = sculptor.getRandom();
            int numStartBoulders = rand.nextBetweenExclusive(2, 5);
            float angleOffset = rand.nextFloat() * (float) (2f * Math.PI);
            for (int i = 0; i < numStartBoulders; i++) {
                float angleInc = (float) (2f * Math.PI) / ((float) numStartBoulders * 2f);
                float angle = angleOffset + angleInc * (i * 2) + rand.nextFloat() * angleInc;
                Vec3d spawnBoulderPos = sculptor.pillar.getPos().add(new Vec3d(rand.nextFloat() * 3 + 3, 0, 0).rotateY(angle));
                EntityBoulderSculptor boulderPlatform = new EntityBoulderSculptor(EntityHandler.BOULDER_SCULPTOR, sculptor.getWorld(), sculptor, Blocks.STONE.getDefaultState(), BlockPos.ORIGIN, EntityGeomancyBase.GeomancyTier.MEDIUM);
                boulderPlatform.setPosition(spawnBoulderPos.add(0, 1, 0));
                if (i == 0) boulderPlatform.setMainPath();
                sculptor.getWorld().spawnEntity(boulderPlatform);
            }
            sculptor.numLivePaths = numStartBoulders;
        }

        @Override
        public boolean tryAbility() {
            Vec3d from = this.getUser().getPos();
            Vec3d to = from.subtract(0, MAX_RANGE_TO_GROUND, 0);
            BlockHitResult result = this.getUser().getWorld().raycast(new RaycastContext(from, to, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, this.getUser()));
            if (result.getType() != HitResult.Type.MISS) {
                this.spawnPillarPos = result.getBlockPos();
                this.spawnPillarBlock = this.getUser().getWorld().getBlockState(this.spawnPillarPos);
                if (result.getSide() != Direction.UP) {
                    BlockState blockAbove = this.getUser().getWorld().getBlockState(this.spawnPillarPos.up());
                    return !blockAbove.shouldSuffocate(this.getUser().getWorld(), this.spawnPillarPos.up()) && !blockAbove.isAir();
                }
                return true;
            }
            return false;
        }

        @Override
        public void start() {
            super.start();
            this.playAnimation(TEST_START_ANIM);
        }

        @Override
        protected void beginSection(AbilitySection section) {
            super.beginSection(section);
            if (section.sectionType == AbilitySection.AbilitySectionType.ACTIVE && this.spawnPillarPos != null) {
//                if (!getUser().level().isClientSide()) {
//                    EntityBlockSwapper.EntityBlockSwapperSculptor swapper = new EntityBlockSwapper.EntityBlockSwapperSculptor(EntityHandler.BLOCK_SWAPPER_SCULPTOR.get(), getUser().level(), getUser().blockPosition(), Blocks.AIR.defaultBlockState(), 60, false, false);
//                    getUser().level().addFreshEntity(swapper);
//                }

                if (this.spawnPillarBlock == null || !EffectGeomancy.isBlockUseable(this.spawnPillarBlock))
                    this.spawnPillarBlock = Blocks.STONE.getDefaultState();
                this.getUser().pillar = new EntityPillar.EntityPillarSculptor(EntityHandler.PILLAR_SCULPTOR, this.getUser().getWorld(), this.getUser(), Blocks.STONE.getDefaultState(), this.spawnPillarPos);
                this.getUser().pillar.setTier(EntityGeomancyBase.GeomancyTier.SMALL);
                this.getUser().pillar.setPosition(this.spawnPillarPos.getX() + 0.5F, this.spawnPillarPos.getY() + 1, this.spawnPillarPos.getZ() + 0.5F);
                this.getUser().pillar.setDoRemoveTimer(false);
                if (this.getUser().pillar.checkCanSpawn()) {
                    this.getUser().getWorld().spawnEntity(this.getUser().pillar);
                }

                placeStartingBoulders(this.getUser());
            }
        }

        @Override
        public void tickUsing() {
            super.tickUsing();
            if (this.getCurrentSection().sectionType == AbilitySection.AbilitySectionType.ACTIVE) {
                List<LivingEntity> livingEntities = this.getUser().getEntityLivingBaseNearby(5, 5, 5, 5);
                for (LivingEntity livingEntity : livingEntities) {
                    Vec3d userPos = this.getUser().getPos().multiply(1, 0, 1);
                    Vec3d entityPos = livingEntity.getPos().multiply(1, 0, 1);
                    Vec3d vec = userPos.subtract(entityPos).normalize().multiply(-Math.min(1.0 / userPos.squaredDistanceTo(entityPos), 2));
                    livingEntity.addVelocity(vec.x, vec.y, vec.z);
                }

//                if (!getUser().level().isClientSide() && getUser().pillar != null) {
//                    getUser().setPos(getUser().pillar.position().add(0, getUser().pillar.getHeight(), 0));
//                }
//
//                if (getUser().pillar != null && getUser().pillar.getHeight() >= TEST_HEIGHT) {
//                    nextSection();
//                }
            }
        }

        @Override
        public boolean canCancelActiveAbility() {
            return this.getUser().getActiveAbilityType() == HURT_ABILITY;
        }
    }

    public static abstract class EndTestAbility extends Ability<EntitySculptor> {

        private static final RawAnimation TEST_FAIL_START_ANIM = RawAnimation.begin().then("test_fail_start", Animation.LoopType.PLAY_ONCE);

        public EndTestAbility(AbilityType<EntitySculptor, ? extends EndTestAbility> abilityType, EntitySculptor user, int recoveryDuration) {
            super(abilityType, user, new AbilitySection[]{
                    new AbilitySection.AbilitySectionInfinite(AbilitySection.AbilitySectionType.ACTIVE),
                    new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.RECOVERY, recoveryDuration)
            });
        }

        @Override
        public boolean tryAbility() {
            return this.getUser().pillar != null;
        }

        @Override
        public void start() {
            super.start();
            this.playAnimation(TEST_FAIL_START_ANIM);
            if (this.getUser().pillar != null) this.getUser().pillar.startFalling();
            this.getUser().testing = false;
        }

        @Override
        public void tickUsing() {
            super.tickUsing();
            if (this.getCurrentSection().sectionType == AbilitySection.AbilitySectionType.ACTIVE) {
                if (!this.getUser().getWorld().isClient() && this.getUser().pillar != null) {
                    this.getUser().setPosition(this.getUser().pillar.getPos().add(0, this.getUser().pillar.getHeight(), 0));
                }
                if (this.getUser().pillar == null || this.getUser().pillar.isRemoved()) {
                    AbilityHandler.INSTANCE.sendJumpToSectionMessage(this.getUser(), this.getAbilityType(), 1);
                }
            }
        }

        @Override
        protected boolean canContinueUsing() {
            return super.canContinueUsing() && this.getUser().getTarget() == null;
        }

        @Override
        public void end() {
            super.end();
            if (this.getUser() != null) {
                this.getUser().pillar = null;
                this.getUser().setTestingPlayer(null);
                this.getUser().testing = false;
                this.getUser().numLivePaths = 0;
            }
        }

        @Override
        public boolean canCancelActiveAbility() {
            return true;
        }

        @Override
        protected void beginSection(AbilitySection section) {
            super.beginSection(section);
            if (section.sectionType == AbilitySection.AbilitySectionType.RECOVERY) {
                this.playFinishingAnimation();
            }
        }

        protected abstract void playFinishingAnimation();
    }

    public static class FailTestAbility extends EndTestAbility {
        private static final RawAnimation TEST_FAIL_END = RawAnimation.begin().thenLoop("test_fail_end");

        public FailTestAbility(AbilityType<EntitySculptor, ? extends EndTestAbility> abilityType, EntitySculptor user) {
            super(abilityType, user, 30);
        }

        @Override
        protected void playFinishingAnimation() {
            this.playAnimation(TEST_FAIL_END);
        }
    }

    public static class PassTestAbility extends EndTestAbility {

        private static final RawAnimation TEST_PASS_END = RawAnimation.begin().thenLoop("test_pass_end");

        public PassTestAbility(AbilityType<EntitySculptor, PassTestAbility> abilityType, EntitySculptor user) {
            super(abilityType, user, 120);
        }

        @Override
        public void start() {
            if (this.getUser().testingPlayer != null) {
                List<EntityBoulderSculptor> platforms = this.getUser().getWorld().getNonSpectatingEntities(EntityBoulderSculptor.class, this.getUser().testingPlayer.getBoundingBox().stretch(0, -6, 0));
                EntityBoulderSculptor platformBelowPlayer = platforms.get(0);
                platformBelowPlayer.descend();
            }
            super.start();
        }

        @Override
        public void end() {
            super.end();
            this.getUser().dropItem(ItemHandler.EARTHREND_GAUNTLET);
        }

        @Override
        public boolean canCancelActiveAbility() {
            return false;
        }

        @Override
        protected void playFinishingAnimation() {
            this.playAnimation(TEST_PASS_END);
        }
    }

    public static class AttackAbility extends Ability<EntitySculptor> {
        private static final int STARTUP_TIME = 3;
        private static final RawAnimation ATTACK_START = RawAnimation.begin().thenLoop("attack_1");
        private EntityBoulderSculptor boulderToFire;
        private Vec3d prevTargetPos;

        public AttackAbility(AbilityType abilityType, EntitySculptor user) {
            super(abilityType, user, new AbilitySection[]{
                    new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.STARTUP, STARTUP_TIME),
                    new AbilitySection.AbilitySectionInstant(AbilitySection.AbilitySectionType.ACTIVE),
                    new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.RECOVERY, 5)
            });
        }

        @Override
        public void start() {
            super.start();
            this.playAnimation(ATTACK_START);
        }

        @Override
        protected void beginSection(AbilitySection section) {
            super.beginSection(section);
            if (!this.getUser().getWorld().isClient() && this.getUser().getTarget() != null) {
                LivingEntity target = this.getUser().getTarget();
                Collections.shuffle(this.getUser().boulders);
                if (section.sectionType == AbilitySection.AbilitySectionType.STARTUP) {
                    for (EntityBoulderSculptor boulder : this.getUser().boulders) {
                        if (boulder.isRemoved()) continue;
                        if (!boulder.isFinishedRising()) continue;
                        if (!boulder.active) continue;
                        Vec3d vecBetweenSculptorAndTarget = this.getUser().getTarget().getPos().subtract(this.getUser().getPos()).normalize();
                        Vec3d vecBetweenSculptorAndBoulder = boulder.getPos().subtract(this.getUser().getPos()).normalize();
                        if (vecBetweenSculptorAndBoulder.dotProduct(vecBetweenSculptorAndTarget) > 0.5) {
                            this.boulderToFire = boulder;
                            break;
                        }
                    }
                    if (this.boulderToFire == null)
                        AbilityHandler.INSTANCE.sendInterruptAbilityMessage(this.getUser(), ATTACK_ABILITY);
                    this.prevTargetPos = target.getPos().add(0, target.getHeight() / 2.0, 0);
                }
                if (section.sectionType == AbilitySection.AbilitySectionType.ACTIVE) {
                    Vec3d targetPos = target.getPos().add(0, target.getHeight() / 2.0, 0);
                    double timeToReach = this.boulderToFire.getPos().subtract(targetPos).length() / this.boulderToFire.getSpeed();
                    Vec3d targetMovement = targetPos.subtract(this.prevTargetPos).multiply(timeToReach * 0.93 * 1.0 / 4.0);
                    targetMovement = targetMovement.multiply(1, 0, 1);
                    Vec3d futureTargetPos = targetPos.add(targetMovement);
                    Vec3d projectileMid = this.boulderToFire.getPos().add(0, this.boulderToFire.getHeight() / 2.0, 0);
                    Vec3d shootVec = futureTargetPos.subtract(projectileMid).normalize();
                    this.boulderToFire.shoot(shootVec.multiply(this.boulderToFire.getSpeed()));
                    this.getUser().boulders.remove(this.boulderToFire);
                    this.boulderToFire = null;
                }
            }
        }
    }

    public static class RunTestGoal extends Goal {

        private final EntitySculptor sculptor;

        RunTestGoal(EntitySculptor sculptor) {
            this.sculptor = sculptor;
            this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
        }

        @Override
        public boolean canStart() {
            return this.sculptor.testingPlayer != null;
        }

        @Override
        public void start() {
            super.start();
            this.sculptor.testing = true;
            this.sculptor.sendAbilityMessage(START_TEST);
        }

        @Override
        public void tick() {
            super.tick();
            if (this.sculptor.testingPlayer == null) {
                this.sculptor.sendAbilityMessage(FAIL_TEST);
                this.sculptor.prevPlayerPosition = Optional.empty();
                this.sculptor.prevPlayerVelY = Optional.empty();
            } else if (this.sculptor.testing) {
                this.sculptor.checkIfPlayerCheats();
            }
        }

        @Override
        public void stop() {
            super.stop();
            this.sculptor.setTestingPlayer(null);
            this.sculptor.testing = false;
        }
    }

    public static class CombatBehaviorGoal extends Goal {
        private final EntitySculptor sculptor;

        public CombatBehaviorGoal(EntitySculptor sculptor) {
            this.sculptor = sculptor;
            this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
        }

        @Override
        public boolean canStart() {
            LivingEntity target = this.sculptor.getTarget();
            return target != null && target.isAlive();
        }

        @Override
        public void start() {
            super.start();
            if (this.sculptor.getPillar() != null && this.sculptor.getPillar().getHeight() < TEST_HEIGHT) {
                this.sculptor.getPillar().startRising();
            } else {
                this.sculptor.sendAbilityMessage(START_TEST);
            }
        }

        @Override
        public void tick() {
            super.tick();
            if (this.sculptor.getActiveAbility() == null) {
                AbilityHandler.INSTANCE.sendAbilityMessage(this.sculptor, ATTACK_ABILITY);
            }
            if (this.sculptor.boulders.isEmpty() && this.sculptor.getActiveAbilityType() != START_TEST) {
                StartTestAbility.placeStartingBoulders(this.sculptor);
            }
        }

        @Override
        public void stop() {
            super.stop();
            this.sculptor.sendAbilityMessage(FAIL_TEST);
        }
    }
}

package com.bobmowzie.mowziesmobs.server.entity.umvuthana;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.ai.EntityAIUmvuthanaTrade;
import com.bobmowzie.mowziesmobs.server.ai.EntityAIUmvuthanaTradeLook;
import com.bobmowzie.mowziesmobs.server.ai.UmvuthanaHurtByTargetAI;
import com.bobmowzie.mowziesmobs.server.block.BlockHandler;
import com.bobmowzie.mowziesmobs.server.entity.LeaderSunstrikeImmune;
import com.bobmowzie.mowziesmobs.server.entity.umvuthana.trade.Trade;
import com.bobmowzie.mowziesmobs.server.entity.umvuthana.trade.TradeStore;
import com.bobmowzie.mowziesmobs.server.inventory.ContainerUmvuthanaTrade;
import com.bobmowzie.mowziesmobs.server.item.UmvuthanaMask;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.GoToWalkTargetGoal;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.ServerConfigHandler;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Difficulty;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

public class EntityUmvuthanaMinion extends EntityUmvuthana implements LeaderSunstrikeImmune, Monster {
    private static final TradeStore DEFAULT = new TradeStore.Builder()
            .addTrade(Items.GOLD_NUGGET, 4, BlockHandler.CLAWED_LOG.asItem(), 1, 9)
            .addTrade(Items.GOLD_NUGGET, 7, BlockHandler.CLAWED_LOG.asItem(), 2, 9)
            .addTrade(Items.GOLD_NUGGET, 5, Items.COOKED_CHICKEN, 2, 2)
            .addTrade(Items.GOLD_NUGGET, 4, Items.COOKED_CHICKEN, 1, 2)
            .addTrade(Items.GOLD_NUGGET, 7, Items.COOKED_PORKCHOP, 2, 2)
            .addTrade(Items.GOLD_NUGGET, 4, Items.COOKED_PORKCHOP, 1, 2)
            .addTrade(Items.GOLD_NUGGET, 1, Items.FEATHER, 4, 2)
            .addTrade(Items.GOLD_NUGGET, 1, Items.STICK, 12, 2)
            .addTrade(Items.GOLD_NUGGET, 3, Items.CAMPFIRE, 1, 2)

            .addTrade(Items.MELON_SLICE, 3, Items.GOLD_NUGGET, 5, 2)
            .addTrade(Items.CHICKEN, 1, Items.GOLD_NUGGET, 3, 2)
            .addTrade(Items.CHICKEN, 1, Items.GOLD_NUGGET, 4, 2)
            .addTrade(Items.PORKCHOP, 1, Items.GOLD_NUGGET, 6, 2)
            .addTrade(Items.BEETROOT, 3, Items.GOLD_NUGGET, 6, 2)
            .addTrade(Items.SALMON, 2, Items.GOLD_NUGGET, 8, 1)
            .addTrade(Items.COD, 2, Items.GOLD_NUGGET, 7, 1)
            .addTrade(Items.FLINT, 2, Items.FEATHER, 5, 2)
            .addTrade(Items.FEATHER, 5, Items.FLINT, 2, 2)
            .addTrade(Items.ACACIA_SAPLING, 2, Items.GOLD_NUGGET, 4, 2)
            .addTrade(Items.BONE, 3, Items.GOLD_NUGGET, 4, 1)
            .addTrade(Items.BONE, 2, Items.GOLD_NUGGET, 2, 1)
            .build();

    private static final TrackedData<Optional<Trade>> TRADE = DataTracker.registerData(EntityUmvuthanaMinion.class, MowziesMobs.OPTIONAL_TRADE);
    //    private static final DataParameter<Integer> NUM_SALES = EntityDataManager.createKey(EntityBarakoaya.class, DataSerializers.VARINT);
    private static final TrackedData<Optional<UUID>> MISBEHAVED_PLAYER = DataTracker.registerData(EntityUmvuthanaMinion.class, TrackedDataHandlerRegistry.OPTIONAL_UUID);
    private static final TrackedData<Boolean> IS_TRADING = DataTracker.registerData(EntityUmvuthanaMinion.class, TrackedDataHandlerRegistry.BOOLEAN);

    //TODO: Sale limits. After X sales, go out of stock and change trade.

    private static final int MIN_OFFER_TIME = 5 * 60 * 20;

    private static final int MAX_OFFER_TIME = 20 * 60 * 20;

    private TradeStore tradeStore = TradeStore.EMPTY;

    private int timeOffering;

//    private static final int SOLD_OUT_TIME = 5 * 60 * 20;
//    private static final int MAX_SALES = 5;

    private PlayerEntity customer;

    public EntityUmvuthanaMinion(EntityType<? extends EntityUmvuthanaMinion> type, World world) {
        super(type, world);
        this.setWeapon(0);
//        setNumSales(MAX_SALES);
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(1, new EntityAIUmvuthanaTrade(this));
        this.goalSelector.add(1, new EntityAIUmvuthanaTradeLook(this));
        this.goalSelector.add(7, new GoToWalkTargetGoal(this, 0.4));
    }

    @Override
    protected void registerTargetGoals() {
        this.targetSelector.add(3, new UmvuthanaHurtByTargetAI(this, true));
        this.targetSelector.add(4, new ActiveTargetGoal<PlayerEntity>(this, PlayerEntity.class, 0, true, true, target -> {
            if (target instanceof PlayerEntity) {
                if (this.getWorld().getDifficulty() == Difficulty.PEACEFUL) return false;
                ItemStack headArmorStack = ((PlayerEntity) target).getInventory().armor.get(3);
                return !(headArmorStack.getItem() instanceof UmvuthanaMask) || target == this.getMisbehavedPlayer();
            }
            return true;
        }) {
            @Override
            public void stop() {
                super.stop();
                EntityUmvuthanaMinion.this.setMisbehavedPlayerId(null);
            }
        });
        this.targetSelector.add(5, new ActiveTargetGoal<>(this, ZombieEntity.class, 0, true, true, null));
        this.targetSelector.add(5, new ActiveTargetGoal<>(this, AbstractSkeletonEntity.class, 0, true, false, null));
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.getDataTracker().startTracking(TRADE, Optional.empty());
        this.dataTracker.startTracking(MISBEHAVED_PLAYER, Optional.empty());
        this.dataTracker.startTracking(IS_TRADING, false);
//        getDataManager().register(NUM_SALES, MAX_SALES);
    }

    public Trade getOfferingTrade() {
        return this.getDataTracker().get(TRADE).orElse(null);
    }

    public boolean isOfferingTrade() {
        if (this.getDataTracker().get(TRADE) instanceof Optional) {
            return this.getDataTracker().get(TRADE).isPresent();
        } else return false;
    }

    //    public int getNumSales() {
//        return getDataManager().get(NUM_SALES);
//    }
//
//    public void setNumSales(int numSales) {
//        getDataManager().set(NUM_SALES, numSales);
//    }

    public void setOfferingTrade(Trade trade) {
        this.getDataTracker().set(TRADE, Optional.ofNullable(trade));
    }

    public PlayerEntity getCustomer() {
        return this.customer;
    }

    public void setCustomer(PlayerEntity customer) {
        this.setTrading(customer != null);
        this.customer = customer;
    }

    public boolean isTrading() {
        return this.dataTracker.get(IS_TRADING);
    }

    public void setTrading(boolean trading) {
        this.dataTracker.set(IS_TRADING, trading);
    }

    protected boolean canHoldVaryingWeapons() {
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getTarget() instanceof PlayerEntity) {
            if (((PlayerEntity) this.getTarget()).isCreative() || this.getTarget().isSpectator()) this.setTarget(null);
        }
        if ((!this.isOfferingTrade() || this.timeOffering <= 0) && this.tradeStore.hasStock()) {
            this.setOfferingTrade(this.tradeStore.get(this.random));
            this.timeOffering = this.random.nextInt(MAX_OFFER_TIME - MIN_OFFER_TIME + 1) + MIN_OFFER_TIME;
        }
    }

    public void openGUI(PlayerEntity playerEntity) {
        this.setCustomer(playerEntity);
        MowziesMobs.PROXY.setReferencedMob(this);
        if (!this.getWorld().isClient && this.getTarget() == null && this.isAlive()) {
            playerEntity.openHandledScreen(new NamedScreenHandlerFactory() {
                @Override
                public ScreenHandler createMenu(int id, PlayerInventory playerInventory, PlayerEntity player) {
                    return new ContainerUmvuthanaTrade(id, EntityUmvuthanaMinion.this, playerInventory);
                }

                @Override
                public Text getDisplayName() {
                    return EntityUmvuthanaMinion.this.getDisplayName();
                }
            });
        }
    }

    @Override
    protected ActionResult interactMob(PlayerEntity player, Hand hand) {
        if (this.canTradeWith(player) && this.getTarget() == null && this.isAlive()) {
            this.openGUI(player);
            return ActionResult.success(this.getWorld().isClient);
        }
        return ActionResult.PASS;
    }

    public boolean canTradeWith(PlayerEntity player) {
        if (this.isTrading()) {
            return false;
        }
        ItemStack headStack = player.getInventory().armor.get(3);
        return headStack.getItem() instanceof UmvuthanaMask && this.isOfferingTrade();
    }

    @Nullable
    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason reason, @Nullable EntityData livingData, @Nullable NbtCompound compound) {
        this.tradeStore = DEFAULT;
        if (reason == SpawnReason.COMMAND) this.setPositionTarget(this.getBlockPos(), 25);
        return super.initialize(world, difficulty, reason, livingData, compound);
    }

    @Override
    public boolean cannotDespawn() {
        return true;
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound compound) {
        super.writeCustomDataToNbt(compound);
        compound.put("tradeStore", this.tradeStore.serialize());
        if (this.isOfferingTrade()) {
            compound.put("offeringTrade", this.getOfferingTrade().serialize());
        }
        compound.putInt("timeOffering", this.timeOffering);
        compound.putInt("HomePosX", this.getPositionTarget().getX());
        compound.putInt("HomePosY", this.getPositionTarget().getY());
        compound.putInt("HomePosZ", this.getPositionTarget().getZ());
        compound.putInt("HomeDist", (int) this.getPositionTargetRange());
        if (this.getMisbehavedPlayerId() != null) {
            compound.putUuid("MisbehavedPlayer", this.getMisbehavedPlayerId());
        }
//        compound.setInteger("numSales", getNumSales());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound compound) {
        super.readCustomDataFromNbt(compound);
        this.tradeStore = TradeStore.deserialize(compound.getCompound("tradeStore"));
        this.setOfferingTrade(Trade.deserialize(compound.getCompound("offeringTrade")));
        this.timeOffering = compound.getInt("timeOffering");
        int i = compound.getInt("HomePosX");
        int j = compound.getInt("HomePosY");
        int k = compound.getInt("HomePosZ");
        int dist = compound.getInt("HomeDist");
        this.setPositionTarget(new BlockPos(i, j, k), dist);
        UUID uuid;
        if (compound.containsUuid("MisbehavedPlayer")) {
            uuid = compound.getUuid("MisbehavedPlayer");
        } else {
            String s = compound.getString("MisbehavedPlayer");
            uuid = ServerConfigHandler.getPlayerUuidByName(this.getServer(), s);
        }

        if (uuid != null) {
            try {
                this.setMisbehavedPlayerId(uuid);
            } catch (Throwable ignored) {

            }
        }
//        setNumSales(compound.getInteger("numSales"));
    }

    @Nullable
    public UUID getMisbehavedPlayerId() {
        return this.dataTracker.get(MISBEHAVED_PLAYER).orElse(null);
    }

    public void setMisbehavedPlayerId(@Nullable UUID p_184754_1_) {
        this.dataTracker.set(MISBEHAVED_PLAYER, Optional.ofNullable(p_184754_1_));
    }

    @Nullable
    public LivingEntity getMisbehavedPlayer() {
        try {
            UUID uuid = this.getMisbehavedPlayerId();
            return uuid == null ? null : this.getWorld().getPlayerByUuid(uuid);
        } catch (IllegalArgumentException illegalargumentexception) {
            return null;
        }
    }
}

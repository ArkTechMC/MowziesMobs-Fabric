package com.bobmowzie.mowziesmobs;

import com.bobmowzie.mowziesmobs.client.ClientProxy;
import com.bobmowzie.mowziesmobs.client.model.tools.MowzieModelFactory;
import com.bobmowzie.mowziesmobs.client.particle.ParticleHandler;
import com.bobmowzie.mowziesmobs.event.ItemEvents;
import com.bobmowzie.mowziesmobs.event.LivingEvents;
import com.bobmowzie.mowziesmobs.event.PlayerEvents;
import com.bobmowzie.mowziesmobs.server.ServerEventHandler;
import com.bobmowzie.mowziesmobs.server.ServerProxy;
import com.bobmowzie.mowziesmobs.server.ability.AbilityCommonEventHandler;
import com.bobmowzie.mowziesmobs.server.advancement.AdvancementHandler;
import com.bobmowzie.mowziesmobs.server.block.BlockHandler;
import com.bobmowzie.mowziesmobs.server.block.entity.BlockEntityHandler;
import com.bobmowzie.mowziesmobs.server.creativetab.CreativeTabHandler;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.umvuthana.trade.Trade;
import com.bobmowzie.mowziesmobs.server.inventory.ContainerHandler;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import com.bobmowzie.mowziesmobs.server.loot.LootTableHandler;
import com.bobmowzie.mowziesmobs.server.message.ServerNetworkHelper;
import com.bobmowzie.mowziesmobs.server.potion.EffectHandler;
import com.bobmowzie.mowziesmobs.server.potion.PotionTypeHandler;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import com.bobmowzie.mowziesmobs.server.world.feature.structure.StructureTypeHandler;
import com.bobmowzie.mowziesmobs.server.world.feature.structure.jigsaw.JigsawHandler;
import com.bobmowzie.mowziesmobs.server.world.feature.structure.processor.ProcessorHandler;
import com.bobmowzie.mowziesmobs.server.world.spawn.SpawnHandler;
import com.iafenvoy.uranus.event.EntityEvents;
import com.iafenvoy.uranus.event.LivingEntityEvents;
import io.github.fabricators_of_create.porting_lib.entity.events.CriticalHitEvent;
import io.github.fabricators_of_create.porting_lib.entity.events.EntityMountEvents;
import io.github.fabricators_of_create.porting_lib.event.common.BlockEvents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.*;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandler;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.bernie.geckolib.GeckoLib;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Optional;
import java.util.function.Supplier;

public final class MowziesMobs implements ModInitializer {
    public static final String MODID = "mowziesmobs";
    public static final Logger LOGGER = LogManager.getLogger();
    public static ServerProxy PROXY;
    public static final TrackedDataHandler<Optional<Trade>> OPTIONAL_TRADE = new TrackedDataHandler<>() {
        @Override
        public void write(PacketByteBuf buf, Optional<Trade> value) {
            if (value.isPresent()) {
                Trade trade = value.get();
                buf.writeItemStack(trade.getInput());
                buf.writeItemStack(trade.getOutput());
                buf.writeInt(trade.getWeight());
            } else {
                buf.writeItemStack(ItemStack.EMPTY);
            }
        }

        @Override
        public Optional<Trade> read(PacketByteBuf buf) {
            ItemStack input = buf.readItemStack();
            if (input == ItemStack.EMPTY) {
                return Optional.empty();
            }
            return Optional.of(new Trade(input, buf.readItemStack(), buf.readInt()));
        }

        @Override
        public TrackedData<Optional<Trade>> create(int id) {
            return new TrackedData<>(id, this);
        }

        @Override
        public Optional<Trade> copy(Optional<Trade> value) {
            return value.map(Trade::new);
        }
    };

    @Override
    public void onInitialize() {
        GeckoLibUtil.addCustomBakedModelFactory(MODID, new MowzieModelFactory());
        GeckoLib.initialize();

        PROXY = (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT ? (Supplier<ServerProxy>) ClientProxy::new : (Supplier<ServerProxy>) ServerProxy::new).get();
        BlockHandler.init();
        EntityHandler.init();
        ItemHandler.init();
        MMSounds.init();
        BlockEntityHandler.init();
        ParticleHandler.init();
        StructureTypeHandler.init();
        ContainerHandler.init();
        EffectHandler.init();
        PotionTypeHandler.init();
        SpawnHandler.addBiomeSpawns();
        LootTableHandler.init();
        CreativeTabHandler.init();

        SpawnHandler.registerSpawnPlacementTypes();
        AdvancementHandler.preInit();
        JigsawHandler.registerJigsawElements();
        ProcessorHandler.registerStructureProcessors();

        AbilityCommonEventHandler.register();

        ServerNetworkHelper.register();
        LivingEntityEvents.DAMAGE.register(ServerEventHandler::onLivingHurt);
        EntityEvents.ON_JOIN_WORLD.register(ServerEventHandler::onJoinWorld);
        LivingEvents.AFTER_TICK.register(ServerEventHandler::onLivingTick);
        LivingEvents.ADD_EFFECT.register(ServerEventHandler::onAddPotionEffect);
        LivingEvents.REMOVE_EFFECT.register(ServerEventHandler::onRemovePotionEffect);
        PlayerEvents.AFTER_TICK.register(ServerEventHandler::onPlayerTick);
        BlockEvents.POST_PROCESS_PLACE.register(ServerEventHandler::onPlaceBlock);
        BlockEvents.BLOCK_BREAK.register(ServerEventHandler::onBreakBlock);
        UseEntityCallback.EVENT.register(ServerEventHandler::onPlayerInteractEntity);
        UseBlockCallback.EVENT.register(ServerEventHandler::onPlayerInteractBlock);
        LivingEntityEvents.DAMAGE.register(ServerEventHandler::onLivingDamage);
        UseItemCallback.EVENT.register(ServerEventHandler::onPlayerUseItem);
        AttackBlockCallback.EVENT.register(ServerEventHandler::onPlayerLeftClickBlock);
        io.github.fabricators_of_create.porting_lib.entity.events.LivingEntityEvents.LivingJumpEvent.JUMP.register(ServerEventHandler::onLivingJump);
        AttackEntityCallback.EVENT.register(ServerEventHandler::onPlayerAttack);
        CriticalHitEvent.CRITICAL_HIT.register(ServerEventHandler::checkCritEvent);
        EntityMountEvents.MOUNT.register(ServerEventHandler::onRideEntity);
        PlayerEvents.RESPAWN.register(ServerEventHandler::onPlayerRespawn);
        ItemEvents.FILL_BUCKET.register(ServerEventHandler::onFillBucket);

        TrackedDataHandlerRegistry.register(OPTIONAL_TRADE);
    }
}

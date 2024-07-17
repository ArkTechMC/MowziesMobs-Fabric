package com.bobmowzie.mowziesmobs.client;

import com.bobmowzie.mowziesmobs.client.render.block.SculptorBlockMarking;
import com.bobmowzie.mowziesmobs.client.render.entity.FrozenRenderHandler;
import com.bobmowzie.mowziesmobs.client.sound.*;
import com.bobmowzie.mowziesmobs.server.ServerProxy;
import com.bobmowzie.mowziesmobs.server.ability.AbilityClientEventHandler;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntitySolarBeam;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntitySunstrike;
import com.bobmowzie.mowziesmobs.server.entity.naga.EntityNaga;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.ModelPredicateProvider;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.particle.BlockDustParticle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.*;

@Environment(EnvType.CLIENT)
public class ClientProxy extends ServerProxy {
    public static final Map<UUID, Identifier> bossBarRegistryNames = new HashMap<>();
    public static final Long2ObjectMap<SculptorBlockMarking> sculptorMarkedBlocks = new Long2ObjectOpenHashMap<>();
    private static final List<SunblockSound> sunblockSounds = new ArrayList<>();
    private Entity referencedMob = null;

    @Override
    public void init(final IEventBus modbus) {
        super.init(modbus);

        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ConfigHandler.CLIENT_CONFIG);

        modbus.register(MMModels.class);
        MinecraftForge.EVENT_BUS.register(ClientEventHandler.INSTANCE);
        MinecraftForge.EVENT_BUS.register(FrozenRenderHandler.INSTANCE);
        MinecraftForge.EVENT_BUS.register(AbilityClientEventHandler.INSTANCE);

        FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientLayerRegistry::onAddLayers);
    }

    @Override
    public void onLateInit(final IEventBus modbus) {
        ModelPredicateProvider pulling = ModelPredicateProviderRegistry.get(Items.BOW, new Identifier("pulling"));
        ModelPredicateProviderRegistry.register(ItemHandler.BLOWGUN.get().asItem(), new Identifier("pulling"), pulling);
    }

    @Override
    public void playSunstrikeSound(EntitySunstrike strike) {
        MinecraftClient.getInstance().getSoundManager().play(new SunstrikeSound(strike));
    }

    @Override
    public void playIceBreathSound(Entity entity) {
        MinecraftClient.getInstance().getSoundManager().play(new IceBreathSound(entity));
    }

    @Override
    public void playBoulderChargeSound(LivingEntity player) {
        MinecraftClient.getInstance().getSoundManager().play(new SpawnBoulderChargeSound(player));
    }

    @Override
    public void playNagaSwoopSound(EntityNaga naga) {
        MinecraftClient.getInstance().getSoundManager().play(new NagaSwoopSound(naga));
    }

    @Override
    public void playBlackPinkSound(AbstractMinecartEntity entity) {
        MinecraftClient.getInstance().getSoundManager().play(new BlackPinkSound(entity));
    }

    @Override
    public void playSunblockSound(LivingEntity entity) {
        if (ConfigHandler.CLIENT.doUmvuthanaCraneHealSound.get()) {
            sunblockSounds.removeIf(e -> e == null || e.isDone());
            if (sunblockSounds.size() < 10) {
                SunblockSound sunblockSound = new SunblockSound(entity);
                sunblockSounds.add(sunblockSound);
                try {
                    MinecraftClient.getInstance().getSoundManager().play(sunblockSound);
                } catch (ConcurrentModificationException ignored) {

                }
            }
        }
    }

    @Override
    public void playSolarBeamSound(EntitySolarBeam entity) {
        MinecraftClient.getInstance().getSoundManager().play(new SolarBeamSound(entity, false));
        MinecraftClient.getInstance().getSoundManager().play(new SolarBeamSound(entity, true));
    }

    @Override
    public void minecartParticles(ClientWorld world, AbstractMinecartEntity minecart, float scale, double x, double y, double z, BlockState state, BlockPos pos) {
        final int size = 3;
        float offset = -0.5F * scale;
        for (int ix = 0; ix < size; ix++) {
            for (int iy = 0; iy < size; iy++) {
                for (int iz = 0; iz < size; iz++) {
                    double dx = (double) ix / size * scale;
                    double dy = (double) iy / size * scale;
                    double dz = (double) iz / size * scale;
                    Vec3d minecartMotion = minecart.getVelocity();
                    MinecraftClient.getInstance().particleManager.addParticle(new BlockDustParticle(
                            world,
                            x + dx + offset, y + dy + offset, z + dz + offset,
                            dx + minecartMotion.getX(), dy + minecartMotion.getY(), dz + minecartMotion.getZ(),
                            state
                    ) {
                    }.updateSprite(state, pos));
                }
            }
        }
    }

    public void setTPS(float tickRate) {

    }

    public Entity getReferencedMob() {
        return this.referencedMob;
    }

    public void setReferencedMob(Entity referencedMob) {
        this.referencedMob = referencedMob;
    }

    public void sculptorMarkBlock(int id, BlockPos pos) {
        SculptorBlockMarking blockMarking = sculptorMarkedBlocks.get(pos.asLong());
        if (blockMarking == null) {
            blockMarking = new SculptorBlockMarking(pos);
            sculptorMarkedBlocks.put(pos.asLong(), blockMarking);
        } else {
            blockMarking.resetTick();
        }
    }

    public void updateMarkedBlocks() {
        Iterator<SculptorBlockMarking> iterator = sculptorMarkedBlocks.values().iterator();

        while (iterator.hasNext()) {
            SculptorBlockMarking blockMarking = iterator.next();
            int i = blockMarking.getTicks();
            if (i > blockMarking.getDuration()) {
                iterator.remove();
            }
            blockMarking.tick();
        }
    }
}

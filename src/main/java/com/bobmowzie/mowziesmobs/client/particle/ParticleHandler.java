package com.bobmowzie.mowziesmobs.client.particle;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.particle.util.AdvancedParticleBase;
import com.bobmowzie.mowziesmobs.client.particle.util.AdvancedParticleData;
import com.bobmowzie.mowziesmobs.client.particle.util.DecalParticleData;
import com.bobmowzie.mowziesmobs.client.particle.util.RibbonParticleData;
import com.mojang.serialization.Codec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;


public class ParticleHandler {
    public static final DefaultParticleType SPARKLE = register("sparkle", false);
    public static final ParticleType<AdvancedParticleData> RING2 = register("ring", AdvancedParticleData.DESERIALIZER);    public static final ParticleType<ParticleVanillaCloudExtended.VanillaCloudData> VANILLA_CLOUD_EXTENDED = register("vanilla_cloud_extended", new ParticleType<ParticleVanillaCloudExtended.VanillaCloudData>(false, ParticleVanillaCloudExtended.VanillaCloudData.DESERIALIZER) {
        @Override
        public Codec<ParticleVanillaCloudExtended.VanillaCloudData> getCodec() {
            return ParticleVanillaCloudExtended.VanillaCloudData.CODEC(VANILLA_CLOUD_EXTENDED);
        }
    });
    public static final ParticleType<AdvancedParticleData> RING_BIG = register("ring_big", AdvancedParticleData.DESERIALIZER);    public static final ParticleType<ParticleSnowFlake.SnowflakeData> SNOWFLAKE = register("snowflake", new ParticleType<ParticleSnowFlake.SnowflakeData>(false, ParticleSnowFlake.SnowflakeData.DESERIALIZER) {
        @Override
        public Codec<ParticleSnowFlake.SnowflakeData> getCodec() {
            return ParticleSnowFlake.SnowflakeData.CODEC(SNOWFLAKE);
        }
    });
    public static final ParticleType<AdvancedParticleData> PIXEL = register("pixel", AdvancedParticleData.DESERIALIZER);    public static final ParticleType<ParticleCloud.CloudData> CLOUD = register("cloud_soft", new ParticleType<ParticleCloud.CloudData>(false, ParticleCloud.CloudData.DESERIALIZER) {
        @Override
        public Codec<ParticleCloud.CloudData> getCodec() {
            return ParticleCloud.CloudData.CODEC(CLOUD);
        }
    });
    public static final ParticleType<AdvancedParticleData> ORB2 = register("orb", AdvancedParticleData.DESERIALIZER);    public static final ParticleType<ParticleOrb.OrbData> ORB = register("orb_0", new ParticleType<ParticleOrb.OrbData>(false, ParticleOrb.OrbData.DESERIALIZER) {
        @Override
        public Codec<ParticleOrb.OrbData> getCodec() {
            return ParticleOrb.OrbData.CODEC(ORB);
        }
    });
    public static final ParticleType<AdvancedParticleData> EYE = register("eye", AdvancedParticleData.DESERIALIZER);    public static final ParticleType<ParticleRing.RingData> RING = register("ring_0", new ParticleType<ParticleRing.RingData>(false, ParticleRing.RingData.DESERIALIZER) {
        @Override
        public Codec<ParticleRing.RingData> getCodec() {
            return ParticleRing.RingData.CODEC(RING);
        }
    });
    public static final ParticleType<AdvancedParticleData> BUBBLE = register("bubble", AdvancedParticleData.DESERIALIZER);
    public static final ParticleType<AdvancedParticleData> SUN = register("sun", AdvancedParticleData.DESERIALIZER);
    public static final ParticleType<AdvancedParticleData> SUN_NOVA = register("sun_nova", AdvancedParticleData.DESERIALIZER);
    public static final ParticleType<AdvancedParticleData> FLARE = register("flare", AdvancedParticleData.DESERIALIZER);
    public static final ParticleType<AdvancedParticleData> FLARE_RADIAL = register("flare_radial", AdvancedParticleData.DESERIALIZER);
    public static final ParticleType<AdvancedParticleData> BURST_IN = register("ring1", AdvancedParticleData.DESERIALIZER);
    public static final ParticleType<AdvancedParticleData> BURST_MESSY = register("burst_messy", AdvancedParticleData.DESERIALIZER);
    public static final ParticleType<AdvancedParticleData> RING_SPARKS = register("sparks_ring", AdvancedParticleData.DESERIALIZER);
    public static final ParticleType<AdvancedParticleData> BURST_OUT = register("ring2", AdvancedParticleData.DESERIALIZER);
    public static final ParticleType<AdvancedParticleData> GLOW = register("glow", AdvancedParticleData.DESERIALIZER);
    public static final ParticleType<AdvancedParticleData> ARROW_HEAD = register("arrow_head", AdvancedParticleData.DESERIALIZER);
    public static final ParticleType<DecalParticleData> STRIX_FOOTPRINT = registerDecal("strix_footprint", DecalParticleData.DESERIALIZER);
    public static final ParticleType<DecalParticleData> GROUND_CRACK = registerDecal("crack", DecalParticleData.DESERIALIZER);
    public static final ParticleType<RibbonParticleData> RIBBON_FLAT = registerRibbon("ribbon_flat", RibbonParticleData.DESERIALIZER);
    public static final ParticleType<RibbonParticleData> RIBBON_STREAKS = registerRibbon("ribbon_streaks", RibbonParticleData.DESERIALIZER);
    public static final ParticleType<RibbonParticleData> RIBBON_GLOW = registerRibbon("ribbon_glow", RibbonParticleData.DESERIALIZER);
    public static final ParticleType<RibbonParticleData> RIBBON_SQUIGGLE = registerRibbon("ribbon_squiggle", RibbonParticleData.DESERIALIZER);

    private static DefaultParticleType register(String key, boolean alwaysShow) {
        return Registry.register(Registries.PARTICLE_TYPE, new Identifier(MowziesMobs.MODID, key), FabricParticleTypes.simple(alwaysShow));
    }

    private static <T extends ParticleEffect> ParticleType<T> register(String key, ParticleType<T> type) {
        return Registry.register(Registries.PARTICLE_TYPE, new Identifier(MowziesMobs.MODID, key), type);
    }

    private static ParticleType<AdvancedParticleData> register(String key, ParticleEffect.Factory<AdvancedParticleData> deserializer) {
        return Registry.register(Registries.PARTICLE_TYPE, new Identifier(MowziesMobs.MODID, key), new ParticleType(false, deserializer) {
            public Codec<AdvancedParticleData> getCodec() {
                return AdvancedParticleData.CODEC(this);
            }
        });
    }

    private static <T> ParticleType<DecalParticleData> registerDecal(String key, ParticleEffect.Factory<DecalParticleData> deserializer) {
        return Registry.register(Registries.PARTICLE_TYPE, new Identifier(MowziesMobs.MODID, key), new ParticleType(false, deserializer) {
            public Codec<DecalParticleData> getCodec() {
                return DecalParticleData.CODEC_RIBBON(this);
            }
        });
    }

    private static ParticleType<RibbonParticleData> registerRibbon(String key, ParticleEffect.Factory<RibbonParticleData> deserializer) {
        return Registry.register(Registries.PARTICLE_TYPE, new Identifier(MowziesMobs.MODID, key), new ParticleType(false, deserializer) {
            public Codec<RibbonParticleData> getCodec() {
                return RibbonParticleData.CODEC_RIBBON(this);
            }
        });
    }

    public static void init() {
    }

    @Environment(EnvType.CLIENT)
    public static void registerParticles() {
        ParticleFactoryRegistry.getInstance().register(ParticleHandler.SPARKLE, ParticleSparkle.SparkleFactory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleHandler.VANILLA_CLOUD_EXTENDED, ParticleVanillaCloudExtended.CloudFactory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleHandler.SNOWFLAKE, ParticleSnowFlake.SnowFlakeFactory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleHandler.CLOUD, ParticleCloud.CloudFactory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleHandler.ORB, ParticleOrb.OrbFactory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleHandler.RING, ParticleRing.RingFactory::new);

        ParticleFactoryRegistry.getInstance().register(ParticleHandler.RING2, AdvancedParticleBase.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleHandler.RING_BIG, AdvancedParticleBase.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleHandler.PIXEL, AdvancedParticleBase.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleHandler.ORB2, AdvancedParticleBase.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleHandler.EYE, AdvancedParticleBase.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleHandler.BUBBLE, AdvancedParticleBase.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleHandler.SUN, AdvancedParticleBase.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleHandler.SUN_NOVA, AdvancedParticleBase.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleHandler.FLARE, AdvancedParticleBase.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleHandler.FLARE_RADIAL, AdvancedParticleBase.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleHandler.BURST_IN, AdvancedParticleBase.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleHandler.BURST_MESSY, AdvancedParticleBase.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleHandler.RING_SPARKS, AdvancedParticleBase.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleHandler.BURST_OUT, AdvancedParticleBase.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleHandler.GLOW, AdvancedParticleBase.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleHandler.ARROW_HEAD, AdvancedParticleBase.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleHandler.STRIX_FOOTPRINT, ParticleDecal.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleHandler.GROUND_CRACK, ParticleDecal.Factory::new);

        ParticleFactoryRegistry.getInstance().register(ParticleHandler.RIBBON_FLAT, ParticleRibbon.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleHandler.RIBBON_STREAKS, ParticleRibbon.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleHandler.RIBBON_GLOW, ParticleRibbon.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleHandler.RIBBON_SQUIGGLE, ParticleRibbon.Factory::new);
    }










}

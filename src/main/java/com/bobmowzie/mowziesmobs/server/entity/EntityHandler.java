package com.bobmowzie.mowziesmobs.server.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.entity.effects.*;
import com.bobmowzie.mowziesmobs.server.entity.effects.geomancy.*;
import com.bobmowzie.mowziesmobs.server.entity.foliaath.EntityBabyFoliaath;
import com.bobmowzie.mowziesmobs.server.entity.foliaath.EntityFoliaath;
import com.bobmowzie.mowziesmobs.server.entity.frostmaw.EntityFrostmaw;
import com.bobmowzie.mowziesmobs.server.entity.frostmaw.EntityFrozenController;
import com.bobmowzie.mowziesmobs.server.entity.grottol.EntityGrottol;
import com.bobmowzie.mowziesmobs.server.entity.lantern.EntityLantern;
import com.bobmowzie.mowziesmobs.server.entity.naga.EntityNaga;
import com.bobmowzie.mowziesmobs.server.entity.sculptor.EntitySculptor;
import com.bobmowzie.mowziesmobs.server.entity.umvuthana.*;
import com.bobmowzie.mowziesmobs.server.entity.wroughtnaut.EntityWroughtnaut;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class EntityHandler {
    public static final EntityType<EntityFoliaath> FOLIAATH = register("foliaath", build(EntityFoliaath::new, SpawnGroup.MONSTER, false, 0.5f, 2.5f));
    public static final EntityType<EntityBabyFoliaath> BABY_FOLIAATH = register("baby_foliaath", build(EntityBabyFoliaath::new, SpawnGroup.MONSTER, false, 0.4f, 0.4f));
    public static final EntityType<EntityWroughtnaut> WROUGHTNAUT = register("ferrous_wroughtnaut", build(EntityWroughtnaut::new, SpawnGroup.MONSTER, false, 2.5f, 3.5f, 1));
    public static final EntityType<EntityUmvuthanaFollowerToRaptor> UMVUTHANA_FOLLOWER_TO_RAPTOR = register("umvuthana_follower_raptor", build(EntityUmvuthanaFollowerToRaptor::new, SpawnGroup.MONSTER, false, MaskType.FEAR.entityWidth, MaskType.FEAR.entityHeight, 1));
    public static final EntityType<EntityUmvuthanaFollowerToPlayer> UMVUTHANA_FOLLOWER_TO_PLAYER = register("umvuthana_follower_player", build(EntityUmvuthanaFollowerToPlayer::new, SpawnGroup.MONSTER, false, MaskType.FEAR.entityWidth, MaskType.FEAR.entityHeight, 1));
    public static final EntityType<EntityUmvuthanaCraneToPlayer> UMVUTHANA_CRANE_TO_PLAYER = register("umvuthana_crane_player", build(EntityUmvuthanaCraneToPlayer::new, SpawnGroup.MONSTER, false, MaskType.FAITH.entityWidth, MaskType.FAITH.entityHeight, 1));
    public static final EntityType<EntityUmvuthanaMinion> UMVUTHANA_MINION = register("umvuthana", build(EntityUmvuthanaMinion::new, SpawnGroup.MONSTER, false, MaskType.FEAR.entityWidth, MaskType.FEAR.entityHeight, 1));
    public static final EntityType<EntityUmvuthanaRaptor> UMVUTHANA_RAPTOR = register("umvuthana_raptor", build(EntityUmvuthanaRaptor::new, SpawnGroup.MONSTER, false, MaskType.FURY.entityWidth, MaskType.FURY.entityHeight, 1));
    public static final EntityType<EntityUmvuthanaCrane> UMVUTHANA_CRANE = register("umvuthana_crane", build(EntityUmvuthanaCrane::new, SpawnGroup.MONSTER, false, MaskType.FEAR.entityWidth, MaskType.FEAR.entityHeight, 1));
    public static final EntityType<EntityUmvuthi> UMVUTHI = register("umvuthi", build(EntityUmvuthi::new, SpawnGroup.MONSTER, false, 1.5f, 3.2f, 1));
    public static final EntityType<EntityFrostmaw> FROSTMAW = register("frostmaw", build(EntityFrostmaw::new, SpawnGroup.MONSTER, false, 4f, 4f, 1));
    public static final EntityType<EntityGrottol> GROTTOL = register("grottol", build(EntityGrottol::new, SpawnGroup.MONSTER, false, 0.9F, 1.2F, 1));
    public static final EntityType<EntityLantern> LANTERN = register("lantern", build(EntityLantern::new, SpawnGroup.AMBIENT, false, 1.0f, 1.0f, 1));
    public static final EntityType<EntityNaga> NAGA = register("naga", build(EntityNaga::new, SpawnGroup.MONSTER, false, 3.0f, 1.0f, 1, 128));
    public static final EntityType<EntitySculptor> SCULPTOR = register("sculptor", build(EntitySculptor::new, SpawnGroup.MISC, false, 1.0f, 2.0f, 1));

    public static final EntityType<EntitySunstrike> SUNSTRIKE = register("sunstrike", build(EntitySunstrike::new, SpawnGroup.MISC, false, 0.1F, 0.1F));
    public static final EntityType<EntitySolarBeam> SOLAR_BEAM = register("solar_beam", build(EntitySolarBeam::new, SpawnGroup.MISC, false, 0.1F, 0.1F, 1));
    public static final EntityType<EntityBoulderProjectile> BOULDER_PROJECTILE = register("boulder_projectile", build(EntityBoulderProjectile::new, SpawnGroup.MISC, false, 1, 1, 1));
    public static final EntityType<EntityRockSling> ROCK_SLING = register("rock_sling", build(EntityRockSling::new, SpawnGroup.MISC, false, 1, 1, 1));
    public static final EntityType<EntityBoulderSculptor> BOULDER_SCULPTOR = register("boulder_platform", build(EntityBoulderSculptor::new, SpawnGroup.MISC, false, 1, 1, 1));
    public static final EntityType<EntityPillar> PILLAR = register("pillar", build(EntityPillar::new, SpawnGroup.MISC, false, 1f, 1f, 1));
    public static final EntityType<EntityPillar.EntityPillarSculptor> PILLAR_SCULPTOR = register("pillar_sculptor", build(EntityPillar.EntityPillarSculptor::new, SpawnGroup.MISC, false, 1f, 1f, 1));
    public static final EntityType<EntityPillarPiece> PILLAR_PIECE = register("pillar_piece", build(EntityPillarPiece::new, SpawnGroup.MISC, false, 1f, 1f, 1));

    public static final EntityType<EntityAxeAttack> AXE_ATTACK = register("axe_attack", build(EntityAxeAttack::new, SpawnGroup.MISC, false, 1f, 1f, 1));
    public static final EntityType<EntityIceBreath> ICE_BREATH = register("ice_breath", build(EntityIceBreath::new, SpawnGroup.MISC, false, 0F, 0F, 1));
    public static final EntityType<EntityIceBall> ICE_BALL = register("ice_ball", build(EntityIceBall::new, SpawnGroup.MISC, false, 0.5F, 0.5F, 20));
    public static final EntityType<EntityFrozenController> FROZEN_CONTROLLER = register("frozen_controller", build(EntityFrozenController::new, SpawnGroup.MISC, true, 0, 0));
    public static final EntityType<EntityDart> DART = register("dart", build(EntityDart::new, SpawnGroup.MISC, true, 0.5F, 0.5F, 20));
    public static final EntityType<EntityPoisonBall> POISON_BALL = register("poison_ball", build(EntityPoisonBall::new, SpawnGroup.MISC, false, 0.5F, 0.5F, 20));
    public static final EntityType<EntitySuperNova> SUPER_NOVA = register("super_nova", build(EntitySuperNova::new, SpawnGroup.MISC, false, 1, 1, Integer.MAX_VALUE));
    public static final EntityType<EntityFallingBlock> FALLING_BLOCK = register("falling_block", build(EntityFallingBlock::new, SpawnGroup.MISC, false, 1, 1));
    public static final EntityType<EntityBlockSwapper> BLOCK_SWAPPER = register("block_swapper", build(EntityBlockSwapper::new, SpawnGroup.MISC, true, 1, 1, Integer.MAX_VALUE));
    public static final EntityType<EntityBlockSwapper.EntityBlockSwapperSculptor> BLOCK_SWAPPER_SCULPTOR = register("block_swapper_sculptor", build(EntityBlockSwapper.EntityBlockSwapperSculptor::new, SpawnGroup.MISC, true, 1, 1, Integer.MAX_VALUE));
    public static final EntityType<EntityCameraShake> CAMERA_SHAKE = register("camera_shake", build(EntityCameraShake::new, SpawnGroup.MISC, false, 1, 1, Integer.MAX_VALUE));
//    private static EntityType.Builder<TestEntity> testEntityBuilder() {
//        return EntityType.Builder.of(TestEntity::new, SpawnGroup.MISC);
//    }
//    public static final RegistryObject<EntityType<TestEntity>> TEST_ENTITY = REG.register("test_entity", () -> testEntityBuilder(,false,1f, 2f,Integer.MAX_VALUE));

    private static <T extends Entity> EntityType<T> build(EntityType.EntityFactory<T> constructor, SpawnGroup category, boolean noSpawn, float sizeX, float sizeY) {
        FabricEntityTypeBuilder<T> builder = FabricEntityTypeBuilder.create(category, constructor).dimensions(EntityDimensions.changing(sizeX, sizeY));
        if (noSpawn) builder.disableSummon();
        return builder.build();
    }

    private static <T extends Entity> EntityType<T> build(EntityType.EntityFactory<T> constructor, SpawnGroup category, boolean noSpawn, float sizeX, float sizeY, int updateInterval) {
        FabricEntityTypeBuilder<T> builder = FabricEntityTypeBuilder.create(category, constructor).dimensions(EntityDimensions.changing(sizeX, sizeY)).trackedUpdateRate(updateInterval);
        if (noSpawn) builder.disableSummon();
        return builder.build();
    }

    private static <T extends Entity> EntityType<T> build(EntityType.EntityFactory<T> constructor, SpawnGroup category, boolean noSpawn, float sizeX, float sizeY, int updateInterval, int trackingRange) {
        FabricEntityTypeBuilder<T> builder = FabricEntityTypeBuilder.create(category, constructor).dimensions(EntityDimensions.changing(sizeX, sizeY)).trackedUpdateRate(updateInterval).trackRangeBlocks(trackingRange);
        if (noSpawn) builder.disableSummon();
        return builder.build();
    }

    private static <T extends Entity> EntityType<T> register(String entityName, EntityType<T> builder) {
        return Registry.register(Registries.ENTITY_TYPE, new Identifier(MowziesMobs.MODID, entityName), builder);
    }

    public static void init() {
        FabricDefaultAttributeRegistry.register(EntityHandler.FOLIAATH, EntityFoliaath.createAttributes().build());
        FabricDefaultAttributeRegistry.register(EntityHandler.BABY_FOLIAATH, EntityBabyFoliaath.createAttributes().build());
        FabricDefaultAttributeRegistry.register(EntityHandler.WROUGHTNAUT, EntityWroughtnaut.createAttributes().build());
        FabricDefaultAttributeRegistry.register(EntityHandler.UMVUTHANA_RAPTOR, EntityUmvuthanaRaptor.createAttributes().build());
        FabricDefaultAttributeRegistry.register(EntityHandler.UMVUTHANA_MINION, EntityUmvuthana.createAttributes().build());
        FabricDefaultAttributeRegistry.register(EntityHandler.UMVUTHANA_FOLLOWER_TO_PLAYER, EntityUmvuthanaFollowerToPlayer.createAttributes().build());
        FabricDefaultAttributeRegistry.register(EntityHandler.UMVUTHANA_CRANE_TO_PLAYER, EntityUmvuthanaFollowerToPlayer.createAttributes().build());
        FabricDefaultAttributeRegistry.register(EntityHandler.UMVUTHANA_FOLLOWER_TO_RAPTOR, EntityUmvuthana.createAttributes().build());
        FabricDefaultAttributeRegistry.register(EntityHandler.UMVUTHANA_CRANE, EntityUmvuthana.createAttributes().build());
        FabricDefaultAttributeRegistry.register(EntityHandler.UMVUTHI, EntityUmvuthi.createAttributes().build());
        FabricDefaultAttributeRegistry.register(EntityHandler.FROSTMAW, EntityFrostmaw.createAttributes().build());
        FabricDefaultAttributeRegistry.register(EntityHandler.NAGA, EntityNaga.createAttributes().build());
        FabricDefaultAttributeRegistry.register(EntityHandler.LANTERN, EntityLantern.createAttributes().build());
        FabricDefaultAttributeRegistry.register(EntityHandler.GROTTOL, EntityGrottol.createAttributes().build());
        FabricDefaultAttributeRegistry.register(EntityHandler.SCULPTOR, EntitySculptor.createAttributes().build());
    }
}

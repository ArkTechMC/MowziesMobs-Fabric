package com.bobmowzie.mowziesmobs.server.world.feature.structure;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.world.feature.structure.jigsaw.MowzieJigsawManager;
import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.structure.StructureType;
import org.apache.logging.log4j.Level;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

// Based on Telepathicgrunt's tutorial class: https://github.com/TelepathicGrunt/StructureTutorialMod/blob/1.18.0-Forge-Jigsaw/src/main/java/com/telepathicgrunt/structuretutorial/structures/RunDownHouseStructure.java
public class MonasteryStructure extends MowzieStructure {

    public static final Set<String> MUST_CONNECT_POOLS = Set.of(MowziesMobs.MODID + ":monastery/path_pool", MowziesMobs.MODID + ":monastery/path_connector_pool");
    public static final Set<String> REPLACE_POOLS = Set.of(MowziesMobs.MODID + ":monastery/path_pool");
    public static final String STRAIGHT_POOL = MowziesMobs.MODID + ":monastery/dead_end_connect_pool";

    public static final Codec<MonasteryStructure> CODEC = createCodec(MonasteryStructure::new);
    /**
     * The StructureSpawnListGatherEvent event allows us to have mobs that spawn naturally over time in our structure.
     * No other mobs will spawn in the structure of the same entity classification.
     * The reason you want to match the classifications is so that your structure's mob
     * will contribute to that classification's cap. Otherwise, it may cause a runaway
     * spawning of the mob that will never stop.
     * <p>
     * We use Lazy so that if you classload this class before you register your entities, you will not crash.
     * Instead, the field and the entities inside will only be referenced when StructureSpawnListGatherEvent
     * fires much later after entity registration.
     */
    private static final List<SpawnSettings.SpawnEntry> STRUCTURE_MONSTERS = ImmutableList.of(
            new SpawnSettings.SpawnEntry(EntityType.ILLUSIONER, 100, 4, 9),
            new SpawnSettings.SpawnEntry(EntityType.VINDICATOR, 100, 4, 9)
    );
    private static final List<SpawnSettings.SpawnEntry> STRUCTURE_CREATURES = ImmutableList.of(
            new SpawnSettings.SpawnEntry(EntityType.SHEEP, 30, 10, 15),
            new SpawnSettings.SpawnEntry(EntityType.RABBIT, 100, 1, 2)
    );

    public MonasteryStructure(Config settings) {
        // Create the pieces layout of the structure and give it to the game
        super(settings, ConfigHandler.COMMON.MOBS.SCULPTOR.generationConfig, StructureTypeHandler.SCULPTOR_BIOMES, true, true, true);
    }

    public static Optional<StructurePosition> createPiecesGenerator(Predicate<Context> canGeneratePredicate, Context context) {

        if (!canGeneratePredicate.test(context)) {
            return Optional.empty();
        }

        Context newContext = new Context(
                context.dynamicRegistryManager(),
                context.chunkGenerator(),
                context.biomeSource(),
                context.noiseConfig(),
                context.structureTemplateManager(),
                context.random(),
                context.seed(),
                context.chunkPos(),
                context.world(),
                context.biomePredicate()
        );

        BlockPos blockpos = context.chunkPos().getCenterAtY(0);

        Optional<StructurePosition> structurePiecesGenerator =
                MowzieJigsawManager.addPieces(
                        newContext, // Used for JigsawPlacement to get all the proper behaviors done.
                        RegistryEntry.of(context.dynamicRegistryManager().get(RegistryKeys.TEMPLATE_POOL)
                                .get(new Identifier(MowziesMobs.MODID, "monastery/start_pool"))), blockpos, // Position of the structure. Y value is ignored if last parameter is set to true.
                        false,  // Special boundary adjustments for villages. It's... hard to explain. Keep this false and make your pieces not be partially intersecting.
                        // Either not intersecting or fully contained will make children pieces spawn just fine. It's easier that way.
                        true, // Place at heightmap (top land). Set this to false for structure to be place at the passed in blockpos's Y value instead.
                        // Definitely keep this false when placing structures in the nether as otherwise, heightmap placing will put the structure on the Bedrock roof.
                        140,
                        "mowziesmobs:monastery/path",
                        "mowziesmobs:monastery/interior",
                        MUST_CONNECT_POOLS, REPLACE_POOLS, STRAIGHT_POOL, 23
                );

        if (structurePiecesGenerator.isPresent()) {
            MowziesMobs.LOGGER.log(Level.DEBUG, "Monastery at " + blockpos);
        }
        return structurePiecesGenerator;
    }

    // Hooked up in StructureTutorialMain. You can move this elsewhere or change it up.
    /*public static void setupStructureSpawns(final StructureSpawnListGatherEvent event) {
        // TODO
//        if(event.getStructure() == FeatureHandler.MONASTERY.get()) {
//            event.addEntitySpawns(MobCategory.MONSTER, STRUCTURE_MONSTERS.get());
//            event.addEntitySpawns(MobCategory.CREATURE, STRUCTURE_CREATURES.get());
//        }
    }*/

    @Override
    public GenerationStep.Feature getFeatureGenerationStep() {
        return GenerationStep.Feature.UNDERGROUND_DECORATION;
    }

    @Override
    public Optional<StructurePosition> getStructurePosition(Context context) {
        return createPiecesGenerator(t -> this.checkLocation(t), context);
    }

    @Override
    public StructureType<?> getType() {
        return StructureTypeHandler.MONASTERY;
    }
}

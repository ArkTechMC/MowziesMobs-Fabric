package com.bobmowzie.mowziesmobs.server.world.feature.structure.jigsaw;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import net.minecraft.block.JigsawBlock;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.structure.JigsawJunction;
import net.minecraft.structure.PoolStructurePiece;
import net.minecraft.structure.StructureTemplate.StructureBlockInfo;
import net.minecraft.structure.StructureTemplateManager;
import net.minecraft.structure.pool.*;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.CheckedRandom;
import net.minecraft.util.math.random.ChunkRandom;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeCoords;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.noise.NoiseConfig;
import net.minecraft.world.gen.structure.Structure;
import net.minecraft.world.gen.structure.Structure.Context;
import org.apache.commons.lang3.mutable.MutableObject;
import org.slf4j.Logger;

import java.util.*;
import java.util.function.Predicate;

public class MowzieJigsawManager {
    static final Logger LOGGER = LogUtils.getLogger();
    private static final Map<String, Integer> poolPlaceOrder = new HashMap<>();
    public static Comparator<Pair<StructureBlockInfo, PieceState>> placeOrderComparator = (p1, p2) -> {
        int p1Order = poolPlaceOrder.getOrDefault(p1.getFirst().nbt().getString("pool"), 0);
        int p2Order = poolPlaceOrder.getOrDefault(p2.getFirst().nbt().getString("pool"), 0);
        int result = Integer.compare(p1Order, p2Order);
        if (result == 0) result = Integer.compare(p1.getSecond().depth, p2.getSecond().depth);
        if (result == 0) result = Integer.compare(p1.hashCode(), p2.hashCode());
        return result;
    };

    static {
        poolPlaceOrder.put("mowziesmobs:monastery/interior/wall_corner_pool", -2);
        poolPlaceOrder.put("mowziesmobs:monastery/interior/wall_middle_pool", -2);
        poolPlaceOrder.put("mowziesmobs:monastery/interior/center_pool", -3);
        poolPlaceOrder.put("mowziesmobs:monastery/interior/blocker_pool", -4);
        poolPlaceOrder.put("mowziesmobs:monastery/interior/tower_stairs_1_pool", -5);
        poolPlaceOrder.put("mowziesmobs:monastery/interior/tower_stairs_2_pool", -5);
        poolPlaceOrder.put("mowziesmobs:monastery/interior/room_stairs_1_pool", -5);
        poolPlaceOrder.put("mowziesmobs:monastery/interior/room_stairs_2_pool", -5);
    }

    // TODO: Update to use ResourceKeys and Holders
    public static Optional<Structure.StructurePosition> addPieces(
            Context context,
            RegistryEntry<StructurePool> pool, BlockPos genPos, boolean villageBoundaryAdjust, boolean useTerrainHeight, int maxDistFromStart,
            String pathJigsawName, String interiorJigsawName,
            Set<String> mustConnectPools, Set<String> replacePools, String deadEndConnectorPool, int maxDepth
    ) {
        ChunkRandom worldgenrandom = new ChunkRandom(new CheckedRandom(0L));
        worldgenrandom.setCarverSeed(context.seed(), context.chunkPos().x, context.chunkPos().z);
        DynamicRegistryManager registryaccess = context.dynamicRegistryManager();
        ChunkGenerator chunkgenerator = context.chunkGenerator();
        StructureTemplateManager structuremanager = context.structureTemplateManager();
        HeightLimitView levelheightaccessor = context.world();
        Predicate<RegistryEntry<Biome>> predicate = context.biomePredicate();
        Registry<StructurePool> registry = registryaccess.get(RegistryKeys.TEMPLATE_POOL);
        BlockRotation rotation = BlockRotation.random(worldgenrandom);
        StructurePool structuretemplatepool = pool.value();
        StructurePoolElement structurepoolelement = structuretemplatepool.getRandomElement(worldgenrandom);
        if (structurepoolelement == EmptyPoolElement.INSTANCE) {
            return Optional.empty();
        } else {
            PoolStructurePiece poolelementstructurepiece = new PoolStructurePiece(structuremanager, structurepoolelement, genPos, structurepoolelement.getGroundLevelDelta(), rotation, structurepoolelement.getBoundingBox(structuremanager, genPos, rotation));
            BlockBox pieceBoundingBox = poolelementstructurepiece.getBoundingBox();
            BlockPos offset = BlockPos.ORIGIN;
            if (structurepoolelement instanceof MowziePoolElement) {
                offset = ((MowziePoolElement) structurepoolelement).bounds.offset;
                offset = offset.rotate(rotation);
            }
            int centerX = (pieceBoundingBox.getMaxX() + pieceBoundingBox.getMinX()) / 2;
            int centerZ = (pieceBoundingBox.getMaxZ() + pieceBoundingBox.getMinZ()) / 2;
            int height;
            if (useTerrainHeight) {
                height = genPos.getY() + chunkgenerator.getHeightOnGround(centerX + offset.getX(), centerZ + offset.getZ(), Heightmap.Type.WORLD_SURFACE_WG, levelheightaccessor, context.noiseConfig()) + offset.getY();
            } else {
                height = genPos.getY();
            }

            if (!predicate.test(chunkgenerator.getBiomeSource().getBiome(BiomeCoords.fromBlock(centerX), BiomeCoords.fromBlock(height), BiomeCoords.fromBlock(centerZ), context.noiseConfig().getMultiNoiseSampler()))) {
                return Optional.empty();
            } else {
                int l = pieceBoundingBox.getMinY() + poolelementstructurepiece.getGroundLevelDelta();
                poolelementstructurepiece.translate(0, height - l, 0);
                return Optional.of(new Structure.StructurePosition(new BlockPos(centerX, height, centerZ), (builder) -> {
                    List<PoolStructurePiece> list = Lists.newArrayList();
                    list.add(poolelementstructurepiece);
                    if (maxDepth >= 0) {
                        Box aabb = new Box(centerX - maxDistFromStart, height - maxDistFromStart, centerZ - maxDistFromStart, centerX + maxDistFromStart + 1, height + maxDistFromStart + 1, centerZ + maxDistFromStart + 1);
                        VoxelShape shape = VoxelShapes.combineAndSimplify(VoxelShapes.cuboid(aabb), VoxelShapes.cuboid(Box.from(pieceBoundingBox)), BooleanBiFunction.ONLY_FIRST);

                        // Bounding voxelshapes
                        MutableObject<VoxelShape> free = new MutableObject<>(shape);
                        MutableObject<VoxelShape> interiorFree = new MutableObject<>(VoxelShapes.empty());
                        MutableObject<Map<String, VoxelShape>> specialBounds = new MutableObject<>(new HashMap<>());

                        Placer placer = new Placer(registry, maxDepth, chunkgenerator, structuremanager, list, worldgenrandom, pathJigsawName, interiorJigsawName, free, interiorFree, specialBounds);

                        // Place starting piece
                        String tag = null;
                        if (poolelementstructurepiece.getPoolElement() instanceof MowziePoolElement)
                            tag = ((MowziePoolElement) poolelementstructurepiece.getPoolElement()).getRandomTag(worldgenrandom);
                        PieceState startingPiece = new PieceState(poolelementstructurepiece, 0, null, tag);
                        for (StructureBlockInfo structureBlockInfo : placer.getJigsawBlocksFromPieceState(startingPiece)) {
                            placer.placing.add(new Pair<>(structureBlockInfo, startingPiece));
                        }

                        // Iteratively place child pieces until 'placing' is empty or max depth is reached
                        while (!placer.placing.isEmpty()) {
                            Pair<StructureBlockInfo, PieceState> nextJigsawBlock = placer.placing.first();
                            placer.placing.remove(nextJigsawBlock);
                            if (nextJigsawBlock.getSecond().depth > maxDepth) {
                                break;
                            }
                            placer.tryPlacingChildren(nextJigsawBlock.getFirst(), nextJigsawBlock.getSecond(), villageBoundaryAdjust, levelheightaccessor, context.noiseConfig());
                        }

                        Placer fallbackPlacer = new FallbackPlacer(registry, maxDepth, chunkgenerator, structuremanager, list, worldgenrandom, pathJigsawName, interiorJigsawName, free, interiorFree, specialBounds, placer);
                        while (!fallbackPlacer.placing.isEmpty()) {
                            Pair<StructureBlockInfo, PieceState> nextJigsawBlock = fallbackPlacer.placing.first();
                            fallbackPlacer.placing.remove(nextJigsawBlock);
                            fallbackPlacer.tryPlacingChildren(nextJigsawBlock.getFirst(), nextJigsawBlock.getSecond(), villageBoundaryAdjust, levelheightaccessor, context.noiseConfig());
                        }

                        Placer interiorPlacer = new InteriorPlacer(registry, maxDepth, chunkgenerator, structuremanager, list, worldgenrandom, pathJigsawName, interiorJigsawName, free, interiorFree, specialBounds, fallbackPlacer);
                        while (!interiorPlacer.placing.isEmpty()) {
                            Pair<StructureBlockInfo, PieceState> nextJigsawBlock = interiorPlacer.placing.first();
                            interiorPlacer.placing.remove(nextJigsawBlock);
                            interiorPlacer.tryPlacingChildren(nextJigsawBlock.getFirst(), nextJigsawBlock.getSecond(), villageBoundaryAdjust, levelheightaccessor, context.noiseConfig());
                        }

                        list.sort((p1, p2) -> {
                            int i1, i2;
                            i1 = i2 = 0;
                            if (p1.getPoolElement() instanceof MowziePoolElement)
                                i1 = ((MowziePoolElement) p1.getPoolElement()).genOrder;
                            if (p2.getPoolElement() instanceof MowziePoolElement)
                                i2 = ((MowziePoolElement) p2.getPoolElement()).genOrder;
                            return Integer.compare(i1, i2);
                        });

                        list.forEach(builder::addPiece);
                    }
                }));
            }
        }
    }

    public interface PieceFactory {
        PoolStructurePiece create(StructureTemplateManager p_210301_, StructurePoolElement p_210302_, BlockPos p_210303_, int p_210304_, BlockRotation p_210305_, BlockBox p_210306_);
    }

    static class PieceState {
        final PoolStructurePiece piece;
        final int depth;
        final PieceState parent;
        final Set<PieceState> children;
        final String tag;

        PieceState(PoolStructurePiece p_210311_, int p_210313_, PieceState parent, String tag) {
            this.piece = p_210311_;
            this.depth = p_210313_;
            this.parent = parent;
            this.children = new HashSet<>();
            this.tag = tag;
        }
    }

    public record PieceSelection(PieceState pieceState, StructurePoolElement origPiece, StructurePoolElement nextPiece,
                                 PoolStructurePiece poolelementstructurepiece, StructureBlockInfo origJigsaw,
                                 StructureBlockInfo connectedJigsaw, StructureBlockInfo nextJigsaw,
                                 BlockBox nextPieceBoundingBoxPlaced, BlockBox nextPieceInteriorBoundingBox, int l2) {
    }

    static class Placer {
        protected final Random random;
        protected final String pathJigsawName;
        protected final String interiorJigsawName;
        final Registry<StructurePool> pools;
        final int maxDepth;
        final ChunkGenerator chunkGenerator;
        final StructureTemplateManager structureManager;
        final List<? super PoolStructurePiece> pieces;
        final SortedSet<Pair<StructureBlockInfo, PieceState>> placing = new TreeSet<>(placeOrderComparator);
        final SortedSet<Pair<StructureBlockInfo, PieceState>> fallbacks = new TreeSet<>(placeOrderComparator);
        final SortedSet<Pair<StructureBlockInfo, PieceState>> interior = new TreeSet<>(placeOrderComparator);
        protected int numPaths;
        MutableObject<VoxelShape> free;
        MutableObject<VoxelShape> interiorFree;
        MutableObject<Map<String, VoxelShape>> specialBounds;

        Placer(Registry<StructurePool> p_210323_, int p_210324_, ChunkGenerator p_210326_, StructureTemplateManager p_210327_, List<? super PoolStructurePiece> p_210328_, Random p_210329_,
               String pathJigsawName, String interiorJigsawName,
               MutableObject<VoxelShape> free, MutableObject<VoxelShape> interiorFree, MutableObject<Map<String, VoxelShape>> specialBounds) {
            this.pools = p_210323_;
            this.maxDepth = p_210324_;
            this.chunkGenerator = p_210326_;
            this.structureManager = p_210327_;
            this.pieces = p_210328_;
            this.random = p_210329_;
            this.numPaths = 0;

            this.pathJigsawName = pathJigsawName;
            this.interiorJigsawName = interiorJigsawName;

            this.free = free;
            this.interiorFree = interiorFree;
            this.specialBounds = specialBounds;
        }

        void tryPlacingChildren(StructureBlockInfo thisPieceJigsawBlock, PieceState pieceState, boolean villageBoundaryAdjust, HeightLimitView heightAccessor, NoiseConfig state) {
            if (this.skipJigsawBlock(thisPieceJigsawBlock, pieceState)) return;

            if (thisPieceJigsawBlock.nbt().getString("target").equals(this.pathJigsawName)) {
                if (this.numPaths > 0) this.numPaths--;
            }
            String pool = this.selectPool(thisPieceJigsawBlock);
            Identifier poolResourceLocation = new Identifier(pool);
            Optional<StructurePool> poolOptional = this.pools.getOrEmpty(poolResourceLocation);
            // If pool exists and is not empty
            if (poolOptional.isPresent() && (poolOptional.get().getElementCount() != 0 || Objects.equals(poolResourceLocation, StructurePools.EMPTY.getValue()))) {
                RegistryEntry<StructurePool> fallbackPoolHolder = poolOptional.get().getFallback();
                // If fallback pool exists and is not empty
                if (fallbackPoolHolder.value().getElementCount() != 0 || fallbackPoolHolder.matchesKey(StructurePools.EMPTY)) {

                    PieceSelection pieceSelection = this.selectPiece(pieceState, poolOptional.get(), fallbackPoolHolder.value(), villageBoundaryAdjust, thisPieceJigsawBlock, heightAccessor, state);

                    if (pieceSelection != null) this.addNextPieceState(pieceSelection);
                } else {
                    LOGGER.warn("Empty or non-existent fallback pool: {}", fallbackPoolHolder.getKey().map((p_255599_) -> {
                        return p_255599_.getValue().toString();
                    }).orElse("<unregistered>"));
                }
            } else {
                LOGGER.warn("Empty or non-existent pool: {}", poolResourceLocation);
            }

        }

        protected boolean skipJigsawBlock(StructureBlockInfo thisPieceJigsawBlock, PieceState pieceState) {
            if (thisPieceJigsawBlock.nbt().getString("target").equals(this.interiorJigsawName)) {
                this.interior.add(Pair.of(thisPieceJigsawBlock, pieceState));
                return true;
            }
            return false;
        }

        List<StructureBlockInfo> getJigsawBlocksFromPieceState(PieceState pieceState) {
            StructurePoolElement structurepoolelement = pieceState.piece.getPoolElement();
            BlockPos blockpos = pieceState.piece.getPos();
            BlockRotation rotation = pieceState.piece.getRotation();
            return structurepoolelement.getStructureBlockInfos(this.structureManager, blockpos, rotation, this.random);
        }

        String selectPool(StructureBlockInfo thisPieceJigsawBlock) {
            return thisPieceJigsawBlock.nbt().getString("pool");
        }

        List<StructurePoolElement> addPoolElements(PieceState pieceState, StructurePool pool, StructurePool fallbackPool) {
            List<StructurePoolElement> structurePoolElements = Lists.newArrayList();
            structurePoolElements.addAll(pool.getElementIndicesInRandomOrder(this.random));
            return structurePoolElements;
        }

        boolean isJigsawBlockFacingFreeSpace(StructureBlockInfo jigsawBlockInfo, MutableObject<VoxelShape> freeSpace) {
            Direction direction = JigsawBlock.getFacing(jigsawBlockInfo.state());
            BlockPos thisJigsawBlockPos = jigsawBlockInfo.pos();
            BlockPos nextJigsawBlockPos = thisJigsawBlockPos.offset(direction);
            Box aabb = new Box(nextJigsawBlockPos);
            return !VoxelShapes.matchesAnywhere(freeSpace.getValue(), VoxelShapes.cuboid(aabb.contract(0.25D)), BooleanBiFunction.ONLY_SECOND);
        }

        boolean canJigsawBlockFitNextPiece(StructureBlockInfo jigsawBlockInfo, MutableObject<VoxelShape> freeSpace) {
            Direction direction = JigsawBlock.getFacing(jigsawBlockInfo.state());
            BlockPos thisJigsawBlockPos = jigsawBlockInfo.pos();
            BlockPos nextJigsawBlockPos = thisJigsawBlockPos.offset(direction);
            Box aabb = new Box(nextJigsawBlockPos);
            Vec3i offset = direction.getVector();
            aabb = aabb.expand(2).offset(offset.getX() * 2, offset.getY() * 2, offset.getZ() * 2);
            return !VoxelShapes.matchesAnywhere(freeSpace.getValue(), VoxelShapes.cuboid(aabb.contract(0.25D)), BooleanBiFunction.ONLY_SECOND);
        }

        PieceSelection selectPiece(PieceState pieceState, StructurePool poolOptional, StructurePool fallbackPoolOptional, boolean villageBoundaryAdjust, StructureBlockInfo thisPieceJigsawBlock, HeightLimitView heightAccessor, NoiseConfig state) {
            BlockBox thisPieceBoundingBox = pieceState.piece.getBoundingBox();
            int thisPieceMinY = thisPieceBoundingBox.getMinY();
            Direction direction = JigsawBlock.getFacing(thisPieceJigsawBlock.state());
            BlockPos thisJigsawBlockPos = thisPieceJigsawBlock.pos();
            BlockPos nextJigsawBlockPos = thisJigsawBlockPos.offset(direction);
            int thisPieceHeightFromBottomToJigsawBlock = thisJigsawBlockPos.getY() - thisPieceMinY;
            StructurePoolElement structurepoolelement = pieceState.piece.getPoolElement();
            StructurePool.Projection structuretemplatepool$projection = structurepoolelement.getProjection();
            boolean thisPieceIsRigid = structuretemplatepool$projection == StructurePool.Projection.RIGID;
            int k = -1;

            // Loop through pool elements
            List<StructurePoolElement> structurePoolElements = this.addPoolElements(pieceState, poolOptional, fallbackPoolOptional);
            structurePoolElements.sort((p1, p2) -> {
                int i1, i2;
                i1 = i2 = 0;
                if (p1 instanceof MowziePoolElement) i1 = ((MowziePoolElement) p1).priority;
                if (p2 instanceof MowziePoolElement) i2 = ((MowziePoolElement) p2).priority;
                return Integer.compare(i1, i2);
            });
            for (StructurePoolElement nextPieceCandidate : structurePoolElements) {

//                if (nextPieceCandidate.toString().contains("room") || nextPieceCandidate.toString().contains("tower")) continue;

                // If empty element, break from the loop and spawn nothing
                if (nextPieceCandidate == FallbackPoolElement.INSTANCE) {
                    break;
                }
                if (nextPieceCandidate == EmptyPoolElement.INSTANCE || nextPieceCandidate instanceof SinglePoolElement && nextPieceCandidate.toString().equals("Single[Left[minecraft:empty]]")) {
                    return null;
                }

                if (nextPieceCandidate instanceof MowziePoolElement mowziePoolElement) {
                    if (!mowziePoolElement.checkCriteria(pieceState, this)) continue;
                }

                // Iterate through possible rotations of this element
                for (BlockRotation nextPieceRotation : BlockRotation.randomRotationOrder(this.random)) {
                    List<StructureBlockInfo> nextPieceJigsawBlocks = nextPieceCandidate.getStructureBlockInfos(this.structureManager, BlockPos.ORIGIN, nextPieceRotation, this.random);
                    BlockBox nextPieceBoundingBoxOrigin = nextPieceCandidate.getBoundingBox(this.structureManager, BlockPos.ORIGIN, nextPieceRotation);

                    int largestSizeOfNextNextPiece;
                    // Village boundary adjustment
                    if (villageBoundaryAdjust && nextPieceBoundingBoxOrigin.getBlockCountY() <= 16) {
                        // Map each jigsaw block to the size of the largest next piece it can generate and largestSizeOfNextNextPiece becomes the largest of these
                        largestSizeOfNextNextPiece = nextPieceJigsawBlocks.stream().mapToInt((blockInfo) -> {
                            // If the next piece's jigsaw block would place inside its own bounding box, return 0
                            if (!nextPieceBoundingBoxOrigin.contains(blockInfo.pos().offset(JigsawBlock.getFacing(blockInfo.state())))) {
                                return 0;
                            } else {
                                Identifier resourcelocation2 = new Identifier(blockInfo.nbt().getString("pool"));
                                Optional<StructurePool> optional2 = this.pools.getOrEmpty(resourcelocation2);
                                Optional<StructurePool> optional3 = optional2.flatMap((p_210344_) -> {
                                    return this.pools.getOrEmpty(p_210344_.getFallback().getKey().get());
                                });
                                int j3 = optional2.map((structureTemplatePool) -> {
                                    return structureTemplatePool.getHighestY(this.structureManager);
                                }).orElse(0);
                                int k3 = optional3.map((structureTemplatePool) -> {
                                    return structureTemplatePool.getHighestY(this.structureManager);
                                }).orElse(0);
                                return Math.max(j3, k3);
                            }
                        }).max().orElse(0);
                    } else {
                        largestSizeOfNextNextPiece = 0;
                    }

                    for (StructureBlockInfo nextPieceJigsawBlock : nextPieceJigsawBlocks) {
                        boolean canAttach;
                        if (nextPieceCandidate instanceof MowziePoolElement && ((MowziePoolElement) nextPieceCandidate).twoWay()) {
                            canAttach = MowziePoolElement.canAttachTwoWays(thisPieceJigsawBlock, nextPieceJigsawBlock);
                        } else {
                            canAttach = JigsawBlock.attachmentMatches(thisPieceJigsawBlock, nextPieceJigsawBlock);
                        }
                        if (canAttach) {
                            BlockPos nextPieceJigsawBlockPos = nextPieceJigsawBlock.pos();
                            BlockPos nextPiecePos = nextJigsawBlockPos.subtract(nextPieceJigsawBlockPos);
                            BlockBox nextPieceBoundingBox = nextPieceCandidate.getBoundingBox(this.structureManager, nextPiecePos, nextPieceRotation);
                            int nextPieceMinY = nextPieceBoundingBox.getMinY();
                            if (nextPieceCandidate instanceof MowziePoolElement) {
                                nextPieceMinY -= ((MowziePoolElement) nextPieceCandidate).bounds.boundsMinOffset.getY();
                            }
                            StructurePool.Projection structuretemplatepool$projection1 = nextPieceCandidate.getProjection();
                            boolean nextPieceIsRigid = structuretemplatepool$projection1 == StructurePool.Projection.RIGID;
                            int nextPieceJigsawBlockY = nextPieceJigsawBlockPos.getY();
                            int k1 = thisPieceHeightFromBottomToJigsawBlock - nextPieceJigsawBlockY + JigsawBlock.getFacing(thisPieceJigsawBlock.state()).getOffsetY();
                            int l1;
                            if (thisPieceIsRigid && nextPieceIsRigid) {
                                l1 = thisPieceMinY + k1;
                            } else {
                                if (k == -1) {
                                    k = this.chunkGenerator.getHeightOnGround(thisJigsawBlockPos.getX(), thisJigsawBlockPos.getZ(), Heightmap.Type.WORLD_SURFACE_WG, heightAccessor, state);
                                }

                                l1 = k - nextPieceJigsawBlockY;
                            }

                            int i2 = l1 - nextPieceMinY;
                            BlockBox nextPieceBoundingBoxPlaced = nextPieceBoundingBox.offset(0, i2, 0);
                            BlockPos blockpos5 = nextPiecePos.add(0, i2, 0);
                            if (largestSizeOfNextNextPiece > 0) {
                                int j2 = Math.max(largestSizeOfNextNextPiece + 1, nextPieceBoundingBoxPlaced.getMaxY() - nextPieceBoundingBoxPlaced.getMinY());
                                nextPieceBoundingBoxPlaced.encompass(new BlockPos(nextPieceBoundingBoxPlaced.getMinX(), nextPieceBoundingBoxPlaced.getMinY() + j2, nextPieceBoundingBoxPlaced.getMinZ()));
                            }

                            // Check height params
                            if (nextPieceCandidate instanceof MowziePoolElement) {
                                Optional<Integer> maxHeight = ((MowziePoolElement) nextPieceCandidate).conditions.maxHeight;
                                Optional<Integer> minHeight = ((MowziePoolElement) nextPieceCandidate).conditions.minHeight;
                                if (maxHeight.isPresent() || minHeight.isPresent()) {
                                    int freeHeight = this.chunkGenerator.getHeightOnGround(nextPieceBoundingBoxPlaced.getMinX() + nextPieceBoundingBoxPlaced.getBlockCountX() / 2, nextPieceBoundingBoxPlaced.getMinZ() + nextPieceBoundingBoxPlaced.getBlockCountZ() / 2, Heightmap.Type.WORLD_SURFACE_WG, heightAccessor, state);
                                    if (maxHeight.isPresent() && nextPieceMinY - freeHeight > maxHeight.get()) continue;
                                    if (minHeight.isPresent() && nextPieceMinY - freeHeight < minHeight.get()) continue;
                                }
                            }

                            if (this.checkBounds(nextPieceCandidate, pieceState, nextPieceBoundingBoxPlaced, nextPiecePos, nextPieceRotation, i2)) {
                                // Special bounding boxes
                                if (nextPieceCandidate instanceof MowziePoolElement mowziePoolElement) {

                                    Optional<String> needsOverlapBounds = mowziePoolElement.bounds.needsOverlapBounds;
                                    if (needsOverlapBounds.isPresent()) {
                                        if (!this.specialBounds.getValue().containsKey(needsOverlapBounds.get()) || !VoxelShapes.matchesAnywhere(this.specialBounds.getValue().get(needsOverlapBounds.get()), VoxelShapes.cuboid(Box.from(nextPieceBoundingBoxPlaced).contract(0.25D)), BooleanBiFunction.AND)) {
                                            continue;
                                        }
                                    }

                                    Optional<String> forbiddenOverlapBounds = mowziePoolElement.bounds.forbiddenOverlapBounds;
                                    if (forbiddenOverlapBounds.isPresent()) {
                                        if (this.specialBounds.getValue().containsKey(forbiddenOverlapBounds.get()) && VoxelShapes.matchesAnywhere(this.specialBounds.getValue().get(forbiddenOverlapBounds.get()), VoxelShapes.cuboid(Box.from(nextPieceBoundingBoxPlaced).contract(0.25D)), BooleanBiFunction.AND)) {
                                            continue;
                                        }
                                    }
                                }

                                int pieceGroundLevelDelta = pieceState.piece.getGroundLevelDelta();
                                int k2;
                                if (nextPieceIsRigid) {
                                    k2 = pieceGroundLevelDelta - k1;
                                } else {
                                    k2 = nextPieceCandidate.getGroundLevelDelta();
                                }

                                PoolStructurePiece poolelementstructurepiece = new PoolStructurePiece(this.structureManager, nextPieceCandidate, blockpos5, k2, nextPieceRotation, nextPieceBoundingBoxPlaced);
                                int l2;
                                if (thisPieceIsRigid) {
                                    l2 = thisPieceMinY + thisPieceHeightFromBottomToJigsawBlock;
                                } else if (nextPieceIsRigid) {
                                    l2 = l1 + nextPieceJigsawBlockY;
                                } else {
                                    if (k == -1) {
                                        k = this.chunkGenerator.getHeightOnGround(thisJigsawBlockPos.getX(), thisJigsawBlockPos.getZ(), Heightmap.Type.WORLD_SURFACE_WG, heightAccessor, state);
                                    }

                                    l2 = k + k1 / 2;
                                }

                                BlockBox interiorBounds = null;
                                if (nextPieceCandidate instanceof MowziePoolElement) {
                                    interiorBounds = ((MowziePoolElement) nextPieceCandidate).getInteriorBoundingBox(this.structureManager, nextPiecePos, nextPieceRotation);
                                    if (interiorBounds != null) interiorBounds = interiorBounds.offset(0, i2, 0);
                                }
                                return new PieceSelection(pieceState, structurepoolelement, nextPieceCandidate, poolelementstructurepiece, thisPieceJigsawBlock, nextPieceJigsawBlock, null, nextPieceBoundingBoxPlaced, interiorBounds, l2);
                            }
                        }
                    }
                }
            }
            this.fallbacks.add(new Pair<>(thisPieceJigsawBlock, pieceState));
            return null;
        }

        protected boolean checkBounds(StructurePoolElement nextPieceCandidate, PieceState pieceState, BlockBox nextPieceBoundingBoxPlaced, BlockPos nextPiecePos, BlockRotation nextPieceRotation, int i2) {
            boolean ignoreBounds = false;
            BlockBox spaceCheckBounds = nextPieceBoundingBoxPlaced;
            if (nextPieceCandidate instanceof MowziePoolElement) {
                ignoreBounds = ((MowziePoolElement) nextPieceCandidate).ignoresBounds();
                spaceCheckBounds = ((MowziePoolElement) nextPieceCandidate).getCheckBoundingBox(this.structureManager, nextPiecePos, nextPieceRotation).offset(0, i2, 0);
            }
            VoxelShape freeSpace = VoxelShapes.combine(this.free.getValue(), VoxelShapes.cuboid(Box.from(pieceState.piece.getBoundingBox())), BooleanBiFunction.OR);
            return ignoreBounds || !VoxelShapes.matchesAnywhere(freeSpace, VoxelShapes.cuboid(Box.from(spaceCheckBounds).contract(0.25D)), BooleanBiFunction.ONLY_SECOND);
        }

        void addNextPieceState(PieceSelection pieceSelection) {
            // Subtract the bounding box from the free space
            if (!(pieceSelection.nextPiece instanceof MowziePoolElement && (((MowziePoolElement) pieceSelection.nextPiece).ignoresBounds() || !((MowziePoolElement) pieceSelection.nextPiece).placeBounds()))) {
                this.free.setValue(VoxelShapes.combine(this.free.getValue(), VoxelShapes.cuboid(Box.from(pieceSelection.nextPieceBoundingBoxPlaced)), BooleanBiFunction.ONLY_FIRST));
            }

            // Add the bounds to the interior space
            if (pieceSelection.nextPiece instanceof MowziePoolElement && pieceSelection.nextPieceInteriorBoundingBox != null) {
                this.interiorFree.setValue(VoxelShapes.combine(this.interiorFree.getValue(), VoxelShapes.cuboid(Box.from(pieceSelection.nextPieceInteriorBoundingBox)), BooleanBiFunction.OR));
            }

            // Special bounds - add this piece's bounds to any special bounds its set to
            if (pieceSelection.nextPiece instanceof MowziePoolElement mowziePoolElement) {
                Optional<String> nextPieceSpecialBounds = mowziePoolElement.bounds.specialBounds;
                if (nextPieceSpecialBounds.isPresent()) {
                    if (this.specialBounds.getValue().containsKey(nextPieceSpecialBounds.get())) {
                        this.specialBounds.getValue().put(nextPieceSpecialBounds.get(), VoxelShapes.combine(this.specialBounds.getValue().get(nextPieceSpecialBounds.get()), VoxelShapes.cuboid(Box.from(pieceSelection.nextPieceBoundingBoxPlaced)), BooleanBiFunction.OR));
                    } else {
                        this.specialBounds.getValue().put(nextPieceSpecialBounds.get(), VoxelShapes.cuboid(Box.from(pieceSelection.nextPieceBoundingBoxPlaced)));
                    }
                }
            }

            BlockBox thisPieceBoundingBox = pieceSelection.pieceState.piece.getBoundingBox();
            int thisPieceMinY = thisPieceBoundingBox.getMinY();
            int thisPieceHeightFromBottomToJigsawBlock = pieceSelection.origJigsaw.pos().getY() - thisPieceMinY;
            int pieceGroundLevelDelta = pieceSelection.pieceState.piece.getGroundLevelDelta();
            int k1 = thisPieceHeightFromBottomToJigsawBlock - pieceSelection.connectedJigsaw.pos().getY() + JigsawBlock.getFacing(pieceSelection.origJigsaw.state()).getOffsetY();
            int k2;
            if (pieceSelection.nextPiece.getProjection() == StructurePool.Projection.RIGID) {
                k2 = pieceGroundLevelDelta - k1;
            } else {
                k2 = pieceSelection.nextPiece.getGroundLevelDelta();
            }

            pieceSelection.pieceState.piece.addJunction(new JigsawJunction(pieceSelection.connectedJigsaw.pos().getX(), pieceSelection.l2 - thisPieceHeightFromBottomToJigsawBlock + pieceGroundLevelDelta, pieceSelection.connectedJigsaw.pos().getZ(), k1, pieceSelection.nextPiece.getProjection()));
            pieceSelection.poolelementstructurepiece.addJunction(new JigsawJunction(pieceSelection.origJigsaw.pos().getX(), pieceSelection.l2 - pieceSelection.connectedJigsaw.pos().getY() + k2, pieceSelection.origJigsaw.pos().getZ(), -k1, pieceSelection.origPiece.getProjection()));
            this.pieces.add(pieceSelection.poolelementstructurepiece);
            String tag = null;
            if (pieceSelection.nextPiece instanceof MowziePoolElement mowziePoolElement) {
                if (mowziePoolElement.tags.inheritsTag) tag = pieceSelection.pieceState.tag;
                else tag = ((MowziePoolElement) pieceSelection.nextPiece).getRandomTag(this.random);
            }
            PieceState nextPieceState = new PieceState(pieceSelection.poolelementstructurepiece, pieceSelection.pieceState.depth + 1, pieceSelection.pieceState, tag);

            // Queue up the next jigsaw pieces
            List<StructureBlockInfo> nextJigsaws = this.getJigsawBlocksFromPieceState(nextPieceState);
            // Skip the jigsaw piece that just connected (unless its two-way)
            if (!(pieceSelection.poolelementstructurepiece.getPoolElement() instanceof MowziePoolElement) || !((MowziePoolElement) pieceSelection.poolelementstructurepiece.getPoolElement()).twoWay()) {
                nextJigsaws.removeIf(jigsaw -> {
                    Direction direction = JigsawBlock.getFacing(pieceSelection.origJigsaw.state());
                    BlockPos thisJigsawBlockPos = pieceSelection.origJigsaw.pos();
                    BlockPos nextJigsawBlockPos = thisJigsawBlockPos.offset(direction);
                    return jigsaw.pos().equals(nextJigsawBlockPos);
                });
            }

            // Count up the number of next paths. Check for overridden count first.
            if (pieceSelection.nextPiece instanceof MowziePoolElement && ((MowziePoolElement) pieceSelection.nextPiece).conditions.numPathsOverride.isPresent()) {
                this.numPaths += ((MowziePoolElement) pieceSelection.nextPiece).conditions.numPathsOverride.get();
            } else {
                for (StructureBlockInfo jigsaw : nextJigsaws) {
                    if (jigsaw.nbt().getString("target").equals(this.pathJigsawName)) {
                        this.numPaths++;
                    }
                }
            }

            for (StructureBlockInfo jigsaw : nextJigsaws) {
                this.placing.add(new Pair<>(jigsaw, nextPieceState));
            }

            pieceSelection.pieceState.children.add(nextPieceState);
        }

    }

    static final class FallbackPlacer extends Placer {

        FallbackPlacer(Registry<StructurePool> p_210323_, int p_210324_, ChunkGenerator p_210326_, StructureTemplateManager p_210327_, List<? super PoolStructurePiece> p_210328_, Random p_210329_,
                       String pathJigsawName, String interiorJigsawName,
                       MutableObject<VoxelShape> free, MutableObject<VoxelShape> interiorFree, MutableObject<Map<String, VoxelShape>> specialBounds,
                       Placer previousPlacer) {
            super(p_210323_, p_210324_, p_210326_, p_210327_, p_210328_, p_210329_, pathJigsawName, interiorJigsawName, free, interiorFree, specialBounds);
            this.placing.addAll(previousPlacer.placing);
            this.placing.addAll(previousPlacer.fallbacks);
            this.interior.addAll(previousPlacer.interior);
            this.numPaths = previousPlacer.numPaths;
        }

        List<StructurePoolElement> addPoolElements(PieceState pieceState, StructurePool pool, StructurePool fallbackPool) {
            List<StructurePoolElement> structurePoolElements = Lists.newArrayList();
            structurePoolElements.addAll(fallbackPool.getElementIndicesInRandomOrder(this.random));
            return structurePoolElements;
        }
    }

    static final class InteriorPlacer extends Placer {

        InteriorPlacer(Registry<StructurePool> p_210323_, int p_210324_, ChunkGenerator p_210326_, StructureTemplateManager p_210327_, List<? super PoolStructurePiece> p_210328_, Random p_210329_,
                       String pathJigsawName, String interiorJigsawName,
                       MutableObject<VoxelShape> free, MutableObject<VoxelShape> interiorFree, MutableObject<Map<String, VoxelShape>> specialBounds,
                       Placer previousPlacer) {
            super(p_210323_, p_210324_, p_210326_, p_210327_, p_210328_, p_210329_, pathJigsawName, interiorJigsawName, free, interiorFree, specialBounds);
            this.placing.addAll(previousPlacer.interior);
            this.free = interiorFree;
            this.numPaths = previousPlacer.numPaths;
        }

        @Override
        protected boolean skipJigsawBlock(StructureBlockInfo thisPieceJigsawBlock, PieceState pieceState) {
            return false;
        }

        @Override
        protected boolean checkBounds(StructurePoolElement nextPieceCandidate, PieceState pieceState, BlockBox nextPieceBoundingBoxPlaced, BlockPos nextPiecePos, BlockRotation nextPieceRotation, int i2) {
            boolean ignoreBounds = false;
            BlockBox spaceCheckBounds = nextPieceBoundingBoxPlaced;
            if (nextPieceCandidate instanceof MowziePoolElement) {
                ignoreBounds = ((MowziePoolElement) nextPieceCandidate).ignoresBounds();
                spaceCheckBounds = ((MowziePoolElement) nextPieceCandidate).getCheckBoundingBox(this.structureManager, nextPiecePos, nextPieceRotation).offset(0, i2, 0);
            }
            return ignoreBounds || !VoxelShapes.matchesAnywhere(this.free.getValue(), VoxelShapes.cuboid(Box.from(spaceCheckBounds).contract(0.25D)), BooleanBiFunction.ONLY_SECOND);
        }
    }

    /* DOESNT WORK. Maybe try again later
    static final class DeadEndConnectorPlacer extends Placer {
        Map<BlockPos, BlockPos> destinations = new HashMap<>();
        private Set<String> mustConnectPools;
        private Set<String> toReplacePools;
        private String deadEndConnectorPool;

        DeadEndConnectorPlacer(Registry<StructureTemplatePool> p_210323_, int p_210324_, PieceFactory p_210325_, ChunkGenerator p_210326_, StructureManager p_210327_, List<? super PoolElementStructurePiece> p_210328_, Random p_210329_, String pathJigsawName, Placer oldPlacer, Set<String> mustConnectPools, Set<String> toReplacePools, String deadEndConnectorPool) {
            super(p_210323_, p_210324_, p_210325_, p_210326_, p_210327_, p_210328_, p_210329_, pathJigsawName);
            this.numPaths = oldPlacer.numPaths;
            this.mustConnectPools = mustConnectPools;
            this.toReplacePools = toReplacePools;
            this.deadEndConnectorPool = deadEndConnectorPool;
            List<StructureBlockInfo> needConnecting = new ArrayList<>();
            while (!oldPlacer.placing.isEmpty()) {
                Pair<StructureBlockInfo, PieceState> toPlace = oldPlacer.placing.removeFirst();
                StructureBlockInfo jigsawBlock = toPlace.getFirst();
                PieceState pieceState = toPlace.getSecond();
                if (jigsawMustConnect(jigsawBlock) && canJigsawBlockFitNextPiece(jigsawBlock, pieceState.free)) {
                    needConnecting.add(jigsawBlock);
                    this.placing.addLast(toPlace);
                }
            }

            // Match each pos with its closest neighbor - naive approach for now. Robust approach needs Kuhn�밠unkres algorithm
            Set<StructureBlockInfo> used = new HashSet<>();
            for (StructureBlockInfo block1 : needConnecting) {
                if (used.contains(block1)) continue;
                StructureBlockInfo closest = null;
                double bestDist = Double.MAX_VALUE;
                for (StructureBlockInfo block2 : needConnecting) {
                    if (block1 == block2 || used.contains(block2)) continue;
                    double dist = block1.pos().distSqr(block2.pos());
                    if (dist < bestDist) {
                        bestDist = dist;
                        closest = block2;
                    }
                }
                if (closest != null) {
                    destinations.put(block1.pos(), closest.pos());
                    destinations.put(closest.pos(), block1.pos());
                    used.add(block1);
                    used.add(closest);
                }
            }
        }

        // Replace the pool with dead end connector pool
        @Override
        String selectPool(StructureBlockInfo thisPieceJigsawBlock) {
            String pool = super.selectPool(thisPieceJigsawBlock);
            if (toReplacePools != null && toReplacePools.contains(pool)) {
                pool = deadEndConnectorPool;
            }
            return pool;
        }

        @Override
        List<StructurePoolElement> addPoolElements(PieceState pieceState, StructureTemplatePool pool, StructureTemplatePool fallbackPool) {
            List<StructurePoolElement> structurePoolElements = Lists.newArrayList();
            structurePoolElements.addAll(pool.getShuffledTemplates(this.random));
            return structurePoolElements;
        }

        boolean jigsawMustConnect(StructureBlockInfo jigsaw) {
            return mustConnectPools.contains(jigsaw.nbt().getString("pool"));
        }

        @Override
        void addNextPieceState(PieceSelection pieceSelection) {
            super.addNextPieceState(pieceSelection);
        }

        @Override
        PieceSelection selectPiece(PieceState pieceState, StructureTemplatePool pool, StructureTemplatePool fallbackPool, boolean villageBoundaryAdjust, StructureBlockInfo thisPieceJigsawBlock, LevelHeightAccessor heightAccessor) {
            // If this isn't coming after a must-connect jigsaw block, use normal placement
            if (!jigsawMustConnect(thisPieceJigsawBlock)) return super.selectPiece(pieceState, pool, fallbackPool, villageBoundaryAdjust, thisPieceJigsawBlock, heightAccessor);

            // If no destination, stop placing
            BlockPos destination = this.destinations.get(thisPieceJigsawBlock.pos());
            if (destination == null) return null;
            if (thisPieceJigsawBlock.pos().distSqr(destination) <= 1) return null;

            BoundingBox thisPieceBoundingBox = pieceState.piece.getBoundingBox();
            int thisPieceMinY = thisPieceBoundingBox.minY();
            Direction direction = JigsawBlock.getFrontFacing(thisPieceJigsawBlock.state);
            BlockPos thisJigsawBlockPos = thisPieceJigsawBlock.pos();
            BlockPos nextJigsawBlockPos = thisJigsawBlockPos.relative(direction);
            int thisPieceHeightFromBottomToJigsawBlock = thisJigsawBlockPos.getY() - thisPieceMinY;
            StructurePoolElement structurepoolelement = pieceState.piece.getElement();
            StructureTemplatePool.Projection structuretemplatepool$projection = structurepoolelement.getProjection();
            boolean thisPieceIsRigid = structuretemplatepool$projection == StructureTemplatePool.Projection.RIGID;
            int k = -1;

            float closestDist = Float.MAX_VALUE;
            List<PieceSelection> rejects = new ArrayList<>();
            PieceSelection pieceSelection = null;
            boolean ignoreBounds = false;
            // Loop through pool elements
            List<StructurePoolElement> structurePoolElements = addPoolElements(pieceState, pool, fallbackPool);
            Set<StructurePoolElement> structurePoolElementSet = new HashSet<>(structurePoolElements);
            for(StructurePoolElement nextPieceCandidate : structurePoolElementSet) {
                // If empty element, break from the loop and spawn nothing
                if (nextPieceCandidate instanceof SinglePoolElement && ((SinglePoolElement) nextPieceCandidate).toString().equals("Single[Left[minecraft:empty]]")) {
                    break;
                }

                if (nextPieceCandidate instanceof MowziePoolElement) {
                    MowziePoolElement mowziePoolElement = (MowziePoolElement) nextPieceCandidate;
                    if (!mowziePoolElement.checkCriteria(pieceState, this)) continue;
                }

                // Iterate through possible rotations of this element
                for (Rotation nextPieceRotation : Rotation.getShuffled(this.random)) {
                    List<StructureBlockInfo> nextPieceJigsawBlocks = nextPieceCandidate.getShuffledJigsawBlocks(this.structureManager, BlockPos.ZERO, nextPieceRotation, this.random);
                    BoundingBox nextPieceBoundingBoxOrigin = nextPieceCandidate.getBoundingBox(this.structureManager, BlockPos.ZERO, nextPieceRotation);
                    int largestSizeOfNextNextPiece;
                    // Village boundary adjustment
                    if (villageBoundaryAdjust && nextPieceBoundingBoxOrigin.getYSpan() <= 16) {
                        // Map each jigsaw block to the size of the largest next piece it can generate and largestSizeOfNextNextPiece becomes the largest of these
                        largestSizeOfNextNextPiece = nextPieceJigsawBlocks.stream().mapToInt((blockInfo) -> {
                            // If the next piece's jigsaw block would place inside its own bounding box, return 0
                            if (!nextPieceBoundingBoxOrigin.isInside(blockInfo.pos().relative(JigsawBlock.getFrontFacing(blockInfo.state)))) {
                                return 0;
                            } else {
                                ResourceLocation resourcelocation2 = new ResourceLocation(blockInfo.nbt().getString("pool"));
                                Optional<StructureTemplatePool> optional2 = this.pools.getOptional(resourcelocation2);
                                Optional<StructureTemplatePool> optional3 = optional2.flatMap((p_210344_) -> {
                                    return this.pools.getOptional(p_210344_.getFallback());
                                });
                                int j3 = optional2.map((structureTemplatePool) -> {
                                    return structureTemplatePool.getMaxSize(this.structureManager);
                                }).orElse(0);
                                int k3 = optional3.map((structureTemplatePool) -> {
                                    return structureTemplatePool.getMaxSize(this.structureManager);
                                }).orElse(0);
                                return Math.max(j3, k3);
                            }
                        }).max().orElse(0);
                    } else {
                        largestSizeOfNextNextPiece = 0;
                    }

                    for (StructureBlockInfo nextPieceJigsawBlock : nextPieceJigsawBlocks) {
                        boolean canAttach;
                        if (nextPieceCandidate instanceof MowziePoolElement && ((MowziePoolElement) nextPieceCandidate).twoWay()) {
                            canAttach = MowziePoolElement.canAttachTwoWays(thisPieceJigsawBlock, nextPieceJigsawBlock);
                        } else {
                            canAttach = JigsawBlock.canAttach(thisPieceJigsawBlock, nextPieceJigsawBlock);
                        }
                        if (canAttach) {
                            BlockPos nextPieceJigsawBlockPos = nextPieceJigsawBlock.pos();
                            BlockPos nextPiecePos = nextJigsawBlockPos.subtract(nextPieceJigsawBlockPos);
                            BoundingBox nextPieceBoundingBox = nextPieceCandidate.getBoundingBox(this.structureManager, nextPiecePos, nextPieceRotation);
                            int nextPieceMinY = nextPieceBoundingBox.minY();
                            if (nextPieceCandidate instanceof MowziePoolElement) {
                                nextPieceMinY -= ((MowziePoolElement) nextPieceCandidate).bounds.boundsMinOffset.getY();
                            }
                            StructureTemplatePool.Projection structuretemplatepool$projection1 = nextPieceCandidate.getProjection();
                            boolean nextPieceIsRigid = structuretemplatepool$projection1 == StructureTemplatePool.Projection.RIGID;
                            int nextPieceJigsawBlockY = nextPieceJigsawBlockPos.getY();
                            int k1 = thisPieceHeightFromBottomToJigsawBlock - nextPieceJigsawBlockY + JigsawBlock.getFrontFacing(thisPieceJigsawBlock.state).getStepY();
                            int l1;
                            if (thisPieceIsRigid && nextPieceIsRigid) {
                                l1 = thisPieceMinY + k1;
                            } else {
                                if (k == -1) {
                                    k = this.chunkGenerator.getFirstFreeHeight(thisJigsawBlockPos.getX(), thisJigsawBlockPos.getZ(), Heightmap.Types.WORLD_SURFACE_WG, heightAccessor);
                                }

                                l1 = k - nextPieceJigsawBlockY;
                            }

                            int i2 = l1 - nextPieceMinY;
                            BoundingBox nextPieceBoundingBoxPlaced = nextPieceBoundingBox.moved(0, i2, 0);
                            BlockPos blockpos5 = nextPiecePos.offset(0, i2, 0);
                            if (largestSizeOfNextNextPiece > 0) {
                                int j2 = Math.max(largestSizeOfNextNextPiece + 1, nextPieceBoundingBoxPlaced.maxY() - nextPieceBoundingBoxPlaced.minY());
                                nextPieceBoundingBoxPlaced.encapsulate(new BlockPos(nextPieceBoundingBoxPlaced.minX(), nextPieceBoundingBoxPlaced.minY() + j2, nextPieceBoundingBoxPlaced.minZ()));
                            }

                            // Check height params
                            if (nextPieceCandidate instanceof MowziePoolElement) {
                                Optional<Integer> maxHeight = ((MowziePoolElement) nextPieceCandidate).maxHeight;
                                Optional<Integer> minHeight = ((MowziePoolElement) nextPieceCandidate).minHeight;
                                if (maxHeight.isPresent() || minHeight.isPresent()) {
                                    int freeHeight = this.chunkGenerator.getFirstFreeHeight(nextPieceBoundingBoxPlaced.minX() + nextPieceBoundingBoxPlaced.getXSpan() / 2, nextPieceBoundingBoxPlaced.minZ() + nextPieceBoundingBoxPlaced.getZSpan() / 2, Heightmap.Types.WORLD_SURFACE_WG, heightAccessor);
                                    if (maxHeight.isPresent() && nextPieceMinY - freeHeight > maxHeight.get()) continue;
                                    if (minHeight.isPresent() && nextPieceMinY - freeHeight < minHeight.get()) continue;
                                }
                            }

                            if (nextPieceCandidate instanceof MowziePoolElement)
                                ignoreBounds = ((MowziePoolElement) nextPieceCandidate).ignoresBounds();
                            if (ignoreBounds || !Shapes.joinIsNotEmpty(pieceState.free.getValue(), Shapes.create(AABB.of(nextPieceBoundingBoxPlaced).deflate(0.25D)), BooleanOp.ONLY_SECOND)) {
                                // Special bounding boxes
                                if (nextPieceCandidate instanceof MowziePoolElement) {
                                    MowziePoolElement mowziePoolElement = (MowziePoolElement) nextPieceCandidate;

                                    Optional<String> needsOverlapBounds = mowziePoolElement.bounds.needsOverlapBounds;
                                    if (needsOverlapBounds.isPresent() && pieceState.specialBounds.getValue().containsKey(needsOverlapBounds.get())) {
                                        if (!Shapes.joinIsNotEmpty(pieceState.specialBounds.getValue().get(needsOverlapBounds.get()), Shapes.create(AABB.of(nextPieceBoundingBoxPlaced).deflate(0.25D)), BooleanOp.AND)) {
                                            continue;
                                        }
                                    }
                                }

                                int pieceGroundLevelDelta = pieceState.piece.getGroundLevelDelta();
                                int k2;
                                if (nextPieceIsRigid) {
                                    k2 = pieceGroundLevelDelta - k1;
                                } else {
                                    k2 = nextPieceCandidate.getGroundLevelDelta();
                                }

                                PoolElementStructurePiece poolelementstructurepiece = this.factory.create(this.structureManager, nextPieceCandidate, blockpos5, k2, nextPieceRotation, nextPieceBoundingBoxPlaced);
                                int l2;
                                if (thisPieceIsRigid) {
                                    l2 = thisPieceMinY + thisPieceHeightFromBottomToJigsawBlock;
                                } else if (nextPieceIsRigid) {
                                    l2 = l1 + nextPieceJigsawBlockY;
                                } else {
                                    if (k == -1) {
                                        k = this.chunkGenerator.getFirstFreeHeight(thisJigsawBlockPos.getX(), thisJigsawBlockPos.getZ(), Heightmap.Types.WORLD_SURFACE_WG, heightAccessor);
                                    }

                                    l2 = k + k1 / 2;
                                }

                                StructureBlockInfo nextJigsaw = null;
                                StructurePoolElement nextStructurepoolelement = poolelementstructurepiece.getElement();
                                BlockPos blockpos = poolelementstructurepiece.getPosition();
                                Rotation rotation = poolelementstructurepiece.getRotation();
                                for (StructureBlockInfo otherJigsaw : nextStructurepoolelement.getShuffledJigsawBlocks(this.structureManager, blockpos, rotation, this.random)) {
                                    if (jigsawMustConnect(otherJigsaw)) {
                                        if (otherJigsaw.pos().distSqr(destination) <= 1) {
                                            // Connected!
                                            this.destinations.remove(thisPieceJigsawBlock.pos());
                                            this.destinations.remove(destination);
                                            return new PieceSelection(pieceState, structurepoolelement, nextPieceCandidate, poolelementstructurepiece, thisPieceJigsawBlock, nextPieceJigsawBlock, null, nextPieceBoundingBoxPlaced, l2);
                                        }
                                        nextJigsaw = otherJigsaw;
                                        break;
//                                        if (canJigsawBlockFitNextPiece(otherJigsaw, pieceState.free)) {
//                                            nextJigsaw = otherJigsaw;
//                                            break;
//                                        }
                                    }
                                }
                                if (nextJigsaw == null) continue;

                                float distance = (float) nextJigsaw.pos().distSqr(destination);
                                PieceSelection thisPieceSelection = new PieceSelection(pieceState, structurepoolelement, nextPieceCandidate, poolelementstructurepiece, thisPieceJigsawBlock, nextPieceJigsawBlock, nextJigsaw, nextPieceBoundingBoxPlaced, l2);
                                if (distance < closestDist) {
                                    rejects.add(pieceSelection);
                                    pieceSelection = thisPieceSelection;
                                    closestDist = distance;
                                }
                                else rejects.add(thisPieceSelection);
                            }
                        }
                    }
                }
            }
            if (pieceSelection != null) {
                this.destinations.remove(pieceSelection.origJigsaw.pos());
                this.destinations.put(pieceSelection.nextJigsaw.pos(), destination);
                this.destinations.put(destination, pieceSelection.nextJigsaw.pos());
            }
            return pieceSelection;
        }
    }*/
}

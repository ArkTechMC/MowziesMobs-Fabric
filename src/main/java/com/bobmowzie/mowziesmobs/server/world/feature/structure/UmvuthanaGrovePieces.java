package com.bobmowzie.mowziesmobs.server.world.feature.structure;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.block.BlockHandler;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.umvuthana.EntityUmvuthanaMinion;
import com.bobmowzie.mowziesmobs.server.entity.umvuthana.EntityUmvuthi;
import com.bobmowzie.mowziesmobs.server.entity.umvuthana.MaskType;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import com.bobmowzie.mowziesmobs.server.item.ItemUmvuthanaMask;
import com.bobmowzie.mowziesmobs.server.loot.LootTableHandler;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import net.minecraft.block.*;
import net.minecraft.block.enums.SlabType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.state.property.Properties;
import net.minecraft.structure.*;
import net.minecraft.structure.processor.BlockIgnoreStructureProcessor;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.ChunkRandom;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Heightmap;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.level.block.*;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class UmvuthanaGrovePieces {
    public static final Identifier PLATFORM_1 = new Identifier(MowziesMobs.MODID, "umvuthana/umvuthana_platform_1");
    public static final Identifier PLATFORM_2 = new Identifier(MowziesMobs.MODID, "umvuthana/umvuthana_platform_2");
    public static final Identifier[] PLATFORMS = new Identifier[]{
            PLATFORM_1,
            PLATFORM_2
    };
    public static final Identifier PLATFORM_EXTEND = new Identifier(MowziesMobs.MODID, "umvuthana/umvuthana_platform_extend");
    public static final Identifier FIREPIT = new Identifier(MowziesMobs.MODID, "umvuthana/umvuthana_firepit");
    public static final Identifier FIREPIT_SMALL_1 = new Identifier(MowziesMobs.MODID, "umvuthana/umvuthana_firepit_small_1");
    public static final Identifier FIREPIT_SMALL_2 = new Identifier(MowziesMobs.MODID, "umvuthana/umvuthana_firepit_small_2");
    public static final Identifier[] FIREPIT_SMALL = new Identifier[]{
            FIREPIT_SMALL_1,
            FIREPIT_SMALL_2
    };
    public static final Identifier TREE_1 = new Identifier(MowziesMobs.MODID, "umvuthana/umvuthana_tree_1");
    public static final Identifier TREE_2 = new Identifier(MowziesMobs.MODID, "umvuthana/umvuthana_tree_2");
    public static final Identifier TREE_3 = new Identifier(MowziesMobs.MODID, "umvuthana/umvuthana_tree_3");
    public static final Identifier[] TREES = new Identifier[]{
            TREE_1,
            TREE_2,
            TREE_3
    };
    public static final Identifier SPIKE_1 = new Identifier(MowziesMobs.MODID, "umvuthana/umvuthana_spike_1");
    public static final Identifier SPIKE_2 = new Identifier(MowziesMobs.MODID, "umvuthana/umvuthana_spike_2");
    public static final Identifier SPIKE_3 = new Identifier(MowziesMobs.MODID, "umvuthana/umvuthana_spike_3");
    public static final Identifier SPIKE_4 = new Identifier(MowziesMobs.MODID, "umvuthana/umvuthana_spike_4");
    public static final Identifier[] SPIKES = new Identifier[]{
            SPIKE_1,
            SPIKE_2,
            SPIKE_3,
            SPIKE_4
    };
    public static final Identifier THRONE = new Identifier(MowziesMobs.MODID, "umvuthana/umvuthi_throne");
    private static final Set<Block> BLOCKS_NEEDING_POSTPROCESSING = ImmutableSet.<Block>builder().add(Blocks.NETHER_BRICK_FENCE).add(Blocks.TORCH).add(Blocks.WALL_TORCH).add(Blocks.OAK_FENCE).add(Blocks.SPRUCE_FENCE).add(Blocks.DARK_OAK_FENCE).add(Blocks.ACACIA_FENCE).add(Blocks.BIRCH_FENCE).add(Blocks.JUNGLE_FENCE).add(Blocks.MANGROVE_FENCE).add(Blocks.LADDER).add(Blocks.SKELETON_SKULL).build();
    private static final Map<Identifier, BlockPos> OFFSET = ImmutableMap.<Identifier, BlockPos>builder()
            .put(PLATFORM_1, new BlockPos(-5, 0, -5))
            .put(PLATFORM_2, new BlockPos(0, 0, -5))
            .put(PLATFORM_EXTEND, new BlockPos(8, 1, -2))
            .put(FIREPIT, new BlockPos(-3, -2, -3))
            .put(FIREPIT_SMALL_1, new BlockPos(-1, 0, -1))
            .put(FIREPIT_SMALL_2, new BlockPos(-1, 0, -1))
            .put(TREE_1, new BlockPos(-5, 1, -3))
            .put(TREE_2, new BlockPos(-3, 1, -3))
            .put(TREE_3, new BlockPos(-3, 1, -3))
            .put(SPIKE_1, new BlockPos(-1, 1, 0))
            .put(SPIKE_2, new BlockPos(0, 1, 0))
            .put(SPIKE_3, new BlockPos(0, 1, 0))
            .put(SPIKE_4, new BlockPos(0, 1, 0))
            .put(THRONE, new BlockPos(-9, 0, 0))
            .build();

    private static final Map<Identifier, Pair<BlockPos, BlockPos>> BOUNDS_OFFSET = ImmutableMap.<Identifier, Pair<BlockPos, BlockPos>>builder()
            .put(PLATFORM_1, new Pair<>(new BlockPos(1, 0, 0), new BlockPos(-3, 0, -3)))
            .put(PLATFORM_2, new Pair<>(new BlockPos(0, 0, 0), new BlockPos(0, 0, -3)))
            .put(PLATFORM_EXTEND, new Pair<>(new BlockPos(0, 0, 0), new BlockPos(0, 0, 0)))
            .put(FIREPIT, new Pair<>(new BlockPos(0, 0, 0), new BlockPos(0, 0, 0)))
            .put(FIREPIT_SMALL_1, new Pair<>(new BlockPos(0, 0, 0), new BlockPos(0, 0, 0)))
            .put(FIREPIT_SMALL_2, new Pair<>(new BlockPos(0, 0, 0), new BlockPos(0, 0, 0)))
            .put(TREE_1, new Pair<>(new BlockPos(1, 0, 1), new BlockPos(-3, 0, -3)))
            .put(TREE_2, new Pair<>(new BlockPos(2, 0, 1), new BlockPos(-1, 0, -3)))
            .put(TREE_3, new Pair<>(new BlockPos(2, 0, 2), new BlockPos(-2, 0, -2)))
            .put(SPIKE_1, new Pair<>(new BlockPos(0, 0, 0), new BlockPos(0, 0, 0)))
            .put(SPIKE_2, new Pair<>(new BlockPos(0, 0, 0), new BlockPos(0, 0, 0)))
            .put(SPIKE_3, new Pair<>(new BlockPos(0, 0, 0), new BlockPos(0, 0, 0)))
            .put(SPIKE_4, new Pair<>(new BlockPos(0, 0, 0), new BlockPos(0, 0, 0)))
            .put(THRONE, new Pair<>(new BlockPos(4, 0, 1), new BlockPos(-4, 0, -3)))
            .build();

    public static StructurePiece addPiece(Identifier resourceLocation, StructureTemplateManager manager, BlockPos pos, BlockRotation rot, StructurePiecesHolder pieces, ChunkRandom rand) {
        StructurePiece newPiece = new Piece(manager, resourceLocation, rot, pos);
        pieces.addPiece(newPiece);
        return newPiece;
    }

    public static StructurePiece addPieceCheckBounds(Identifier resourceLocation, StructureTemplateManager manager, BlockPos pos, BlockRotation rot, StructurePiecesHolder pieces, ChunkRandom rand, List<StructurePiece> ignore) {
        Piece newPiece = new Piece(manager, resourceLocation, rot, pos);
        StructurePiece collisionPiece = pieces.getIntersecting(newPiece.getCollisionBoundingBox());
        if (collisionPiece != null && !ignore.contains(collisionPiece)) return null;
        pieces.addPiece(newPiece);
        return newPiece;
    }

    public static StructurePiece addPlatform(StructureTemplateManager manager, BlockPos pos, BlockRotation rot, StructurePiecesCollector builder, ChunkRandom rand) {
        int whichPlatform = rand.nextInt(PLATFORMS.length);
        Piece newPiece = new Piece(manager, PLATFORMS[whichPlatform], rot, pos);
        if (findCollisionPiece(builder.pieces, newPiece.getCollisionBoundingBox()) != null) return null;
        builder.addPiece(newPiece);
        if (whichPlatform == 1) {
            Piece extension = new Piece(manager, PLATFORM_EXTEND, rot, pos);
            if (findCollisionPiece(builder.pieces, extension.getCollisionBoundingBox(), newPiece) != null)
                return newPiece;
            builder.addPiece(extension);
        }
        return newPiece;
    }

    public static StructurePiece addPieceCheckBounds(Identifier resourceLocation, StructureTemplateManager manager, BlockPos pos, BlockRotation rot, StructurePiecesCollector pieces, ChunkRandom rand) {
        return addPieceCheckBounds(resourceLocation, manager, pos, rot, pieces, rand, Collections.emptyList());
    }

    @Nullable
    public static StructurePiece findCollisionPiece(List<StructurePiece> pieces, BlockBox bounds, StructurePiece ignore) {
        for (StructurePiece structurePiece : pieces) {
            if (structurePiece == ignore) continue;
            if (structurePiece instanceof Piece && ((Piece) structurePiece).getCollisionBoundingBox().intersects(bounds)) {
                return structurePiece;
            } else if (structurePiece.getBoundingBox().intersects(bounds)) {
                return structurePiece;
            }
        }
        return null;
    }

    @Nullable
    public static StructurePiece findCollisionPiece(List<StructurePiece> pieces, BlockBox bounds) {
        return findCollisionPiece(pieces, bounds, null);
    }

    public static class Piece extends SimpleStructurePiece {
        public BlockBox collisionBoundingBox;
        protected Identifier resourceLocation;

        public Piece(StructurePieceType pieceType, StructureTemplateManager manager, Identifier resourceLocationIn, BlockRotation rotation, BlockPos pos) {
            super(pieceType, 0, manager, resourceLocationIn, resourceLocationIn.toString(), makeSettings(rotation, resourceLocationIn), makePosition(resourceLocationIn, pos, rotation));
            this.resourceLocation = resourceLocationIn;
            this.collisionBoundingBox = this.makeCollisionBoundingBox();
            if (this.resourceLocation == THRONE || this.resourceLocation == FIREPIT)
                this.boundingBox = this.getBoundingBox().offset(0, 1, 0);
        }

        public Piece(StructurePieceType pieceType, StructureContext context, NbtCompound tagCompound) {
            super(pieceType, tagCompound, context.structureTemplateManager(), (resourceLocation) -> makeSettings(BlockRotation.valueOf(tagCompound.getString("Rot")), resourceLocation));
            this.collisionBoundingBox = this.makeCollisionBoundingBox();
            if (this.resourceLocation == THRONE || this.resourceLocation == FIREPIT)
                this.boundingBox = this.getBoundingBox().offset(0, 1, 0);
        }

        public Piece(StructureTemplateManager manager, Identifier resourceLocationIn, BlockRotation rotation, BlockPos pos) {
            this(StructureTypeHandler.UMVUTHANA_GROVE_PIECE, manager, resourceLocationIn, rotation, pos);
        }

        public Piece(StructureContext context, NbtCompound tagCompound) {
            this(StructureTypeHandler.UMVUTHANA_GROVE_PIECE, context, tagCompound);
        }

        private static StructurePlacementData makeSettings(BlockRotation rotation, Identifier resourceLocation) {
            return (new StructurePlacementData()).setRotation(rotation).setMirror(BlockMirror.NONE).addProcessor(BlockIgnoreStructureProcessor.IGNORE_STRUCTURE_BLOCKS);
        }

        private static BlockPos makePosition(Identifier resourceLocation, BlockPos pos, BlockRotation rotation) {
            return pos.add(UmvuthanaGrovePieces.OFFSET.get(resourceLocation).rotate(rotation));
        }

        public BlockBox makeCollisionBoundingBox() {
            StructureTemplate structuretemplate = this.template;
            BlockPos boundsMinOffset, boundsMaxOffset;
            boundsMinOffset = boundsMaxOffset = new BlockPos(0, 0, 0);
            Pair<BlockPos, BlockPos> boundsOffset = BOUNDS_OFFSET.get(this.resourceLocation);
            if (boundsOffset != null) {
                boundsMinOffset = boundsOffset.getFirst();
                boundsMaxOffset = boundsOffset.getSecond();
            }

            Vec3i sizeVec = structuretemplate.getSize().add(-1, -1, -1);
            BlockPos blockpos = StructureTemplate.transformAround(BlockPos.ORIGIN.add(boundsMinOffset), this.placementData.getMirror(), this.placementData.getRotation(), this.placementData.getPosition());
            BlockPos blockpos1 = StructureTemplate.transformAround(BlockPos.ORIGIN.add(sizeVec).add(boundsMaxOffset), this.placementData.getMirror(), this.placementData.getRotation(), this.placementData.getPosition());
            return BlockBox.create(blockpos, blockpos1).move(this.pos);
        }

        public BlockBox getCollisionBoundingBox() {
            return this.collisionBoundingBox;
        }

        /**
         * (abstract) Helper method to read subclass data from NBT
         */
        @Override
        protected void writeNbt(StructureContext context, NbtCompound tagCompound) {
            super.writeNbt(context, tagCompound);
            tagCompound.putString("Rot", this.placementData.getRotation().name());
        }

        @Override
        public void generate(StructureWorldAccess p_192682_, StructureAccessor p_192683_, ChunkGenerator p_192684_, Random p_192685_, BlockBox p_192686_, ChunkPos p_192687_, BlockPos p_192688_) {
            super.generate(p_192682_, p_192683_, p_192684_, p_192685_, p_192686_, p_192687_, p_192688_);
        }

        /*
         * If you added any data marker structure blocks to your structure, you can access and modify them here. In this case,
         * our structure has a data maker with the string "chest" put into it. So we check to see if the incoming function is
         * "chest" and if it is, we now have that exact position.
         *
         * So what is done here is we replace the structure block with a chest and we can then set the loottable for it.
         *
         * You can set other data markers to do other behaviors such as spawn a random mob in a certain spot, randomize what
         * rare block spawns under the floor, or what item an Item Frame will have.
         */
        @Override
        protected void handleMetadata(String function, BlockPos pos, ServerWorldAccess worldIn, Random rand, BlockBox sbb) {
            BlockRotation rotation = this.placementData.getRotation();
            if (function.equals("support")) {
                worldIn.setBlockState(pos, Blocks.OAK_FENCE.getDefaultState(), 3);
                this.fillAirLiquidDown(worldIn, Blocks.OAK_FENCE.getDefaultState(), pos.down());
            } else if (function.equals("trunk")) {
                this.fillAirLiquidDownTrunk(worldIn, pos, rand);
            } else if (function.equals("leg")) {
                this.fillAirLiquidDown(worldIn, Blocks.STRIPPED_MANGROVE_LOG.getDefaultState(), pos);
            } else if (function.equals("base")) {
                this.fillAirLiquidDownBase(worldIn, pos, rand);
            } else if (function.equals("umvuthi")) {
                this.setBlockState(worldIn, pos, Blocks.AIR.getDefaultState());
                EntityUmvuthi barako = new EntityUmvuthi(EntityHandler.UMVUTHI, worldIn.toServerWorld());
                barako.setPosition(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
                int i = rotation.rotate(3, 4);
                barako.setDirection(i);
                barako.initialize(worldIn, worldIn.getLocalDifficulty(barako.getBlockPos()), SpawnReason.STRUCTURE, null, null);
                BlockPos offset = new BlockPos(0, 0, -18);
                offset = offset.rotate(rotation);
                BlockPos firePitPos = pos.add(offset);
                firePitPos = worldIn.getTopPosition(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, firePitPos);
                barako.setPositionTarget(firePitPos, -1);
                worldIn.spawnEntity(barako);
            } else if ("chest".equals(function)) {
                Direction facing = Direction.NORTH;
                facing = rotation.rotate(facing);
                this.addChest(worldIn, sbb, rand, pos, LootTableHandler.UMVUTHANA_GROVE_CHEST, Blocks.CHEST.getDefaultState().with(Properties.HORIZONTAL_FACING, facing));
            } else if ("skull".equals(function)) {
                BlockPos groundPos = this.getGroundPos(worldIn, pos);
                this.setBlockState(worldIn, groundPos.up(), Blocks.SKELETON_SKULL.getDefaultState().with(Properties.ROTATION, rand.nextInt(16)));
            } else if ("campfire".equals(function)) {
                BlockPos groundPos = this.getGroundPos(worldIn, pos);
                this.setBlockState(worldIn, groundPos.up(), Blocks.CAMPFIRE.getDefaultState());
            } else if (function.length() > 5 && "spike".equals(function.substring(0, 5))) {
                String[] split = function.split("_");
                int logCount = 2;
                int fenceCount = 1;
                int barCount = 1;
                int skullCount = 0;
                if (split.length > 1) logCount = Integer.parseInt(split[1]);
                if (split.length > 2) fenceCount = Integer.parseInt(split[2]);
                if (split.length > 3) barCount = Integer.parseInt(split[3]);
                if (split.length > 4) skullCount = Integer.parseInt(split[4]);
                this.genSpike(worldIn, pos, rand, logCount, fenceCount, barCount, skullCount);
            } else if (function.length() > 6 && "stairs".equals(function.substring(0, 6))) {
                String[] split = function.split("_");
                Direction stairDirection = Direction.EAST;
                Direction newDirection = null;
                if (split.length > 1) newDirection = Direction.byName(split[1]);
                if (newDirection != null) stairDirection = newDirection;
                stairDirection = rotation.rotate(stairDirection);
                this.genStairs(worldIn, pos, rand, stairDirection);
            } else if ("chest_under".equals(function)) {
                if (rand.nextFloat() < 0.5) worldIn.removeBlock(pos, false);
                else {
                    BlockPos groundPos = this.getGroundPos(worldIn, pos);
                    Direction facing = rand.nextFloat() < 0.5 ? Direction.NORTH : Direction.EAST;
                    facing = rotation.rotate(facing);
                    this.addChest(worldIn, sbb, rand, groundPos.up(), LootTableHandler.UMVUTHANA_GROVE_CHEST, Blocks.CHEST.getDefaultState().with(Properties.HORIZONTAL_FACING, facing));
                }
            } else if (function.length() > 4 && "mask".equals(function.substring(0, 4))) {
                worldIn.removeBlock(pos, false);
                String[] split = function.split("_");
                Direction direction = Direction.NORTH;
                if (split.length > 1) direction = Direction.byName(split[1]);
                ItemFrameEntity itemFrame = new ItemFrameEntity(worldIn.toServerWorld(), pos, rotation.rotate(direction));
                int i = rand.nextInt(MaskType.values().length);
                MaskType type = MaskType.values()[i];
                ItemUmvuthanaMask mask = switch (type) {
                    case BLISS -> ItemHandler.UMVUTHANA_MASK_BLISS;
                    case FEAR -> ItemHandler.UMVUTHANA_MASK_FEAR;
                    case FURY -> ItemHandler.UMVUTHANA_MASK_FURY;
                    case MISERY -> ItemHandler.UMVUTHANA_MASK_MISERY;
                    case RAGE -> ItemHandler.UMVUTHANA_MASK_RAGE;
                    case FAITH -> ItemHandler.UMVUTHANA_MASK_FAITH;
                };
                ItemStack stack = new ItemStack(mask);
                itemFrame.setHeldItemStack(stack, false);
                worldIn.spawnEntity(itemFrame);
            } else {
                worldIn.removeBlock(pos, false);
            }
        }

        protected void setBlockState(WorldAccess worldIn, BlockPos pos, BlockState state) {
            FluidState ifluidstate = worldIn.getFluidState(pos);
            if (!ifluidstate.isEmpty()) {
                worldIn.getFluidTickScheduler().isTicking(pos, ifluidstate.getFluid());
                if (state.contains(Properties.WATERLOGGED)) state = state.with(Properties.WATERLOGGED, true);
            }
            worldIn.setBlockState(pos, state, 2);
            if (BLOCKS_NEEDING_POSTPROCESSING.contains(state.getBlock())) {
                worldIn.getChunk(pos).markBlockForPostProcessing(pos);
            }
        }

        public BlockPos getGroundPos(WorldAccess worldIn, BlockPos startPos) {
            while (!Block.hasTopRim(worldIn, startPos) && startPos.getY() > worldIn.getBottomY()) {
                startPos = startPos.down();
            }
            return startPos;
        }

        public void fillAirLiquidDown(WorldAccess worldIn, BlockState state, BlockPos startPos) {
            int i = startPos.getX();
            int j = startPos.getY();
            int k = startPos.getZ();
            while (!Block.hasTopRim(worldIn, new BlockPos(i, j, k)) && j > 1) {
                BlockPos pos = new BlockPos(i, j, k);
                this.setBlockState(worldIn, pos, state);
                --j;
            }
        }

        public void fillAirLiquidDownTrunk(WorldAccess worldIn, BlockPos startPos, Random rand) {
            int i = startPos.getX();
            int j = startPos.getY();
            int k = startPos.getZ();
            while (!Block.hasTopRim(worldIn, new BlockPos(i, j, k)) && j > 1) {
                BlockPos pos = new BlockPos(i, j, k);
                this.setBlockState(worldIn, pos, rand.nextFloat() < 0.2 ? BlockHandler.CLAWED_LOG.getDefaultState() : Blocks.STRIPPED_JUNGLE_WOOD.getDefaultState());
                --j;
            }
        }

        public void fillAirLiquidDownBase(WorldAccess worldIn, BlockPos startPos, Random rand) {
            int i = startPos.getX();
            int j = startPos.getY();
            int k = startPos.getZ();
            while (!Block.hasTopRim(worldIn, new BlockPos(i, j, k)) && j > 1) {
                BlockPos pos = new BlockPos(i, j, k);
                this.setBlockState(worldIn, pos, rand.nextFloat() < 0.5 ? Blocks.STRIPPED_MANGROVE_LOG.getDefaultState() : Blocks.RED_TERRACOTTA.getDefaultState());
                --j;
            }
        }

        public void genStairs(WorldAccess worldIn, BlockPos pos, Random rand, Direction direction) {
            for (int i = 1; i < 5; i++) {
                if (!Block.hasTopRim(worldIn, pos)) {
                    BlockState state = rand.nextFloat() > 0.5 ? Blocks.ACACIA_SLAB.getDefaultState() : Blocks.SMOOTH_RED_SANDSTONE_SLAB.getDefaultState();
                    if (i % 2 == 1) state = state.with(SlabBlock.TYPE, SlabType.TOP);
                    this.setBlockState(worldIn, pos, state);

                    pos = pos.offset(direction);
                    if (i % 2 == 0) pos = pos.offset(Direction.DOWN);
                } else {
                    return;
                }
            }
            pos = pos.offset(direction.getOpposite());
            this.fillAirLiquidDown(worldIn, Blocks.STRIPPED_MANGROVE_LOG.getDefaultState(), pos);
            this.fillAirLiquidDown(worldIn, Blocks.LADDER.getDefaultState().with(LadderBlock.FACING, direction), pos.offset(direction));
        }

        public void genSpike(WorldAccess worldIn, BlockPos startPos, Random rand, int numLogs, int numFence, int numBars, int numSkulls) {
            int groundPos = worldIn.getTopY(Heightmap.Type.OCEAN_FLOOR_WG, startPos.getX(), startPos.getZ());
            BlockPos.Mutable pos = new BlockPos.Mutable(startPos.getX(), groundPos - 1, startPos.getZ());
            for (int i = 0; i < numLogs; i++) {
                this.setBlockState(worldIn, pos, Blocks.STRIPPED_MANGROVE_LOG.getDefaultState());
                pos.move(Direction.UP);
            }
            for (int i = 0; i < numFence; i++) {
                this.setBlockState(worldIn, pos, Blocks.MANGROVE_FENCE.getDefaultState());
                pos.move(Direction.UP);
            }
            for (int i = 0; i < numBars; i++) {
                this.setBlockState(worldIn, pos, Blocks.IRON_BARS.getDefaultState());
                pos.move(Direction.UP);
            }
            if (rand.nextFloat() < 0.1 && numSkulls > 0) {
                this.setBlockState(worldIn, pos, Blocks.SKELETON_SKULL.getDefaultState().with(Properties.ROTATION, rand.nextInt(16)));
            }
        }
    }

    public static class FirepitPiece extends Piece {
        public FirepitPiece(StructureTemplateManager manager, BlockRotation rotation, BlockPos pos) {
            super(StructureTypeHandler.UMVUTHANA_FIREPIT, manager, FIREPIT, rotation, pos);
        }

        public FirepitPiece(StructureContext context, NbtCompound tagCompound) {
            super(StructureTypeHandler.UMVUTHANA_FIREPIT, context, tagCompound);
        }

        public BlockPos findGround(WorldAccess worldIn, int x, int z) {
            int i = this.applyXTransform(x, z);
            int k = this.applyZTransform(x, z);
            int j = worldIn.getTopY(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, i, k);
            return new BlockPos(i, j, k);
        }

        @Override
        public void generate(StructureWorldAccess worldIn, StructureAccessor structureManager, ChunkGenerator chunkGenerator, Random randomIn, BlockBox p_230383_5_, ChunkPos p_230383_6_, BlockPos p_230383_7_) {
            super.generate(worldIn, structureManager, chunkGenerator, randomIn, p_230383_5_, p_230383_6_, p_230383_7_);
            BlockPos centerPos = this.findGround(worldIn, 4, 4);

            // Spawn Umvuthana
            int numUmvuthana = randomIn.nextInt(5) + 5;
            for (int i = 1; i <= numUmvuthana; i++) {
                int distance;
                int angle;
                EntityUmvuthanaMinion umvuthana = new EntityUmvuthanaMinion(EntityHandler.UMVUTHANA_MINION, worldIn.toServerWorld());
                for (int j = 1; j <= 20; j++) {
                    distance = randomIn.nextInt(10) + 2;
                    angle = randomIn.nextInt(360);
                    int x = (int) (distance * Math.sin(Math.toRadians(angle))) + 4;
                    int z = (int) (distance * Math.cos(Math.toRadians(angle))) + 4;
                    BlockPos bPos = this.findGround(worldIn, x, z);
                    umvuthana.setPosition(bPos.getX(), bPos.getY(), bPos.getZ());
                    if (bPos.getY() > 0 && umvuthana.canSpawn(worldIn, SpawnReason.STRUCTURE) && worldIn.isSpaceEmpty(umvuthana.getBoundingBox())) {
                        umvuthana.initialize(worldIn, worldIn.getLocalDifficulty(umvuthana.getBlockPos()), SpawnReason.STRUCTURE, null, null);
                        umvuthana.setPositionTarget(centerPos, 25);
                        worldIn.spawnEntity(umvuthana);
                        break;
                    }
                }
            }
        }
    }
}

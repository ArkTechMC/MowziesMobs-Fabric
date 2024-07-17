package com.bobmowzie.mowziesmobs.server.world.feature.structure;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.google.common.collect.ImmutableMap;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.structure.*;
import net.minecraft.structure.processor.BlockIgnoreStructureProcessor;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.ServerWorldAccess;

import java.util.Map;

public class FrostmawPieces {

    private static final Identifier FROSTMAW = new Identifier(MowziesMobs.MODID, "frostmaw_spawn");

    private static final Map<Identifier, BlockPos> OFFSET = ImmutableMap.of(
            FROSTMAW, new BlockPos(0, 1, 0)
    );

    public static void addPieces(StructureTemplateManager manager, BlockPos pos, BlockRotation rot, StructurePiecesHolder pieces, Random rand) {
        BlockPos rotationOffset = new BlockPos(0, 0, 0).rotate(rot);
        BlockPos blockPos = rotationOffset.add(pos);
        pieces.addPiece(new FrostmawPiece(manager, FROSTMAW, blockPos, rot));
    }

    public static class FrostmawPiece extends SimpleStructurePiece {

        public FrostmawPiece(StructureTemplateManager templateManagerIn, Identifier resourceLocationIn, BlockPos pos, BlockRotation rotationIn) {
            super(StructureTypeHandler.FROSTMAW_PIECE, 0, templateManagerIn, resourceLocationIn, resourceLocationIn.toString(), makeSettings(rotationIn, resourceLocationIn), makePosition(resourceLocationIn, pos));
        }

        public FrostmawPiece(StructureContext context, NbtCompound tag) {
            super(StructureTypeHandler.FROSTMAW_PIECE, tag, context.structureTemplateManager(), (resourceLocation) -> makeSettings(BlockRotation.valueOf(tag.getString("Rot")), resourceLocation));
        }

        private static StructurePlacementData makeSettings(BlockRotation rotation, Identifier resourceLocation) {
            return (new StructurePlacementData()).setRotation(rotation).setMirror(BlockMirror.NONE).addProcessor(BlockIgnoreStructureProcessor.IGNORE_STRUCTURE_BLOCKS);
        }

        private static BlockPos makePosition(Identifier resourceLocation, BlockPos pos) {
            return pos.add(FrostmawPieces.OFFSET.get(resourceLocation));
        }

        /**
         * (abstract) Helper method to read subclass data from NBT
         */
        @Override
        protected void writeNbt(StructureContext context, NbtCompound tagCompound) {
            super.writeNbt(context, tagCompound);
            tagCompound.putString("Rot", this.placementData.getRotation().name());
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

        }
    }
}

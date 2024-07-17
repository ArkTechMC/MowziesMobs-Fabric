package com.bobmowzie.mowziesmobs.server.world.feature.structure;

import com.bobmowzie.mowziesmobs.MowziesMobs;
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

public class WroughtnautChamberPieces {

    private static final Identifier PART = new Identifier(MowziesMobs.MODID, "wroughtnaut_chamber");

    public static void start(StructureTemplateManager manager, BlockPos pos, BlockRotation rot, StructurePiecesHolder pieces) {
        pieces.addPiece(new Piece(manager, PART, pos, rot));
    }

    public static class Piece extends SimpleStructurePiece {
        public Piece(StructureTemplateManager templateManagerIn, Identifier resourceLocationIn, BlockPos pos, BlockRotation rotationIn) {
            super(StructureTypeHandler.WROUGHTNAUT_CHAMBER_PIECE, 0, templateManagerIn, resourceLocationIn, resourceLocationIn.toString(), makeSettings(rotationIn, resourceLocationIn), pos);
        }

        public Piece(StructureContext context, NbtCompound tagCompound) {
            super(StructureTypeHandler.WROUGHTNAUT_CHAMBER_PIECE, tagCompound, context.structureTemplateManager(), (resourceLocation) -> makeSettings(BlockRotation.valueOf(tagCompound.contains("Rot") ? tagCompound.getString("Rot") : BlockRotation.NONE.name()), resourceLocation));
        }

        private static StructurePlacementData makeSettings(BlockRotation rotation, Identifier resourceLocation) {
            return (new StructurePlacementData()).setRotation(rotation).setMirror(BlockMirror.NONE).addProcessor(BlockIgnoreStructureProcessor.IGNORE_STRUCTURE_BLOCKS);
        }

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

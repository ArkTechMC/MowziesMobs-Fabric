package com.bobmowzie.mowziesmobs.client.model.tools;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;

/**
 * Created by BobMowzie on 5/1/2017.
 */
public class BlockModelRenderer extends AdvancedModelRenderer {
    private BlockState blockState;

    public BlockModelRenderer(AdvancedModelBase model) {
        super(model);
        this.setBlockState(Blocks.DIRT.getDefaultState());
    }

    public BlockState getBlockState() {
        return this.blockState;
    }

    public void setBlockState(BlockState blockState) {
        this.blockState = blockState;
    }
}

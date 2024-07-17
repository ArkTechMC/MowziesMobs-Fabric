package com.bobmowzie.mowziesmobs.server.block;

import net.minecraft.block.Block;

public interface ICopiedBlockProperties {
    Block getBaseBlock();

    void setBaseBlock(Block block);
}

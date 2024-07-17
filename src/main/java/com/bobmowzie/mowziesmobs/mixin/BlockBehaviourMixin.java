package com.bobmowzie.mowziesmobs.mixin;

import com.bobmowzie.mowziesmobs.server.block.ICopiedBlockProperties;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractBlock.Settings.class)
public class BlockBehaviourMixin implements ICopiedBlockProperties {
    public Block baseBlock;

    @Inject(method = "copy", at = @At("RETURN"))
    private static void onCopy(AbstractBlock blockBehaviour, CallbackInfoReturnable<AbstractBlock.Settings> cir) {
        if (blockBehaviour instanceof Block) {
            ICopiedBlockProperties copiedBlockProperties = (ICopiedBlockProperties) cir.getReturnValue();
            copiedBlockProperties.setBaseBlock((Block) blockBehaviour);
        }
    }

    @Override
    public Block getBaseBlock() {
        return this.baseBlock;
    }

    @Override
    public void setBaseBlock(Block block) {
        this.baseBlock = block;
    }
}

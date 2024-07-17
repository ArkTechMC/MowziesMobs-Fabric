package com.bobmowzie.mowziesmobs.server.item;

import com.bobmowzie.mowziesmobs.server.block.BlockHandler;
import com.bobmowzie.mowziesmobs.server.block.RakedSandBlock;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraftforge.common.Tags;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nullable;
import java.util.List;

public class ItemSandRake extends Item {
    public ItemSandRake(Settings properties) {
        super(properties);
    }

    @Override
    public boolean canRepair(ItemStack thisStack, ItemStack ingredientStack) {
        return ingredientStack.isIn(ItemTags.PLANKS) || super.canRepair(thisStack, ingredientStack);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World level = context.getWorld();
        BlockPos blockpos = context.getBlockPos();
        BlockState blockstate = level.getBlockState(blockpos);
        if (context.getSide() == Direction.UP) {
            PlayerEntity player = context.getPlayer();
            if (player != null) {
                ItemPlacementContext blockPlaceContext = new ItemPlacementContext(player, context.getHand(), context.getStack(), new BlockHitResult(context.getHitPos(), context.getSide(), context.getBlockPos(), context.hitsInsideBlock()));
                RakedSandBlock origBlock = null;
                if (blockstate.isIn(Tags.Blocks.SAND_COLORLESS)) {
                    origBlock = (RakedSandBlock) BlockHandler.RAKED_SAND;
                } else if (blockstate.isIn(Tags.Blocks.SAND_RED)) {
                    origBlock = (RakedSandBlock) BlockHandler.RED_RAKED_SAND;
                }

                if (origBlock != null) {
                    BlockState blockState = origBlock.getPlacementState(blockPlaceContext);
                    if (blockState != null) {
                        level.playSound(player, blockpos, MMSounds.BLOCK_RAKE_SAND, SoundCategory.BLOCKS, 1.0F, 1.0F);
                        if (!level.isClient) {
                            level.setBlockState(blockpos, blockState, 11);
                            origBlock.onBlockAdded(blockState, level, blockpos, blockstate, false);
                            origBlock.updateState(blockState, level, blockpos, false);
                            context.getStack().damage(1, player, (p_43122_) -> p_43122_.sendToolBreakStatus(context.getHand()));
                        }
                    }
                    return ActionResult.success(level.isClient);
                } else {
                    return ActionResult.PASS;
                }
            }
        }
        return ActionResult.PASS;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World worldIn, List<Text> tooltip, TooltipContext flagIn) {
        super.appendTooltip(stack, worldIn, tooltip, flagIn);
        tooltip.add(Text.translatable(this.getTranslationKey() + ".text.0").setStyle(ItemHandler.TOOLTIP_STYLE));
    }
}

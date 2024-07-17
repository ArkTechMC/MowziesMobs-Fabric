package com.bobmowzie.mowziesmobs.server.item;

import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.foliaath.EntityBabyFoliaath;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemFoliaathSeed extends Item {
    public ItemFoliaathSeed(Settings properties) {
        super(properties);
    }

    public Entity spawnCreature(ServerWorldAccess world, MobEntity entity, double x, double y, double z) {
        if (entity != null) {
            entity.refreshPositionAndAngles(x + 0.5, y, z + 0.5, world.toServerWorld().random.nextFloat() * 360 - 180, 0);
            entity.headYaw = entity.getYaw();
            entity.bodyYaw = entity.getYaw();
            entity.initialize(world, world.getLocalDifficulty(entity.getBlockPos()), SpawnReason.MOB_SUMMONED, null, null);
            if (!entity.canSpawn(world, SpawnReason.MOB_SUMMONED)) {
                return null;
            }
            world.spawnEntity(entity);
        }
        return entity;
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        PlayerEntity player = context.getPlayer();
        if (player == null) return ActionResult.FAIL;
        Hand hand = context.getHand();
        Direction facing = context.getSide();
        ItemStack stack = player.getStackInHand(hand);
        BlockPos pos = context.getBlockPos();
        World world = context.getWorld();
        if (world.isClient) {
            return ActionResult.SUCCESS;
        } else if (!player.canPlaceOn(pos.offset(facing), facing, stack)) {
            return ActionResult.FAIL;
        }
        Entity entity = this.spawnCreature((ServerWorld) world, new EntityBabyFoliaath(EntityHandler.BABY_FOLIAATH, world), pos.getX(), pos.getY() + 1, pos.getZ());
        if (entity != null) {
            if (entity instanceof LivingEntity && stack.hasCustomName()) {
                entity.setCustomName(stack.getName());
            }
            if (!player.isCreative()) {
                stack.decrement(1);
            }
        }
        return ActionResult.SUCCESS;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World worldIn, List<Text> tooltip, TooltipContext flagIn) {
        super.appendTooltip(stack, worldIn, tooltip, flagIn);
        tooltip.add(Text.translatable(this.getTranslationKey() + ".text.0").setStyle(ItemHandler.TOOLTIP_STYLE));
        tooltip.add(Text.translatable(this.getTranslationKey() + ".text.1").setStyle(ItemHandler.TOOLTIP_STYLE));
        tooltip.add(Text.translatable(this.getTranslationKey() + ".text.2").setStyle(ItemHandler.TOOLTIP_STYLE));
        tooltip.add(Text.translatable(this.getTranslationKey() + ".text.3").setStyle(ItemHandler.TOOLTIP_STYLE));
    }
}

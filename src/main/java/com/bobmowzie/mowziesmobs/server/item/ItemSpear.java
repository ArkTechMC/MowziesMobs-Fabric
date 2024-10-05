package com.bobmowzie.mowziesmobs.server.item;

import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterials;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.text.Text;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ItemSpear extends MowzieToolItem {
    public ItemSpear(Settings properties) {
        super(-2 + ConfigHandler.COMMON.TOOLS_AND_ABILITIES.SPEAR.toolConfig.attackDamageValue, -4f + ConfigHandler.COMMON.TOOLS_AND_ABILITIES.SPEAR.toolConfig.attackSpeedValue, ToolMaterials.STONE, BlockTags.HOE_MINEABLE, properties);
    }

    public static LivingEntity raytraceEntities(World world, PlayerEntity player, double range) {
        SpearHitResult result = new SpearHitResult();
        Vec3d pos = new Vec3d(player.getX(), player.getY() + player.getStandingEyeHeight(), player.getZ());
        Vec3d segment = player.getRotationVector();
        segment = pos.add(segment.x * range, segment.y * range, segment.z * range);
        result.setBlockHit(world.raycast(new RaycastContext(pos, segment, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, player)));
        double collidePosX, collidePosY, collidePosZ;
        if (result.blockHit != null) {
            collidePosX = result.blockHit.getPos().x;
            collidePosY = result.blockHit.getPos().y;
            collidePosZ = result.blockHit.getPos().z;
        } else {
            Vec3d end = player.getRotationVector().multiply(range).add(pos);
            collidePosX = end.x;
            collidePosY = end.y;
            collidePosZ = end.z;
        }

        List<LivingEntity> entities = world.getNonSpectatingEntities(LivingEntity.class, new Box(Math.min(pos.x, collidePosX), Math.min(pos.y, collidePosY), Math.min(pos.z, collidePosZ), Math.max(pos.x, collidePosX), Math.max(pos.y, collidePosY), Math.max(pos.z, collidePosZ)).expand(1, 1, 1));
        LivingEntity closest = null;
        for (LivingEntity entity : entities) {
            if (entity == player) {
                continue;
            }
            float pad = entity.getTargetingMargin();
            Box aabb = entity.getBoundingBox().expand(pad, pad, pad);
            boolean hit = aabb.intersects(pos, segment);
            if (aabb.contains(pos) || hit) {
                result.addEntityHit(entity);
                if (closest == null || player.distanceTo(closest) > player.distanceTo(entity)) closest = entity;
            }
        }
        return closest;
    }

    //FIXME: No API
//    @Override
//    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
//        return enchantment.target == EnchantmentTarget.WEAPON || enchantment.target == EnchantmentTarget.BREAKABLE;
//    }

    public boolean canMine(BlockState p_43291_, World p_43292_, BlockPos p_43293_, PlayerEntity p_43294_) {
        return !p_43294_.isCreative();
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World worldIn, List<Text> tooltip, TooltipContext flagIn) {
        super.appendTooltip(stack, worldIn, tooltip, flagIn);
        tooltip.add(Text.translatable(this.getTranslationKey() + ".text.0").setStyle(ItemHandler.TOOLTIP_STYLE));
        tooltip.add(Text.translatable(this.getTranslationKey() + ".text.1").setStyle(ItemHandler.TOOLTIP_STYLE));
    }

    @Override
    public ConfigHandler.ToolConfig getConfig() {
        return ConfigHandler.COMMON.TOOLS_AND_ABILITIES.SPEAR.toolConfig;
    }

    public static class SpearHitResult {
        private final List<LivingEntity> entities = new ArrayList<>();
        private HitResult blockHit;

        public HitResult getBlockHit() {
            return this.blockHit;
        }

        public void setBlockHit(HitResult blockHit) {
            this.blockHit = blockHit;
        }

        public void addEntityHit(LivingEntity entity) {
            this.entities.add(entity);
        }
    }
}

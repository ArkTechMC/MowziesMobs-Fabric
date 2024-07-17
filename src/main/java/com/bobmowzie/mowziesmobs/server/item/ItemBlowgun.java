package com.bobmowzie.mowziesmobs.server.item;

import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import net.minecraft.world.item.*;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

public class ItemBlowgun extends BowItem {
    public static final Predicate<ItemStack> DARTS = (p_220002_0_) -> p_220002_0_.getItem() == ItemHandler.DART;

    public ItemBlowgun(Settings properties) {
        super(properties);
    }

    public static float getArrowVelocity(int charge) {
        float f = (float) charge / 5.0F;
        f = (f * f + f * 2.0F) / 3.0F;
        if (f > 1.0F) {
            f = 1.0F;
        }

        return f;
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft) {
        if (entityLiving instanceof PlayerEntity playerentity) {
            boolean flag = playerentity.getAbilities().creativeMode || EnchantmentHelper.getLevel(Enchantments.INFINITY, stack) > 0;
            ItemStack itemstack = playerentity.getProjectileType(stack);

            int i = this.getMaxUseTime(stack) - timeLeft;

            if (!itemstack.isEmpty() || flag) {
                if (itemstack.isEmpty()) {
                    itemstack = new ItemStack(Items.ARROW);
                }

                float f = getArrowVelocity(i);
                if (!((double) f < 0.1D)) {
                    boolean flag1 = playerentity.getAbilities().creativeMode || (itemstack.getItem() instanceof ItemDart && ((ItemDart) itemstack.getItem()).isInfinite(itemstack, stack, playerentity));
                    if (!worldIn.isClient) {
                        ArrowItem arrowitem = (ArrowItem) (itemstack.getItem() instanceof ItemDart ? itemstack.getItem() : ItemHandler.DART);
                        PersistentProjectileEntity abstractarrowentity = arrowitem.createArrow(worldIn, itemstack, playerentity);
                        abstractarrowentity = customArrow(abstractarrowentity);
                        abstractarrowentity.setVelocity(playerentity, playerentity.getPitch(), playerentity.getYaw(), 0.0F, f * 1.1F /*ALTERED FROM PARENT*/, 1.0F);
                        if (f == 1.0F) {
                            abstractarrowentity.setCritical(true);
                        }

                        int j = EnchantmentHelper.getLevel(Enchantments.POWER, stack);
                        if (j > 0) {
                            abstractarrowentity.setDamage(abstractarrowentity.getDamage() + (double) j * 0.5D + 0.5D);
                        }

                        int k = EnchantmentHelper.getLevel(Enchantments.PUNCH, stack);
                        if (k > 0) {
                            abstractarrowentity.setPunch(k);
                        }

                        if (EnchantmentHelper.getLevel(Enchantments.FLAME, stack) > 0) {
                            abstractarrowentity.setOnFireFor(100);
                        }

                        stack.damage(1, playerentity, (player) -> {
                            player.sendToolBreakStatus(playerentity.getActiveHand());
                        });
                        if (flag1 || playerentity.getAbilities().creativeMode && (itemstack.getItem() == Items.SPECTRAL_ARROW || itemstack.getItem() == Items.TIPPED_ARROW)) {
                            abstractarrowentity.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
                        }

                        worldIn.spawnEntity(abstractarrowentity);
                    }

                    worldIn.playSound((PlayerEntity) null, playerentity.getX(), playerentity.getY(), playerentity.getZ(), MMSounds.ENTITY_UMVUTHANA_BLOWDART, SoundCategory.PLAYERS, 1.0F, 1.0F / (playerentity.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F); //CHANGED FROM PARENT CLASS
                    if (!flag1 && !playerentity.getAbilities().creativeMode) {
                        itemstack.decrement(1);
                        if (itemstack.isEmpty()) {
                            playerentity.getInventory().removeOne(itemstack);
                        }
                    }

                    playerentity.incrementStat(Stats.USED.getOrCreateStat(this));
                }
            }
        }
    }

    @Override
    public int getMaxUseTime(ItemStack p_77626_1_) {
        return 72000;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World worldIn, List<Text> tooltip, TooltipContext flagIn) {
        super.appendTooltip(stack, worldIn, tooltip, flagIn);
        tooltip.add(Text.translatable(this.getTranslationKey() + ".text.0").setStyle(ItemHandler.TOOLTIP_STYLE));
        tooltip.add(Text.translatable(this.getTranslationKey() + ".text.1").setStyle(ItemHandler.TOOLTIP_STYLE));
        tooltip.add(Text.translatable(this.getTranslationKey() + ".text.2").setStyle(ItemHandler.TOOLTIP_STYLE));
    }

    /**
     * Get the predicate to match ammunition when searching the player's inventory, not their main/offhand
     */
    public Predicate<ItemStack> getProjectiles() {
        return DARTS;
    }

    @Override
    public Predicate<ItemStack> getHeldProjectiles() {
        return DARTS;
    }
}

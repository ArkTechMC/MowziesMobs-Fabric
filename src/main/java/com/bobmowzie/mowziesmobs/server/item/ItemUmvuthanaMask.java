package com.bobmowzie.mowziesmobs.server.item;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.render.item.RenderUmvuthanaMaskArmor;
import com.bobmowzie.mowziesmobs.client.render.item.RenderUmvuthanaMaskItem;
import com.bobmowzie.mowziesmobs.server.capability.PlayerCapability;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.umvuthana.EntityUmvuthana;
import com.bobmowzie.mowziesmobs.server.entity.umvuthana.EntityUmvuthanaCraneToPlayer;
import com.bobmowzie.mowziesmobs.server.entity.umvuthana.EntityUmvuthanaFollowerToPlayer;
import com.bobmowzie.mowziesmobs.server.entity.umvuthana.MaskType;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import com.iafenvoy.uranus.client.render.armor.IArmorTextureProvider;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ArmorMaterials;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraft.world.item.*;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;
import java.util.function.Consumer;

public class ItemUmvuthanaMask extends MowzieArmorItem implements UmvuthanaMask, GeoItem, IArmorTextureProvider {
    private static final UmvuthanaMaskMaterial UMVUTHANA_MASK_MATERIAL = new UmvuthanaMaskMaterial();
    private static final RawAnimation UMVUTHANA_ANIM = RawAnimation.begin().thenLoop("umvuthana");
    private static final RawAnimation PLAYER_ANIM = RawAnimation.begin().thenLoop("player");
    private final MaskType type;
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public String controllerName = "controller";

    public ItemUmvuthanaMask(MaskType type, Settings properties) {
        super(UMVUTHANA_MASK_MATERIAL, Type.HELMET, properties);
        this.type = type;
    }

    public StatusEffect getPotion() {
        return this.type.potion;
    }

    @Override
    public boolean canRepair(ItemStack itemStack, ItemStack materialItemStack) {
        return false;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);
        ItemStack headStack = player.getInventory().armor.get(3);
        if (headStack.getItem() instanceof ItemSolVisage) {
            if (ConfigHandler.COMMON.TOOLS_AND_ABILITIES.SOL_VISAGE.breakable && !player.isCreative())
                headStack.damage(2, player, p -> p.sendToolBreakStatus(hand));
            boolean didSpawn = this.spawnUmvuthana(this.type, stack, player, (float) stack.getDamage() / (float) stack.getMaxDamage());
            if (didSpawn) {
                if (!player.isCreative()) stack.decrement(1);
                return new TypedActionResult<>(ActionResult.SUCCESS, stack);
            }
        }
        return super.use(world, player, hand);
    }

    private boolean spawnUmvuthana(MaskType mask, ItemStack stack, PlayerEntity player, float durability) {
        PlayerCapability.IPlayerCapability playerCapability = PlayerCapability.get(player);
        if (playerCapability != null && playerCapability.getPackSize() < ConfigHandler.COMMON.TOOLS_AND_ABILITIES.SOL_VISAGE.maxFollowers) {
            player.playSound(MMSounds.ENTITY_UMVUTHI_BELLY, 1.5f, 1);
            player.playSound(MMSounds.ENTITY_UMVUTHANA_BLOWDART, 1.5f, 0.5f);
            double angle = player.getHeadYaw();
            if (angle < 0) {
                angle = angle + 360;
            }
            EntityUmvuthanaFollowerToPlayer umvuthana;
            if (mask == MaskType.FAITH)
                umvuthana = new EntityUmvuthanaCraneToPlayer(EntityHandler.UMVUTHANA_CRANE_TO_PLAYER, player.getWorld(), player);
            else
                umvuthana = new EntityUmvuthanaFollowerToPlayer(EntityHandler.UMVUTHANA_FOLLOWER_TO_PLAYER, player.getWorld(), player);
//            property.addPackMember(umvuthana);
            if (!player.getWorld().isClient) {
                if (mask != MaskType.FAITH) {
                    int weapon;
                    if (mask != MaskType.FURY) weapon = umvuthana.randomizeWeapon();
                    else weapon = 0;
                    umvuthana.setWeapon(weapon);
                }
                umvuthana.updatePositionAndAngles(player.getX() + 1 * Math.sin(-angle * (Math.PI / 180)), player.getY() + 1.5, player.getZ() + 1 * Math.cos(-angle * (Math.PI / 180)), (float) angle, 0);
                umvuthana.setActive(false);
                umvuthana.active = false;
                player.getWorld().spawnEntity(umvuthana);
                double vx = 0.5 * Math.sin(-angle * Math.PI / 180);
                double vy = 0.5;
                double vz = 0.5 * Math.cos(-angle * Math.PI / 180);
                umvuthana.setVelocity(vx, vy, vz);
                umvuthana.setHealth((1.0f - durability) * umvuthana.getMaxHealth());
                umvuthana.setMask(mask);
                umvuthana.setStoredMask(stack.copy());
                if (stack.hasCustomName())
                    umvuthana.setCustomName(stack.getName());
            }
            return true;
        }
        return false;
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        super.initializeClient(consumer);
        consumer.accept(new IClientItemExtensions() {
            private final BuiltinModelItemRenderer itemRenderer = new RenderUmvuthanaMaskItem();
            private GeoArmorRenderer<?> armorRenderer;

            @Override
            public BipedEntityModel<?> getHumanoidArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlot equipmentSlot, BipedEntityModel<?> original) {
                if (this.armorRenderer == null)
                    this.armorRenderer = new RenderUmvuthanaMaskArmor();
                if (equipmentSlot == EquipmentSlot.HEAD)
                    this.armorRenderer.prepForRender(entityLiving, itemStack, equipmentSlot, original);
                return this.armorRenderer;
            }

            @Override
            public BuiltinModelItemRenderer getCustomRenderer() {
                return this.itemRenderer;
            }
        });
    }

    public MaskType getMaskType() {
        return this.type;
    }

    @Override
    public Identifier getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        return new Identifier(MowziesMobs.MODID, "textures/item/umvuthana_mask_" + this.type.name + ".png");
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World worldIn, List<Text> tooltip, TooltipContext flagIn) {
        super.appendTooltip(stack, worldIn, tooltip, flagIn);
        tooltip.add(Text.translatable(this.getTranslationKey() + ".text.0").setStyle(ItemHandler.TOOLTIP_STYLE));
        tooltip.add(Text.translatable(this.getTranslationKey() + ".text.1").setStyle(ItemHandler.TOOLTIP_STYLE));
    }

    @Override
    public ConfigHandler.ArmorConfig getConfig() {
        return ConfigHandler.COMMON.TOOLS_AND_ABILITIES.UMVUTHANA_MASK.armorConfig;
    }

    public <P extends Item & GeoItem> PlayState predicate(AnimationState<P> event) {
        Entity entity = event.getData(DataTickets.ENTITY);
        if (entity instanceof EntityUmvuthana) {
            event.getController().setAnimation(UMVUTHANA_ANIM);
        } else {
            event.getController().setAnimation(PLAYER_ANIM);
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, this.controllerName, 0, this::predicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    private static class UmvuthanaMaskMaterial implements ArmorMaterial {

        @Override
        public int getDurability(Type equipmentSlotType) {
            return ArmorMaterials.LEATHER.getDurability(equipmentSlotType);
        }

        @Override
        public int getProtection(Type equipmentSlotType) {
            return ConfigHandler.COMMON.TOOLS_AND_ABILITIES.UMVUTHANA_MASK.armorConfig.damageReductionValue;
        }

        @Override
        public int getEnchantability() {
            return ArmorMaterials.LEATHER.getEnchantability();
        }

        @Override
        public SoundEvent getEquipSound() {
            return ArmorMaterials.LEATHER.getEquipSound();
        }

        @Override
        public Ingredient getRepairIngredient() {
            return null;
        }

        @Override
        public String getName() {
            return "umvuthana_mask";
        }

        @Override
        public float getToughness() {
            return ConfigHandler.COMMON.TOOLS_AND_ABILITIES.UMVUTHANA_MASK.armorConfig.toughnessValue;
        }

        @Override
        public float getKnockbackResistance() {
            return ArmorMaterials.LEATHER.getKnockbackResistance();
        }
    }
}

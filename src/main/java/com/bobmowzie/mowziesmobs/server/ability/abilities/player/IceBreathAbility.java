package com.bobmowzie.mowziesmobs.server.ability.abilities.player;

import com.bobmowzie.mowziesmobs.server.ability.AbilitySection;
import com.bobmowzie.mowziesmobs.server.ability.AbilityType;
import com.bobmowzie.mowziesmobs.server.ability.PlayerAbility;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntityIceBreath;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import software.bernie.geckolib.core.animation.RawAnimation;

public class IceBreathAbility extends PlayerAbility {
    private static final RawAnimation ICE_BREATH_START_ANIM = RawAnimation.begin().thenPlay("ice_breath_start");
    private static final RawAnimation ICE_BREATH_LOOP_ANIM = RawAnimation.begin().thenLoop("ice_breath_loop");
    private static final RawAnimation ICE_BREATH_END_ANIM = RawAnimation.begin().thenPlay("ice_breath_end");
    protected EntityIceBreath iceBreath;
    public IceBreathAbility(AbilityType<PlayerEntity, IceBreathAbility> abilityType, PlayerEntity user) {
        super(abilityType, user, new AbilitySection[]{
                new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.STARTUP, 6),
                new AbilitySection.AbilitySectionInfinite(AbilitySection.AbilitySectionType.ACTIVE),
                new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.RECOVERY, 6)
        });
    }

    @Override
    public void start() {
        super.start();
        LivingEntity user = this.getUser();
        if (!this.getUser().getWorld().isClient()) {
            EntityIceBreath iceBreath = new EntityIceBreath(EntityHandler.ICE_BREATH, user.getWorld(), user);
            iceBreath.updatePositionAndAngles(user.getX(), user.getY() + user.getStandingEyeHeight() - 0.5f, user.getZ(), user.getYaw(), user.getPitch());
            user.getWorld().spawnEntity(iceBreath);
            this.iceBreath = iceBreath;
        }
        this.playAnimation(ICE_BREATH_START_ANIM);

        if (this.getUser().getActiveHand() == Hand.MAIN_HAND) {
            this.heldItemMainHandVisualOverride = this.getUser().getMainHandStack();
            this.heldItemOffHandVisualOverride = ItemStack.EMPTY;
            this.firstPersonOffHandDisplay = HandDisplay.DONT_RENDER;
        } else {
            this.heldItemOffHandVisualOverride = this.getUser().getOffHandStack();
            this.heldItemMainHandVisualOverride = ItemStack.EMPTY;
            this.firstPersonMainHandDisplay = HandDisplay.DONT_RENDER;
        }
    }

    @Override
    public void tickUsing() {
        super.tickUsing();
        if (this.getCurrentSection().sectionType != AbilitySection.AbilitySectionType.RECOVERY && !this.checkIceCrystal()) {
            this.jumpToSection(2);
        }
    }

    @Override
    public void end() {
        super.end();
        if (this.iceBreath != null) this.iceBreath.discard();
    }

    private boolean checkIceCrystal() {
        ItemStack stack = this.getUser().getActiveItem();
        if (this.getTicksInUse() <= 1) return true;
        if (stack.getItem() != ItemHandler.ICE_CRYSTAL) return false;
        return stack.getDamage() + 5 < stack.getMaxDamage() || ConfigHandler.COMMON.TOOLS_AND_ABILITIES.ICE_CRYSTAL.breakable;
    }

    @Override
    public boolean canUse() {
        return this.checkIceCrystal() && super.canUse();
    }

    @Override
    protected void beginSection(AbilitySection section) {
        super.beginSection(section);
        if (section.sectionType == AbilitySection.AbilitySectionType.STARTUP) {
            this.playAnimation(ICE_BREATH_START_ANIM);
        } else if (section.sectionType == AbilitySection.AbilitySectionType.ACTIVE) {
            this.playAnimation(ICE_BREATH_LOOP_ANIM);
        } else if (section.sectionType == AbilitySection.AbilitySectionType.RECOVERY) {
            this.playAnimation(ICE_BREATH_END_ANIM);
        }
    }

    @Override
    public boolean preventsItemUse(ItemStack stack) {
        return stack.getItem() != ItemHandler.ICE_CRYSTAL;
    }
}

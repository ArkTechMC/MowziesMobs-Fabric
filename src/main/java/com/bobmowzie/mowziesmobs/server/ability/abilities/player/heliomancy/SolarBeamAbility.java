package com.bobmowzie.mowziesmobs.server.ability.abilities.player.heliomancy;

import com.bobmowzie.mowziesmobs.server.ability.AbilitySection;
import com.bobmowzie.mowziesmobs.server.ability.AbilityType;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntitySolarBeam;
import com.bobmowzie.mowziesmobs.server.potion.EffectHandler;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import software.bernie.geckolib.core.animation.RawAnimation;

public class SolarBeamAbility extends HeliomancyAbilityBase {
    private static final RawAnimation SOLAR_BEAM_CHARGE_ANIM = RawAnimation.begin().thenPlay("solar_beam_charge");
    protected EntitySolarBeam solarBeam;

    public SolarBeamAbility(AbilityType<PlayerEntity, SolarBeamAbility> abilityType, PlayerEntity user) {
        super(abilityType, user, new AbilitySection[]{
                new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.STARTUP, 20),
                new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.ACTIVE, 55),
                new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.RECOVERY, 20)
        });
    }

    @Override
    public void start() {
        super.start();
        LivingEntity user = this.getUser();
        EntitySolarBeam solarBeam = new EntitySolarBeam(EntityHandler.SOLAR_BEAM, user.getWorld(), user, user.getX(), user.getY() + 1.2f, user.getZ(), (float) ((user.headYaw + 90) * Math.PI / 180), (float) (-user.getPitch() * Math.PI / 180), 55);
        this.solarBeam = solarBeam;
        if (!this.getUser().getWorld().isClient()) {
            solarBeam.setHasPlayer(true);
            user.getWorld().spawnEntity(solarBeam);
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 80, 2, false, false));
        } else {
            this.heldItemMainHandVisualOverride = ItemStack.EMPTY;
            this.heldItemOffHandVisualOverride = ItemStack.EMPTY;
            this.firstPersonOffHandDisplay = HandDisplay.FORCE_RENDER;
            this.firstPersonMainHandDisplay = HandDisplay.FORCE_RENDER;
        }
        this.playAnimation(SOLAR_BEAM_CHARGE_ANIM);
    }

    @Override
    protected void beginSection(AbilitySection section) {
        super.beginSection(section);
        if (section.sectionType == AbilitySection.AbilitySectionType.ACTIVE) {
            if (!this.getLevel().isClient()) {
                StatusEffectInstance sunsBlessingInstance = this.getUser().getStatusEffect(EffectHandler.SUNS_BLESSING);
                if (sunsBlessingInstance != null) {
                    int duration = sunsBlessingInstance.getDuration();
                    this.getUser().removeStatusEffect(EffectHandler.SUNS_BLESSING);
                    int solarBeamCost = ConfigHandler.COMMON.TOOLS_AND_ABILITIES.SUNS_BLESSING.solarBeamCost * 60 * 20;
                    if (duration - solarBeamCost > 0) {
                        this.getUser().addStatusEffect(new StatusEffectInstance(EffectHandler.SUNS_BLESSING, duration - solarBeamCost, 0, false, false));
                    }
                }
            }
        }
    }

    @Override
    public void end() {
        super.end();
        if (this.solarBeam != null) {
            this.solarBeam.discard();
        }
    }
}

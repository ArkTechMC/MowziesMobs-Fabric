package com.bobmowzie.mowziesmobs.server.ability.abilities.player.heliomancy;

import com.bobmowzie.mowziesmobs.client.particle.ParticleOrb;
import com.bobmowzie.mowziesmobs.server.ability.AbilityHandler;
import com.bobmowzie.mowziesmobs.server.ability.AbilitySection;
import com.bobmowzie.mowziesmobs.server.ability.AbilityType;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.umvuthana.EntityUmvuthi;
import com.bobmowzie.mowziesmobs.server.potion.EffectHandler;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import io.github.fabricators_of_create.porting_lib.entity.events.PlayerInteractionEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import software.bernie.geckolib.core.animation.RawAnimation;

import java.util.List;

public class SolarFlareAbility extends HeliomancyAbilityBase {

    private static final RawAnimation SOLAR_FLARE_ANIM = RawAnimation.begin().thenPlay("solar_flare");

    public SolarFlareAbility(AbilityType<PlayerEntity, SolarFlareAbility> abilityType, PlayerEntity user) {
        super(abilityType, user, EntityUmvuthi.SolarFlareAbility.SECTION_TRACK);
    }

    @Override
    public void start() {
        super.start();
        this.getUser().playSound(MMSounds.ENTITY_UMVUTHI_BURST, 1.7f, 1.5f);
        this.playAnimation(SOLAR_FLARE_ANIM);
        if (this.getLevel().isClient) {
            this.heldItemMainHandVisualOverride = ItemStack.EMPTY;
            this.heldItemOffHandVisualOverride = ItemStack.EMPTY;
            this.firstPersonOffHandDisplay = HandDisplay.FORCE_RENDER;
            this.firstPersonMainHandDisplay = HandDisplay.FORCE_RENDER;
        }
    }

    @Override
    public boolean canUse() {
        if (this.getUser() == null || !this.getUser().getInventory().getMainHandStack().isEmpty()) return false;
        return this.getUser().hasStatusEffect(EffectHandler.SUNS_BLESSING) && super.canUse();
    }

    @Override
    public void tickUsing() {
        super.tickUsing();
        if (this.getTicksInUse() < 16) {
            this.getUser().addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 2, 2, false, false));
        }

        if (this.getTicksInUse() <= 6 && this.getLevel().isClient) {
            int particleCount = 8;
            while (--particleCount != 0) {
                double radius = 2f;
                double yaw = this.rand.nextFloat() * 2 * Math.PI;
                double pitch = this.rand.nextFloat() * 2 * Math.PI;
                double ox = radius * Math.sin(yaw) * Math.sin(pitch);
                double oy = radius * Math.cos(pitch);
                double oz = radius * Math.cos(yaw) * Math.sin(pitch);
                this.getLevel().addParticle(new ParticleOrb.OrbData((float) this.getUser().getX(), (float) this.getUser().getY() + this.getUser().getHeight() / 2f, (float) this.getUser().getZ(), 6), this.getUser().getX() + ox, this.getUser().getY() + this.getUser().getHeight() / 2f + oy, this.getUser().getZ() + oz, 0, 0, 0);
            }
        }

        if (this.getTicksInUse() == 10) {
            if (this.getLevel().isClient) {
                for (int i = 0; i < 30; i++) {
                    final float velocity = 0.25F;
                    float yaw = (float) (i * (Math.PI * 2 / 30));
                    float vy = this.rand.nextFloat() * 0.1F - 0.05f;
                    float vx = velocity * MathHelper.cos(yaw);
                    float vz = velocity * MathHelper.sin(yaw);
                    this.getLevel().addParticle(ParticleTypes.FLAME, this.getUser().getX(), this.getUser().getY() + 1, this.getUser().getZ(), vx, vy, vz);
                }
            }
        }
    }

    @Override
    protected void beginSection(AbilitySection section) {
        super.beginSection(section);
        if (section.sectionType == AbilitySection.AbilitySectionType.ACTIVE) {
            PlayerEntity user = this.getUser();
            float radius = 3.2f;
            List<LivingEntity> hit = this.getEntityLivingBaseNearby(user, radius, radius, radius, radius);
            for (LivingEntity aHit : hit) {
                if (aHit == this.getUser()) {
                    continue;
                }
                float damage = 2.0f;
                float knockback = 3.0f;
                damage *= (float) ConfigHandler.COMMON.TOOLS_AND_ABILITIES.SUNS_BLESSING.sunsBlessingAttackMultiplier;
                if (aHit.damage(user.getDamageSources().playerAttack(user), damage)) {
                    Vec3d vec3 = aHit.getPos().subtract(user.getPos()).normalize().multiply((double) knockback * 0.6D);
                    if (vec3.lengthSquared() > 0.0D)
                        aHit.addVelocity(vec3.x, 0.1D, vec3.z);
                }
            }
        }
    }

    @Override
    public void onLeftClickEmpty(PlayerInteractionEvents.LeftClickEmpty event) {
        super.onLeftClickEmpty(event);
        if (event.getEntity() == this.getUser() && event.getEntity().isSneaking())
            AbilityHandler.INSTANCE.sendPlayerTryAbilityMessage(event.getEntity(), AbilityHandler.SOLAR_FLARE_ABILITY);
    }

    @Override
    public void onLeftClickEntity(PlayerEntity player, World world, Hand hand, Entity entity, EntityHitResult hitResult) {
        super.onLeftClickEntity(player, world, hand, entity, hitResult);
        if (this.getUser() == player && player.isSneaking())
            AbilityHandler.INSTANCE.sendPlayerTryAbilityMessage(player, AbilityHandler.SOLAR_FLARE_ABILITY);
    }
}

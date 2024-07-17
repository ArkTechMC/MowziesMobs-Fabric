package com.bobmowzie.mowziesmobs.server.ability.abilities.player.heliomancy;

import com.bobmowzie.mowziesmobs.client.render.entity.player.GeckoFirstPersonRenderer;
import com.bobmowzie.mowziesmobs.client.render.entity.player.GeckoPlayer;
import com.bobmowzie.mowziesmobs.client.render.entity.player.GeckoRenderPlayer;
import com.bobmowzie.mowziesmobs.server.ability.Ability;
import com.bobmowzie.mowziesmobs.server.ability.AbilityHandler;
import com.bobmowzie.mowziesmobs.server.ability.AbilitySection;
import com.bobmowzie.mowziesmobs.server.ability.AbilityType;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntitySuperNova;
import com.bobmowzie.mowziesmobs.server.entity.umvuthana.EntityUmvuthi;
import com.bobmowzie.mowziesmobs.server.potion.EffectHandler;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.Perspective;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import software.bernie.geckolib.core.animation.RawAnimation;

import java.util.List;

public class SupernovaAbility extends HeliomancyAbilityBase {
    private static final int BUFFER = 5;
    private static final RawAnimation SUPERNOVA_ANIM = RawAnimation.begin().thenPlay("supernova");
    private boolean leftClickDown;
    private boolean rightClickDown;
    private int timeSinceLeftUp;
    private int timeSinceRightUp;
    private final Vec3d[] particleEmitter;

    public SupernovaAbility(AbilityType<PlayerEntity, SupernovaAbility> abilityType, PlayerEntity user) {
        super(abilityType, user, EntityUmvuthi.SupernovaAbility.SECTION_TRACK);
        this.particleEmitter = new Vec3d[1];
    }

    @Override
    public void start() {
        super.start();
        this.getUser().playSound(MMSounds.ENTITY_SUPERNOVA_START, 3f, 1f);
        this.playAnimation(SUPERNOVA_ANIM);

        if (this.getLevel().isClient) {
            this.heldItemMainHandVisualOverride = ItemStack.EMPTY;
            this.heldItemOffHandVisualOverride = ItemStack.EMPTY;
            this.firstPersonOffHandDisplay = HandDisplay.FORCE_RENDER;
            this.firstPersonMainHandDisplay = HandDisplay.FORCE_RENDER;
        }
    }

    @Override
    public void tickUsing() {
        super.tickUsing();

        if (this.getTicksInUse() < 84) {
            this.getUser().addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 2, 4, false, false));
        }

        if (this.getCurrentSection().sectionType == AbilitySection.AbilitySectionType.STARTUP) {
            this.getUser().addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 2, 1, false, false));
        }

        if (this.getTicksInUse() == 30) {
            this.getUser().playSound(MMSounds.ENTITY_SUPERNOVA_BLACKHOLE, 2f, 1.2f);
        }

        if (this.getTicksInUse() < 30) {
            List<LivingEntity> entities = this.getEntityLivingBaseNearby(this.getUser(), 16, 16, 16, 16);
            for (LivingEntity inRange : entities) {
                if (inRange instanceof PlayerEntity && ((PlayerEntity) inRange).getAbilities().invulnerable) continue;
                Vec3d diff = inRange.getPos().subtract(this.getUser().getPos().add(0, 3, 0));
                diff = diff.normalize().multiply(0.03);
                inRange.setVelocity(inRange.getVelocity().subtract(diff));

                if (inRange.getY() < this.getUser().getY() + 3)
                    inRange.setVelocity(inRange.getVelocity().add(0, 0.075, 0));
            }
        }

        // Particle effects
        if (this.getLevel().isClient) {
            // First person
            if (this.getUser() == MinecraftClient.getInstance().player && MinecraftClient.getInstance().options.getPerspective() == Perspective.FIRST_PERSON) {
                GeckoPlayer geckoPlayer = GeckoPlayer.getGeckoPlayer(this.getUser(), GeckoPlayer.Perspective.FIRST_PERSON);
                if (geckoPlayer != null) {
                    GeckoFirstPersonRenderer renderPlayer = (GeckoFirstPersonRenderer) geckoPlayer.getPlayerRenderer();
                    if (renderPlayer.particleEmitterRoot != null) {
                        this.particleEmitter[0] = renderPlayer.particleEmitterRoot;
                    }
                }
            }
            // Third person
            else {
                GeckoPlayer geckoPlayer = GeckoPlayer.getGeckoPlayer(this.getUser(), GeckoPlayer.Perspective.THIRD_PERSON);
                if (geckoPlayer != null) {
                    GeckoRenderPlayer renderPlayer = (GeckoRenderPlayer) geckoPlayer.getPlayerRenderer();
                    if (renderPlayer.particleEmitterRoot != null) {
                        this.particleEmitter[0] = this.getUser().getPos().add(renderPlayer.particleEmitterRoot).add(0, this.getUser().getHeight() / 2f + 0.3f, 0);
                    }
                }
            }
            // Do the effects with whichever emitter location
            EntityUmvuthi.SupernovaAbility.superNovaEffects(this, this.particleEmitter, this.getLevel());
        }
    }

    @Override
    protected void beginSection(AbilitySection section) {
        super.beginSection(section);
        if (section.sectionType == AbilitySection.AbilitySectionType.ACTIVE) {
            if (!this.getUser().getWorld().isClient) {
                EntitySuperNova superNova = new EntitySuperNova(EntityHandler.SUPER_NOVA, this.getUser().getWorld(), this.getUser(), this.getUser().getX(), this.getUser().getY() + this.getUser().getHeight() / 2f, this.getUser().getZ());
                this.getUser().getWorld().spawnEntity(superNova);

                StatusEffectInstance sunsBlessingInstance = this.getUser().getStatusEffect(EffectHandler.SUNS_BLESSING);
                if (sunsBlessingInstance != null) {
                    int duration = sunsBlessingInstance.getDuration();
                    this.getUser().removeStatusEffect(EffectHandler.SUNS_BLESSING);
                    int supernovaCost = ConfigHandler.COMMON.TOOLS_AND_ABILITIES.SUNS_BLESSING.supernovaCost * 60 * 20;
                    if (duration - supernovaCost > 0) {
                        this.getUser().addStatusEffect(new StatusEffectInstance(EffectHandler.SUNS_BLESSING, duration - supernovaCost, 0, false, false));
                    }
                }
            }
        }
    }

    @Override
    public void onLeftMouseDown(PlayerEntity player) {
        super.onLeftMouseDown(player);
        if (player == this.getUser()) this.leftClickDown = true;
    }

    @Override
    public void onLeftMouseUp(PlayerEntity player) {
        super.onLeftMouseUp(player);
        if (player == this.getUser()) {
            this.leftClickDown = false;
            this.timeSinceLeftUp = BUFFER;
        }
    }

    @Override
    public void onRightMouseDown(PlayerEntity player) {
        super.onRightMouseDown(player);
        if (player == this.getUser()) this.rightClickDown = true;
    }

    @Override
    public void onRightMouseUp(PlayerEntity player) {
        super.onRightMouseUp(player);
        if (player == this.getUser()) {
            this.rightClickDown = false;
            this.timeSinceRightUp = BUFFER;
        }
    }

    public boolean isRightClickDown() {
        return this.rightClickDown || this.timeSinceRightUp > 0;
    }

    public boolean isLeftClickDown() {
        return this.leftClickDown || this.timeSinceLeftUp > 0;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getUser().isSneaking() && this.isLeftClickDown() && this.isRightClickDown()) {
            AbilityHandler.INSTANCE.sendAbilityMessage(this.getUser(), AbilityHandler.SUPERNOVA_ABILITY);
        }
        if (this.timeSinceRightUp > 0) this.timeSinceRightUp--;
        if (this.timeSinceLeftUp > 0) this.timeSinceLeftUp--;
    }

    @Override
    public boolean canCancelActiveAbility() {
        Ability<?> ability = this.getActiveAbility();
        return ability != null && (ability.getAbilityType() == AbilityHandler.SOLAR_FLARE_ABILITY || ability.getAbilityType() == AbilityHandler.SOLAR_BEAM_ABILITY) && ability.getTicksInUse() < 5;
    }
}

package com.bobmowzie.mowziesmobs.server.ability;

import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieAnimationController;
import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieGeoModel;
import com.bobmowzie.mowziesmobs.client.render.entity.player.GeckoPlayer;
import com.bobmowzie.mowziesmobs.server.ability.AbilitySection.AbilitySectionDuration;
import com.bobmowzie.mowziesmobs.server.ability.AbilitySection.AbilitySectionInstant;
import com.bobmowzie.mowziesmobs.server.capability.AbilityCapability;
import com.bobmowzie.mowziesmobs.server.entity.MowzieGeckoEntity;
import com.bobmowzie.mowziesmobs.server.potion.EffectHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

import java.util.List;
import java.util.Random;

public class Ability<T extends LivingEntity> {
    private final AbilitySection[] sectionTrack;
    private final AbilityType<T, ? extends Ability> abilityType;
    private final T user;
    private final AbilityCapability.IAbilityCapability abilityCapability;
    protected int cooldownMax;
    protected Random rand;
    protected RawAnimation activeAnimation;
    private int ticksInUse;
    private int ticksInSection;
    private int currentSectionIndex;
    private boolean isUsing;
    private int cooldownTimer;

    public Ability(AbilityType<T, ? extends Ability> abilityType, T user, AbilitySection[] sectionTrack, int cooldownMax) {
        this.abilityType = abilityType;
        this.user = user;
        this.abilityCapability = AbilityCapability.get(user);
        this.sectionTrack = sectionTrack;
        this.cooldownMax = cooldownMax;
        this.rand = new Random();
    }

    public Ability(AbilityType<T, ? extends Ability> abilityType, T user, AbilitySection[] sectionTrack) {
        this(abilityType, user, sectionTrack, 0);
    }

    public void start() {
        if (!this.runsInBackground()) this.abilityCapability.setActiveAbility(this);
        this.ticksInUse = 0;
        this.ticksInSection = 0;
        this.currentSectionIndex = 0;
        this.isUsing = true;
        this.beginSection(this.getSectionTrack()[0]);
    }

    public void playAnimation(RawAnimation animation) {
        if (this.getUser() instanceof MowzieGeckoEntity entity && this.getUser().getWorld().isClient()) {
            this.activeAnimation = animation;
            MowzieAnimationController<MowzieGeckoEntity> controller = entity.getController();
            if (controller != null) {
                controller.playAnimation(entity, animation);
            }
        }
    }

    public void tick() {
        if (this.isUsing()) {
            if (this.getUser().canMoveVoluntarily() && !this.canContinueUsing())
                AbilityHandler.INSTANCE.sendInterruptAbilityMessage(this.getUser(), this.abilityType);

            this.tickUsing();

            this.ticksInUse++;
            this.ticksInSection++;
            AbilitySection section = this.getCurrentSection();
            if (section instanceof AbilitySectionInstant) {
                this.nextSection();
            } else if (section instanceof AbilitySectionDuration sectionDuration) {
                if (this.ticksInSection > sectionDuration.duration) this.nextSection();
            }
        } else {
            this.tickNotUsing();
            if (this.getCooldownTimer() > 0) this.cooldownTimer--;
        }
    }

    public void tickUsing() {

    }

    public void tickNotUsing() {

    }

    public void end() {
        this.ticksInUse = 0;
        this.ticksInSection = 0;
        this.isUsing = false;
        this.cooldownTimer = this.getMaxCooldown();
        this.currentSectionIndex = 0;
        if (!this.runsInBackground()) this.abilityCapability.setActiveAbility(null);
    }

    public void interrupt() {
        this.end();
    }

    public void complete() {
        this.end();
    }

    /**
     * Server-only check to see if the user can use this ability. Checked before packet is sent.
     *
     * @return Whether or not the ability can be used
     */
    public boolean canUse() {
        if (this.getUser().hasStatusEffect(EffectHandler.FROZEN)) return false;
        boolean toReturn = (!this.isUsing() || this.canCancelSelf()) && this.cooldownTimer == 0;
        if (!this.runsInBackground())
            toReturn = toReturn && (this.abilityCapability.getActiveAbility() == null || this.canCancelActiveAbility() || this.abilityCapability.getActiveAbility().canBeCanceledByAbility(this));
        return toReturn;
    }

    /**
     * Both sides check and behavior when user tries to use this ability. Ability only starts if this returns true.
     * Called after packet is received.
     *
     * @return Whether or not the ability try succeeded
     */
    public boolean tryAbility() {
        return true;
    }

    public boolean canCancelActiveAbility() {
        return false;
    }

    public Ability getActiveAbility() {
        AbilityCapability.IAbilityCapability capability = this.getAbilityCapability();
        if (capability == null) return null;
        return this.getAbilityCapability().getActiveAbility();
    }

    public boolean canCancelSelf() {
        return false;
    }

    public boolean canBeCanceledByAbility(Ability ability) {
        return false;
    }

    protected boolean canContinueUsing() {
        return !this.getUser().hasStatusEffect(EffectHandler.FROZEN);
    }

    public boolean isUsing() {
        return this.isUsing;
    }

    public T getUser() {
        return this.user;
    }

    public World getLevel() {
        return this.user.getWorld();
    }

    public int getTicksInUse() {
        return this.ticksInUse;
    }

    public int getTicksInSection() {
        return this.ticksInSection;
    }

    public int getCooldownTimer() {
        return this.cooldownTimer;
    }

    public void nextSection() {
        this.jumpToSection(this.currentSectionIndex + 1);
    }

    public void jumpToSection(int sectionIndex) {
        this.endSection(this.getCurrentSection());
        this.currentSectionIndex = sectionIndex;
        this.ticksInSection = 0;
        if (this.currentSectionIndex >= this.getSectionTrack().length) {
            this.complete();
        } else {
            this.beginSection(this.getCurrentSection());
        }
    }

    protected void endSection(AbilitySection section) {

    }

    protected void beginSection(AbilitySection section) {

    }

    public AbilitySection getCurrentSection() {
        if (this.currentSectionIndex >= this.getSectionTrack().length) return null;
        return this.getSectionTrack()[this.currentSectionIndex];
    }

    public boolean damageInterrupts() {
        return false;
    }

    public void onTakeDamage(LivingEntity entity, DamageSource source, float amount) {
        if (this.isUsing() && amount > 0.0 && this.damageInterrupts())
            AbilityHandler.INSTANCE.sendInterruptAbilityMessage(this.getUser(), this.getAbilityType());
    }

    /**
     * Non-background abilities require no other non-background abilities running to run.
     * Only one non-background ability can run at once.
     * Background abilities can all run simultaneously
     *
     * @return
     */
    public boolean runsInBackground() {
        return false;
    }

    /**
     * Unused for background abilities
     *
     * @return
     */
    public boolean preventsAttacking() {
        return true;
    }

    /**
     * Unused for background abilities
     *
     * @return
     */
    public boolean preventsBlockBreakingBuilding() {
        return true;
    }

    /**
     * Unused for background abilities
     *
     * @return
     */
    public boolean preventsInteracting() {
        return true;
    }

    /**
     * Unused for background abilities
     *
     * @return
     */
    public boolean preventsItemUse(ItemStack stack) {
        return true;
    }

    public AbilitySection[] getSectionTrack() {
        return this.sectionTrack;
    }

    public int getMaxCooldown() {
        return this.cooldownMax;
    }

    public AbilityCapability.IAbilityCapability getAbilityCapability() {
        return this.abilityCapability;
    }

    public <E extends GeoEntity> PlayState animationPredicate(AnimationState<E> e, GeckoPlayer.Perspective perspective) {
        if (this.activeAnimation == null || this.activeAnimation.getAnimationStages().isEmpty())
            return PlayState.STOP;
        e.getController().setAnimation(this.activeAnimation);
        return PlayState.CONTINUE;
    }

    public void codeAnimations(MowzieGeoModel<? extends GeoEntity> model, float partialTick) {

    }

    public boolean isAnimating() {
        return this.isUsing();
    }

    public AbilityType<T, ? extends Ability> getAbilityType() {
        return this.abilityType;
    }

    public List<LivingEntity> getEntityLivingBaseNearby(LivingEntity player, double distanceX, double distanceY, double distanceZ, double radius) {
        return this.getEntitiesNearby(player, LivingEntity.class, distanceX, distanceY, distanceZ, radius);
    }

    public <T extends Entity> List<T> getEntitiesNearby(LivingEntity player, Class<T> entityClass, double r) {
        return player.getWorld().getEntitiesByClass(entityClass, player.getBoundingBox().expand(r, r, r), e -> e != player && player.distanceTo(e) <= r);
    }

    public <T extends Entity> List<T> getEntitiesNearby(LivingEntity player, Class<T> entityClass, double dX, double dY, double dZ, double r) {
        return player.getWorld().getEntitiesByClass(entityClass, player.getBoundingBox().expand(dX, dY, dZ), e -> e != player && player.distanceTo(e) <= r);
    }

    public NbtCompound writeNBT() {
        NbtCompound compound = new NbtCompound();
        if (this.isUsing()) {
            compound.putInt("ticks_in_use", this.ticksInUse);
            compound.putInt("ticks_in_section", this.ticksInSection);
            compound.putInt("current_section", this.currentSectionIndex);
        } else if (this.cooldownTimer > 0) {
            compound.putInt("cooldown_timer", this.cooldownTimer);
        }
        return compound;
    }

    public void readNBT(NbtElement nbt) {
        NbtCompound compound = (NbtCompound) nbt;
        this.isUsing = compound.contains("ticks_in_use");
        if (this.isUsing) {
            this.ticksInUse = compound.getInt("ticks_in_use");
            this.ticksInSection = compound.getInt("ticks_in_section");
            this.currentSectionIndex = compound.getInt("current_section");
        } else {
            this.cooldownTimer = compound.getInt("cooldown_timer");
        }
    }

    // Client events
    public void onRenderTick() {

    }
}

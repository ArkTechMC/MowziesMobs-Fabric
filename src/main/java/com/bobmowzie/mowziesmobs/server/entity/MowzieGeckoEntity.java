package com.bobmowzie.mowziesmobs.server.entity;

import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieAnimationController;
import com.bobmowzie.mowziesmobs.server.ability.Ability;
import com.bobmowzie.mowziesmobs.server.ability.AbilityHandler;
import com.bobmowzie.mowziesmobs.server.ability.AbilityType;
import com.bobmowzie.mowziesmobs.server.ability.abilities.player.SimpleAnimationAbility;
import com.bobmowzie.mowziesmobs.server.capability.AbilityCapability;
import com.bobmowzie.mowziesmobs.server.capability.CapabilityHandler;
import com.bobmowzie.mowziesmobs.server.capability.FrozenCapability;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public abstract class MowzieGeckoEntity extends MowzieEntity implements GeoEntity {
    private static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("idle");
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);    protected MowzieAnimationController<MowzieGeckoEntity> controller = new MowzieAnimationController<>(this, "controller", 5, this::predicate, 0);

    public MowzieGeckoEntity(EntityType<? extends MowzieEntity> type, World world) {
        super(type, world);
    }

    @Override
    protected int getDeathDuration() {
        Ability deathAbility = this.getActiveAbility();
        if (deathAbility instanceof SimpleAnimationAbility)
            return ((SimpleAnimationAbility) deathAbility).getDuration();
        return 20;
    }

    @Override
    public void writeSpawnData(PacketByteBuf buffer) {

    }

    public abstract AbilityType getHurtAbility();

    public abstract AbilityType getDeathAbility();


    @Override
    public boolean damage(DamageSource source, float damage) {
        boolean attack = super.damage(source, damage);
        if (attack) {
            if (this.getHealth() > 0.0F && (this.getActiveAbility() == null || this.getActiveAbility().damageInterrupts()) && this.playsHurtAnimation) {
                this.sendAbilityMessage(this.getHurtAbility());
            } else if (this.getHealth() <= 0.0F) {
                this.sendAbilityMessage(this.getDeathAbility());
            }
        }
        return attack;
    }

    protected <E extends GeoEntity> PlayState predicate(AnimationState<E> state) {
        AbilityCapability.IAbilityCapability abilityCapability = this.getAbilityCapability();
        FrozenCapability.IFrozenCapability frozenCapability = CapabilityHandler.getCapability(this, CapabilityHandler.FROZEN_CAPABILITY);
        if (abilityCapability == null) {
            return PlayState.STOP;
        }
        if (frozenCapability != null && frozenCapability.getFrozen()) {
            return PlayState.STOP;
        }

        if (abilityCapability.getActiveAbility() != null) {
            this.getController().transitionLength(0);
            return abilityCapability.animationPredicate(state, null);
        } else {
            this.loopingAnimations(state);
            return PlayState.CONTINUE;
        }
    }

    protected <E extends GeoEntity> void loopingAnimations(AnimationState<E> event) {
        event.getController().setAnimation(IDLE_ANIM);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(this.controller);
    }

    public MowzieAnimationController<MowzieGeckoEntity> getController() {
        return this.controller;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    public AbilityType<?, ?>[] getAbilities() {
        return new AbilityType[]{};
    }

    public AbilityCapability.IAbilityCapability getAbilityCapability() {
        return AbilityHandler.INSTANCE.getAbilityCapability(this);
    }

    public Ability getActiveAbility() {
        AbilityCapability.IAbilityCapability capability = this.getAbilityCapability();
        if (capability == null) return null;
        return this.getAbilityCapability().getActiveAbility();
    }

    public AbilityType getActiveAbilityType() {
        Ability ability = this.getActiveAbility();
        if (ability == null) return null;
        return ability.getAbilityType();
    }

    public Ability getAbility(AbilityType abilityType) {
        AbilityCapability.IAbilityCapability capability = this.getAbilityCapability();
        if (capability == null) return null;
        return this.getAbilityCapability().getAbilityMap().get(abilityType);
    }

    public void sendAbilityMessage(AbilityType abilityType) {
        AbilityHandler.INSTANCE.sendAbilityMessage(this, abilityType);
    }


}

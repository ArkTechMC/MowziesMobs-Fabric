package com.bobmowzie.mowziesmobs.server.ai;

import com.bobmowzie.mowziesmobs.server.ability.Ability;
import com.bobmowzie.mowziesmobs.server.ability.AbilityHandler;
import com.bobmowzie.mowziesmobs.server.ability.AbilityType;
import com.bobmowzie.mowziesmobs.server.entity.MowzieGeckoEntity;
import net.minecraft.entity.ai.goal.Goal;

import java.util.EnumSet;

public class UseAbilityAI<T extends MowzieGeckoEntity> extends Goal {

    protected final T entity;

    protected AbilityType abilityType;

    public UseAbilityAI(T entity, AbilityType ability) {
        this(entity, ability, true);
    }

    public UseAbilityAI(T entity, AbilityType ability, boolean interruptsAI) {
        this.entity = entity;
        this.abilityType = ability;
        if (interruptsAI) this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
    }

    @Override
    public boolean canStart() {
        if (this.entity.getActiveAbility() == null) return false;
        return this.entity.getActiveAbility().getAbilityType() == this.abilityType;
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void stop() {
        super.stop();
        Ability ability = this.entity.getActiveAbility();
        if (ability != null && ability.getAbilityType() == this.abilityType) {
            AbilityHandler.INSTANCE.sendInterruptAbilityMessage(this.entity, ability.getAbilityType());
        }
    }

    @Override
    public boolean shouldRunEveryTick() {
        return true;
    }
}

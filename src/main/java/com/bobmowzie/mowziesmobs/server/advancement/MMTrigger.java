package com.bobmowzie.mowziesmobs.server.advancement;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.advancement.PlayerAdvancementTracker;
import net.minecraft.advancement.criterion.Criterion;
import net.minecraft.advancement.criterion.CriterionConditions;

import java.util.Map;
import java.util.Set;

public abstract class MMTrigger<E extends CriterionConditions, T extends MMTrigger.Listener<E>> implements Criterion<E> {

    protected final Map<PlayerAdvancementTracker, T> listeners = Maps.newHashMap();

    @Override
    public void beginTrackingCondition(PlayerAdvancementTracker playerAdvancements, Criterion.ConditionsContainer<E> listener) {
        Listener<E> listeners = this.listeners.computeIfAbsent(playerAdvancements, this::createListener);

        listeners.add(listener);
    }

    @Override
    public void endTrackingCondition(PlayerAdvancementTracker playerAdvancements, Criterion.ConditionsContainer<E> listener) {
        Listener<E> listeners = this.listeners.get(playerAdvancements);

        if (listeners != null) {
            listeners.remove(listener);

            if (listeners.isEmpty()) {
                this.listeners.remove(playerAdvancements);
            }
        }
    }

    @Override
    public void endTracking(PlayerAdvancementTracker playerAdvancementsIn) {
        this.listeners.remove(playerAdvancementsIn);
    }

    public abstract T createListener(PlayerAdvancementTracker playerAdvancements);

    static class Listener<E extends CriterionConditions> {
        protected final PlayerAdvancementTracker playerAdvancements;
        protected final Set<Criterion.ConditionsContainer<E>> listeners = Sets.newHashSet();

        public Listener(PlayerAdvancementTracker playerAdvancementsIn) {
            this.playerAdvancements = playerAdvancementsIn;
        }

        public boolean isEmpty() {
            return this.listeners.isEmpty();
        }

        public void add(Criterion.ConditionsContainer<E> listener) {
            this.listeners.add(listener);
        }

        public void remove(Criterion.ConditionsContainer<E> listener) {
            this.listeners.remove(listener);
        }
    }
}
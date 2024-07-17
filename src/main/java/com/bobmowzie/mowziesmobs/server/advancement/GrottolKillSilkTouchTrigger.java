package com.bobmowzie.mowziesmobs.server.advancement;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.google.gson.JsonObject;
import net.minecraft.advancement.PlayerAdvancementTracker;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class GrottolKillSilkTouchTrigger extends MMTrigger<AbstractCriterionConditions, GrottolKillSilkTouchTrigger.Listener> {
    public static final Identifier ID = new Identifier(MowziesMobs.MODID, "kill_grottol_silk_touch");

    public GrottolKillSilkTouchTrigger() {
    }

    @Override
    public Identifier getId() {
        return ID;
    }

    @Override
    public AbstractCriterionConditions conditionsFromJson(JsonObject object, AdvancementEntityPredicateDeserializer conditions) {
        LootContextPredicate player = EntityPredicate.contextPredicateFromJson(object, "player", conditions);
        return new Instance(player);
    }

    @Override
    public Listener createListener(PlayerAdvancementTracker playerAdvancements) {
        return new Listener(playerAdvancements);
    }

    public void trigger(ServerPlayerEntity player) {
        Listener listeners = this.listeners.get(player.getAdvancementTracker());

        if (listeners != null) {
            listeners.trigger();
        }
    }

    static class Listener extends MMTrigger.Listener<AbstractCriterionConditions> {

        public Listener(PlayerAdvancementTracker playerAdvancementsIn) {
            super(playerAdvancementsIn);
        }

        public void trigger() {
            this.listeners.stream().findFirst().ifPresent(listener -> listener.grant(this.playerAdvancements));
        }
    }

    public static class Instance extends AbstractCriterionConditions {
        public Instance(LootContextPredicate player) {
            super(GrottolKillSilkTouchTrigger.ID, player);
        }
    }
}

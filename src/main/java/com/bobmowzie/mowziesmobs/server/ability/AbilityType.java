package com.bobmowzie.mowziesmobs.server.ability;

import net.minecraft.entity.LivingEntity;

public class AbilityType<M extends LivingEntity, T extends Ability<M>> implements Comparable<AbilityType<M, T>> {
    private final IFactory<M, T> factory;
    private final String name;

    public AbilityType(String name, IFactory<M, T> factoryIn) {
        this.factory = factoryIn;
        this.name = name;
    }

    public T makeInstance(LivingEntity user) {
        return this.factory.create(this, (M) user);
    }

    public String getName() {
        return this.name;
    }

    @Override
    public int compareTo(AbilityType<M, T> o) {
        return this.getName().compareTo(o.getName());
    }

    public interface IFactory<M extends LivingEntity, T extends Ability<M>> {
        T create(AbilityType<M, T> p_create_1_, M user);
    }
}

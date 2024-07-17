package com.bobmowzie.mowziesmobs.server.capability;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistryV3;
import dev.onyxstudios.cca.api.v3.component.ComponentV3;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public class LivingCapability {
    public static Identifier ID = new Identifier(MowziesMobs.MODID, "living_cap");

    public interface ILivingCapability {
        float getLastDamage();

        void setLastDamage(float damage);

        boolean getHasSunblock();

        void setHasSunblock(boolean hasSunblock);

        void tick(LivingEntity entity);

        NbtCompound serializeNBT(NbtCompound tag);

        void deserializeNBT(NbtCompound nbt);
    }

    public static class LivingCapabilityImp implements ILivingCapability {
        float lastDamage = 0;
        boolean hasSunblock;

        @Override
        public float getLastDamage() {
            return this.lastDamage;
        }

        @Override
        public void setLastDamage(float damage) {
            this.lastDamage = damage;
        }

        @Override
        public boolean getHasSunblock() {
            return this.hasSunblock;
        }

        @Override
        public void setHasSunblock(boolean hasSunblock) {
            this.hasSunblock = hasSunblock;
        }

        @Override
        public void tick(LivingEntity entity) {
//            if (!hasSunblock && entity.isPotionActive(EffectHandler.SUNBLOCK)) hasSunblock = true;
        }

        @Override
        public NbtCompound serializeNBT(NbtCompound tag) {
            return tag;
        }

        @Override
        public void deserializeNBT(NbtCompound nbt) {
        }
    }

    public static class LivingProvider implements ComponentV3, AutoSyncedComponent, CommonTickingComponent {
        protected static final ComponentKey<AbilityCapability.AbilityProvider> LIVING_COMPONENT = ComponentRegistryV3.INSTANCE.getOrCreate(new Identifier(MowziesMobs.MODID, "ability"), AbilityCapability.AbilityProvider.class);
        private final ILivingCapability capability = new LivingCapabilityImp();
        private final LivingEntity entity;

        public LivingProvider(LivingEntity entity) {
            this.entity = entity;
        }

        public static AbilityCapability.AbilityProvider get(LivingEntity entity) {
            return LIVING_COMPONENT.get(entity);
        }

        @Override
        public void tick() {
            this.capability.tick(this.entity);
        }

        @Override
        public void readFromNbt(@NotNull NbtCompound tag) {
            this.capability.deserializeNBT(tag);
        }

        @Override
        public void writeToNbt(@NotNull NbtCompound tag) {
            this.capability.serializeNBT(tag);
        }
    }
}

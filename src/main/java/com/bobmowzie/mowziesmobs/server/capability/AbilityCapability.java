package com.bobmowzie.mowziesmobs.server.capability;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieGeoModel;
import com.bobmowzie.mowziesmobs.client.render.entity.player.GeckoPlayer;
import com.bobmowzie.mowziesmobs.server.ability.Ability;
import com.bobmowzie.mowziesmobs.server.ability.AbilityHandler;
import com.bobmowzie.mowziesmobs.server.ability.AbilityType;
import com.bobmowzie.mowziesmobs.server.entity.MowzieGeckoEntity;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistryV3;
import dev.onyxstudios.cca.api.v3.component.ComponentV3;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.object.PlayState;

import java.util.*;

public class AbilityCapability {
    public static IAbilityCapability get(LivingEntity entity) {
        return AbilityProvider.get(entity).capability;
    }

    public interface IAbilityCapability {

        void activateAbility(LivingEntity entity, AbilityType<?, ?> ability);

        void instanceAbilities(LivingEntity entity);

        void tick(LivingEntity entity);

        AbilityType<?, ?>[] getAbilityTypesOnEntity(LivingEntity entity);

        Map<AbilityType<?, ?>, Ability> getAbilityMap();

        Ability getAbilityFromType(AbilityType<?, ?> abilityType);

        Collection<Ability> getAbilities();

        Ability getActiveAbility();

        void setActiveAbility(Ability<?> activeAbility);

        boolean attackingPrevented();

        boolean blockBreakingBuildingPrevented();

        boolean interactingPrevented();

        boolean itemUsePrevented(ItemStack itemStack);

        <E extends GeoEntity> PlayState animationPredicate(AnimationState<E> e, GeckoPlayer.Perspective perspective);

        void codeAnimations(MowzieGeoModel<? extends GeoEntity> model, float partialTick);

        NbtCompound serializeNBT(NbtCompound tag);

        void deserializeNBT(NbtCompound nbt);

    }

    public static class AbilityCapabilityImp implements IAbilityCapability {
        SortedMap<AbilityType<?, ?>, Ability> abilityInstances = new TreeMap<>();
        Ability activeAbility = null;
        Map<String, NbtElement> nbtMap = new HashMap<>();

        @Override
        public void instanceAbilities(LivingEntity entity) {
            this.setActiveAbility(null);
            for (AbilityType<? extends LivingEntity, ?> abilityType : this.getAbilityTypesOnEntity(entity)) {
                Ability ability = abilityType.makeInstance(entity);
                this.abilityInstances.put(abilityType, ability);
                if (this.nbtMap.containsKey(abilityType.getName()))
                    ability.readNBT(this.nbtMap.get(abilityType.getName()));
            }
        }

        @Override
        public void activateAbility(LivingEntity entity, AbilityType<?, ?> abilityType) {
            Ability ability = this.abilityInstances.get(abilityType);
            if (ability != null) {
                boolean tryResult = ability.tryAbility();
                if (tryResult) ability.start();
            } else
                System.out.println("Ability " + abilityType.toString() + " does not exist on mob " + entity.getClass().getSimpleName());
        }

        @Override
        public void tick(LivingEntity entity) {
            for (Ability ability : this.abilityInstances.values()) {
                ability.tick();
            }
        }

        @Override
        public AbilityType<?, ?>[] getAbilityTypesOnEntity(LivingEntity entity) {
            if (entity instanceof PlayerEntity) {
                return AbilityHandler.PLAYER_ABILITIES;
            }
            if (entity instanceof MowzieGeckoEntity) {
                return ((MowzieGeckoEntity) entity).getAbilities();
            }
            return new AbilityType[0];
        }

        @Override
        public Map<AbilityType<?, ?>, Ability> getAbilityMap() {
            return this.abilityInstances;
        }

        @Override
        public Ability getAbilityFromType(AbilityType<?, ?> abilityType) {
            return this.abilityInstances.get(abilityType);
        }

        @Override
        public Ability<?> getActiveAbility() {
            return this.activeAbility;
        }

        @Override
        public void setActiveAbility(Ability<?> activeAbility) {
            if (this.getActiveAbility() != null && this.getActiveAbility().isUsing())
                this.getActiveAbility().interrupt();
            this.activeAbility = activeAbility;
        }

        @Override
        public Collection<Ability> getAbilities() {
            return this.abilityInstances.values();
        }

        @Override
        public boolean attackingPrevented() {
            return this.getActiveAbility() != null && this.getActiveAbility().preventsAttacking();
        }

        @Override
        public boolean blockBreakingBuildingPrevented() {
            return this.getActiveAbility() != null && this.getActiveAbility().preventsBlockBreakingBuilding();
        }

        @Override
        public boolean interactingPrevented() {
            return this.getActiveAbility() != null && this.getActiveAbility().preventsInteracting();
        }

        @Override
        public boolean itemUsePrevented(ItemStack itemStack) {
            return this.getActiveAbility() != null && this.getActiveAbility().preventsItemUse(itemStack);
        }

        @Override
        public <E extends GeoEntity> PlayState animationPredicate(AnimationState<E> e, GeckoPlayer.Perspective perspective) {
            return this.getActiveAbility().animationPredicate(e, perspective);
        }

        public void codeAnimations(MowzieGeoModel<? extends GeoEntity> model, float partialTick) {
            this.getActiveAbility().codeAnimations(model, partialTick);
        }

        @Override
        public NbtCompound serializeNBT(NbtCompound compound) {
            for (Map.Entry<AbilityType<?, ?>, Ability> abilityEntry : this.getAbilityMap().entrySet()) {
                NbtCompound nbt = abilityEntry.getValue().writeNBT();
                if (!nbt.isEmpty()) {
                    compound.put(abilityEntry.getKey().getName(), nbt);
                }
            }
            return compound;
        }

        @Override
        public void deserializeNBT(NbtCompound nbt) {
            Set<String> keys = nbt.getKeys();
            for (String abilityName : keys) {
                this.nbtMap.put(abilityName, nbt.get(abilityName));
            }
        }
    }

    public static class AbilityProvider implements ComponentV3, AutoSyncedComponent, CommonTickingComponent {
        protected static final ComponentKey<AbilityProvider> COMPONENT = ComponentRegistryV3.INSTANCE.getOrCreate(new Identifier(MowziesMobs.MODID, "ability"), AbilityProvider.class);
        private final IAbilityCapability capability = new AbilityCapabilityImp();
        private final LivingEntity entity;

        public AbilityProvider(LivingEntity entity) {
            this.entity = entity;
        }

        public static AbilityProvider get(LivingEntity entity) {
            return COMPONENT.get(entity);
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

package com.bobmowzie.mowziesmobs.server.capability;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.render.entity.player.GeckoPlayer;
import com.bobmowzie.mowziesmobs.server.ability.Ability;
import com.bobmowzie.mowziesmobs.server.ability.AbilityHandler;
import com.bobmowzie.mowziesmobs.server.ability.PlayerAbility;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.umvuthana.EntityUmvuthanaFollowerToPlayer;
import com.bobmowzie.mowziesmobs.server.item.ItemEarthrendGauntlet;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import com.bobmowzie.mowziesmobs.server.message.StaticVariables;
import com.bobmowzie.mowziesmobs.server.potion.EffectHandler;
import com.bobmowzie.mowziesmobs.server.power.Power;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistryV3;
import dev.onyxstudios.cca.api.v3.component.ComponentV3;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PlayerCapability {
    public static Identifier ID = new Identifier(MowziesMobs.MODID, "player_cap");

    public interface IPlayerCapability {

        Power[] getPowers();

        void tick(PlayerEntity player);

        void addedToWorld(EntityJoinLevelEvent event);

        boolean isVerticalSwing();

        void setVerticalSwing(boolean verticalSwing);

        int getUntilSunstrike();

        void setUntilSunstrike(int untilSunstrike);

        int getUntilAxeSwing();

        void setUntilAxeSwing(int untilAxeSwing);

        boolean getAxeCanAttack();

        void setAxeCanAttack(boolean axeCanAttack);

        boolean isMouseRightDown();

        void setMouseRightDown(boolean mouseRightDown);

        boolean isMouseLeftDown();

        void setMouseLeftDown(boolean mouseLeftDown);

        boolean isPrevSneaking();

        void setPrevSneaking(boolean prevSneaking);

        int getTribeCircleTick();

        void setTribeCircleTick(int tribeCircleTick);

        List<EntityUmvuthanaFollowerToPlayer> getUmvuthanaPack();

        void setUmvuthanaPack(List<EntityUmvuthanaFollowerToPlayer> umvuthanaPack);

        int getTribePackRadius();

        void setTribePackRadius(int tribePackRadius);

        int getPackSize();

        Vec3d getPrevMotion();

        void removePackMember(EntityUmvuthanaFollowerToPlayer tribePlayer);

        void addPackMember(EntityUmvuthanaFollowerToPlayer tribePlayer);

        boolean getUsingSolarBeam();

        void setUsingSolarBeam(boolean b);

        float getPrevCooledAttackStrength();

        void setPrevCooledAttackStrength(float cooledAttackStrength);

        @Environment(EnvType.CLIENT)
        GeckoPlayer.GeckoPlayerThirdPerson getGeckoPlayer();

        NbtCompound serializeNBT(NbtCompound tag);

        void deserializeNBT(NbtCompound nbt);
    }

    public static class PlayerCapabilityImp implements IPlayerCapability {
        public boolean verticalSwing = false;
        public int untilSunstrike = 0;
        public int untilAxeSwing = 0;
        public boolean mouseRightDown = false;
        public boolean mouseLeftDown = false;
        public boolean prevSneaking;
        public int tribeCircleTick;
        public List<EntityUmvuthanaFollowerToPlayer> umvuthanaPack = new ArrayList<>();
        public int tribePackRadius = 3;
        public boolean axeCanAttack;
        public Vec3d prevMotion;
        public Power[] powers = new Power[]{};
        private int prevTime;
        private int time;
        private float prevCooledAttackStrength;
        @Environment(EnvType.CLIENT)
        private GeckoPlayer.GeckoPlayerThirdPerson geckoPlayer;
        private boolean usingSolarBeam;

        public boolean isVerticalSwing() {
            return this.verticalSwing;
        }

        public void setVerticalSwing(boolean verticalSwing) {
            this.verticalSwing = verticalSwing;
        }

        public int getUntilSunstrike() {
            return this.untilSunstrike;
        }

        public void setUntilSunstrike(int untilSunstrike) {
            this.untilSunstrike = untilSunstrike;
        }

        public int getUntilAxeSwing() {
            return this.untilAxeSwing;
        }

        public void setUntilAxeSwing(int untilAxeSwing) {
            this.untilAxeSwing = untilAxeSwing;
        }

        public boolean getAxeCanAttack() {
            return this.axeCanAttack;
        }

        public void setAxeCanAttack(boolean axeCanAttack) {
            this.axeCanAttack = axeCanAttack;
        }

        public boolean isMouseRightDown() {
            return this.mouseRightDown;
        }

        public void setMouseRightDown(boolean mouseRightDown) {
            this.mouseRightDown = mouseRightDown;
        }

        public boolean isMouseLeftDown() {
            return this.mouseLeftDown;
        }

        public void setMouseLeftDown(boolean mouseLeftDown) {
            this.mouseLeftDown = mouseLeftDown;
        }

        public boolean isPrevSneaking() {
            return this.prevSneaking;
        }

        public void setPrevSneaking(boolean prevSneaking) {
            this.prevSneaking = prevSneaking;
        }

        public int getTribeCircleTick() {
            return this.tribeCircleTick;
        }

        public void setTribeCircleTick(int tribeCircleTick) {
            this.tribeCircleTick = tribeCircleTick;
        }

        public List<EntityUmvuthanaFollowerToPlayer> getUmvuthanaPack() {
            return this.umvuthanaPack;
        }

        public void setUmvuthanaPack(List<EntityUmvuthanaFollowerToPlayer> umvuthanaPack) {
            this.umvuthanaPack = umvuthanaPack;
        }

        public int getTribePackRadius() {
            return this.tribePackRadius;
        }

        public void setTribePackRadius(int tribePackRadius) {
            this.tribePackRadius = tribePackRadius;
        }

        public Vec3d getPrevMotion() {
            return this.prevMotion;
        }

        public boolean getUsingSolarBeam() {
            return this.usingSolarBeam;
        }

        public void setUsingSolarBeam(boolean b) {
            this.usingSolarBeam = b;
        }

        @Override
        public float getPrevCooledAttackStrength() {
            return this.prevCooledAttackStrength;
        }

        @Override
        public void setPrevCooledAttackStrength(float cooledAttackStrength) {
            this.prevCooledAttackStrength = cooledAttackStrength;
        }

        @Environment(EnvType.CLIENT)
        public GeckoPlayer.GeckoPlayerThirdPerson getGeckoPlayer() {
            return this.geckoPlayer;
        }

        @Override
        public void addedToWorld(EntityJoinLevelEvent event) {
            // Create the geckoplayer instances when an entity joins the world
            // Normally, the animation controllers and lastModel field are only set when rendered for the first time, but this won't work for player animations
            if (event.getLevel().isClientSide()) {
                PlayerEntity player = (PlayerEntity) event.getEntity();
                this.geckoPlayer = new GeckoPlayer.GeckoPlayerThirdPerson(player);
                // Only create 1st person instance if the player joining is this client's player
                if (event.getEntity() == MinecraftClient.getInstance().player) {
                    GeckoPlayer.GeckoPlayerFirstPerson geckoPlayerFirstPerson = new GeckoPlayer.GeckoPlayerFirstPerson(player);
                }
            }
        }

        public void tick(PlayerEntity player) {
            this.tribeCircleTick++;

            this.prevMotion = player.getPos().subtract(new Vec3d(player.prevX, player.prevY, player.prevZ));
            this.prevTime = this.time;
            if (this.untilSunstrike > 0) {
                this.untilSunstrike--;
            }
            if (this.untilAxeSwing > 0) {
                this.untilAxeSwing--;
            }

            if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER) {
                if (player.getMainHandStack().getItem() instanceof ItemEarthrendGauntlet || player.getOffHandStack().getItem() instanceof ItemEarthrendGauntlet) {
                    player.addStatusEffect(new StatusEffectInstance(EffectHandler.GEOMANCY, 20, 0, false, false));
                }

                List<EntityUmvuthanaFollowerToPlayer> pack = this.umvuthanaPack;
                float theta = (2 * (float) Math.PI / pack.size());
                for (int i = 0; i < pack.size(); i++) {
                    EntityUmvuthanaFollowerToPlayer barakoan = pack.get(i);
                    barakoan.index = i;
                    float distanceToPlayer = player.distanceTo(barakoan);
                    if (barakoan.getTarget() == null && barakoan.getActiveAbility() == null) {
                        if (distanceToPlayer > 4)
                            barakoan.getNavigation().startMovingTo(player.getX() + this.tribePackRadius * MathHelper.cos(theta * i), player.getY(), player.getZ() + this.tribePackRadius * MathHelper.sin(theta * i), 0.45);
                        else
                            barakoan.getNavigation().stop();
                        if (distanceToPlayer > 20 && player.isOnGround()) {
                            this.tryTeleportUmvuthanaFollower(player, barakoan);
                        }
                    }
                }
            }

            Ability<?> iceBreathAbility = AbilityHandler.INSTANCE.getAbility(player, AbilityHandler.ICE_BREATH_ABILITY);
            if (iceBreathAbility != null && !iceBreathAbility.isUsing()) {
                for (ItemStack stack : player.getInventory().main)
                    this.restoreIceCrystalStack(player, stack);
                for (ItemStack stack : player.getInventory().offHand)
                    this.restoreIceCrystalStack(player, stack);
            }

            this.useIceCrystalStack(player);

            if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
                if (MinecraftClient.getInstance().options.attackKey.isPressed() && !this.mouseLeftDown) {
                    this.mouseLeftDown = true;
                    ClientPlayNetworking.send(StaticVariables.LEFT_MOUSE_DOWN, PacketByteBufs.create());
                    for (Power power : this.powers) power.onLeftMouseDown(player);
                    AbilityCapability.IAbilityCapability abilityCapability = AbilityHandler.INSTANCE.getAbilityCapability(player);
                    if (abilityCapability != null)
                        for (Ability<?> ability : abilityCapability.getAbilities())
                            if (ability instanceof PlayerAbility playerAbility)
                                playerAbility.onLeftMouseDown(player);
                }
                if (MinecraftClient.getInstance().options.useKey.isPressed() && !this.mouseRightDown) {
                    this.mouseRightDown = true;
                    ClientPlayNetworking.send(StaticVariables.RIGHT_MOUSE_DOWN, PacketByteBufs.create());
                    for (Power power : this.powers) power.onRightMouseDown(player);
                    AbilityCapability.IAbilityCapability abilityCapability = AbilityHandler.INSTANCE.getAbilityCapability(player);
                    if (abilityCapability != null)
                        for (Ability<?> ability : abilityCapability.getAbilities())
                            if (ability instanceof PlayerAbility playerAbility)
                                playerAbility.onRightMouseDown(player);
                }
                if (!MinecraftClient.getInstance().options.attackKey.isPressed() && this.mouseLeftDown) {
                    this.mouseLeftDown = false;
                    ClientPlayNetworking.send(StaticVariables.LEFT_MOUSE_UP, PacketByteBufs.create());
                    for (Power power : this.powers) power.onLeftMouseUp(player);
                    AbilityCapability.IAbilityCapability abilityCapability = AbilityHandler.INSTANCE.getAbilityCapability(player);
                    if (abilityCapability != null)
                        for (Ability<?> ability : abilityCapability.getAbilities())
                            if (ability instanceof PlayerAbility playerAbility)
                                playerAbility.onLeftMouseUp(player);
                }
                if (!MinecraftClient.getInstance().options.useKey.isPressed() && this.mouseRightDown) {
                    this.mouseRightDown = false;
                    ClientPlayNetworking.send(StaticVariables.RIGHT_MOUSE_UP, PacketByteBufs.create());
                    for (Power power : this.powers) power.onRightMouseUp(player);
                    AbilityCapability.IAbilityCapability abilityCapability = AbilityHandler.INSTANCE.getAbilityCapability(player);
                    if (abilityCapability != null)
                        for (Ability<?> ability : abilityCapability.getAbilities())
                            if (ability instanceof PlayerAbility playerAbility)
                                playerAbility.onRightMouseUp(player);
                }
            }

            if (player.isSneaking() && !this.prevSneaking) {
                for (Power power : this.powers) power.onSneakDown(player);
                AbilityCapability.IAbilityCapability abilityCapability = AbilityHandler.INSTANCE.getAbilityCapability(player);
                if (abilityCapability != null) {
                    for (Ability<?> ability : abilityCapability.getAbilities()) {
                        if (ability instanceof PlayerAbility playerAbility)
                            playerAbility.onSneakDown(player);
                    }
                }
            } else if (!player.isSneaking() && this.prevSneaking) {
                for (Power power : this.powers) power.onSneakUp(player);
                AbilityCapability.IAbilityCapability abilityCapability = AbilityHandler.INSTANCE.getAbilityCapability(player);
                if (abilityCapability != null) {
                    for (Ability<?> ability : abilityCapability.getAbilities()) {
                        if (ability instanceof PlayerAbility playerAbility)
                            playerAbility.onSneakUp(player);
                    }
                }
            }
            this.prevSneaking = player.isSneaking();
        }

        private void restoreIceCrystalStack(PlayerEntity entity, ItemStack stack) {
            if (stack.getItem() == ItemHandler.ICE_CRYSTAL) {
                if (!ConfigHandler.COMMON.TOOLS_AND_ABILITIES.ICE_CRYSTAL.breakable) {
                    stack.setDamage(Math.max(stack.getDamage() - 1, 0));
                }
            }
        }

        private void useIceCrystalStack(PlayerEntity player) {
            ItemStack stack = player.getActiveItem();
            if (stack.getItem() == ItemHandler.ICE_CRYSTAL) {
                Ability iceBreathAbility = AbilityHandler.INSTANCE.getAbility(player, AbilityHandler.ICE_BREATH_ABILITY);
                if (iceBreathAbility != null && iceBreathAbility.isUsing()) {
                    Hand handIn = player.getActiveHand();
                    if (stack.getDamage() + 5 < stack.getMaxDamage()) {
                        stack.damage(5, player, p -> p.sendToolBreakStatus(handIn));
                    } else {
                        if (ConfigHandler.COMMON.TOOLS_AND_ABILITIES.ICE_CRYSTAL.breakable) {
                            stack.damage(5, player, p -> p.sendToolBreakStatus(handIn));
                        }
                        iceBreathAbility.end();
                    }
                }
            }
        }

        private void tryTeleportUmvuthanaFollower(PlayerEntity player, EntityUmvuthanaFollowerToPlayer umvuthana) {
            int x = MathHelper.floor(player.getX()) - 2;
            int z = MathHelper.floor(player.getZ()) - 2;
            int y = MathHelper.floor(player.getBoundingBox().minY);

            for (int l = 0; l <= 4; ++l) {
                for (int i1 = 0; i1 <= 4; ++i1) {
                    if ((l < 1 || i1 < 1 || l > 3 || i1 > 3) && umvuthana.isTeleportFriendlyBlock(x, z, y, l, i1)) {
                        umvuthana.refreshPositionAndAngles((float) (x + l) + 0.5F, y, (float) (z + i1) + 0.5F, umvuthana.getYaw(), umvuthana.getPitch());
                        umvuthana.getNavigation().stop();
                        return;
                    }
                }
            }
        }

        public int getTick() {
            return this.time;
        }

        public void decrementTime() {
            this.time--;
        }

        public int getPackSize() {
            this.umvuthanaPack.removeIf(Entity::isRemoved);
            return this.umvuthanaPack.size();
        }

        public void removePackMember(EntityUmvuthanaFollowerToPlayer followerToPlayer) {
            this.umvuthanaPack.remove(followerToPlayer);
        }

        public void addPackMember(EntityUmvuthanaFollowerToPlayer followerToPlayer) {
            this.umvuthanaPack.add(followerToPlayer);
        }

        public Power[] getPowers() {
            return this.powers;
        }

        @Override
        public NbtCompound serializeNBT(NbtCompound compound) {
            compound.putInt("untilSunstrike", this.untilSunstrike);
            compound.putInt("untilAxeSwing", this.untilAxeSwing);
            compound.putInt("prevTime", this.prevTime);
            compound.putInt("time", this.time);
            return compound;
        }

        @Override
        public void deserializeNBT(NbtCompound compound) {
            this.untilSunstrike = compound.getInt("untilSunstrike");
            this.untilAxeSwing = compound.getInt("untilAxeSwing");
            this.prevTime = compound.getInt("prevTime");
            this.time = compound.getInt("time");
        }
    }

    public static class PlayerProvider implements ComponentV3, AutoSyncedComponent, CommonTickingComponent {
        protected static final ComponentKey<AbilityCapability.AbilityProvider> LIVING_COMPONENT = ComponentRegistryV3.INSTANCE.getOrCreate(new Identifier(MowziesMobs.MODID, "ability"), AbilityCapability.AbilityProvider.class);
        private final IPlayerCapability capability = new PlayerCapabilityImp();
        private final PlayerEntity player;

        public PlayerProvider(PlayerEntity player) {
            this.player = player;
        }

        public static AbilityCapability.AbilityProvider get(PlayerEntity player) {
            return LIVING_COMPONENT.get(player);
        }

        @Override
        public void tick() {
            this.capability.tick(this.player);
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

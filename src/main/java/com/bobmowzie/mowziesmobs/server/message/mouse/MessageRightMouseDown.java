package com.bobmowzie.mowziesmobs.server.message.mouse;

import com.bobmowzie.mowziesmobs.server.ability.Ability;
import com.bobmowzie.mowziesmobs.server.ability.AbilityHandler;
import com.bobmowzie.mowziesmobs.server.ability.PlayerAbility;
import com.bobmowzie.mowziesmobs.server.capability.AbilityCapability;
import com.bobmowzie.mowziesmobs.server.capability.CapabilityHandler;
import com.bobmowzie.mowziesmobs.server.capability.PlayerCapability;
import com.bobmowzie.mowziesmobs.server.power.Power;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * Created by BobMowzie on 5/25/2017.
 */
public class MessageRightMouseDown {
    public MessageRightMouseDown() {
    }

    public static void serialize(final MessageRightMouseDown message, final PacketByteBuf buf) {

    }

    public static MessageRightMouseDown deserialize(final PacketByteBuf buf) {
        final MessageRightMouseDown message = new MessageRightMouseDown();
        return message;
    }

    public static final class Handler implements BiConsumer<MessageRightMouseDown, Supplier<NetworkEvent.Context>> {
        @Override
        public void accept(final MessageRightMouseDown message, final Supplier<NetworkEvent.Context> contextSupplier) {
            final NetworkEvent.Context context = contextSupplier.get();
            final ServerPlayerEntity player = context.getSender();
            context.enqueueWork(() -> this.accept(message, player));
            context.setPacketHandled(true);
        }

        private void accept(final MessageRightMouseDown message, final ServerPlayerEntity player) {
            if (player != null) {
                PlayerCapability.IPlayerCapability capability = CapabilityHandler.getCapability(player, CapabilityHandler.PLAYER_CAPABILITY);
                if (capability != null) {
                    capability.setMouseRightDown(true);
                    Power[] powers = capability.getPowers();
                    for (int i = 0; i < powers.length; i++) {
                        powers[i].onRightMouseDown(player);
                    }
                }

                AbilityCapability.IAbilityCapability abilityCapability = AbilityHandler.INSTANCE.getAbilityCapability(player);
                if (abilityCapability != null) {
                    for (Ability ability : abilityCapability.getAbilities()) {
                        if (ability instanceof PlayerAbility) {
                            ((PlayerAbility) ability).onRightMouseDown(player);
                        }
                    }
                }
            }
        }
    }
}
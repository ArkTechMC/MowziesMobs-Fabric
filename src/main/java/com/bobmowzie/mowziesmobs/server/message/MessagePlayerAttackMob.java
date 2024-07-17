package com.bobmowzie.mowziesmobs.server.message;

import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * Created by BobMowzie on 10/28/2016.
 */
public class MessagePlayerAttackMob {
    private int entityID;

    public MessagePlayerAttackMob() {

    }

    public MessagePlayerAttackMob(LivingEntity target) {
        this.entityID = target.getId();
    }

    public static void serialize(final MessagePlayerAttackMob message, final PacketByteBuf buf) {
        buf.writeVarInt(message.entityID);
    }

    public static MessagePlayerAttackMob deserialize(final PacketByteBuf buf) {
        final MessagePlayerAttackMob message = new MessagePlayerAttackMob();
        message.entityID = buf.readVarInt();
        return message;
    }

    public static class Handler implements BiConsumer<MessagePlayerAttackMob, Supplier<NetworkEvent.Context>> {
        @Override
        public void accept(final MessagePlayerAttackMob message, final Supplier<NetworkEvent.Context> contextSupplier) {
            final NetworkEvent.Context context = contextSupplier.get();
            final ServerPlayerEntity player = context.getSender();
            context.enqueueWork(() -> {
                if (player != null) {
                    Entity entity = player.level().getEntity(message.entityID);
                    if (entity != null) player.attack(entity);
                }
            });
            context.setPacketHandled(true);
        }
    }
}

package com.bobmowzie.mowziesmobs.server.message;

import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketByteBuf;

/**
 * Created by BobMowzie on 11/14/2016.
 */
public class MessageSculptorTrade {
    private int entityID;

    public MessageSculptorTrade() {

    }

    public MessageSculptorTrade(LivingEntity sender) {
        this.entityID = sender.getId();
    }

    public static void serialize(final MessageSculptorTrade message, final PacketByteBuf buf) {
        buf.writeVarInt(message.entityID);
    }

    public static MessageSculptorTrade deserialize(final PacketByteBuf buf) {
        final MessageSculptorTrade message = new MessageSculptorTrade();
        message.entityID = buf.readVarInt();
        return message;
    }

    public int getEntityID() {
        return this.entityID;
    }
}

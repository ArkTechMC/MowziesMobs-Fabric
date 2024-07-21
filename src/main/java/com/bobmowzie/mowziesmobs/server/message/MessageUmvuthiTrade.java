package com.bobmowzie.mowziesmobs.server.message;

import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketByteBuf;

/**
 * Created by BobMowzie on 11/14/2016.
 */
public class MessageUmvuthiTrade {
    private int entityID;

    public MessageUmvuthiTrade() {

    }

    public MessageUmvuthiTrade(LivingEntity sender) {
        this.entityID = sender.getId();
    }

    public static void serialize(final MessageUmvuthiTrade message, final PacketByteBuf buf) {
        buf.writeVarInt(message.entityID);
    }

    public static MessageUmvuthiTrade deserialize(final PacketByteBuf buf) {
        final MessageUmvuthiTrade message = new MessageUmvuthiTrade();
        message.entityID = buf.readVarInt();
        return message;
    }

    public int getEntityID() {
        return this.entityID;
    }
}

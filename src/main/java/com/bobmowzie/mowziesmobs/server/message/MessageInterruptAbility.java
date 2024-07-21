package com.bobmowzie.mowziesmobs.server.message;

import net.minecraft.network.PacketByteBuf;

public class MessageInterruptAbility {
    private int entityID;
    private int index;

    public MessageInterruptAbility() {
    }

    public MessageInterruptAbility(int entityID, int index) {
        this.entityID = entityID;
        this.index = index;
    }

    public static void serialize(final MessageInterruptAbility message, final PacketByteBuf buf) {
        buf.writeVarInt(message.entityID);
        buf.writeVarInt(message.index);
    }

    public static MessageInterruptAbility deserialize(final PacketByteBuf buf) {
        final MessageInterruptAbility message = new MessageInterruptAbility();
        message.entityID = buf.readVarInt();
        message.index = buf.readVarInt();
        return message;
    }

    public int getEntityID() {
        return this.entityID;
    }

    public int getIndex() {
        return this.index;
    }
}

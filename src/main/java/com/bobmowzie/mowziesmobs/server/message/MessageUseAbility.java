package com.bobmowzie.mowziesmobs.server.message;

import net.minecraft.network.PacketByteBuf;

public class MessageUseAbility {
    private int entityID;
    private int index;

    public MessageUseAbility() {

    }

    public MessageUseAbility(int entityID, int index) {
        this.entityID = entityID;
        this.index = index;
    }

    public static void serialize(final MessageUseAbility message, final PacketByteBuf buf) {
        buf.writeVarInt(message.entityID);
        buf.writeVarInt(message.index);
    }

    public static MessageUseAbility deserialize(final PacketByteBuf buf) {
        final MessageUseAbility message = new MessageUseAbility();
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

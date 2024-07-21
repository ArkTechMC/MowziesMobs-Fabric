package com.bobmowzie.mowziesmobs.server.message;

import net.minecraft.network.PacketByteBuf;

public class MessagePlayerUseAbility {
    private int index;

    public MessagePlayerUseAbility() {

    }

    public MessagePlayerUseAbility(int index) {
        this.index = index;
    }

    public static void serialize(final MessagePlayerUseAbility message, final PacketByteBuf buf) {
        buf.writeVarInt(message.index);
    }

    public static MessagePlayerUseAbility deserialize(final PacketByteBuf buf) {
        final MessagePlayerUseAbility message = new MessagePlayerUseAbility();
        message.index = buf.readVarInt();
        return message;
    }

    public int getIndex() {
        return this.index;
    }
}

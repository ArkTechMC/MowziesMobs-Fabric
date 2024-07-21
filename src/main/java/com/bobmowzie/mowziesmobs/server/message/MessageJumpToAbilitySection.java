package com.bobmowzie.mowziesmobs.server.message;

import net.minecraft.network.PacketByteBuf;

public class MessageJumpToAbilitySection {
    private int entityID;
    private int index;
    private int sectionIndex;

    public MessageJumpToAbilitySection() {
    }

    public MessageJumpToAbilitySection(int entityID, int index, int sectionIndex) {
        this.entityID = entityID;
        this.index = index;
        this.sectionIndex = sectionIndex;
    }

    public static void serialize(final MessageJumpToAbilitySection message, final PacketByteBuf buf) {
        buf.writeVarInt(message.entityID);
        buf.writeVarInt(message.index);
        buf.writeVarInt(message.sectionIndex);
    }

    public static MessageJumpToAbilitySection deserialize(final PacketByteBuf buf) {
        final MessageJumpToAbilitySection message = new MessageJumpToAbilitySection();
        message.entityID = buf.readVarInt();
        message.index = buf.readVarInt();
        message.sectionIndex = buf.readVarInt();
        return message;
    }

    public int getIndex() {
        return this.index;
    }

    public int getEntityID() {
        return this.entityID;
    }

    public int getSectionIndex() {
        return this.sectionIndex;
    }
}

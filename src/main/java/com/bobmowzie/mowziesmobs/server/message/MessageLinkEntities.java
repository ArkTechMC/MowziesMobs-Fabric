package com.bobmowzie.mowziesmobs.server.message;

import com.bobmowzie.mowziesmobs.server.entity.ILinkedEntity;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;

/**
 * Created by BobMowzie on 10/28/2016.
 */
public class MessageLinkEntities {
    private int sourceID;
    private int targetID;

    public MessageLinkEntities() {

    }

    public MessageLinkEntities(Entity source, Entity target) {
        if (source instanceof ILinkedEntity) {
            this.sourceID = source.getId();
            this.targetID = target.getId();
        }
    }

    public static void serialize(final MessageLinkEntities message, final PacketByteBuf buf) {
        buf.writeVarInt(message.sourceID);
        buf.writeVarInt(message.targetID);
    }

    public static MessageLinkEntities deserialize(final PacketByteBuf buf) {
        final MessageLinkEntities message = new MessageLinkEntities();
        message.sourceID = buf.readVarInt();
        message.targetID = buf.readVarInt();
        return message;
    }

    public int getSourceID() {
        return this.sourceID;
    }

    public int getTargetID() {
        return this.targetID;
    }
}

package com.bobmowzie.mowziesmobs.server.message;

import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketByteBuf;

/**
 * Created by BobMowzie on 5/31/2017.
 */
public class MessageFreezeEffect {
    private int entityID;
    private boolean isFrozen;

    public MessageFreezeEffect() {

    }

    public MessageFreezeEffect(LivingEntity entity, boolean activate) {
        this.entityID = entity.getId();
        this.isFrozen = activate;
    }

    public static void serialize(final MessageFreezeEffect message, final PacketByteBuf buf) {
        buf.writeVarInt(message.entityID);
        buf.writeBoolean(message.isFrozen);
    }

    public static MessageFreezeEffect deserialize(final PacketByteBuf buf) {
        final MessageFreezeEffect message = new MessageFreezeEffect();
        message.entityID = buf.readVarInt();
        message.isFrozen = buf.readBoolean();
        return message;
    }

    public int getEntityID() {
        return this.entityID;
    }

    public boolean isFrozen() {
        return this.isFrozen;
    }
}

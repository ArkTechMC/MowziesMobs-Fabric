package com.bobmowzie.mowziesmobs.server.message;

import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketByteBuf;

/**
 * Created by BobMowzie on 5/31/2017.
 */
public class MessageSunblockEffect {
    private int entityID;
    private boolean hasSunblock;

    public MessageSunblockEffect() {

    }

    public MessageSunblockEffect(LivingEntity entity, boolean activate) {
        this.entityID = entity.getId();
        this.hasSunblock = activate;
    }

    public static void serialize(final MessageSunblockEffect message, final PacketByteBuf buf) {
        buf.writeVarInt(message.entityID);
        buf.writeBoolean(message.hasSunblock);
    }

    public static MessageSunblockEffect deserialize(final PacketByteBuf buf) {
        final MessageSunblockEffect message = new MessageSunblockEffect();
        message.entityID = buf.readVarInt();
        message.hasSunblock = buf.readBoolean();
        return message;
    }

    public int getEntityID() {
        return this.entityID;
    }

    public boolean hasSunblock() {
        return this.hasSunblock;
    }
}

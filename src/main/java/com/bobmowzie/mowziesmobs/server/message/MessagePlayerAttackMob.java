package com.bobmowzie.mowziesmobs.server.message;

import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketByteBuf;

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

    public int getEntityID() {
        return this.entityID;
    }
}

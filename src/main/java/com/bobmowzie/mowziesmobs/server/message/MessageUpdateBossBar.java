package com.bobmowzie.mowziesmobs.server.message;

import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.util.UUID;

public class MessageUpdateBossBar {
    private UUID bossID;
    private boolean remove;
    private Identifier registryName;

    public MessageUpdateBossBar() {

    }

    // Set entity to null to remove boss from the map
    public MessageUpdateBossBar(UUID bossID, LivingEntity entity) {
        this.bossID = bossID;
        if (entity != null) {
            this.registryName = Registries.ENTITY_TYPE.getId(entity.getType());
            this.remove = false;
        } else {
            this.registryName = null;
            this.remove = true;
        }
    }

    public static void serialize(final MessageUpdateBossBar message, final PacketByteBuf buf) {
        buf.writeUuid(message.bossID);
        buf.writeBoolean(message.remove);
        if (!message.remove && message.registryName != null) buf.writeIdentifier(message.registryName);
    }

    public static MessageUpdateBossBar deserialize(final PacketByteBuf buf) {
        final MessageUpdateBossBar message = new MessageUpdateBossBar();
        message.bossID = buf.readUuid();
        message.remove = buf.readBoolean();
        if (!message.remove) message.registryName = buf.readIdentifier();
        return message;
    }

    public UUID getBossID() {
        return this.bossID;
    }

    public boolean isRemove() {
        return this.remove;
    }

    public Identifier getRegistryName() {
        return this.registryName;
    }
}

package com.bobmowzie.mowziesmobs.server.message;

import com.bobmowzie.mowziesmobs.client.ClientProxy;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

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
            this.registryName = ForgeRegistries.ENTITY_TYPES.getKey(entity.getType());
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

    public static class Handler implements BiConsumer<MessageUpdateBossBar, Supplier<NetworkEvent.Context>> {
        @Override
        public void accept(final MessageUpdateBossBar message, final Supplier<NetworkEvent.Context> contextSupplier) {
            final NetworkEvent.Context context = contextSupplier.get();
            context.enqueueWork(() -> {
                if (message.registryName == null) {
                    ClientProxy.bossBarRegistryNames.remove(message.bossID);
                } else {
                    ClientProxy.bossBarRegistryNames.put(message.bossID, message.registryName);
                }
            });
            context.setPacketHandled(true);
        }
    }
}

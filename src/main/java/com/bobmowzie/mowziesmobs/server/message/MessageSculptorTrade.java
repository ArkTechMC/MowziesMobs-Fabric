package com.bobmowzie.mowziesmobs.server.message;

import com.bobmowzie.mowziesmobs.server.entity.sculptor.EntitySculptor;
import com.bobmowzie.mowziesmobs.server.inventory.ContainerSculptorTrade;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * Created by BobMowzie on 11/14/2016.
 */
public class MessageSculptorTrade {
    private int entityID;

    public MessageSculptorTrade() {

    }

    public MessageSculptorTrade(LivingEntity sender) {
        this.entityID = sender.getId();
    }

    public static void serialize(final MessageSculptorTrade message, final PacketByteBuf buf) {
        buf.writeVarInt(message.entityID);
    }

    public static MessageSculptorTrade deserialize(final PacketByteBuf buf) {
        final MessageSculptorTrade message = new MessageSculptorTrade();
        message.entityID = buf.readVarInt();
        return message;
    }

    public static class Handler implements BiConsumer<MessageSculptorTrade, Supplier<NetworkEvent.Context>> {
        @Override
        public void accept(final MessageSculptorTrade message, final Supplier<NetworkEvent.Context> contextSupplier) {
            final NetworkEvent.Context context = contextSupplier.get();
            final ServerPlayerEntity player = context.getSender();
            context.enqueueWork(() -> {
                if (player != null) {
                    Entity entity = player.level().getEntity(message.entityID);
                    if (!(entity instanceof EntitySculptor sculptor)) {
                        return;
                    }
                    if (sculptor.getCustomer() != player) {
                        return;
                    }
                    AbstractContainerMenu container = player.containerMenu;
                    if (!(container instanceof ContainerSculptorTrade)) {
                        return;
                    }
                    if (sculptor.checkTestObstructed()) {
                        return;
                    }
                    boolean satisfied = sculptor.fulfillDesire(container.getSlot(0));
                    if (satisfied) {
                        ((ContainerSculptorTrade) container).returnItems();
                        container.broadcastChanges();
                        sculptor.setTestingPlayer(player);
                    }
                }
            });
            context.setPacketHandled(true);
        }
    }
}

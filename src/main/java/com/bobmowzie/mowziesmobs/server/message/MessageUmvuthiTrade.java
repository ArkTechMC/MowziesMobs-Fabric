package com.bobmowzie.mowziesmobs.server.message;

import com.bobmowzie.mowziesmobs.server.entity.umvuthana.EntityUmvuthi;
import com.bobmowzie.mowziesmobs.server.inventory.ContainerUmvuthiTrade;
import com.bobmowzie.mowziesmobs.server.potion.EffectHandler;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * Created by BobMowzie on 11/14/2016.
 */
public class MessageUmvuthiTrade {
    private int entityID;

    public MessageUmvuthiTrade() {

    }

    public MessageUmvuthiTrade(LivingEntity sender) {
        this.entityID = sender.getId();
    }

    public static void serialize(final MessageUmvuthiTrade message, final PacketByteBuf buf) {
        buf.writeVarInt(message.entityID);
    }

    public static MessageUmvuthiTrade deserialize(final PacketByteBuf buf) {
        final MessageUmvuthiTrade message = new MessageUmvuthiTrade();
        message.entityID = buf.readVarInt();
        return message;
    }

    public static class Handler implements BiConsumer<MessageUmvuthiTrade, Supplier<NetworkEvent.Context>> {
        @Override
        public void accept(final MessageUmvuthiTrade message, final Supplier<NetworkEvent.Context> contextSupplier) {
            final NetworkEvent.Context context = contextSupplier.get();
            final ServerPlayerEntity player = context.getSender();
            context.enqueueWork(() -> {
                if (player != null) {
                    Entity entity = player.level().getEntity(message.entityID);
                    if (!(entity instanceof EntityUmvuthi umvuthi)) {
                        return;
                    }
                    if (umvuthi.getCustomer() != player) {
                        return;
                    }
                    AbstractContainerMenu container = player.containerMenu;
                    if (!(container instanceof ContainerUmvuthiTrade)) {
                        return;
                    }
                    boolean satisfied = umvuthi.hasTradedWith(player);
                    if (!satisfied) {
                        if (satisfied = umvuthi.fulfillDesire(container.getSlot(0))) {
                            umvuthi.rememberTrade(player);
                            ((ContainerUmvuthiTrade) container).returnItems();
                            container.broadcastChanges();
                        }
                    }
                    if (satisfied) {
                        player.addEffect(new MobEffectInstance(EffectHandler.SUNS_BLESSING.get(), -1, 0, false, false));
                        if (umvuthi.getActiveAbilityType() != EntityUmvuthi.BLESS_ABILITY) {
                            umvuthi.sendAbilityMessage(EntityUmvuthi.BLESS_ABILITY);
                            umvuthi.playSound(MMSounds.ENTITY_UMVUTHI_BLESS.get(), 2, 1);
                        }
                    }
                }
            });
            context.setPacketHandled(true);
        }
    }
}

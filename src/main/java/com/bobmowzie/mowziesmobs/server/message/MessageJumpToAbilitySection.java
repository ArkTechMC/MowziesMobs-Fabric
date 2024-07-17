package com.bobmowzie.mowziesmobs.server.message;

import com.bobmowzie.mowziesmobs.server.ability.Ability;
import com.bobmowzie.mowziesmobs.server.ability.AbilityType;
import com.bobmowzie.mowziesmobs.server.capability.AbilityCapability;
import com.bobmowzie.mowziesmobs.server.capability.CapabilityHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

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

    public static class Handler implements BiConsumer<MessageJumpToAbilitySection, Supplier<NetworkEvent.Context>> {
        @Override
        public void accept(final MessageJumpToAbilitySection message, final Supplier<NetworkEvent.Context> contextSupplier) {
            final NetworkEvent.Context context = contextSupplier.get();
            context.enqueueWork(() -> {
                LivingEntity entity = (LivingEntity) Minecraft.getInstance().level.getEntity(message.entityID);
                if (entity != null) {
                    AbilityCapability.IAbilityCapability abilityCapability = CapabilityHandler.getCapability(entity, CapabilityHandler.ABILITY_CAPABILITY);
                    if (abilityCapability != null) {
                        AbilityType<?, ?> abilityType = abilityCapability.getAbilityTypesOnEntity(entity)[message.index];
                        Ability instance = abilityCapability.getAbilityMap().get(abilityType);
                        if (instance.isUsing()) instance.jumpToSection(message.sectionIndex);
                    }
                }
            });
            context.setPacketHandled(true);
        }
    }
}

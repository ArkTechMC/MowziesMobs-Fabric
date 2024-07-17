package com.bobmowzie.mowziesmobs.server.message;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.block.BlockGrottol;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public final class MessageBlackPinkInYourArea {
    private int entityID;

    public MessageBlackPinkInYourArea() {
    }

    public MessageBlackPinkInYourArea(AbstractMinecartEntity minecart) {
        this(minecart.getId());
    }

    private MessageBlackPinkInYourArea(int entityId) {
        this.entityID = entityId;
    }

    public static void serialize(final MessageBlackPinkInYourArea message, final PacketByteBuf buf) {
        buf.writeVarInt(message.entityID);
    }

    public static MessageBlackPinkInYourArea deserialize(final PacketByteBuf buf) {
        final MessageBlackPinkInYourArea message = new MessageBlackPinkInYourArea();
        message.entityID = buf.readVarInt();
        return message;
    }

    public static class Handler implements BiConsumer<MessageBlackPinkInYourArea, Supplier<NetworkEvent.Context>> {
        @Override
        public void accept(final MessageBlackPinkInYourArea message, final Supplier<NetworkEvent.Context> contextSupplier) {
            final NetworkEvent.Context context = contextSupplier.get();
            context.enqueueWork(() -> {
                ClientLevel world = Minecraft.getInstance().level;
                Entity entity = world.getEntity(message.entityID);
                if (entity instanceof AbstractMinecart minecart) {
                    MowziesMobs.PROXY.playBlackPinkSound(minecart);
                    BlockState state = Blocks.STONE.defaultBlockState()
                            .setValue(BlockGrottol.VARIANT, BlockGrottol.Variant.BLACK_PINK);
                    BlockPos pos = minecart.blockPosition();
                    final float scale = 0.75F;
                    double x = minecart.getX(),
                            y = minecart.getY() + 0.375F + 0.5F + (minecart.getDefaultDisplayOffset() - 8) / 16.0F * scale,
                            z = minecart.getZ();
                    SoundType sound = state.getBlock().getSoundType(state, world, pos, minecart);
                    world.playLocalSound(
                            x, y, z,
                            sound.getBreakSound(),
                            minecart.getSoundSource(),
                            (sound.getVolume() + 1.0F) / 2.0F,
                            sound.getPitch() * 0.8F,
                            false
                    );
                    MowziesMobs.PROXY.minecartParticles(world, minecart, scale, x, y, z, state, pos);
                }
            });
            context.setPacketHandled(true);
        }
    }
}

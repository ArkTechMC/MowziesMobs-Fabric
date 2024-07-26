package com.bobmowzie.mowziesmobs.server.entity.grottol;

import com.bobmowzie.mowziesmobs.server.message.MessageBlackPinkInYourArea;
import com.bobmowzie.mowziesmobs.server.message.StaticVariables;
import com.iafenvoy.uranus.ServerHelper;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.world.World;

import java.util.function.BiConsumer;

public final class BlackPinkInYourArea implements BiConsumer<World, AbstractMinecartEntity> {
    private BlackPinkInYourArea() {
    }

    public static BlackPinkInYourArea create() {
        return new BlackPinkInYourArea();
    }

    @Override
    public void accept(World world, AbstractMinecartEntity minecart) {
        /*BlockState state = minecart.getDisplayTile();
        if (state.getBlock() != BlockHandler.GROTTOL.get()) {
            state = BlockHandler.GROTTOL.get().getDefaultState();
            minecart.setDisplayTileOffset(minecart.getDefaultDisplayTileOffset());
        }
        minecart.setDisplayTile(state.with(BlockGrottol.VARIANT, BlockGrottol.Variant.BLACK_PINK));*/
        PacketByteBuf buf = PacketByteBufs.create();
        MessageBlackPinkInYourArea.serialize(new MessageBlackPinkInYourArea(minecart), buf);
        ServerHelper.sendToAll(StaticVariables.BLACK_PINK_IN_YOUR_AREA, buf);
    }
}

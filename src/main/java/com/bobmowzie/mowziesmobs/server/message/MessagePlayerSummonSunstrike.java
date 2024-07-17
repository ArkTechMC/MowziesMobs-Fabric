package com.bobmowzie.mowziesmobs.server.message;

import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntitySunstrike;
import com.bobmowzie.mowziesmobs.server.potion.EffectHandler;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class MessagePlayerSummonSunstrike {
    private static final double REACH = 15;

    public MessagePlayerSummonSunstrike() {

    }

    private static BlockHitResult rayTrace(LivingEntity entity, double reach) {
        Vec3d pos = entity.getCameraPosVec(0);
        Vec3d segment = entity.getRotationVector();
        segment = pos.add(segment.x * reach, segment.y * reach, segment.z * reach);
        return entity.getWorld().raycast(new RaycastContext(pos, segment, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, entity));
    }

    public static void serialize(final MessagePlayerSummonSunstrike message, final PacketByteBuf buf) {
    }

    public static MessagePlayerSummonSunstrike deserialize(final PacketByteBuf buf) {
        final MessagePlayerSummonSunstrike message = new MessagePlayerSummonSunstrike();
        return message;
    }

    public static class Handler implements BiConsumer<MessagePlayerSummonSunstrike, Supplier<NetworkEvent.Context>> {
        @Override
        public void accept(final MessagePlayerSummonSunstrike message, final Supplier<NetworkEvent.Context> contextSupplier) {
            final NetworkEvent.Context context = contextSupplier.get();
            final ServerPlayerEntity player = context.getSender();
            context.enqueueWork(() -> {
                BlockHitResult raytrace = rayTrace(player, REACH);
                if (raytrace.getType() == HitResult.Type.BLOCK && raytrace.getDirection() == Direction.UP && player.getInventory().getSelected().isEmpty() && player.hasEffect(EffectHandler.SUNS_BLESSING.get())) {
                    BlockPos hit = raytrace.getBlockPos();
                    EntitySunstrike sunstrike = new EntitySunstrike(EntityType.get(), player.level(), player, hit.getX(), hit.getY(), hit.getZ());
                    sunstrike.onSummon();
                    player.level().addFreshEntity(sunstrike);
                }
            });
            context.setPacketHandled(true);
        }
    }
}

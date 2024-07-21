package com.bobmowzie.mowziesmobs.server.entity;

import com.bobmowzie.mowziesmobs.server.message.MessageUpdateBossBar;
import com.bobmowzie.mowziesmobs.server.message.StaticVariables;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class MMBossInfoServer extends ServerBossBar {
    private final MowzieEntity entity;

    private final Set<ServerPlayerEntity> unseen = new HashSet<>();

    public MMBossInfoServer(MowzieEntity entity) {
        super(entity.getDisplayName(), entity.bossBarColor(), Style.PROGRESS);
        this.setVisible(entity.hasBossBar());
        this.entity = entity;
    }

    public void update() {
        this.setPercent(this.entity.getHealth() / this.entity.getMaxHealth());
        Iterator<ServerPlayerEntity> it = this.unseen.iterator();
        while (it.hasNext()) {
            ServerPlayerEntity player = it.next();
            if (this.entity.getVisibilityCache().canSee(player)) {
                super.addPlayer(player);
                it.remove();
            }
        }
    }

    @Override
    public void addPlayer(ServerPlayerEntity player) {
        PacketByteBuf buf = PacketByteBufs.create();
        MessageUpdateBossBar.serialize(new MessageUpdateBossBar(this.getUuid(), this.entity), buf);
        ServerPlayNetworking.send(player, StaticVariables.UPDATE_BOSS_BAR, buf);
        if (this.entity.getVisibilityCache().canSee(player)) {
            super.addPlayer(player);
        } else {
            this.unseen.add(player);
        }
    }

    @Override
    public void removePlayer(ServerPlayerEntity player) {
        PacketByteBuf buf = PacketByteBufs.create();
        MessageUpdateBossBar.serialize(new MessageUpdateBossBar(this.getUuid(), null), buf);
        ServerPlayNetworking.send(player, StaticVariables.UPDATE_BOSS_BAR, buf);
        super.removePlayer(player);
        this.unseen.remove(player);
    }
}

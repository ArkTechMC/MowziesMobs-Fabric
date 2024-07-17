package com.bobmowzie.mowziesmobs.server.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.message.MessageUpdateBossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraftforge.network.NetworkDirection;

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
        MowziesMobs.NETWORK.sendTo(new MessageUpdateBossBar(this.getUuid(), this.entity), player.networkHandler.connection, NetworkDirection.PLAY_TO_CLIENT);
        if (this.entity.getVisibilityCache().canSee(player)) {
            super.addPlayer(player);
        } else {
            this.unseen.add(player);
        }
    }

    @Override
    public void removePlayer(ServerPlayerEntity player) {
        MowziesMobs.NETWORK.sendTo(new MessageUpdateBossBar(this.getUuid(), null), player.networkHandler.connection, NetworkDirection.PLAY_TO_CLIENT);
        super.removePlayer(player);
        this.unseen.remove(player);
    }
}

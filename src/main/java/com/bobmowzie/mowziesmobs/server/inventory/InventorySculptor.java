package com.bobmowzie.mowziesmobs.server.inventory;

import com.bobmowzie.mowziesmobs.server.entity.sculptor.EntitySculptor;
import net.minecraft.entity.player.PlayerEntity;

public final class InventorySculptor extends InventoryOneInput {
    private final EntitySculptor sculptor;

    public InventorySculptor(EntitySculptor sculptor) {
        super(sculptor);
        this.sculptor = sculptor;
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return this.sculptor.getCustomer() == player;
    }
}

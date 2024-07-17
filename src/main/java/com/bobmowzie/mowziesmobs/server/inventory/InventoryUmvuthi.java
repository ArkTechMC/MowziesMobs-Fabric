package com.bobmowzie.mowziesmobs.server.inventory;

import com.bobmowzie.mowziesmobs.server.entity.umvuthana.EntityUmvuthi;
import net.minecraft.entity.player.PlayerEntity;

public final class InventoryUmvuthi extends InventoryOneInput {
    private final EntityUmvuthi umvuthi;

    public InventoryUmvuthi(EntityUmvuthi umvuthi) {
        super(umvuthi);
        this.umvuthi = umvuthi;
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return this.umvuthi.getCustomer() == player;
    }
}

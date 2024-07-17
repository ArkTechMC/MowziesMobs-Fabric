package com.bobmowzie.mowziesmobs.server.inventory;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.entity.umvuthana.EntityUmvuthi;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.slot.Slot;

public final class ContainerUmvuthiTrade extends ContainerTradeBase {
    private final EntityUmvuthi barako;
    private final InventoryUmvuthi inventoryUmvuthi;

    public ContainerUmvuthiTrade(int id, PlayerInventory playerInventory) {
        this(id, (EntityUmvuthi) MowziesMobs.PROXY.getReferencedMob(), playerInventory);
    }

    public ContainerUmvuthiTrade(int id, EntityUmvuthi barako, PlayerInventory playerInv) {
        this(id, barako, new InventoryUmvuthi(barako), playerInv);
    }

    public ContainerUmvuthiTrade(int id, EntityUmvuthi barako, InventoryUmvuthi inventory, PlayerInventory playerInv) {
        super(ContainerHandler.CONTAINER_UMVUTHI_TRADE, id, barako, inventory, playerInv);
        this.barako = barako;
        this.inventoryUmvuthi = inventory;
    }

    @Override
    protected void addCustomSlots(PlayerInventory playerInv) {
        EntityUmvuthi barako = (EntityUmvuthi) this.getTradingMob();
        InventoryUmvuthi inventoryUmvuthi = (InventoryUmvuthi) this.inventory;
        if (barako != null && !barako.hasTradedWith(playerInv.player)) this.addSlot(new Slot(inventoryUmvuthi, 0, 69, 54));
    }

    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        if (this.barako != null) this.barako.setCustomer(null);
    }

    public EntityUmvuthi getUmvuthi() {
        return this.barako;
    }

    public InventoryUmvuthi getInventoryUmvuthi() {
        return this.inventoryUmvuthi;
    }
}

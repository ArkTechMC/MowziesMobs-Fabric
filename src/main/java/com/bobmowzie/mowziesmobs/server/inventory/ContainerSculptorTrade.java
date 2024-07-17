package com.bobmowzie.mowziesmobs.server.inventory;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.entity.sculptor.EntitySculptor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.slot.Slot;

public final class ContainerSculptorTrade extends ContainerTradeBase {
    private final EntitySculptor sculptor;
    private final InventorySculptor inventorySculptor;

    public ContainerSculptorTrade(int id, PlayerInventory playerInventory) {
        this(id, (EntitySculptor) MowziesMobs.PROXY.getReferencedMob(), playerInventory);
    }

    public ContainerSculptorTrade(int id, EntitySculptor sculptor, PlayerInventory playerInv) {
        this(id, sculptor, new InventorySculptor(sculptor), playerInv);
    }

    public ContainerSculptorTrade(int id, EntitySculptor sculptor, InventorySculptor inventory, PlayerInventory playerInv) {
        super(ContainerHandler.CONTAINER_SCULPTOR_TRADE, id, sculptor, inventory, playerInv);
        this.sculptor = sculptor;
        this.inventorySculptor = inventory;
    }

    @Override
    protected void addCustomSlots(PlayerInventory playerInv) {
        this.addSlot(new Slot(this.inventory, 0, 69, 54));
    }

    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        if (this.sculptor != null) this.sculptor.setCustomer(null);
    }

    public EntitySculptor getSculptor() {
        return this.sculptor;
    }

    public InventorySculptor getInventorySculptor() {
        return this.inventorySculptor;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return super.canUse(player) && !this.sculptor.isTesting();
    }
}

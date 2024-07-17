package com.bobmowzie.mowziesmobs.server.inventory;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.entity.umvuthana.EntityUmvuthanaMinion;
import com.bobmowzie.mowziesmobs.server.entity.umvuthana.trade.Trade;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public final class ContainerUmvuthanaTrade extends ContainerTradeBase {
    private final EntityUmvuthanaMinion umvuthanaMinion;
    private final InventoryUmvuthana inventoryUmvuthana;

    public ContainerUmvuthanaTrade(int id, PlayerInventory playerInventory) {
        this(id, (EntityUmvuthanaMinion) MowziesMobs.PROXY.getReferencedMob(), playerInventory);
    }

    public ContainerUmvuthanaTrade(int id, EntityUmvuthanaMinion barakoaya, PlayerInventory playerInv) {
        this(id, barakoaya, new InventoryUmvuthana(barakoaya), playerInv);
    }

    public ContainerUmvuthanaTrade(int id, EntityUmvuthanaMinion umvuthanaMinion, InventoryUmvuthana inventory, PlayerInventory playerInv) {
        super(ContainerHandler.CONTAINER_UMVUTHANA_TRADE, id, umvuthanaMinion, inventory, playerInv);
        this.inventoryUmvuthana = inventory;
        this.umvuthanaMinion = umvuthanaMinion;
    }

    @Override
    protected void addCustomSlots(PlayerInventory playerInv) {
        this.addSlot(new Slot(this.getInventory(), 0, 80, 54));
        this.addSlot(new SlotResult(this.getInventory(), 1, 133, 54));
    }

    @Override
    public void onContentChanged(Inventory inv) {
        this.inventoryUmvuthana.reset();
        super.onContentChanged(inv);
    }

    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        if (this.umvuthanaMinion != null) this.umvuthanaMinion.setCustomer(null);
    }

    public EntityUmvuthanaMinion getUmvuthana() {
        return this.umvuthanaMinion;
    }

    public InventoryUmvuthana getInventoryUmvuthana() {
        return this.inventoryUmvuthana;
    }

    private class SlotResult extends Slot {
        private int removeCount;

        public SlotResult(Inventory inventory, int index, int x, int y) {
            super(inventory, index, x, y);
        }

        @Override
        public boolean canInsert(ItemStack stack) {
            return false;
        }

        @Override
        public ItemStack takeStack(int amount) {
            if (this.hasStack()) {
                this.removeCount += Math.min(amount, this.getStack().getCount());
            }
            return super.takeStack(amount);
        }

        @Override
        protected void onCrafted(ItemStack stack, int amount) {
            this.removeCount += amount;
            super.onCrafted(stack, amount);
        }

        @Override
        protected void onCrafted(ItemStack stack) {
            stack.onCraft(ContainerUmvuthanaTrade.this.umvuthanaMinion.getWorld(), ContainerUmvuthanaTrade.this.player, this.removeCount);
            this.removeCount = 0;
        }

        @Override
        public ItemStack takeStackRange(int p_150648_, int p_150649_, PlayerEntity p_150650_) {
            return super.takeStackRange(p_150648_, p_150649_, p_150650_);
        }

        @Override
        public void onTakeItem(PlayerEntity player, ItemStack stack) {
            this.onCrafted(stack);
            if (ContainerUmvuthanaTrade.this.umvuthanaMinion != null && ContainerUmvuthanaTrade.this.umvuthanaMinion.isOfferingTrade()) {
                Trade trade = ContainerUmvuthanaTrade.this.umvuthanaMinion.getOfferingTrade();
                ItemStack input = this.inventory.getStack(0);
                ItemStack tradeInput = trade.getInput();
                if (input.getItem() == tradeInput.getItem() && input.getCount() >= tradeInput.getCount()) {
                    input.decrement(tradeInput.getCount());
                    if (input.getCount() <= 0) {
                        input = ItemStack.EMPTY;
                    }
                    this.inventory.setStack(0, input);
                }
            }
        }
    }
}

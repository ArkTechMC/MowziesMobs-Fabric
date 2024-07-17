package com.bobmowzie.mowziesmobs.server.inventory;

import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;

public abstract class ContainerTradeBase extends ScreenHandler {
    protected final MowzieEntity tradingMob;
    protected final Inventory inventory;
    protected final PlayerEntity player;
    private final int numCustomSlots;

    public ContainerTradeBase(ScreenHandlerType<?> menuType, int id, MowzieEntity tradingMob, Inventory inventory, PlayerInventory playerInv) {
        super(menuType, id);
        this.tradingMob = tradingMob;
        this.inventory = inventory;
        this.player = playerInv.player;
        this.addCustomSlots(playerInv);
        this.numCustomSlots = this.slots.size();
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new Slot(playerInv, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }
        for (int col = 0; col < 9; col++) {
            this.addSlot(new Slot(playerInv, col, 8 + col * 18, 142));
        }
    }

    protected void addCustomSlots(PlayerInventory playerInv) {

    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.tradingMob != null && this.inventory.canPlayerUse(player) && this.tradingMob.isAlive() && this.tradingMob.distanceTo(player) < 8;
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int index) {
        ItemStack stack = ItemStack.EMPTY;
        int playerHotbarStart = this.numCustomSlots + 27;
        int playerInventoryEnd = this.numCustomSlots + 36;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasStack()) {
            ItemStack contained = slot.getStack();
            stack = contained.copy();
            if (index == 1) {
                if (!this.insertItem(contained, this.numCustomSlots, playerInventoryEnd, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickTransfer(contained, stack);
            } else if (index != 0) {
                if (index >= this.numCustomSlots && index < playerHotbarStart) {
                    if (!this.insertItem(contained, playerHotbarStart, playerInventoryEnd, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= playerHotbarStart && index < playerInventoryEnd && !this.insertItem(contained, this.numCustomSlots, playerHotbarStart, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(contained, this.numCustomSlots, playerInventoryEnd, false)) {
                return ItemStack.EMPTY;
            }
            if (contained.getCount() == 0) {
                slot.setStackNoCallbacks(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
            if (contained.getCount() == stack.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTakeItem(player, contained);
        }
        return stack;
    }

    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        this.returnItems();
    }

    public void returnItems() {
        if (!this.player.getWorld().isClient) {
            ItemStack stack = this.inventory.removeStack(0);
            if (stack != ItemStack.EMPTY) {
                this.player.getInventory().offerOrDrop(stack);
            }
        }
    }

    public MowzieEntity getTradingMob() {
        return this.tradingMob;
    }

    public Inventory getInventory() {
        return this.inventory;
    }
}
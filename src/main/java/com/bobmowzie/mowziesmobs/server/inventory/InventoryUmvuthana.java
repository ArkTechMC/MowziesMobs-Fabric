package com.bobmowzie.mowziesmobs.server.inventory;

import com.bobmowzie.mowziesmobs.server.entity.umvuthana.EntityUmvuthanaMinion;
import com.bobmowzie.mowziesmobs.server.entity.umvuthana.trade.Trade;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.util.collection.DefaultedList;

import java.util.List;

public final class InventoryUmvuthana implements Inventory {
    private final EntityUmvuthanaMinion umvuthana;

    private final List<ItemStack> slots = DefaultedList.ofSize(2, ItemStack.EMPTY);

    private Trade trade;

    public InventoryUmvuthana(EntityUmvuthanaMinion umvuthana) {
        this.umvuthana = umvuthana;
    }

    private static boolean areItemsEqual(ItemStack s1, ItemStack s2) {
        return ItemStack.areEqual(s1, s2) && (!s2.hasNbt() || s1.hasNbt() && NbtHelper.matches(s2.getNbt(), s1.getNbt(), false));
    }

    @Override
    public int size() {
        return this.slots.size();
    }

    @Override
    public ItemStack getStack(int index) {
        return this.slots.get(index);
    }

    @Override
    public ItemStack removeStack(int index, int count) {
        if (index == 1 && this.slots.get(index) != ItemStack.EMPTY) {
            return Inventories.splitStack(this.slots, index, this.slots.get(index).getCount());
        }
        ItemStack stack = Inventories.splitStack(this.slots, index, count);
        if (stack != ItemStack.EMPTY && this.doUpdateForSlotChange(index)) {
            this.reset();
        }
        return stack;
    }

    @Override
    public ItemStack removeStack(int index) {
        return Inventories.removeStack(this.slots, index);
    }

    @Override
    public void setStack(int index, ItemStack stack) {
        this.slots.set(index, stack);
        if (stack != ItemStack.EMPTY && stack.getCount() > this.getMaxCountPerStack()) {
            stack.setCount(this.getMaxCountPerStack());
        }
        if (this.doUpdateForSlotChange(index)) {
            this.reset();
        }
    }

    private boolean doUpdateForSlotChange(int slot) {
        return slot == 0;
    }

    @Override
    public int getMaxCountPerStack() {
        return 64;
    }

    @Override
    public void markDirty() {
        this.reset();
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return this.umvuthana.getCustomer() == player;
    }

    @Override
    public void onOpen(PlayerEntity player) {
    }

    @Override
    public void onClose(PlayerEntity player) {
    }

    @Override
    public boolean isValid(int index, ItemStack stack) {
        return true;
    }

    @Override
    public void clear() {
        this.slots.clear(); // NonNullList.clear fills with default value
    }

    public void reset() {
        this.trade = null;
        ItemStack input = this.slots.get(0);
        if (input == ItemStack.EMPTY) {
            this.setStack(1, ItemStack.EMPTY);
        } else if (this.umvuthana.isOfferingTrade()) {
            Trade trade = this.umvuthana.getOfferingTrade();
            ItemStack tradeInput = trade.getInput();
            if (areItemsEqual(input, tradeInput) && input.getCount() >= tradeInput.getCount()) {
                this.trade = trade;
                this.setStack(1, trade.getOutput());
            } else {
                this.setStack(1, ItemStack.EMPTY);
            }
        }
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : this.slots) {
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }
}

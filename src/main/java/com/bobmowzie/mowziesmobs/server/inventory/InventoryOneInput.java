package com.bobmowzie.mowziesmobs.server.inventory;

import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public abstract class InventoryOneInput implements Inventory {
    protected final MowzieEntity tradingEntity;
    protected ItemStack input = ItemStack.EMPTY;
    protected List<ChangeListener> listeners;

    public InventoryOneInput(MowzieEntity tradingEntity) {
        this.tradingEntity = tradingEntity;
    }

    public void addListener(ChangeListener listener) {
        if (this.listeners == null) {
            this.listeners = new ArrayList<>();
        }
        this.listeners.add(listener);
    }

    @Override
    public int size() {
        return 1;
    }

    @Override
    public void onOpen(PlayerEntity player) {
    }

    @Override
    public void onClose(PlayerEntity player) {
    }

    @Override
    public ItemStack getStack(int index) {
        return index == 0 ? this.input : ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeStack(int index, int count) {
        ItemStack stack;
        if (index == 0 && this.input != ItemStack.EMPTY && count > 0) {
            ItemStack split = this.input.split(count);
            if (this.input.getCount() == 0) {
                this.input = ItemStack.EMPTY;
            }
            stack = split;
            this.markDirty();
        } else {
            stack = ItemStack.EMPTY;
        }
        return stack;
    }

    @Override
    public ItemStack removeStack(int index) {
        if (index != 0) {
            return ItemStack.EMPTY;
        }
        ItemStack s = this.input;
        this.input = ItemStack.EMPTY;
        this.markDirty();
        return s;
    }

    @Override
    public void setStack(int index, ItemStack stack) {
        if (index == 0) {
            this.input = stack;
            if (stack != ItemStack.EMPTY && stack.getCount() > this.getMaxCountPerStack()) {
                stack.setCount(this.getMaxCountPerStack());
            }
            this.markDirty();
        }
    }

    @Override
    public boolean isValid(int index, ItemStack stack) {
        return true;
    }

    @Override
    public void clear() {
        this.input = ItemStack.EMPTY;
        this.markDirty();
    }

    @Override
    public boolean isEmpty() {
        return !this.input.isEmpty();
    }

    @Override
    public int getMaxCountPerStack() {
        return 64;
    }

    @Override
    public void markDirty() {
        if (this.listeners != null) {
            for (ChangeListener listener : this.listeners) {
                listener.onChange(this);
            }
        }
    }

    public interface ChangeListener {
        void onChange(Inventory inv);
    }
}

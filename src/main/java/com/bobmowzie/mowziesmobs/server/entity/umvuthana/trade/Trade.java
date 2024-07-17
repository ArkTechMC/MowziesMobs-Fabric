package com.bobmowzie.mowziesmobs.server.entity.umvuthana.trade;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

public final class Trade {
    private final ItemStack input;

    private final ItemStack output;

    private final int weight;

    public Trade(ItemStack input, ItemStack output, int weight) {
        this.input = input.copy();
        this.output = output.copy();
        this.weight = weight;
    }

    public Trade(Trade trade) {
        this(trade.input, trade.output, trade.weight);
    }

    public static Trade deserialize(NbtCompound compound) {
        ItemStack input = ItemStack.fromNbt(compound.getCompound("input"));
        ItemStack output = ItemStack.fromNbt(compound.getCompound("output"));
        int weight = compound.getInt("weight");
        if (input.isEmpty() || output.isEmpty() || weight < 1) {
            return null;
        }
        return new Trade(input, output, weight);
    }

    public ItemStack getInput() {
        return this.input.copy();
    }

    public ItemStack getOutput() {
        return this.output.copy();
    }

    public int getWeight() {
        return this.weight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof Trade trade) {
            return this.weight == trade.weight && ItemStack.areEqual(this.input, trade.input) && ItemStack.areEqual(this.output, trade.output);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return 961 * this.input.hashCode() + 31 * this.output.hashCode() + this.weight;
    }

    public NbtCompound serialize() {
        NbtCompound compound = new NbtCompound();
        compound.put("input", this.input.writeNbt(new NbtCompound()));
        compound.put("output", this.output.writeNbt(new NbtCompound()));
        compound.putInt("weight", this.weight);
        return compound;
    }
}

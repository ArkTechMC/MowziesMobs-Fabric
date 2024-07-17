package com.bobmowzie.mowziesmobs.server.entity.umvuthana.trade;

import com.google.common.collect.ImmutableSet;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.math.random.Random;

public final class TradeStore {
    public static final TradeStore EMPTY = new TradeStore(ImmutableSet.of(), 0);

    private final ImmutableSet<Trade> trades;

    private final int totalWeight;

    private TradeStore(ImmutableSet<Trade> trades, int totalWeight) {
        this.trades = trades;
        this.totalWeight = totalWeight;
    }

    public static TradeStore deserialize(NbtCompound compound) {
        NbtList tradesList = compound.getList("trades", NbtElement.COMPOUND_TYPE);
        int totalWeight = 0;
        ImmutableSet.Builder<Trade> trades = new ImmutableSet.Builder<>();
        for (int i = 0; i < tradesList.size(); i++) {
            Trade trade = Trade.deserialize(tradesList.getCompound(i));
            if (trade != null) {
                trades.add(trade);
                totalWeight += trade.getWeight();
            }
        }
        return new TradeStore(trades.build(), totalWeight);
    }

    public boolean hasStock() {
        return this.trades.size() > 0;
    }

    public Trade get(Random rng) {
        if (this.totalWeight <= 0) {
            return null;
        }
        int w = rng.nextInt(this.totalWeight);
        for (Trade t : this.trades) {
            w -= t.getWeight();
            if (w < 0) {
                return t;
            }
        }
        return null;
    }

    public NbtCompound serialize() {
        NbtCompound compound = new NbtCompound();
        NbtList tradesList = new NbtList();
        for (Trade trade : this.trades) {
            tradesList.add(trade.serialize());
        }
        compound.put("trades", tradesList);
        return compound;
    }

    public static final class Builder {
        private final ImmutableSet.Builder<Trade> trades = new ImmutableSet.Builder<>();

        private int totalWeight;

        public Builder addTrade(Item input, int inputCount, Item output, int outputCount, int weight) {
            return this.addTrade(new ItemStack(input, inputCount), new ItemStack(output, outputCount), weight);
        }

        public Builder addTrade(ItemStack input, ItemStack output, int weight) {
            this.trades.add(new Trade(input, output, weight));
            this.totalWeight += weight;
            return this;
        }

        public TradeStore build() {
            return new TradeStore(this.trades.build(), this.totalWeight);
        }
    }
}

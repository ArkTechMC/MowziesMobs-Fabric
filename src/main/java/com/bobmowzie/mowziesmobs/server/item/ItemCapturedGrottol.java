package com.bobmowzie.mowziesmobs.server.item;

import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.grottol.EntityGrottol;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.control.LookControl;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;

import java.util.UUID;

public class ItemCapturedGrottol extends Item {
    public ItemCapturedGrottol(Settings properties) {
        super(properties.maxCount(1));
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        PlayerEntity player = context.getPlayer();
        BlockPos pos = context.getBlockPos();
        Direction facing = context.getSide();
        Hand hand = context.getHand();
        World world = context.getWorld();
        if (context.getSide() == Direction.DOWN) {
            return ActionResult.FAIL;
        }
        BlockPos location = pos.offset(facing);
        ItemStack stack = player.getStackInHand(hand);
        if (!player.canPlaceOn(location, facing, stack)) {
            return ActionResult.FAIL;
        }
        if (!world.isClient) {
            EntityGrottol grottol = new EntityGrottol(EntityHandler.GROTTOL, world);
            NbtCompound compound = stack.getSubNbt("EntityTag");
            if (compound != null) {
                this.setData(grottol, compound);
            }
            grottol.refreshPositionAndAngles(location, 0, 0);
            this.lookAtPlayer(grottol, player);
            grottol.initialize((ServerWorldAccess) world, world.getLocalDifficulty(location), SpawnReason.MOB_SUMMONED, null, null);
            world.spawnEntity(grottol);
            if (!player.getAbilities().creativeMode) {
                stack.decrement(1);
            }
        }
        return ActionResult.SUCCESS;
    }

    private void setData(EntityGrottol grottol, NbtCompound compound) {
        NbtCompound data = new NbtCompound();
        grottol.writeNbt(data);
        UUID id = grottol.getUuid();
        data.copyFrom(compound);
        grottol.readNbt(data);
        grottol.setUuid(id);
    }

    private void lookAtPlayer(EntityGrottol grottol, PlayerEntity player) {
        LookControl helper = new LookControl(grottol);
        helper.lookAt(player, 180, 90);
        helper.tick();
        /*GoalSelector ai = grottol.goalSelector;
        Set<GoalSelector.EntityAITaskEntry> tasks = Sets.newLinkedHashSet(ai..taskEntries);
        ai.taskEntries.removeIf(entry -> !(entry.action instanceof LookAtGoal));
        grottol.getRNG().setSeed("IS MATh RElatEd tO ScIENCe?".hashCode());
        ai.onUpdateTasks();
        grottol.getRNG().setSeed(new Random().nextLong());
        ai.taskEntries.clear();
        ai.taskEntries.addAll(tasks);*/
    }

    public ItemStack create(EntityGrottol grottol) {
        NbtCompound compound = new NbtCompound();
        grottol.writeNbt(compound);
        ItemStack stack = new ItemStack(this);
        stack.setSubNbt("EntityTag", compound);
        return stack;
    }
}

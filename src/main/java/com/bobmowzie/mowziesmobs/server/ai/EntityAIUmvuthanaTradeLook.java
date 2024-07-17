package com.bobmowzie.mowziesmobs.server.ai;

import com.bobmowzie.mowziesmobs.server.entity.umvuthana.EntityUmvuthanaMinion;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.player.PlayerEntity;

import java.util.EnumSet;

public class EntityAIUmvuthanaTradeLook extends LookAtEntityGoal {
    private final EntityUmvuthanaMinion umvuthana;

    public EntityAIUmvuthanaTradeLook(EntityUmvuthanaMinion umvuthana) {
        super(umvuthana, PlayerEntity.class, 8);
        this.umvuthana = umvuthana;
        this.setControls(EnumSet.of(Control.LOOK));
    }

    @Override
    public boolean canStart() {
        if (this.umvuthana.isTrading()) {
            this.target = this.umvuthana.getCustomer();
            return true;
        }
        return false;
    }
}

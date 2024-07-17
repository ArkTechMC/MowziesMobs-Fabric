package com.bobmowzie.mowziesmobs.server.ai;

import com.bobmowzie.mowziesmobs.server.entity.umvuthana.EntityUmvuthanaMinion;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;

import java.util.EnumSet;

public final class EntityAIUmvuthanaTrade extends Goal {
    private final EntityUmvuthanaMinion umvuthana;

    public EntityAIUmvuthanaTrade(EntityUmvuthanaMinion umvuthana) {
        this.umvuthana = umvuthana;
        this.setControls(EnumSet.of(Control.MOVE, Control.JUMP, Control.TARGET));
    }

    @Override
    public boolean canStart() {
        if (!this.umvuthana.isAlive() || this.umvuthana.isTouchingWater() || !this.umvuthana.isOnGround() || this.umvuthana.velocityModified) {
            return false;
        } else {
            PlayerEntity plyr = this.umvuthana.getCustomer();
            return plyr != null && this.umvuthana.squaredDistanceTo(plyr) <= 16 && plyr.currentScreenHandler != null;
        }
    }

    @Override
    public void start() {
        this.umvuthana.getNavigation().stop();
    }

    @Override
    public void stop() {
        this.umvuthana.setCustomer(null);
    }
}

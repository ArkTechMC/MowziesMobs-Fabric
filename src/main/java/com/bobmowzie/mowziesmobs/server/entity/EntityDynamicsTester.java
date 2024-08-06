package com.bobmowzie.mowziesmobs.server.entity;

import com.bobmowzie.mowziesmobs.client.model.tools.dynamics.DynamicChain;
import com.iafenvoy.uranus.animation.Animation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.WanderAroundGoal;
import net.minecraft.world.World;

public class EntityDynamicsTester extends MowzieLLibraryEntity {
    @Environment(EnvType.CLIENT)
    public DynamicChain dc;

    public EntityDynamicsTester(World world) {
        //FIXME: What is this?
        super(EntityHandler.UMVUTHI, world);
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(4, new WanderAroundGoal(this, 0.3));
        this.goalSelector.add(8, new LookAroundGoal(this));
    }

    @Override
    public Animation getDeathAnimation() {
        return null;
    }

    @Override
    public Animation getHurtAnimation() {
        return null;
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[0];
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getWorld().isClient) {
            if (this.age == 1) {
                this.dc = new DynamicChain(this);
            }
            this.dc.updateSpringConstraint(0.1f, 0.3f, 0.6f, 1f, true, 0.5f, 1);
            this.bodyYaw = this.getYaw();
        }
    }
}

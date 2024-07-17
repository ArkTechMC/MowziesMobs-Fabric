package com.bobmowzie.mowziesmobs.server.entity.umvuthana;

import com.bobmowzie.mowziesmobs.server.ai.UmvuthanaHurtByTargetAI;
import com.bobmowzie.mowziesmobs.server.entity.LeaderSunstrikeImmune;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.Monster;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;

import java.util.Optional;
import java.util.UUID;

public class EntityUmvuthanaFollowerToRaptor extends EntityUmvuthanaFollower<EntityUmvuthanaRaptor> implements LeaderSunstrikeImmune, Monster {
    public EntityUmvuthanaFollowerToRaptor(EntityType<? extends EntityUmvuthanaFollowerToRaptor> type, World world) {
        this(type, world, null);
    }

    public EntityUmvuthanaFollowerToRaptor(EntityType<? extends EntityUmvuthanaFollowerToRaptor> type, World world, EntityUmvuthanaRaptor leader) {
        super(type, world, EntityUmvuthanaRaptor.class, leader);
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(3, new UmvuthanaHurtByTargetAI(this, true));
    }

    @Override
    public void tick() {
        super.tick();
        if (this.leader != null) {
            this.setTarget(this.leader.getTarget());
        }

        if (!this.getWorld().isClient && this.getWorld().getDifficulty() == Difficulty.PEACEFUL) {
            this.discard();
        }
    }

    @Override
    protected int getGroupCircleTick() {
        if (this.leader == null) return 0;
        return this.leader.circleTick;
    }

    @Override
    protected int getPackSize() {
        if (this.leader == null) return 0;
        return this.leader.getPackSize();
    }

    @Override
    protected void addAsPackMember() {
        if (this.leader == null) return;
        this.leader.addPackMember(this);
    }

    @Override
    protected void removeAsPackMember() {
        if (this.leader == null) return;
        this.leader.removePackMember(this);
    }

    public void removeLeader() {
        this.setLeaderUUID(ABSENT_LEADER);
        this.leader = null;
        this.setTarget(null);
    }

    @Override
    public void setLeaderUUID(Optional<UUID> uuid) {
        super.setLeaderUUID(uuid);
        if (uuid == ABSENT_LEADER) this.registerHuntingTargetGoals();
    }
}

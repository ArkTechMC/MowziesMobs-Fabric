package com.bobmowzie.mowziesmobs.server.entity.effects;

import com.bobmowzie.mowziesmobs.server.entity.ILinkedEntity;
import com.bobmowzie.mowziesmobs.server.message.MessageLinkEntities;
import com.bobmowzie.mowziesmobs.server.message.StaticVariables;
import com.iafenvoy.uranus.ServerHelper;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by BobMowzie on 9/2/2018.
 */
public abstract class EntityMagicEffect extends Entity implements ILinkedEntity {
    private static final TrackedData<Optional<UUID>> CASTER = DataTracker.registerData(EntityMagicEffect.class, TrackedDataHandlerRegistry.OPTIONAL_UUID);
    public LivingEntity caster;
    protected boolean hasSyncedCaster = false;

    public EntityMagicEffect(EntityType<? extends EntityMagicEffect> type, World worldIn) {
        super(type, worldIn);
    }

    public EntityMagicEffect(EntityType<? extends EntityMagicEffect> type, World world, LivingEntity caster) {
        super(type, world);
        if (!world.isClient && caster != null) {
            this.setCasterID(caster.getUuid());
        }
    }

    @Override
    public PistonBehavior getPistonBehavior() {
        return PistonBehavior.IGNORE;
    }

    @Override
    protected void initDataTracker() {
        this.getDataTracker().startTracking(CASTER, Optional.empty());
    }

    public Optional<UUID> getCasterID() {
        return this.getDataTracker().get(CASTER);
    }

    public void setCasterID(UUID id) {
        this.getDataTracker().set(CASTER, Optional.of(id));
    }

    @Override
    public boolean canHit() {
        return false;
    }

    @Override
    public void pushAwayFrom(Entity entityIn) {
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public void onSpawnPacket(EntitySpawnS2CPacket packet) {
        super.onSpawnPacket(packet);
        if (!this.getWorld().isClient() && this.getCasterID().isPresent() && this.caster == null) {
            Entity casterEntity = ((ServerWorld) this.getWorld()).getEntity(this.getCasterID().get());
            if (casterEntity instanceof LivingEntity) {
                this.caster = (LivingEntity) casterEntity;
                PacketByteBuf buf = PacketByteBufs.create();
                MessageLinkEntities.serialize(new MessageLinkEntities(this, this.caster), buf);
                ServerHelper.sendToAll(StaticVariables.LINK_ENTITIES, buf);
            }
            this.hasSyncedCaster = true;
        }
    }

    @Override
    public void link(Entity entity) {
        if (entity instanceof LivingEntity) {
            this.caster = (LivingEntity) entity;
        }
        this.hasSyncedCaster = true;
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound compound) {
        this.setCasterID(compound.getUuid("caster"));
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound compound) {
        if (this.getCasterID().isPresent()) {
            compound.putUuid("caster", this.getCasterID().get());
        }
    }

    public List<Entity> getEntitiesNearby(double radius) {
        return this.getEntitiesNearby(Entity.class, radius);
    }

    public <T extends Entity> List<T> getEntitiesNearby(Class<T> entityClass, double r) {
        return this.getWorld().getEntitiesByClass(entityClass, this.getBoundingBox().expand(r, r, r), e -> e != this && this.distanceTo(e) <= r + e.getWidth() / 2f);
    }

    public <T extends Entity> List<T> getEntitiesNearbyCube(Class<T> entityClass, double r) {
        return this.getWorld().getEntitiesByClass(entityClass, this.getBoundingBox().expand(r, r, r), e -> e != this);
    }

    public boolean raytraceCheckEntity(Entity entity) {
        Vec3d from = this.getPos();
        int numChecks = 3;
        for (int i = 0; i < numChecks; i++) {
            float increment = entity.getHeight() / (numChecks + 1);
            Vec3d to = entity.getPos().add(0, increment * (i + 1), 0);
            BlockHitResult result = this.getWorld().raycast(new RaycastContext(from, to, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, this));
            if (result.getType() != HitResult.Type.BLOCK) {
                return true;
            }
        }
        return false;
    }
}

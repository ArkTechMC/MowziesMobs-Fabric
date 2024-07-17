package com.bobmowzie.mowziesmobs.server.entity;

import com.ilexiconn.llibrary.server.animation.Animation;
import com.ilexiconn.llibrary.server.animation.AnimationHandler;
import com.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.world.World;
import org.apache.commons.lang3.ArrayUtils;

public abstract class MowzieLLibraryEntity extends MowzieEntity implements IAnimatedEntity {
    private int animationTick;
    private Animation animation = NO_ANIMATION;

    public MowzieLLibraryEntity(EntityType<? extends MowzieEntity> type, World world) {
        super(type, world);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getAnimation() != NO_ANIMATION) {
            this.animationTick++;
            if (this.getWorld().isClient && this.animationTick >= this.animation.getDuration()) {
                this.setAnimation(NO_ANIMATION);
            }
        }
    }

    protected void onAnimationFinish(Animation animation) {
    }

    @Override
    public boolean damage(DamageSource source, float damage) {
        boolean attack = super.damage(source, damage);
        if (attack) {
            if (this.getHealth() > 0.0F && (this.getAnimation() == NO_ANIMATION || this.hurtInterruptsAnimation) && this.playsHurtAnimation) {
                AnimationHandler.INSTANCE.sendAnimationMessage(this, this.getHurtAnimation());
            } else if (this.getHealth() <= 0.0F) {
                AnimationHandler.INSTANCE.sendAnimationMessage(this, this.getDeathAnimation());
            }
        }
        return attack;
    }

    @Override
    public int getAnimationTick() {
        return this.animationTick;
    }

    @Override
    public void setAnimationTick(int tick) {
        this.animationTick = tick;
    }

    @Override
    public Animation getAnimation() {
        return this.animation;
    }

    @Override
    public void setAnimation(Animation animation) {
        if (animation == NO_ANIMATION) {
            this.onAnimationFinish(this.animation);
        }
        this.animation = animation;
        this.setAnimationTick(0);
    }

    public abstract Animation getDeathAnimation();

    public abstract Animation getHurtAnimation();

    @Override
    protected int getDeathDuration() {
        Animation death;
        if ((death = this.getDeathAnimation()) != null) {
            return death.getDuration() - 20;
        }
        return 20;
    }

    @Override
    public void writeSpawnData(PacketByteBuf buf) {
        buf.writeInt(ArrayUtils.indexOf(this.getAnimations(), this.getAnimation()));
        buf.writeInt(this.getAnimationTick());
    }

    @Override
    public void readSpawnData(PacketByteBuf buf) {
        int animOrdinal = buf.readInt();
        int animTick = buf.readInt();
        this.setAnimation(animOrdinal == -1 ? IAnimatedEntity.NO_ANIMATION : this.getAnimations()[animOrdinal]);
        this.setAnimationTick(animTick);
    }
}

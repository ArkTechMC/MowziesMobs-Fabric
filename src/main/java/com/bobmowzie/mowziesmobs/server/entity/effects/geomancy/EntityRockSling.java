package com.bobmowzie.mowziesmobs.server.entity.effects.geomancy;

import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

public class EntityRockSling extends EntityBoulderProjectile implements GeoEntity {
    private static final RawAnimation ROLL_ANIM = RawAnimation.begin().thenLoop("roll");
    private Vec3d launchVec;


    public EntityRockSling(EntityType<? extends EntityRockSling> type, World worldIn) {
        super(type, worldIn);
    }

    public EntityRockSling(EntityType<? extends EntityBoulderProjectile> type, World world, LivingEntity caster, BlockState blockState, BlockPos pos, GeomancyTier tier) {
        super(type, world, caster, blockState, pos, tier);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.age > 30 + this.random.nextInt(35) && this.launchVec != null) {
            this.setVelocity(this.launchVec.normalize().multiply(2f + this.random.nextFloat() / 5, 2f, 2f + this.random.nextFloat() / 5));

        }

    }

    public void setLaunchVec(Vec3d vec) {
        this.launchVec = vec;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        super.registerControllers(controllers);
        AnimationController<EntityRockSling> controller = new AnimationController<>(this, "controller", 0,
                event -> {
                    event.getController()
                            .setAnimation(ROLL_ANIM);
                    return PlayState.CONTINUE;
                });
        controllers.add(controller);
    }
}

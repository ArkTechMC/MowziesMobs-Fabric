package com.bobmowzie.mowziesmobs.server.ability.abilities.player.heliomancy;

import com.bobmowzie.mowziesmobs.server.ability.AbilitySection;
import com.bobmowzie.mowziesmobs.server.ability.AbilityType;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntitySunstrike;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import software.bernie.geckolib.core.animation.RawAnimation;

public class SunstrikeAbility extends HeliomancyAbilityBase {
    private static final double REACH = 15;
    private final static int SUNSTRIKE_RECOVERY = 15;
    private static final RawAnimation SUNSTRIKE_ANIM = RawAnimation.begin().thenPlay("sunstrike");
    protected BlockHitResult rayTrace;

    public SunstrikeAbility(AbilityType<PlayerEntity, SunstrikeAbility> abilityType, PlayerEntity user) {
        super(abilityType, user, new AbilitySection[]{
                new AbilitySection.AbilitySectionInstant(AbilitySection.AbilitySectionType.ACTIVE),
                new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.RECOVERY, SUNSTRIKE_RECOVERY)
        });
    }

    private static BlockHitResult rayTrace(LivingEntity entity, double reach) {
        Vec3d pos = entity.getCameraPosVec(0);
        Vec3d segment = entity.getRotationVector();
        segment = pos.add(segment.x * reach, segment.y * reach, segment.z * reach);
        return entity.getWorld().raycast(new RaycastContext(pos, segment, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, entity));
    }

    @Override
    public boolean tryAbility() {
        super.tryAbility();
        LivingEntity user = this.getUser();
        BlockHitResult raytrace = rayTrace(user, REACH);
        if (raytrace.getType() == HitResult.Type.BLOCK && raytrace.getSide() == Direction.UP) {
            this.rayTrace = raytrace;
            return true;
        }
        return false;
    }

    @Override
    public void start() {
        super.start();
        LivingEntity user = this.getUser();
        if (!user.getWorld().isClient()) {
            BlockPos hit = this.rayTrace.getBlockPos();
            EntitySunstrike sunstrike = new EntitySunstrike(EntityHandler.SUNSTRIKE, user.getWorld(), user, hit.getX(), hit.getY(), hit.getZ());
            sunstrike.onSummon();
            user.getWorld().spawnEntity(sunstrike);
        }
        this.playAnimation(SUNSTRIKE_ANIM);
    }

    @Override
    public boolean preventsBlockBreakingBuilding() {
        return false;
    }

    @Override
    public boolean preventsAttacking() {
        return false;
    }

    @Override
    public boolean preventsInteracting() {
        return false;
    }
}

package com.bobmowzie.mowziesmobs.server.ability;

import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieAnimationController;
import com.bobmowzie.mowziesmobs.client.render.entity.player.GeckoPlayer;
import io.github.fabricators_of_create.porting_lib.entity.events.LivingEntityEvents;
import io.github.fabricators_of_create.porting_lib.entity.events.PlayerInteractionEvents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animation.Animation;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

public class PlayerAbility extends Ability<PlayerEntity> {
    private static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("idle");
    protected RawAnimation activeFirstPersonAnimation;
    protected ItemStack heldItemMainHandVisualOverride;
    protected ItemStack heldItemOffHandVisualOverride;
    protected HandDisplay firstPersonMainHandDisplay;
    protected HandDisplay firstPersonOffHandDisplay;

    public PlayerAbility(AbilityType<PlayerEntity, ? extends Ability> abilityType, PlayerEntity user, AbilitySection[] sectionTrack, int cooldownMax) {
        super(abilityType, user, sectionTrack, cooldownMax);
        if (user.getWorld().isClient) {
            this.activeAnimation = IDLE_ANIM;
            this.heldItemMainHandVisualOverride = null;
            this.heldItemOffHandVisualOverride = null;
            this.firstPersonMainHandDisplay = HandDisplay.DEFAULT;
            this.firstPersonOffHandDisplay = HandDisplay.DEFAULT;
        }
    }

    public PlayerAbility(AbilityType<PlayerEntity, ? extends Ability> abilityType, PlayerEntity user, AbilitySection[] sectionTrack) {
        this(abilityType, user, sectionTrack, 0);
    }

    public void playAnimation(RawAnimation animation, GeckoPlayer.Perspective perspective) {
        if (this.getUser() != null && this.getUser().getWorld().isClient) {
            if (perspective == GeckoPlayer.Perspective.FIRST_PERSON) {
                this.activeFirstPersonAnimation = animation;
            } else {
                this.activeAnimation = animation;
            }
            MowzieAnimationController<GeckoPlayer> controller = GeckoPlayer.getAnimationController(this.getUser(), perspective);
            GeckoPlayer geckoPlayer = GeckoPlayer.getGeckoPlayer(this.getUser(), perspective);
            if (controller != null && geckoPlayer != null) {
                controller.playAnimation(geckoPlayer, animation);
            }
        }
    }

    public void playAnimation(String animationName, GeckoPlayer.Perspective perspective, Animation.LoopType loopType) {
        this.playAnimation(RawAnimation.begin().then(animationName, loopType), perspective);
    }

    public void playAnimation(RawAnimation animation) {
        this.playAnimation(animation, GeckoPlayer.Perspective.FIRST_PERSON);
        this.playAnimation(animation, GeckoPlayer.Perspective.THIRD_PERSON);
    }

    @Override
    public void end() {
        super.end();
        if (this.getUser().getWorld().isClient) {
            this.heldItemMainHandVisualOverride = null;
            this.heldItemOffHandVisualOverride = null;
            this.firstPersonMainHandDisplay = HandDisplay.DEFAULT;
            this.firstPersonOffHandDisplay = HandDisplay.DEFAULT;
        }
    }

    @Override
    public boolean canUse() {
        return super.canUse() && !this.getUser().isSpectator();
    }

    @Override
    protected boolean canContinueUsing() {
        return super.canContinueUsing() && !this.getUser().isSpectator();
    }

    public <E extends GeoEntity> PlayState animationPredicate(AnimationState<E> e, GeckoPlayer.Perspective perspective) {
        RawAnimation whichAnimation;
        if (perspective == GeckoPlayer.Perspective.FIRST_PERSON) {
            whichAnimation = this.activeFirstPersonAnimation;
        } else {
            whichAnimation = this.activeAnimation;
        }
        if (whichAnimation == null || whichAnimation.getAnimationStages().isEmpty())
            return PlayState.STOP;
        e.getController().setAnimation(whichAnimation);
        return PlayState.CONTINUE;
    }

    @Environment(EnvType.CLIENT)
    public ItemStack heldItemMainHandOverride() {
        return this.heldItemMainHandVisualOverride;
    }

    @Environment(EnvType.CLIENT)
    public ItemStack heldItemOffHandOverride() {
        return this.heldItemOffHandVisualOverride;
    }

    @Environment(EnvType.CLIENT)
    public HandDisplay getFirstPersonMainHandDisplay() {
        return this.firstPersonMainHandDisplay;
    }

    @Environment(EnvType.CLIENT)
    public HandDisplay getFirstPersonOffHandDisplay() {
        return this.firstPersonOffHandDisplay;
    }

    // Events
    public void onRightClickEmpty(PlayerInteractEvent.RightClickEmpty event) {

    }

    public void onRightClickBlock(PlayerEntity player, World world, Hand hand, BlockHitResult hitResult) {

    }

    public void onRightClickWithItem(PlayerEntity player, World world, Hand hand) {

    }

    public void onRightClickEntity(PlayerEntity player, World world, Hand hand, Entity entity, EntityHitResult hitResult) {

    }

    public void onLeftClickEmpty(PlayerInteractionEvents.LeftClickEmpty event) {

    }

    public void onLeftClickBlock(PlayerEntity player, World world, Hand hand, BlockPos pos, Direction direction) {

    }

    public void onLeftClickEntity(PlayerEntity player, World world, Hand hand, Entity entity, EntityHitResult hitResult) {

    }

    public void onTakeDamage(LivingEntity entity, DamageSource source, float amount) {

    }

    public void onJump(LivingEntityEvents.LivingJumpEvent event) {

    }

    public void onRightMouseDown(PlayerEntity player) {

    }

    public void onLeftMouseDown(PlayerEntity player) {

    }

    public void onRightMouseUp(PlayerEntity player) {

    }

    public void onLeftMouseUp(PlayerEntity player) {

    }

    public void onSneakDown(PlayerEntity player) {

    }

    public void onSneakUp(PlayerEntity player) {

    }

    public enum HandDisplay {
        DEFAULT,
        DONT_RENDER,
        FORCE_RENDER
    }
}

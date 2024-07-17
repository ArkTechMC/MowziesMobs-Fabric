package com.bobmowzie.mowziesmobs.client;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.gui.CustomBossBar;
import com.bobmowzie.mowziesmobs.client.model.entity.ModelGeckoPlayerFirstPerson;
import com.bobmowzie.mowziesmobs.client.model.entity.ModelGeckoPlayerThirdPerson;
import com.bobmowzie.mowziesmobs.client.render.MMRenderType;
import com.bobmowzie.mowziesmobs.client.render.block.SculptorBlockMarking;
import com.bobmowzie.mowziesmobs.client.render.entity.player.GeckoFirstPersonRenderer;
import com.bobmowzie.mowziesmobs.client.render.entity.player.GeckoPlayer;
import com.bobmowzie.mowziesmobs.client.render.entity.player.GeckoRenderPlayer;
import com.bobmowzie.mowziesmobs.server.ability.AbilityHandler;
import com.bobmowzie.mowziesmobs.server.capability.AbilityCapability;
import com.bobmowzie.mowziesmobs.server.capability.CapabilityHandler;
import com.bobmowzie.mowziesmobs.server.capability.FrozenCapability;
import com.bobmowzie.mowziesmobs.server.capability.PlayerCapability;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntityCameraShake;
import com.bobmowzie.mowziesmobs.server.entity.frostmaw.EntityFrozenController;
import com.bobmowzie.mowziesmobs.server.item.ItemBlowgun;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.OverlayVertexConsumer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockRenderView;
import net.minecraftforge.client.event.*;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;

@Environment(EnvType.CLIENT)
public enum ClientEventHandler {
    INSTANCE;

    private static final Identifier FROZEN_BLUR = new Identifier("textures/misc/powder_snow_outline.png");
    private static final Identifier SCULPTOR_BLOCK_GLOW = new Identifier(MowziesMobs.MODID, "textures/entity/sculptor_highlight.png");

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onHandRender(RenderHandEvent event) {
        if (!ConfigHandler.CLIENT.customPlayerAnims.get()) return;
        PlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) return;
        boolean shouldAnimate = false;
        AbilityCapability.IAbilityCapability abilityCapability = AbilityHandler.INSTANCE.getAbilityCapability(player);
        if (abilityCapability != null) shouldAnimate = abilityCapability.getActiveAbility() != null;
//        shouldAnimate = (player.ticksExisted / 20) % 2 == 0;
        if (shouldAnimate) {
            PlayerCapability.IPlayerCapability playerCapability = CapabilityHandler.getCapability(player, CapabilityHandler.PLAYER_CAPABILITY);
            if (playerCapability != null) {
                GeckoPlayer.GeckoPlayerFirstPerson geckoPlayer = GeckoFirstPersonRenderer.GECKO_PLAYER_FIRST_PERSON;
                if (geckoPlayer != null) {
                    ModelGeckoPlayerFirstPerson geckoFirstPersonModel = (ModelGeckoPlayerFirstPerson) geckoPlayer.getModel();
                    GeckoFirstPersonRenderer firstPersonRenderer = (GeckoFirstPersonRenderer) geckoPlayer.getPlayerRenderer();

                    if (geckoFirstPersonModel != null && firstPersonRenderer != null) {
                        if (!geckoFirstPersonModel.isUsingSmallArms() && ((AbstractClientPlayerEntity) player).getModel().equals("slim")) {
                            firstPersonRenderer.setSmallArms();
                        }
                        event.setCanceled(true);

                        if (event.isCanceled()) {
                            float delta = event.getPartialTick();
                            float f1 = MathHelper.lerp(delta, player.prevPitch, player.getPitch());
                            firstPersonRenderer.renderItemInFirstPerson((AbstractClientPlayerEntity) player, f1, delta, event.getHand(), event.getSwingProgress(), event.getItemStack(), event.getEquipProgress(), event.getPoseStack(), event.getMultiBufferSource(), event.getPackedLight(), geckoPlayer);
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void renderLivingEvent(RenderLivingEvent.Pre<? extends LivingEntity, ? extends EntityModel<? extends LivingEntity>> event) {
        if (event.getEntity() instanceof PlayerEntity) {
            if (!ConfigHandler.CLIENT.customPlayerAnims.get()) return;
            PlayerEntity player = (PlayerEntity) event.getEntity();
            if (player == null) return;
            float delta = event.getPartialTick();
            AbilityCapability.IAbilityCapability abilityCapability = AbilityHandler.INSTANCE.getAbilityCapability(player);
//        shouldAnimate = (player.ticksExisted / 20) % 2 == 0;
            if (abilityCapability != null && abilityCapability.getActiveAbility() != null) {
                PlayerCapability.IPlayerCapability playerCapability = CapabilityHandler.getCapability(event.getEntity(), CapabilityHandler.PLAYER_CAPABILITY);
                if (playerCapability != null) {
                    GeckoPlayer.GeckoPlayerThirdPerson geckoPlayer = playerCapability.getGeckoPlayer();
                    if (geckoPlayer != null) {
                        ModelGeckoPlayerThirdPerson geckoPlayerModel = (ModelGeckoPlayerThirdPerson) geckoPlayer.getModel();
                        GeckoRenderPlayer animatedPlayerRenderer = (GeckoRenderPlayer) geckoPlayer.getPlayerRenderer();

                        if (geckoPlayerModel != null && animatedPlayerRenderer != null) {
                            event.setCanceled(true);

                            if (event.isCanceled()) {
                                animatedPlayerRenderer.render((AbstractClientPlayerEntity) event.getEntity(), event.getEntity().getYRot(), delta, event.getPoseStack(), event.getMultiBufferSource(), event.getPackedLight(), geckoPlayer);
                            }
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.START || event.player == null) {
            return;
        }
        PlayerEntity player = event.player;
        PlayerCapability.IPlayerCapability playerCapability = CapabilityHandler.getCapability(player, CapabilityHandler.PLAYER_CAPABILITY);
        if (playerCapability != null && event.side == LogicalSide.CLIENT) {
            GeckoPlayer geckoPlayer = playerCapability.getGeckoPlayer();
            if (geckoPlayer != null) geckoPlayer.tick();
            if (player == MinecraftClient.getInstance().player)
                GeckoFirstPersonRenderer.GECKO_PLAYER_FIRST_PERSON.tick();
        }
//        if(player.getInventory().getArmor(3).is(ItemHandler.SOL_VISAGE.asItem())){
//            int tick = player.tickCount;
//            double orbitSpeed = 50;
//            double orbitSize = 0.6;
//            double xOffset = (Math.sin(tick * orbitSpeed) * orbitSize);
//            double zOffset= (Math.cos(tick * orbitSpeed) * orbitSize);
//            Vec3 particleVec = Vec3.ZERO.add(xOffset, 2.2f, zOffset).yRot((float)Math.toRadians(-player.getYHeadRot())).xRot((float) Math.toRadians(0f)).add(player.position());
//            Vec3 particleVec2 = Vec3.ZERO.add(-xOffset, 2.2f, -zOffset).yRot((float)Math.toRadians(-player.getYHeadRot())).xRot((float) Math.toRadians(0f)).add(player.position());
//
//            player.level.addParticle(ParticleTypes.SMALL_FLAME, particleVec.x, particleVec.y, particleVec.z, 0d, 0d, 0d);
//            player.level.addParticle(ParticleTypes.SMALL_FLAME, particleVec2.x, particleVec2.y, particleVec2.z, 0d, 0d, 0d);
//
//        }

        if (player == MinecraftClient.getInstance().player) {

        }
    }

    @SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent event) {
        PlayerEntity player = MinecraftClient.getInstance().player;
//        if (player != null) {
//            PlayerCapability.IPlayerCapability playerCapability = CapabilityHandler.getCapability(player, CapabilityHandler.PLAYER_CAPABILITY);
//            if (playerCapability != null && playerCapability.getGeomancy().canUse(player) && playerCapability.getGeomancy().isSpawningBoulder() && playerCapability.getGeomancy().getSpawnBoulderCharge() > 2) {
//                Vector3d lookPos = playerCapability.getGeomancy().getLookPos();
//                Vector3d playerEyes = player.getEyePosition(Minecraft.getInstance().getRenderPartialTicks());
//                Vector3d vec = playerEyes.subtract(lookPos).normalize();
//                float yaw = (float) Math.atan2(vec.z, vec.x);
//                float pitch = (float) Math.asin(vec.y);
//                player.rotationYaw = (float) (yaw * 180f/Math.PI + 90);
//                player.rotationPitch = (float) (pitch * 180f/Math.PI);
//                player.rotationYawHead = player.rotationYaw;
//                player.prevRotationYaw = player.rotationYaw;
//                player.prevRotationPitch = player.rotationPitch;
//                player.prevRotationYawHead = player.rotationYawHead;
//            }
        FrozenCapability.IFrozenCapability frozenCapability = CapabilityHandler.getCapability(player, CapabilityHandler.FROZEN_CAPABILITY);
        if (frozenCapability != null && frozenCapability.getFrozen() && frozenCapability.getPrevFrozen()) {
            player.setYaw(frozenCapability.getFrozenYaw());
            player.setPitch(frozenCapability.getFrozenPitch());
            player.headYaw = frozenCapability.getFrozenYawHead();
            player.prevYaw = player.getYaw();
            player.prevPitch = player.getPitch();
            player.prevHeadYaw = player.headYaw;
        }
    }

    @SubscribeEvent
    public void onRenderLiving(RenderLivingEvent.Pre event) {
        LivingEntity entity = event.getEntity();
        FrozenCapability.IFrozenCapability frozenCapability = CapabilityHandler.getCapability(entity, CapabilityHandler.FROZEN_CAPABILITY);
        if (frozenCapability != null && frozenCapability.getFrozen() && frozenCapability.getPrevFrozen()) {
            entity.setYaw(entity.prevYaw = frozenCapability.getFrozenYaw());
            entity.setPitch(entity.prevPitch = frozenCapability.getFrozenPitch());
            entity.headYaw = entity.prevHeadYaw = frozenCapability.getFrozenYawHead();
            entity.bodyYaw = entity.prevBodyYaw = frozenCapability.getFrozenRenderYawOffset();
            entity.handSwingProgress = entity.lastHandSwingProgress = frozenCapability.getFrozenSwingProgress();
            entity.limbAnimator.setSpeed(frozenCapability.getFrozenWalkAnimSpeed());
            entity.limbAnimator.pos = frozenCapability.getFrozenWalkAnimPosition();
            entity.setSneaking(false);
        }
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderGuiOverlayEvent.Post e) {
        final int startTime = 210;
        final int pointStart = 1200;
        final int timePerMillis = 22;
        if (e.getOverlay() == VanillaGuiOverlay.FROSTBITE.type()) {
            if (MinecraftClient.getInstance().player != null) {
                FrozenCapability.IFrozenCapability frozenCapability = CapabilityHandler.getCapability(MinecraftClient.getInstance().player, CapabilityHandler.FROZEN_CAPABILITY);
                if (frozenCapability != null && frozenCapability.getFrozen() && MinecraftClient.getInstance().options.getPerspective() == Perspective.FIRST_PERSON) {
                    Window res = e.getWindow();
                    e.getGuiGraphics().blit(FROZEN_BLUR, 0, 0, 0, 0, res.getScaledWidth(), res.getScaledHeight(), res.getScaledWidth(), res.getScaledHeight());
                }
            }
        }
    }

    // Remove frozen overlay
    @SubscribeEvent
    public void onRenderHUD(RenderGuiOverlayEvent.Pre event) {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player != null && player.hasVehicle()) {
            if (player.getVehicle() instanceof EntityFrozenController) {
                if (event.getOverlay() == VanillaGuiOverlay.MOUNT_HEALTH.type()) {
                    event.setCanceled(true);
                }
                MinecraftClient.getInstance().inGameHud.setOverlayMessage(Text.empty(), false);
            }
        }
    }

    @SubscribeEvent
    public void updateFOV(ComputeFovModifierEvent event) {
        PlayerEntity player = event.getPlayer();
        if (player.isUsingItem() && player.getActiveItem().getItem() instanceof ItemBlowgun) {
            int i = player.getItemUseTime();
            float f1 = (float) i / 5.0F;
            if (f1 > 1.0F) {
                f1 = 1.0F;
            } else {
                f1 = f1 * f1;
            }

            event.setNewFovModifier(1.0F - f1 * 0.15F);
        }
    }

    @SubscribeEvent
    public void onSetupCamera(ViewportEvent.ComputeCameraAngles event) {
        PlayerEntity player = MinecraftClient.getInstance().player;
        float delta = MinecraftClient.getInstance().getTickDelta();
        float ticksExistedDelta = player.age + delta;
        if (player != null) {
            if (ConfigHandler.CLIENT.doCameraShakes.get() && !MinecraftClient.getInstance().isPaused()) {
                float shakeAmplitude = 0;
                for (EntityCameraShake cameraShake : player.getWorld().getNonSpectatingEntities(EntityCameraShake.class, player.getBoundingBox().expand(20, 20, 20))) {
                    if (cameraShake.distanceTo(player) < cameraShake.getRadius()) {
                        shakeAmplitude += cameraShake.getShakeAmount(player, delta);
                    }
                }
                if (shakeAmplitude > 1.0f) shakeAmplitude = 1.0f;
                event.setPitch((float) (event.getPitch() + shakeAmplitude * Math.cos(ticksExistedDelta * 3 + 2) * 25));
                event.setYaw((float) (event.getYaw() + shakeAmplitude * Math.cos(ticksExistedDelta * 5 + 1) * 25));
                event.setRoll((float) (event.getRoll() + shakeAmplitude * Math.cos(ticksExistedDelta * 4) * 25));
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onRenderBossBar(CustomizeGuiOverlayEvent.BossEventProgress event) {
        if (!ConfigHandler.CLIENT.customBossBars.get()) return;
        Identifier bossRegistryName = ClientProxy.bossBarRegistryNames.getOrDefault(event.getBossEvent().getId(), null);
        if (bossRegistryName == null) return;
        CustomBossBar customBossBar = CustomBossBar.customBossBars.getOrDefault(bossRegistryName, null);
        if (customBossBar == null) return;

        event.setCanceled(true);
        customBossBar.renderBossBar(event);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onRenderLevelStage(RenderLevelStageEvent event) {
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_BLOCK_ENTITIES) {
            ClientWorld level = MinecraftClient.getInstance().world;
            if (MinecraftClient.getInstance().player != null && level != null && level.getModelDataManager() != null) {
                Vec3d cameraPos = event.getCamera().getPosition();
                double d0 = cameraPos.getX();
                double d1 = cameraPos.getY();
                double d2 = cameraPos.getZ();
                for (Long2ObjectMap.Entry<SculptorBlockMarking> entry : ClientProxy.sculptorMarkedBlocks.long2ObjectEntrySet()) {
                    BlockPos blockpos2 = BlockPos.fromLong(entry.getLongKey());
                    double d3 = (double) blockpos2.getX() - d0;
                    double d4 = (double) blockpos2.getY() - d1;
                    double d5 = (double) blockpos2.getZ() - d2;
                    if (!(d3 * d3 + d4 * d4 + d5 * d5 > 1024.0D)) {
                        SculptorBlockMarking blockMarking = entry.getValue();
                        float alpha = 1f - (float) blockMarking.getTicks() / (float) blockMarking.getDuration();
                        event.getPoseStack().pushPose();
                        event.getPoseStack().translate((double) blockpos2.getX() - d0, (double) blockpos2.getY() - d1, (double) blockpos2.getZ() - d2);
                        MatrixStack.Entry posestack$pose1 = event.getPoseStack().last();
                        float f = (float) MinecraftClient.getInstance().player.age + MinecraftClient.getInstance().getPartialTick();
                        float blockOffset = (blockpos2.getX() + blockpos2.getY() + blockpos2.getZ()) * 0.25f;
                        VertexConsumer vertexconsumer1 = new OverlayVertexConsumer(MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers().getBuffer(MMRenderType.highlight(SCULPTOR_BLOCK_GLOW, f * 0.02f + blockOffset, f * 0.01f + blockOffset)), posestack$pose1.getPositionMatrix(), posestack$pose1.getNormalMatrix(), 0.25F);
                        net.minecraftforge.client.model.data.ModelData modelData = level.getModelDataManager().getAt(blockpos2);
                        this.renderBreakingTexture(level.getBlockState(blockpos2), blockpos2, level, event.getPoseStack(), level.random, vertexconsumer1, modelData == null ? net.minecraftforge.client.model.data.ModelData.EMPTY : modelData);

                        event.getPoseStack().popPose();
                    }
                }
            }
        }
    }

    private void renderBreakingTexture(BlockState state, BlockPos pos, BlockRenderView blockAndTintGetter, MatrixStack poseStack, Random random, VertexConsumer vertexConsumer, net.minecraftforge.client.model.data.ModelData modelData) {
        if (state.getRenderType() == BlockRenderType.MODEL) {
            BlockRenderManager blockRenderDispatcher = MinecraftClient.getInstance().getBlockRenderManager();
            BakedModel bakedmodel = blockRenderDispatcher.getModel(state);
            long i = state.getRenderingSeed(pos);
            blockRenderDispatcher.getModelRenderer().render(blockAndTintGetter, bakedmodel, state, pos, poseStack, vertexConsumer, true, random, i, OverlayTexture.DEFAULT_UV, modelData, null);
        }
    }

    @SubscribeEvent
    public void onLevelTick(TickEvent.LevelTickEvent event) {
        if (event.side == LogicalSide.CLIENT) {
            MowziesMobs.PROXY.updateMarkedBlocks();
        }
    }
}

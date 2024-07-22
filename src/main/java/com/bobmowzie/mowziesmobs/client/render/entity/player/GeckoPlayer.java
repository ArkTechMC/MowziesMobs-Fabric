package com.bobmowzie.mowziesmobs.client.render.entity.player;

import com.bobmowzie.mowziesmobs.client.model.entity.ModelGeckoPlayerFirstPerson;
import com.bobmowzie.mowziesmobs.client.model.entity.ModelGeckoPlayerThirdPerson;
import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieAnimationController;
import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieGeoModel;
import com.bobmowzie.mowziesmobs.server.capability.AbilityCapability;
import com.bobmowzie.mowziesmobs.server.capability.PlayerCapability;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.resource.ResourceManager;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;

@Environment(EnvType.CLIENT)
public abstract class GeckoPlayer implements GeoEntity {

    public static final String THIRD_PERSON_CONTROLLER_NAME = "thirdPersonAnimation";
    public static final String FIRST_PERSON_CONTROLLER_NAME = "firstPersonAnimation";
    private static final RawAnimation IDLE_ANIMATION = RawAnimation.begin().thenLoop("idle");
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    protected GeoRenderer<GeckoPlayer> renderer;
    protected MowzieGeoModel<GeckoPlayer> model;
    protected MowzieAnimationController<GeckoPlayer> controller;
    private int tickTimer = 0;
    private final PlayerEntity player;

    public GeckoPlayer(PlayerEntity player) {
        this.player = player;
        this.setup(player);
    }

    @Nullable
    public static GeckoPlayer getGeckoPlayer(PlayerEntity player, Perspective perspective) {
        if (perspective == Perspective.FIRST_PERSON) return GeckoFirstPersonRenderer.GECKO_PLAYER_FIRST_PERSON;
        PlayerCapability.IPlayerCapability playerCapability = PlayerCapability.get(player);
        if (playerCapability != null) {
            return playerCapability.getGeckoPlayer();
        }
        return null;
    }

    public static MowzieAnimationController<GeckoPlayer> getAnimationController(PlayerEntity player, Perspective perspective) {
        PlayerCapability.IPlayerCapability playerCapability = PlayerCapability.get(player);
        if (playerCapability != null) {
            GeckoPlayer geckoPlayer;
            if (perspective == Perspective.FIRST_PERSON)
                geckoPlayer = GeckoFirstPersonRenderer.GECKO_PLAYER_FIRST_PERSON;
            else geckoPlayer = playerCapability.getGeckoPlayer();
            if (geckoPlayer != null) {
                return geckoPlayer.controller;
            }
        }
        return null;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        this.controller = new MowzieAnimationController<>(this, this.getControllerName(), 0, this::predicate, 0);
        controllers.add(this.controller);
    }

    public MowzieAnimationController<GeckoPlayer> getController() {
        return this.controller;
    }

    public PlayerEntity getPlayer() {
        return this.player;
    }

    public void tick() {
        this.tickTimer++;
    }

    @Override
    public double getTick(Object entity) {
        return ((GeckoPlayer) entity).tickTimer;
    }

    public <E extends GeoEntity> PlayState predicate(AnimationState<E> e) {
        e.getController().transitionLength(0);
        PlayerEntity player = this.getPlayer();
        if (player == null) {
            return PlayState.STOP;
        }
        AbilityCapability.IAbilityCapability abilityCapability = AbilityCapability.get(player);
        if (abilityCapability == null) {
            return PlayState.STOP;
        }

        if (abilityCapability.getActiveAbility() != null) {
            return abilityCapability.animationPredicate(e, this.getPerspective());
        } else {
            e.getController().setAnimation(IDLE_ANIMATION);
            return PlayState.CONTINUE;
        }
    }

    public GeoRenderer<GeckoPlayer> getPlayerRenderer() {
        return this.renderer;
    }

    public MowzieGeoModel<GeckoPlayer> getModel() {
        return this.model;
    }

    public abstract String getControllerName();

    public abstract Perspective getPerspective();

    public abstract void setup(PlayerEntity player);

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    public enum Perspective {
        FIRST_PERSON,
        THIRD_PERSON
    }

    public static class GeckoPlayerFirstPerson extends GeckoPlayer {
        public GeckoPlayerFirstPerson(PlayerEntity player) {
            super(player);
        }

        @Override
        public String getControllerName() {
            return FIRST_PERSON_CONTROLLER_NAME;
        }

        @Override
        public Perspective getPerspective() {
            return Perspective.FIRST_PERSON;
        }

        @Override
        public void setup(PlayerEntity player) {
            ModelGeckoPlayerFirstPerson modelGeckoPlayer = new ModelGeckoPlayerFirstPerson();
            this.model = modelGeckoPlayer;
            GeckoFirstPersonRenderer geckoRenderer = new GeckoFirstPersonRenderer(MinecraftClient.getInstance(), modelGeckoPlayer);
            this.renderer = geckoRenderer;
            if (!geckoRenderer.getModelsToLoad().containsKey(this.getClass())) {
                geckoRenderer.getModelsToLoad().put(this.getClass(), geckoRenderer);
            }

            this.getAnimatableInstanceCache().getManagerForId(this.renderer.getInstanceId(this));
            this.getController().setLastModel(this.model);
            GeckoFirstPersonRenderer.GECKO_PLAYER_FIRST_PERSON = this;
        }
    }

    public static class GeckoPlayerThirdPerson extends GeckoPlayer {
        public static GeckoRenderPlayer GECKO_RENDERER_THIRD_PERSON_NORMAL;
        public static ModelGeckoPlayerThirdPerson GECKO_MODEL_THIRD_PERSON_NORMAL;
        public static GeckoRenderPlayer GECKO_RENDERER_THIRD_PERSON_SLIM;
        public static ModelGeckoPlayerThirdPerson GECKO_MODEL_THIRD_PERSON_SLIM;

        protected GeoRenderer<GeckoPlayer> rendererSlim;
        protected MowzieGeoModel<GeckoPlayer> modelSlim;

        public GeckoPlayerThirdPerson(PlayerEntity player) {
            super(player);
        }

        public static void initRenderer() {
            GECKO_MODEL_THIRD_PERSON_NORMAL = new ModelGeckoPlayerThirdPerson();
            GECKO_MODEL_THIRD_PERSON_SLIM = new ModelGeckoPlayerThirdPerson();
            GECKO_MODEL_THIRD_PERSON_SLIM.setUseSmallArms(true);

            MinecraftClient minecraft = MinecraftClient.getInstance();
            EntityRenderDispatcher dispatcher = minecraft.getEntityRenderDispatcher();
            ItemRenderer itemRenderer = minecraft.getItemRenderer();
            ResourceManager resourceManager = minecraft.getResourceManager();
            EntityModelLoader entityModelSet = minecraft.getEntityModelLoader();
            TextRenderer font = minecraft.textRenderer;
            EntityRendererFactory.Context context = new EntityRendererFactory.Context(dispatcher, itemRenderer, minecraft.getBlockRenderManager(), dispatcher.getHeldItemRenderer(), resourceManager, entityModelSet, font);
            GeckoRenderPlayer geckoRenderer = new GeckoRenderPlayer(context, false, GECKO_MODEL_THIRD_PERSON_NORMAL);
            if (!geckoRenderer.getModelsToLoad().containsKey(GeckoPlayerThirdPerson.class)) {
                geckoRenderer.getModelsToLoad().put(GeckoPlayerThirdPerson.class, geckoRenderer);
            }
            GECKO_RENDERER_THIRD_PERSON_NORMAL = geckoRenderer;

            GeckoRenderPlayer geckoRendererSlim = new GeckoRenderPlayer(context, true, GECKO_MODEL_THIRD_PERSON_SLIM);
            if (!geckoRendererSlim.getModelsToLoad().containsKey(GeckoPlayerThirdPerson.class)) {
                geckoRendererSlim.getModelsToLoad().put(GeckoPlayerThirdPerson.class, geckoRendererSlim);
            }
            GECKO_RENDERER_THIRD_PERSON_SLIM = geckoRendererSlim;
        }

        @Override
        public String getControllerName() {
            return THIRD_PERSON_CONTROLLER_NAME;
        }

        @Override
        public Perspective getPerspective() {
            return Perspective.THIRD_PERSON;
        }

        @Override
        public void setup(PlayerEntity player) {
            this.model = GECKO_MODEL_THIRD_PERSON_NORMAL;
            this.renderer = GECKO_RENDERER_THIRD_PERSON_NORMAL;
            this.modelSlim = GECKO_MODEL_THIRD_PERSON_SLIM;
            this.rendererSlim = GECKO_RENDERER_THIRD_PERSON_SLIM;

            this.getAnimatableInstanceCache().getManagerForId(this.renderer.getInstanceId(this));
            this.getController().setLastModel(this.model);
        }

        public boolean isPlayerSlim() {
            return ((AbstractClientPlayerEntity) this.getPlayer()).getModel().equals("slim");
        }

        @Override
        public MowzieGeoModel<GeckoPlayer> getModel() {
            return this.isPlayerSlim() ? this.modelSlim : this.model;
        }

        @Override
        public GeoRenderer<GeckoPlayer> getPlayerRenderer() {
            return this.isPlayerSlim() ? this.rendererSlim : this.renderer;
        }
    }
}
package com.bobmowzie.mowziesmobs.client;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.entity.umvuthana.MaskType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelModifier;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.json.ModelOverrideList;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.render.model.json.Transformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;

import java.util.List;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class MMModels {
    public static final String[] HAND_MODEL_ITEMS = new String[]{"wrought_axe", "spear", "earthrend_gauntlet", "sculptor_staff"};
    private static boolean additionalBaked = false;

    public static BakedModel onModelBakeEvent(BakedModel model, ModelModifier.AfterBake.Context context) {
        Map<Identifier, BakedModel> map = context.loader().getBakedModelMap();
        for (String item : HAND_MODEL_ITEMS) {
            if (new Identifier(MowziesMobs.MODID, item).equals(context.id())) {
                Identifier modelInventory = new ModelIdentifier(new Identifier(MowziesMobs.MODID, item), "inventory");
                Identifier modelHand = new ModelIdentifier(new Identifier(MowziesMobs.MODID, item + "_in_hand"), "inventory");
                BakedModel bakedModelDefault = map.get(modelInventory);
                BakedModel bakedModelHand = map.get(modelHand);
                return new BakedModelImpl(bakedModelDefault) {
                    public Transformation getTransform(ModelTransformationMode cameraTransformType) {
                        BakedModel modelToUse = bakedModelDefault;
                        if (cameraTransformType == ModelTransformationMode.FIRST_PERSON_LEFT_HAND || cameraTransformType == ModelTransformationMode.FIRST_PERSON_RIGHT_HAND
                                || cameraTransformType == ModelTransformationMode.THIRD_PERSON_LEFT_HAND || cameraTransformType == ModelTransformationMode.THIRD_PERSON_RIGHT_HAND)
                            modelToUse = bakedModelHand;
                        return modelToUse.getTransformation().getTransformation(cameraTransformType);
                    }
                };
            }
        }
        if (!additionalBaked) {
            additionalBaked = true;
            for (MaskType type : MaskType.values()) {
                ModelIdentifier maskModelInventory = new ModelIdentifier(new Identifier(MowziesMobs.MODID, "umvuthana_mask_" + type.name), "inventory");
                ModelIdentifier maskModelFrame = new ModelIdentifier(new Identifier(MowziesMobs.MODID, "umvuthana_mask_" + type.name + "_frame"), "inventory");
                bakeMask(map, maskModelInventory, maskModelFrame);
            }
            ModelIdentifier maskModelInventory = new ModelIdentifier(new Identifier(MowziesMobs.MODID, "sol_visage"), "inventory");
            ModelIdentifier maskModelFrame = new ModelIdentifier(new Identifier(MowziesMobs.MODID, "sol_visage_frame"), "inventory");
            bakeMask(map, maskModelInventory, maskModelFrame);
        }
        return model;
    }

    private static void bakeMask(Map<Identifier, BakedModel> map, ModelIdentifier maskModelInventory, ModelIdentifier maskModelFrame) {
        BakedModel maskBakedModelDefault = map.get(maskModelInventory);
        BakedModel maskBakedModelFrame = map.get(maskModelFrame);
        BakedModel maskModelWrapper = new BakedModelImpl(maskBakedModelDefault) {
            @Override
            public Transformation getTransform(ModelTransformationMode cameraTransformType) {
                BakedModel modelToUse = maskBakedModelDefault;
                if (cameraTransformType == ModelTransformationMode.FIXED)
                    modelToUse = maskBakedModelFrame;
                return modelToUse.getTransformation().getTransformation(cameraTransformType);
            }
        };
        map.put(maskModelInventory, maskModelWrapper);
    }

    public static abstract class BakedModelImpl implements BakedModel {
        private final BakedModel defaultModel;

        public BakedModelImpl(BakedModel defaultModel) {
            this.defaultModel = defaultModel;
        }

        @Override
        public List<BakedQuad> getQuads(BlockState state, Direction side, Random rand) {
            return this.defaultModel.getQuads(state, side, rand);
        }

        @Override
        public boolean useAmbientOcclusion() {
            return this.defaultModel.useAmbientOcclusion();
        }

        @Override
        public boolean hasDepth() {
            return this.defaultModel.hasDepth();
        }

        @Override
        public boolean isSideLit() {
            return false;
        }

        @Override
        public boolean isBuiltin() {
            return this.defaultModel.isBuiltin();
        }

        @Override
        public Sprite getParticleSprite() {
            return this.defaultModel.getParticleSprite();
        }

        @Override
        public ModelTransformation getTransformation() {
            return new ModelTransformation(
                    this.getTransform(ModelTransformationMode.THIRD_PERSON_LEFT_HAND),
                    this.getTransform(ModelTransformationMode.THIRD_PERSON_RIGHT_HAND),
                    this.getTransform(ModelTransformationMode.FIRST_PERSON_LEFT_HAND),
                    this.getTransform(ModelTransformationMode.FIRST_PERSON_RIGHT_HAND),
                    this.getTransform(ModelTransformationMode.HEAD),
                    this.getTransform(ModelTransformationMode.GUI),
                    this.getTransform(ModelTransformationMode.GROUND),
                    this.getTransform(ModelTransformationMode.FIXED)
            );
        }

        @Override
        public ModelOverrideList getOverrides() {
            return this.defaultModel.getOverrides();
        }

        public abstract Transformation getTransform(ModelTransformationMode cameraTransformType);
    }
}
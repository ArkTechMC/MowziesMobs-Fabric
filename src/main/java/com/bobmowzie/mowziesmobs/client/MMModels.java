package com.bobmowzie.mowziesmobs.client;

import com.bobmowzie.mowziesmobs.server.entity.umvuthana.MaskType;
import io.github.fabricators_of_create.porting_lib.event.client.ModelLoadCallback;
import io.github.fabricators_of_create.porting_lib.mixin.client.ModelBakeryMixin;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.json.ModelOverrideList;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class MMModels {
    public static final String[] HAND_MODEL_ITEMS = new String[]{"wrought_axe", "spear", "earthrend_gauntlet", "sculptor_staff"};

    @SubscribeEvent
    public static void onModelBakeEvent(ModelEvent.ModifyBakingResult event) {
        Map<Identifier, BakedModel> map = event.getModels();

        for (String item : HAND_MODEL_ITEMS) {
            Identifier modelInventory = new ModelIdentifier(new Identifier("mowziesmobs", item), "inventory");
            Identifier modelHand = new ModelIdentifier(new Identifier("mowziesmobs", item + "_in_hand"), "inventory");

            BakedModel bakedModelDefault = map.get(modelInventory);
            BakedModel bakedModelHand = map.get(modelHand);
            BakedModel modelWrapper = new BakedModel() {
                @Override
                public List<BakedQuad> getQuads(BlockState state, Direction side, Random rand) {
                    return bakedModelDefault.getQuads(state, side, rand);
                }

                @Override
                public boolean useAmbientOcclusion() {
                    return bakedModelDefault.useAmbientOcclusion();
                }

                @Override
                public boolean hasDepth() {
                    return bakedModelDefault.hasDepth();
                }

                @Override
                public boolean isSideLit() {
                    return false;
                }

                @Override
                public boolean isBuiltin() {
                    return bakedModelDefault.isBuiltin();
                }

                @Override
                public Sprite getParticleSprite() {
                    return bakedModelDefault.getParticleSprite();
                }

                @Override
                public ModelTransformation getTransformation() {
                    return null;
                }

                @Override
                public ModelOverrideList getOverrides() {
                    return bakedModelDefault.getOverrides();
                }

                @Override
                public BakedModel getTransform(ModelTransformationMode cameraTransformType, MatrixStack mat, boolean applyLeftHandTransform) {
                    BakedModel modelToUse = bakedModelDefault;
                    if (cameraTransformType == ModelTransformationMode.FIRST_PERSON_LEFT_HAND || cameraTransformType == ModelTransformationMode.FIRST_PERSON_RIGHT_HAND
                            || cameraTransformType == ModelTransformationMode.THIRD_PERSON_LEFT_HAND || cameraTransformType == ModelTransformationMode.THIRD_PERSON_RIGHT_HAND) {
                        modelToUse = bakedModelHand;
                    }
                    return ForgeHooksClient.handleCameraTransforms(mat, modelToUse, cameraTransformType, applyLeftHandTransform);
                }
            };
            map.put(modelInventory, modelWrapper);
        }

        for (MaskType type : MaskType.values()) {
            ModelIdentifier maskModelInventory = new ModelIdentifier(new Identifier("mowziesmobs", "umvuthana_mask_" + type.name), "inventory");
            ModelIdentifier maskModelFrame = new ModelIdentifier(new Identifier("mowziesmobs", "umvuthana_mask_" + type.name + "_frame"), "inventory");
            bakeMask(map, maskModelInventory, maskModelFrame);
        }
        ModelIdentifier maskModelInventory = new ModelIdentifier(new Identifier("mowziesmobs", "sol_visage"), "inventory");
        ModelIdentifier maskModelFrame = new ModelIdentifier(new Identifier("mowziesmobs", "sol_visage_frame"), "inventory");
        bakeMask(map, maskModelInventory, maskModelFrame);
    }

    private static void bakeMask(Map<Identifier, BakedModel> map, ModelIdentifier maskModelInventory, ModelIdentifier maskModelFrame) {
        BakedModel maskBakedModelDefault = map.get(maskModelInventory);
        BakedModel maskBakedModelFrame = map.get(maskModelFrame);
        BakedModel maskModelWrapper = new BakedModel() {
            @Override
            public List<BakedQuad> getQuads(BlockState state, Direction side, Random rand) {
                return maskBakedModelDefault.getQuads(state, side, rand);
            }

            @Override
            public boolean useAmbientOcclusion() {
                return maskBakedModelDefault.useAmbientOcclusion();
            }

            @Override
            public boolean hasDepth() {
                return maskBakedModelDefault.hasDepth();
            }

            @Override
            public boolean isSideLit() {
                return false;
            }

            @Override
            public boolean isBuiltin() {
                return maskBakedModelDefault.isBuiltin();
            }

            @Override
            public Sprite getParticleSprite() {
                return maskBakedModelDefault.getParticleSprite();
            }

            @Override
            public ModelOverrideList getOverrides() {
                return maskBakedModelDefault.getOverrides();
            }

            @Override
            public BakedModel applyTransform(ModelTransformationMode cameraTransformType, MatrixStack mat, boolean applyLeftHandTransform) {
                BakedModel modelToUse = maskBakedModelDefault;
                if (cameraTransformType == ModelTransformationMode.FIXED) {
                    modelToUse = maskBakedModelFrame;
                }
                return ForgeHooksClient.handleCameraTransforms(mat, modelToUse, cameraTransformType, applyLeftHandTransform);
            }
        };

        map.put(maskModelInventory, maskModelWrapper);
    }
}
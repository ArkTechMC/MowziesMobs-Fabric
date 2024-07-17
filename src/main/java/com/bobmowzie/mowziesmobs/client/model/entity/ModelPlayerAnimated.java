package com.bobmowzie.mowziesmobs.client.model.entity;

import com.bobmowzie.mowziesmobs.client.model.tools.ModelPartMatrix;
import com.google.common.collect.ImmutableList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.random.Random;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class ModelPlayerAnimated<T extends LivingEntity> extends PlayerEntityModel<T> {
    private final List<ModelPart> parts;

    public ModelPlayerAnimated(ModelPart root, boolean smallArmsIn) {
        super(root, smallArmsIn);
        ModelPartMatrix bodyMatrix = new ModelPartMatrix(this.body, false);
        ModelPartMatrix headMatrix = new ModelPartMatrix(this.head, false);
        ModelPartMatrix rightArmMatrix = new ModelPartMatrix(this.rightArm, false);
        ModelPartMatrix leftArmMatrix = new ModelPartMatrix(this.leftArm, false);
        ModelPartMatrix rightLegMatrix = new ModelPartMatrix(this.rightLeg, false);
        ModelPartMatrix leftLegMatrix = new ModelPartMatrix(this.leftLeg, false);

        ModelPartMatrix hatMatrix = new ModelPartMatrix(this.hat, false);
        ModelPartMatrix jacketMatrix = new ModelPartMatrix(this.jacket, false);
        ModelPartMatrix leftSleeveMatrix = new ModelPartMatrix(this.leftSleeve, false);
        ModelPartMatrix rightSleeveMatrix = new ModelPartMatrix(this.rightSleeve, false);
        ModelPartMatrix leftPantsMatrix = new ModelPartMatrix(this.leftPants, false);
        ModelPartMatrix rightPantsMatrix = new ModelPartMatrix(this.rightPants, false);
        ModelPartMatrix earMatrix = new ModelPartMatrix(this.ear, false);

        Map<ModelPart, ModelPart> origToNew = new HashMap<>();
        origToNew.put(this.body, bodyMatrix);
        origToNew.put(this.head, headMatrix);
        origToNew.put(this.rightArm, rightArmMatrix);
        origToNew.put(this.leftArm, leftArmMatrix);
        origToNew.put(this.rightLeg, rightLegMatrix);
        origToNew.put(this.leftLeg, leftLegMatrix);

        origToNew.put(this.hat, hatMatrix);
        origToNew.put(this.jacket, jacketMatrix);
        origToNew.put(this.leftSleeve, leftSleeveMatrix);
        origToNew.put(this.rightSleeve, rightSleeveMatrix);
        origToNew.put(this.leftPants, leftPantsMatrix);
        origToNew.put(this.rightPants, rightPantsMatrix);
        origToNew.put(this.ear, earMatrix);

        this.body = bodyMatrix;
        this.head = headMatrix;
        this.rightArm = rightArmMatrix;
        this.leftArm = leftArmMatrix;
        this.rightLeg = rightLegMatrix;
        this.leftLeg = leftLegMatrix;

        this.hat = hatMatrix;
        this.jacket = jacketMatrix;
        this.leftSleeve = leftSleeveMatrix;
        this.rightSleeve = rightSleeveMatrix;
        this.leftPants = leftPantsMatrix;
        this.rightPants = rightPantsMatrix;
        this.ear = earMatrix;

        List<ModelPart> originalList = root.traverse().filter((p_170824_) -> !p_170824_.isEmpty()).collect(ImmutableList.toImmutableList());

        this.parts = new ArrayList<>();
        for (ModelPart origPart : originalList) {
            ModelPart newPart = origToNew.get(origPart);
            if (newPart != null) this.parts.add(newPart);
        }
    }

    public static void setUseMatrixMode(PlayerEntityModel<? extends LivingEntity> bipedModel, boolean useMatrixMode) {
        if (bipedModel.hat instanceof ModelPartMatrix) {
            ((ModelPartMatrix) bipedModel.jacket).setUseMatrixMode(useMatrixMode);
            ((ModelPartMatrix) bipedModel.leftPants).setUseMatrixMode(useMatrixMode);
            ((ModelPartMatrix) bipedModel.rightPants).setUseMatrixMode(useMatrixMode);
            ((ModelPartMatrix) bipedModel.rightSleeve).setUseMatrixMode(useMatrixMode);
            ((ModelPartMatrix) bipedModel.leftSleeve).setUseMatrixMode(useMatrixMode);
            ((ModelPartMatrix) bipedModel.ear).setUseMatrixMode(useMatrixMode);
        }
        ModelBipedAnimated.setUseMatrixMode(bipedModel, useMatrixMode);
    }

    public static void copyFromGeckoModel(PlayerEntityModel<?> playerModel, ModelGeckoPlayerThirdPerson geckoModel) {
        ModelBipedAnimated.copyFromGeckoModel(playerModel, geckoModel);

        if (playerModel.jacket instanceof ModelPartMatrix) {
            ((ModelPartMatrix) playerModel.jacket).setWorldXform(geckoModel.bipedBody().getWorldSpaceMatrix());
            ((ModelPartMatrix) playerModel.jacket).setWorldNormal(geckoModel.bipedBody().getWorldSpaceNormal());
        }

        if (playerModel.leftPants instanceof ModelPartMatrix) {
            ((ModelPartMatrix) playerModel.leftPants).setWorldXform(geckoModel.bipedLeftLeg().getWorldSpaceMatrix());
            ((ModelPartMatrix) playerModel.leftPants).setWorldNormal(geckoModel.bipedLeftLeg().getWorldSpaceNormal());
        }

        if (playerModel.rightPants instanceof ModelPartMatrix) {
            ((ModelPartMatrix) playerModel.rightPants).setWorldXform(geckoModel.bipedRightLeg().getWorldSpaceMatrix());
            ((ModelPartMatrix) playerModel.rightPants).setWorldNormal(geckoModel.bipedRightLeg().getWorldSpaceNormal());
        }

        if (playerModel.rightSleeve instanceof ModelPartMatrix) {
            ((ModelPartMatrix) playerModel.rightSleeve).setWorldXform(geckoModel.bipedRightArm().getWorldSpaceMatrix());
            ((ModelPartMatrix) playerModel.rightSleeve).setWorldNormal(geckoModel.bipedRightArm().getWorldSpaceNormal());
        }

        if (playerModel.leftSleeve instanceof ModelPartMatrix) {
            ((ModelPartMatrix) playerModel.leftSleeve).setWorldXform(geckoModel.bipedLeftArm().getWorldSpaceMatrix());
            ((ModelPartMatrix) playerModel.leftSleeve).setWorldNormal(geckoModel.bipedLeftArm().getWorldSpaceNormal());
        }

        if (playerModel.ear instanceof ModelPartMatrix) {
            ((ModelPartMatrix) playerModel.ear).setWorldXform(geckoModel.bipedHead().getWorldSpaceMatrix());
            ((ModelPartMatrix) playerModel.ear).setWorldNormal(geckoModel.bipedHead().getWorldSpaceNormal());
        }
    }

    @Override
    public void setAngles(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.leftPants.copyTransform(this.leftLeg);
        this.rightPants.copyTransform(this.rightLeg);
        this.leftSleeve.copyTransform(this.leftArm);
        this.rightSleeve.copyTransform(this.rightArm);
        this.jacket.copyTransform(this.body);
        this.hat.copyTransform(this.head);
        this.ear.copyTransform(this.head);
    }

    @Override
    public ModelPart getRandomPart(Random randomIn) {
        return this.parts.get(randomIn.nextInt(this.parts.size()));
    }

    @Override
    public void copyBipedStateTo(BipedEntityModel<T> modelIn) {
        modelIn.head = new ModelPartMatrix(modelIn.head);
        modelIn.hat = new ModelPartMatrix(modelIn.hat);
        modelIn.body = new ModelPartMatrix(modelIn.body);
        modelIn.leftArm = new ModelPartMatrix(modelIn.leftArm);
        modelIn.rightArm = new ModelPartMatrix(modelIn.rightArm);
        modelIn.leftLeg = new ModelPartMatrix(modelIn.leftLeg);
        modelIn.rightLeg = new ModelPartMatrix(modelIn.rightLeg);
        ModelBipedAnimated.setUseMatrixMode(modelIn, true);
        super.copyBipedStateTo(modelIn);
    }
}

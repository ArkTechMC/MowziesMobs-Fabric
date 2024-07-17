package com.bobmowzie.mowziesmobs.client.model.entity;

import com.bobmowzie.mowziesmobs.client.model.tools.ModelPartMatrix;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.LivingEntity;

public class ModelBipedAnimated<T extends LivingEntity> extends BipedEntityModel<T> {
    public ModelBipedAnimated(ModelPart root) {
        super(root);
        this.body = new ModelPartMatrix(this.body);
        this.head = new ModelPartMatrix(this.head);
        this.hat = new ModelPartMatrix(this.hat);
        this.rightArm = new ModelPartMatrix(this.rightArm);
        this.leftArm = new ModelPartMatrix(this.leftArm);
        this.rightLeg = new ModelPartMatrix(this.rightLeg);
        this.leftLeg = new ModelPartMatrix(this.leftLeg);
    }

    public static void copyFromGeckoModel(BipedEntityModel<?> bipedModel, ModelGeckoPlayerThirdPerson geckoModel) {
        if (bipedModel.body instanceof ModelPartMatrix) {
            ((ModelPartMatrix) bipedModel.body).setWorldXform(geckoModel.bipedBody().getWorldSpaceMatrix());
            ((ModelPartMatrix) bipedModel.body).setWorldNormal(geckoModel.bipedBody().getWorldSpaceNormal());
        }

        if (bipedModel.head instanceof ModelPartMatrix) {
            ((ModelPartMatrix) bipedModel.head).setWorldXform(geckoModel.bipedHead().getWorldSpaceMatrix());
            ((ModelPartMatrix) bipedModel.head).setWorldNormal(geckoModel.bipedHead().getWorldSpaceNormal());
        }

        if (bipedModel.hat instanceof ModelPartMatrix) {
            ((ModelPartMatrix) bipedModel.hat).setWorldXform(geckoModel.bipedHead().getWorldSpaceMatrix());
            ((ModelPartMatrix) bipedModel.hat).setWorldNormal(geckoModel.bipedHead().getWorldSpaceNormal());
        }

        if (bipedModel.leftLeg instanceof ModelPartMatrix) {
            ((ModelPartMatrix) bipedModel.leftLeg).setWorldXform(geckoModel.bipedLeftLeg().getWorldSpaceMatrix());
            ((ModelPartMatrix) bipedModel.leftLeg).setWorldNormal(geckoModel.bipedLeftLeg().getWorldSpaceNormal());
        }

        if (bipedModel.rightLeg instanceof ModelPartMatrix) {
            ((ModelPartMatrix) bipedModel.rightLeg).setWorldXform(geckoModel.bipedRightLeg().getWorldSpaceMatrix());
            ((ModelPartMatrix) bipedModel.rightLeg).setWorldNormal(geckoModel.bipedRightLeg().getWorldSpaceNormal());
        }

        if (bipedModel.rightArm instanceof ModelPartMatrix) {
            ((ModelPartMatrix) bipedModel.rightArm).setWorldXform(geckoModel.bipedRightArm().getWorldSpaceMatrix());
            ((ModelPartMatrix) bipedModel.rightArm).setWorldNormal(geckoModel.bipedRightArm().getWorldSpaceNormal());
        }

        if (bipedModel.leftArm instanceof ModelPartMatrix) {
            ((ModelPartMatrix) bipedModel.leftArm).setWorldXform(geckoModel.bipedLeftArm().getWorldSpaceMatrix());
            ((ModelPartMatrix) bipedModel.leftArm).setWorldNormal(geckoModel.bipedLeftArm().getWorldSpaceNormal());
        }
    }

    public static void setUseMatrixMode(BipedEntityModel<? extends LivingEntity> bipedModel, boolean useMatrixMode) {
        if (bipedModel.body instanceof ModelPartMatrix) {
            ((ModelPartMatrix) bipedModel.body).setUseMatrixMode(useMatrixMode);
            ((ModelPartMatrix) bipedModel.head).setUseMatrixMode(useMatrixMode);
            ((ModelPartMatrix) bipedModel.hat).setUseMatrixMode(useMatrixMode);
            ((ModelPartMatrix) bipedModel.leftLeg).setUseMatrixMode(useMatrixMode);
            ((ModelPartMatrix) bipedModel.rightLeg).setUseMatrixMode(useMatrixMode);
            ((ModelPartMatrix) bipedModel.rightArm).setUseMatrixMode(useMatrixMode);
            ((ModelPartMatrix) bipedModel.leftArm).setUseMatrixMode(useMatrixMode);
        }
    }
}

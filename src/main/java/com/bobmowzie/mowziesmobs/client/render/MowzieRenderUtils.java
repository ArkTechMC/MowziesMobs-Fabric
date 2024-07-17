package com.bobmowzie.mowziesmobs.client.render;

import com.bobmowzie.mowziesmobs.client.model.tools.MathUtils;
import com.bobmowzie.mowziesmobs.client.model.tools.ModelPartMatrix;
import com.bobmowzie.mowziesmobs.client.model.tools.AdvancedModelRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.cache.object.GeoCube;

public class MowzieRenderUtils {
    public static void matrixStackFromModel(MatrixStack matrixStack, AdvancedModelRenderer modelRenderer) {
        AdvancedModelRenderer parent = modelRenderer.getParent();
        if (parent != null) matrixStackFromModel(matrixStack, parent);
        modelRenderer.translateRotate(matrixStack);
    }

    public static Vec3d getWorldPosFromModel(Entity entity, float entityYaw, AdvancedModelRenderer modelRenderer) {
        MatrixStack matrixStack = new MatrixStack();
        matrixStack.translate(entity.getX(), entity.getY(), entity.getZ());
        matrixStack.multiply(MathUtils.quatFromRotationXYZ(0, -entityYaw + 180, 0, true));
        matrixStack.scale(-1, -1, 1);
        matrixStack.translate(0, -1.5f, 0);
        MowzieRenderUtils.matrixStackFromModel(matrixStack, modelRenderer);
        MatrixStack.Entry matrixEntry = matrixStack.peek();
        Matrix4f matrix4f = matrixEntry.getPositionMatrix();

        Vector4f vec = new Vector4f(0, 0, 0, 1);
        vec.mul(matrix4f);
        return new Vec3d(vec.x(), vec.y(), vec.z());
    }

    public static void translateRotateGeckolib(GeoBone bone, MatrixStack matrixStackIn) {
        matrixStackIn.translate(bone.getPivotX() / 16.0F, bone.getPivotY() / 16.0F, (double) (bone.getPivotZ() / 16.0F));
        if (bone.getRotZ() != 0.0F) {
            matrixStackIn.multiply(RotationAxis.POSITIVE_Z.rotation(bone.getRotZ()));
        }

        if (bone.getRotY() != 0.0F) {
            matrixStackIn.multiply(RotationAxis.POSITIVE_Y.rotation(bone.getRotY()));
        }

        if (bone.getRotX() != 0.0F) {
            matrixStackIn.multiply(RotationAxis.POSITIVE_X.rotation(bone.getRotX()));
        }

        matrixStackIn.scale(bone.getScaleX(), bone.getScaleY(), bone.getScaleZ());
    }

    public static void matrixStackFromModel(MatrixStack matrixStack, GeoBone geoBone) {
        GeoBone parent = geoBone.getParent();
        if (parent != null) matrixStackFromModel(matrixStack, parent);
        translateRotateGeckolib(geoBone, matrixStack);
    }

    public static Vec3d getWorldPosFromModel(Entity entity, float entityYaw, GeoBone geoBone) {
        MatrixStack matrixStack = new MatrixStack();
        matrixStack.translate(entity.getX(), entity.getY(), entity.getZ());
        matrixStack.multiply(MathUtils.quatFromRotationXYZ(0, -entityYaw + 180, 0, true));
        matrixStack.scale(-1, -1, 1);
        matrixStack.translate(0, -1.5f, 0);
        MowzieRenderUtils.matrixStackFromModel(matrixStack, geoBone);
        MatrixStack.Entry matrixEntry = matrixStack.peek();
        Matrix4f matrix4f = matrixEntry.getPositionMatrix();

        Vector4f vec = new Vector4f(0, 0, 0, 1);
        vec.mul(matrix4f);
        return new Vec3d(vec.x(), vec.y(), vec.z());
    }

    // Mirrored render utils
    public static void moveToPivotMirror(MatrixStack stack, GeoCube cube) {
        Vec3d pivot = cube.pivot();
        stack.translate(-pivot.getX() / 16.0F, pivot.getY() / 16.0F, pivot.getZ() / 16.0F);
    }

    public static void translateAwayFromPivotPointMirror(MatrixStack stack, GeoCube cube) {
        Vec3d pivot = cube.pivot();
        stack.translate(pivot.getX() / 16.0F, -pivot.getY() / 16.0F, -pivot.getZ() / 16.0F);
    }

    public static void moveToPivotMirror(MatrixStack stack, GeoBone bone) {
        stack.translate(-bone.getPivotX() / 16.0F, bone.getPivotY() / 16.0F, (double) (bone.getPivotZ() / 16.0F));
    }

    public static void translateAwayFromPivotPointMirror(MatrixStack stack, GeoBone bone) {
        stack.translate(bone.getPivotX() / 16.0F, -bone.getPivotY() / 16.0F, (double) (-bone.getPivotZ() / 16.0F));
    }

    public static void translateMirror(MatrixStack stack, GeoBone bone) {
        stack.translate(bone.getPosX() / 16.0F, bone.getPosY() / 16.0F, (double) (bone.getPosZ() / 16.0F));
    }

    public static void rotateMirror(MatrixStack stack, GeoBone bone) {
        if (bone.getRotZ() != 0.0F) {
            stack.multiply(RotationAxis.POSITIVE_Z.rotation(-bone.getRotZ()));
        }

        if (bone.getRotY() != 0.0F) {
            stack.multiply(RotationAxis.POSITIVE_Y.rotation(-bone.getRotY()));
        }

        if (bone.getRotX() != 0.0F) {
            stack.multiply(RotationAxis.POSITIVE_X.rotation(bone.getRotX()));
        }

    }

    // Used for elytra layer, parrot layer, cape layer
    public static void transformStackToModelPart(MatrixStack stack, ModelPartMatrix part) {
        stack.peek().getPositionMatrix().identity();
        stack.peek().getNormalMatrix().identity();
        stack.push();
        stack.peek().getPositionMatrix().mul(part.getWorldXform());
        stack.peek().getNormalMatrix().mul(part.getWorldNormal());
    }
}

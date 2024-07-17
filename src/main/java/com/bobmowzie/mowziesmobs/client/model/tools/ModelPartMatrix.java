package com.bobmowzie.mowziesmobs.client.model.tools;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class ModelPartMatrix extends ModelPart {
    private Matrix4f worldXform;
    private Matrix3f worldNormal;

    private final boolean resetUseMatrixMode;
    private boolean useMatrixMode;

    // For debugging
    private String name;

    public ModelPartMatrix(ModelPart original) {
        this(original, true);
    }

    public ModelPartMatrix(ModelPart original, boolean resetUseMatrixMode) {
        super(original.cuboids, original.children);
        this.copyTransform(original);

        this.worldNormal = new Matrix3f();
        this.worldNormal.identity();
        this.worldXform = new Matrix4f();
        this.worldXform.identity();

        this.useMatrixMode = true;
        this.resetUseMatrixMode = resetUseMatrixMode;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void rotate(MatrixStack matrixStackIn) {
        if (!this.useMatrixMode || this.getWorldNormal() == null || this.getWorldXform() == null) {
            super.rotate(matrixStackIn);
        } else {
            MatrixStack.Entry last = matrixStackIn.peek();
            last.getPositionMatrix().identity();
            last.getNormalMatrix().identity();
            last.getPositionMatrix().mul(this.getWorldXform());
            last.getNormalMatrix().mul(this.getWorldNormal());
        }
        if (this.resetUseMatrixMode) this.useMatrixMode = false;
    }

    @Override
    public void copyTransform(ModelPart modelRendererIn) {
        if (modelRendererIn instanceof ModelPartMatrix other) {
            this.setWorldNormal(other.getWorldNormal());
            this.setWorldXform(other.getWorldXform());
        }
        super.copyTransform(modelRendererIn);
    }

    public Matrix3f getWorldNormal() {
        return this.worldNormal;
    }

    public void setWorldNormal(Matrix3f worldNormal) {
        this.worldNormal = worldNormal;
    }

    public Matrix4f getWorldXform() {
        return this.worldXform;
    }

    public void setWorldXform(Matrix4f worldXform) {
        this.worldXform = worldXform;
    }

    public boolean isUseMatrixMode() {
        return this.useMatrixMode;
    }

    public void setUseMatrixMode(boolean useMatrixMode) {
        this.useMatrixMode = useMatrixMode;
    }
}

package com.bobmowzie.mowziesmobs.client.model.tools.geckolib;

import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Vector3d;
import software.bernie.geckolib.cache.object.GeoBone;

public class MowzieGeoBone extends GeoBone {

    public Matrix4f rotMat;
    protected boolean forceMatrixTransform = false;

    public MowzieGeoBone(@Nullable GeoBone parent, String name, Boolean mirror, @Nullable Double inflate, @Nullable Boolean dontRender, @Nullable Boolean reset) {
        super(parent, name, mirror, inflate, dontRender, reset);
        this.rotMat = null;
    }

    public static void removeMatrixTranslation(Matrix4f matrix) {
        matrix.m30(0);
        matrix.m31(0);
        matrix.m32(0);
    }

    public MowzieGeoBone getParent() {
        return (MowzieGeoBone) super.getParent();
    }

    // Position utils
    public void addPos(Vec3d vec) {
        this.addPos((float) vec.getX(), (float) vec.getY(), (float) vec.getZ());
    }

    public void addPos(float x, float y, float z) {
        this.addPosX(x);
        this.addPosY(y);
        this.addPosZ(z);
    }

    public void addPosX(float x) {
        this.setPosX(this.getPosX() + x);
    }

    public void addPosY(float y) {
        this.setPosY(this.getPosY() + y);
    }

    public void addPosZ(float z) {
        this.setPosZ(this.getPosZ() + z);
    }

    public void setPos(float x, float y, float z) {
        this.setPosX(x);
        this.setPosY(y);
        this.setPosZ(z);
    }

    public Vector3d getPos() {
        return new Vector3d(this.getPosX(), this.getPosY(), this.getPosZ());
    }

    public void setPos(Vec3d vec) {
        this.setPos((float) vec.getX(), (float) vec.getY(), (float) vec.getZ());
    }

    // Rotation utils
    public void addRot(Vec3d vec) {
        this.addRot((float) vec.getX(), (float) vec.getY(), (float) vec.getZ());
    }

    public void addRot(float x, float y, float z) {
        this.addRotX(x);
        this.addRotY(y);
        this.addRotZ(z);
    }

    public void addRotX(float x) {
        this.setRotX(this.getRotX() + x);
    }

    public void addRotY(float y) {
        this.setRotY(this.getRotY() + y);
    }

    public void addRotZ(float z) {
        this.setRotZ(this.getRotZ() + z);
    }

    public void setRot(float x, float y, float z) {
        this.setRotX(x);
        this.setRotY(y);
        this.setRotZ(z);
    }

    public Vector3d getRot() {
        return new Vector3d(this.getRotX(), this.getRotY(), this.getRotZ());
    }

    public void setRot(Vector3d vec) {
        this.setRot((float) vec.x(), (float) vec.y(), (float) vec.z());
    }

    public void setRot(Vec3d vec) {
        this.setRot((float) vec.getX(), (float) vec.getY(), (float) vec.getZ());
    }

    // Scale utils
    public void multiplyScale(Vec3d vec) {
        this.multiplyScale((float) vec.getX(), (float) vec.getY(), (float) vec.getZ());
    }

    public void multiplyScale(float x, float y, float z) {
        this.setScaleX(this.getScaleX() * x);
        this.setScaleY(this.getScaleY() * y);
        this.setScaleZ(this.getScaleZ() * z);
    }

    public void setScale(float x, float y, float z) {
        this.setScaleX(x);
        this.setScaleY(y);
        this.setScaleZ(z);
    }

    public Vector3d getScale() {
        return new Vector3d(this.getScaleX(), this.getScaleY(), this.getScaleZ());
    }

    public void setScale(Vec3d vec) {
        this.setScale((float) vec.getX(), (float) vec.getY(), (float) vec.getZ());
    }

    public void setScale(float scale) {
        this.setScale(scale, scale, scale);
    }

    public void addRotationOffsetFromBone(MowzieGeoBone source) {
        this.setRotX(this.getRotX() + source.getRotX() - source.getInitialSnapshot().getRotX());
        this.setRotY(this.getRotY() + source.getRotY() - source.getInitialSnapshot().getRotY());
        this.setRotZ(this.getRotZ() + source.getRotZ() - source.getInitialSnapshot().getRotZ());
    }

    public boolean isForceMatrixTransform() {
        return this.forceMatrixTransform;
    }

    public void setForceMatrixTransform(boolean forceMatrixTransform) {
        this.forceMatrixTransform = forceMatrixTransform;
    }

    public Matrix4f getModelRotationMat() {
        Matrix4f matrix = new Matrix4f(this.getModelSpaceMatrix());
        removeMatrixTranslation(matrix);
        return matrix;
    }

    public void setModelRotationMat(Matrix4f mat) {
        this.rotMat = mat;
    }
}
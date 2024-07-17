package com.bobmowzie.mowziesmobs.client.model.tools;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import software.bernie.geckolib.cache.object.GeoBone;

public class RigUtils {
    public static Vec3d lerp(Vec3d v, Vec3d u, float alpha) {
        return new Vec3d(
                MathHelper.lerp(alpha, (float) v.getX(), (float) u.getX()),
                MathHelper.lerp(alpha, (float) v.getY(), (float) u.getY()),
                MathHelper.lerp(alpha, (float) v.getZ(), (float) u.getZ())
        );
    }

    public static Vec3d lerpAngles(Vec3d v, Vec3d u, float alpha) {
        return new Vec3d(
                Math.toRadians(MathHelper.lerpAngleDegrees(alpha, (float) Math.toDegrees(v.getX()), (float) Math.toDegrees(u.getX()))),
                Math.toRadians(MathHelper.lerpAngleDegrees(alpha, (float) Math.toDegrees(v.getY()), (float) Math.toDegrees(u.getY()))),
                Math.toRadians(MathHelper.lerpAngleDegrees(alpha, (float) Math.toDegrees(v.getZ()), (float) Math.toDegrees(u.getZ())))
        );
    }

    public static Vec3d blendAngles(Vec3d v, Vec3d u, float alpha) {
        return new Vec3d(
                Math.toRadians(MathHelper.wrapDegrees(Math.toDegrees(v.getX()) * alpha + Math.toDegrees(u.getX()))),
                Math.toRadians(MathHelper.wrapDegrees(Math.toDegrees(v.getY()) * alpha + Math.toDegrees(u.getY()))),
                Math.toRadians(MathHelper.wrapDegrees(Math.toDegrees(v.getZ()) * alpha + Math.toDegrees(u.getZ())))
        );
    }

    public static Quaternionf matrixToQuaternion(Matrix3f matrix) {
        double tr = matrix.m00() + matrix.m11() + matrix.m22();
        double qw = 0;
        double qx = 0;
        double qy = 0;
        double qz = 0;

        if (tr > 0) {
            double S = Math.sqrt(tr + 1.0) * 2; // S=4*qw
            qw = 0.25 * S;
            qx = (matrix.m21() - matrix.m12()) / S;
            qy = (matrix.m02() - matrix.m20()) / S;
            qz = (matrix.m10() - matrix.m01()) / S;
        } else if ((matrix.m00() > matrix.m11()) & (matrix.m00() > matrix.m22())) {
            double S = Math.sqrt(1.0 + matrix.m00() - matrix.m11() - matrix.m22()) * 2; // S=4*qx
            qw = (matrix.m21() - matrix.m12()) / S;
            qx = 0.25 * S;
            qy = (matrix.m01() + matrix.m10()) / S;
            qz = (matrix.m02() + matrix.m20()) / S;
        } else if (matrix.m11() > matrix.m22()) {
            double S = Math.sqrt(1.0 + matrix.m11() - matrix.m00() - matrix.m22()) * 2; // S=4*qy
            qw = (matrix.m02() - matrix.m20()) / S;
            qx = (matrix.m01() + matrix.m10()) / S;
            qy = 0.25 * S;
            qz = (matrix.m12() + matrix.m21()) / S;
        } else {
            double S = Math.sqrt(1.0 + matrix.m22() - matrix.m00() - matrix.m11()) * 2; // S=4*qz
            qw = (matrix.m10() - matrix.m01()) / S;
            qx = (matrix.m02() + matrix.m20()) / S;
            qy = (matrix.m12() + matrix.m21()) / S;
            qz = 0.25 * S;
        }

        return new Quaternionf((float) qw, (float) qx, (float) qy, (float) qz);
    }

    public static void removeMatrixRotation(Matrix4f matrix) {
        matrix.m00(1);
        matrix.m11(1);
        matrix.m22(1);
        matrix.m01(0);
        matrix.m02(0);
        matrix.m10(0);
        matrix.m12(0);
        matrix.m20(0);
        matrix.m21(0);
    }

    public static void removeMatrixTranslation(Matrix4f matrix) {
        matrix.m03(0);
        matrix.m13(0);
        matrix.m23(0);
    }

    public static Quaternionf betweenVectors(Vec3d u, Vec3d v) {
        Vec3d a = u.crossProduct(v);
        float w = (float) (Math.sqrt(u.lengthSquared() * v.lengthSquared()) + u.dotProduct(v));
        Quaternionf q = new Quaternionf((float) a.getX(), -(float) a.getY(), -(float) a.getZ(), w);
        q.normalize();
        return q;
    }

    public static Vector3f translationFromMatrix(Matrix4f matrix4f) {
        return new Vector3f(matrix4f.m03(), matrix4f.m13(), matrix4f.m23());
    }

    public static Vector3f eulerAnglesZYXFromMatrix(Matrix4f matrix4f) {
        // From https://www.geometrictools.com/Documentation/EulerAngles.pdf
        float thetaZ;
        float thetaY;
        float thetaX;
        if (matrix4f.m20() < 1f) {
            if (matrix4f.m20() > -1f) {
                thetaY = (float) Math.asin(-matrix4f.m20());
                thetaZ = (float) Math.atan2(matrix4f.m10(), matrix4f.m00());
                thetaX = (float) Math.atan2(matrix4f.m21(), matrix4f.m22());
            } else { // m20 = −1
                thetaY = (float) (Math.PI / 2f);
                thetaZ = -(float) Math.atan2(-matrix4f.m12(), matrix4f.m11());
                thetaX = 0;
            }
        } else { // m20 = +1
            thetaY = -(float) (Math.PI / 2f);
            thetaZ = (float) Math.atan2(-matrix4f.m12(), matrix4f.m11());
            thetaX = 0;
        }
        return new Vector3f(thetaX, thetaY, thetaZ);
    }

    public static Vector3f eulerAnglesXYZFromMatrix(Matrix4f matrix4f) {
        // From https://www.geometrictools.com/Documentation/EulerAngles.pdf
        float thetaZ;
        float thetaY;
        float thetaX;
        if (matrix4f.m20() < 1f) {
            if (matrix4f.m20() > -1f) {
                thetaY = (float) Math.asin(matrix4f.m02());
                thetaX = (float) Math.atan2(-matrix4f.m12(), matrix4f.m22());
                thetaZ = (float) Math.atan2(-matrix4f.m01(), matrix4f.m00());
            } else { // m20 = −1
                thetaY = -(float) (Math.PI / 2f);
                thetaX = -(float) Math.atan2(matrix4f.m10(), matrix4f.m11());
                thetaZ = 0;
            }
        } else { // m20 = +1
            thetaY = (float) (Math.PI / 2f);
            thetaX = (float) Math.atan2(matrix4f.m10(), matrix4f.m11());
            thetaZ = 0;
        }
        return new Vector3f(thetaX, thetaY, thetaZ);
    }

    public static class BoneTransform {
        private final Vec3d translation;
        private final Vec3d rotation;
        private final Vec3d scale;

        public BoneTransform(
                double tx, double ty, double tz,
                double rx, double ry, double rz,
                double sx, double sy, double sz
        ) {
            this.translation = new Vec3d(tx, ty, tz);
            this.rotation = new Vec3d(rx, ry, rz);
            this.scale = new Vec3d(sx, sy, sz);
        }

        public BoneTransform(Vec3d t, Vec3d r, Vec3d s) {
            this.translation = t;
            this.rotation = r;
            this.scale = s;
        }

        public BoneTransform blend(BoneTransform other, float alpha) {
            return new BoneTransform(
                    this.translation.multiply(alpha).add(other.translation),
                    RigUtils.blendAngles(this.rotation, other.rotation, alpha),
                    this.scale.multiply(alpha).add(other.scale)
            );
        }

        public void apply(GeoBone bone) {
            this.apply(bone, false);
        }

        public void apply(GeoBone bone, boolean mirrorX) {
            float mirror = mirrorX ? -1 : 1;
            bone.setPosX(bone.getPosX() + mirror * (float) this.translation.getX());
            bone.setPosY(bone.getPosY() + (float) this.translation.getY());
            bone.setPosZ(bone.getPosZ() + (float) this.translation.getZ());

            bone.setRotX(bone.getRotX() + (float) this.rotation.getX());
            bone.setRotY(bone.getRotY() + mirror * (float) this.rotation.getY());
            bone.setRotZ(bone.getRotZ() + mirror * (float) this.rotation.getZ());

            bone.setScaleX(bone.getScaleX() * (float) this.scale.getX());
            bone.setScaleY(bone.getScaleY() * (float) this.scale.getY());
            bone.setScaleZ(bone.getScaleZ() * (float) this.scale.getZ());
        }
    }

    public static class BlendShape3DEntry {
        private final BoneTransform transform;
        private final Vec3d direction;
        private final float power;

        public BlendShape3DEntry(BoneTransform transform, Vec3d direction, float power) {
            this.transform = transform;
            this.direction = direction.normalize();
            this.power = power;
        }

        public double getWeight(Vec3d dir) {
            double dot = dir.normalize().dotProduct(this.direction.normalize());
            dot = Math.max(dot, 0);
            dot = Math.pow(dot, 0.01 * this.power);
            return dot;
        }

        public BoneTransform blend(BoneTransform other, float alpha) {
            return this.transform.blend(other, alpha);
        }
    }

    public static class BlendShape3D {
        private final BlendShape3DEntry[] entries;

        public BlendShape3D(BlendShape3DEntry[] entries) {
            this.entries = entries;
        }

        public void evaluate(GeoBone bone, Vec3d dir) {
            this.evaluate(bone, dir, false);
        }

        private double[] getWeights(Vec3d dir) {
            double[] weights = new double[this.entries.length];
            double[] dotProducts = new double[this.entries.length];
            double totalDotProduct = 0.0;
            for (int i = 0; i < this.entries.length; i++) {
                BlendShape3DEntry entry = this.entries[i];
                double dot = 1.0 - entry.getWeight(dir);
                if (dot > 0.0) {
                    totalDotProduct += 1.0 / dot;
                    dotProducts[i] = dot;
                } else {
                    weights[i] = 1.0;
                    return weights;
                }
            }
            for (int i = 0; i < this.entries.length; i++) {
                double dot_prod = totalDotProduct * dotProducts[i];
                if (dot_prod > 0) weights[i] = 1 / dot_prod;
                else weights[i] = 0.0;
            }
            return weights;
        }

        private double[] getWeightsGradientBand(Vec3d dir) {
            double[] weights = new double[this.entries.length];
            double[] sqrdDistances = new double[this.entries.length];
            double[] angularDistances = new double[this.entries.length];
            double totalSqrdDistance = 0.0;
            double totalAngularDistance = 0.0;
            for (int i = 0; i < this.entries.length; i++) {
                BlendShape3DEntry entry = this.entries[i];
                double sqrdDistance = dir.subtract(entry.direction).dotProduct(dir.subtract(entry.direction));
                if (sqrdDistance > 0.0) {
                    double angularDistance = -(MathHelper.clamp(dir.dotProduct(entry.direction), -1, 1) - 1) * 0.5;
                    totalSqrdDistance += 1.0 / sqrdDistance;
                    if (angularDistance > 0) totalAngularDistance += 1.0 / angularDistance;
                    sqrdDistances[i] = sqrdDistance;
                    angularDistances[i] = angularDistance;
                } else {
                    weights[i] = 1.0;
                    return weights;
                }
            }
            for (int i = 0; i < this.entries.length; i++) {
                double sqrdDistance = totalSqrdDistance * sqrdDistances[i];
                double angularDistance = totalAngularDistance * angularDistances[i];
                if (sqrdDistance > 0.0 && angularDistance > 0.0)
                    weights[i] = (1.0 / sqrdDistance) * 0.5 + (1.0 / angularDistance) * 0.5;
                else if (sqrdDistance > 0.0)
                    weights[i] = (1 / sqrdDistance) * 0.5 + 0.5;
                else weights[i] = 0.0;
            }
            return weights;
        }

        public void evaluate(GeoBone bone, Vec3d d, boolean mirrorX) {
            Vec3d dir = mirrorX ? d.multiply(-1, 1, 1) : d;
            dir = dir.normalize();

            double[] weights = this.getWeights(dir);
            BoneTransform transform = new BoneTransform(
                    0.0, 0.0, 0.0,
                    0.0, 0.0, 0.0,
                    0.0, 0.0, 0.0
            );
            for (int i = 0; i < this.entries.length; i++) {
                BlendShape3DEntry entry = this.entries[i];
                transform = entry.blend(transform, (float) MathHelper.clamp(weights[i], 0, 1));
            }
            transform.apply(bone, mirrorX);
        }
    }
}

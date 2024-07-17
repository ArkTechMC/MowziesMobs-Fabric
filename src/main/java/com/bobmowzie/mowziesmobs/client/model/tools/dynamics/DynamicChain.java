package com.bobmowzie.mowziesmobs.client.model.tools.dynamics;

import com.bobmowzie.mowziesmobs.client.model.tools.AdvancedModelRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

/**
 * Created by BobMowzie on 8/30/2018.
 */
public class DynamicChain {
    private final Entity entity;
    private Vec3d[] p;
    private Vec3d[] v;
    private Vec3d[] a;
    private Vec3d[] F;
    private float[] m;
    private float[] d;
    private Vec3d[] T;
    private Vec3d[] r;
    private Vec3d[] rv;
    private Vec3d[] ra;
    private Vec3d prevP;
    private Vec3d prevV;

    private Vec3d[] pOrig;
    private int prevUpdateTick;

    public DynamicChain(Entity entity) {
        this.entity = entity;
        this.p = new Vec3d[0];
        this.v = new Vec3d[0];
        this.a = new Vec3d[0];
        this.F = new Vec3d[0];
        this.m = new float[0];
        this.d = new float[0];
        this.T = new Vec3d[0];
        this.ra = new Vec3d[0];
        this.rv = new Vec3d[0];
        this.r = new Vec3d[0];
        this.pOrig = new Vec3d[0];
        this.prevUpdateTick = -1;
    }

    private static Vec3d fromPitchYaw(float pitch, float yaw) {
        float f = MathHelper.cos(-yaw - (float) Math.PI);
        float f1 = MathHelper.sin(-yaw - (float) Math.PI);
        float f2 = -MathHelper.cos(-pitch);
        float f3 = MathHelper.sin(-pitch);
        return new Vec3d(f1 * f2, f3, f * f2);
    }

    private static Vec3d angleBetween(Vec3d p1, Vec3d p2) {
//        Quaternion q = new Quaternion();
//        Vector3d v1 = p2.subtract(p1);
//        Vector3d v2 = new Vector3d(1, 0, 0);
//        Vector3d a = v1.crossProduct(v2);
//        q.setX((float) a.x);
//        q.setY((float) a.y);
//        q.setZ((float) a.z);
//        q.setW((float)(Math.sqrt(Math.pow(v1.length(), 2) * Math.pow(v2.length(), 2)) + v1.dotProduct(v2)));
//        return q;

        float dz = (float) (p2.z - p1.z);
        float dx = (float) (p2.x - p1.x);
        float dy = (float) (p2.y - p1.y);

        float yaw = (float) MathHelper.atan2(dz, dx);
        float pitch = (float) MathHelper.atan2(Math.sqrt(dz * dz + dx * dx), dy);
        return wrapAngles(new Vec3d(yaw, pitch, 0));

//        Vector3d vec1 = p2.subtract(p1);
//        Vector3d vec2 = new Vector3d(1, 0, 0);
//        Vector3d vec1YawCalc = new Vector3d(vec1.x, vec2.y, vec1.z);
//        Vector3d vec1PitchCalc = new Vector3d(vec2.x, vec1.y, vec2.z);
//        float yaw = (float) Math.acos((vec1YawCalc.dotProduct(vec2))/(vec1YawCalc.length() * vec2.length()));
//        float pitch = (float) Math.acos((vec1PitchCalc.dotProduct(vec2))/(vec1PitchCalc.length() * vec2.length()));
//        return new Vector3d(yaw, pitch, 0);

//        Vector3d vec1 = p2.subtract(p1).normalize();
//        Vector3d vec2 = new Vector3d(0, 0, -1);
//        return toEuler(vec1.crossProduct(vec2).normalize(), Math.acos(vec1.dotProduct(vec2)/(vec1.length() * vec2.length())));

//        Vector3d vec1 = p2.subtract(p1);
//        Vector3d vec2 = new Vector3d(0, 0, -1);
//        Vector3d vec1XY = new Vector3d(vec1.x, vec1.y, 0);
//        Vector3d vec2XY = new Vector3d(vec2.x, vec2.y, 0);
//        Vector3d vec1XZ = new Vector3d(vec1.x, 0, vec2.z);
//        Vector3d vec2XZ = new Vector3d(vec2.x, 0, vec2.z);
//        Vector3d vec1YZ = new Vector3d(0, vec1.y, vec2.z);
//        Vector3d vec2YZ = new Vector3d(0, vec2.y, vec2.z);
//        double yaw = Math.acos(vec1XZ.dotProduct(vec2XZ));
//        double pitch = Math.acos(vec1YZ.dotProduct(vec2YZ));
//        double roll = Math.acos(vec1XY.dotProduct(vec2XY));
//        return new Vector3d(yaw - Math.PI/2, pitch + Math.PI/2, 0);

//        return toPitchYaw(p1.subtract(p2).normalize());
    }

    public static Vec3d toPitchYaw(Vec3d vector) {
//        float f = MathHelper.cos(-p_189986_1_ * 0.017453292F - (float)Math.PI);
//        float f1 = MathHelper.sin(-p_189986_1_ * 0.017453292F - (float)Math.PI);
//        float f2 = -MathHelper.cos(-p_189986_0_ * 0.017453292F);
//        float f3 = MathHelper.sin(-p_189986_0_ * 0.017453292F);
//        return new Vector3d((double)(f1 * f2), (double)f3, (double)(f * f2));

        double f3 = vector.y;
        double pitch = -Math.asin(f3);
        double f2 = -Math.cos(pitch);
//        if (Math.abs(f2) < 0.0001) return new Vector3d(0, pitch, 0);
        double f1 = vector.x / f2;
        double yaw = -Math.asin(f1) + Math.PI / 2;

        return wrapAngles(new Vec3d(yaw, pitch, 0));
    }

    private static Vec3d toEuler(Vec3d axis, double angle) {
        //System.out.println(axis + ", " + angle);
        double s = Math.sin(angle);
        double c = Math.cos(angle);
        double t = 1 - c;

        double yaw = 0;
        double pitch = 0;
        double roll = 0;

        double x = axis.x;
        double y = axis.y;
        double z = axis.z;

        if ((x * y * t + z * s) > 0.998) { // north pole singularity detected
            yaw = 2 * Math.atan2(x * Math.sin(angle / 2), Math.cos(angle / 2));
            pitch = Math.PI / 2;
            roll = 0;
        } else if ((x * y * t + z * s) < -0.998) { // south pole singularity detected
            yaw = -2 * Math.atan2(x * Math.sin(angle / 2), Math.cos(angle / 2));
            pitch = -Math.PI / 2;
            roll = 0;
        } else {
            yaw = Math.atan2(y * s - x * z * t, 1 - (y * y + z * z) * t);
            pitch = Math.asin(x * y * t + z * s);
            roll = Math.atan2(x * s - y * z * t, 1 - (x * x + z * z) * t);
        }

        return new Vec3d(yaw, pitch, roll);
    }

    private static Vec3d wrapAngles(Vec3d r) {
//        double x = Math.toRadians(MathHelper.wrapDegrees(Math.toDegrees(r.x)));
//        double y = Math.toRadians(MathHelper.wrapDegrees(Math.toDegrees(r.y)));
//        double z = Math.toRadians(MathHelper.wrapDegrees(Math.toDegrees(r.z)));

        double x = r.x;
        double y = r.y;
        double z = r.z;

        while (x > Math.PI) x -= 2 * Math.PI;
        while (x < -Math.PI) x += 2 * Math.PI;

        while (y > Math.PI) y -= 2 * Math.PI;
        while (y < -Math.PI) y += 2 * Math.PI;

        while (z > Math.PI) z -= 2 * Math.PI;
        while (z < -Math.PI) z += 2 * Math.PI;

        return new Vec3d(x, y, z);
    }

    private static Vec3d multiply(Vec3d u, Vec3d v, boolean preserveDir) {
        if (preserveDir) {
            return new Vec3d(u.x * Math.abs(v.x), u.y * Math.abs(v.y), u.z * Math.abs(v.z));
        }
        return new Vec3d(u.x * v.x, u.y * v.y, u.z * v.z);
    }

    public void updateBendConstraint(float gravityAmount, float stiffness, float stiffnessFalloff, float damping, int numUpdates, boolean useFloor) {
        Vec3d[] prevPos = new Vec3d[this.p.length];
        Vec3d[] prevVel = new Vec3d[this.v.length];
        for (int i = 0; i < this.p.length; i++) {
            prevPos[i] = this.p[i];
            prevVel[i] = this.v[i];
        }
        for (int j = 0; j < numUpdates; j++) {
            for (int i = 0; i < this.p.length - 1; i++) {
                if (i == 0) {
                    Vec3d root = this.pOrig[i]; //origModelRenderers[i].getWorldPos(entity, LLibrary.PROXY.getPartialTicks());

                    this.p[i] = root;
                    this.v[i] = this.p[i].subtract(prevPos[i]);
                    this.a[i] = this.v[i].subtract(prevVel[i]);
                }

                Vec3d target = angleBetween(
                        this.pOrig[i], //origModelRenderers[i].getWorldPos(entity, LLibrary.PROXY.getPartialTicks()),
                        this.pOrig[i + 1]); //origModelRenderers[i + 1].getWorldPos(entity, LLibrary.PROXY.getPartialTicks()));
                //float gravity = 1 - (float) Math.pow(1 - gravityAmount, (i + 1));
                //target = new Vector3d(target.x, (1-gravity) * target.y + gravity * Math.PI, target.z);

                this.r[i] = angleBetween(this.p[i], this.p[i + 1]);

                this.T[i] = wrapAngles(this.r[i].subtract(target)).multiply(-stiffness / (Math.pow(i + 1, stiffnessFalloff)));
                double down = Math.PI / 2;
                Vec3d gravityVec = wrapAngles(new Vec3d(
                        0,
                        gravityAmount * this.d[i + 1] * this.m[i + 1] * (Math.sin(down - this.r[i].y + down)),
                        0));
                Vec3d floorVec = new Vec3d(0, 1 * this.d[i + 1] * this.m[i + 1] * (Math.sin(Math.PI / 2 - this.r[i].y + Math.PI / 2)), 0);
                if (useFloor && this.entity.isOnGround() && this.p[i + 1].y < this.entity.getY()) {
                    this.T[i] = this.T[i].subtract(floorVec);
                }
                this.T[i] = wrapAngles(this.T[i].add(gravityVec));
                this.ra[i] = this.T[i].multiply(1 / (this.m[i + 1] * this.d[i + 1] * this.d[i + 1]));
                this.rv[i] = this.rv[i].add(this.ra[i].multiply(1 / ((float) numUpdates))).multiply(damping);
                this.rv[i] = wrapAngles(this.rv[i]);
                this.r[i] = this.r[i].add(this.rv[i].multiply(1 / ((float) numUpdates)));
                this.r[i] = wrapAngles(this.r[i]);

                this.p[i + 1] = fromPitchYaw((float) (this.r[i].y - Math.PI / 2), (float) (this.r[i].x - Math.PI / 2)).multiply(this.d[i + 1]).add(this.p[i]);
                this.v[i + 1] = this.p[i + 1].subtract(prevPos[i + 1]);
                this.a[i + 1] = this.v[i + 1].subtract(prevVel[i + 1]);
            }
        }
    }

    public void updateSpringConstraint(float gravityAmount, float dampAmount, float stiffness, float maxForce, boolean doAttract, float attractFalloff, int numUpdates) {
        for (int j = 0; j < numUpdates; j++) {
            for (int i = 0; i < this.p.length; i++) {
                this.a[i] = this.F[i].multiply(1 / this.m[i]);
                this.v[i] = this.v[i].add(this.a[i].multiply(1 / ((float) numUpdates)));
                this.p[i] = this.p[i].add(this.v[i].multiply(1 / ((float) numUpdates)));

                Vec3d disp;
                if (i == 0) {
                    Vec3d root = this.pOrig[i]; //origModelRenderers[i].getWorldPos(entity, LLibrary.PROXY.getPartialTicks());
                    disp = this.p[i].subtract(root);
                } else {
                    disp = this.p[i].subtract(this.p[i - 1]);
                }
                disp = disp.normalize().multiply(disp.length() - this.d[i]);
                Vec3d damp = this.v[i].multiply(dampAmount);
                Vec3d gravity = new Vec3d(0, -gravityAmount, 0);
                Vec3d attract = this.pOrig[0].subtract(this.p[i]); //origModelRenderers[i].getWorldPos(entity, LLibrary.PROXY.getPartialTicks()).subtract(p[i]);
                this.F[i] = disp.multiply(-stiffness * (disp.length())).add(gravity.multiply(this.m[i])).subtract(damp);
                if (i == 0 || doAttract) {
                    this.F[i] = this.F[i].add(attract.multiply(1 / (1 + i * i * attractFalloff)));
                }
                if (this.F[i].length() > maxForce) this.F[i].normalize().multiply(maxForce);

//            if (disp.length() > 0 && F[i].dotProduct(disp) > 0) {
//                Vector3d antiStretch = disp.normalize().scale(1.5 * F[i].dotProduct(disp) / (disp.length()));
//                F[i] = F[i].add(antiStretch);
//                System.out.println(antiStretch);
//            }
            }
        }
    }

    public void setChain(AdvancedModelRenderer[] chainOrig, AdvancedModelRenderer[] chainDynamic) {
        this.p = new Vec3d[chainOrig.length];
        this.v = new Vec3d[chainOrig.length];
        this.a = new Vec3d[chainOrig.length];
        this.F = new Vec3d[chainOrig.length];
        this.m = new float[chainOrig.length];
        this.d = new float[chainOrig.length];
        this.T = new Vec3d[chainOrig.length];
        this.r = new Vec3d[chainOrig.length];
        this.rv = new Vec3d[chainOrig.length];
        this.ra = new Vec3d[chainOrig.length];
        this.pOrig = new Vec3d[chainOrig.length];
        for (int i = 0; i < chainOrig.length; i++) {
            this.pOrig[i] = chainOrig[i].getWorldPos(this.entity, 0);
            this.p[i] = this.pOrig[i];
            this.v[i] = new Vec3d(0, 0, 0);
            this.a[i] = new Vec3d(0, 0, 0);
            this.F[i] = new Vec3d(0, 0, 0);
            this.T[i] = new Vec3d(0, 0, 0);
            this.r[i] = new Vec3d(0, 0, 0);
            this.rv[i] = new Vec3d(0, 0, 0);
            this.ra[i] = new Vec3d(0, 0, 0);
            this.m[i] = 0.5f + 0.5f / (i + 1);
            if (i > 0) {
                this.d[i] = (float) this.p[i].distanceTo(this.p[i - 1]);
            } else {
                this.d[i] = 1f;
            }
            chainOrig[i].setIsHidden(true);
        }

        for (int i = 0; i < chainOrig.length - 1; i++) {
            this.r[i] = angleBetween(this.p[i], this.p[i + 1]);
        }

        this.prevP = this.p[0];
        this.prevV = this.v[0];

        for (int i = 0; i < chainOrig.length; i++) {
            if (chainDynamic[i] == null) {
                chainDynamic[i] = new AdvancedModelRenderer(chainOrig[i]);
            }
        }
    }

    public void updateChain(float delta, AdvancedModelRenderer[] chainOrig, AdvancedModelRenderer[] chainDynamic, float gravityAmount, float stiffness, float stiffnessFalloff, float damping, int numUpdates, boolean useFloor) {
        if (this.p.length != chainOrig.length || Double.isNaN(this.p[1].x)) {
            this.setChain(chainOrig, chainDynamic);
        }

        if (this.prevUpdateTick != this.entity.age) {
            for (int i = 0; i < chainOrig.length; i++) {
                this.pOrig[i] = chainOrig[i].getWorldPos(this.entity, delta);
            }

            this.updateBendConstraint(gravityAmount, stiffness, stiffnessFalloff, damping, numUpdates, useFloor);

            this.prevUpdateTick = this.entity.age;
        }

        if (chainDynamic == null) return;
        if (MinecraftClient.getInstance().isPaused()) delta = 0.5f;
        for (int i = chainDynamic.length - 1; i >= 0; i--) {
            if (chainDynamic[i] == null) return;
            Vec3d renderPos = this.p[i].add(this.v[i].multiply(delta)).add(this.a[i].multiply(0.5 * delta * delta));
            chainDynamic[i].setWorldPos(this.entity, renderPos, delta);

            if (i < chainDynamic.length - 1) {
                Vec3d p1 = new Vec3d(chainDynamic[i].rotationPointX, chainDynamic[i].rotationPointY, chainDynamic[i].rotationPointZ);
                Vec3d p2 = new Vec3d(chainDynamic[i + 1].rotationPointX, chainDynamic[i + 1].rotationPointY, chainDynamic[i + 1].rotationPointZ);
                Vec3d diff = p2.subtract(p1);
                float yaw = (float) Math.atan2(diff.x, diff.z);
                float pitch = -(float) Math.asin(diff.y / diff.length());
                chainDynamic[i].rotateAngleY = chainDynamic[i].defaultRotationY + yaw;
                chainDynamic[i].rotateAngleX = chainDynamic[i].defaultRotationZ + pitch;
                chainDynamic[i].rotateAngleZ = (float) this.r[i].z;

                Vec3d diffRotated = diff;
                diffRotated = diffRotated.rotateY(yaw);
                diffRotated = diffRotated.rotateX(pitch);
//                System.out.println(diffRotated);
//                dynModelRenderers[i].setScale(1, 1, 1);
//                dynModelRenderers[i].setScale(1, 1, 1 + (float)diffRotated.z/16);
            }
        }
    }

    public void render(MatrixStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha, AdvancedModelRenderer[] dynModelRenderers) {
        if (dynModelRenderers == null) return;
        for (int i = 0; i < dynModelRenderers.length - 1; i++) {
            if (dynModelRenderers[i] == null) return;
            dynModelRenderers[i].render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        }
    }
}

package com.bobmowzie.mowziesmobs.client.particle.util;

import com.bobmowzie.mowziesmobs.client.model.tools.MathUtils;
import com.bobmowzie.mowziesmobs.client.particle.ParticleRibbon;
import com.bobmowzie.mowziesmobs.client.render.MMRenderType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.*;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.ParticleType;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class AdvancedParticleBase extends SpriteBillboardParticle {
    public boolean doRender;

    public float airDrag;
    public float red, green, blue, alpha;
    public float prevRed, prevGreen, prevBlue, prevAlpha;
    public float scale, prevScale, particleScale;
    public ParticleRotation rotation;
    public boolean emissive;
    public double prevMotionX, prevMotionY, prevMotionZ;

    public ParticleComponent[] components;

    public ParticleRibbon ribbon;

    protected AdvancedParticleBase(ClientWorld worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double motionX, double motionY, double motionZ, ParticleRotation rotation, double scale, double r, double g, double b, double a, double drag, double duration, boolean emissive, boolean canCollide, ParticleComponent[] components) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, 0.0D, 0.0D, 0.0D);
        this.velocityX = motionX;
        this.velocityY = motionY;
        this.velocityZ = motionZ;
        this.red = (float) (r);
        this.green = (float) (g);
        this.blue = (float) (b);
        this.alpha = (float) (a);
        this.scale = (float) scale;
        this.maxAge = (int) duration;
        this.airDrag = (float) drag;
        this.rotation = rotation;
        this.components = components;
        this.emissive = emissive;
        this.ribbon = null;

        this.doRender = true;

        for (ParticleComponent component : components) {
            component.init(this);
        }

        this.prevPosX = this.x;
        this.prevPosY = this.y;
        this.prevPosZ = this.z;
        this.prevRed = this.red;
        this.prevGreen = this.green;
        this.prevBlue = this.blue;
        this.prevAlpha = this.alpha;
        this.rotation.setPrevValues();
        this.prevScale = this.scale;
        this.collidesWithWorld = canCollide;
    }

    public static void spawnParticle(World world, ParticleType<AdvancedParticleData> particle, double x, double y, double z, double motionX, double motionY, double motionZ, boolean faceCamera, double yaw, double pitch, double roll, double faceCameraAngle, double scale, double r, double g, double b, double a, double drag, double duration, boolean emissive, boolean canCollide) {
        spawnParticle(world, particle, x, y, z, motionX, motionY, motionZ, faceCamera, yaw, pitch, roll, faceCameraAngle, scale, r, g, b, a, drag, duration, emissive, canCollide, new ParticleComponent[]{});
    }

    public static void spawnParticle(World world, ParticleType<AdvancedParticleData> particle, double x, double y, double z, double motionX, double motionY, double motionZ, boolean faceCamera, double yaw, double pitch, double roll, double faceCameraAngle, double scale, double r, double g, double b, double a, double drag, double duration, boolean emissive, boolean canCollide, ParticleComponent[] components) {
        ParticleRotation rotation = faceCamera ? new ParticleRotation.FaceCamera((float) faceCameraAngle) : new ParticleRotation.EulerAngles((float) yaw, (float) pitch, (float) roll);
        world.addParticle(new AdvancedParticleData(particle, rotation, scale, r, g, b, a, drag, duration, emissive, canCollide, components), x, y, z, motionX, motionY, motionZ);
    }

    public static void spawnParticle(World world, ParticleType<AdvancedParticleData> particle, double x, double y, double z, double motionX, double motionY, double motionZ, ParticleRotation rotation, double scale, double r, double g, double b, double a, double drag, double duration, boolean emissive, boolean canCollide, ParticleComponent[] components) {
        world.addParticle(new AdvancedParticleData(particle, rotation, scale, r, g, b, a, drag, duration, emissive, canCollide, components), x, y, z, motionX, motionY, motionZ);
    }

    public static void spawnAlwaysVisibleParticle(World world, ParticleType<AdvancedParticleData> particle, double distanceLimit, double x, double y, double z, double motionX, double motionY, double motionZ, ParticleRotation rotation, double scale, double r, double g, double b, double a, double drag, double duration, boolean emissive, boolean canCollide, ParticleComponent[] components) {
        Camera camera = MinecraftClient.getInstance().gameRenderer.getCamera();
        boolean overrideLimiter = camera.getPos().squaredDistanceTo(x, y, z) < distanceLimit * distanceLimit;
        world.addImportantParticle(new AdvancedParticleData(particle, rotation, scale, r, g, b, a, drag, duration, emissive, canCollide, components), overrideLimiter, x, y, z, motionX, motionY, motionZ);
    }

    public static void spawnAlwaysVisibleParticle(World world, ParticleType<AdvancedParticleData> particle, double distanceLimit, double x, double y, double z, double motionX, double motionY, double motionZ, boolean faceCamera, double yaw, double pitch, double roll, double faceCameraAngle, double scale, double r, double g, double b, double a, double drag, double duration, boolean emissive, boolean canCollide, ParticleComponent[] components) {
        Camera camera = MinecraftClient.getInstance().gameRenderer.getCamera();
        boolean overrideLimiter = camera.getPos().squaredDistanceTo(x, y, z) < distanceLimit * distanceLimit;
        ParticleRotation rotation = faceCamera ? new ParticleRotation.FaceCamera((float) faceCameraAngle) : new ParticleRotation.EulerAngles((float) yaw, (float) pitch, (float) roll);
        world.addImportantParticle(new AdvancedParticleData(particle, rotation, scale, r, g, b, a, drag, duration, emissive, canCollide, components), overrideLimiter, x, y, z, motionX, motionY, motionZ);
    }

    @Override
    public ParticleTextureSheet getType() {
        return MMRenderType.PARTICLE_SHEET_TRANSLUCENT_NO_DEPTH;
    }

    public int getBrightness(float partialTick) {
        int i = super.getBrightness(partialTick);
        if (this.emissive) {
            int k = i >> 16 & 255;
            return 240 | k << 16;
        } else return i;
    }

    @Override
    public void tick() {
        this.prevRed = this.red;
        this.prevGreen = this.green;
        this.prevBlue = this.blue;
        this.prevAlpha = this.alpha;
        this.prevScale = this.scale;
        this.rotation.setPrevValues();
        this.prevPosX = this.x;
        this.prevPosY = this.y;
        this.prevPosZ = this.z;
        this.prevMotionX = this.velocityX;
        this.prevMotionY = this.velocityY;
        this.prevMotionZ = this.velocityZ;

        for (ParticleComponent component : this.components) {
            component.preUpdate(this);
        }

        if (this.age++ >= this.maxAge) {
            this.markDead();
        }

        this.updatePosition();

        for (ParticleComponent component : this.components) {
            component.postUpdate(this);
        }

        if (this.ribbon != null) {
            this.ribbon.setPos(this.x, this.y, this.z);
            this.ribbon.positions[0] = new Vec3d(this.x, this.y, this.z);
            this.ribbon.prevPositions[0] = this.getPrevPos();
        }
    }

    protected void updatePosition() {
        //this.motionY -= 0.04D * (double)this.particleGravity;
        this.move(this.velocityX, this.velocityY, this.velocityZ);

        if (this.onGround && this.collidesWithWorld) {
            this.velocityX *= 0.699999988079071D;
            this.velocityZ *= 0.699999988079071D;
        }

        this.velocityX *= this.airDrag;
        this.velocityY *= this.airDrag;
        this.velocityZ *= this.airDrag;
    }

    @Override
    public void buildGeometry(VertexConsumer buffer, Camera renderInfo, float partialTicks) {
        this.alpha = this.prevAlpha + (this.alpha - this.prevAlpha) * partialTicks;
        if (this.alpha < 0.01) this.alpha = 0.01f;
        this.red = this.prevRed + (this.red - this.prevRed) * partialTicks;
        this.green = this.prevGreen + (this.green - this.prevGreen) * partialTicks;
        this.blue = this.prevBlue + (this.blue - this.prevBlue) * partialTicks;
        this.particleScale = this.prevScale + (this.scale - this.prevScale) * partialTicks;

        for (ParticleComponent component : this.components) {
            component.preRender(this, partialTicks);
        }

        if (!this.doRender) return;

        Vec3d Vector3d = renderInfo.getPos();
        float f = (float) (MathHelper.lerp(partialTicks, this.prevPosX, this.x) - Vector3d.getX());
        float f1 = (float) (MathHelper.lerp(partialTicks, this.prevPosY, this.y) - Vector3d.getY());
        float f2 = (float) (MathHelper.lerp(partialTicks, this.prevPosZ, this.z) - Vector3d.getZ());

        Quaternionf quaternion = new Quaternionf(0.0F, 0.0F, 0.0F, 1.0F);
        if (this.rotation instanceof ParticleRotation.FaceCamera faceCameraRot) {
            if (faceCameraRot.faceCameraAngle == 0.0F && faceCameraRot.prevFaceCameraAngle == 0.0F) {
                quaternion = renderInfo.getRotation();
            } else {
                quaternion = new Quaternionf(renderInfo.getRotation());
                float f3 = MathHelper.lerp(partialTicks, faceCameraRot.prevFaceCameraAngle, faceCameraRot.faceCameraAngle);
                quaternion.mul(RotationAxis.POSITIVE_Z.rotation(f3));
            }
        } else if (this.rotation instanceof ParticleRotation.EulerAngles eulerRot) {
            float rotX = eulerRot.prevPitch + (eulerRot.pitch - eulerRot.prevPitch) * partialTicks;
            float rotY = eulerRot.prevYaw + (eulerRot.yaw - eulerRot.prevYaw) * partialTicks;
            float rotZ = eulerRot.prevRoll + (eulerRot.roll - eulerRot.prevRoll) * partialTicks;
            Quaternionf quatX = MathUtils.quatFromRotationXYZ(rotX, 0, 0, false);
            Quaternionf quatY = MathUtils.quatFromRotationXYZ(0, rotY, 0, false);
            Quaternionf quatZ = MathUtils.quatFromRotationXYZ(0, 0, rotZ, false);
            quaternion.mul(quatZ);
            quaternion.mul(quatY);
            quaternion.mul(quatX);
        }
        if (this.rotation instanceof ParticleRotation.OrientVector orientRot) {
            double x = orientRot.prevOrientation.x + (orientRot.orientation.x - orientRot.prevOrientation.x) * partialTicks;
            double y = orientRot.prevOrientation.y + (orientRot.orientation.y - orientRot.prevOrientation.y) * partialTicks;
            double z = orientRot.prevOrientation.z + (orientRot.orientation.z - orientRot.prevOrientation.z) * partialTicks;
            float pitch = (float) Math.asin(-y);
            float yaw = (float) (MathHelper.atan2(x, z));
            Quaternionf quatX = MathUtils.quatFromRotationXYZ(pitch, 0, 0, false);
            Quaternionf quatY = MathUtils.quatFromRotationXYZ(0, yaw, 0, false);
            quaternion.mul(quatY);
            quaternion.mul(quatX);
        }

        Vector3f[] avector3f = new Vector3f[]{new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F)};
        float f4 = this.particleScale * 0.1f;

        for (int i = 0; i < 4; ++i) {
            Vector3f vector3f = avector3f[i];
            quaternion.transform(vector3f);
            vector3f.mul(f4);
            vector3f.add(f, f1, f2);
        }

        float f7 = this.getMinU();
        float f8 = this.getMaxU();
        float f5 = this.getMinV();
        float f6 = this.getMaxV();
        int j = this.getBrightness(partialTicks);
        buffer.vertex(avector3f[0].x(), avector3f[0].y(), avector3f[0].z()).texture(f8, f6).color(this.red, this.green, this.blue, this.alpha).light(j).next();
        buffer.vertex(avector3f[1].x(), avector3f[1].y(), avector3f[1].z()).texture(f8, f5).color(this.red, this.green, this.blue, this.alpha).light(j).next();
        buffer.vertex(avector3f[2].x(), avector3f[2].y(), avector3f[2].z()).texture(f7, f5).color(this.red, this.green, this.blue, this.alpha).light(j).next();
        buffer.vertex(avector3f[3].x(), avector3f[3].y(), avector3f[3].z()).texture(f7, f6).color(this.red, this.green, this.blue, this.alpha).light(j).next();

        for (ParticleComponent component : this.components) {
            component.postRender(this, buffer, renderInfo, partialTicks, j);
        }
    }

    public float getAge() {
        return this.age;
    }

    public double getPosX() {
        return this.x;
    }

    public void setPosX(double posX) {
        this.setPos(posX, this.y, this.z);
    }

    public double getPosY() {
        return this.y;
    }

    public void setPosY(double posY) {
        this.setPos(this.x, posY, this.z);
    }

    public double getPosZ() {
        return this.z;
    }

    public void setPosZ(double posZ) {
        this.setPos(this.x, this.y, posZ);
    }

    public double getMotionX() {
        return this.velocityX;
    }

    public void setMotionX(double motionX) {
        this.velocityX = motionX;
    }

    public double getMotionY() {
        return this.velocityY;
    }

    public void setMotionY(double motionY) {
        this.velocityY = motionY;
    }

    public double getMotionZ() {
        return this.velocityZ;
    }

    public void setMotionZ(double motionZ) {
        this.velocityZ = motionZ;
    }

    public Vec3d getPrevPos() {
        return new Vec3d(this.prevPosX, this.prevPosY, this.prevPosZ);
    }

    public double getPrevPosX() {
        return this.prevPosX;
    }

    public double getPrevPosY() {
        return this.prevPosY;
    }

    public double getPrevPosZ() {
        return this.prevPosZ;
    }

    public World getWorld() {
        return this.world;
    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<AdvancedParticleData> {
        private final SpriteProvider spriteSet;

        public Factory(SpriteProvider sprite) {
            this.spriteSet = sprite;
        }

        @Override
        public Particle createParticle(AdvancedParticleData typeIn, ClientWorld worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            AdvancedParticleBase particle = new AdvancedParticleBase(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, typeIn.getRotation(), typeIn.getScale(), typeIn.getRed(), typeIn.getGreen(), typeIn.getBlue(), typeIn.getAlpha(), typeIn.getAirDrag(), typeIn.getDuration(), typeIn.isEmissive(), typeIn.getCanCollide(), typeIn.getComponents());
            particle.setColor((float) typeIn.getRed(), (float) typeIn.getGreen(), (float) typeIn.getBlue());
            particle.setSprite(this.spriteSet);
            return particle;
        }
    }
}

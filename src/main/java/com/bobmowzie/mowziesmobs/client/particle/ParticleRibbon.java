package com.bobmowzie.mowziesmobs.client.particle;

import com.bobmowzie.mowziesmobs.client.particle.util.AdvancedParticleBase;
import com.bobmowzie.mowziesmobs.client.particle.util.ParticleComponent;
import com.bobmowzie.mowziesmobs.client.particle.util.ParticleRotation;
import com.bobmowzie.mowziesmobs.client.particle.util.RibbonComponent.PropertyOverLength;
import com.bobmowzie.mowziesmobs.client.particle.util.RibbonComponent.PropertyOverLength.EnumRibbonProperty;
import com.bobmowzie.mowziesmobs.client.particle.util.RibbonParticleData;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.joml.Vector4f;

public class ParticleRibbon extends AdvancedParticleBase {
    public Vec3d[] positions;
    public Vec3d[] prevPositions;

    public float texPanOffset;

    protected ParticleRibbon(ClientWorld worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double motionX, double motionY, double motionZ, ParticleRotation rotation, double scale, double r, double g, double b, double a, double drag, double duration, boolean emissive, int length, ParticleComponent[] components) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, motionX, motionY, motionZ, rotation, scale, r, g, b, a, drag, duration, emissive, false, components);
        this.positions = new Vec3d[length];
        this.prevPositions = new Vec3d[length];
        if (this.positions.length >= 1) this.positions[0] = new Vec3d(this.getPosX(), this.getPosY(), this.getPosZ());
        if (this.prevPositions.length >= 1) this.prevPositions[0] = this.getPrevPos();
    }

    @Override
    protected void updatePosition() {
        super.updatePosition();
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

        int j = this.getBrightness(partialTicks);

        float r = this.red;
        float g = this.green;
        float b = this.blue;
        float a = this.alpha;
        float scale = this.particleScale;
        float prevR = r;
        float prevG = g;
        float prevB = b;
        float prevA = a;
        float prevScale = scale;

        for (ParticleComponent component : this.components) {
            if (component instanceof PropertyOverLength pOverLength) {
                float value = pOverLength.evaluate(0);
                if (pOverLength.getProperty() == EnumRibbonProperty.SCALE) {
                    prevScale *= value;
                } else if (pOverLength.getProperty() == EnumRibbonProperty.RED) {
                    prevR *= value;
                } else if (pOverLength.getProperty() == EnumRibbonProperty.GREEN) {
                    prevG *= value;
                } else if (pOverLength.getProperty() == EnumRibbonProperty.BLUE) {
                    prevB *= value;
                } else if (pOverLength.getProperty() == EnumRibbonProperty.ALPHA) {
                    prevA *= value;
                }
            }
        }

        Vec3d offsetDir = new Vec3d(0, 0, 0);
        for (int index = 0; index < this.positions.length - 1; index++) {
            if (this.positions[index] == null || this.positions[index + 1] == null) continue;

            r = this.red;
            g = this.green;
            b = this.blue;
            scale = this.particleScale;
            float t = ((float) index + 1) / ((float) this.positions.length - 1);
            float tPrev = ((float) index) / ((float) this.positions.length - 1);

            for (ParticleComponent component : this.components) {
                if (component instanceof PropertyOverLength pOverLength) {
                    float value = pOverLength.evaluate(t);
                    if (pOverLength.getProperty() == EnumRibbonProperty.SCALE) {
                        scale *= value;
                    } else if (pOverLength.getProperty() == EnumRibbonProperty.RED) {
                        r *= value;
                    } else if (pOverLength.getProperty() == EnumRibbonProperty.GREEN) {
                        g *= value;
                    } else if (pOverLength.getProperty() == EnumRibbonProperty.BLUE) {
                        b *= value;
                    } else if (pOverLength.getProperty() == EnumRibbonProperty.ALPHA) {
                        a *= value;
                    }
                }
            }

            Vec3d Vector3d = renderInfo.getPos();
            Vec3d p1 = this.prevPositions[index].add(this.positions[index].subtract(this.prevPositions[index]).multiply(partialTicks)).subtract(Vector3d);
            Vec3d p2 = this.prevPositions[index + 1].add(this.positions[index + 1].subtract(this.prevPositions[index + 1]).multiply(partialTicks)).subtract(Vector3d);

            if (index == 0) {
                Vec3d moveDir = p2.subtract(p1).normalize();
                if (this.rotation instanceof ParticleRotation.FaceCamera) {
                    Vec3d viewVec = new Vec3d(renderInfo.getHorizontalPlane());
                    offsetDir = moveDir.crossProduct(viewVec).normalize();
                } else {
                    offsetDir = moveDir.crossProduct(new Vec3d(0, 1, 0)).normalize();
                }
                offsetDir = offsetDir.multiply(prevScale);
            }

            Vec3d[] aVector3d2 = new Vec3d[]{offsetDir.multiply(-1), offsetDir, null, null};
            Vec3d moveDir = p2.subtract(p1).normalize();
            if (this.rotation instanceof ParticleRotation.FaceCamera) {
                Vec3d viewVec = new Vec3d(renderInfo.getHorizontalPlane());
                offsetDir = moveDir.crossProduct(viewVec).normalize();
            } else {
                offsetDir = moveDir.crossProduct(new Vec3d(0, 1, 0)).normalize();
            }
            offsetDir = offsetDir.multiply(scale);
            aVector3d2[2] = offsetDir;
            aVector3d2[3] = offsetDir.multiply(-1);

            Vector4f[] vertices2 = new Vector4f[]{
                    new Vector4f((float) aVector3d2[0].x, (float) aVector3d2[0].y, (float) aVector3d2[0].z, 1f),
                    new Vector4f((float) aVector3d2[1].x, (float) aVector3d2[1].y, (float) aVector3d2[1].z, 1f),
                    new Vector4f((float) aVector3d2[2].x, (float) aVector3d2[2].y, (float) aVector3d2[2].z, 1f),
                    new Vector4f((float) aVector3d2[3].x, (float) aVector3d2[3].y, (float) aVector3d2[3].z, 1f)
            };
            Matrix4f boxTranslate = (new Matrix4f()).translate((float) p1.x, (float) p1.y, (float) p1.z);
            vertices2[0].mul(boxTranslate);
            vertices2[1].mul(boxTranslate);
            boxTranslate = (new Matrix4f()).translate((float) p2.x, (float) p2.y, (float) p2.z);
            vertices2[2].mul(boxTranslate);
            vertices2[3].mul(boxTranslate);

            float halfU = (this.getMaxU() - this.getMinU()) / 2 + this.getMinU();
            float f = this.getMinU() + this.texPanOffset;
            float f1 = halfU + this.texPanOffset;
            float f2 = this.getMinV();
            float f3 = this.getMaxV();

            buffer.vertex(vertices2[0].x(), vertices2[0].y(), vertices2[0].z()).texture(f1, f3).color(prevR, prevG, prevB, prevA).light(j).next();
            buffer.vertex(vertices2[1].x(), vertices2[1].y(), vertices2[1].z()).texture(f1, f2).color(prevR, prevG, prevB, prevA).light(j).next();
            buffer.vertex(vertices2[2].x(), vertices2[2].y(), vertices2[2].z()).texture(f, f2).color(r, g, b, a).light(j).next();
            buffer.vertex(vertices2[3].x(), vertices2[3].y(), vertices2[3].z()).texture(f, f3).color(r, g, b, a).light(j).next();

            prevR = r;
            prevG = g;
            prevB = b;
            prevA = a;
        }

        for (ParticleComponent component : this.components) {
            component.postRender(this, buffer, renderInfo, partialTicks, j);
        }
    }

    @Override
    public Box getBoundingBox() {
        if (this.positions == null || this.positions.length <= 0 || this.positions[0] == null) return super.getBoundingBox();
        double minX = this.positions[0].getX() - 0.1;
        double minY = this.positions[0].getY() - 0.1;
        double minZ = this.positions[0].getZ() - 0.1;
        double maxX = this.positions[0].getX() + 0.1;
        double maxY = this.positions[0].getY() + 0.1;
        double maxZ = this.positions[0].getZ() + 0.1;
        for (Vec3d pos : this.positions) {
            if (pos == null) continue;
            minX = Math.min(minX, pos.getX());
            minY = Math.min(minY, pos.getY());
            minZ = Math.min(minZ, pos.getZ());
            maxX = Math.max(maxX, pos.getX());
            maxY = Math.max(maxY, pos.getY());
            maxZ = Math.max(maxZ, pos.getZ());
        }
        return new Box(minX, minY, minZ, maxX, maxY, maxZ);
    }

    public float getMinUPublic() {
        return this.getMinU();
    }

    public float getMaxUPublic() {
        return this.getMaxU();
    }

    public float getMinVPublic() {
        return this.getMinV();
    }

    public float getMaxVPublic() {
        return this.getMaxV();
    }

    @Environment(EnvType.CLIENT)
    public static final class Factory implements ParticleFactory<RibbonParticleData> {
        private final SpriteProvider spriteSet;

        public Factory(SpriteProvider spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public Particle createParticle(RibbonParticleData typeIn, ClientWorld worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            ParticleRibbon particle = new ParticleRibbon(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, typeIn.getRotation(), typeIn.getScale(), typeIn.getRed(), typeIn.getGreen(), typeIn.getBlue(), typeIn.getAlpha(), typeIn.getAirDrag(), typeIn.getDuration(), typeIn.isEmissive(), typeIn.getLength(), typeIn.getComponents());
            particle.setSpriteForAge(this.spriteSet);
            return particle;
        }
    }
}
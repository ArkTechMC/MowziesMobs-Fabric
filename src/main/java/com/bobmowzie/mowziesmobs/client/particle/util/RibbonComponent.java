package com.bobmowzie.mowziesmobs.client.particle.util;

import com.bobmowzie.mowziesmobs.client.particle.ParticleRibbon;
import net.minecraft.particle.ParticleType;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class RibbonComponent extends ParticleComponent {
    int length;
    ParticleType<? extends RibbonParticleData> ribbon;
    double yaw, pitch, roll, scale, r, g, b, a;
    boolean faceCamera;
    boolean emissive;
    ParticleComponent[] components;

    public RibbonComponent(ParticleType<? extends RibbonParticleData> particle, int length, double yaw, double pitch, double roll, double scale, double r, double g, double b, double a, boolean faceCamera, boolean emissive, ParticleComponent[] components) {
        this.length = length;
        this.yaw = yaw;
        this.pitch = pitch;
        this.roll = roll;
        this.scale = scale;
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
        this.emissive = emissive;
        this.faceCamera = faceCamera;
        this.components = components;
        this.ribbon = particle;
    }

    @Override
    public void init(AdvancedParticleBase particle) {
        super.init(particle);
        if (particle != null) {

            ParticleComponent[] newComponents = new ParticleComponent[this.components.length + 2];
            System.arraycopy(this.components, 0, newComponents, 0, this.components.length);
            newComponents[this.components.length] = new AttachToParticle(particle);
            newComponents[this.components.length + 1] = new Trail();

            World world = particle.getWorld();
            double x = particle.getPosX();
            double y = particle.getPosY();
            double z = particle.getPosZ();
            double duration = particle.getMaxAge() + this.length;
            ParticleRotation rotation = this.faceCamera ? new ParticleRotation.FaceCamera((float) 0) : new ParticleRotation.EulerAngles((float) this.yaw, (float) this.pitch, (float) this.roll);
            world.addParticle(new RibbonParticleData(this.ribbon, rotation, this.scale, this.r, this.g, this.b, this.a, 0, duration, this.emissive, this.length, newComponents), x, y, z, 0, 0, 0);
        }
    }

    private static class AttachToParticle extends ParticleComponent {
        AdvancedParticleBase attachedParticle;

        public AttachToParticle(AdvancedParticleBase attachedParticle) {
            this.attachedParticle = attachedParticle;
        }

        @Override
        public void init(AdvancedParticleBase particle) {
            super.init(particle);
            this.attachedParticle.ribbon = (ParticleRibbon) particle;
        }
    }

    public static class PropertyOverLength extends ParticleComponent {
        private final AnimData animData;
        private final EnumRibbonProperty property;
        public PropertyOverLength(EnumRibbonProperty property, AnimData animData) {
            this.animData = animData;
            this.property = property;
        }

        public float evaluate(float t) {
            return this.animData.evaluate(t);
        }

        public EnumRibbonProperty getProperty() {
            return this.property;
        }

        public enum EnumRibbonProperty {
            RED, GREEN, BLUE, ALPHA,
            SCALE
        }
    }

    public static class Trail extends ParticleComponent {
        @Override
        public void postUpdate(AdvancedParticleBase particle) {
            if (particle instanceof ParticleRibbon ribbon) {
                for (int i = ribbon.positions.length - 1; i > 0; i--) {
                    ribbon.positions[i] = ribbon.positions[i - 1];
                    ribbon.prevPositions[i] = ribbon.prevPositions[i - 1];
                }
                ribbon.positions[0] = new Vec3d(ribbon.getPosX(), ribbon.getPosY(), ribbon.getPosZ());
                ribbon.prevPositions[0] = ribbon.getPrevPos();
            }
        }
    }

    public static class BeamPinning extends ParticleComponent {
        private final Vec3d[] startLocation;
        private final Vec3d[] endLocation;

        public BeamPinning(Vec3d[] startLocation, Vec3d[] endLocation) {
            this.startLocation = startLocation;
            this.endLocation = endLocation;
        }

        @Override
        public void postUpdate(AdvancedParticleBase particle) {
            if (particle instanceof ParticleRibbon ribbon && this.validateLocation(this.startLocation) && this.validateLocation(this.endLocation)) {
                ribbon.setPos(this.startLocation[0].getX(), this.startLocation[0].getY(), this.startLocation[0].getZ());

                Vec3d increment = this.endLocation[0].subtract(this.startLocation[0]).multiply(1.0f / (float) (ribbon.positions.length - 1));
                for (int i = 0; i < ribbon.positions.length; i++) {
                    Vec3d newPos = this.startLocation[0].add(increment.multiply(i));
                    ribbon.prevPositions[i] = ribbon.positions[i] == null ? newPos : ribbon.positions[i];
                    ribbon.positions[i] = newPos;
                }
            }
        }

        private boolean validateLocation(Vec3d[] location) {
            return location != null && location.length >= 1 && location[0] != null;
        }
    }

    public static class PanTexture extends ParticleComponent {
        float startOffset = 0;
        float speed = 1;

        public PanTexture(float startOffset, float speed) {
            this.startOffset = startOffset;
            this.speed = speed;
        }

        @Override
        public void preRender(AdvancedParticleBase particle, float partialTicks) {
            if (particle instanceof ParticleRibbon ribbon) {
                float time = (ribbon.getAge() - 1 + partialTicks) / (ribbon.getMaxAge());
                float t = (this.startOffset + time * this.speed) % 1.0f;
                ribbon.texPanOffset = (ribbon.getMaxUPublic() - ribbon.getMinUPublic()) / 2 * t;
            }
        }
    }
}

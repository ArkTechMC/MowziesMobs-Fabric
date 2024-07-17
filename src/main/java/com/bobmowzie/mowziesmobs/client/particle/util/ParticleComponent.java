package com.bobmowzie.mowziesmobs.client.particle.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.util.math.Vec3d;
import org.joml.AxisAngle4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public abstract class ParticleComponent {
    public ParticleComponent() {

    }

    public static Constant constant(float value) {
        return new Constant(value);
    }

    public void init(AdvancedParticleBase particle) {

    }

    public void preUpdate(AdvancedParticleBase particle) {

    }

    public void postUpdate(AdvancedParticleBase particle) {

    }

    public void preRender(AdvancedParticleBase particle, float partialTicks) {

    }

    public void postRender(AdvancedParticleBase particle, VertexConsumer buffer, Camera renderInfo, float partialTicks, int lightmap) {

    }

    public abstract static class AnimData {
        public float evaluate(float t) {
            return 0;
        }
    }

    public static class KeyTrack extends AnimData {
        float[] values;
        float[] times;

        public KeyTrack(float[] values, float[] times) {
            this.values = values;
            this.times = times;
            if (values.length != times.length)
                System.out.println("Malformed key track. Must have same number of keys and values or key track will evaluate to 0.");
        }

        public static KeyTrack startAndEnd(float startValue, float endValue) {
            return new KeyTrack(new float[]{startValue, endValue}, new float[]{0, 1});
        }

        public static KeyTrack oscillate(float value1, float value2, int frequency) {
            if (frequency <= 1) new KeyTrack(new float[]{value1, value2}, new float[]{0, 1});
            float step = 1.0f / frequency;
            float[] times = new float[frequency + 1];
            float[] values = new float[frequency + 1];
            for (int i = 0; i < frequency + 1; i++) {
                float value = i % 2 == 0 ? value1 : value2;
                times[i] = step * i;
                values[i] = value;
            }
            return new KeyTrack(values, times);
        }

        @Override
        public float evaluate(float t) {
            if (this.values.length != this.times.length) return 0;
            for (int i = 0; i < this.times.length; i++) {
                float time = this.times[i];
                if (t == time) return this.values[i];
                else if (t < time) {
                    if (i == 0) return this.values[0];
                    float a = (t - this.times[i - 1]) / (time - this.times[i - 1]);
                    return this.values[i - 1] * (1 - a) + this.values[i] * a;
                } else {
                    if (i == this.values.length - 1) return this.values[i];
                }
            }
            return 0;
        }
    }

    public static class Oscillator extends AnimData {
        float value1, value2;
        float frequency;
        float phaseShift;

        public Oscillator(float value1, float value2, float frequency, float phaseShift) {
            this.value1 = value1;
            this.value2 = value2;
            this.frequency = frequency;
            this.phaseShift = phaseShift;
        }

        @Override
        public float evaluate(float t) {
            float a = (this.value2 - this.value1) / 2f;
            return (float) (this.value1 + a + a * Math.cos(t * this.frequency + this.phaseShift));
        }
    }

    public static class Constant extends AnimData {
        float value;

        public Constant(float value) {
            this.value = value;
        }

        @Override
        public float evaluate(float t) {
            return this.value;
        }
    }

    public static class PropertyControl extends ParticleComponent {
        private final AnimData animData;
        private final EnumParticleProperty property;
        private final boolean additive;
        public PropertyControl(EnumParticleProperty property, AnimData animData, boolean additive) {
            this.property = property;
            this.animData = animData;
            this.additive = additive;
        }

        @Override
        public void init(AdvancedParticleBase particle) {
            float value = this.animData.evaluate(0);
            this.applyUpdate(particle, value);
            this.applyRender(particle, value);
        }

        @Override
        public void preRender(AdvancedParticleBase particle, float partialTicks) {
            float ageFrac = (particle.getAge() + partialTicks) / particle.getMaxAge();
            float value = this.animData.evaluate(ageFrac);
            this.applyRender(particle, value);
        }

        @Override
        public void preUpdate(AdvancedParticleBase particle) {
            float ageFrac = particle.getAge() / particle.getMaxAge();
            float value = this.animData.evaluate(ageFrac);
            this.applyUpdate(particle, value);
        }

        private void applyUpdate(AdvancedParticleBase particle, float value) {
            if (this.property == EnumParticleProperty.POS_X) {
                if (this.additive) particle.setPosX(particle.getPosX() + value);
                else particle.setPosX(value);
            } else if (this.property == EnumParticleProperty.POS_Y) {
                if (this.additive) particle.setPosY(particle.getPosY() + value);
                else particle.setPosY(value);
            } else if (this.property == EnumParticleProperty.POS_Z) {
                if (this.additive) particle.setPosZ(particle.getPosZ() + value);
                else particle.setPosZ(value);
            } else if (this.property == EnumParticleProperty.MOTION_X) {
                if (this.additive) particle.setMotionX(particle.getMotionX() + value);
                else particle.setMotionX(value);
            } else if (this.property == EnumParticleProperty.MOTION_Y) {
                if (this.additive) particle.setMotionY(particle.getMotionY() + value);
                else particle.setMotionY(value);
            } else if (this.property == EnumParticleProperty.MOTION_Z) {
                if (this.additive) particle.setMotionZ(particle.getMotionZ() + value);
                else particle.setMotionZ(value);
            } else if (this.property == EnumParticleProperty.AIR_DRAG) {
                if (this.additive) particle.airDrag += value;
                else particle.airDrag = value;
            }
        }

        private void applyRender(AdvancedParticleBase particle, float value) {
            if (this.property == EnumParticleProperty.RED) {
                if (this.additive) particle.red += value;
                else particle.red = value;
            } else if (this.property == EnumParticleProperty.GREEN) {
                if (this.additive) particle.green += value;
                else particle.green = value;
            } else if (this.property == EnumParticleProperty.BLUE) {
                if (this.additive) particle.blue += value;
                else particle.blue = value;
            } else if (this.property == EnumParticleProperty.ALPHA) {
                if (this.additive) particle.alpha += value;
                else particle.alpha = value;
            } else if (this.property == EnumParticleProperty.SCALE) {
                if (this.additive) particle.scale += value;
                else particle.scale = value;
            } else if (this.property == EnumParticleProperty.YAW) {
                if (particle.rotation instanceof ParticleRotation.EulerAngles eulerRot) {
                    if (this.additive) eulerRot.yaw += value;
                    else eulerRot.yaw = value;
                }
            } else if (this.property == EnumParticleProperty.PITCH) {
                if (particle.rotation instanceof ParticleRotation.EulerAngles eulerRot) {
                    if (this.additive) eulerRot.pitch += value;
                    else eulerRot.pitch = value;
                }
            } else if (this.property == EnumParticleProperty.ROLL) {
                if (particle.rotation instanceof ParticleRotation.EulerAngles eulerRot) {
                    if (this.additive) eulerRot.roll += value;
                    else eulerRot.roll = value;
                }
            } else if (this.property == EnumParticleProperty.PARTICLE_ANGLE) {
                if (particle.rotation instanceof ParticleRotation.FaceCamera faceCameraRot) {
                    if (this.additive) faceCameraRot.faceCameraAngle += value;
                    else faceCameraRot.faceCameraAngle = value;
                }
            }
        }

        public enum EnumParticleProperty {
            POS_X, POS_Y, POS_Z,
            MOTION_X, MOTION_Y, MOTION_Z,
            RED, GREEN, BLUE, ALPHA,
            SCALE,
            YAW, PITCH, ROLL, // For not facing camera
            PARTICLE_ANGLE, // For facing camera
            AIR_DRAG
        }
    }

    public static class PinLocation extends ParticleComponent {
        private final Vec3d[] location;

        public PinLocation(Vec3d[] location) {
            this.location = location;
        }

        @Override
        public void init(AdvancedParticleBase particle) {
            if (this.location != null && this.location.length > 0 && this.location[0] != null) {
                particle.setPos(this.location[0].x, this.location[0].y, this.location[0].z);
            }
        }

        @Override
        public void preUpdate(AdvancedParticleBase particle) {
            if (this.location != null && this.location.length > 0 && this.location[0] != null) {
                particle.setPos(this.location[0].x, this.location[0].y, this.location[0].z);
            }
        }

        @Override
        public void preRender(AdvancedParticleBase particle, float partialTicks) {
            super.preRender(particle, partialTicks);
            particle.doRender = this.location != null && this.location.length > 0 && this.location[0] != null;
        }
    }

    public static class Attractor extends ParticleComponent {
        private final Vec3d[] location;
        private final float strength;
        private final float killDist;
        private final EnumAttractorBehavior behavior;
        private Vec3d startLocation;
        public Attractor(Vec3d[] location, float strength, float killDist, EnumAttractorBehavior behavior) {
            this.location = location;
            this.strength = strength;
            this.killDist = killDist;
            this.behavior = behavior;
        }

        @Override
        public void init(AdvancedParticleBase particle) {
            this.startLocation = new Vec3d(particle.getPosX(), particle.getPosY(), particle.getPosZ());
        }

        @Override
        public void preUpdate(AdvancedParticleBase particle) {
            float ageFrac = particle.getAge() / (particle.getMaxAge() - 1);
            if (this.location.length > 0) {
                Vec3d destinationVec = this.location[0];
                Vec3d currPos = new Vec3d(particle.getPosX(), particle.getPosY(), particle.getPosZ());
                Vec3d diff = destinationVec.subtract(currPos);
                if (diff.length() < this.killDist) particle.markDead();
                if (this.behavior == EnumAttractorBehavior.EXPONENTIAL) {
                    Vec3d path = destinationVec.subtract(this.startLocation).multiply(Math.pow(ageFrac, this.strength)).add(this.startLocation).subtract(currPos);
                    particle.move(path.x, path.y, path.z);
                } else if (this.behavior == EnumAttractorBehavior.LINEAR) {
                    Vec3d path = destinationVec.subtract(this.startLocation).multiply(ageFrac).add(this.startLocation).subtract(currPos);
                    particle.move(path.x, path.y, path.z);
                } else {
                    double dist = Math.max(diff.length(), 0.001);
                    diff = diff.normalize().multiply(this.strength / (dist * dist));
                    particle.setMotionX(Math.min(particle.getMotionX() + diff.x, 5));
                    particle.setMotionY(Math.min(particle.getMotionY() + diff.y, 5));
                    particle.setMotionZ(Math.min(particle.getMotionZ() + diff.z, 5));
                }
            }
        }

        public enum EnumAttractorBehavior {
            LINEAR,
            EXPONENTIAL,
            SIMULATED,
        }
    }

    public static class Orbit extends ParticleComponent {
        private final Vec3d[] location;
        private final AnimData phase;
        private final AnimData radius;
        private final AnimData axisX;
        private final AnimData axisY;
        private final AnimData axisZ;
        private final boolean faceCamera;

        public Orbit(Vec3d[] location, AnimData phase, AnimData radius, AnimData axisX, AnimData axisY, AnimData axisZ, boolean faceCamera) {
            this.location = location;
            this.phase = phase;
            this.radius = radius;
            this.axisX = axisX;
            this.axisY = axisY;
            this.axisZ = axisZ;
            this.faceCamera = faceCamera;
        }

        @Override
        public void init(AdvancedParticleBase particle) {
            this.apply(particle, 0);
        }

        @Override
        public void preUpdate(AdvancedParticleBase particle) {
            float ageFrac = particle.getAge() / particle.getMaxAge();
            this.apply(particle, ageFrac);
        }

        private void apply(AdvancedParticleBase particle, float t) {
            float p = this.phase.evaluate(t);
            float r = this.radius.evaluate(t);
            Vector3f axis;
            if (this.faceCamera && MinecraftClient.getInstance().player != null) {
                Vec3d lookAngle = MinecraftClient.getInstance().player.getRotationVector();
                axis = new Vector3f((float) lookAngle.getX(), (float) lookAngle.getY(), (float) lookAngle.getZ());
                axis.normalize();
            } else {
                axis = new Vector3f(this.axisX.evaluate(t), this.axisY.evaluate(t), this.axisZ.evaluate(t));
                axis.normalize();
            }

            Quaternionf quat = new Quaternionf(new AxisAngle4f(p * (float) Math.PI * 2, axis));
            Vector3f up = new Vector3f(0, 1, 0);
            Vector3f start = axis;
            if (Math.abs(axis.dot(up)) > 0.99) {
                start = new Vector3f(1, 0, 0);
            }
            start.cross(up);
            start.normalize();
            Vector3f newPos = start;
            quat.transform(newPos);
            newPos.mul(r);

            if (this.location.length > 0 && this.location[0] != null) {
                newPos.add((float) this.location[0].x, (float) this.location[0].y, (float) this.location[0].z);
            }
            particle.setPos(newPos.x(), newPos.y(), newPos.z());
        }
    }

    public static class FaceMotion extends ParticleComponent {
        public FaceMotion() {

        }

        @Override
        public void preRender(AdvancedParticleBase particle, float partialTicks) {
            super.preRender(particle, partialTicks);
            double dx = particle.getPosX() - particle.getPrevPosX();
            double dy = particle.getPosY() - particle.getPrevPosY();
            double dz = particle.getPosZ() - particle.getPrevPosZ();
            double d = Math.sqrt(dx * dx + dy * dy + dz * dz);
            if (d != 0) {
                if (particle.rotation instanceof ParticleRotation.EulerAngles eulerRot) {
                    double a = dy / d;
                    a = Math.max(-1, Math.min(1, a));
                    float pitch = -(float) Math.asin(a);
                    float yaw = -(float) (Math.atan2(dz, dx) + Math.PI);
                    eulerRot.roll = pitch;
                    eulerRot.yaw = yaw;
//                particle.roll = (float) Math.PI / 2;
                } else if (particle.rotation instanceof ParticleRotation.OrientVector orientRot) {
                    orientRot.orientation = new Vec3d(dx, dy, dz).normalize();
                }
            }
        }
    }

    public static class AnimatedTexture extends ParticleComponent {

    }
}

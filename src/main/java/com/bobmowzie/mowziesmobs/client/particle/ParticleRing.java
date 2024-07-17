package com.bobmowzie.mowziesmobs.client.particle;

import com.bobmowzie.mowziesmobs.client.render.MMRenderType;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.Locale;

/**
 * Created by BobMowzie on 6/2/2017.
 */
public class ParticleRing extends SpriteBillboardParticle {
    private final EnumRingBehavior behavior;
    public float r, g, b;
    public float opacity;
    public boolean facesCamera;
    public float yaw, pitch;
    public float size;

    public ParticleRing(ClientWorld world, double x, double y, double z, double motionX, double motionY, double motionZ, float yaw, float pitch, int duration, float r, float g, float b, float opacity, float size, boolean facesCamera, EnumRingBehavior behavior) {
        super(world, x, y, z);
        this.setBoundingBoxSpacing(1, 1);
        this.size = size * 0.1f;
        this.maxAge = duration;
        this.alpha = 1;
        this.r = r;
        this.g = g;
        this.b = b;
        this.opacity = opacity;
        this.yaw = yaw;
        this.pitch = pitch;
        this.facesCamera = facesCamera;
        this.velocityX = motionX;
        this.velocityY = motionY;
        this.velocityZ = motionZ;
        this.behavior = behavior;
    }

    @Override
    public int getBrightness(float delta) {
        return 240 | super.getBrightness(delta) & 0xFF0000;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.age >= this.maxAge) {
            this.markDead();
        }
        this.age++;
    }

    @Override
    public void buildGeometry(VertexConsumer buffer, Camera renderInfo, float partialTicks) {
        float var = (this.age + partialTicks) / this.maxAge;
        if (this.behavior == EnumRingBehavior.GROW) {
            this.scale = this.size * var;
        } else if (this.behavior == EnumRingBehavior.SHRINK) {
            this.scale = this.size * (1 - var);
        } else if (this.behavior == EnumRingBehavior.GROW_THEN_SHRINK) {
            this.scale = (float) (this.size * (1 - var - Math.pow(2000, -var)));
        } else {
            this.scale = this.size;
        }
        this.alpha = this.opacity * 0.95f * (1 - (this.age + partialTicks) / this.maxAge) + 0.05f;
        this.red = this.r;
        this.green = this.g;
        this.blue = this.b;

        Vec3d Vector3d = renderInfo.getPos();
        float f = (float) (MathHelper.lerp(partialTicks, this.prevPosX, this.x) - Vector3d.getX());
        float f1 = (float) (MathHelper.lerp(partialTicks, this.prevPosY, this.y) - Vector3d.getY());
        float f2 = (float) (MathHelper.lerp(partialTicks, this.prevPosZ, this.z) - Vector3d.getZ());
        Quaternionf quaternionf = new Quaternionf(0.0F, 0.0F, 0.0F, 1.0F);
        if (this.facesCamera) {
            if (this.angle == 0.0F) {
                quaternionf = renderInfo.getRotation();
            } else {
                quaternionf = new Quaternionf(renderInfo.getRotation());
                float f3 = MathHelper.lerp(partialTicks, this.prevAngle, this.angle);
                quaternionf.mul(RotationAxis.POSITIVE_Z.rotation(f3));
            }
        } else {
            Quaternionf quatX = MathUtils.quatFromRotationXYZ(this.pitch, 0, 0, false);
            Quaternionf quatY = MathUtils.quatFromRotationXYZ(0, this.yaw, 0, false);
            quaternionf.mul(quatY);
            quaternionf.mul(quatX);
        }

        Vector3f vector3f1 = new Vector3f(-1.0F, -1.0F, 0.0F);
        quaternionf.transform(vector3f1);
        Vector3f[] avector3f = new Vector3f[]{new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F)};
        float f4 = this.getSize(partialTicks);

        for (int i = 0; i < 4; ++i) {
            Vector3f vector3f = avector3f[i];
            quaternionf.transform(vector3f);
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
    }

    @Override
    public ParticleTextureSheet getType() {
        return MMRenderType.PARTICLE_SHEET_TRANSLUCENT_NO_DEPTH;
    }

    public enum EnumRingBehavior {
        SHRINK,
        GROW,
        CONSTANT,
        GROW_THEN_SHRINK
    }

    @Environment(EnvType.CLIENT)
    public static final class RingFactory implements ParticleFactory<RingData> {
        private final SpriteProvider spriteSet;

        public RingFactory(SpriteProvider sprite) {
            this.spriteSet = sprite;
        }

        @Override
        public Particle createParticle(RingData typeIn, ClientWorld worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            ParticleRing particle = new ParticleRing(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, typeIn.getYaw(), typeIn.getPitch(), typeIn.getDuration(), typeIn.getR(), typeIn.getG(), typeIn.getB(), typeIn.getA(), typeIn.getScale(), typeIn.getFacesCamera(), typeIn.getBehavior());
            particle.setSpriteForAge(this.spriteSet);
            return particle;
        }
    }

    public static class RingData implements ParticleEffect {
        public static final Factory<RingData> DESERIALIZER = new Factory<RingData>() {
            public RingData read(ParticleType<RingData> particleTypeIn, StringReader reader) throws CommandSyntaxException {
                reader.expect(' ');
                float yaw = (float) reader.readDouble();
                reader.expect(' ');
                float pitch = (float) reader.readDouble();
                reader.expect(' ');
                float r = (float) reader.readDouble();
                reader.expect(' ');
                float g = (float) reader.readDouble();
                reader.expect(' ');
                float b = (float) reader.readDouble();
                reader.expect(' ');
                float a = (float) reader.readDouble();
                reader.expect(' ');
                float scale = (float) reader.readDouble();
                reader.expect(' ');
                int duration = reader.readInt();
                reader.expect(' ');
                boolean facesCamera = reader.readBoolean();
                return new RingData(yaw, pitch, duration, r, g, b, a, scale, facesCamera, EnumRingBehavior.GROW);
            }

            public RingData read(ParticleType<RingData> particleTypeIn, PacketByteBuf buffer) {
                return new RingData(buffer.readFloat(), buffer.readFloat(), buffer.readInt(), buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readBoolean(), EnumRingBehavior.GROW);
            }
        };

        private final float yaw;
        private final float pitch;
        private final float r;
        private final float g;
        private final float b;
        private final float a;
        private final float scale;
        private final int duration;
        private final boolean facesCamera;
        private final EnumRingBehavior behavior;

        public RingData(float yaw, float pitch, int duration, float r, float g, float b, float a, float scale, boolean facesCamera, EnumRingBehavior behavior) {
            this.yaw = yaw;
            this.pitch = pitch;
            this.r = r;
            this.g = g;
            this.b = b;
            this.a = a;
            this.scale = scale;
            this.duration = duration;
            this.facesCamera = facesCamera;
            this.behavior = behavior;
        }

        public static Codec<RingData> CODEC(ParticleType<RingData> particleType) {
            return RecordCodecBuilder.create((codecBuilder) -> codecBuilder.group(
                            Codec.FLOAT.fieldOf("yaw").forGetter(RingData::getYaw),
                            Codec.FLOAT.fieldOf("pitch").forGetter(RingData::getPitch),
                            Codec.FLOAT.fieldOf("r").forGetter(RingData::getR),
                            Codec.FLOAT.fieldOf("g").forGetter(RingData::getG),
                            Codec.FLOAT.fieldOf("b").forGetter(RingData::getB),
                            Codec.FLOAT.fieldOf("a").forGetter(RingData::getA),
                            Codec.FLOAT.fieldOf("scale").forGetter(RingData::getScale),
                            Codec.INT.fieldOf("duration").forGetter(RingData::getDuration),
                            Codec.BOOL.fieldOf("facesCamera").forGetter(RingData::getFacesCamera),
                            Codec.STRING.fieldOf("behavior").forGetter((ringData) -> ringData.getBehavior().toString())
                    ).apply(codecBuilder, (yaw, pitch, r, g, b, a, scale, duration, facesCamera, behavior) ->
                            new RingData(yaw, pitch, duration, r, g, b, a, scale, facesCamera, EnumRingBehavior.valueOf(behavior)))
            );
        }

        @Override
        public void write(PacketByteBuf buffer) {
            buffer.writeFloat(this.r);
            buffer.writeFloat(this.g);
            buffer.writeFloat(this.b);
            buffer.writeFloat(this.scale);
            buffer.writeInt(this.duration);
        }

        @SuppressWarnings("deprecation")
        @Override
        public String asString() {
            return String.format(Locale.ROOT, "%s %.2f %.2f %.2f %.2f %.2f %.2f %.2f %d %b", Registries.PARTICLE_TYPE.getId(this.getType()),
                    this.yaw, this.pitch, this.r, this.g, this.b, this.scale, this.a, this.duration, this.facesCamera);
        }

        @Override
        public ParticleType<RingData> getType() {
            return ParticleHandler.RING;
        }

        @Environment(EnvType.CLIENT)
        public float getYaw() {
            return this.yaw;
        }

        @Environment(EnvType.CLIENT)
        public float getPitch() {
            return this.pitch;
        }

        @Environment(EnvType.CLIENT)
        public float getR() {
            return this.r;
        }

        @Environment(EnvType.CLIENT)
        public float getG() {
            return this.g;
        }

        @Environment(EnvType.CLIENT)
        public float getB() {
            return this.b;
        }

        @Environment(EnvType.CLIENT)
        public float getA() {
            return this.a;
        }

        @Environment(EnvType.CLIENT)
        public float getScale() {
            return this.scale;
        }

        @Environment(EnvType.CLIENT)
        public int getDuration() {
            return this.duration;
        }

        @Environment(EnvType.CLIENT)
        public boolean getFacesCamera() {
            return this.facesCamera;
        }

        @Environment(EnvType.CLIENT)
        public EnumRingBehavior getBehavior() {
            return this.behavior;
        }
    }
}

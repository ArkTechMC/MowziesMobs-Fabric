package com.bobmowzie.mowziesmobs.client.particle;

import com.bobmowzie.mowziesmobs.client.model.tools.MathUtils;
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

import java.util.Locale;

public class ParticleOrb extends SpriteBillboardParticle {
    private double targetX;
    private double targetY;
    private double targetZ;
    private double startX;
    private double startY;
    private double startZ;
    private double signX;
    private double signZ;
    private int mode;
    private double duration;

    public ParticleOrb(ClientWorld world, double x, double y, double z, double targetX, double targetZ) {
        super(world, x, y, z);
        this.targetX = targetX;
        this.targetZ = targetZ;
        this.scale = (4.5F + this.random.nextFloat() * 1.5F) * 0.1f;
        this.maxAge = 120;
        this.signX = Math.signum(targetX - x);
        this.signZ = Math.signum(targetZ - z);
        this.mode = 0;
        this.alpha = 0;
        this.red = this.green = this.blue = 1;
    }

    public ParticleOrb(ClientWorld world, double x, double y, double z, double targetX, double targetY, double targetZ, double speed) {
        this(world, x, y, z, targetX, targetZ);
        this.targetY = targetY;
        this.startX = x;
        this.startY = y;
        this.startZ = z;
        this.duration = speed;
        this.mode = 1;
        this.alpha = 0.1f;
    }

    public ParticleOrb(ClientWorld world, double x, double y, double z, double vx, double vy, double vz, double r, double g, double b, double scale, int duration) {
        super(world, x, y, z);
        scale = (float) scale * 0.1f;
        this.maxAge = duration;
        this.duration = duration;
        this.velocityX = vx;
        this.velocityY = vy;
        this.velocityZ = vz;
        this.setColor((float) r, (float) g, (float) b);
        this.mode = 2;
    }

    @Override
    public ParticleTextureSheet getType() {
        return MMRenderType.PARTICLE_SHEET_TRANSLUCENT_NO_DEPTH;
    }

    @Override
    public int getBrightness(float delta) {
        return 240 | super.getBrightness(delta) & 0xFF0000;
    }

    @Override
    public void tick() {
        this.alpha = 0.1f;
        this.prevPosX = this.x;
        this.prevPosY = this.y;
        this.prevPosZ = this.z;
        if (this.mode == 0) {
            double vecX = this.targetX - this.x;
            double vecZ = this.targetZ - this.z;
            double dist = Math.sqrt(vecX * vecX + vecZ * vecZ);
            if (dist > 2 || Math.signum(vecX) != this.signX || Math.signum(vecZ) != this.signZ || this.age > this.maxAge) {
                this.markDead();
                return;
            }
            final double peak = 0.5;
            this.alpha = (float) (dist > peak ? MathUtils.linearTransformd(dist, peak, 2, 1, 0) : MathUtils.linearTransformd(dist, 0.1F, peak, 0, 1));
            final double minVel = 0.05, maxVel = 0.3;
            double progress = Math.sin(-Math.PI / 4 * dist) + 1;
            double magMultipler = (progress * (maxVel - minVel) + minVel) / dist;
            vecX *= magMultipler;
            vecZ *= magMultipler;
            this.velocityX = vecX;
            this.velocityY = progress;
            this.velocityZ = vecZ;
            this.move(this.velocityX, this.velocityY, this.velocityZ);
        } else if (this.mode == 1) {
            this.alpha = ((float) this.age / (float) this.duration);//(float) (1 * Math.sqrt(Math.pow(posX - startX, 2) + Math.pow(posY - startY, 2) + Math.pow(posZ - startZ, 2)) / Math.sqrt(Math.pow(targetX - startX, 2) + Math.pow(targetY - startY, 2) + Math.pow(targetZ - startZ, 2)));
            this.x = this.startX + (this.targetX - this.startX) / (1 + Math.exp(-(8 / this.duration) * (this.age - this.duration / 2)));
            this.y = this.startY + (this.targetY - this.startY) / (1 + Math.exp(-(8 / this.duration) * (this.age - this.duration / 2)));
            this.z = this.startZ + (this.targetZ - this.startZ) / (1 + Math.exp(-(8 / this.duration) * (this.age - this.duration / 2)));
            if (this.age == this.duration) {
                this.markDead();
            }
        } else if (this.mode == 2) {
            super.tick();
//            particleAlpha = ((float)age/(float)maxAge);
            if (this.age >= this.maxAge) {
                this.markDead();
            }
        }
        this.age++;
    }

    @Override
    public void buildGeometry(VertexConsumer buffer, Camera renderInfo, float partialTicks) {
        if (this.mode == 2) this.alpha = Math.max(1 - ((float) this.age + partialTicks) / (float) this.duration, 0.001f);
        else this.alpha = ((float) this.age + partialTicks) / (float) this.duration;
        super.buildGeometry(buffer, renderInfo, partialTicks);
    }

    @Environment(EnvType.CLIENT)
    public static final class OrbFactory implements ParticleFactory<OrbData> {
        private final SpriteProvider spriteSet;

        public OrbFactory(SpriteProvider sprite) {
            this.spriteSet = sprite;
        }

        @Override
        public Particle createParticle(OrbData typeIn, ClientWorld worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            ParticleOrb particle;
            if (typeIn.getMode() == 0)
                particle = new ParticleOrb(worldIn, x, y, z, typeIn.getTargetX(), typeIn.getTargetZ());
            else if (typeIn.getMode() == 1)
                particle = new ParticleOrb(worldIn, x, y, z, typeIn.getTargetX(), typeIn.getTargetY(), typeIn.getTargetZ(), typeIn.getSpeed());
            else
                particle = new ParticleOrb(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, typeIn.getR(), typeIn.getG(), typeIn.getB(), typeIn.getScale(), typeIn.getDuration());
            particle.setSpriteForAge(this.spriteSet);
            return particle;
        }
    }

    public static class OrbData implements ParticleEffect {
        public static final Factory<OrbData> DESERIALIZER = new Factory<>() {
            public OrbData read(ParticleType<OrbData> particleTypeIn, StringReader reader) throws CommandSyntaxException {
                reader.expect(' ');
                float r = (float) reader.readDouble();
                reader.expect(' ');
                float g = (float) reader.readDouble();
                reader.expect(' ');
                float b = (float) reader.readDouble();
                reader.expect(' ');
                float scale = (float) reader.readDouble();
                reader.expect(' ');
                int duration = reader.readInt();
                return new OrbData(r, g, b, scale, duration);
            }

            public OrbData read(ParticleType<OrbData> particleTypeIn, PacketByteBuf buffer) {
                return new OrbData(buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readInt());
            }
        };

        private final float r;
        private final float g;
        private final float b;
        private float scale;
        private int duration;

        private float targetX;
        private float targetY;
        private float targetZ;
        private float speed;

        private int mode;

        public OrbData(float targetX, float targetZ) {
            this.targetX = targetX;
            this.targetZ = targetZ;
            this.r = this.g = this.b = 1;

            this.mode = 0;
        }

        public OrbData(float targetX, float targetY, float targetZ, float speed) {
            this(targetX, targetZ);
            this.targetY = targetY;
            this.speed = speed;

            this.mode = 1;
        }

        public OrbData(float r, float g, float b, float scale, int duration) {
            this.r = r;
            this.g = g;
            this.b = b;
            this.scale = scale;
            this.duration = duration;

            this.mode = 2;
        }

        public static Codec<OrbData> CODEC(ParticleType<OrbData> particleType) {
            return RecordCodecBuilder.create((codecBuilder) -> codecBuilder.group(
                            Codec.FLOAT.fieldOf("r").forGetter(OrbData::getR),
                            Codec.FLOAT.fieldOf("g").forGetter(OrbData::getG),
                            Codec.FLOAT.fieldOf("b").forGetter(OrbData::getB),
                            Codec.FLOAT.fieldOf("scale").forGetter(OrbData::getScale),
                            Codec.INT.fieldOf("duration").forGetter(OrbData::getDuration)
                    ).apply(codecBuilder, OrbData::new)
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

        @Override
        public String asString() {
            return String.format(Locale.ROOT, "%s %.2f %.2f %.2f %.2f %d", Registries.PARTICLE_TYPE.getId(this.getType()),
                    this.r, this.g, this.b, this.scale, this.duration);
        }

        @Override
        public ParticleType<OrbData> getType() {
            return ParticleHandler.ORB;
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
        public float getScale() {
            return this.scale;
        }

        @Environment(EnvType.CLIENT)
        public int getDuration() {
            return this.duration;
        }

        @Environment(EnvType.CLIENT)
        public float getTargetX() {
            return this.targetX;
        }

        @Environment(EnvType.CLIENT)
        public float getTargetY() {
            return this.targetY;
        }

        @Environment(EnvType.CLIENT)
        public float getTargetZ() {
            return this.targetZ;
        }

        @Environment(EnvType.CLIENT)
        public float getSpeed() {
            return this.speed;
        }

        @Environment(EnvType.CLIENT)
        public int getMode() {
            return this.mode;
        }
    }
}

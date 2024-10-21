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

import java.util.Locale;

/**
 * Created by BobMowzie on 6/2/2017.
 */
public class ParticleCloud extends SpriteBillboardParticle {
    private final float red;
    private final float green;
    private final float blue;
    private final EnumCloudBehavior behavior;
    private final float airDrag;
    private float scale;

    public ParticleCloud(ClientWorld world, double x, double y, double z, double vx, double vy, double vz, double r, double g, double b, double scale, int duration, EnumCloudBehavior behavior, double airDrag) {
        super(world, x, y, z);
        this.scale = (float) scale * 0.5f * 0.1f;
        this.maxAge = duration;
        this.velocityX = vx * 0.5;
        this.velocityY = vy * 0.5;
        this.velocityZ = vz * 0.5;
        this.red = (float) r;
        this.green = (float) g;
        this.blue = (float) b;
        this.behavior = behavior;
        this.angle = this.prevAngle = (float) (this.random.nextInt(4) * Math.PI / 2);
        this.airDrag = (float) airDrag;
    }

    @Override
    public ParticleTextureSheet getType() {
        return MMRenderType.PARTICLE_SHEET_TRANSLUCENT_NO_DEPTH;
    }

    @Override
    public void tick() {
        super.tick();
        this.velocityX *= this.airDrag;
        this.velocityY *= this.airDrag;
        this.velocityZ *= this.airDrag;
    }

    @Override
    public void buildGeometry(VertexConsumer buffer, Camera renderInfo, float partialTicks) {
        float var = (this.age + partialTicks) / (float) this.maxAge;
        this.alpha = 0.2f * ((float) (1 - Math.exp(5 * (var - 1)) - Math.pow(2000, -var)));
        if (this.alpha < 0.01) this.alpha = 0.01f;
        if (this.behavior == EnumCloudBehavior.SHRINK) this.scale = this.scale * ((1 - 0.7f * var) + 0.3f);
        else if (this.behavior == EnumCloudBehavior.GROW) this.scale = this.scale * ((0.7f * var) + 0.3f);

        super.buildGeometry(buffer, renderInfo, partialTicks);
    }

    public enum EnumCloudBehavior {
        SHRINK,
        GROW,
        CONSTANT
    }

    @Environment(EnvType.CLIENT)
    public static final class CloudFactory implements ParticleFactory<CloudData> {
        private final SpriteProvider spriteSet;

        public CloudFactory(SpriteProvider sprite) {
            this.spriteSet = sprite;
        }

        @Override
        public Particle createParticle(CloudData typeIn, ClientWorld worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            ParticleCloud particleCloud = new ParticleCloud(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, typeIn.getR(), typeIn.getG(), typeIn.getB(), typeIn.getScale(), typeIn.getDuration(), typeIn.getBehavior(), typeIn.getAirDrag());
            particleCloud.setSpriteForAge(this.spriteSet);
            particleCloud.setColor(typeIn.getR(), typeIn.getG(), typeIn.getB());
            return particleCloud;
        }
    }

    public static class CloudData implements ParticleEffect {
        public static final Factory<CloudData> DESERIALIZER = new Factory<>() {
            public CloudData read(ParticleType<CloudData> particleTypeIn, StringReader reader) throws CommandSyntaxException {
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
                reader.expect(' ');
                float airDrag = (float) reader.readDouble();
                return new CloudData(particleTypeIn, r, g, b, scale, duration, EnumCloudBehavior.CONSTANT, airDrag);
            }

            public CloudData read(ParticleType<CloudData> particleTypeIn, PacketByteBuf buffer) {
                return new CloudData(particleTypeIn, buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readInt(), EnumCloudBehavior.CONSTANT, buffer.readFloat());
            }
        };

        private final ParticleType<CloudData> type;

        private final float r;
        private final float g;
        private final float b;
        private final float scale;
        private final int duration;
        private final EnumCloudBehavior behavior;
        private final float airDrag;

        public CloudData(ParticleType<CloudData> type, float r, float g, float b, float scale, int duration, EnumCloudBehavior behavior, float airDrag) {
            this.type = type;
            this.r = r;
            this.g = g;
            this.b = b;
            this.scale = scale;
            this.behavior = behavior;
            this.airDrag = airDrag;
            this.duration = duration;
        }

        public static Codec<CloudData> CODEC(ParticleType<CloudData> particleType) {
            return RecordCodecBuilder.create((codecBuilder) -> codecBuilder.group(
                            Codec.FLOAT.fieldOf("r").forGetter(CloudData::getR),
                            Codec.FLOAT.fieldOf("g").forGetter(CloudData::getG),
                            Codec.FLOAT.fieldOf("b").forGetter(CloudData::getB),
                            Codec.FLOAT.fieldOf("scale").forGetter(CloudData::getScale),
                            Codec.STRING.fieldOf("behavior").forGetter((cloudData) -> cloudData.getBehavior().toString()),
                            Codec.INT.fieldOf("duration").forGetter(CloudData::getDuration),
                            Codec.FLOAT.fieldOf("airdrag").forGetter(CloudData::getAirDrag)
                    ).apply(codecBuilder, (r, g, b, scale, behavior, duration, airdrag) ->
                            new CloudData(particleType, r, g, b, scale, duration, EnumCloudBehavior.valueOf(behavior), airdrag))
            );
        }

        @Override
        public void write(PacketByteBuf buffer) {
            buffer.writeFloat(this.r);
            buffer.writeFloat(this.g);
            buffer.writeFloat(this.b);
            buffer.writeFloat(this.scale);
            buffer.writeInt(this.duration);
            buffer.writeFloat(this.airDrag);
        }

        @Override
        public String asString() {
            return String.format(Locale.ROOT, "%s %.2f %.2f %.2f %.2f %d %.2f", Registries.PARTICLE_TYPE.getId(this.getType()),
                    this.r, this.g, this.b, this.scale, this.duration, this.airDrag);
        }

        @Override
        public ParticleType<CloudData> getType() {
            return this.type;
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
        public EnumCloudBehavior getBehavior() {
            return this.behavior;
        }

        @Environment(EnvType.CLIENT)
        public int getDuration() {
            return this.duration;
        }

        @Environment(EnvType.CLIENT)
        public float getAirDrag() {
            return this.airDrag;
        }
    }
}

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
import org.joml.AxisAngle4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.Locale;

/**
 * Created by BobMowzie on 6/2/2017.
 */
public class ParticleSnowFlake extends SpriteBillboardParticle {
    private final float spread;
    boolean swirls;
    private int swirlTick;

    public ParticleSnowFlake(ClientWorld world, double x, double y, double z, double vX, double vY, double vZ, double duration, boolean swirls) {
        super(world, x, y, z);
        this.setBoundingBoxSpacing(1, 1);
        this.velocityX = vX;
        this.velocityY = vY;
        this.velocityZ = vZ;
        this.maxAge = (int) duration;
        this.swirlTick = this.random.nextInt(120);
        this.spread = this.random.nextFloat();
        this.swirls = swirls;
    }

    @Override
    protected float getMaxU() {
        return super.getMaxU() - (super.getMaxU() - super.getMinU()) / 8f;
    }

    @Override
    protected float getMaxV() {
        return super.getMaxV() - (super.getMaxV() - super.getMinV()) / 8f;
    }

    @Override
    public ParticleTextureSheet getType() {
        return MMRenderType.PARTICLE_SHEET_TRANSLUCENT_NO_DEPTH;
    }

    @Override
    public void tick() {
        super.tick();

        if (this.swirls) {
            Vector3f motionVec = new Vector3f((float) this.velocityX, (float) this.velocityY, (float) this.velocityZ);
            motionVec.normalize();
            float yaw = (float) Math.atan2(motionVec.x(), motionVec.z());
            float pitch = (float) Math.atan2(motionVec.y(), 1);
            float swirlRadius = 4f * (this.age / (float) this.maxAge) * this.spread;
            Quaternionf quatSpin = new Quaternionf(new AxisAngle4f(this.swirlTick * 0.2f, motionVec));
            Quaternionf quatOrient = MathUtils.quatFromRotationXYZ(pitch, yaw, 0, false);
            Vector3f vec = new Vector3f(swirlRadius, 0, 0);
            quatOrient.transform(vec);
            quatSpin.transform(vec);
            this.x += vec.x();
            this.y += vec.y();
            this.z += vec.z();
        }

        if (this.age >= this.maxAge) {
            this.markDead();
        }
        this.age++;
        this.swirlTick++;
    }

    @Override
    public void buildGeometry(VertexConsumer buffer, Camera renderInfo, float partialTicks) {
        float var = (this.age + partialTicks) / (float) this.maxAge;
        this.alpha = (float) (1 - Math.exp(10 * (var - 1)) - Math.pow(2000, -var));
        if (this.alpha < 0.01) this.alpha = 0.01f;
        super.buildGeometry(buffer, renderInfo, partialTicks);
    }

    @Environment(EnvType.CLIENT)
    public static final class SnowFlakeFactory implements ParticleFactory<SnowflakeData> {
        private final SpriteProvider spriteSet;

        public SnowFlakeFactory(SpriteProvider sprite) {
            this.spriteSet = sprite;
        }

        @Override
        public Particle createParticle(SnowflakeData typeIn, ClientWorld worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            ParticleSnowFlake particle = new ParticleSnowFlake(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, typeIn.getDuration(), typeIn.getSwirls());
            particle.setSprite(this.spriteSet);
            return particle;
        }
    }

    public static class SnowflakeData implements ParticleEffect {
        public static final Factory<SnowflakeData> DESERIALIZER = new Factory<SnowflakeData>() {
            public SnowflakeData read(ParticleType<SnowflakeData> particleTypeIn, StringReader reader) throws CommandSyntaxException {
                reader.expect(' ');
                float duration = (float) reader.readDouble();
                reader.expect(' ');
                boolean swirls = reader.readBoolean();
                return new SnowflakeData(duration, swirls);
            }

            public SnowflakeData read(ParticleType<SnowflakeData> particleTypeIn, PacketByteBuf buffer) {
                return new SnowflakeData(buffer.readFloat(), buffer.readBoolean());
            }
        };

        private final float duration;
        private final boolean swirls;

        public SnowflakeData(float duration, boolean spins) {
            this.duration = duration;
            this.swirls = spins;
        }

        public static Codec<SnowflakeData> CODEC(ParticleType<SnowflakeData> particleType) {
            return RecordCodecBuilder.create((codecBuilder) -> codecBuilder.group(
                            Codec.FLOAT.fieldOf("duration").forGetter(SnowflakeData::getDuration),
                            Codec.BOOL.fieldOf("swirls").forGetter(SnowflakeData::getSwirls)
                    ).apply(codecBuilder, SnowflakeData::new)
            );
        }

        @Override
        public void write(PacketByteBuf buffer) {
            buffer.writeFloat(this.duration);
            buffer.writeBoolean(this.swirls);
        }

        @Override
        public String asString() {
            return String.format(Locale.ROOT, "%s %.2f %b", Registries.PARTICLE_TYPE.getId(this.getType()),
                    this.duration, this.swirls);
        }

        @Override
        public ParticleType<SnowflakeData> getType() {
            return ParticleHandler.SNOWFLAKE;
        }

        @Environment(EnvType.CLIENT)
        public float getDuration() {
            return this.duration;
        }

        @Environment(EnvType.CLIENT)
        public boolean getSwirls() {
            return this.swirls;
        }
    }
}

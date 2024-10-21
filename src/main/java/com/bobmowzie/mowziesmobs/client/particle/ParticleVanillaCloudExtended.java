package com.bobmowzie.mowziesmobs.client.particle;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.util.math.Vec3d;

import java.util.Locale;

public class ParticleVanillaCloudExtended extends SpriteBillboardParticle {
    private final SpriteProvider animatedSprite;

    private final float oSize;
    private final float airDrag;
    private final float red;
    private final float green;
    private final float blue;

    private final Vec3d[] destination;

    protected ParticleVanillaCloudExtended(ClientWorld worldIn, SpriteProvider animatedSprite, double xCoordIn, double yCoordIn, double zCoordIn, double motionX, double motionY, double motionZ, double scale, double r, double g, double b, double drag, double duration, Vec3d[] destination) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, 0.0D, 0.0D, 0.0D);
        this.velocityX *= 0.10000000149011612D;
        this.velocityY *= 0.10000000149011612D;
        this.velocityZ *= 0.10000000149011612D;
        this.velocityX += motionX;
        this.velocityY += motionY;
        this.velocityZ += motionZ;
        float f1 = 1.0F - this.random.nextFloat() * 0.3F;
        this.red = (float) (f1 * r);
        this.green = (float) (f1 * g);
        this.blue = (float) (f1 * b);
        this.scale *= 0.75F;
        this.scale *= 2.5F;
        this.oSize = this.scale * (float) scale;
        this.maxAge = (int) duration;
        if (this.maxAge == 0) this.maxAge = 1;
        this.airDrag = (float) drag;
        this.destination = destination;
        this.collidesWithWorld = false;
        this.animatedSprite = animatedSprite;
        if (destination != null) this.setSprite(animatedSprite.getSprite(this.maxAge - this.age, this.maxAge));
        else this.setSpriteForAge(this.animatedSprite);
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public void tick() {
        this.prevPosX = this.x;
        this.prevPosY = this.y;
        this.prevPosZ = this.z;

        if (this.age++ >= this.maxAge) {
            this.markDead();
        }

        this.setSpriteForAge(this.animatedSprite);

        if (this.destination != null && this.destination.length == 1) {
            this.setSprite(this.animatedSprite.getSprite(this.maxAge - this.age, this.maxAge));

            Vec3d destinationVec = this.destination[0];
            Vec3d diff = destinationVec.subtract(new Vec3d(this.x, this.y, this.z));
            if (diff.length() < 0.5) this.markDead();
            float attractScale = 0.7f * ((float) this.age / (float) this.maxAge) * ((float) this.age / (float) this.maxAge);
            this.velocityX = diff.x * attractScale;
            this.velocityY = diff.y * attractScale;
            this.velocityZ = diff.z * attractScale;
        }
        this.move(this.velocityX, this.velocityY, this.velocityZ);
        this.velocityX *= this.airDrag;
        this.velocityY *= this.airDrag;
        this.velocityZ *= this.airDrag;

        if (this.onGround) {
            this.velocityX *= 0.699999988079071D;
            this.velocityZ *= 0.699999988079071D;
        }
    }

    @Environment(EnvType.CLIENT)
    public static final class CloudFactory implements ParticleFactory<VanillaCloudData> {
        private final SpriteProvider spriteSet;

        public CloudFactory(SpriteProvider sprite) {
            this.spriteSet = sprite;
        }

        @Override
        public Particle createParticle(VanillaCloudData typeIn, ClientWorld worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            ParticleVanillaCloudExtended particle = new ParticleVanillaCloudExtended(worldIn, this.spriteSet, x, y, z, xSpeed, ySpeed, zSpeed, typeIn.getScale(), typeIn.getRed(), typeIn.getGreen(), typeIn.getBlue(), typeIn.getDrag(), typeIn.getDuration(), typeIn.getDestination());
            particle.setColor(typeIn.getRed(), typeIn.getGreen(), typeIn.getBlue());
            return particle;
        }
    }

    public static class VanillaCloudData implements ParticleEffect {
        public static final Factory<VanillaCloudData> DESERIALIZER = new Factory<VanillaCloudData>() {
            public VanillaCloudData read(ParticleType<VanillaCloudData> particleTypeIn, StringReader reader) throws CommandSyntaxException {
                reader.expect(' ');
                float scale = (float) reader.readDouble();
                reader.expect(' ');
                float red = (float) reader.readDouble();
                reader.expect(' ');
                float green = (float) reader.readDouble();
                reader.expect(' ');
                float blue = (float) reader.readDouble();
                reader.expect(' ');
                float drag = (float) reader.readDouble();
                reader.expect(' ');
                float duration = (float) reader.readDouble();
                return new VanillaCloudData(scale, red, green, blue, drag, duration, null);
            }

            public VanillaCloudData read(ParticleType<VanillaCloudData> particleTypeIn, PacketByteBuf buffer) {
                return new VanillaCloudData(buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), null);
            }
        };

        private final float red;
        private final float green;
        private final float blue;
        private final float scale;
        private final float drag;
        private final float duration;
        private final Vec3d[] destination;

        public VanillaCloudData(float scale, float redIn, float greenIn, float blueIn, float drag, float duration, Vec3d[] destination) {
            this.red = redIn;
            this.green = greenIn;
            this.blue = blueIn;
            this.scale = scale;
            this.drag = drag;
            this.duration = duration;
            this.destination = destination;
        }

        public static Codec<VanillaCloudData> CODEC(ParticleType<VanillaCloudData> particleType) {
            return RecordCodecBuilder.create((codecBuilder) -> codecBuilder.group(
                            Codec.FLOAT.fieldOf("r").forGetter(VanillaCloudData::getRed),
                            Codec.FLOAT.fieldOf("g").forGetter(VanillaCloudData::getGreen),
                            Codec.FLOAT.fieldOf("b").forGetter(VanillaCloudData::getBlue),
                            Codec.FLOAT.fieldOf("scale").forGetter(VanillaCloudData::getScale),
                            Codec.FLOAT.fieldOf("duration").forGetter(VanillaCloudData::getDuration),
                            Codec.FLOAT.fieldOf("drag").forGetter(VanillaCloudData::getScale)
                    ).apply(codecBuilder, (r, g, b, scale, duration, drag) ->
                            new VanillaCloudData(r, g, b, scale, drag, duration, null))
            );
        }

        @Override
        public void write(PacketByteBuf buffer) {
            buffer.writeFloat(this.scale);
            buffer.writeFloat(this.red);
            buffer.writeFloat(this.green);
            buffer.writeFloat(this.blue);
            buffer.writeFloat(this.drag);
            buffer.writeFloat(this.duration);
        }

        @SuppressWarnings("deprecation")
        @Override
        public String asString() {
            return String.format(Locale.ROOT, "%s %.2f %.2f %.2f %.2f %.2f %.2f", Registries.PARTICLE_TYPE.getId(this.getType()),
                    this.scale, this.red, this.green, this.blue, this.drag, this.duration);
        }

        @Override
        public ParticleType<VanillaCloudData> getType() {
            return ParticleHandler.VANILLA_CLOUD_EXTENDED;
        }

        @Environment(EnvType.CLIENT)
        public float getScale() {
            return this.scale;
        }

        @Environment(EnvType.CLIENT)
        public float getRed() {
            return this.red;
        }

        @Environment(EnvType.CLIENT)
        public float getGreen() {
            return this.green;
        }

        @Environment(EnvType.CLIENT)
        public float getBlue() {
            return this.blue;
        }

        @Environment(EnvType.CLIENT)
        public float getDrag() {
            return this.drag;
        }

        @Environment(EnvType.CLIENT)
        public float getDuration() {
            return this.duration;
        }

        @Environment(EnvType.CLIENT)
        public Vec3d[] getDestination() {
            return this.destination;
        }
    }
}

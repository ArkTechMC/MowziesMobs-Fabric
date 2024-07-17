package com.bobmowzie.mowziesmobs.client.particle;

import com.bobmowzie.mowziesmobs.client.render.MMRenderType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;

/**
 * Created by BobMowzie on 6/2/2017.
 */
public class ParticleSparkle extends SpriteBillboardParticle {
    private final float red;
    private final float green;
    private final float blue;
    private float scale;

    public ParticleSparkle(ClientWorld world, double x, double y, double z, double vx, double vy, double vz, double r, double g, double b, double scale, int duration) {
        super(world, x, y, z);
        this.scale = (float) scale;
        this.maxAge = duration;
        this.velocityX = vx;
        this.velocityY = vy;
        this.velocityZ = vz;
        this.red = (float) r;
        this.green = (float) g;
        this.blue = (float) b;
        this.collidesWithWorld = false;
    }

    @Override
    protected float getMaxU() {
        return super.getMaxU() - (super.getMaxU() - super.getMinU()) / 16f;
    }

    @Override
    protected float getMaxV() {
        return super.getMaxV() - (super.getMaxV() - super.getMinV()) / 16f;
    }

    @Override
    public void buildGeometry(VertexConsumer buffer, Camera renderInfo, float partialTicks) {
        float a = ((float) this.age + partialTicks) / this.maxAge;
        this.alpha = -4 * a * a + 4 * a;
        if (this.alpha < 0.01) this.alpha = 0.01f;
        this.scale = (-4 * a * a + 4 * a) * this.scale;

        super.buildGeometry(buffer, renderInfo, partialTicks);
    }

    @Override
    public ParticleTextureSheet getType() {
        return MMRenderType.PARTICLE_SHEET_TRANSLUCENT_NO_DEPTH;
    }

    @Environment(EnvType.CLIENT)
    public static final class SparkleFactory implements ParticleFactory<DefaultParticleType> {

        private final SpriteProvider spriteSet;

        public SparkleFactory(SpriteProvider sprite) {
            this.spriteSet = sprite;
        }

        @Override
        public Particle createParticle(DefaultParticleType typeIn, ClientWorld worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            ParticleSparkle particle = new ParticleSparkle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, 1, 1, 1, 0.4d, 13);
            particle.setSprite(this.spriteSet);
            return particle;
        }
    }
}

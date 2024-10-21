package com.bobmowzie.mowziesmobs.client.particle;

import com.bobmowzie.mowziesmobs.client.particle.util.AdvancedParticleBase;
import com.bobmowzie.mowziesmobs.client.particle.util.DecalParticleData;
import com.bobmowzie.mowziesmobs.client.particle.util.ParticleComponent;
import com.bobmowzie.mowziesmobs.client.particle.util.ParticleRotation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.WorldView;

public class ParticleDecal extends AdvancedParticleBase {
    private final SpriteProvider sprites;
    protected int spriteSize = 8;
    protected int bufferSize = 32;

    protected ParticleDecal(ClientWorld worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double motionX, double motionY, double motionZ, ParticleRotation rotation, double scale, double r, double g, double b, double a, double drag, double duration, boolean emissive, boolean canCollide, SpriteProvider sprites, ParticleComponent[] components) {
        this(worldIn, xCoordIn, yCoordIn, zCoordIn, motionX, motionY, motionZ, rotation, scale, r, g, b, a, drag, duration, emissive, canCollide, sprites, 8, 32, components);
    }

    protected ParticleDecal(ClientWorld worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double motionX, double motionY, double motionZ, ParticleRotation rotation, double scale, double r, double g, double b, double a, double drag, double duration, boolean emissive, boolean canCollide, SpriteProvider sprites, int spriteSize, int bufferSize, ParticleComponent[] components) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, motionX, motionY, motionZ, rotation, scale, r, g, b, a, drag, duration, emissive, canCollide, components);
        this.spriteSize = spriteSize;
        this.bufferSize = bufferSize;
        this.setSpriteForAge(sprites);
        this.sprites = sprites;
    }

    private static Vec2f rotateVec2(Vec2f v, float angle) {
        return new Vec2f(v.x * (float) Math.cos(angle) - v.y * (float) Math.sin(angle),
                v.x * (float) Math.sin(angle) + v.y * (float) Math.cos(angle));
    }

    private static void renderBlockDecal(VertexConsumer buffer, Camera renderInfo, WorldView level, BlockPos blockPos, double x, double y, double z, float u0, float u1, float v0, float v1, float scale, float spriteScale, float alpha, float rotation, float r, float g, float b, int lightColor) {
        Vec2f center = new Vec2f((float) x, (float) z);
        BlockPos blockpos = blockPos.down();
        BlockState blockstate = level.getBlockState(blockpos);
        if (blockstate.getRenderType() != BlockRenderType.INVISIBLE) {
            if (blockstate.isFullCube(level, blockpos)) {
                VoxelShape voxelshape = blockstate.getOutlineShape(level, blockPos.down());
                if (!voxelshape.isEmpty()) {
                    float f = alpha;
                    if (f >= 0.0F) {
                        if (f > 1.0F) {
                            f = 1.0F;
                        }

                        double rad2 = Math.sqrt(2.0);
                        double minX = x - scale * spriteScale * rad2;
                        double minZ = z - scale * spriteScale * rad2;
                        double maxX = x + scale * spriteScale * rad2;
                        double maxZ = z + scale * spriteScale * rad2;
                        Box aabb = voxelshape.getBoundingBox();
                        float d0 = blockPos.getX() + (float) aabb.minX;
                        float d1 = blockPos.getX() + (float) aabb.maxX;
                        float d2 = blockPos.getY() + (float) aabb.minY + 0.015625f;
                        float d3 = blockPos.getZ() + (float) aabb.minZ;
                        float d4 = blockPos.getZ() + (float) aabb.maxZ;
                        if (d0 < minX) d0 = (float) minX;
                        if (d1 > maxX) d1 = (float) maxX;
                        if (d3 < minZ) d3 = (float) minZ;
                        if (d4 > maxZ) d4 = (float) maxZ;
                        Vec2f[] corners = new Vec2f[]{
                                new Vec2f(d0, d3),
                                new Vec2f(d1, d3),
                                new Vec2f(d1, d4),
                                new Vec2f(d0, d4),
                        };
                        for (Vec2f corner : corners) {
                            Vec2f cornerRelative = rotateVec2(corner.add(center.negate()), -rotation);
                            Vec2f uv = new Vec2f((cornerRelative.x / (2.0f * scale) + 0.5f) * (u1 - u0) + u0, (cornerRelative.y / (2.0f * scale) + 0.5f) * (v1 - v0) + v0);
                            decalVertex(buffer, renderInfo, f, corner.x, d2, corner.y, uv.x, uv.y, r, g, b, lightColor);
                        }
                    }

                }
            }
        }
    }

    private static void decalVertex(VertexConsumer buffer, Camera renderInfo, float alpha, float x, float y, float z, float u, float v, float r, float g, float b, int lightColor) {
        Vec3d vector3d = renderInfo.getPos();
//        Vector3d = new Vec3(0, 1, 0);
        buffer.vertex(x - vector3d.getX(), y - vector3d.getY(), z - vector3d.getZ()).texture(u, v).color(r, g, b, alpha).light(lightColor).next();
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

        this.setSprite(this.sprites.getSprite(Math.min(this.age, 5), 5));

        float decalRot = 0.0f;
        if (this.rotation instanceof ParticleRotation.EulerAngles eulerRot) {
            float rotY = eulerRot.prevYaw + (eulerRot.yaw - eulerRot.prevYaw) * partialTicks;
            decalRot = rotY;
        }

        float u0 = this.getMinU();
        float u1 = this.getMaxU();
        float v0 = this.getMinV();
        float v1 = this.getMaxV();
        int lightColor = this.getBrightness(partialTicks);

        float spriteScale = (float) this.spriteSize / (float) this.bufferSize;
        Vec3d minCorner = new Vec3d(-this.particleScale, -this.particleScale, -this.particleScale).rotateY(decalRot).add(this.x, this.y, this.z);
        Vec3d maxCorner = new Vec3d(this.particleScale, this.particleScale, this.particleScale).rotateY(decalRot).add(this.x, this.y, this.z);

        for (BlockPos blockpos : BlockPos.iterate(BlockPos.ofFloored(minCorner), BlockPos.ofFloored(maxCorner))) {
            renderBlockDecal(buffer, renderInfo, this.world, blockpos, this.x, this.y, this.z, u0, u1, v0, v1, this.particleScale, spriteScale, this.alpha, decalRot, this.red, this.green, this.blue, lightColor);
        }

        for (ParticleComponent component : this.components) {
            component.postRender(this, buffer, renderInfo, partialTicks, lightColor);
        }
    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<DecalParticleData> {
        private final SpriteProvider spriteSet;

        public Factory(SpriteProvider sprite) {
            this.spriteSet = sprite;
        }

        @Override
        public Particle createParticle(DecalParticleData typeIn, ClientWorld worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            ParticleDecal particle = new ParticleDecal(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, typeIn.getRotation(), typeIn.getScale(), typeIn.getRed(), typeIn.getGreen(), typeIn.getBlue(), typeIn.getAlpha(), typeIn.getAirDrag(), typeIn.getDuration(), typeIn.isEmissive(), typeIn.getCanCollide(), this.spriteSet, typeIn.getSpriteSize(), typeIn.getBufferSize(), typeIn.getComponents());
            particle.setColor((float) typeIn.getRed(), (float) typeIn.getGreen(), (float) typeIn.getBlue());
            return particle;
        }
    }
}

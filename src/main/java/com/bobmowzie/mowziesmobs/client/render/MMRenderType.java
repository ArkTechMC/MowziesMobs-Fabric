package com.bobmowzie.mowziesmobs.client.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.render.*;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public abstract class MMRenderType extends RenderLayer {
    public static ParticleTextureSheet PARTICLE_SHEET_TRANSLUCENT_NO_DEPTH = new ParticleTextureSheet() {
        public void begin(BufferBuilder p_217600_1_, TextureManager p_217600_2_) {
            RenderSystem.depthMask(false);
            RenderSystem.disableCull();
            RenderSystem.setShaderTexture(0, SpriteAtlasTexture.PARTICLE_ATLAS_TEXTURE);
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);
//            RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            p_217600_1_.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR_LIGHT);
        }

        public void draw(Tessellator p_217599_1_) {
            p_217599_1_.draw();
        }

        public String toString() {
            return "PARTICLE_SHEET_TRANSLUCENT_NO_DEPTH";
        }
    };

    public MMRenderType(String nameIn, VertexFormat formatIn, VertexFormat.DrawMode drawModeIn, int bufferSizeIn, boolean useDelegateIn, boolean needsSortingIn, Runnable setupTaskIn, Runnable clearTaskIn) {
        super(nameIn, formatIn, drawModeIn, bufferSizeIn, useDelegateIn, needsSortingIn, setupTaskIn, clearTaskIn);
    }

    public static RenderLayer getGlowingEffect(Identifier locationIn) {
        Texture shard = new Texture(locationIn, false, false);
        MultiPhaseParameters rendertype$state = MultiPhaseParameters.builder().texture(shard).program(BEACON_BEAM_PROGRAM).transparency(TRANSLUCENT_TRANSPARENCY).cull(DISABLE_CULLING).overlay(ENABLE_OVERLAY_COLOR).writeMaskState(COLOR_MASK).build(false);
        return of("glow_effect", VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL, VertexFormat.DrawMode.QUADS, 256, true, true, rendertype$state);
    }

    public static RenderLayer getSolarFlare(Identifier locationIn) {
        Texture shard = new Texture(locationIn, false, false);
        MultiPhaseParameters rendertype$state = MultiPhaseParameters.builder().texture(shard).program(BEACON_BEAM_PROGRAM).transparency(TRANSLUCENT_TRANSPARENCY).depthTest(ALWAYS_DEPTH_TEST).cull(DISABLE_CULLING).overlay(DISABLE_OVERLAY_COLOR).writeMaskState(COLOR_MASK).build(false);
        return of("solar_flare", VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL, VertexFormat.DrawMode.QUADS, 256, true, false, rendertype$state);
    }

    public static RenderLayer highlight(Identifier resourceLocation, float x, float y) {
        return of("highlight", VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL, VertexFormat.DrawMode.QUADS, 256, false, true, MultiPhaseParameters.builder().program(ENERGY_SWIRL_PROGRAM).texture(new Texture(resourceLocation, false, false)).texturing(new OffsetTexturing(x, y)).transparency(ADDITIVE_TRANSPARENCY).writeMaskState(COLOR_MASK).lightmap(ENABLE_LIGHTMAP).layering(POLYGON_OFFSET_LAYERING).build(false));
    }
}

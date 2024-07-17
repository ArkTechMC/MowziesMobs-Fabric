package com.bobmowzie.mowziesmobs.client.render.entity.layer;

import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieGeoBone;
import com.bobmowzie.mowziesmobs.client.render.MMRenderType;
import com.bobmowzie.mowziesmobs.client.render.entity.RenderSunstrike;
import com.bobmowzie.mowziesmobs.client.render.entity.RenderUmvuthi;
import com.bobmowzie.mowziesmobs.client.render.entity.player.GeckoRenderPlayer;
import com.bobmowzie.mowziesmobs.server.ability.AbilityHandler;
import com.bobmowzie.mowziesmobs.server.ability.abilities.player.heliomancy.SolarFlareAbility;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector4f;

public class SolarFlareLayer extends FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {
    private final GeckoRenderPlayer renderPlayerAnimated;

    public SolarFlareLayer(GeckoRenderPlayer entityRendererIn) {
        super(entityRendererIn);
        this.renderPlayerAnimated = entityRendererIn;
    }

    @Override
    public void render(MatrixStack matrixStackIn, VertexConsumerProvider bufferIn, int packedLightIn, AbstractClientPlayerEntity player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        SolarFlareAbility ability = (SolarFlareAbility) AbilityHandler.INSTANCE.getAbility(player, AbilityHandler.SOLAR_FLARE_ABILITY);
        if (ability != null && ability.isUsing() && ability.getTicksInUse() > RenderUmvuthi.BURST_START_FRAME && ability.getTicksInUse() < RenderUmvuthi.BURST_START_FRAME + RenderUmvuthi.BURST_FRAME_COUNT - 1) {
            matrixStackIn.push();
            MowzieGeoBone bone = this.renderPlayerAnimated.getAnimatedPlayerModel().getMowzieBone("Body");
            Vector4f vecTranslation = new Vector4f(0, 0, 0, 1);
            vecTranslation.mul(bone.getWorldSpaceMatrix());
            MatrixStack newMatrixStack = new MatrixStack();
            newMatrixStack.translate(vecTranslation.x(), vecTranslation.y(), vecTranslation.z());
            newMatrixStack.scale(0.8f, 0.8f, 0.8f);
            VertexConsumer ivertexbuilder = bufferIn.getBuffer(MMRenderType.getSolarFlare(RenderSunstrike.TEXTURE));
            MatrixStack.Entry matrixstack$entry2 = newMatrixStack.peek();
            Matrix4f matrix4f2 = matrixstack$entry2.getPositionMatrix();
            Matrix3f matrix3f = matrixstack$entry2.getNormalMatrix();
            RenderUmvuthi.drawBurst(matrix4f2, matrix3f, ivertexbuilder, ability.getTicksInUse() - RenderUmvuthi.BURST_START_FRAME + partialTicks, packedLightIn);
            matrixStackIn.pop();
        }
    }
}

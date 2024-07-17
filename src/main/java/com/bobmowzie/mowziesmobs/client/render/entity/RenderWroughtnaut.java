package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.entity.ModelWroughtnaut;
import com.bobmowzie.mowziesmobs.client.render.entity.layer.ItemLayer;
import com.bobmowzie.mowziesmobs.client.render.entity.layer.WroughtnautEyesLayer;
import com.bobmowzie.mowziesmobs.server.entity.wroughtnaut.EntityWroughtnaut;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

@Environment(EnvType.CLIENT)
public class RenderWroughtnaut extends MobEntityRenderer<EntityWroughtnaut, ModelWroughtnaut<EntityWroughtnaut>> {
    private static final Identifier TEXTURE = new Identifier(MowziesMobs.MODID, "textures/entity/wroughtnaut.png");

    public RenderWroughtnaut(EntityRendererFactory.Context mgr) {
        super(mgr, new ModelWroughtnaut<>(), 1.0F);
        this.addFeature(new WroughtnautEyesLayer<>(this));
        this.addFeature(new ItemLayer<>(this, this.getModel().sword, Items.DIAMOND_SWORD.getDefaultStack(), ModelTransformationMode.GROUND));
    }

    @Override
    public void render(EntityWroughtnaut p_115455_, float p_115456_, float p_115457_, MatrixStack p_115458_, VertexConsumerProvider p_115459_, int p_115460_) {
        super.render(p_115455_, p_115456_, p_115457_, p_115458_, p_115459_, p_115460_);
        if (this.dispatcher.shouldRenderHitboxes() && !p_115455_.isInvisible() && !MinecraftClient.getInstance().hasReducedDebugInfo()) {
            Vec3d forward = p_115455_.getRotationVecClient();
            Vec3d bodyFacing = Vec3d.fromPolar(0, p_115455_.bodyYaw);
            Matrix4f matrix4f = p_115458_.peek().getPositionMatrix();
            Matrix3f matrix3f = p_115458_.peek().getNormalMatrix();
            VertexConsumer consumer = p_115459_.getBuffer(RenderLayer.getLines());
            consumer.vertex(matrix4f, 0.0F, p_115455_.getStandingEyeHeight() + 0.1f, 0.0F).color(0, 255, 255, 255).normal(matrix3f, (float) forward.x, (float) forward.y, (float) forward.z).next();
            consumer.vertex(matrix4f, (float) (forward.x * 2.0D), (float) ((double) p_115455_.getStandingEyeHeight() + 0.1f + forward.y * 2.0D), (float) (forward.z * 2.0D)).color(0, 255, 255, 255).normal(matrix3f, (float) forward.x, (float) forward.y, (float) forward.z).next();

            consumer.vertex(matrix4f, 0.0F, p_115455_.getStandingEyeHeight() + 0.2f, 0.0F).color(255, 0, 255, 255).normal(matrix3f, (float) bodyFacing.x, (float) bodyFacing.y, (float) bodyFacing.z).next();
            consumer.vertex(matrix4f, (float) (bodyFacing.x * 2.0D), (float) ((double) p_115455_.getStandingEyeHeight() + 0.2f + bodyFacing.y * 2.0D), (float) (bodyFacing.z * 2.0D)).color(255, 0, 255, 255).normal(matrix3f, (float) bodyFacing.x, (float) bodyFacing.y, (float) bodyFacing.z).next();
        }
    }

    @Override
    protected float getLyingAngle(EntityWroughtnaut entity) {
        return 0;
    }

    @Override
    public Identifier getTexture(EntityWroughtnaut entity) {
        return RenderWroughtnaut.TEXTURE;
    }
}

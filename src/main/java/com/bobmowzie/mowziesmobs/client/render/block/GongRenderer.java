package com.bobmowzie.mowziesmobs.client.render.block;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.LayerHandler;
import com.bobmowzie.mowziesmobs.client.model.tools.MathUtils;
import com.bobmowzie.mowziesmobs.server.block.entity.GongBlockEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;

public class GongRenderer implements BlockEntityRenderer<GongBlockEntity> {
    public static final Identifier TEXTURE = new Identifier(MowziesMobs.MODID, "textures/block/gong.png");
    private final ModelPart gongBase;
    private final ModelPart chain;

    public GongRenderer(BlockEntityRendererFactory.Context context) {
        ModelPart modelpart = context.getLayerModelPart(LayerHandler.GONG_LAYER);
        this.gongBase = modelpart.getChild("root");
        this.chain = this.gongBase.getChild("chain");
    }

    public static TexturedModelData createBodyLayer() {
        ModelData meshdefinition = new ModelData();
        ModelPartData partdefinition = meshdefinition.getRoot();

        ModelPartData root = partdefinition.addChild("root", ModelPartBuilder.create().uv(69, 68).cuboid(-35.75F, -23.25F, 5.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.0F))
                .uv(35, 65).cuboid(0.25F, -57.25F, 7.0F, 4.0F, 34.0F, 4.0F, new Dilation(0.0F))
                .uv(69, 85).cuboid(-1.75F, -59.25F, 5.0F, 8.0F, 2.0F, 8.0F, new Dilation(0.0F))
                .uv(0, 69).cuboid(-1.75F, -23.25F, 5.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.0F))
                .uv(52, 68).cuboid(-33.75F, -57.25F, 7.0F, 4.0F, 34.0F, 4.0F, new Dilation(0.0F))
                .uv(0, 86).cuboid(-35.75F, -59.25F, 5.0F, 8.0F, 2.0F, 8.0F, new Dilation(0.0F))
                .uv(0, 17).cuboid(-37.75F, -56.25F, 7.5F, 46.0F, 2.0F, 3.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(-38.75F, -63.25F, 3.0F, 48.0F, 4.0F, 12.0F, new Dilation(0.0F))
                .uv(51, 23).cuboid(-27.75F, -59.25F, 8.5F, 26.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.pivot(14.75F, 39.25F, -8.5F));

        ModelPartData chain = root.addChild("chain", ModelPartBuilder.create().uv(51, 27).cuboid(-11.0F, 0.0F, 0.0F, 22.0F, 8.0F, 0.0F, new Dilation(0.0F)), ModelTransform.pivot(-14.75F, -54.25F, 8.5F));

        ModelPartData gong = chain.addChild("gong", ModelPartBuilder.create().uv(1, 24).cuboid(-11.75F, -11.75F, -1.0F, 22.0F, 22.0F, 2.0F, new Dilation(0.0F))
                .uv(51, 36).cuboid(-3.75F, -3.75F, -1.0F, 6.0F, 6.0F, 2.0F, new Dilation(0.0F))
                .uv(49, 47).cuboid(-9.75F, -9.75F, -1.0F, 18.0F, 18.0F, 2.0F, new Dilation(0.0F))
                .uv(0, 49).cuboid(-9.75F, -9.75F, -0.5F, 18.0F, 18.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.75F, 18.75F, 0.0F));

        return TexturedModelData.of(meshdefinition, 128, 128);
    }

    @Override
    public void render(GongBlockEntity entity, float delta, MatrixStack poseStack, VertexConsumerProvider buffer, int packedLight, int overlay) {
        poseStack.push();
        poseStack.translate(0.5, 1.485, 0.5);
        poseStack.multiply(MathUtils.quatFromRotationXYZ(0, 0, 180, true));
        if (entity.facing.getAxis() == Direction.Axis.X) {
            poseStack.multiply(MathUtils.quatFromRotationXYZ(0, 90, 0, true));
        }

        float f = (float) entity.ticks + delta;
        float f1 = 0.0F;
        if (entity.shaking) {
            float f3 = MathHelper.sin(f / (float) Math.PI) / (4.0F + f / 2.0F);
            if (entity.clickDirection == Direction.NORTH) {
                f1 = f3;
            } else if (entity.clickDirection == Direction.SOUTH) {
                f1 = -f3;
            } else if (entity.clickDirection == Direction.EAST) {
                f1 = f3;
            } else if (entity.clickDirection == Direction.WEST) {
                f1 = -f3;
            }
        }

        this.chain.pitch = f1;

        VertexConsumer vertexconsumer = buffer.getBuffer(RenderLayer.getEntityCutoutNoCull(TEXTURE));
        this.gongBase.render(poseStack, vertexconsumer, packedLight, overlay);
        poseStack.pop();
    }
}

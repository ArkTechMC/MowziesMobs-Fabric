package com.bobmowzie.mowziesmobs.client.model.entity;

import com.bobmowzie.mowziesmobs.client.model.tools.BlockModelRenderer;
import com.bobmowzie.mowziesmobs.server.entity.effects.geomancy.EntityBoulderBase;
import com.bobmowzie.mowziesmobs.server.entity.effects.geomancy.EntityGeomancyBase;
import com.bobmowzie.mowziesmobs.client.model.tools.AdvancedModelBase;
import com.bobmowzie.mowziesmobs.client.model.tools.AdvancedModelRenderer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;

import java.util.Random;

/**
 * Created by BobMowzie on 4/14/2017.
 */
public class ModelBoulder<T extends EntityBoulderBase> extends AdvancedModelBase {

    public BlockModelRenderer boulder0block1;
    public AdvancedModelRenderer boulder1;
    public BlockModelRenderer boulder1block1;
    public BlockModelRenderer boulder1block2;
    public BlockModelRenderer boulder1block3;
    public BlockModelRenderer boulder1block4;
    public BlockModelRenderer boulder1block5;
    public BlockModelRenderer boulder1block6;
    public AdvancedModelRenderer boulder2;
    public BlockModelRenderer boulder2block1;
    public BlockModelRenderer boulder2block2;
    public BlockModelRenderer boulder2block3;
    public BlockModelRenderer boulder2block4;
    public BlockModelRenderer boulder2block5;
    public BlockModelRenderer boulder2block6;
    public BlockModelRenderer boulder2block7;
    public BlockModelRenderer boulder2block8;
    public BlockModelRenderer boulder2block9;
    public BlockModelRenderer boulder2block10;
    public BlockModelRenderer boulder2block11;
    public BlockModelRenderer boulder2block12;
    public BlockModelRenderer boulder2block13;
    public BlockModelRenderer boulder2block14;
    public BlockModelRenderer boulder2block15;
    public BlockModelRenderer boulder2block16;

    public AdvancedModelRenderer boulder3;
    public BlockModelRenderer boulder3block1;
    public BlockModelRenderer boulder3block2;
    public BlockModelRenderer boulder3block3;
    public BlockModelRenderer boulder3block4;
    public BlockModelRenderer boulder3block5;
    public BlockModelRenderer boulder3block6;
    public BlockModelRenderer boulder3block7;
    public BlockModelRenderer boulder3block8;
    public BlockModelRenderer boulder3block9;
    public BlockModelRenderer boulder3block10;
    public BlockModelRenderer boulder3block11;
    public BlockModelRenderer boulder3block12;
    public BlockModelRenderer boulder3block13;
    public BlockModelRenderer boulder3block14;
    public BlockModelRenderer boulder3block15;
    public BlockModelRenderer boulder3block16;
    public BlockModelRenderer boulder3block17;
    public BlockModelRenderer boulder3block18;
    public BlockModelRenderer boulder3block19;
    public BlockModelRenderer boulder3block20;
    public BlockModelRenderer boulder3block21;
    public BlockModelRenderer boulder3block22;
    public BlockModelRenderer boulder3block23;
    public BlockModelRenderer boulder3block24;
    public BlockModelRenderer boulder3block25;
    public BlockModelRenderer boulder3block26;
    public BlockModelRenderer boulder3block27;
    public BlockModelRenderer boulder3block28;

    public BlockModelRenderer[] blockModels;

    private EntityGeomancyBase.GeomancyTier size;

    public ModelBoulder() {
        this.textureWidth = 16;
        this.textureHeight = 16;
        this.boulder0block1 = new BlockModelRenderer(this);
        this.boulder0block1.setRotationPoint(0F, -8F, 0F);
        this.boulder0block1.addBox(-8F, 8F, -8F, 0, 0, 0, 0.0F);
        this.boulder1 = new AdvancedModelRenderer(this);
        this.boulder1.setRotationPoint(0F, 0F, 0F);
        this.boulder1block1 = new BlockModelRenderer(this);
        this.boulder1block1.setRotationPoint(0, 0, 0);
        this.boulder0block1.addBox(-8F, 8F, -8F, 0, 0, 0, 0.0F);
        this.boulder1block2 = new BlockModelRenderer(this);
        this.boulder1block2.setRotationPoint(0, -10, 0);
        this.boulder1block2.addBox(-8F, 8F, -8F, 0, 0, 0, 0.0F);
        this.boulder1block2.setScale(0.99f, 1, 0.99f);
        this.boulder1block3 = new BlockModelRenderer(this);
        this.boulder1block3.setRotationPoint(0, -0.01f, 8);
        this.boulder1block3.addBox(-8F, 8F, -8F, 0, 0, 0, 0.0F);
        this.boulder1block4 = new BlockModelRenderer(this);
        this.boulder1block4.setRotationPoint(8, -0.01f, 0);
        this.boulder1block4.addBox(-8F, 8F, -8F, 0, 0, 0, 0.0F);
        this.boulder1block5 = new BlockModelRenderer(this);
        this.boulder1block5.setRotationPoint(-8, -0.01f, 0);
        this.boulder1block5.addBox(-8F, 8F, -8F, 0, 0, 0, 0.0F);
        this.boulder1block6 = new BlockModelRenderer(this);
        this.boulder1block6.setRotationPoint(0, -0.01f, -8);
        this.boulder1block6.addBox(-8F, 8F, -8F, 0, 0, 0, 0.0F);

        this.boulder2block9 = new BlockModelRenderer(this);
        this.boulder2block9.setRotationPoint(10.0F, 16.0F, -10.0F);
        this.boulder2block9.addBox(-8.0F, -8.0F, -8.0F, 0, 0, 0, 0.0F);
        this.boulder2block15 = new BlockModelRenderer(this);
        this.boulder2block15.setRotationPoint(8.0F, 0.0F, 0.0F);
        this.boulder2block15.addBox(-8.0F, -8.0F, -8.0F, 0, 0, 0, 0.0F);
        this.boulder2block3 = new BlockModelRenderer(this);
        this.boulder2block3.setRotationPoint(0.0F, 16.0F, 0.0F);
        this.boulder2block3.addBox(-8.0F, -8.0F, -8.0F, 0, 0, 0, 0.0F);
        this.boulder2block5 = new BlockModelRenderer(this);
        this.boulder2block5.setRotationPoint(-16.0F, 16.0F, 0.0F);
        this.boulder2block5.addBox(-8.0F, -8.0F, -8.0F, 0, 0, 0, 0.0F);
        this.boulder2block12 = new BlockModelRenderer(this);
        this.boulder2block12.setRotationPoint(-10.0F, 16.0F, -10.0F);
        this.boulder2block12.addBox(-8.0F, -8.0F, -8.0F, 0, 0, 0, 0.0F);
        this.boulder2block16 = new BlockModelRenderer(this);
        this.boulder2block16.setRotationPoint(0.0F, 0.0F, 8.0F);
        this.boulder2block16.addBox(-8.0F, -8.0F, -8.0F, 0, 0, 0, 0.0F);
        this.boulder2block4 = new BlockModelRenderer(this);
        this.boulder2block4.setRotationPoint(16.0F, 16.0F, 0.0F);
        this.boulder2block4.addBox(-8.0F, -8.0F, -8.0F, 0, 0, 0, 0.0F);
        this.boulder2block8 = new BlockModelRenderer(this);
        this.boulder2block8.setRotationPoint(10.0F, 16.0F, 10.0F);
        this.boulder2block8.addBox(-8.0F, -8.0F, -8.0F, 0, 0, 0, 0.0F);
        this.boulder2 = new AdvancedModelRenderer(this);
        this.boulder2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.boulder2block10 = new BlockModelRenderer(this);
        this.boulder2block10.setRotationPoint(-10.0F, 16.0F, -10.0F);
        this.boulder2block10.addBox(-8.0F, -8.0F, -8.0F, 0, 0, 0, 0.0F);
        this.boulder2block7 = new BlockModelRenderer(this);
        this.boulder2block7.setRotationPoint(0.0F, 16.0F, 16.0F);
        this.boulder2block7.addBox(-8.0F, -8.0F, -8.0F, 0, 0, 0, 0.0F);
        this.boulder2block6 = new BlockModelRenderer(this);
        this.boulder2block6.setRotationPoint(0.0F, 16.0F, -16.0F);
        this.boulder2block6.addBox(-8.0F, -8.0F, -8.0F, 0, 0, 0, 0.0F);
        this.boulder2block11 = new BlockModelRenderer(this);
        this.boulder2block11.setRotationPoint(-10.0F, 16.0F, 10.0F);
        this.boulder2block11.addBox(-8.0F, -8.0F, -8.0F, 0, 0, 0, 0.0F);
        this.boulder2block1 = new BlockModelRenderer(this);
        this.boulder2block1.setRotationPoint(0.0F, -8.0F, 0.0F);
        this.boulder2block1.addBox(-8.0F, -8.0F, -8.0F, 0, 0, 0, 0.0F);
        this.boulder2block14 = new BlockModelRenderer(this);
        this.boulder2block14.setRotationPoint(-8.0F, 0.0F, 0.0F);
        this.boulder2block14.addBox(-8.0F, -8.0F, -8.0F, 0, 0, 0, 0.0F);
        this.boulder2block13 = new BlockModelRenderer(this);
        this.boulder2block13.setRotationPoint(0.0F, 0.0F, -8.0F);
        this.boulder2block13.addBox(-8.0F, -8.0F, -8.0F, 0, 0, 0, 0.0F);
        this.boulder2block2 = new BlockModelRenderer(this);
        this.boulder2block2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.boulder2block2.addBox(-8.0F, -8.0F, -8.0F, 0, 0, 0, 0.0F);

        this.boulder3block9 = new BlockModelRenderer(this);
        this.boulder3block9.setRotationPoint(10.0F, 16.0F, -10.0F);
        this.boulder3block9.addBox(-8.0F, -8.0F, -8.0F, 0, 0, 0, 0.0F);
        this.boulder3block15 = new BlockModelRenderer(this);
        this.boulder3block15.setRotationPoint(8.0F, 0.0F, 0.0F);
        this.boulder3block15.addBox(-8.0F, -8.0F, -8.0F, 0, 0, 0, 0.0F);
        this.boulder3block3 = new BlockModelRenderer(this);
        this.boulder3block3.setRotationPoint(0.0F, 16.0F, 0.0F);
        this.boulder3block3.addBox(-8.0F, -8.0F, -8.0F, 0, 0, 0, 0.0F);
        this.boulder3block5 = new BlockModelRenderer(this);
        this.boulder3block5.setRotationPoint(-16.0F, 16.0F, 0.0F);
        this.boulder3block5.addBox(-8.0F, -8.0F, -8.0F, 0, 0, 0, 0.0F);
        this.boulder3block16 = new BlockModelRenderer(this);
        this.boulder3block16.setRotationPoint(0.0F, 0.0F, 8.0F);
        this.boulder3block16.addBox(-8.0F, -8.0F, -8.0F, 0, 0, 0, 0.0F);
        this.boulder3block4 = new BlockModelRenderer(this);
        this.boulder3block4.setRotationPoint(16.0F, 16.0F, 0.0F);
        this.boulder3block4.addBox(-8.0F, -8.0F, -8.0F, 0, 0, 0, 0.0F);
        this.boulder3block8 = new BlockModelRenderer(this);
        this.boulder3block8.setRotationPoint(10.0F, 16.0F, 10.0F);
        this.boulder3block8.addBox(-8.0F, -8.0F, -8.0F, 0, 0, 0, 0.0F);
        this.boulder3 = new AdvancedModelRenderer(this);
        this.boulder3.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.boulder3block10 = new BlockModelRenderer(this);
        this.boulder3block10.setRotationPoint(-10.0F, 16.0F, -10.0F);
        this.boulder3block10.addBox(-8.0F, -8.0F, -8.0F, 0, 0, 0, 0.0F);
        this.boulder3block7 = new BlockModelRenderer(this);
        this.boulder3block7.setRotationPoint(0.0F, 16.0F, 16.0F);
        this.boulder3block7.addBox(-8.0F, -8.0F, -8.0F, 0, 0, 0, 0.0F);
        this.boulder3block6 = new BlockModelRenderer(this);
        this.boulder3block6.setRotationPoint(0.0F, 16.0F, -16.0F);
        this.boulder3block6.addBox(-8.0F, -8.0F, -8.0F, 0, 0, 0, 0.0F);
        this.boulder3block11 = new BlockModelRenderer(this);
        this.boulder3block11.setRotationPoint(-10.0F, 16.0F, 10.0F);
        this.boulder3block11.addBox(-8.0F, -8.0F, -8.0F, 0, 0, 0, 0.0F);
        this.boulder3block12 = new BlockModelRenderer(this);
        this.boulder3block12.setRotationPoint(0.0F, -16.0F, 0.0F);
        this.boulder3block12.addBox(-8.0F, -8.0F, -8.0F, 0, 0, 0, 0.0F);
        this.boulder3block1 = new BlockModelRenderer(this);
        this.boulder3block1.setRotationPoint(0.0F, -8.0F, 0.0F);
        this.boulder3block1.addBox(-8.0F, -8.0F, -8.0F, 0, 0, 0, 0.0F);
        this.boulder3block14 = new BlockModelRenderer(this);
        this.boulder3block14.setRotationPoint(-8.0F, 0.0F, 0.0F);
        this.boulder3block14.addBox(-8.0F, -8.0F, -8.0F, 0, 0, 0, 0.0F);
        this.boulder3block13 = new BlockModelRenderer(this);
        this.boulder3block13.setRotationPoint(0.0F, 0.0F, -8.0F);
        this.boulder3block13.addBox(-8.0F, -8.0F, -8.0F, 0, 0, 0, 0.0F);
        this.boulder3block2 = new BlockModelRenderer(this);
        this.boulder3block2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.boulder3block2.addBox(-8.0F, -8.0F, -8.0F, 0, 0, 0, 0.0F);
        this.boulder3block17 = new BlockModelRenderer(this);
        this.boulder3block17.setRotationPoint(0.0F, 0.0F, -8.0F);
        this.boulder3block17.addBox(-8.0F, -8.0F, -8.0F, 0, 0, 0, 0.0F);
        this.boulder3block18 = new BlockModelRenderer(this);
        this.boulder3block18.setRotationPoint(0.0F, 0.0F, 8.0F);
        this.boulder3block18.addBox(-8.0F, -8.0F, -8.0F, 0, 0, 0, 0.0F);
        this.boulder3block19 = new BlockModelRenderer(this);
        this.boulder3block19.setRotationPoint(-8.0F, 0.0F, 0.0F);
        this.boulder3block19.addBox(-8.0F, -8.0F, -8.0F, 0, 0, 0, 0.0F);
        this.boulder3block20 = new BlockModelRenderer(this);
        this.boulder3block20.setRotationPoint(8.0F, 0.0F, 0.0F);
        this.boulder3block20.addBox(-8.0F, -8.0F, -8.0F, 0, 0, 0, 0.0F);
        this.boulder3block21 = new BlockModelRenderer(this);
        this.boulder3block21.setRotationPoint(8.0F, 0.0F, 8.0F);
        this.boulder3block21.addBox(-8.0F, -8.0F, -8.0F, 0, 0, 0, 0.0F);
        this.boulder3block22 = new BlockModelRenderer(this);
        this.boulder3block22.setRotationPoint(-8.0F, 0.0F, 8.0F);
        this.boulder3block22.addBox(-8.0F, -8.0F, -8.0F, 0, 0, 0, 0.0F);
        this.boulder3block23 = new BlockModelRenderer(this);
        this.boulder3block23.setRotationPoint(-8.0F, 0.0F, -8.0F);
        this.boulder3block23.addBox(-8.0F, -8.0F, -8.0F, 0, 0, 0, 0.0F);
        this.boulder3block24 = new BlockModelRenderer(this);
        this.boulder3block24.setRotationPoint(8.0F, 0.0F, -8.0F);
        this.boulder3block24.addBox(-8.0F, -8.0F, -8.0F, 0, 0, 0, 0.0F);
        this.boulder3block25 = new BlockModelRenderer(this);
        this.boulder3block25.setRotationPoint(16.0F, 16.0F, 0.0F);
        this.boulder3block25.addBox(-8.0F, -8.0F, -8.0F, 0, 0, 0, 0.0F);
        this.boulder3block26 = new BlockModelRenderer(this);
        this.boulder3block26.setRotationPoint(-16.0F, 16.0F, 0.0F);
        this.boulder3block26.addBox(-8.0F, -8.0F, -8.0F, 0, 0, 0, 0.0F);
        this.boulder3block27 = new BlockModelRenderer(this);
        this.boulder3block27.setRotationPoint(0.0F, 16.0F, -16.0F);
        this.boulder3block27.addBox(-8.0F, -8.0F, -8.0F, 0, 0, 0, 0.0F);
        this.boulder3block28 = new BlockModelRenderer(this);
        this.boulder3block28.setRotationPoint(0.0F, 16.0F, 16.0F);
        this.boulder3block28.addBox(-8.0F, -8.0F, -8.0F, 0, 0, 0, 0.0F);

        this.boulder3.addChild(this.boulder3block9);
        this.boulder3.addChild(this.boulder3block15);
        this.boulder3.addChild(this.boulder3block3);
        this.boulder3.addChild(this.boulder3block5);
        this.boulder3.addChild(this.boulder3block16);
        this.boulder3.addChild(this.boulder3block4);
        this.boulder3.addChild(this.boulder3block8);
        this.boulder3.addChild(this.boulder3block10);
        this.boulder3.addChild(this.boulder3block7);
        this.boulder3.addChild(this.boulder3block6);
        this.boulder3.addChild(this.boulder3block11);
        this.boulder3.addChild(this.boulder3block1);
        this.boulder3.addChild(this.boulder3block14);
        this.boulder3.addChild(this.boulder3block12);
        this.boulder3.addChild(this.boulder3block13);
        this.boulder3.addChild(this.boulder3block2);
        this.boulder3.addChild(this.boulder3block17);
        this.boulder3.addChild(this.boulder3block18);
        this.boulder3.addChild(this.boulder3block19);
        this.boulder3.addChild(this.boulder3block20);
        this.boulder3.addChild(this.boulder3block21);
        this.boulder3.addChild(this.boulder3block22);
        this.boulder3.addChild(this.boulder3block23);
        this.boulder3.addChild(this.boulder3block24);
        this.boulder3.addChild(this.boulder3block25);
        this.boulder3.addChild(this.boulder3block26);
        this.boulder3.addChild(this.boulder3block27);
        this.boulder3.addChild(this.boulder3block28);

        this.boulder2.addChild(this.boulder2block9);
        this.boulder2.addChild(this.boulder2block15);
        this.boulder2.addChild(this.boulder2block3);
        this.boulder2.addChild(this.boulder2block5);
        this.boulder2.addChild(this.boulder2block12);
        this.boulder2.addChild(this.boulder2block16);
        this.boulder2.addChild(this.boulder2block4);
        this.boulder2.addChild(this.boulder2block8);
        this.boulder2.addChild(this.boulder2block10);
        this.boulder2.addChild(this.boulder2block7);
        this.boulder2.addChild(this.boulder2block6);
        this.boulder2.addChild(this.boulder2block11);
        this.boulder2.addChild(this.boulder2block1);
        this.boulder2.addChild(this.boulder2block14);
        this.boulder2.addChild(this.boulder2block13);
        this.boulder2.addChild(this.boulder2block2);

        this.boulder1.addChild(this.boulder1block1);
        this.boulder1.addChild(this.boulder1block2);
        this.boulder1.addChild(this.boulder1block3);
        this.boulder1.addChild(this.boulder1block4);
        this.boulder1.addChild(this.boulder1block5);
        this.boulder1.addChild(this.boulder1block6);

        this.blockModels = new BlockModelRenderer[]{this.boulder0block1, this.boulder1block1, this.boulder1block2, this.boulder1block3, this.boulder1block4, this.boulder1block5, this.boulder1block6, this.boulder2block1, this.boulder2block2, this.boulder2block3, this.boulder2block4, this.boulder2block5, this.boulder2block6, this.boulder2block7, this.boulder2block8, this.boulder2block9, this.boulder2block10, this.boulder2block11, this.boulder2block12, this.boulder2block13, this.boulder2block14, this.boulder2block15, this.boulder2block16,
                this.boulder3block1, this.boulder3block2, this.boulder3block3, this.boulder3block4, this.boulder3block5, this.boulder3block6, this.boulder3block7, this.boulder3block8, this.boulder3block9, this.boulder3block10, this.boulder3block11, this.boulder3block12, this.boulder3block13, this.boulder3block14, this.boulder3block15, this.boulder3block16, this.boulder3block17, this.boulder3block18, this.boulder3block19, this.boulder3block20, this.boulder3block21, this.boulder3block22, this.boulder3block23, this.boulder3block24, this.boulder3block25, this.boulder3block26, this.boulder3block27, this.boulder3block28
        };
        Random rng = new Random(0x11c08b85b1943001L);
        for (BlockModelRenderer blockModel : this.blockModels) {
            float scale = rng.nextFloat() * 0.01f - 0.005f;
            blockModel.setScale(1 + scale, 1 + scale, 1 + scale);
        }
        this.updateDefaultPose();
    }

    @Override
    public void render(MatrixStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        if (this.size == EntityGeomancyBase.GeomancyTier.SMALL)
            this.boulder0block1.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        else if (this.size == EntityGeomancyBase.GeomancyTier.MEDIUM)
            this.boulder1.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        else if (this.size == EntityGeomancyBase.GeomancyTier.LARGE)
            this.boulder2.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        else this.boulder3.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }

    @Override
    public void setAngles(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entityIn instanceof EntityBoulderBase entity) {
            this.size = entity.getTier();
            this.resetToDefaultPose();
            int tick = Math.max(entity.age, 0);
            float delta = ageInTicks - entity.age;
            for (int i = 0; i < this.blockModels.length; i++) {
                this.blockModels[i].setBlockState(entity.getBlock());
            }
            this.boulder0block1.rotationPointY += -32 * (float) (Math.pow(0.6 * (tick + delta + 1), -3));
            this.boulder0block1.rotationPointY += 2 * Math.cos(0.1f * (entity.age + entity.animationOffset + delta));

            this.boulder1.rotationPointY += -32 * (float) (Math.pow(0.2 * (tick + delta + 1), -3));
            this.boulder1.rotationPointY += 2.4 * Math.cos(0.07f * (entity.age + entity.animationOffset + delta));

            this.boulder2.rotationPointY += -8 * (float) (Math.pow(0.05 * (tick + delta + 1), -1));
            this.boulder2.rotationPointY += 2.8 * Math.cos(0.04f * (entity.age + entity.animationOffset + delta));

            this.boulder3.rotationPointY += -90 + Math.min(90, 1.2 * (tick + delta));
            this.boulder3.rotationPointY += 3.2 * Math.cos(0.03f * (entity.age + entity.animationOffset + delta));

            this.boulder3.rotationPointY += 16;
            this.boulder3block1.rotationPointY -= 8;
            this.boulder3block2.rotationPointY -= 8;
            this.boulder3block4.rotationPointX += 8;
            this.boulder3block5.rotationPointX -= 8;
            this.boulder3block6.rotationPointZ -= 8;
            this.boulder3block7.rotationPointZ += 8;
            this.boulder3block8.rotationPointX += 6;
            this.boulder3block8.rotationPointZ += 6;
            this.boulder3block9.rotationPointX += 6;
            this.boulder3block9.rotationPointZ -= 6;
            this.boulder3block10.rotationPointX -= 6;
            this.boulder3block10.rotationPointZ -= 6;
            this.boulder3block11.rotationPointX -= 6;
            this.boulder3block11.rotationPointZ += 6;
            this.boulder3block12.rotationPointY -= 8;
            this.boulder3block13.rotationPointZ -= 8;
            this.boulder3block14.rotationPointX -= 8;
            this.boulder3block15.rotationPointX += 8;
            this.boulder3block16.rotationPointZ += 8;
            this.boulder3block17.rotationPointY -= 16;
            this.boulder3block18.rotationPointY -= 16;
            this.boulder3block19.rotationPointY -= 16;
            this.boulder3block20.rotationPointY -= 16;
        }
    }
}

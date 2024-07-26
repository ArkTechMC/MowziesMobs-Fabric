package com.bobmowzie.mowziesmobs.client.model.entity;

import com.bobmowzie.mowziesmobs.client.model.tools.AdvancedModelBase;
import com.bobmowzie.mowziesmobs.client.model.tools.MMModelAnimator;
import com.bobmowzie.mowziesmobs.server.entity.MowzieLLibraryEntity;
import com.iafenvoy.uranus.client.model.tools.BasicModelRenderer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;

import java.util.function.Function;

public abstract class MowzieEntityModel<T extends MowzieLLibraryEntity> extends AdvancedModelBase<T> {
    protected final MMModelAnimator animator = MMModelAnimator.create();

    protected MowzieEntityModel() {
        super();
    }

    protected MowzieEntityModel(Function<Identifier, RenderLayer> renderTypeFunction) {
        super(renderTypeFunction);
    }

    protected static void setRotateAngle(BasicModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

    @Override
    public void setAngles(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        float delta = ageInTicks - entityIn.age;
        this.animator.update(entityIn, delta);
        this.animate(entityIn, limbSwing, limbSwingAmount, netHeadYaw, headPitch, delta);
    }

    @SuppressWarnings("unchecked")
    private T cast(Entity entity) {
        return (T) entity;
    }

    protected abstract void animate(T entity, float limbSwing, float limbSwingAmount, float headYaw, float headPitch, float delta);
}

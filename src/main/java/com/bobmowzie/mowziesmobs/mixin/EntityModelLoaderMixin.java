package com.bobmowzie.mowziesmobs.mixin;

import com.bobmowzie.mowziesmobs.client.MowziesMobsClient;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.resource.ResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityModelLoader.class)
public class EntityModelLoaderMixin {
    @Unique
    private boolean loaded = false;

    @Inject(method = "reload", at = @At("RETURN"))
    private void onReload(ResourceManager manager, CallbackInfo ci) {
        if(!this.loaded){
            this.loaded =true;
            MowziesMobsClient.bootstrapItemRenderers();
        }
    }
}

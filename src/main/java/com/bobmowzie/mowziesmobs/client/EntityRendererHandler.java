package com.bobmowzie.mowziesmobs.client;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.gui.GuiSculptorTrade;
import com.bobmowzie.mowziesmobs.client.gui.GuiUmvuthanaTrade;
import com.bobmowzie.mowziesmobs.client.gui.GuiUmvuthiTrade;
import com.bobmowzie.mowziesmobs.client.render.block.GongRenderer;
import com.bobmowzie.mowziesmobs.client.render.entity.*;
import com.bobmowzie.mowziesmobs.server.block.entity.BlockEntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.umvuthana.MaskType;
import com.bobmowzie.mowziesmobs.server.inventory.ContainerHandler;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.util.Identifier;

import java.util.Collection;

public class EntityRendererHandler {
    public static void registerEntityRenderer() {
        EntityRendererRegistry.register(EntityHandler.BABY_FOLIAATH, RenderFoliaathBaby::new);
        EntityRendererRegistry.register(EntityHandler.FOLIAATH, RenderFoliaath::new);
        EntityRendererRegistry.register(EntityHandler.WROUGHTNAUT, RenderWroughtnaut::new);
        EntityRendererRegistry.register(EntityHandler.UMVUTHI, RenderUmvuthi::new);
        EntityRendererRegistry.register(EntityHandler.UMVUTHANA_RAPTOR, RenderUmvuthana::new);
        EntityRendererRegistry.register(EntityHandler.UMVUTHANA_FOLLOWER_TO_RAPTOR, RenderUmvuthana::new);
        EntityRendererRegistry.register(EntityHandler.UMVUTHANA_MINION, RenderUmvuthana::new);
        EntityRendererRegistry.register(EntityHandler.UMVUTHANA_FOLLOWER_TO_PLAYER, RenderUmvuthana::new);
        EntityRendererRegistry.register(EntityHandler.UMVUTHANA_CRANE_TO_PLAYER, RenderUmvuthana::new);
        EntityRendererRegistry.register(EntityHandler.UMVUTHANA_CRANE, RenderUmvuthana::new);
        EntityRendererRegistry.register(EntityHandler.FROSTMAW, RenderFrostmaw::new);
        EntityRendererRegistry.register(EntityHandler.GROTTOL, RenderGrottol::new);
        EntityRendererRegistry.register(EntityHandler.LANTERN, RenderLantern::new);
        EntityRendererRegistry.register(EntityHandler.NAGA, RenderNaga::new);
        EntityRendererRegistry.register(EntityHandler.SCULPTOR, RenderSculptor::new);

        EntityRendererRegistry.register(EntityHandler.DART, RenderDart::new);
        EntityRendererRegistry.register(EntityHandler.SUNSTRIKE, RenderSunstrike::new);
        EntityRendererRegistry.register(EntityHandler.SOLAR_BEAM, RenderSolarBeam::new);
        EntityRendererRegistry.register(EntityHandler.BOULDER_PROJECTILE, RenderBoulder::new);
        EntityRendererRegistry.register(EntityHandler.BOULDER_SCULPTOR, RenderBoulder::new);
        EntityRendererRegistry.register(EntityHandler.PILLAR, RenderPillar::new);
        EntityRendererRegistry.register(EntityHandler.PILLAR_SCULPTOR, RenderPillar::new);
        EntityRendererRegistry.register(EntityHandler.PILLAR_PIECE, RenderNothing::new);
        EntityRendererRegistry.register(EntityHandler.AXE_ATTACK, RenderAxeAttack::new);
        EntityRendererRegistry.register(EntityHandler.POISON_BALL, RenderPoisonBall::new);
        EntityRendererRegistry.register(EntityHandler.ICE_BALL, RenderIceBall::new);
        EntityRendererRegistry.register(EntityHandler.ICE_BREATH, RenderNothing::new);
        EntityRendererRegistry.register(EntityHandler.FROZEN_CONTROLLER, RenderNothing::new);
        EntityRendererRegistry.register(EntityHandler.SUPER_NOVA, RenderSuperNova::new);
        EntityRendererRegistry.register(EntityHandler.FALLING_BLOCK, RenderFallingBlock::new);
        EntityRendererRegistry.register(EntityHandler.BLOCK_SWAPPER, RenderNothing::new);
        EntityRendererRegistry.register(EntityHandler.BLOCK_SWAPPER_SCULPTOR, RenderNothing::new);
        EntityRendererRegistry.register(EntityHandler.CAMERA_SHAKE, RenderNothing::new);
//        EntityRendererRegistry.register(EntityHandler.TEST_ENTITY, RenderNothing::new);

        BlockEntityRendererFactories.register(BlockEntityHandler.GONG_BLOCK_ENTITY, GongRenderer::new);
        EntityRendererRegistry.register(EntityHandler.ROCK_SLING, RenderRockSling::new);

        HandledScreens.register(ContainerHandler.CONTAINER_UMVUTHANA_TRADE, GuiUmvuthanaTrade::new);
        HandledScreens.register(ContainerHandler.CONTAINER_UMVUTHI_TRADE, GuiUmvuthiTrade::new);
        HandledScreens.register(ContainerHandler.CONTAINER_SCULPTOR_TRADE, GuiSculptorTrade::new);
    }

    public static void onRegisterModels(Collection<Identifier> collection) {
        for (String item : MMModels.HAND_MODEL_ITEMS)
            collection.add(new ModelIdentifier(new Identifier(MowziesMobs.MODID, item + "_in_hand"), "inventory"));
        for (MaskType type : MaskType.values())
            collection.add(new ModelIdentifier(new Identifier(MowziesMobs.MODID, "umvuthana_mask_" + type.name + "_frame"), "inventory"));
        collection.add(new ModelIdentifier(new Identifier(MowziesMobs.MODID, "sol_visage_frame"), "inventory"));
    }
}
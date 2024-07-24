package com.bobmowzie.mowziesmobs.client.gui;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public class CustomBossBar {
    public static Map<Identifier, CustomBossBar> customBossBars = new HashMap<>();

    static {
        customBossBars.put(Registries.ENTITY_TYPE.getId(EntityHandler.UMVUTHI), new CustomBossBar(
                new Identifier(MowziesMobs.MODID, "textures/gui/boss_bar/umvuthi_bar_base.png"),
                new Identifier(MowziesMobs.MODID, "textures/gui/boss_bar/umvuthi_bar_overlay.png"),
                4, 8, 2, -12, -6, 256, 16, 21, Formatting.GOLD));
        customBossBars.put(Registries.ENTITY_TYPE.getId(EntityHandler.FROSTMAW), new CustomBossBar(
                new Identifier(MowziesMobs.MODID, "textures/gui/boss_bar/frostmaw_bar_base.png"),
                new Identifier(MowziesMobs.MODID, "textures/gui/boss_bar/frostmaw_bar_overlay.png"),
                10, 32, 2, -4, -3, 256, 32, 25, Formatting.WHITE));
    }

    private final Identifier baseTexture;
    private final Identifier overlayTexture;
    private final boolean hasOverlay;

    private final int baseHeight;
    private final int baseTextureHeight;
    private final int baseOffsetY;
    private final int overlayOffsetX;
    private final int overlayOffsetY;
    private final int overlayWidth;
    private final int overlayHeight;

    private final int verticalIncrement;

    private final Formatting textColor;

    public CustomBossBar(Identifier baseTexture, Identifier overlayTexture, int baseHeight, int baseTextureHeight, int baseOffsetY, int overlayOffsetX, int overlayOffsetY, int overlayWidth, int overlayHeight, int verticalIncrement, Formatting textColor) {
        this.baseTexture = baseTexture;
        this.overlayTexture = overlayTexture;
        this.hasOverlay = overlayTexture != null;
        this.baseHeight = baseHeight;
        this.baseTextureHeight = baseTextureHeight;
        this.baseOffsetY = baseOffsetY;
        this.overlayOffsetX = overlayOffsetX;
        this.overlayOffsetY = overlayOffsetY;
        this.overlayWidth = overlayWidth;
        this.overlayHeight = overlayHeight;
        this.verticalIncrement = verticalIncrement;
        this.textColor = textColor;
    }

    public Identifier getBaseTexture() {
        return this.baseTexture;
    }

    public Identifier getOverlayTexture() {
        return this.overlayTexture;
    }

    public boolean hasOverlay() {
        return this.hasOverlay;
    }

    public int getBaseHeight() {
        return this.baseHeight;
    }

    public int getBaseTextureHeight() {
        return this.baseTextureHeight;
    }

    public int getBaseOffsetY() {
        return this.baseOffsetY;
    }

    public int getOverlayOffsetX() {
        return this.overlayOffsetX;
    }

    public int getOverlayOffsetY() {
        return this.overlayOffsetY;
    }

    public int getOverlayWidth() {
        return this.overlayWidth;
    }

    public int getOverlayHeight() {
        return this.overlayHeight;
    }

    public int getVerticalIncrement() {
        return this.verticalIncrement;
    }

    public Formatting getTextColor() {
        return this.textColor;
    }

    public void renderBossBar(DrawContext context, int x, int y, BossBar bossBar, int width, int height) {
        int i = MinecraftClient.getInstance().getWindow().getScaledWidth();
        int j = y - 9;
        MinecraftClient.getInstance().getProfiler().push("customBossBarBase");

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, this.getBaseTexture());
        this.drawBar(context, x + 1, y + this.getBaseOffsetY(), bossBar);
        Text component = bossBar.getName().copy().formatted(this.getTextColor());
        MinecraftClient.getInstance().getProfiler().pop();

        int l = MinecraftClient.getInstance().textRenderer.getWidth(component);
        int i1 = i / 2 - l / 2;
        context.drawTextWithShadow(MinecraftClient.getInstance().textRenderer, component, i1, j, 16777215);

        if (this.hasOverlay()) {
            MinecraftClient.getInstance().getProfiler().push("customBossBarOverlay");
            RenderSystem.setShaderTexture(0, this.getOverlayTexture());
            context.drawTexture(this.getOverlayTexture(), x + 1 + this.getOverlayOffsetX(), y + this.getOverlayOffsetY() + this.getBaseOffsetY(), 0, 0, this.getOverlayWidth(), this.getOverlayHeight(), this.getOverlayWidth(), this.getOverlayHeight());
            MinecraftClient.getInstance().getProfiler().pop();
        }
    }

    private void drawBar(DrawContext guiGraphics, int x, int y, BossBar event) {
        guiGraphics.drawTexture(this.getBaseTexture(), x, y, 0, 0, 182, this.getBaseHeight(), 256, this.getBaseTextureHeight());
        int i = (int) (event.getPercent() * 183.0F);
        if (i > 0) {
            guiGraphics.drawTexture(this.getBaseTexture(), x, y, 0, this.getBaseHeight(), i, this.getBaseHeight(), 256, this.getBaseTextureHeight());
        }
    }
}
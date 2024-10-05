package com.bobmowzie.mowziesmobs.client.gui;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.entity.umvuthana.EntityUmvuthanaMinion;
import com.bobmowzie.mowziesmobs.server.entity.umvuthana.trade.Trade;
import com.bobmowzie.mowziesmobs.server.inventory.ContainerUmvuthanaTrade;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public final class GuiUmvuthanaTrade extends HandledScreen<ContainerUmvuthanaTrade> {
    private static final Identifier TEXTURE = new Identifier(MowziesMobs.MODID, "textures/gui/container/umvuthana.png");

    private final EntityUmvuthanaMinion umvuthana;

    public GuiUmvuthanaTrade(ContainerUmvuthanaTrade screenContainer, PlayerInventory inv, Text titleIn) {
        super(screenContainer, inv, titleIn);
        this.umvuthana = screenContainer.getUmvuthana();
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int state) {
//        if (barakoaya.getAnimation() == IAnimatedEntity.NO_ANIMATION) {
//            if (isHovering(13, 23, 8, 14, mouseX, mouseY)) {
//                barakoaya.setAnimation(EntityBarakoaVillager.ATTACK_ANIMATION);
//                barakoaya.setAnimationTick(3);
//            }
//        } TODO
        return super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    protected void drawBackground(DrawContext guiGraphics, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.colorMask(true, true, true, true);
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        guiGraphics.drawTexture(TEXTURE, x, y, 0, 0, this.backgroundWidth, this.backgroundHeight);
        this.umvuthana.renderingInGUI = true;
        InventoryScreen.drawEntity(guiGraphics, x + 33, y + 64, 20, x + 33 - x, y + 21 - y, this.umvuthana);
        this.umvuthana.renderingInGUI = false;
    }

    @Override
    protected void drawForeground(DrawContext guiGraphics, int x, int y) {
        String title = I18n.translate("entity.mowziesmobs.umvuthana.trade");
        guiGraphics.drawText(this.textRenderer, title, (int) (this.backgroundWidth / 2f - this.textRenderer.getWidth(title) / 2f + 26), 6, 4210752, false);
        guiGraphics.drawText(this.textRenderer, I18n.translate("container.inventory"), 8, this.backgroundHeight - 96 + 2, 4210752, false);
    }

    @Override
    public void render(DrawContext guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        this.drawMouseoverTooltip(guiGraphics, mouseX, mouseY);
        if (this.umvuthana.isOfferingTrade()) {
            Trade trade = this.umvuthana.getOfferingTrade();
            ItemStack input = trade.getInput();
            ItemStack output = trade.getOutput();
            guiGraphics.getMatrices().push();

            guiGraphics.getMatrices().translate(0, 0, 100);
            guiGraphics.drawItem(input, this.x + 80, this.y + 24);
            guiGraphics.drawItemInSlot(this.textRenderer, input, this.x + 80, this.y + 24);
            guiGraphics.drawItem(output, this.x + 134, this.y + 24);
            guiGraphics.drawItemInSlot(this.textRenderer, output, this.x + 134, this.y + 24);

            if (this.isPointWithinBounds(80, 24, 16, 16, mouseX, mouseY)) {
                guiGraphics.drawItemTooltip(this.textRenderer, input, mouseX, mouseY);
            } else if (this.isPointWithinBounds(134, 24, 16, 16, mouseX, mouseY)) {
                guiGraphics.drawItemTooltip(this.textRenderer, output, mouseX, mouseY);
            }
            guiGraphics.getMatrices().pop();
        }
    }
}

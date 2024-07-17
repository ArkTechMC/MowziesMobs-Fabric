package com.bobmowzie.mowziesmobs.client.gui;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.entity.umvuthana.EntityUmvuthi;
import com.bobmowzie.mowziesmobs.server.inventory.ContainerUmvuthiTrade;
import com.bobmowzie.mowziesmobs.server.inventory.InventoryUmvuthi;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import com.bobmowzie.mowziesmobs.server.message.MessageUmvuthiTrade;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public final class GuiUmvuthiTrade extends HandledScreen<ContainerUmvuthiTrade> implements InventoryUmvuthi.ChangeListener {
    private static final Identifier TEXTURE_TRADE = new Identifier(MowziesMobs.MODID, "textures/gui/container/umvuthi_trade.png");
    private static final Identifier TEXTURE_REPLENISH = new Identifier(MowziesMobs.MODID, "textures/gui/container/umvuthi_replenish.png");

    private final EntityUmvuthi umvuthi;
    private final PlayerEntity player;

    private final InventoryUmvuthi inventory;

    private final ItemStack output = new ItemStack(ItemHandler.GRANT_SUNS_BLESSING);

    private ButtonWidget grantButton;

    private boolean hasTraded;

    public GuiUmvuthiTrade(ContainerUmvuthiTrade screenContainer, PlayerInventory inv, Text titleIn) {
        super(screenContainer, inv, titleIn);
        this.umvuthi = screenContainer.getUmvuthi();
        this.player = inv.player;
        this.inventory = screenContainer.getInventoryUmvuthi();
        this.hasTraded = this.umvuthi.hasTradedWith(inv.player);
        this.inventory.addListener(this);
    }

    @Override
    protected void init() {
        super.init();
        String text = I18n.translate(this.hasTraded ? "entity.mowziesmobs.umvuthi.replenish.button.text" : "entity.mowziesmobs.umvuthi.trade.button.text");
        this.grantButton = this.addDrawableChild(ButtonWidget.builder(Text.translatable(text), this::actionPerformed).width(204).position(this.x + 115, this.y + 52).size(56, 20).build());
        this.grantButton.active = this.hasTraded;
        this.updateButton();
    }

    private void actionPerformed(ButtonWidget button) {
        if (button == this.grantButton) {
            this.hasTraded = true;
            this.updateButton();
            MowziesMobs.NETWORK.sendToServer(new MessageUmvuthiTrade(this.umvuthi));
            if (!MinecraftClient.getInstance().isInSingleplayer()) {
                boolean satisfied = this.umvuthi.hasTradedWith(this.player);
                if (!satisfied) {
                    if (this.umvuthi.fulfillDesire(this.handler.getSlot(0))) {
                        this.umvuthi.rememberTrade(this.player);
                        this.handler.sendContentUpdates();
                    }
                }
            }
        }
    }

    @Override
    protected void drawBackground(DrawContext guiGraphics, float partialTicks, int x, int y) {
        RenderSystem.colorMask(true, true, true, true);
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, this.hasTraded ? TEXTURE_REPLENISH : TEXTURE_TRADE);
        //minecraft.getTextureManager().bindForSetup(hasTraded ? TEXTURE_REPLENISH : TEXTURE_TRADE);
        guiGraphics.drawTexture(this.hasTraded ? TEXTURE_REPLENISH : TEXTURE_TRADE, x, y, 0, 0, this.backgroundWidth, this.backgroundHeight);
        this.umvuthi.renderingInGUI = true;
        InventoryScreen.drawEntity(guiGraphics, x + 33, y + 57, 10, x + 33 - x, y + 21 - y, this.umvuthi);
        this.umvuthi.renderingInGUI = false;
    }

    @Override
    protected void drawForeground(DrawContext guiGraphics, int x, int y) {
        String title = I18n.translate("entity.mowziesmobs.umvuthi.trade");
        guiGraphics.drawText(this.textRenderer, title, (int) (this.backgroundWidth / 2f - this.textRenderer.getWidth(title) / 2f) + 30, 6, 0x404040, false);
        guiGraphics.drawText(this.textRenderer, I18n.translate("container.inventory"), 8, this.backgroundHeight - 96 + 2, 0x404040, false);
    }

    @Override
    public void render(DrawContext guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        this.drawMouseoverTooltip(guiGraphics, mouseX, mouseY);
        ItemStack inSlot = this.inventory.getStack(0);
        guiGraphics.getMatrices().push();

        guiGraphics.getMatrices().translate(0, 0, 100);
        if (this.hasTraded) {
            guiGraphics.drawItem(this.output, this.x + 106, this.y + 24);
            guiGraphics.drawItemInSlot(this.textRenderer, this.output, this.x + 106, this.y + 24);
            if (this.isPointWithinBounds(106, 24, 16, 16, mouseX, mouseY)) {
                guiGraphics.drawItemTooltip(this.textRenderer, this.output, mouseX, mouseY);
            }
        } else {
            guiGraphics.drawItem(this.umvuthi.getDesires(), this.x + 68, this.y + 24);
            guiGraphics.drawItemInSlot(this.textRenderer, this.umvuthi.getDesires(), this.x + 68, this.y + 24);
            guiGraphics.drawItem(this.output, this.x + 134, this.y + 24);
            guiGraphics.drawItemInSlot(this.textRenderer, this.output, this.x + 134, this.y + 24);
            if (this.isPointWithinBounds(68, 24, 16, 16, mouseX, mouseY)) {
                guiGraphics.drawItemTooltip(this.textRenderer, this.umvuthi.getDesires(), mouseX, mouseY);
            } else if (this.isPointWithinBounds(134, 24, 16, 16, mouseX, mouseY)) {
                guiGraphics.drawItemTooltip(this.textRenderer, this.output, mouseX, mouseY);
            }
        }

        if (this.grantButton.isMouseOver(mouseX, mouseY)) {
            guiGraphics.drawHoverEvent(this.textRenderer, this.getHoverText(), mouseX, mouseY);
        }
        guiGraphics.getMatrices().pop();
    }

    @Override
    public void onChange(Inventory inv) {
        this.grantButton.active = this.hasTraded || this.umvuthi.doesItemSatisfyDesire(inv.getStack(0));
    }

    private void updateButton() {
        if (this.hasTraded) {
            this.grantButton.setMessage(Text.translatable(I18n.translate("entity.mowziesmobs.umvuthi.replenish.button.text")));
            this.grantButton.setWidth(108);
            this.grantButton.setPosition(this.x + 63, this.grantButton.getY());
        } else {
            this.grantButton.setMessage(Text.translatable(I18n.translate("entity.mowziesmobs.umvuthi.trade.button.text")));
        }
    }

    private Style getHoverText() {
        MutableText text = Text.translatable(I18n.translate(this.hasTraded ? "entity.mowziesmobs.umvuthi.replenish.button.hover" : "entity.mowziesmobs.umvuthi.trade.button.hover"));
        return text.getStyle().withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, text));
    }
}

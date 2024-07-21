package com.bobmowzie.mowziesmobs.client.gui;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.entity.sculptor.EntitySculptor;
import com.bobmowzie.mowziesmobs.server.inventory.ContainerSculptorTrade;
import com.bobmowzie.mowziesmobs.server.inventory.InventorySculptor;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import com.bobmowzie.mowziesmobs.server.message.MessageSculptorTrade;
import com.bobmowzie.mowziesmobs.server.message.StaticVariables;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.PressableTextWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.joml.Quaternionf;

public final class GuiSculptorTrade extends HandledScreen<ContainerSculptorTrade> implements InventorySculptor.ChangeListener {
    private static final Identifier TEXTURE_TRADE = new Identifier(MowziesMobs.MODID, "textures/gui/container/umvuthi_trade.png");

    private final EntitySculptor sculptor;
    private final PlayerEntity player;

    private final InventorySculptor inventory;

    private final ItemStack output = new ItemStack(ItemHandler.EARTHREND_GAUNTLET);

    private ButtonWidget beginButton;

    public GuiSculptorTrade(ContainerSculptorTrade screenContainer, PlayerInventory inv, Text titleIn) {
        super(screenContainer, inv, titleIn);
        this.sculptor = screenContainer.getSculptor();
        this.player = inv.player;
        this.inventory = screenContainer.getInventorySculptor();
        this.inventory.addListener(this);
    }

    @Override
    protected void init() {
        super.init();
        String text = I18n.translate("entity.mowziesmobs.sculptor.trade.button.text");
        this.beginButton = this.addDrawableChild(new PressableTextWidget(this.x + 115, this.y + 52, 56, 20, Text.translatable(text), this::actionPerformed, this.textRenderer));
        this.updateButton();
    }

    private void actionPerformed(ButtonWidget button) {
        if (button == this.beginButton) {
            PacketByteBuf buf = PacketByteBufs.create();
            MessageSculptorTrade.serialize(new MessageSculptorTrade(this.sculptor), PacketByteBufs.create());
            ClientPlayNetworking.send(StaticVariables.SCULPTOR_TRADE, buf);
        }
    }

    @Override
    protected void drawBackground(DrawContext guiGraphics, float partialTicks, int x, int y) {
        RenderSystem.colorMask(true, true, true, true);
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        guiGraphics.drawTexture(TEXTURE_TRADE, x, y, 0, 0, this.backgroundWidth, this.backgroundHeight);
        InventoryScreen.drawEntity(guiGraphics, x + 33, y + 56, 14, new Quaternionf(), null, this.sculptor);
    }

    @Override
    protected void drawForeground(DrawContext guiGraphics, int x, int y) {
        super.drawForeground(guiGraphics, x, y);
        guiGraphics.drawTextWithShadow(this.textRenderer, this.title, (int) (this.backgroundWidth / 2f - this.textRenderer.getWidth(this.title) / 2f) + 30, 6, 0x404040);
        guiGraphics.drawTextWithShadow(this.textRenderer, I18n.translate("container.inventory"), 8, this.backgroundHeight - 96 + 2, 0x404040);
    }

    @Override
    public void render(DrawContext guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        this.drawMouseoverTooltip(guiGraphics, mouseX, mouseY);
        ItemStack inSlot = this.inventory.getStack(0);
        guiGraphics.getMatrices().push();

        guiGraphics.getMatrices().translate(0.0F, 0.0F, 100.0F);

        guiGraphics.drawItem(this.sculptor.getDesires(), this.x + 68, this.y + 24);
        guiGraphics.drawItemInSlot(this.textRenderer, this.sculptor.getDesires(), this.x + 68, this.y + 24);
        guiGraphics.drawItem(this.output, this.x + 134, this.y + 24);
        guiGraphics.drawItemInSlot(this.textRenderer, this.output, this.x + 134, this.y + 24);
        if (this.isPointWithinBounds(68, 24, 16, 16, mouseX, mouseY)) {
            guiGraphics.drawItemTooltip(this.textRenderer, this.sculptor.getDesires(), mouseX, mouseY);
        } else if (this.isPointWithinBounds(134, 24, 16, 16, mouseX, mouseY)) {
            guiGraphics.drawItemTooltip(this.textRenderer, this.output, mouseX, mouseY);
        }

        if (this.beginButton.isMouseOver(mouseX, mouseY)) {
            guiGraphics.drawHoverEvent(this.textRenderer, this.getHoverText(), mouseX, mouseY);
        }
        guiGraphics.getMatrices().pop();
    }

    @Override
    public void onChange(Inventory inv) {
        this.beginButton.active = this.sculptor.doesItemSatisfyDesire(inv.getStack(0));
    }

    private void updateButton() {
        this.beginButton.setMessage(Text.translatable(I18n.translate("entity.mowziesmobs.sculptor.trade.button.text")));
    }

    private Style getHoverText() {
        MutableText text = Text.translatable(I18n.translate("entity.mowziesmobs.sculptor.trade.button.hover"));
        return text.getStyle().withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, text));
    }
}

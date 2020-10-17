package net.ineter.chemsolutions.blocks;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.ineter.chemsolutions.ChemistrySolutions;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

@SuppressWarnings("deprecation")
@OnlyIn(Dist.CLIENT)
public class GrinderContainerScreen extends ContainerScreen<OreGrinderContainer> {
    private final ResourceLocation guiTexture = new ResourceLocation(ChemistrySolutions.MODID, "textures/gui/container/machine_generic.png");

    public GrinderContainerScreen(OreGrinderContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
    }

    @Override
    public void render(@Nonnull MatrixStack p_230430_1_, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
        this.renderBackground(p_230430_1_);
        super.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
        this.renderHoveredTooltip(p_230430_1_, p_230430_2_, p_230430_3_);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(@Nonnull MatrixStack matrixStack, float partialTicks, int x, int y) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        if (this.minecraft != null) {
            this.minecraft.getTextureManager().bindTexture(this.guiTexture);
        }
        int lvt_5_1_ = this.guiLeft;
        int lvt_6_1_ = this.guiTop;
        this.blit(matrixStack, lvt_5_1_, lvt_6_1_, 0, 0, this.xSize, this.ySize);
        int lvt_7_2_ = this.getContainer().getCookProgressionScaled(24);
        this.blit(matrixStack, lvt_5_1_ + 79, lvt_6_1_ + 34, 176, 14, lvt_7_2_, 16);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(@Nonnull MatrixStack p_230451_1_, int p_230451_2_, int p_230451_3_) {
        super.drawGuiContainerForegroundLayer(p_230451_1_, p_230451_2_, p_230451_3_);
        this.font.drawString(p_230451_1_, I18n.format("container.ore_grinder.energy", this.getContainer().getEnergy()), 8, 15, 0xFF404040);
    }
}

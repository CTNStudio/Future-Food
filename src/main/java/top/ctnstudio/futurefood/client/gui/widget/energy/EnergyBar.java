package top.ctnstudio.futurefood.client.gui.widget.energy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageWidget;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import top.ctnstudio.futurefood.core.FutureFood;

import static top.ctnstudio.futurefood.util.TextUtil.getDigitalText;

public class EnergyBar extends ImageWidget.Sprite {
  public static final String TOOLTIP = FutureFood.ID + ".gui.energy.tooltip";
  public static final ResourceLocation TEXTURE = FutureFood.modRL("energy_bar");
  private int energy;
  private int maxEnergy;

  public EnergyBar(int x, int y, int energy, int maxEnergy) {
    this(x, y, 12, 39, TEXTURE, energy, maxEnergy);
  }

  public EnergyBar(int x, int y, int width, int height, ResourceLocation sprite, int energy, int maxEnergy) {
    super(x, y, width, height, sprite);
    this.energy = energy;
    this.maxEnergy = maxEnergy;
  }

  public void setMaxEnergy(int maxEnergy) {
    this.maxEnergy = maxEnergy;
  }

  public void setEnergy(int energy, int maxEnergy) {
    this.energy = energy;
    this.maxEnergy = maxEnergy;
  }

  public int getEnergy() {
    return this.energy;
  }

  public void setEnergy(int energy) {
    this.energy = energy;
  }

  @Override
  public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
    if (isHovered()) {
      Minecraft minecraft = Minecraft.getInstance();
      String energyText = getDigitalText(energy);
      String maxEnergyText = getDigitalText(maxEnergy);
      MutableComponent translatable = Component.translatable(TOOLTIP, energyText + "FE", maxEnergyText + "FE");
      guiGraphics.renderTooltip(minecraft.font, translatable, mouseX, mouseY);
    }
    if (this.maxEnergy > 0) {
      int energy = Math.max(0, this.energy);
      int height = (int) ((Math.min(energy, this.maxEnergy) / (float) this.maxEnergy) * this.getHeight());
      int yPosition = this.getHeight() - height;
      guiGraphics.blitSprite(this.sprite,
        this.getWidth(), this.getHeight(),
        0, yPosition,
        this.getX(), this.getY() + yPosition,
        this.getWidth(), height);
    }
  }

  public ResourceLocation getTexture() {
    return this.sprite;
  }
}

package top.ctnstudio.futurefood.client.gui.widget.energy;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.checkerframework.checker.nullness.qual.Nullable;
import top.ctnstudio.futurefood.core.FutureFood;

public class EnergyBar extends ImageWidget.Sprite {
  public static final String TOOLTIP = "futurefood.gui.energy.tooltip";
  public static final ResourceLocation TEXTURE = FutureFood.modRL("energy_bar");
  private int energy;
  private int maxEnergy;

  public EnergyBar(int x, int y, int width, int height, ResourceLocation sprite, int maxEnergy) {
    super(x, y, width, height, sprite);
    this.maxEnergy = maxEnergy;
  }

  public EnergyBar(int x, int y, int maxEnergy) {
    this(x, y, 12, 39, TEXTURE, maxEnergy);
  }

  public void setMaxEnergy(int maxEnergy) {
    this.maxEnergy = maxEnergy;
  }

  public void setEnergy(int energy) {
    this.energy = Math.min(energy, maxEnergy);
  }

  public int getEnergy() {
    return this.energy;
  }

  @Override
  @Nullable
  public Tooltip getTooltip() {
    return Tooltip.create(Component.translatable(TOOLTIP, this.energy, this.maxEnergy));
  }

  @Override
  public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
    int height = (energy / maxEnergy) * this.getHeight();
    int yPosition = this.getHeight() - height;
    guiGraphics.blitSprite(this.sprite,
      this.getWidth(), height,
      0, 0,
      this.getX(), this.getY() + yPosition,
      this.getWidth(), height);
  }

  public ResourceLocation getTexture() {
    return this.sprite;
  }
}

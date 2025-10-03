package top.ctnstudio.futurefood.client.gui.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageWidget;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import top.ctnstudio.futurefood.common.menu.ParticleColliderMenu;
import top.ctnstudio.futurefood.core.FutureFood;

import static top.ctnstudio.futurefood.util.TextUtil.formatGameTime;

public class ParticleColliderScreen extends EnergyScreen<ParticleColliderMenu> {
  public static final ResourceLocation BG = FutureFood.modRL("textures/gui/container/particle_collider.png");
  protected ProgressBar progressBar;

  public ParticleColliderScreen(ParticleColliderMenu menu, Inventory playerInventory, Component title) {
    super(menu, playerInventory, title, BG);
  }

  @Override
  protected void init() {
    super.init();
    this.progressBar = new ProgressBar(leftPos + 65, topPos + 67, menu.getRemainingTick(), menu.getMaxWorkTick());
    addRenderableWidget(progressBar);
  }

  @Override
  public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
    if (progressBar != null) {
      progressBar.setTick(menu.getEnergy(), menu.getMaxEnergy());
    }
    super.render(guiGraphics, mouseX, mouseY, partialTick);
  }

  @Override
  protected void containerTick() {
    if (progressBar != null) {
      progressBar.setTick(menu.getEnergy(), menu.getMaxEnergy());
    }
    super.containerTick();
  }

  public static class ProgressBar extends ImageWidget.Sprite {
    public static final String TOOLTIP = FutureFood.ID + ".gui.progress.tooltip";
    public static final ResourceLocation TEXTURE = FutureFood.modRL("container/particle_collider/progresso.png");
    private int remainingTick;
    private int maxWorkTick;

    public ProgressBar(int x, int y, int remainingTick, int maxWorkTick) {
      super(x, y, 46, 12, TEXTURE);
      this.remainingTick = remainingTick;
      this.maxWorkTick = maxWorkTick;
    }

    public void setMaxWorkTick(int maxWorkTick) {
      this.maxWorkTick = maxWorkTick;
    }

    public void setTick(int energy, int maxTick) {
      this.remainingTick = energy;
      this.maxWorkTick = maxTick;
    }

    public int getRemainingTick() {
      return this.remainingTick;
    }

    public void setRemainingTick(int remainingTick) {
      this.remainingTick = remainingTick;
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
      if (maxWorkTick > 0 && remainingTick > 0 & isHovered()) {
        Minecraft minecraft = Minecraft.getInstance();
        MutableComponent translatable = Component.translatable(TOOLTIP, formatGameTime(remainingTick));
        guiGraphics.renderTooltip(minecraft.font, translatable, mouseX, mouseY);
      }
      if (this.maxWorkTick > 0) {
        int spriteWidth = this.getWidth();
        int spriteHeight = this.getHeight();

        int remainingTick = Math.max(0, this.remainingTick);
        int uWidth = (int) ((Math.min(remainingTick, this.maxWorkTick) / (float) this.maxWorkTick) * spriteWidth);
        int xPosition = spriteWidth - uWidth;
        int x = this.getX() + xPosition;
        int y = this.getY();

        guiGraphics.blitSprite(this.sprite, spriteWidth, spriteHeight, xPosition, 0, x, y, uWidth, spriteHeight);
      }
    }

    public ResourceLocation getTexture() {
      return this.sprite;
    }
  }
}

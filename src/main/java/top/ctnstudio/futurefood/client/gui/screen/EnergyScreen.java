package top.ctnstudio.futurefood.client.gui.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import top.ctnstudio.futurefood.client.gui.widget.energy.EnergyBar;
import top.ctnstudio.futurefood.common.menu.BasicEnergyMenu;
import top.ctnstudio.futurefood.core.FutureFood;

public class EnergyScreen<T extends BasicEnergyMenu> extends AbstractContainerScreen<T> {
  public static final ResourceLocation BG = FutureFood.modRL("textures/gui/container/energy.png");
  private final ResourceLocation textureBg;
  protected EnergyBar energyBar;

  public EnergyScreen(T menu, Inventory playerInventory, Component title) {
    this(menu, playerInventory, title, BG);
  }

  public EnergyScreen(T menu, Inventory playerInventory, Component title, ResourceLocation textureBg) {
    super(menu, playerInventory, title);
    this.textureBg = textureBg;
    this.titleLabelX += 8;
    this.titleLabelY -= 2;
  }

  @Override
  protected void init() {
    super.init();
    this.energyBar = new EnergyBar(leftPos + 2, topPos + 15, menu.getEnergy(), menu.getMaxEnergy());
    addRenderableWidget(energyBar);
  }

  @Override
  public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
    containerTick();
    super.render(guiGraphics, mouseX, mouseY, partialTick);
    renderTooltip(guiGraphics, mouseX, mouseY);
  }

  @Override
  protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
    PoseStack pose = guiGraphics.pose();
    pose.pushPose();
    guiGraphics.blit(textureBg, leftPos, topPos, 0, 0, this.imageWidth, this.imageHeight);
    pose.popPose();
  }

  @Override
  protected void containerTick() {
    super.containerTick();
    if (energyBar != null) {
      energyBar.setValue(menu.getEnergy(), menu.getMaxEnergy());
    }
  }

  @Override
  protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
    guiGraphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 4210752, false);
  }
}

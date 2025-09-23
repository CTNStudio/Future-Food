package top.ctnstudio.futurefood.client.gui.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import top.ctnstudio.futurefood.common.menu.EnergyMenu;
import top.ctnstudio.futurefood.common.menu.EnergyMenu.EnergyData;
import top.ctnstudio.futurefood.client.gui.widget.energy.EnergyBar;
import top.ctnstudio.futurefood.core.FutureFood;

public class EnergyScreen extends AbstractContainerScreen<EnergyMenu> {
  public static final ResourceLocation DEFAULT_BG = FutureFood.modRL("textures/gui/container/energy.png");
  public static final ResourceLocation ENERGY_ICON = FutureFood.modRL("energy_icon");
  private final ResourceLocation textureBg;
  protected EnergyBar listener;

  public EnergyScreen(EnergyMenu menu, Inventory playerInventory, Component title, ResourceLocation textureBg) {
    super(menu, playerInventory, title);
    this.textureBg = textureBg;
  }

  public EnergyScreen(EnergyMenu menu, Inventory playerInventory, Component title) {
    this(menu, playerInventory, title, DEFAULT_BG);
  }

  @Override
  protected void init() {
    super.init();
    EnergyData energyData = getMenu().getEnergyData();
    EnergyBar listener = new EnergyBar(1, 14, energyData.getEnergy(), energyData.getMaxEnergy());
    addWidget(listener);
  }

  @Override
  protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
    PoseStack pose = guiGraphics.pose();
    pose.pushPose();
    guiGraphics.blit(textureBg, leftPos, topPos, 0, 0, this.imageWidth, this.imageHeight);
    pose.popPose();
  }

  @Override
  public void render(GuiGraphics gg, int mouseX, int mouseY, float partialTick) {
    super.renderBackground(gg, mouseX, mouseY, partialTick);
    super.render(gg, mouseX, mouseY, partialTick);
    this.renderTooltip(gg, mouseX, mouseY);
  }

  @Override
  protected void containerTick() {
    super.containerTick();
    EnergyData energyData = getMenu().getEnergyData();
    if (listener != null) {
      listener.setEnergy(energyData.getEnergy(), energyData.getMaxEnergy());
    }
  }
}

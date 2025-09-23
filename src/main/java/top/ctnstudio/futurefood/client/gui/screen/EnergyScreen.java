package top.ctnstudio.futurefood.client.gui.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import top.ctnstudio.futurefood.client.gui.menu.EnergyMenu;
import top.ctnstudio.futurefood.client.gui.widget.energy.EnergyBar;
import top.ctnstudio.futurefood.core.FutureFood;

//todo
public class EnergyScreen extends AbstractContainerScreen<EnergyMenu> {
  public static final ResourceLocation DEFAULT_BG = FutureFood.modRL("textures/gui/container/energy.png");
  public static final ResourceLocation ENERGY_ICON = FutureFood.modRL("energy_icon");
  private final ResourceLocation textureBg;
  protected EnergyBar listener;
  private int energy;
  private int maxEnergy;
  public EnergyScreen(EnergyMenu menu, Inventory playerInventory, Component title, ResourceLocation textureBg) {
    super(menu, playerInventory, title);
    this.textureBg = textureBg;
    energy = menu.getEnergy();
    maxEnergy = menu.getMaxEnergy();
  }

  public EnergyScreen(EnergyMenu menu, Inventory playerInventory, Component title) {
    this(menu, playerInventory, title, DEFAULT_BG);
  }

  @Override
  protected void init() {
    super.init();
    energy = menu.getEnergy();
    maxEnergy = menu.getMaxEnergy();
    this.listener = new EnergyBar(leftPos + 1, topPos + 14, energy, maxEnergy);
    addRenderableWidget(listener);
  }

  @Override
  public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
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
    energy = menu.getEnergy();
    maxEnergy = menu.getMaxEnergy();
    if (listener != null) {
      listener.setEnergy(energy, maxEnergy);
    }
  }
}

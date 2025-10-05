package top.ctnstudio.futurefood.client.gui.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;
import top.ctnstudio.futurefood.client.gui.widget.ImageProgressBar;
import top.ctnstudio.futurefood.common.menu.GluttonyMenu;
import top.ctnstudio.futurefood.core.FutureFood;
import top.ctnstudio.futurefood.util.TextUtil;

public class GluttonyScreen extends EnergyScreen<GluttonyMenu> {
  public static final ResourceLocation BG = FutureFood.modRL("textures/gui/container/gluttony.png");
  public static final ResourceLocation PROGRESS_BAR_TEXTURE = FutureFood.modRL("container/gluttony/progresso");
  public static final String TOOLTIP = FutureFood.ID + ".gui.gluttony.progress.tooltip";
  protected ImageProgressBar progressBar;

  public GluttonyScreen(GluttonyMenu menu, Inventory playerInventory, Component title) {
    super(menu, playerInventory, title, BG);
  }

  @Override
  protected void init() {
    super.init();
    this.progressBar = new ImageProgressBar.Horizontal(
      leftPos + 54, topPos + 39,
      67, 18,
      menu.getRemainingTick(), menu.getMaxWorkTick(),
      PROGRESS_BAR_TEXTURE, TOOLTIP, false) {
      @Override
      public @NotNull Component getTooltipComponent() {
        return Component.translatable(getTooltipKey(), TextUtil.formatGameTime(getRenderValue()));
      }
    };
    addRenderableWidget(progressBar);
  }

  @Override
  public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
    super.render(guiGraphics, mouseX, mouseY, partialTick);
  }

  @Override
  protected void containerTick() {
    if (progressBar != null) {
      progressBar.setValue(menu.getRemainingTick(), menu.getMaxWorkTick());
    }
    super.containerTick();
  }
}

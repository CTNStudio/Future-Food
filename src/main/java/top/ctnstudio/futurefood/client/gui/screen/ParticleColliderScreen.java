package top.ctnstudio.futurefood.client.gui.screen;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;
import top.ctnstudio.futurefood.client.gui.widget.ImageProgressBar;
import top.ctnstudio.futurefood.common.menu.ParticleColliderMenu;
import top.ctnstudio.futurefood.core.FutureFood;
import top.ctnstudio.futurefood.util.TextUtil;

public class ParticleColliderScreen extends BasicEnergyScreen<ParticleColliderMenu> {
  public static final ResourceLocation BG = FutureFood.modRL("textures/gui/container/particle_collider.png");
  public static final ResourceLocation PROGRESS_BAR_TEXTURE = FutureFood.modRL("container/particle_collider/progresso");
  public static final String TOOLTIP = FutureFood.ID + ".gui.particle_collider.progress.tooltip";

  protected ImageProgressBar progressBar;

  public ParticleColliderScreen(ParticleColliderMenu menu, Inventory playerInventory, Component title) {
    super(menu, playerInventory, title, BG);
  }

  @Override
  protected void init() {
    super.init();
    this.progressBar = new ImageProgressBar.Horizontal(
      leftPos + 65, topPos + 67,
      46, 12,
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
  protected void containerTick() {
    if (progressBar != null) {
      progressBar.setValue(menu.getRemainingTick(), menu.getMaxWorkTick());
    }
    super.containerTick();
  }
}

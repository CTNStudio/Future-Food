package top.ctnstudio.futurefood.client.gui.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class TestLayerDraw extends LayeredDraw implements LayeredDraw.Layer {
  private final Minecraft minecraft;
  private final Font font;
  private final int fontHeight;
  public final List<Component> componentList;

  public TestLayerDraw(GuiGraphics guiGraphics, DeltaTracker deltaTracker, Minecraft minecraft) {
    componentList = new ArrayList<>();
    for (int i = 0; i < 1145; i++) {
      componentList.add(Component.empty());
    }
    this.minecraft = minecraft;
    font = minecraft.font;
    fontHeight = font.lineHeight;

    render(guiGraphics, deltaTracker);
  }

  @Override
  public void render(@NotNull GuiGraphics guiGraphics, @NotNull DeltaTracker deltaTracker) {
    PoseStack pose = guiGraphics.pose();
    pose.pushPose();

    if (minecraft.options.hideGui) {
      return;
    }
    Player player = this.minecraft.getCameraEntity() instanceof Player p ? p : null;
    if (player == null || player.isCreative() || player.isSpectator() || componentList.isEmpty()) {
      return;
    }
    int y = 0;
    for (Component component : componentList) {
      guiGraphics.drawString(font, component, 0, y + fontHeight, 0xFFFFFF);
    }

    pose.popPose();
  }
}

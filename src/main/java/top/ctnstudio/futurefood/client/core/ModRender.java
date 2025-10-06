package top.ctnstudio.futurefood.client.core;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import top.ctnstudio.futurefood.client.renderer.HighlightLinksRender;
import top.ctnstudio.futurefood.client.util.ColorUtil;
import top.ctnstudio.futurefood.common.item.data_component.ItemBlockPosData;
import top.ctnstudio.futurefood.core.FutureFood;
import top.ctnstudio.futurefood.core.init.ModDataComponent;

import java.util.List;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(modid = FutureFood.ID, value = Dist.CLIENT)
public final class ModRender {
  public static final String ITEM_TOOLTIP_POSITION = FutureFood.ID + ".item.tooltip.position";
  public static final String ITEM_TOOLTIP_POSITION_EMPTY = FutureFood.ID + ".item.tooltip.position.empty";

  @SubscribeEvent
  public static void levelRender(final RenderLevelStageEvent event) {
    final RenderLevelStageEvent.Stage stage = event.getStage();
    final Minecraft minecraft = Minecraft.getInstance();
    final ClientLevel level = minecraft.level;
    final Frustum frustum = event.getFrustum();
    final PoseStack pose = event.getPoseStack();
    final Camera camera = event.getCamera();

    if (stage == RenderLevelStageEvent.Stage.AFTER_TRIPWIRE_BLOCKS) {
      HighlightLinksRender.get().levelRender(minecraft, level, frustum, pose, camera);
    }
  }

  @SubscribeEvent
  public static void itemTooltipRender(final ItemTooltipEvent event) {
    final var itemStack = event.getItemStack();
    final var tooltip = event.getToolTip();
    final int tooltipSize = tooltip.size();

    itemTooltipPosition(itemStack, tooltip, tooltipSize);
  }

  private static void itemTooltipPosition(ItemStack itemStack, List<Component> tooltip, int tooltipSize) {
    if (!itemStack.has(ModDataComponent.BLOCK_POS)) {
      return;
    }
    ItemBlockPosData integers = itemStack.get(ModDataComponent.BLOCK_POS);
    final MutableComponent text;
    if (integers != null && !integers.isEmpty()) {
      text = Component.translatable(ITEM_TOOLTIP_POSITION,
        Component.literal("X " + integers.getX()).withColor(ColorUtil.rgbColor("#FF0000")),
        Component.literal("Y " + integers.getY()).withColor(ColorUtil.rgbColor("#4CAF50")),
        Component.literal("Z " + integers.getZ()).withColor(ColorUtil.rgbColor("#2196F3")));
    } else {
      text = Component.translatable(ITEM_TOOLTIP_POSITION_EMPTY);
    }
    tooltip.add(tooltipSize >= 1 ? 1 : 0, text.withColor(ColorUtil.rgbColor("#808080")));
  }
}

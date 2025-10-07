package top.ctnstudio.futurefood.client;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import top.ctnstudio.futurefood.client.util.ColorUtil;
import top.ctnstudio.futurefood.common.data_component.ItemBlockPosData;
import top.ctnstudio.futurefood.core.FutureFood;
import top.ctnstudio.futurefood.core.init.ModDataComponent;
import top.ctnstudio.futurefood.util.TextUtil;

import java.util.List;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(modid = FutureFood.ID, value = Dist.CLIENT)
public final class ModItemTooltipRender {
  public static final String ITEM_TOOLTIP_POSITION = FutureFood.ID + ".item.tooltip.position";
  public static final String ITEM_TOOLTIP_POSITION_EMPTY = FutureFood.ID + ".item.tooltip.position.empty";
  public static final String ITEM_TOOLTIP_ENERGY_STORAGE = FutureFood.ID + ".item.tooltip.energyStorage";

  @SubscribeEvent
  public static void itemTooltipRender(final ItemTooltipEvent event) {
    final var itemStack = event.getItemStack();
    final var tooltip = event.getToolTip();
    final int tooltipSize = tooltip.size();

    position(itemStack, tooltip, tooltipSize);
    energyStorage(itemStack, tooltip, tooltipSize);
  }

  private static void energyStorage(ItemStack itemStack, List<Component> tooltip, int tooltipSize) {
    var energyStorageData = itemStack.get(ModDataComponent.ENERGY_STORAGE);
    if (energyStorageData == null) {
      return;
    }
    MutableComponent text = Component.translatable(ITEM_TOOLTIP_ENERGY_STORAGE,
      Component.literal(TextUtil.getDigitalText(energyStorageData.energyStored())).withColor(ColorUtil.rgbColor("#ffffff")),
      Component.literal(TextUtil.getDigitalText(energyStorageData.maxEnergyStored())).withColor(ColorUtil.rgbColor("#ffffff")));
    text.withColor(ColorUtil.rgbColor("#808080"));
    tooltip.add(tooltipSize >= 1 ? 1 : 0, text);
  }

  private static void position(ItemStack itemStack, List<Component> tooltip, int tooltipSize) {
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
    text.withColor(ColorUtil.rgbColor("#808080"));
    tooltip.add(tooltipSize >= 1 ? 1 : 0, text);
  }
}

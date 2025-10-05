package top.ctnstudio.futurefood.client.event;

import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import top.ctnstudio.futurefood.client.util.TextureMapBuilder;
import top.ctnstudio.futurefood.core.FutureFood;
import top.ctnstudio.futurefood.core.init.ModItem;

/**
 * 物品渲染附加
 */
@EventBusSubscriber
public class ItemPropertyEvents {
  // 物品堆叠 0~0.63 对应1~64
  public static final ResourceLocation STACKING = FutureFood.modRL("stacking");

  public static final ClampedItemPropertyFunction STACKING_PRICE_64 =
    (itemStack, clientLevel, livingEntity, seed) ->
      TextureMapBuilder.round((itemStack.getCount() - 1) * 0.01f);


  /**
   * 注册物品渲染附加
   */
  @SubscribeEvent
  public static void onClientSetup(FMLClientSetupEvent event) {
    register(event, ModItem.FOOD_ESSENCE, STACKING, STACKING_PRICE_64);
  }

  private static void register(FMLClientSetupEvent event, ItemLike item, ResourceLocation name, ClampedItemPropertyFunction property) {
    event.enqueueWork(() -> ItemProperties.register(item.asItem(), name, property));
  }
}

package ctn.futurefood.init;

import ctn.futurefood.FutureFood;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

/**
 * 物品渲染附加
 */
@EventBusSubscriber(value = Dist.CLIENT)
public class FfItemPropertyEvents {
	
	
	/**
	 * 注册物品渲染附加
	 */
	@SubscribeEvent
	public static void onClientSetup(FMLClientSetupEvent event) {
	}
	
	private static void registerProperties(FMLClientSetupEvent event, Item item, ResourceLocation propertiesName, ClampedItemPropertyFunction propertyFunction) {
		event.enqueueWork(() -> ItemProperties.register(item, propertiesName, propertyFunction));
	}
	
	private static ResourceLocation registerProperties(String name) {
		return ResourceLocation.fromNamespaceAndPath(FutureFood.ID, name);
	}
}

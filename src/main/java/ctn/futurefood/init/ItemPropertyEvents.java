package ctn.futurefood.init;

import ctn.ctntemplate.CtnTemplate;
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
public class ItemPropertyEvents {
//	public static final ResourceLocation MODE_BOOLEAN        = registerProperties("mode_boolean");

//	public static final ClampedItemPropertyFunction PROPERTY_MODE_BOOLEAN = (itemStack, clientLevel, livingEntity, i) ->
//			Boolean.TRUE.equals(itemStack.get(PmItemDataComponents.MODE_BOOLEAN)) ? 1 : 0;
	
	
	/**
	 * 注册物品渲染附加
	 */
	@SubscribeEvent
	public static void onClientSetup(FMLClientSetupEvent event) {
//		registerProperties(event, PmItems.CREATIVE_RATIONALITY_TOOL.asItem(), MODE_BOOLEAN, PROPERTY_MODE_BOOLEAN);
	}
	
	private static void registerProperties(FMLClientSetupEvent event, Item item, ResourceLocation propertiesName, ClampedItemPropertyFunction propertyFunction) {
		event.enqueueWork(() -> ItemProperties.register(item, propertiesName, propertyFunction));
	}
	
	private static ResourceLocation registerProperties(String name) {
		return ResourceLocation.fromNamespaceAndPath(CtnTemplate.ID, name);
	}
}

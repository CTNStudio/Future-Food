package ctn.futurefood.init;

import ctn.futurefood.FutureFood;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.NewRegistryEvent;


@EventBusSubscriber
public class FfRegistries {
	
	@SubscribeEvent
	public static void registrar(NewRegistryEvent event) {
	}
	
	private static ResourceLocation getPath(String spells) {
		return ResourceLocation.fromNamespaceAndPath(FutureFood.ID, spells);
	}
}

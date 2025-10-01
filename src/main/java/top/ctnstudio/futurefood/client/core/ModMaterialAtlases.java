package top.ctnstudio.futurefood.client.core;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMaterialAtlasesEvent;
import top.ctnstudio.futurefood.core.FutureFood;

import java.util.Map;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(modid = FutureFood.ID, value = Dist.CLIENT)
public final class ModMaterialAtlases {
  public static final ResourceLocation ENERGY_BALL = FutureFood.modRL("textures/atlas/energy_ball.png");
  public static final ResourceLocation GUI = FutureFood.modRL("textures/atlas/gui.png");

  private static final Map<ResourceLocation, ResourceLocation> MANAGER_TEXTURES = Map.of(
    ModMaterialAtlases.GUI,
    FutureFood.modRL("gui"),
    ModMaterialAtlases.ENERGY_BALL,
    FutureFood.modRL("energy_ball")
  );

  @SubscribeEvent
  public static void registerMaterialAtlases(RegisterMaterialAtlasesEvent event) {
    FutureFood.LOGGER.info("Registering Material Atlases");
    for (var manager : MANAGER_TEXTURES.entrySet()) {
      event.register(manager.getKey(), manager.getValue());
    }
    FutureFood.LOGGER.info("Registering Material Atlases Completed");
  }

  record Manager(ResourceLocation atlasLocation, ResourceLocation atlasInfoLocation) {
  }
}

package top.ctnstudio.futurefood.client;

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
  public static final ResourceLocation BATTERY = FutureFood.modRL("textures/atlas/battery.png");
  public static final ResourceLocation ICON = FutureFood.modRL("textures/atlas/icon.png");
  public static final ResourceLocation SPRITES = FutureFood.modRL("textures/atlas/sprites.png");

  private static final Map<ResourceLocation, ResourceLocation> MANAGER_TEXTURES = Map.of(
    ICON,
    FutureFood.modRL("icon"),
    ENERGY_BALL,
    FutureFood.modRL("energy_ball"),
    BATTERY,
    FutureFood.modRL("battery"),
    SPRITES,
    FutureFood.modRL("sprites")
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

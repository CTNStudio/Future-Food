package top.ctnstudio.futurefood.client.core;

import com.google.common.collect.Sets;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import top.ctnstudio.futurefood.client.renderer.block.QedBlockEntityRenderer;
import top.ctnstudio.futurefood.core.FutureFood;

import java.util.Set;
import java.util.stream.Stream;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(modid = FutureFood.ID, value = Dist.CLIENT)
public final class ModModelLayer {
  private static final Set<ModelLayerLocation> ALL_MODELS = Sets.newHashSet();
  public static final ModelLayerLocation ENERGY_BALL = register("energy_ball");

  @SubscribeEvent
  public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
    FutureFood.LOGGER.info("Registering Layer Definitions");

    event.registerLayerDefinition(ModModelLayer.ENERGY_BALL, QedBlockEntityRenderer::createBodyLayer);

    FutureFood.LOGGER.info("Registering Layer Definitions Completed");
  }

  private static ModelLayerLocation register(String path) {
    return register(path, "main");
  }

  private static ModelLayerLocation register(String path, String model) {
    ModelLayerLocation modellayerlocation = createLocation(path, model);
    if (ALL_MODELS.add(modellayerlocation)) {
      return modellayerlocation;
    }
    throw new IllegalStateException("Duplicate registration for " + modellayerlocation);
  }

  private static ModelLayerLocation createLocation(String path, String model) {
    return new ModelLayerLocation(ResourceLocation.withDefaultNamespace(path), model);
  }

  public static Stream<ModelLayerLocation> getKnownLocations() {
    return ALL_MODELS.stream();
  }
}

package top.ctnstudio.futurefood.core;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import top.ctnstudio.futurefood.client.renderer.BasicGeoBlockRenderer;
import top.ctnstudio.futurefood.client.renderer.ParticleColliderBlockEntityRenderer;
import top.ctnstudio.futurefood.core.init.ModTileEntity;

@Mod(value = FutureFood.ID, dist = Dist.CLIENT)
public class FfClient {
  @SuppressWarnings("unused")
  public FfClient(ModContainer container) {
    FutureFood.LOGGER.info("FutureFood Client Initialized");
  }

  @SubscribeEvent
  public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
    FutureFood.LOGGER.info("Registering Entity Renderers");
    registerBlockEntityRenderer(event, ModTileEntity.PARTICLE_COLLIDER.get(), ParticleColliderBlockEntityRenderer::new);
  }

  public static <T extends BlockEntity> void registerBlockEntityRenderer(final EntityRenderersEvent.RegisterRenderers event,
    BlockEntityType<? extends T> blockEntityType, BlockEntityRendererProvider blockEntityRendererProvider) {
    event.registerBlockEntityRenderer(blockEntityType, blockEntityRendererProvider);
  }

  public static <T extends BlockEntity> void registerBasicBlockEntityRenderer(final EntityRenderersEvent.RegisterRenderers event,
    BlockEntityType<? extends T> blockEntityType, BasicGeoBlockRenderer blockEntityRendererProvider) {
    event.registerBlockEntityRenderer(blockEntityType, context -> blockEntityRendererProvider);
  }
}

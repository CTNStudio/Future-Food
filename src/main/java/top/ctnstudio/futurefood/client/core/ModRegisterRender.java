package top.ctnstudio.futurefood.client.core;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import top.ctnstudio.futurefood.client.renderer.block.BasicGeoBlockRenderer;
import top.ctnstudio.futurefood.client.renderer.block.ParticleColliderBlockEntityRenderer;
import top.ctnstudio.futurefood.client.renderer.block.QedBlockEntityRenderer;
import top.ctnstudio.futurefood.core.FutureFood;
import top.ctnstudio.futurefood.core.init.ModTileEntity;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(modid = FutureFood.ID, value = Dist.CLIENT)
public final class ModRegisterRender {
  @SubscribeEvent
  public static void registerRenders(final EntityRenderersEvent.RegisterRenderers event) {
    FutureFood.LOGGER.info("Registering Entity Renderers");

    registerBlockEntityRenders(event, ModTileEntity.PARTICLE_COLLIDER.get(), ParticleColliderBlockEntityRenderer::new);
    event.registerBlockEntityRenderer(ModTileEntity.QED.get(), QedBlockEntityRenderer::new);

    FutureFood.LOGGER.info("Registering Entity Renderers Completed");
  }

  private static <T extends BlockEntity> void registerBlockEntityRenders(
    final EntityRenderersEvent.RegisterRenderers event, BlockEntityType<? extends T> blockEntityType,
    BlockEntityRendererProvider blockEntityRendererProvider) {
    event.registerBlockEntityRenderer(blockEntityType, blockEntityRendererProvider);
  }

  private static <T extends BlockEntity> void registerBasicBlockEntityRenderer(
    final EntityRenderersEvent.RegisterRenderers event, BlockEntityType<? extends T> blockEntityType,
    BasicGeoBlockRenderer blockEntityRendererProvider) {
    event.registerBlockEntityRenderer(blockEntityType, context -> blockEntityRendererProvider);
  }
}

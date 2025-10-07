package top.ctnstudio.futurefood.client;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import top.ctnstudio.futurefood.client.renderer.block.BasicGeoBlockRender;
import top.ctnstudio.futurefood.client.renderer.block.BatteryBlockEntityRender;
import top.ctnstudio.futurefood.client.renderer.block.ParticleColliderBlockEntityRender;
import top.ctnstudio.futurefood.client.renderer.block.QedBlockEntityRender;
import top.ctnstudio.futurefood.core.FutureFood;
import top.ctnstudio.futurefood.core.init.ModTileEntity;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(modid = FutureFood.ID, value = Dist.CLIENT)
public final class ModRegisterRender {
  @SubscribeEvent
  public static void registerRenders(final EntityRenderersEvent.RegisterRenderers event) {
    FutureFood.LOGGER.info("Registering Entity Renderers");

    registerBlockEntityRenders(event, ModTileEntity.BATTERY.get(), BatteryBlockEntityRender::new);

    event.registerBlockEntityRenderer(ModTileEntity.QED.get(), QedBlockEntityRender::new);
    event.registerBlockEntityRenderer(ModTileEntity.PARTICLE_COLLIDER.get(), ParticleColliderBlockEntityRender::new);

    FutureFood.LOGGER.info("Registering Entity Renderers Completed");
  }

  private static <T extends BlockEntity> void registerBlockEntityRenders(
    final EntityRenderersEvent.RegisterRenderers event, BlockEntityType<? extends T> blockEntityType,
    BlockEntityRendererProvider blockEntityRendererProvider) {
    event.registerBlockEntityRenderer(blockEntityType, blockEntityRendererProvider);
  }

  private static <T extends BlockEntity> void registerBasicBlockEntityRenderer(
    final EntityRenderersEvent.RegisterRenderers event, BlockEntityType<? extends T> blockEntityType,
    BasicGeoBlockRender blockEntityRendererProvider) {
    event.registerBlockEntityRenderer(blockEntityType, context -> blockEntityRendererProvider);
  }
}

package top.ctnstudio.futurefood.core;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import top.ctnstudio.futurefood.client.gui.menu.EnergyMenu;
import top.ctnstudio.futurefood.client.gui.screen.EnergyScreen;
import top.ctnstudio.futurefood.client.renderer.block.BasicGeoBlockRenderer;
import top.ctnstudio.futurefood.client.renderer.block.ParticleColliderBlockEntityRenderer;
import top.ctnstudio.futurefood.core.init.ModMenu;
import top.ctnstudio.futurefood.core.init.ModTileEntity;

@Mod(value = FutureFood.ID, dist = Dist.CLIENT)
@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(modid = FutureFood.ID, value = Dist.CLIENT)
public class FfClient {
  @SuppressWarnings("unused")
  public FfClient(ModContainer container) {
  }

  @SubscribeEvent
  public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
    FutureFood.LOGGER.info("Registering Entity Renderers");
    registerBlockEntityRenderer(event, ModTileEntity.PARTICLE_COLLIDER.get(),
      ParticleColliderBlockEntityRenderer::new);
  }

  @SubscribeEvent
  public static void registerScreens(RegisterMenuScreensEvent event) {
    event.register(ModMenu.ENERGY_MENU.get(), EnergyScreen::new);
  }

  private static <T extends BlockEntity> void registerBlockEntityRenderer(
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

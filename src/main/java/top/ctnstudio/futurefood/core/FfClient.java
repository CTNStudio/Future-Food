package top.ctnstudio.futurefood.core;

import com.mojang.datafixers.util.Function3;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMaterialAtlasesEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import top.ctnstudio.futurefood.client.gui.screen.EnergyScreen;
import top.ctnstudio.futurefood.client.renderer.block.BasicGeoBlockRenderer;
import top.ctnstudio.futurefood.client.renderer.block.ParticleColliderBlockEntityRenderer;
import top.ctnstudio.futurefood.client.renderer.block.QedBlockEntityRenderer;
import top.ctnstudio.futurefood.core.init.ModMenu;
import top.ctnstudio.futurefood.core.init.ModTileEntity;

@Mod(value = FutureFood.ID, dist = Dist.CLIENT)
@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(modid = FutureFood.ID, value = Dist.CLIENT)
public class FfClient {
//  public static final Supplier<LayerDefinition> EMPTY_LAYER_DEFINITION =
//    () -> LayerDefinition.create(new MeshDefinition(), 0, 0);

  @SuppressWarnings("unused")
  public FfClient(ModContainer container) {
  }

  @SubscribeEvent
  public static void registerAtlas(RegisterMaterialAtlasesEvent event) {
    event.register(FutureFood.modRL("energy_ball"), QedBlockEntityRenderer.ENERGY_BALL_SHEET);
  }

  @SubscribeEvent
  public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
    FutureFood.LOGGER.info("Registering Entity Renderers");

    registerBlockEntityRenderer(event, ModTileEntity.PARTICLE_COLLIDER.get(), ParticleColliderBlockEntityRenderer::new);
    event.registerBlockEntityRenderer(ModTileEntity.QED.get(), QedBlockEntityRenderer::new);
  }

  @SubscribeEvent
  public static void registerMenuScreens(RegisterMenuScreensEvent event) {
    FutureFood.LOGGER.info("Registering Menu Screens");

    registerScreen(event, ModMenu.ENERGY_MENU.get(), EnergyScreen::new);
  }

  @SubscribeEvent
  public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
    FutureFood.LOGGER.info("Registering Layer Definitions");

//    event.registerLayerDefinition(ModLayerDefinition.ENERGY_BALL, EMPTY_LAYER_DEFINITION);
  }

  private static <M extends AbstractContainerMenu, E extends Screen & MenuAccess<M>> void registerScreen(
    RegisterMenuScreensEvent event,
    MenuType<M> menuType, Function3<M, Inventory, Component, E> screenConstructor) {
    event.register(menuType, screenConstructor::apply);
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

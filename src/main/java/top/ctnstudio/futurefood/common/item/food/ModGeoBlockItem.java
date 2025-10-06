package top.ctnstudio.futurefood.common.item.food;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;
import top.ctnstudio.futurefood.client.renderer.item.BasicGeoBlockItemRenderer;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class ModGeoBlockItem extends BlockItem implements GeoItem {
  private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
  private final Supplier<BlockEntityWithoutLevelRenderer> defaultRenderer;

  public ModGeoBlockItem(Block block, Properties properties) {
    this(block, properties, () -> new BasicGeoBlockItemRenderer<ModGeoBlockItem>(block));
  }

  public ModGeoBlockItem(Block block, Properties properties,
                         Supplier<BlockEntityWithoutLevelRenderer> defaultRenderer) {
    super(block, properties);
    this.defaultRenderer = defaultRenderer;
  }

  @Override
  public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
  }

  @Override
  public AnimatableInstanceCache getAnimatableInstanceCache() {
    return cache;
  }

  @Override
  public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
    consumer.accept(new GeoRenderProvider() {
      private BlockEntityWithoutLevelRenderer renderer;

      @Override
      public BlockEntityWithoutLevelRenderer getGeoItemRenderer() {
        if (this.renderer == null) {
          BlockEntityWithoutLevelRenderer defaultRenderer = getDefaultRenderer();
          if (defaultRenderer != null) {
            this.renderer = defaultRenderer;
          }
        }

        return this.renderer;
      }
    });
  }

  public BlockEntityWithoutLevelRenderer getDefaultRenderer() {
    if (defaultRenderer != null) {
      BlockEntityWithoutLevelRenderer renderer = defaultRenderer.get();
      if (renderer != null) {
        return renderer;
      }
    }
    return new BasicGeoBlockItemRenderer<ModGeoBlockItem>(getBlock());
  }
}

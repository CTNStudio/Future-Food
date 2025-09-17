package top.ctnstudio.futurefood.client.renderer;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import software.bernie.geckolib.animatable.GeoAnimatable;
import top.ctnstudio.futurefood.client.model.BasicGeoModel;

public class BasicGeoBlockItemRenderer<T extends BlockItem & GeoAnimatable> extends BasicGeoItemRenderer<T> {
  public BasicGeoBlockItemRenderer(BasicGeoModel.BlockModel<T> model) {
    super(model);
  }

  public BasicGeoBlockItemRenderer(Block block) {
    this(BuiltInRegistries.BLOCK.getKey(block).getPath());
  }

  public BasicGeoBlockItemRenderer(String path) {
    super(new BasicGeoModel.BlockModel<>(path));
  }
}
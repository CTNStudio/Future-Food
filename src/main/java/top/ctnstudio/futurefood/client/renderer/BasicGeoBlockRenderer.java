package top.ctnstudio.futurefood.client.renderer;

import net.minecraft.world.level.block.entity.BlockEntity;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;
import top.ctnstudio.futurefood.client.model.BasicGeoModel;

public class BasicGeoBlockRenderer<T extends BlockEntity & GeoAnimatable> extends GeoBlockRenderer<T> {
  public BasicGeoBlockRenderer(GeoModel<T> model) {
    super(model);
  }

  public BasicGeoBlockRenderer(String path) {
    super(new BasicGeoModel.BlockModel<>(path));
  }
}
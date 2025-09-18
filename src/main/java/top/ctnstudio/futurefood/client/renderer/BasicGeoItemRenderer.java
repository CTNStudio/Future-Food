package top.ctnstudio.futurefood.client.renderer;

import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import top.ctnstudio.futurefood.client.model.BasicGeoModel;

@OnlyIn(Dist.CLIENT)
public class BasicGeoItemRenderer<T extends Item & GeoAnimatable> extends GeoItemRenderer<T> {
  public BasicGeoItemRenderer(GeoModel<T> model) {
    super(model);
  }

  public BasicGeoItemRenderer(String path) {
    super(new BasicGeoModel.ItemModel<>(path));
  }
}
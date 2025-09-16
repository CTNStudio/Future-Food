package top.ctnstudio.futurefood.client.model;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.model.GeoModel;
import top.ctnstudio.futurefood.core.FutureFood;

public class BasicGeoModel<T extends GeoAnimatable> extends GeoModel<T> {
  public final String path;

  public BasicGeoModel(String path) {
    this.path = path;
  }

  @Override
  public ResourceLocation getModelResource(T animatable) {
    return FutureFood.modRL("models/geo/" + path + ".geo.json");
  }

  @Override
  public ResourceLocation getTextureResource(T animatable) {
    return FutureFood.modRL("textures/" + path + ".png");
  }

  @Override
  public ResourceLocation getAnimationResource(T animatable) {
    return FutureFood.modRL("animations/" + path + ".json");
  }

  public static class BasicGeoBlockModel<T extends GeoAnimatable> extends BasicGeoModel<T> {
    public BasicGeoBlockModel(String path) {
      super("block/" + path);
    }
  }
}

package top.ctnstudio.futurefood.client.model;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.model.GeoModel;
import top.ctnstudio.futurefood.core.FutureFood;

public class BasicGeoModel<T extends GeoAnimatable> extends GeoModel<T> {
  public final ResourceLocation modelPath;
  public final ResourceLocation texturePath;
  public final ResourceLocation animationsPath;

  public BasicGeoModel(String pathName) {
    this(pathName, pathName, pathName);
  }

  public BasicGeoModel(String modelPath, String texturePath, String animationsPath) {
    this.modelPath = FutureFood.modRL("geo/" + modelPath + ".geo.json");
    this.texturePath = FutureFood.modRL("textures/" + texturePath + ".png");
    this.animationsPath = FutureFood.modRL("textures/" + animationsPath + ".png");
  }

  @Override
  public ResourceLocation getModelResource(T animatable) {
    return modelPath;
  }

  @Override
  public ResourceLocation getTextureResource(T animatable) {
    return texturePath;
  }

  @Override
  public ResourceLocation getAnimationResource(T animatable) {
    return animationsPath;
  }

  public static class BlockModel<T extends GeoAnimatable> extends BasicGeoModel<T> {
    public BlockModel(String pathName) {
      super("block/" + pathName);
    }

    public BlockModel(String modelPath, String texturePath, String animationsPath) {
      super(modelPath, texturePath, animationsPath);
    }
  }

  public static class ItemModel<T extends GeoAnimatable> extends BasicGeoModel<T> {
    public ItemModel(String pathName) {
      super("item/" + pathName);
    }

    public ItemModel(String modelPath, String texturePath, String animationsPath) {
      super(modelPath, texturePath, animationsPath);
    }
  }
}

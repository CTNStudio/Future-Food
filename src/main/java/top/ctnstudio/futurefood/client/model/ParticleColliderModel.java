package top.ctnstudio.futurefood.client.model;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import top.ctnstudio.futurefood.common.block.tile.ParticleColliderBlockEntity;

@OnlyIn(Dist.CLIENT)
public class ParticleColliderModel<T extends ParticleColliderBlockEntity>
  extends BasicGeoModel.BlockModel<T> {
  public ParticleColliderModel(String path) {
    super(path);
  }
}
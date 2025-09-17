package top.ctnstudio.futurefood.client.model;

import top.ctnstudio.futurefood.common.block.tile.ParticleColliderBlockEntity;

public class ParticleColliderModel<T extends ParticleColliderBlockEntity>
  extends BasicGeoModel.BlockModel<T> {
  public ParticleColliderModel(String path) {
    super(path);
  }
}
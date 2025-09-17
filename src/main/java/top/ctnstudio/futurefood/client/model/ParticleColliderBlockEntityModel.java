package top.ctnstudio.futurefood.client.model;

import top.ctnstudio.futurefood.common.block.tile.ParticleColliderBlockEntity;

public class ParticleColliderBlockEntityModel<T extends ParticleColliderBlockEntity> extends BasicGeoModel.BlockModel<T> {
  public ParticleColliderBlockEntityModel(String path) {
    super(path);
  }
}
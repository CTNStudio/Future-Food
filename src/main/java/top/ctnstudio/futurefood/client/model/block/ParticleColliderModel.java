package top.ctnstudio.futurefood.client.model.block;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import top.ctnstudio.futurefood.client.model.BasicGeoModel.BlockModel;
import top.ctnstudio.futurefood.common.block.tile.ParticleColliderBlockEntity;

@OnlyIn(Dist.CLIENT)
public class ParticleColliderModel<T extends ParticleColliderBlockEntity>
  extends BlockModel<T> {
  public ParticleColliderModel(String path) {
    super(path);
  }
}
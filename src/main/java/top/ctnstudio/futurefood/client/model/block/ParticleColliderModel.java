package top.ctnstudio.futurefood.client.model.block;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import top.ctnstudio.futurefood.client.model.BasicGeoModel;
import top.ctnstudio.futurefood.client.model.BasicGeoModel.BlockModel;
import top.ctnstudio.futurefood.common.block.ParticleColliderEntityBlock;
import top.ctnstudio.futurefood.common.block.tile.ParticleColliderBlockEntity;

@OnlyIn(Dist.CLIENT)
public class ParticleColliderModel<T extends ParticleColliderBlockEntity>
  extends BlockModel<T> {
  protected final ResourceLocation activateModel;

  public ParticleColliderModel() {
    super("particle_collider");
    activateModel = BasicGeoModel.getGeoModelPath("block/particle_collider_activate");
  }

  @Override
  public ResourceLocation getModelResource(T animatable) {
    return animatable.getBlockState().getValue(ParticleColliderEntityBlock.ACTIVATE) ? activateModel : super.getModelResource(animatable);
  }
}

package top.ctnstudio.futurefood.client.renderer;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import top.ctnstudio.futurefood.client.model.ParticleColliderModel;
import top.ctnstudio.futurefood.common.block.tile.ParticleColliderBlockEntity;

@OnlyIn(Dist.CLIENT)
public class ParticleColliderBlockEntityRenderer<T extends ParticleColliderBlockEntity>
  extends BasicGeoBlockRenderer<T> {
  public ParticleColliderBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    super(new ParticleColliderModel<>("particle_collider"));
  }
}
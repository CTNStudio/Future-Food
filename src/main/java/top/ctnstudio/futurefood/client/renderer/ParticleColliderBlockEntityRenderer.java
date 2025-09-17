package top.ctnstudio.futurefood.client.renderer;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.GeoModel;
import top.ctnstudio.futurefood.client.model.ParticleColliderModel;
import top.ctnstudio.futurefood.common.block.tile.ParticleColliderBlockEntity;

import java.util.Optional;

public class ParticleColliderBlockEntityRenderer<T extends ParticleColliderBlockEntity>
  extends BasicGeoBlockRenderer<T> {
  public ParticleColliderBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    super(new ParticleColliderModel<>("particle_collider"));
  }
}
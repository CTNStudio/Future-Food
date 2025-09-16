package top.ctnstudio.futurefood.client.renderer;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import top.ctnstudio.futurefood.client.model.ParticleColliderBlockEntityModel;
import top.ctnstudio.futurefood.common.block.tile.ParticleColliderBlockEntity;


public class ParticleColliderBlockEntityRenderer<T extends ParticleColliderBlockEntity>
  extends BasicGeoBlockRenderer<T> {
  public ParticleColliderBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    super(new ParticleColliderBlockEntityModel<>("particle_collider"));
  }
}
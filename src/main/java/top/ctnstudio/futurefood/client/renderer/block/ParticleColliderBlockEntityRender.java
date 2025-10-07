package top.ctnstudio.futurefood.client.renderer.block;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import top.ctnstudio.futurefood.client.model.block.ParticleColliderModel;
import top.ctnstudio.futurefood.common.block.tile.ParticleColliderBlockEntity;

@OnlyIn(Dist.CLIENT)
public final class ParticleColliderBlockEntityRender
  extends BasicGeoBlockRender<ParticleColliderBlockEntity> {
  public ParticleColliderBlockEntityRender(BlockEntityRendererProvider.Context context) {
    super(new ParticleColliderModel<>("particle_collider"));
  }

  @Override
  public AABB getRenderBoundingBox(ParticleColliderBlockEntity blockEntity) {
    BlockPos blockPos = blockEntity.getBlockPos();
    int x = blockPos.getX();
    int y = blockPos.getY();
    int z = blockPos.getZ();
    return new AABB(
      x - 1, y, z - 1,
      x + 1, y + 2, z + 1);
  }
}

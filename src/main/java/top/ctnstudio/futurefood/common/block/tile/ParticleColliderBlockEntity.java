package top.ctnstudio.futurefood.common.block.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import top.ctnstudio.futurefood.core.init.ModTileEntity;

public class ParticleColliderBlockEntity extends BlockEntity {
  public ParticleColliderBlockEntity(BlockPos pos, BlockState blockState) {
    super(ModTileEntity.PARTICLE_COLLIDER.get(), pos, blockState);
  }
}
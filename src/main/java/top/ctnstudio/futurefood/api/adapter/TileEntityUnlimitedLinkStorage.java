package top.ctnstudio.futurefood.api.adapter;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import top.ctnstudio.futurefood.core.FutureFood;

import javax.annotation.CheckForNull;

/**
 * 方块实体无限连接存储
 */
public class TileEntityUnlimitedLinkStorage extends UnlimitedLinkStorage {
  private final BlockEntity tile;

  public TileEntityUnlimitedLinkStorage(BlockEntity tile) {
    this.tile = tile;
  }

  @Override
  public void linkFailure(BlockPos pos) {
    FutureFood.LOGGER.warn("Tile pos of {} failed linked to block pos of {}!",
      tile.getBlockPos(), pos);
  }

  @Override
  @CheckForNull
  public final Level getLevel() {
    return tile.hasLevel() ? tile.getLevel() : null;
  }
}

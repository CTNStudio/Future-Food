package top.ctnstudio.futurefood.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.Optional;

public class BlockEntyUtil {
  /**
   * 获取方块实体
   *
   * @param <T> 方块实体的类型
   * @return 获取方块实体
   */
  public static <T extends BlockEntity> Optional<T> getBlockEntityFromLevel(Level level,
                                                                            BlockPos pos,
                                                                            BlockEntityType<T> type) {
    return level.getBlockEntity(pos, type);
  }

  /**
   * 获取方块实体
   * @return 获取方块实体
   */
  public static BlockEntity getBlockEntityFromLevel(Level level, BlockPos pos) {
    return level.getBlockEntity(pos);
  }
}

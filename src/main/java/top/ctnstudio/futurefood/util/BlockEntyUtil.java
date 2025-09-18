package top.ctnstudio.futurefood.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.Optional;
import java.util.function.Supplier;

public class BlockEntyUtil {
  public static <T extends BlockEntity> Optional<T> getBlockEntityFromLevel(Level level, BlockPos pos, BlockEntityType<T> type) {
    return level.getBlockEntity(pos, type);
  }

  public static BlockEntity getBlockEntityFromLevel(Level level, BlockPos pos) {
    return level.getBlockEntity(pos);
  }
}

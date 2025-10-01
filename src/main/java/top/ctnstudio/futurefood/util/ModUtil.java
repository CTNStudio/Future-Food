package top.ctnstudio.futurefood.util;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

public class ModUtil {
  /**
   * 快速发送到UI
   */
  public static void sendOverlayMessage(String text, Object... args) {
    Minecraft.getInstance().gui.setOverlayMessage(Component.translatable(text, args), false);
  }

  /**
   * 获取一个坐标的周围方块
   *
   * @param start     坐标
   * @param range     范围
   * @param predicate 筛选条件
   * @return 周围方块
   */
  public static Map<BlockPos, BlockState> rangePos(Level level, BlockPos start, int range,
                                                   Predicate<Map.Entry<BlockPos, BlockState>> predicate) {
    Map<BlockPos, BlockState> builder = new LinkedHashMap<>();
    for (int x = -range; x <= range; x++) {
      for (int y = -range; y <= range; y++) {
        for (int z = -range; z <= range; z++) {
          final BlockPos pos = start.offset(x, y, z);
          BlockState blockState = level.getBlockState(pos);
          if (predicate.test(Map.entry(pos, blockState))) {
            builder.put(pos, blockState);
          }
        }
      }
    }

    return builder;
  }

  /**
   * 获取反向方向
   *
   * @param be   方块实体
   * @param side 方向
   * @return 是否是反向方向
   */
  public static boolean getOppositeDirection(BlockEntity be, Direction side) {
    if (side == null) {
      return false;
    }
    Optional<Direction> optionalValue = be.getBlockState().getOptionalValue(BlockStateProperties.FACING);
    if (optionalValue.isEmpty()) {
      return false;
    }
    Direction value = optionalValue.get();
    return value == side.getOpposite();
  }
}

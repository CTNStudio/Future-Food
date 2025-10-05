package top.ctnstudio.futurefood.util;

import com.google.common.collect.LinkedListMultimap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.NotNull;
import top.ctnstudio.futurefood.common.block.DirectionEntityBlock;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

public class BlockUtil {
  /**
   * 获取一个坐标的对应方向的方块
   *
   * @param level     世界
   * @param pos       坐标
   * @param direction 方向
   * @return 周围方块
   */
  public static BlockState getSurroundingDirectionBlock(@NotNull Level level, @NotNull BlockPos pos, @NotNull Direction direction) {
    return level.getBlockState(pos.relative(direction, 1));
  }

  /**
   * 获取一个坐标周围方块
   *
   * @param level 世界
   * @param pos   坐标
   * @return 周围方块
   */
  public static LinkedListMultimap<Direction, BlockState> getSurroundingBlockState(@NotNull Level level, @NotNull BlockPos pos) {
    LinkedListMultimap<Direction, BlockState> map = LinkedListMultimap.create();
    for (Direction direction : Direction.values()) {
      map.put(direction, getSurroundingDirectionBlock(level, pos, direction));
    }
    return map;
  }

  /**
   * 获取一个坐标周围方块坐标
   *
   * @param level 世界
   * @param pos   坐标
   * @return 周围方块坐标
   */
  public static LinkedListMultimap<Direction, BlockPos> getSurroundingBlockPos(@NotNull Level level, @NotNull BlockPos pos) {
    LinkedListMultimap<Direction, BlockPos> map = LinkedListMultimap.create();
    for (Direction direction : Direction.values()) {
      map.put(direction, pos.relative(direction, 1));
    }
    return map;
  }

  /**
   * 获取方块的朝向
   *
   * @param bs 方块状态
   * @return 方向
   */
  public static Optional<Direction> getFacingDirection(@NotNull BlockState bs) {
    return bs.getOptionalValue(DirectionEntityBlock.FACING);
  }

  /**
   * 获取方块的朝向
   *
   * @param bs       方块状态
   * @param property 方向属性
   * @param property 方向属性
   * @return 方向
   */
  public static Optional<Direction> getDirection(@NotNull BlockState bs, Property<Direction> property) {
    return bs.getOptionalValue(property);
  }

  /**
   * 获取一个坐标
   *
   * @param pos 坐标
   * @return 坐标
   */
  public static BlockPos getBlockPos(final List<Integer> pos) {
    return new BlockPos(pos.get(0), pos.get(1), pos.get(2));
  }

  /**
   * 获取一个坐标
   *
   * @param pos 坐标
   * @return 坐标
   */
  public static BlockPos getBlockPos(final int... pos) {
    return new BlockPos(pos[0], pos[1], pos[2]);
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

  /**
   * 将坐标转为列表
   *
   * @param blockPos 坐标
   * @return 整数列表
   */
  @NotNull
  public static List<Integer> getPositionList(Vec3i blockPos) {
    return List.of(blockPos.getX(), blockPos.getY(), blockPos.getZ());
  }

  /**
   * 将坐标转为数组
   *
   * @param blockPos 坐标
   * @return 整数数组
   */
  public static int @NotNull [] getPositionArray(Vec3i blockPos) {
    return new int[]{blockPos.getX(), blockPos.getY(), blockPos.getZ()};
  }
}

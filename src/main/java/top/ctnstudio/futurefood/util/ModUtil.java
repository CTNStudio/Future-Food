package top.ctnstudio.futurefood.util;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

import java.util.*;
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

  /**
   * 控制两个能源槽之间的能量传递
   *
   * @param extract 被提取
   * @param receive 接收的
   */
  public static void controlEnergy(@NotNull IEnergyStorage extract, @NotNull IEnergyStorage receive) {
    int energyStored = extract.getEnergyStored();
    if (energyStored <= 0 ||
      receive.getEnergyStored() >= receive.getMaxEnergyStored() ||
      !extract.canExtract() || !receive.canReceive()) {
      return;
    }
    int extractValue = extract.extractEnergy(energyStored, true);
    if (extractValue <= 0) {
      return;
    }
    int receiveValue = receive.receiveEnergy(extractValue, true);
    if (receiveValue <= 0) {
      return;
    }
    extract.extractEnergy(receiveValue, false);
    receive.receiveEnergy(receiveValue, false);
  }

  /**
   * 获取 IItemHandler 中的物品
   *
   * @param handler IItemHandler 能力
   * @return 限定大小的物品集合
   */
  public static List<ItemStack> getItemStacks(IItemHandler handler) {
    int slots = handler.getSlots();
    ArrayList<ItemStack> arrayList = new ArrayList<>(slots);
    for (int i = 0; i < slots; i++) {
      arrayList.add(handler.getStackInSlot(i));
    }
    return arrayList;
  }

  public static BlockPos getBlockPos(final List<Integer> pos) {
    return new BlockPos(pos.get(0), pos.get(1), pos.get(2));
  }

  public static BlockPos getBlockPos(final int... pos) {
    return new BlockPos(pos[0], pos[1], pos[2]);
  }

  @NotNull
  public static List<Integer> getPositionList(Vec3i blockPos) {
    return List.of(blockPos.getX(), blockPos.getY(), blockPos.getZ());
  }

  public static int @NotNull [] getPositionArray(Vec3i blockPos) {
    return new int[]{blockPos.getX(), blockPos.getY(), blockPos.getZ()};
  }
}

package top.ctnstudio.futurefood.core.init;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import top.ctnstudio.futurefood.common.block.QedEntityBlock;
import top.ctnstudio.futurefood.common.block.tile.BasicEnergyStorageBlockEntity;

import java.util.Optional;

@EventBusSubscriber
public final class ModCapability {
  @SubscribeEvent
  public static void register(final RegisterCapabilitiesEvent event) {
    ModTileEntity.TILES.getEntries().forEach(entry -> {
      Block validBlock = entry.get().getValidBlocks().stream().iterator().next();
      BlockEntity blockEntity = entry.get().create(BlockPos.ZERO, validBlock.defaultBlockState());
      if (blockEntity instanceof BasicEnergyStorageBlockEntity) {
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, entry.get(),
          (be, d) ->
            ((BasicEnergyStorageBlockEntity) be).externalGetEnergyStorage(d));
      }
    });
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
    Optional<Direction> optionalValue = be.getBlockState().getOptionalValue(QedEntityBlock.FACING);
    if (optionalValue.isEmpty()) {
      return false;
    }
    Direction value = optionalValue.get();
    return value == side.getOpposite();
  }
}

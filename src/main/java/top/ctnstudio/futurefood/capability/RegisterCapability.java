package top.ctnstudio.futurefood.capability;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import top.ctnstudio.futurefood.common.block.QedEntityBlock;
import top.ctnstudio.futurefood.core.init.ModTileEntity;

import java.util.Optional;

/**
 * @author wang_
 * @version 2024.3.4.1
 * @description
 * @date 2025/9/18
 */
@EventBusSubscriber
public final class RegisterCapability {
  @SubscribeEvent
  public static void register(final RegisterCapabilitiesEvent event) {
    event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK,
      ModTileEntity.PARTICLE_COLLIDER.get(),
      (be, side) -> be.getEnergyStorage());
    event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, ModTileEntity.QED.get(),
      (be, side) -> !getOppositeDirection(be, side) ? null : be.getEnergyStorage());
    event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, ModTileEntity.QER.get(),
      (be, side) -> !getOppositeDirection(be, side) ? null : be.getEnergyStorage());
  }

  /**
   * 获取反向方向
   *
   * @param be   方块实体
   * @param side 方向
   * @return 是否是反向方向
   */
  private static boolean getOppositeDirection(BlockEntity be, Direction side) {
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

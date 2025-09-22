package top.ctnstudio.futurefood.common.block.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.Nullable;
import top.ctnstudio.futurefood.api.tile.IUnlimitedEntityReceive;
import top.ctnstudio.futurefood.capability.ModEnergyStorage;
import top.ctnstudio.futurefood.core.FutureFood;
import top.ctnstudio.futurefood.core.init.ModTileEntity;

import static top.ctnstudio.futurefood.core.init.ModCapability.getOppositeDirection;

public class QerBlockEntity extends EnergyStorageBlockEntity implements IUnlimitedEntityReceive {
  public static final String GUI_NAME = FutureFood.ID + ".quantum_energy_receiver.gui.name";
  private static final MutableComponent DISPLAY_NAME = Component.translatable(GUI_NAME);

  public QerBlockEntity(BlockPos pos, BlockState blockState) {
    super(ModTileEntity.QER.get(), pos, blockState, new ModEnergyStorage(20480, 4096, 4096));
  }

  @Override
  public IEnergyStorage externalGetEnergyStorage(@Nullable Direction direction) {
    if (direction == null) {
      return energyStorage;
    }
    return !getOppositeDirection(this, direction) ? null :
      super.externalGetEnergyStorage(direction);
  }

  @Override
  public Component getDisplayName() {
    return DISPLAY_NAME;
  }

  /**
   * 能量存储
   */
  @Override
  public IEnergyStorage getEnergyStorage() {
    return energyStorage;
  }
}

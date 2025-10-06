package top.ctnstudio.futurefood.common.block.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.ctnstudio.futurefood.api.adapter.ModEnergyStorage;
import top.ctnstudio.futurefood.api.adapter.ModItemStackHandler;
import top.ctnstudio.futurefood.common.menu.InputOutputEnergyMenu;
import top.ctnstudio.futurefood.core.init.ModTileEntity;

public class BatteryBlockEntity extends BaseEnergyStorageBlockEntity<InputOutputEnergyMenu> {
  public BatteryBlockEntity(BlockPos pos, BlockState blockState) {
    super(ModTileEntity.BATTERY.get(), pos, blockState, new ModItemStackHandler(2), new ModEnergyStorage(1024000));
  }

  @Override
  public void tick(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState bs) {
    if (level.isClientSide()) {
      return;
    }
    controlItemEnergy(itemHandler.getStackInSlot(0), true);
    controlItemEnergy(itemHandler.getStackInSlot(1), false);
  }

  @Override
  public @Nullable InputOutputEnergyMenu createMenu(int containerId, Inventory playerInventory, Player player) {
    if (!player.isAlive()) {
      return null;
    }
    return new InputOutputEnergyMenu(containerId, playerInventory, itemHandler, energyData);
  }
}

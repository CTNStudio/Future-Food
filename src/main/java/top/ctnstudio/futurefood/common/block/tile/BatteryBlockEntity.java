package top.ctnstudio.futurefood.common.block.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.ctnstudio.futurefood.api.adapter.InfiniteModEnergyStorage;
import top.ctnstudio.futurefood.api.adapter.ModEnergyStorage;
import top.ctnstudio.futurefood.api.adapter.ModItemStackHandler;
import top.ctnstudio.futurefood.common.menu.InputOutputEnergyMenu;
import top.ctnstudio.futurefood.core.init.ModTileEntity;
import top.ctnstudio.futurefood.util.EnergyUtil;

public class BatteryBlockEntity extends BaseEnergyStorageBlockEntity<InputOutputEnergyMenu> {
  private final boolean isInfinite;

  public BatteryBlockEntity(BlockPos pos, BlockState blockState) {
    this(pos, blockState, false);
  }

  public BatteryBlockEntity(BlockPos pos, BlockState blockState, boolean isInfinite) {
    super(ModTileEntity.BATTERY.get(), pos, blockState, new ModItemStackHandler(2),
      isInfinite ? new InfiniteModEnergyStorage() : new ModEnergyStorage(1024000));
    this.isInfinite = isInfinite;
  }

  @Override
  public void tick(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState bs) {
    if (level.isClientSide()) {
      return;
    }
    controlItemEnergy(itemHandler.getStackInSlot(0), true);
    controlItemEnergy(itemHandler.getStackInSlot(1), false);

    if (isInfinite) outputEnergy(level, pos);
  }

  // TODO 注意：未做优化 理论要根据方块更新进行缓存再传输，以避免过多的获取
  protected void outputEnergy(@NotNull Level level, @NotNull BlockPos pos) {
    if (energyStorage.getEnergyStored() > 0 && energyStorage.canExtract()) {
      EnergyUtil.getSurroundingEnergyStorage(level, pos).values().stream()
        .filter(e -> e.getValue().isPresent())
        .map(e -> e.getValue().get())
        .forEach(energyStorage -> EnergyUtil.controlEnergy(this.energyStorage, energyStorage));
    }
  }

  @Override
  protected void loadAdditional(CompoundTag nbt, HolderLookup.Provider provider) {
    super.loadAdditional(nbt, provider);
  }

  @Override
  protected void saveAdditional(CompoundTag nbt, HolderLookup.Provider provider) {
    super.saveAdditional(nbt, provider);
  }

  @Override
  public @Nullable InputOutputEnergyMenu createMenu(int containerId, Inventory playerInventory, Player player) {
    if (!player.isAlive()) {
      return null;
    }
    return new InputOutputEnergyMenu(containerId, playerInventory, itemHandler, energyData);
  }

  public boolean isInfinite() {
    return isInfinite;
  }
}

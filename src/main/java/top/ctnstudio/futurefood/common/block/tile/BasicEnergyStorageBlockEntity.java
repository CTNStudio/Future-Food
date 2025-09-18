package top.ctnstudio.futurefood.common.block.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.energy.EnergyStorage;
import top.ctnstudio.futurefood.capability.ModEnergyStorage;

public class BasicEnergyStorageBlockEntity<T extends BlockEntity> extends BlockEntity {
  protected final ModEnergyStorage energyStorage;

  public BasicEnergyStorageBlockEntity(BlockEntityType<? extends T> type,BlockPos pos, BlockState blockState) {
    this(type, pos, blockState, new ModEnergyStorage(10240, 1024, 1024));
  }

  public BasicEnergyStorageBlockEntity(BlockEntityType<? extends T> type, BlockPos pos, BlockState blockState, ModEnergyStorage energyStorage) {
    super(type, pos, blockState);
    this.energyStorage = energyStorage;
  }

  @Override
  protected void loadAdditional(CompoundTag nbt, HolderLookup.Provider registries) {
    super.loadAdditional(nbt, registries);
    ModEnergyStorage.serializeNBT(registries, nbt, energyStorage);
  }

  @Override
  protected void saveAdditional(CompoundTag nbt, HolderLookup.Provider registries) {
    super.saveAdditional(nbt, registries);
    ModEnergyStorage.deserializeNBT(registries, nbt, energyStorage);
  }

  public ModEnergyStorage getEnergyStorage() {
    return energyStorage;
  }
}

package top.ctnstudio.futurefood.api.adapter;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.neoforged.neoforge.energy.EnergyStorage;
import top.ctnstudio.futurefood.api.capability.IEnergyStorageModify;
import top.ctnstudio.futurefood.api.capability.IModEnergyStorage;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ModEnergyStorage extends EnergyStorage implements IModEnergyStorage {
  @Nullable
  protected IEnergyStorageModify onContentsChanged;

  public ModEnergyStorage(int capacity) {
    super(capacity);
  }

  public ModEnergyStorage(int capacity, int maxTransfer) {
    super(capacity, maxTransfer);
  }

  public ModEnergyStorage(int capacity, int maxReceive, int maxExtract) {
    super(capacity, maxReceive, maxExtract);
  }

  public ModEnergyStorage(int capacity, int maxReceive, int maxExtract, int energy) {
    super(capacity, maxReceive, maxExtract, energy);
  }

  @Override
  public void setEnergy(@Nonnegative int energy) {
    this.energy = energy;
    onChanged();
  }

  @Override
  public void setMaxEnergyStored(@Nonnegative int capacity) {
    this.capacity = capacity;
    onChanged();
  }

  @Override
  @Nonnegative
  public int getMaxExtract() {
    return maxExtract;
  }

  @Override
  public void setMaxExtract(@Nonnegative int maxExtract) {
    this.maxExtract = maxExtract;
    onChanged();
  }

  @Override
  @Nonnegative
  public int getMaxReceive() {
    return maxReceive;
  }

  @Override
  public void setMaxReceive(@Nonnegative int maxReceive) {
    this.maxReceive = maxReceive;
    onChanged();
  }

  @Override
  public int receiveEnergy(int toReceive, boolean simulate) {
    int energy = super.receiveEnergy(toReceive, simulate);
    if (!simulate) onChanged();
    return energy;
  }

  @Override
  public int extractEnergy(int toExtract, boolean simulate) {
    int energy = super.extractEnergy(toExtract, simulate);
    if (!simulate) onChanged();
    return energy;
  }

  @Override
  @Nonnull
  public Tag serializeNBT(HolderLookup.Provider provider) {
    CompoundTag nbt = new CompoundTag(4);
    nbt.putInt("energy", energy);
    nbt.putInt("capacity", capacity);
    nbt.putInt("maxReceive", maxReceive);
    nbt.putInt("maxExtract", maxExtract);
    return nbt;
  }

  @Override
  public void deserializeNBT(HolderLookup.Provider provider, Tag nbt) {
    if (!(nbt instanceof CompoundTag compoundTag)) {
      throw new IllegalArgumentException("Can not deserialize to an instance that isn't the " +
        "default implementation");
    }
    energy = compoundTag.getInt("energy");
    capacity = compoundTag.getInt("capacity");
    maxReceive = compoundTag.getInt("maxReceive");
    maxExtract = compoundTag.getInt("maxExtract");
    onLoad();
  }

  @Override
  public String toString() {
    return "EnergyStorage:{" +
      "energy=" + energy +
      "capacity=" + capacity +
      "maxReceive=" + maxReceive +
      "maxExtract=" + maxExtract + "}";
  }

  public int getPercentage() {
    return Math.round((float) energy / capacity * 100.0f);
  }

  public void setOn(IEnergyStorageModify onContentsChanged) {
    this.onContentsChanged = onContentsChanged;
  }

  public void onChanged() {
    if (onContentsChanged != null) onContentsChanged.onEnergyChanged();
  }

  public void onLoad() {
    if (onContentsChanged != null) onContentsChanged.onEnergyLoad();
  }
}

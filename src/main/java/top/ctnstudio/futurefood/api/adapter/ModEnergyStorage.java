package top.ctnstudio.futurefood.api.adapter;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.neoforged.neoforge.energy.EnergyStorage;
import top.ctnstudio.futurefood.api.capability.IModEnergyStorage;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

public class ModEnergyStorage extends EnergyStorage implements IModEnergyStorage {
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
  }

  @Override
  public void setMaxEnergyStored(@Nonnegative int capacity) {
    this.capacity = capacity;
  }

  @Override
  @Nonnegative
  public int getMaxExtract() {
    return maxExtract;
  }

  @Override
  public void setMaxExtract(@Nonnegative int maxExtract) {
    this.maxExtract = maxExtract;
  }

  @Override
  @Nonnegative
  public int getMaxReceive() {
    return maxReceive;
  }

  @Override
  public void setMaxReceive(@Nonnegative int maxReceive) {
    this.maxReceive = maxReceive;
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
  }

//  /**
//   * 序列化 NBT
//   */
//  public static void serializeNBT(HolderLookup.Provider provider, CompoundTag nbt,
//                                  IModEnergyStorage storage) {
//    nbt.putInt("energy", storage.getEnergyStored());
//    nbt.putInt("capacity", storage.getMaxEnergyStored());
//    nbt.putInt("maxReceive", storage.getMaxReceive());
//    nbt.putInt("maxExtract", storage.getMaxExtract());
//  }
//
//  /**
//   * 反序列化 NBT
//   */
//  public static void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt,
//                                    IModEnergyStorage storage) {
//    storage.setEnergy(nbt.getInt("energy"));
//    storage.setMaxEnergyStored(nbt.getInt("capacity"));
//    storage.setMaxReceive(nbt.getInt("maxReceive"));
//    storage.setMaxExtract(nbt.getInt("maxExtract"));
//  }
}

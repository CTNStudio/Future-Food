package top.ctnstudio.futurefood.api.capability;

import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.Nullable;

public interface IModEnergyStorage extends IEnergyStorage {
  void setEnergy(int energy);

  void setMaxEnergyStored(int capacity);

  int getMaxExtract();

  void setMaxExtract(int maxExtract);

  int getMaxReceive();

  void setMaxReceive(int maxReceive);

  @Nullable
  static IModEnergyStorage of(IEnergyStorage i) {
    return i instanceof IModEnergyStorage ? (IModEnergyStorage) i : null;
  }

}

package top.ctnstudio.futurefood.capability;

import net.neoforged.neoforge.energy.IEnergyStorage;

public interface IModEnergyStorage extends IEnergyStorage {
  void setEnergy(int energy);
  void setMaxEnergyStored(int capacity);
  int getMaxExtract();
  void setMaxExtract(int maxExtract);
  int getMaxReceive();
  void setMaxReceive(int maxReceive);
}

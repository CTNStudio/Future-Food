package top.ctnstudio.futurefood.api.adapter;

public class InfiniteModEnergyStorage extends ModEnergyStorage {
  public InfiniteModEnergyStorage() {
    super(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
  }

  @Override
  public int getEnergyStored() {
    return Integer.MAX_VALUE;
  }

  @Override
  public int getMaxEnergyStored() {
    return Integer.MAX_VALUE;
  }

  @Override
  public int getMaxExtract() {
    return Integer.MAX_VALUE;
  }

  @Override
  public int getMaxReceive() {
    return 0;
  }

  @Override
  public boolean canExtract() {
    return true;
  }

  @Override
  public boolean canReceive() {
    return false;
  }

  @Override
  public void setEnergy(int energy) {
  }

  @Override
  public void setMaxEnergyStored(int capacity) {
  }

  @Override
  public void setMaxExtract(int maxExtract) {
  }

  @Override
  public void setMaxReceive(int maxReceive) {
  }
}

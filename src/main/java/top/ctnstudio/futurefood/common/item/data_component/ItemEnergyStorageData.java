package top.ctnstudio.futurefood.common.item.data_component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.common.MutableDataComponentHolder;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;
import top.ctnstudio.futurefood.api.adapter.ModEnergyStorage;
import top.ctnstudio.futurefood.api.capability.IModEnergyStorage;
import top.ctnstudio.futurefood.core.init.ModDataComponent;

import java.util.function.Supplier;

public record ItemEnergyStorageData(
  MutableDataComponentHolder parent) implements top.ctnstudio.futurefood.api.capability.IModEnergyStorage {
  public static final DataComponentType<Data> ENERGY_COMPONENT = ModDataComponent.ENERGY_STORAGE.get();

  @Override
  public void setEnergy(int energy) {
    getEnergyStorageData().setEnergy(energy);
  }

  public @NotNull Data getEnergyStorageData() {
    return parent.getOrDefault(ENERGY_COMPONENT, Data.DEFAULT.get());
  }

  public void setEnergyStorageData(Data data) {
    parent.set(ENERGY_COMPONENT, data);
  }

  @Override
  public void setMaxEnergyStored(int capacity) {
    getEnergyStorageData().setMaxEnergyStored(capacity);
  }

  @Override
  public int getMaxExtract() {
    return getEnergyStorageData().getMaxExtract();
  }

  @Override
  public void setMaxExtract(int maxExtract) {
    getEnergyStorageData().setMaxExtract(maxExtract);
  }

  @Override
  public int getMaxReceive() {
    return getEnergyStorageData().getMaxReceive();
  }

  @Override
  public void setMaxReceive(int maxReceive) {
    getEnergyStorageData().setMaxReceive(maxReceive);
  }

  @Override
  public int receiveEnergy(int toReceive, boolean simulate) {
    return getEnergyStorageData().receiveEnergy(toReceive, simulate);
  }

  @Override
  public int extractEnergy(int toExtract, boolean simulate) {
    return getEnergyStorageData().extractEnergy(toExtract, simulate);
  }

  @Override
  public int getEnergyStored() {
    return getEnergyStorageData().getEnergyStored();
  }

  @Override
  public int getMaxEnergyStored() {
    return getEnergyStorageData().getMaxEnergyStored();
  }

  @Override
  public boolean canExtract() {
    return getEnergyStorageData().canExtract();
  }

  @Override
  public boolean canReceive() {
    return getEnergyStorageData().canReceive();
  }

  public static class Data extends ModEnergyStorage {
    public static final Supplier<Data> DEFAULT = () -> new Data(1000);

    public static final StreamCodec<ByteBuf, Data> STREAM = StreamCodec.composite(
      ByteBufCodecs.VAR_INT, Data::getMaxEnergyStored,
      ByteBufCodecs.VAR_INT, Data::getMaxReceive,
      ByteBufCodecs.VAR_INT, Data::getMaxExtract,
      ByteBufCodecs.VAR_INT, Data::getEnergyStored,
      Data::new);

    public static final Codec<Data> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.INT.fieldOf("maxEnergyStored").forGetter(Data::getMaxEnergyStored),
        Codec.INT.fieldOf("maxReceive").forGetter(Data::getMaxReceive),
        Codec.INT.fieldOf("maxExtract").forGetter(Data::getMaxExtract),
        Codec.INT.fieldOf("energy").forGetter(Data::getEnergyStored))
      .apply(instance, Data::new));

    public Data(int maxEnergyStored) {
      super(maxEnergyStored);
    }

    public Data(int maxEnergyStored, int maxTransfer) {
      super(maxEnergyStored, maxTransfer);
    }

    public Data(int maxEnergyStored, int maxReceive, int maxExtract) {
      super(maxEnergyStored, maxReceive, maxExtract);
    }

    public Data(int maxEnergyStored, int maxReceive, int maxExtract, int energy) {
      super(maxEnergyStored, maxReceive, maxExtract, energy);
    }

    public Data(IModEnergyStorage energyStorage) {
      super(energyStorage.getMaxEnergyStored(), energyStorage.getMaxReceive(),
        energyStorage.getMaxExtract(), energyStorage.getEnergyStored());
    }

    public Data(IEnergyStorage energyStorage) {
      super(energyStorage.getMaxEnergyStored(), energyStorage.getMaxEnergyStored(),
        energyStorage.getMaxEnergyStored(), energyStorage.getEnergyStored());
    }

    public static Data iEnergyStorageCreateData(IEnergyStorage energyStorage) {
      return new Data(energyStorage);
    }

    @Override
    public boolean equals(Object obj) {
      return switch (obj) {
        case ModEnergyStorage energyStorage ->
          energyStorage.getEnergyStored() == getEnergyStored() &&
            energyStorage.getMaxEnergyStored() == getMaxEnergyStored() &&
            energyStorage.getMaxExtract() == getMaxExtract() &&
            energyStorage.getMaxReceive() == getMaxReceive();
        case null, default -> false;
      };
    }

    @Override
    public int hashCode() {
      return super.hashCode() + getMaxEnergyStored() + getMaxReceive() + getMaxExtract() + getEnergyStored();
    }

    @Override
    public String toString() {
      return "EnergyStorageData{" +
        "maxEnergyStored=" + getMaxEnergyStored() +
        "maxReceive=" + getMaxReceive() +
        "maxExtract=" + getMaxExtract() +
        "energy=" + getEnergyStored() +
        '}';
    }
  }
}

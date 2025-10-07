package top.ctnstudio.futurefood.common.data_component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.Mth;
import net.neoforged.neoforge.common.MutableDataComponentHolder;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;
import top.ctnstudio.futurefood.api.adapter.ModEnergyStorage;
import top.ctnstudio.futurefood.api.capability.IModEnergyStorage;
import top.ctnstudio.futurefood.core.init.ModDataComponent;
import top.ctnstudio.futurefood.util.EnergyUtil;

import java.util.function.Supplier;

// TODO 无限状态会被修改 仅影响物品
public record ModComponentEnergyStorage(MutableDataComponentHolder parent,
                                        DataComponentType<EnergyStorageData> dataComponentType)
  implements IModEnergyStorage {

  public ModComponentEnergyStorage(MutableDataComponentHolder parent,
                                   Supplier<DataComponentType<EnergyStorageData>> dataComponentType) {
    this(parent, dataComponentType.get());
  }

  public ModComponentEnergyStorage(MutableDataComponentHolder parent) {
    this(parent, ModDataComponent.ENERGY_STORAGE);
  }

  public @NotNull EnergyStorageData getEnergyStorageData() {
    return parent.getOrDefault(dataComponentType, EnergyStorageData.DEFAULT.get());
  }

  public void setEnergyStorageData(EnergyStorageData data) {
    parent.set(dataComponentType, data);
  }

  @Override
  public void setEnergy(int energy) {
    setEnergyStorageData(getEnergyStorageData().setEnergyStored(energy));
  }

  @Override
  public void setMaxEnergyStored(int capacity) {
    setEnergyStorageData(getEnergyStorageData().setMaxEnergyStored(capacity));
  }

  @Override
  public int getMaxExtract() {
    return getEnergyStorageData().maxExtract();
  }

  @Override
  public void setMaxExtract(int maxExtract) {
    setEnergyStorageData(getEnergyStorageData().setMaxExtract(maxExtract));
  }

  @Override
  public int getMaxReceive() {
    return getEnergyStorageData().maxReceive();
  }

  @Override
  public void setMaxReceive(int maxReceive) {
    setEnergyStorageData(getEnergyStorageData().setMaxReceive(maxReceive));
  }

  public void setEnergyStored(int maxEnergyStored, int maxReceive, int maxExtract, int energyStored) {
    setEnergyStorageData(getEnergyStorageData().setEnergyStored(maxEnergyStored, maxReceive, maxExtract, energyStored));
  }

  public void setEnergyStored(IModEnergyStorage iModEnergyStorage) {
    EnergyUtil.copyEnergy(this, iModEnergyStorage);
  }

  @Override
  public int receiveEnergy(int toReceive, boolean simulate) {
    var data = getEnergyStorageData();
    if (!canReceive() || toReceive <= 0) {
      return 0;
    }

    int energyReceived = Mth.clamp(data.maxEnergyStored() - data.energyStored(),
      0, Math.min(data.maxReceive(), toReceive));
    if (!simulate) setEnergy(data.energyStored() + energyReceived);
    return energyReceived;
  }

  @Override
  public int extractEnergy(int toExtract, boolean simulate) {
    EnergyStorageData data = getEnergyStorageData();
    if (!canExtract() || toExtract <= 0) {
      return 0;
    }

    int energyExtracted = Math.min(data.energyStored(), Math.min(data.maxExtract(), toExtract));
    if (!simulate) setEnergy(data.energyStored() - energyExtracted);
    return energyExtracted;
  }

  @Override
  public int getEnergyStored() {
    return getEnergyStorageData().energyStored();
  }

  @Override
  public int getMaxEnergyStored() {
    return getEnergyStorageData().maxEnergyStored();
  }

  @Override
  public boolean canExtract() {
    return getMaxExtract() > 0;
  }

  @Override
  public boolean canReceive() {
    return getMaxReceive() > 0;
  }

  public record EnergyStorageData(int maxEnergyStored, int maxReceive, int maxExtract,
                                  int energyStored) {
    public static final Supplier<EnergyStorageData> DEFAULT = () -> new EnergyStorageData(1000);
    public static final StreamCodec<ByteBuf, EnergyStorageData> STREAM = StreamCodec.composite(
      ByteBufCodecs.VAR_INT, EnergyStorageData::maxEnergyStored,
      ByteBufCodecs.VAR_INT, EnergyStorageData::maxReceive,
      ByteBufCodecs.VAR_INT, EnergyStorageData::maxExtract,
      ByteBufCodecs.VAR_INT, EnergyStorageData::energyStored,
      EnergyStorageData::new);
    public static final Codec<EnergyStorageData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.INT.fieldOf("maxEnergyStored").forGetter(EnergyStorageData::maxEnergyStored),
        Codec.INT.fieldOf("maxReceive").forGetter(EnergyStorageData::maxReceive),
        Codec.INT.fieldOf("maxExtract").forGetter(EnergyStorageData::maxExtract),
        Codec.INT.fieldOf("energyStored").forGetter(EnergyStorageData::energyStored))
      .apply(instance, EnergyStorageData::new));

    public EnergyStorageData(int maxEnergyStored) {
      this(maxEnergyStored, maxEnergyStored);
    }

    public EnergyStorageData(int maxEnergyStored, int maxTransfer) {
      this(maxEnergyStored, maxTransfer, maxTransfer);
    }

    public EnergyStorageData(int maxEnergyStored, int maxReceive, int maxExtract) {
      this(maxEnergyStored, maxReceive, maxExtract, 0);
    }

    public EnergyStorageData(IModEnergyStorage energyStorage) {
      this(energyStorage.getMaxEnergyStored(), energyStorage.getMaxReceive(), energyStorage.getMaxReceive(), energyStorage.getEnergyStored());
    }

    public EnergyStorageData(IEnergyStorage energyStorage) {
      this(energyStorage.getMaxEnergyStored(), energyStorage.getMaxEnergyStored(), energyStorage.getMaxEnergyStored(), energyStorage.getEnergyStored());
    }

    @Override
    public boolean equals(Object obj) {
      return switch (obj) {
        case ModEnergyStorage energyStorage -> energyStorage.getEnergyStored() == energyStored() &&
          energyStorage.getMaxEnergyStored() == maxEnergyStored() &&
          energyStorage.getMaxExtract() == maxExtract() &&
          energyStorage.getMaxReceive() == maxReceive();
        case null, default -> false;
      };
    }

    public EnergyStorageData setEnergyStored(int energyStored) {
      return new EnergyStorageData(maxEnergyStored, maxReceive, maxExtract, energyStored);
    }

    public EnergyStorageData setMaxExtract(int maxExtract) {
      return new EnergyStorageData(maxEnergyStored, maxReceive, maxExtract, energyStored);
    }

    public EnergyStorageData setMaxReceive(int maxReceive) {
      return new EnergyStorageData(maxEnergyStored, maxReceive, maxExtract, energyStored);
    }

    public EnergyStorageData setMaxEnergyStored(int maxEnergyStored) {
      return new EnergyStorageData(maxEnergyStored, maxReceive, maxExtract, energyStored);
    }

    public EnergyStorageData setEnergyStored(int maxEnergyStored, int maxReceive,
                                             int maxExtract, int energyStored) {
      return new EnergyStorageData(maxEnergyStored, maxReceive, maxExtract, energyStored);
    }
  }
}

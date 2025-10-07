package top.ctnstudio.futurefood.common.payloads;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;
import top.ctnstudio.futurefood.api.adapter.ModEnergyStorage;
import top.ctnstudio.futurefood.api.capability.IModEnergyStorage;
import top.ctnstudio.futurefood.core.FutureFood;
import top.ctnstudio.futurefood.util.EnergyUtil;

/**
 * 能源存储数据包
 */
public record EnergyStorageData(BlockPos pos,
                                IModEnergyStorage iModEnergyStorage) implements CustomPacketPayload {
  public static final Type<EnergyStorageData> TYPE = new Type<>(FutureFood.modRL("energy_storage_data"));
  public static final StreamCodec<ByteBuf, BlockPos> POS_STREAM = StreamCodec.composite(
    ByteBufCodecs.VAR_INT, BlockPos::getX,
    ByteBufCodecs.VAR_INT, BlockPos::getY,
    ByteBufCodecs.VAR_INT, BlockPos::getZ,
    BlockPos::new);

  public static final StreamCodec<ByteBuf, IModEnergyStorage> ENERGY_STORAGE_STREAM = StreamCodec.composite(
    ByteBufCodecs.VAR_INT, IModEnergyStorage::getMaxEnergyStored,
    ByteBufCodecs.VAR_INT, IModEnergyStorage::getMaxReceive,
    ByteBufCodecs.VAR_INT, IModEnergyStorage::getMaxExtract,
    ByteBufCodecs.VAR_INT, IModEnergyStorage::getEnergyStored,
    ModEnergyStorage::new);

  public static final StreamCodec<ByteBuf, EnergyStorageData> STREAM_CODEC = StreamCodec.composite(
    POS_STREAM, EnergyStorageData::pos,
    ENERGY_STORAGE_STREAM, EnergyStorageData::iModEnergyStorage,
    EnergyStorageData::new
  );

  /**
   * 发送到服务端
   */
  public static void toServer(final EnergyStorageData data, final IPayloadContext context) {
  }

  /**
   * 发送到客户端
   */
  public static void toClient(final EnergyStorageData data, final IPayloadContext context) {
    context.enqueueWork(() -> {
      Player player = context.player();
      var capability = player.level().getCapability(Capabilities.EnergyStorage.BLOCK, data.pos, null);
      if (!(capability instanceof IModEnergyStorage iModEnergyStorage)) {
        return;
      }
      EnergyUtil.copyEnergy(iModEnergyStorage, data.iModEnergyStorage());
    });
  }

  @Override
  public @NotNull Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }

  public int maxEnergyStorage() {
    return iModEnergyStorage.getMaxEnergyStored();
  }

  public int maxReceive() {
    return iModEnergyStorage.getMaxReceive();
  }

  public int maxExtract() {
    return iModEnergyStorage.getMaxExtract();
  }

  public int energyStorage() {
    return iModEnergyStorage.getEnergyStored();
  }

  public int getX() {
    return pos.getX();
  }

  public int getY() {
    return pos.getY();
  }

  public int getZ() {
    return pos.getZ();
  }
}

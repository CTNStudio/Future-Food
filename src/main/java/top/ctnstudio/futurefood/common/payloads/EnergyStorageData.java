package top.ctnstudio.futurefood.common.payloads;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import top.ctnstudio.futurefood.common.menu.EnergyMenu;
import top.ctnstudio.futurefood.core.FutureFood;

/**
 * 能源存储数据包
 */
public record EnergyStorageData(int energy, int maxEnergy) implements CustomPacketPayload {
  public static final CustomPacketPayload.Type<EnergyStorageData> TYPE = new CustomPacketPayload.Type<>(FutureFood.modRL("energy_storage_data"));

  public static final StreamCodec<ByteBuf, EnergyStorageData> STREAM_CODEC = StreamCodec.composite(
    ByteBufCodecs.INT,
    EnergyStorageData::energy,
    ByteBufCodecs.INT,
    EnergyStorageData::maxEnergy,
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
      AbstractContainerMenu menu = player.containerMenu;
      if (!(menu instanceof EnergyMenu energyMenu)) {
        return;
      }
      ContainerData energyData = energyMenu.getEnergyData();
      energyData.set(0, data.energy());
      energyData.set(1, data.maxEnergy());
    });
  }

  @Override
  public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }
}

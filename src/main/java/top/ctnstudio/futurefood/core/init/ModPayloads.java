package top.ctnstudio.futurefood.core.init;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import top.ctnstudio.futurefood.common.payloads.EnergyStorageData;
import top.ctnstudio.futurefood.core.FutureFood;

@EventBusSubscriber
public final class ModPayloads {
  @SubscribeEvent
  public static void register(final RegisterPayloadHandlersEvent event) {
    FutureFood.LOGGER.info("Registering Payloads");

    final PayloadRegistrar registrar = event.registrar("1");
    /// 接收来自服务端和客户端的数据
    //..

    /// 接收来自服务端的数据
    registrar.commonToClient(EnergyStorageData.TYPE, EnergyStorageData.STREAM_CODEC, EnergyStorageData::toClient);
    //..

    /// 接收来自客户端的数据
    //..
    FutureFood.LOGGER.info("Registering Payloads Completed");
  }
}

package top.ctnstudio.futurefood.core;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.io.IOException;

/**
 * 数据生成主类
 */
@EventBusSubscriber()
@SuppressWarnings("all")
public class FfDatagen {
  @SubscribeEvent
  public static void gatherData(final GatherDataEvent event) throws IOException {

  }

}

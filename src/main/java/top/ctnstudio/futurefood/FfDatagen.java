package top.ctnstudio.futurefood;


import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.io.File;

import static top.ctnstudio.futurefood.FutureFood.LOGGER;

/**
 * 数据生成主类
 */
@EventBusSubscriber()
public class FfDatagen {
  @SubscribeEvent
  public static void gatherData(final GatherDataEvent event) {
    LOGGER.info("Start generator data.");

    final File root = new File("../src/generated/resources/");
    if (!root.exists() || !root.isDirectory()) {
      root.mkdir();
    }

    // TODO - 用我们自己的方式处理 DataGenerator。
  }
}

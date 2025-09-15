package top.ctnstudio.futurefood;


import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

/**
 * 数据生成主类
 */
@EventBusSubscriber()
public class FfDatagen {
  @SubscribeEvent
  public static void gatherData(GatherDataEvent event) {
    // TODO - 用我们自己的方式处理 DataGenerator。

  }
}

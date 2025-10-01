package top.ctnstudio.futurefood.core;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(value = FutureFood.ID, dist = Dist.CLIENT)
@OnlyIn(Dist.CLIENT)
//@EventBusSubscriber(modid = FutureFood.ID, value = Dist.CLIENT)
public class FfClient {

  @SuppressWarnings("unused")
  public FfClient(ModContainer container) {
  }
}

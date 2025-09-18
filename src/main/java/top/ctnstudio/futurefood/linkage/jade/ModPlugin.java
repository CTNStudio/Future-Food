package top.ctnstudio.futurefood.linkage.jade;

import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;
import top.ctnstudio.futurefood.common.block.tile.BasicEnergyStorageBlockEntity;

@WailaPlugin
public class ModPlugin implements IWailaPlugin {

  @Override
  public void register(IWailaCommonRegistration registration) {
    // 从这里注册请求的数据，Jade 会自动从服务端请求数据。
    registration.registerBlockDataProvider(ModEnergyStorageComponentProvider.get(),
      BasicEnergyStorageBlockEntity.class);
  }

  @Override
  public void registerClient(IWailaClientRegistration registration) {
    // 这里放目标方块，也就是方块实体的目标方块。当玩家看到方块时，
    // Jade 会先访问方块是否符合需要从服务端调取资源，然后再提交
    // 数据到 Jade Tooltip Info。

    // registration.registerBlockComponent(ModEnergyStorageComponentProvider.get(),IEnergyStorage
    // .class);
  }
}
package top.ctnstudio.futurefood.linkage.jade;

import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;
import top.ctnstudio.futurefood.common.block.tile.QedBlockEntity;

@WailaPlugin
public class ModJadePlugin implements IWailaPlugin {

  @Override
  public void register(IWailaCommonRegistration registration) {
    registration.registerBlockDataProvider(QEDProvider.INSTANCE, QedBlockEntity.class);
  }

  @Override
  public void registerClient(IWailaClientRegistration registration) {
  }
}
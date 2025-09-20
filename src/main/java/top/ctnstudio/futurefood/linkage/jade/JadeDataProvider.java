package top.ctnstudio.futurefood.linkage.jade;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IServerDataProvider;

public class JadeDataProvider implements IServerDataProvider<BlockAccessor> {
  static final JadeDataProvider INSTANCE = new JadeDataProvider();

  @Override
  public void appendServerData(CompoundTag data, BlockAccessor accessor) {

  }

  @Override
  public ResourceLocation getUid() {
    return JadeConstants.BLOCK_DATA;
  }
}

package top.ctnstudio.futurefood.linkage.jade;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import top.ctnstudio.futurefood.common.block.tile.QedBlockEntity;

public class QEDProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
  static final QEDProvider INSTANCE = new QEDProvider();

  @Override
  public void appendServerData(CompoundTag data, BlockAccessor accessor) {
    final var qed = (QedBlockEntity) accessor.getBlockEntity();
    data.putInt("energy", qed.getEnergyStored());
  }


  @Override
  public void appendTooltip(ITooltip tooltip, BlockAccessor blockAccessor,
                            IPluginConfig iPluginConfig) {
    final var data = blockAccessor.getServerData();
    tooltip.add(Component.literal("Energy: " + data.getInt("energy")));
  }

  @Override
  public boolean shouldRequestData(BlockAccessor accessor) {
    return true;
  }

  @Override
  public ResourceLocation getUid() {
    return JadeConstants.BLOCK_DATA;
  }
}

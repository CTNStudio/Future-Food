package top.ctnstudio.futurefood.common.block.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;

public interface UnlimitedLink {
  boolean linkBlock(BlockPos pos);

  void linkFailure(BlockPos pos);

  void removeLink(BlockPos pos);

  BlockState getLinkedBlock(BlockPos pos);

  void serializeLinkedListNBT(HolderLookup.Provider provider, CompoundTag nbt);

  void deserializeLinkedListNBT(HolderLookup.Provider provider, CompoundTag nbt);
}

package top.ctnstudio.futurefood.capability;

import com.google.common.collect.Queues;
import com.google.common.collect.Sets;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.energy.IEnergyStorage;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Queue;

public abstract class UnlimitedLinkStorage implements IUnlimitedLinkStorage {
  /**
   * 链接哈希集合
   */
  private final HashSet<BlockPos> linkSet;
  private final Queue<BlockPos> cacheData;

  public UnlimitedLinkStorage() {
    this.linkSet = Sets.newHashSet();
    this.cacheData = Queues.newArrayDeque();
  }

  @Override
  public CompoundTag serializeNBT(Provider provider) {
    CompoundTag nbt = new CompoundTag();
    ListTag tags = new ListTag();
    linkSet.forEach(
      (pos) -> {
        BlockState state = getLinkedBlock(pos);
        if (state == null) {
          return;
        }
        CompoundTag tag = new CompoundTag();
        tag.putIntArray("pos", new int[]{pos.getX(), pos.getY(), pos.getZ()});
      }
    );
    nbt.put("linkList", tags);
    return nbt;
  }

  @Override
  public void deserializeNBT(Provider provider, CompoundTag nbt) {
    Tag linkList = nbt.get("linkList");
    if (!(linkList instanceof ListTag tags)) {
      linkSet.clear();
      return;
    }
    if (tags.isEmpty()) {
      return;
    }
    tags.forEach(tag -> {
      if (!(tag instanceof CompoundTag compoundTag)) {
        return;
      }
      int[] posNbtArray = compoundTag.getIntArray("pos");
      BlockPos pos = new BlockPos(posNbtArray[0], posNbtArray[1], posNbtArray[2]);
      BlockState state = getLinkedBlock(pos);
      if (state == null) {
        return;
      }
      linkSet.add(pos);
    });
  }

  @Override
  @Nullable
  public abstract Level getLevel();

  @Override
  public boolean linkBlock(Level level, BlockPos pos) {
    if (level == null || level.isClientSide) {
      linkFailure(pos);
      return false;
    }
    IEnergyStorage capability = IUnlimitedLinkStorage.getEnergyStorageCapabilities(level, pos);
    if (capability == null) {
      linkFailure(pos);
      return false;
    }
    linkSet.add(pos);
    return true;
  }

  public void addLinkCache(BlockPos pos) {
    this.cacheData.add(pos);
  }

  @Override
  public abstract void linkFailure(BlockPos pos);

  /**
   * 获取一个链接的方块
   *
   * @param pos 要获取的链接方块位置
   * @return 链接的方块
   */
  @Nullable
  public BlockState getLinkedBlock(BlockPos pos) {
    if (getLevel() == null) {
      return null;
    }
    BlockState blockState = getLevel().getBlockState(pos);
    if (blockState.isEmpty()) {
      return null;
    }
    return blockState;
  }

  @Override
  public void removeLink(BlockPos pos) {
    linkSet.remove(pos);
  }

  public HashSet<BlockPos> getLinkSet() {
    return linkSet;
  }

  public Queue<BlockPos> getCacheData() {
    return cacheData;
  }
}

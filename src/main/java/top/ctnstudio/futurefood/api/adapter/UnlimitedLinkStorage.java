package top.ctnstudio.futurefood.api.adapter;

import com.google.common.collect.ImmutableSet;
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
import top.ctnstudio.futurefood.api.capability.IUnlimitedLinkStorage;
import top.ctnstudio.futurefood.core.FutureFood;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;

/**
 * 无限连接存储
 */
public abstract class UnlimitedLinkStorage implements IUnlimitedLinkStorage {
  /**
   * 链接哈希集合
   */
  private final Set<BlockPos> linkSet;

  /**
   * 变更缓存，用于从变更事件中提交需要检查更新的坐标。
   *
   * @see top.ctnstudio.futurefood.event.ModBlockEvent
   */
  private final Queue<BlockPos> cacheData;

  public UnlimitedLinkStorage() {
    this.linkSet = Sets.newHashSet();
    this.cacheData = Queues.newArrayDeque();
  }

  /**
   * 从变更缓存向链接目标添加或删除数据。
   */
  public final void tick() {
    final Level world = this.getLevel();
    if (Objects.isNull(world) || world.isClientSide) {
      return;
    }

    Queue<BlockPos> cacheData = this.getCacheData();

    while (!cacheData.isEmpty()) {
      final BlockPos cache = cacheData.poll();
      var flag = world.getBlockEntity(cache) instanceof IEnergyStorage;
      flag = flag
        ? this.linkBlock(world, cache)
        : this.removeLink(cache);

      if (!flag) {
        FutureFood.LOGGER.warn("Something warn in block pos {} ", cache);
      }
    }
  }

  @Nonnull
  public final Queue<BlockPos> getCacheData() {
    return cacheData;
  }

  @Override
  public final boolean removeLink(BlockPos pos) {
    return linkSet.remove(pos);
  }

  @Override
  public boolean linkBlock(Level level, BlockPos pos) {
    if (level == null) {
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

  @Override
  public boolean isLink(BlockPos pos) {
    return !isContainLink(pos);
  }

  @Override
  public boolean isContainLink(BlockPos pos) {
    return linkSet.contains(pos);
  }

  @Override
  public CompoundTag serializeNBT(Provider provider) {
    CompoundTag nbt = new CompoundTag();
    ListTag tags = new ListTag();
    linkSet.forEach(
      (pos) -> {
        BlockState state = getLinkBlock(pos);
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

  /**
   * 获取一个链接的方块
   *
   * @param pos 要获取的链接方块位置
   * @return 链接的方块
   */
  @CheckForNull
  @Override
  public BlockState getLinkBlock(BlockPos pos) {
    if (Objects.isNull(this.getLevel())) {
      return null;
    }

    BlockState blockState = getLevel().getBlockState(pos);
    return !blockState.isEmpty() ? blockState : null;
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
      BlockState state = getLinkBlock(pos);
      if (state == null) {
        return;
      }
      linkSet.add(pos);
    });
  }

  @Override
  public final void addLinkCache(BlockPos pos) {
    this.cacheData.add(pos);
  }

  @Nonnull
  public final Set<BlockPos> getLinkSet() {
    return ImmutableSet.copyOf(linkSet);
  }

  @Override
  public void clear() {
    linkSet.clear();
  }

  @Override
  public boolean setLink(Level level, BlockPos oldPos, BlockPos newPos) {
    if (level == null || isContainLink(newPos) || !removeLink(oldPos)) {
      return false;
    }
    return linkBlock(level, newPos);
  }

  @Override
  public void setLinkList(Collection<BlockPos> linkList) {
    linkSet.clear();
    linkSet.addAll(linkList);
  }
}

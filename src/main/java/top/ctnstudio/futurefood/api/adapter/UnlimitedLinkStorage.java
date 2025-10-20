package top.ctnstudio.futurefood.api.adapter;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import top.ctnstudio.futurefood.api.capability.IUnlimitedLinkModify;
import top.ctnstudio.futurefood.api.capability.IUnlimitedLinkStorage;
import top.ctnstudio.futurefood.util.EnergyUtil;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static top.ctnstudio.futurefood.util.BlockUtil.getBlockPos;

/**
 * 无限连接存储
 */
// TODO - 貌似不应该把会在 Client Side 处理的 tick 方法删掉，但是安全性有待讨论。
public abstract class UnlimitedLinkStorage implements IUnlimitedLinkStorage {
  /**
   * 链接哈希集合
   */
  private final Set<BlockPos> linkSet;


  @Nullable
  protected IUnlimitedLinkModify onContentsChanged;

  public UnlimitedLinkStorage() {
    this.linkSet = Sets.newHashSet();
  }

  @Override
  public final boolean removeLink(BlockPos pos) {
    boolean is = linkSet.remove(pos);
    onChanged();
    return is;
  }

  @Override
  public boolean linkBlock(Level level, BlockPos pos) {
    if (level == null) {
      linkFailure(pos);
      return false;
    }

    if (EnergyUtil.getEnergyStorageCapabilities(level, pos) == null) {
      linkFailure(pos);
      return false;
    }

    linkSet.add(pos);
    onChanged();
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
    linkSet.forEach(pos -> {
      BlockState state = getLinkBlock(pos);
      if (state == null) {
        return;
      }
      tags.add(new IntArrayTag(new int[]{pos.getX(), pos.getY(), pos.getZ()}));
    });
    nbt.put("linkList", tags);
    return nbt;
  }

  @Override
  public void deserializeNBT(Provider provider, CompoundTag nbt) {
    Tag linkList = nbt.get("linkList");
    if (!(linkList instanceof ListTag tags) || tags.isEmpty()) {
      return;
    }
    linkSet.clear();
    linkSet.addAll(tags.stream().map(tag ->
        tag instanceof IntArrayTag intTags ? getBlockPos(intTags.getAsIntArray()) : null)
      .filter(Objects::nonNull).toList());
    onLoad();
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
    boolean is = linkBlock(level, newPos);
    onChanged();
    return is;
  }

  @Override
  public void setLinkList(Collection<BlockPos> linkList) {
    linkSet.clear();
    linkSet.addAll(linkList);
    onChanged();
  }

  @Override
  public abstract void linkFailure(BlockPos pos);

  /**
   * 获取链接的列表
   *
   * @return 不可修改的链接列表
   */
  @Override
  public List<BlockPos> getLinkPosList() {
    return linkSet.stream().toList();
  }

  @Override
  public int getSize() {
    return linkSet.size();
  }

  @Override
  public abstract @Nullable Level getLevel();

  public void setOn(IUnlimitedLinkModify onContentsChanged) {
    this.onContentsChanged = onContentsChanged;
  }

  @Nullable
  public IUnlimitedLinkModify getOnContentsChanged() {
    return onContentsChanged;
  }

  public void onChanged() {
    if (onContentsChanged != null) onContentsChanged.onLinkChanged();
  }

  public void onLoad() {
    if (onContentsChanged != null) onContentsChanged.onLinkLoad();
  }
}

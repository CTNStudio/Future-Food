package top.ctnstudio.futurefood.common.block.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import top.ctnstudio.futurefood.api.IUnlimitedLink;
import top.ctnstudio.futurefood.capability.ModEnergyStorage;
import top.ctnstudio.futurefood.core.init.ModTileEntity;

import javax.annotation.Nullable;
import java.util.LinkedHashMap;

public class QedBlockEntity extends BasicEnergyStorageBlockEntity<QedBlockEntity> implements IUnlimitedLink {

  // Fixme - 这个大概是多余的，数据传递靠记录器相对不靠谱，可能需要用 KT Tree 之类的算法
  private final LinkedHashMap<BlockPos, IEnergyStorage> linkList; // 链接列表

  public QedBlockEntity(BlockPos pos, BlockState blockState) {
    super(ModTileEntity.QED.get(), pos, blockState, new ModEnergyStorage(20480, 4096, 4096));
    linkList = new LinkedHashMap<>();
  }

  public static <T extends BlockEntity> void tick(Level level, BlockPos blockPos,
                                                  BlockState blockState, T blockEntity) {
    if (level == null) {
      return;
    }
    QedBlockEntity be = (QedBlockEntity) blockEntity;
    // 移除无效的链接
    be.linkList.entrySet().removeIf(entry -> {
      BlockState bs = be.getLinkedBlock(entry.getKey());
      if (bs == null || bs.isEmpty()) {
        return true;
      }
      BlockPos bp = entry.getKey();
      return bp == null;
    });
    be.linkList.forEach((bp, bs) -> {

    });
  }

  @Override
  protected void loadAdditional(CompoundTag nbt, HolderLookup.Provider registries) {
    super.loadAdditional(nbt, registries);
    serializeLinkedListNBT(registries, nbt);
  }

  @Override
  protected void saveAdditional(CompoundTag nbt, HolderLookup.Provider registries) {
    super.saveAdditional(nbt, registries);
    deserializeLinkedListNBT(registries, nbt);
  }

  @Override
  public boolean linkBlock(BlockPos pos) {
    if (pos == null || level == null) {
      linkFailure(pos);
      return false;
    }
    BlockState state = getLinkedBlock(pos);
    if (state == null) {
      linkFailure(pos);
      return false;
    }
    IEnergyStorage capability = getEnergyStorage(level, pos);
    if (capability == null) {
      linkFailure(pos);
      return false;
    }
    linkList.put(pos, capability);
    return true;
  }

  @Override
  public void linkFailure(BlockPos pos) {

  }

  @Override
  public void removeLink(BlockPos pos) {
    if (pos == null) {
      return;
    }
    linkList.remove(pos);
  }

  @Nullable
  @Override
  public BlockState getLinkedBlock(BlockPos pos) {
    if (level == null || pos == null) {
      return null;
    }
    BlockState blockState = level.getBlockState(pos);
    if (blockState == null || blockState.isEmpty()) {
      return null;
    }
    return blockState;
  }

  @Override
  public void serializeLinkedListNBT(HolderLookup.Provider provider, CompoundTag nbt) {
    if (nbt == null || level != null) {
      return;
    }
    ListTag tags = new ListTag();
    linkList.forEach(
      (pos, es) -> {
        if (pos == null || es == null) {
          return;
        }
        BlockState state = getLinkedBlock(pos);
        if (state == null) {
          return;
        }
        CompoundTag tag = new CompoundTag();
        tag.putIntArray("pos", new int[]{pos.getX(), pos.getY(), pos.getZ()});
      }
    );
    nbt.put("linkList", tags);
  }

  @Override
  public void deserializeLinkedListNBT(HolderLookup.Provider provider, CompoundTag nbt) {
    if (level == null || nbt.isEmpty()) {
      return;
    }
    ListTag tags = nbt.getList("linkList", 10);
    if (tags.isEmpty()) {
      return;
    }
    tags.forEach(tag -> {
      if (!(tag instanceof CompoundTag compoundTag)) {
        return;
      }
      int[] pos = compoundTag.getIntArray("pos");
      BlockPos blockPos = new BlockPos(pos[0], pos[1], pos[2]);
      BlockState state = getLinkedBlock(blockPos);
      if (state == null) {
        return;
      }
      IEnergyStorage capability = getEnergyStorage(level, blockPos);
      if (capability == null) {
        return;
      }
      linkList.put(blockPos, capability);
    });
  }

  /**
   * 验证方块是否包含能获取能量的 capability
   *
   * @return 是否包含能获取能量的 capability
   */
  public static boolean verifyEnergyStorage(Level level, BlockPos pos) {
    if (level == null || pos == null) {
      return false;
    }
    return getEnergyStorage(level, pos) != null;
  }

  public static IEnergyStorage getEnergyStorage(Level level, BlockPos pos) {
    IEnergyStorage capability = level.getCapability(Capabilities.EnergyStorage.BLOCK, pos, null);
    if (capability != null) {
      return capability;
    }
    for (Direction direction : Direction.values()) {
      IEnergyStorage capability1 = level.getCapability(Capabilities.EnergyStorage.BLOCK, pos,
        direction);
      if (capability1 != null) {
        return capability1;
      }
    }
    return null;
  }
}

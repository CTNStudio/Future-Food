package top.ctnstudio.futurefood.common.block.tile;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Queues;
import com.google.common.collect.Sets;
import com.google.common.collect.Table;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.energy.IEnergyStorage;
import top.ctnstudio.futurefood.api.tile.IUnlimitedLink;
import top.ctnstudio.futurefood.capability.ModEnergyStorage;
import top.ctnstudio.futurefood.core.FutureFood;
import top.ctnstudio.futurefood.core.init.ModTileEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Queue;

import static top.ctnstudio.futurefood.core.init.ModCapability.getOppositeDirection;

public class QedBlockEntity extends EnergyStorageBlockEntity implements IUnlimitedLink {
  public static final Table<Integer, Integer, Integer> CACHES = HashBasedTable.create();
  public static final int DEFAULT_MAX_REMAINING_TIME = 5;
  public static final String GUI_NAME = FutureFood.ID + ".quantum_energy_diffuser.gui.name";
  private static final MutableComponent DISPLAY_NAME = Component.translatable(GUI_NAME);
  /**
   * 剩余传递计时
   */
  private int remainingTime;
  /**
   * 最大传递计时
   */
  private int maxRemainingTime; // 使用变量以方便后续做升级
  /**
   * 链接哈希集合
   */
  private final HashSet<BlockPos> linkSet;
  private final Queue<BlockPos> cacheData = Queues.newArrayDeque();

  public QedBlockEntity(BlockEntityType<? extends QedBlockEntity> type, BlockPos pos,
                        BlockState blockState, ModEnergyStorage energyStorage,
                        int maxRemainingTime) {
    super(type, pos, blockState, energyStorage);
    this.linkSet = Sets.newHashSet();
    this.maxRemainingTime = maxRemainingTime;
  }

  public QedBlockEntity(BlockPos pos, BlockState blockState) {
    this(ModTileEntity.QED.get(), pos, blockState,
      new ModEnergyStorage(20480, 4096, 4096),
      DEFAULT_MAX_REMAINING_TIME);

    CACHES.put(pos.getY(), pos.getX(), pos.getZ());
  }

  @Override
  public Component getDisplayName() {
    return DISPLAY_NAME;
  }

  public static void tick(@Nonnull Level level, @Nonnull BlockPos pos, @Nonnull BlockState bs,
                          @Nonnull QedBlockEntity tile) {
    if (!tile.cacheData.isEmpty()) {
      for (BlockPos cache : tile.cacheData) {
        if (level.getBlockEntity(cache) instanceof IEnergyStorage) {
          tile.linkBlock(level, cache);
        } else {
          tile.removeLink(pos);
        }
      }
    }

    int time = tile.getRemainingTime(); // 使用方法方便重写逻辑
    if (time > 0) {
      return;
    } else {
      tile.resetRemainingTime();
    }
    tile.executeEnergyTransmission(level, pos, bs);
    tile.remainingTime--;
  }

  /**
   * 执行能量传递
   *
   * @param blockLevel 方块的世界
   * @param pos        当前方块的位置
   * @param bs         当方块的方块状态
   */
  public void executeEnergyTransmission(Level blockLevel, BlockPos pos, BlockState bs) {
    linkSet.forEach((bp) -> {
      IEnergyStorage capability = IUnlimitedLink.getEnergyStorageCapabilities(blockLevel, pos);
      if (capability == null) {
        return;
      }
      int maxReceive = energyStorage.getMaxReceive();
      if (capability.receiveEnergy(maxReceive, true) > 0) {
        capability.receiveEnergy(maxReceive, false);
      }
    });
  }

  @Override
  protected void loadAdditional(CompoundTag nbt, HolderLookup.Provider provider) {
    super.loadAdditional(nbt, provider);
    serializeLinkedListNBT(provider, nbt);
    remainingTime = nbt.getInt("remainingTime");
    if (!nbt.contains("maxRemainingTime")) {
      maxRemainingTime = DEFAULT_MAX_REMAINING_TIME;
    } else {
      maxRemainingTime = nbt.getInt("maxRemainingTime");
    }

    final var pos = this.getBlockPos();
    CACHES.put(pos.getY(), pos.getX(), pos.getZ());
  }

  @Override
  protected void saveAdditional(CompoundTag nbt, HolderLookup.Provider provider) {
    super.saveAdditional(nbt, provider);
    deserializeLinkedListNBT(provider, nbt);
    nbt.putInt("remainingTime", remainingTime);
    nbt.putInt("maxRemainingTime", maxRemainingTime);
  }

  @Override
  public boolean linkBlock(Level level, BlockPos pos) {
    if (level == null || this.level == null || this.level.isClientSide || this.level == level) {
      linkFailure(pos);
      return false;
    }
    IEnergyStorage capability = IUnlimitedLink.getEnergyStorageCapabilities(this.level, pos);
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
  public void linkFailure(BlockPos pos) {

  }

  @Override
  public void removeLink(BlockPos pos) {
    linkSet.remove(pos);
  }

  @Nullable
  @Override
  public BlockState getLinkedBlock(BlockPos pos) {
    if (level == null) {
      return null;
    }
    BlockState blockState = level.getBlockState(pos);
    if (blockState.isEmpty()) {
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
  }

  @Override
  public void deserializeLinkedListNBT(HolderLookup.Provider provider, CompoundTag nbt) {
    if (level == null || nbt.isEmpty()) {
      return;
    }
    ListTag tags = nbt.getList("linkList", DEFAULT_MAX_REMAINING_TIME);
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

  public int getRemainingTime() {
    return remainingTime;
  }

  /**
   * 重置传递时间
   */
  public void resetRemainingTime() {
    remainingTime = maxRemainingTime;
  }

  public int getMaxRemainingTime() {
    return maxRemainingTime;
  }

  @Override
  public IEnergyStorage externalGetEnergyStorage(@Nullable Direction direction) {
    if (direction == null) {
      return energyStorage;
    }
    return !getOppositeDirection(this, direction) ? null :
      super.externalGetEnergyStorage(direction);
  }

  @Override
  public void onLoad() {
    final var pos = this.getBlockPos();
    CACHES.put(pos.getY(), pos.getX(), pos.getZ());
  }

  @Override
  public void onChunkUnloaded() {
    final var pos = this.getBlockPos();
    CACHES.remove(pos.getY(), pos.getX());
  }
}

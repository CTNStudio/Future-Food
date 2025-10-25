package top.ctnstudio.futurefood.common.block.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.ctnstudio.futurefood.api.adapter.ModEnergyStorage;
import top.ctnstudio.futurefood.api.adapter.ModItemStackHandler;
import top.ctnstudio.futurefood.common.block.GluttonyEntityBlock;
import top.ctnstudio.futurefood.common.menu.GluttonyMenu;
import top.ctnstudio.futurefood.core.init.ModTileEntity;
import top.ctnstudio.futurefood.core.recipe.GluttonyRecipe;
import top.ctnstudio.futurefood.core.recipe_manager.GluttonyRecipeManager;
import top.ctnstudio.futurefood.util.EnergyUtil;

// TODO 添加配置功能
// TODO 根据方向调整物品抽入
public class GluttonyBlockEntity extends BaseEnergyStorageBlockEntity<GluttonyMenu> {
  public static final int MAX_WORK_TICK = 20 * 5;
  private int remainingTick;
  private int maxWorkTick;
  private final ContainerData workProgress;
  // 缓存
  private GluttonyRecipe recipe;

  public GluttonyBlockEntity(BlockPos pos, BlockState blockState) {
    super(ModTileEntity.GLUTTONY.get(), pos, blockState,
      new ModItemStackHandler(4), new ModEnergyStorage(81920, 0, 81920));
    this.workProgress = new Data(this);
  }

  @Override
  public void tick(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState bs) {
    if (level.isClientSide()) {
      return;
    }

    controlItemEnergy(itemHandler, true);

    outputEnergy(level, pos);
    working(level, pos);

    BlockState blockState = level.getBlockState(pos);
    if (isWorking() && !blockState.getValue(GluttonyEntityBlock.ACTIVATE)) {
      level.setBlockAndUpdate(pos, blockState.setValue(GluttonyEntityBlock.ACTIVATE, true));
    } else if (!isWorking() && blockState.getValue(GluttonyEntityBlock.ACTIVATE)) {
      level.setBlockAndUpdate(pos, blockState.setValue(GluttonyEntityBlock.ACTIVATE, false));
    }
  }

  // TODO 注意：未做优化 理论要根据方块更新进行缓存再传输，以避免过多的获取
  protected void outputEnergy(@NotNull Level level, @NotNull BlockPos pos) {
    if (energyStorage.getEnergyStored() > 0 && energyStorage.canExtract()) {
      EnergyUtil.getSurroundingEnergyStorage(level, pos).values().stream()
        .filter(e -> e.getValue().isPresent())
        .map(e -> e.getValue().get())
        .forEach(energyStorage -> EnergyUtil.controlEnergy(this.energyStorage, energyStorage));
    }
  }

  /**
   * 大部分都是没必要的在修改输入槽的那一刻就以及决定好了
   * <br>
   * 除非有人直接修改nbt :(
   */
  private void working(@NotNull Level level, @NotNull BlockPos pos) {
    if (!isWorking()) {
      return;
    }

    if (remainingTick > 0) {
      remainingTick--;
      return;
    }

    // 此次是避免直接修改工作进度导致配方为空
    // 获取缓存配方
    if (recipe == null) {
      final var stackInSlot = itemHandler.getStackInSlot(1).copy();
      if (!stackInSlot.isEmpty()) {
        produceProducts(stackInSlot);
        if (recipe == null) {
          resetProducts();
          resetProgress();
          return;
        }
      } else {
        resetProducts();
        resetProgress();
        return;
      }
    }

    // 存储值避免移除输入槽导致值为空
    ItemStack outputItem = recipe.outputItem().copy();
    int outputEnergy = recipe.outputEnergy();
    // 如果输入槽为空，则重置进度
    if (itemHandler.extractItem(1, 1, true).isEmpty()) {
      resetProducts();
      resetProgress();
      return;
    }

    // 输出槽不为当前配方成品
    ItemStack stackInSlot = itemHandler.getStackInSlot(2).copy();
    if (!stackInSlot.isEmpty() && !ItemStack.isSameItemSameComponents(stackInSlot, outputItem)) {
      // 保留当前进度
      return;
    }

    // 输出槽已满继续
    if (!itemHandler.insertItem(2, outputItem, true).isEmpty()) {
      // 保留当前进度
      return;
    }

    if (!itemHandler.getStackInSlot(1).copy().isEmpty()) {
      // 重置进度
      remainingTick = maxWorkTick;
    } else {
      // 重置整体进度
      resetProducts();
      resetProgress();
    }

    // 获取产物
    itemHandler.extractItem(1, 1, false);
    itemHandler.insertItem(2, outputItem, false);

    // 避免满了还继续修改
    if (energyStorage.getEnergyStored() >= energyStorage.getMaxEnergyStored()) {
      return;
    }

    final var energy = Mth.clamp(energyStorage.getEnergyStored() + outputEnergy,
      0,
      energyStorage.getMaxEnergyStored());

    energyStorage.setEnergy(energy);
    outputEnergy(level, pos);
  }

  public void resetProgress() {
    maxWorkTick = 0;
    remainingTick = 0;
  }

  public void resetProducts() {
    recipe = null;
  }

  @Override
  public void onItemChanged(int slot) {
    super.onItemChanged(slot);
    if (slot != 1) {
      return;
    }
    produceProducts(itemHandler.getStackInSlot(slot));
  }

  @Override
  public void onItemLoad() {
    super.onItemLoad();
    produceProducts(itemHandler.getStackInSlot(1));
  }

  /**
   * 获取产物
   *
   * @param item 输入
   */
  protected void produceProducts(ItemStack item) {
    if (item.isEmpty() || !item.has(DataComponents.FOOD)) {
      resetProducts();
      resetProgress();
      return;
    }
    if (recipe != null
      && ItemStack.isSameItem(item, recipe.inputItem())
      && item.get(DataComponents.FOOD).equals(recipe.inputItem().get(DataComponents.FOOD))) {
      return;
    }

    maxWorkTick = MAX_WORK_TICK;
    remainingTick = maxWorkTick;
    recipe = GluttonyRecipeManager.findRecipe(item);
  }

  public boolean isWorking() {
    return maxWorkTick > 0;
  }

  /**
   * 加载数据
   */
  @Override
  protected void loadAdditional(CompoundTag nbt, HolderLookup.Provider provider) {
    super.loadAdditional(nbt, provider);
    if (nbt.contains("remainingTick")) remainingTick = nbt.getInt("remainingTick");
    if (nbt.contains("maxWorkTick")) maxWorkTick = nbt.getInt("maxWorkTick");
  }

  /**
   * 保存数据
   */
  @Override
  protected void saveAdditional(CompoundTag nbt, HolderLookup.Provider provider) {
    super.saveAdditional(nbt, provider);
    nbt.putInt("remainingTick", remainingTick);
    nbt.putInt("maxWorkTick", maxWorkTick);
  }

  @Override
  public @Nullable GluttonyMenu createMenu(int containerId, Inventory playerInventory, Player player) {
    if (!player.isAlive()) {
      return null;
    }
    return new GluttonyMenu(containerId, playerInventory, itemHandler, energyData, workProgress);
  }

  @Override
  public int[] getSlotsForFace(Direction direction) {
    if (direction == Direction.DOWN) {
      return new int[] { 0 };
    } else if (direction == Direction.UP) {
      return new int[] { 1 };
    }

    return new int[] { 2 };
  }

  @Override
  public boolean canPlaceItemThroughFace(int i, ItemStack itemStack, @Nullable Direction direction) {
    // TODO true 改成检查 ItemStack 可以存储电力。
    return i != 2 && (direction == Direction.UP || true);
  }

  @Override
  public boolean canTakeItemThroughFace(int i, ItemStack itemStack, Direction direction) {
    return direction == Direction.DOWN && i == 2;
  }

  @Override
  public int getContainerSize() {
    return this.itemHandler.getSlots();
  }

  @Override
  public boolean isEmpty() {
    for(int i = 0; i < this.itemHandler.getSlots(); i++) {
      if (!this.itemHandler.getStackInSlot(i).isEmpty()) {
        return false;
      }
    }

    return true;
  }

  @Override
  public ItemStack getItem(int i) {
    return this.itemHandler.getStackInSlot(i);
  }

  @Override
  public ItemStack removeItem(int i, int i1) {
    return this.itemHandler.extractItem(i, i1, false);
  }

  @Override
  public ItemStack removeItemNoUpdate(int i) {
    return this.itemHandler.extractItem(i, i, false);
  }

  @Override
  public void setItem(int i, ItemStack itemStack) {
    this.itemHandler.setStackInSlot(i, itemStack);
  }

  @Override
  public void clearContent() {
    for(int i = 0; i < this.itemHandler.getSlots(); i++) {
      this.itemHandler.setStackInSlot(i, ItemStack.EMPTY);
    }
  }

  public record Data(GluttonyBlockEntity blockEntity) implements ContainerData {
    @Override
    public int get(int index) {
      return switch (index) {
        case 0 -> blockEntity.remainingTick;
        case 1 -> blockEntity.maxWorkTick;
        default -> 0;
      };
    }

    @Override
    public void set(int index, int value) {
      switch (index) {
        case 0 -> blockEntity.remainingTick = value;
        case 1 -> blockEntity.maxWorkTick = value;
      }
    }

    @Override
    public int getCount() {
      return 2;
    }
  }
}

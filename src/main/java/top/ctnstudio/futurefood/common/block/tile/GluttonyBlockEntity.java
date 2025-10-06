package top.ctnstudio.futurefood.common.block.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
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
import top.ctnstudio.futurefood.core.init.ModItem;
import top.ctnstudio.futurefood.core.init.ModTileEntity;
import top.ctnstudio.futurefood.util.EnergyUtil;

import java.util.Objects;

// TODO 添加配置功能
// TODO 让外部无法输入能源
// TODO 根据方向调整物品抽入
public class GluttonyBlockEntity extends BaseEnergyStorageBlockEntity<GluttonyMenu> {
  private int remainingTick;
  private int maxWorkTick;
  private final ContainerData workProgress;
  // 缓存
  private RecipeEntry cacheRecipeEntry;

  public GluttonyBlockEntity(BlockPos pos, BlockState blockState) {
    super(ModTileEntity.GLUTTONY.get(), pos, blockState,
      new ModItemStackHandler(4), new ModEnergyStorage(81920));
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

  protected void outputEnergy(@NotNull Level level, @NotNull BlockPos pos) {
    // TODO 注意：未做优化 理论要根据方块更新进行缓存再传输，以避免过多的获取
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
    if (cacheRecipeEntry == null) {
      ItemStack stackInSlot = itemHandler.getStackInSlot(1).copy();
      if (!stackInSlot.isEmpty()) {
        produceProducts(stackInSlot);
        if (cacheRecipeEntry == null) {
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
    ItemStack outputItem = cacheRecipeEntry.outputItem.copy();
    int outputEnergy = cacheRecipeEntry.outputEnergy;
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
    if (!energyStorage.canReceive() || energyStorage.getEnergyStored() >= energyStorage.getMaxEnergyStored() ||
      energyStorage.receiveEnergy(outputEnergy, true) <= 0) {
      return;
    }

    energyStorage.receiveEnergy(outputEnergy, false);
    outputEnergy(level, pos);
  }

  public void resetProgress() {
    maxWorkTick = 0;
    remainingTick = 0;
  }

  public void resetProducts() {
    cacheRecipeEntry = null;
  }

  @Override
  public void onStackContentsChanged(int slot) {
    super.onStackContentsChanged(slot);
    if (slot != 1) {
      return;
    }
    produceProducts(itemHandler.getStackInSlot(slot));
  }

  @Override
  public void onStackLoad() {
    super.onStackLoad();
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
    if (cacheRecipeEntry != null && ItemStack.isSameItem(item, cacheRecipeEntry.inputItem) &&
      item.get(DataComponents.FOOD).equals(cacheRecipeEntry.inputItem.get(DataComponents.FOOD))) {
      return;
    }

    maxWorkTick = 20 * 5;
    remainingTick = maxWorkTick;
    cacheRecipeEntry = getProduct(item);
  }

  /**
   * 计算产物
   *
   * @return 产物
   */
  protected RecipeEntry getProduct(ItemStack item) {
    FoodProperties food = item.get(DataComponents.FOOD);
    int nutritionFactor = food.nutrition();
    int saturationFactor = (int) (food.saturation() * 5);
    int effectFactor = food.effects().stream()
      .map(e -> e.probability() >= 1 ? e.effect() : null)
      .filter(Objects::nonNull)
      .mapToInt(e -> (int) (Math.max(1, (e.getDuration() / 20.0)) * e.getAmplifier() + 1)).sum();

    ItemStack outputItem = ModItem.FOOD_ESSENCE.get().getDefaultInstance();
    int count = Math.max(1, Math.min(outputItem.getMaxStackSize(), (int) (((nutritionFactor * saturationFactor) / 3.5f + effectFactor / 5.0f) / 10.0f)));
    outputItem.setCount(count);
    int outputEnergy = ((nutritionFactor * saturationFactor) * 5 + effectFactor * 10) * 2;
    return new RecipeEntry(item, outputEnergy, outputItem);
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

  public record RecipeEntry(ItemStack inputItem, int outputEnergy, ItemStack outputItem) {
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

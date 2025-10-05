package top.ctnstudio.futurefood.common.block.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;
import top.ctnstudio.futurefood.api.adapter.ModEnergyStorage;
import top.ctnstudio.futurefood.api.adapter.ModItemStackHandler;
import top.ctnstudio.futurefood.api.block.IUnlimitedEntityReceive;
import top.ctnstudio.futurefood.api.recipe.ParticleColliderRecipe;
import top.ctnstudio.futurefood.api.recipe.ParticleColliderRecipeManager;
import top.ctnstudio.futurefood.common.menu.ParticleColliderMenu;
import top.ctnstudio.futurefood.core.init.ModTileEntity;

import java.util.Optional;

// TODO 自定义配方
// TODO 让外部无法提取能源
// TODO 根据方向调整物品抽入
public class ParticleColliderBlockEntity extends EnergyStorageBlockEntity<ParticleColliderMenu>
  implements GeoBlockEntity, IUnlimitedEntityReceive {
  protected static final RawAnimation DEPLOY_ANIM = RawAnimation.begin();
  private int remainingTick;
  private int maxWorkTick;
  private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

  // 槽位
  public static final int INPUT_SLOT_1 = 1;
  public static final int INPUT_SLOT_2 = 2;
  public static final int OUTPUT_SLOT = 3;
  // 使用包装类以实现同步
  private final ContainerData workProgress;

  public ParticleColliderBlockEntity(BlockPos pos, BlockState blockState) {
    super(ModTileEntity.PARTICLE_COLLIDER.get(), pos, blockState, new ModItemStackHandler(4),
      new ModEnergyStorage(102400, 102400, 0));
    this.workProgress = new Data(this);
  }

  @Override
  public void tick(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState bs) {
    if (level.isClientSide) {
      return;
    }
    controlItemEnergy(itemHandler, false);
    this.processRecipe();
  }

  @Override
  public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
    controllers.add(new AnimationController<>(this, this::deployAnimController));
  }

  protected <E extends ParticleColliderBlockEntity & GeoAnimatable> PlayState deployAnimController(final AnimationState<E> state) {
    return state.setAndContinue(DEPLOY_ANIM);
  }

  @Override
  public AnimatableInstanceCache getAnimatableInstanceCache() {
    return cache;
  }

  @Override
  public IEnergyStorage externalGetEnergyStorage(@Nullable Direction direction) {
    return super.externalGetEnergyStorage(direction);
  }

  /**
   * 能量存储
   */
  @Override
  public IEnergyStorage getEnergyStorage() {
    return energyStorage;
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
  public @Nullable ParticleColliderMenu createMenu(int containerId, Inventory playerInventory, Player player) {
    if (!player.isAlive()) {
      return null;
    }
    return new ParticleColliderMenu(containerId, playerInventory, itemHandler, energyData, workProgress);
  }

  public record Data(ParticleColliderBlockEntity blockEntity) implements ContainerData {
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

  private void processRecipe() {
    ItemStack input1 = itemHandler.getStackInSlot(INPUT_SLOT_1);
    ItemStack input2 = itemHandler.getStackInSlot(INPUT_SLOT_2);

    Optional<ParticleColliderRecipe> recipe = ParticleColliderRecipeManager.findRecipe(input1, input2);

    if (recipe.isEmpty()) {
      remainingTick = 0;
      return;
    }

    ParticleColliderRecipe currentRecipe = recipe.get();
    if (!canCraft(currentRecipe)) {
      remainingTick = 0;
      return;
    }

    maxWorkTick = currentRecipe.getProcessingTime();
    int energyPerTick = currentRecipe.getEnergyPerTick();

    if (energyStorage.getEnergyStored() < energyPerTick) {
      return;
    }

    energyStorage.extractEnergy(energyPerTick, false);
    remainingTick++;
    if (this.remainingTick >= this.maxWorkTick) {
      craftItem(currentRecipe);
      remainingTick = 0;
    }
    setChanged();
  }

  private boolean canCraft(ParticleColliderRecipe recipe) {
    if (recipe == null) return false;
    ItemStack output = itemHandler.getStackInSlot(OUTPUT_SLOT);
    ItemStack result = recipe.getOutput();

    if (output.isEmpty()) return true;
    if (!ItemStack.isSameItemSameComponents(output, result)) return false;
    return output.getCount() + result.getCount() <= output.getMaxStackSize();
  }

  private void craftItem(ParticleColliderRecipe recipe) {
    if (!canCraft(recipe)) return;
    ItemStack result = recipe.getOutput();
    ItemStack output = itemHandler.getStackInSlot(OUTPUT_SLOT);

    if (output.isEmpty()) {
      itemHandler.setStackInSlot(OUTPUT_SLOT, result);
    } else {
      output.grow(result.getCount());
    }

    itemHandler.extractItem(INPUT_SLOT_1, 1, false);
    itemHandler.extractItem(INPUT_SLOT_2, 1, false);

    remainingTick = 0;
  }
}

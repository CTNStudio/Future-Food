package top.ctnstudio.futurefood.common.block.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;
import top.ctnstudio.futurefood.api.adapter.ModEnergyStorage;
import top.ctnstudio.futurefood.api.block.IUnlimitedEntityReceive;
import top.ctnstudio.futurefood.common.menu.ParticleColliderMenu;
import top.ctnstudio.futurefood.core.init.ModTileEntity;

// TODO 自定义配方
public class ParticleColliderBlockEntity extends EnergyStorageBlockEntity<ParticleColliderMenu>
  implements GeoBlockEntity, IUnlimitedEntityReceive {
  protected static final RawAnimation DEPLOY_ANIM = RawAnimation.begin();

  private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

  private final WorkTick workTick;
  // 使用包装类以实现同步
  private final WorkProgress workProgress;

  public ParticleColliderBlockEntity(BlockPos pos, BlockState blockState) {
    super(ModTileEntity.PARTICLE_COLLIDER.get(), pos, blockState, new ItemStackHandler(4), new ModEnergyStorage(102400));
    this.workTick = new WorkTick();
    this.workProgress = new WorkProgress(this.workTick);
  }

  @Override
  public void tick(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState bs) {
    if (level.isClientSide) {
      return;
    }
    controlItemEnergy(itemHandler, false);
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
    if (nbt.contains("remainingTick")) setRemainingTick(nbt.getInt("remainingTick"));
    if (nbt.contains("maxWorkTick")) setMaxWorkTick(nbt.getInt("maxWorkTick"));
  }

  /**
   * 保存数据
   */
  @Override
  protected void saveAdditional(CompoundTag nbt, HolderLookup.Provider provider) {
    super.saveAdditional(nbt, provider);
    nbt.putInt("remainingTick", getRemainingTick());
    nbt.putInt("maxWorkTick", getMaxWorkTick());
  }

  public int getRemainingTick() {
    return workTick.getRemainingTick();
  }

  public void setRemainingTick(int remainingTick) {
    this.workTick.setMaxWorkTick(remainingTick);
  }

  public int getMaxWorkTick() {
    return workTick.getMaxWorkTick();
  }

  public void setMaxWorkTick(int maxTick) {
    this.workTick.setMaxWorkTick(maxTick);
  }

  @Override
  public @Nullable ParticleColliderMenu createMenu(int containerId, Inventory playerInventory, Player player) {
    if (!player.isAlive()) {
      return null;
    }
    return new ParticleColliderMenu(containerId, playerInventory, itemHandler, energyData, workProgress);
  }

  public record WorkProgress(WorkTick workTick) implements ContainerData {
    @Override
    public int get(int index) {
      return switch (index) {
        case 0 -> workTick.getRemainingTick();
        case 1 -> workTick.getMaxWorkTick();
        default -> 0;
      };
    }

    @Override
    public void set(int index, int value) {
      switch (index) {
        case 0 -> workTick.setRemainingTick(value);
        case 1 -> workTick.setMaxWorkTick(value);
      }
    }

    @Override
    public int getCount() {
      return 2;
    }
  }

  public static class WorkTick {
    private int remainingTick;
    private int maxWorkTick;

    public int getRemainingTick() {
      return remainingTick;
    }

    public void setRemainingTick(int remainingTick) {
      this.remainingTick = remainingTick;
    }

    public int getMaxWorkTick() {
      return maxWorkTick;
    }

    public void setMaxWorkTick(int maxWorkTick) {
      this.maxWorkTick = maxWorkTick;
    }
  }
}

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
import top.ctnstudio.futurefood.common.menu.ParticleColliderMenu;
import top.ctnstudio.futurefood.core.init.ModTileEntity;

// TODO 自定义配方
public class ParticleColliderBlockEntity extends EnergyStorageBlockEntity<ParticleColliderMenu>
  implements GeoBlockEntity, IUnlimitedEntityReceive {
  protected static final RawAnimation DEPLOY_ANIM = RawAnimation.begin();
  private int remainingTick;
  private int maxWorkTick;
  private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

  // 使用包装类以实现同步
  private final ContainerData workProgress;

  public ParticleColliderBlockEntity(BlockPos pos, BlockState blockState) {
    super(ModTileEntity.PARTICLE_COLLIDER.get(), pos, blockState, new ModItemStackHandler(4), new ModEnergyStorage(102400));
    this.workProgress = new Data(this);
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
}

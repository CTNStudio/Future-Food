package top.ctnstudio.futurefood.common.block.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;
import top.ctnstudio.futurefood.api.tile.IUnlimitedEntityReceive;
import top.ctnstudio.futurefood.capability.ModEnergyStorage;
import top.ctnstudio.futurefood.core.init.ModTileEntity;

public class ParticleColliderBlockEntity extends BasicEnergyStorageBlockEntity implements GeoBlockEntity, IUnlimitedEntityReceive {
  protected static final RawAnimation DEPLOY_ANIM = RawAnimation.begin();

  private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

  public ParticleColliderBlockEntity(BlockPos pos, BlockState blockState) {
    super(ModTileEntity.PARTICLE_COLLIDER.get(), pos, blockState, new ModEnergyStorage(102400,
      102400, 0));
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
  public double getTick(Object object) {
    return 0;
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
}
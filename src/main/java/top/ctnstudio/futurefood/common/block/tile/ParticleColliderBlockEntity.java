package top.ctnstudio.futurefood.common.block.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;
import top.ctnstudio.futurefood.core.init.ModTileEntity;

public class ParticleColliderBlockEntity extends BlockEntity implements GeoBlockEntity {
  protected static final RawAnimation DEPLOY_ANIM = RawAnimation.begin();

  private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

  public ParticleColliderBlockEntity(BlockPos pos, BlockState blockState) {
    super(ModTileEntity.PARTICLE_COLLIDER.get(), pos, blockState);
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
}
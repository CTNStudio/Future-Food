package top.ctnstudio.futurefood.common.block;

import com.mojang.serialization.MapCodec;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.common.extensions.ILevelExtension;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.ctnstudio.futurefood.api.capability.IModEnergyStorage;
import top.ctnstudio.futurefood.common.block.tile.BatteryBlockEntity;
import top.ctnstudio.futurefood.common.block.tile.ModBlockEntity;
import top.ctnstudio.futurefood.core.init.ModBlock;
import top.ctnstudio.futurefood.core.init.ModDataComponent;
import top.ctnstudio.futurefood.core.init.ModTileEntity;
import top.ctnstudio.futurefood.util.EnergyUtil;

import java.util.List;

public class BatteryEntityBlock extends HorizontalDirectionalEntityBlock<BatteryBlockEntity> {
  private final boolean isInfinite;
  private static final MapCodec<BatteryEntityBlock> INFINITE_CODEC = simpleCodec(properties1 -> new BatteryEntityBlock(properties1, true));
  private static final MapCodec<BatteryEntityBlock> CODEC = simpleCodec(properties1 -> new BatteryEntityBlock(properties1, false));

  public BatteryEntityBlock() {
    this(false);
  }

  public BatteryEntityBlock(boolean isInfinite) {
    this(BlockBehaviour.Properties.of()
        .strength(0.4f)
        .lightLevel(state -> {
          if (state.getBlock() instanceof BatteryEntityBlock battery && battery.isInfinite) {
            return 15;
          } else {
            return 12;
          }
        })
      , isInfinite);
  }

  protected BatteryEntityBlock(Properties properties, boolean isInfinite) {
    super(properties
      .noOcclusion()
      .isValidSpawn(ModBlock.argumentNever())
      .isRedstoneConductor(ModBlock.never())
      .isSuffocating(ModBlock.never())
      .isViewBlocking(ModBlock.never()), ModTileEntity.BATTERY);
    this.isInfinite = isInfinite;
  }

  @Override
  protected @NotNull InteractionResult useWithoutItem(@NotNull BlockState state,
                                                      Level level,
                                                      @NotNull BlockPos pos,
                                                      @NotNull Player player,
                                                      @NotNull BlockHitResult hitResult) {
    if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
      serverPlayer.openMenu(state.getMenuProvider(level, pos));
    }
    return InteractionResult.sidedSuccess(level.isClientSide);
  }

  @Override
  protected @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
    return RenderShape.MODEL;
  }

  @Override
  protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
    return isInfinite ? INFINITE_CODEC : CODEC;
  }

  @Override
  public @Nullable BatteryBlockEntity newBlockEntity(@NotNull BlockPos pos,
                                                     @NotNull BlockState state) {
    return new BatteryBlockEntity(pos, state, isInfinite);
  }

  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level,
                                                                @NotNull BlockState state,
                                                                @NotNull BlockEntityType<T> type) {
    return createTickerHelper(type, ModTileEntity.BATTERY.get(), ModBlockEntity::tick);
  }

  @Override
  public @NotNull ItemStack getCloneItemStack(@NotNull BlockState state,
                                              @NotNull HitResult target,
                                              @NotNull LevelReader reader,
                                              @NotNull BlockPos pos,
                                              @NotNull Player player) {
    var stack = super.getCloneItemStack(state, target, reader, pos, player);
    if (!(reader instanceof ILevelExtension level) ||
      !stack.has(ModDataComponent.ENERGY_STORAGE) ||
      !(stack.getCapability(Capabilities.EnergyStorage.ITEM) instanceof IModEnergyStorage itemCapability) ||
      !(level.getCapability(Capabilities.EnergyStorage.BLOCK, pos, null) instanceof IModEnergyStorage blockCapability)) {
      return stack;
    }
    EnergyUtil.copyEnergy(itemCapability, blockCapability);
    return stack;
  }

  @Override
  protected @NotNull List<ItemStack> getDrops(@NotNull BlockState state, LootParams.Builder params) {
    if (!(params.getOptionalParameter(LootContextParams.BLOCK_ENTITY) instanceof BatteryBlockEntity batteryBlockEntity)) {
      return super.getDrops(state, params);
    }
    ObjectArrayList<ItemStack> objectarraylist = new ObjectArrayList<>();
    ItemStack itemStack = new ItemStack(this);
    //noinspection DataFlowIssue
    EnergyUtil.copyEnergy(IModEnergyStorage.of(itemStack.getCapability(Capabilities.EnergyStorage.ITEM)), IModEnergyStorage.of(batteryBlockEntity.externalGetEnergyStorage(null)));

    objectarraylist.add(itemStack);
    return objectarraylist;
  }

  @Override
  protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
    level.invalidateCapabilities(pos);
    super.onPlace(state, level, pos, oldState, movedByPiston);
  }

  public boolean isInfinite() {
    return isInfinite;
  }
}

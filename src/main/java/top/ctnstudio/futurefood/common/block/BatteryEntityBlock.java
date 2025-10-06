package top.ctnstudio.futurefood.common.block;

import com.mojang.serialization.MapCodec;
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
import top.ctnstudio.futurefood.common.item.data_component.ItemEnergyStorageData;
import top.ctnstudio.futurefood.core.init.ModBlock;
import top.ctnstudio.futurefood.core.init.ModDataComponent;
import top.ctnstudio.futurefood.core.init.ModTileEntity;

import java.util.List;

// TODO 物品破坏后保存能量
public class BatteryEntityBlock extends HorizontalDirectionalEntityBlock<BatteryBlockEntity> {
  private static final MapCodec<BatteryEntityBlock> CODEC = simpleCodec(BatteryEntityBlock::new);

  public BatteryEntityBlock() {
    this(BlockBehaviour.Properties.of());
  }

  protected BatteryEntityBlock(Properties properties) {
    super(properties
      .noOcclusion()
      .isValidSpawn(ModBlock.argumentNever())
      .isRedstoneConductor(ModBlock.never())
      .isSuffocating(ModBlock.never())
      .isViewBlocking(ModBlock.never()), ModTileEntity.BATTERY);
  }

  @Override
  protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
    if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
      serverPlayer.openMenu(state.getMenuProvider(level, pos));
    }
    return InteractionResult.sidedSuccess(level.isClientSide);
  }

  @Override
  protected RenderShape getRenderShape(BlockState state) {
    return RenderShape.MODEL;
  }

  @Override
  protected MapCodec<? extends BaseEntityBlock> codec() {
    return CODEC;
  }

  @Override
  public @Nullable BatteryBlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new BatteryBlockEntity(pos, state);
  }

  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> type) {
    return createTickerHelper(type, ModTileEntity.BATTERY.get(), ModBlockEntity::tick);
  }

  @Override
  public @NotNull ItemStack getCloneItemStack(@NotNull BlockState state,
                                              @NotNull HitResult target,
                                              @NotNull LevelReader reader,
                                              @NotNull BlockPos pos,
                                              Player player) {
    var stack = super.getCloneItemStack(state, target, reader, pos, player);
    if (!(reader instanceof ILevelExtension level)) {
      return stack;
    }
    var capability = level.getCapability(Capabilities.EnergyStorage.BLOCK, pos, null);
    if (capability == null) {
      return stack;
    }
    stack.set(ModDataComponent.ENERGY_STORAGE,
      capability instanceof IModEnergyStorage ?
        new ItemEnergyStorageData.Data((IModEnergyStorage) capability) :
        new ItemEnergyStorageData.Data(capability));
    return stack;
  }

  // TODO 破坏保存物品
  @Override
  protected @NotNull List<ItemStack> getDrops(@NotNull BlockState state, LootParams.Builder params) {
    if (!(params.getOptionalParameter(LootContextParams.BLOCK_ENTITY) instanceof BatteryBlockEntity batteryBlockEntity)) {
      return super.getDrops(state, params);
    }

    params.withDynamicDrop(ModDataComponent.ENERGY_STORAGE_NAME, e -> {
      var stack = new ItemStack(this);
      stack.set(ModDataComponent.ENERGY_STORAGE,
        new ItemEnergyStorageData.Data((IModEnergyStorage) batteryBlockEntity.externalGetEnergyStorage(null)));
      e.accept(stack);
    });

    return super.getDrops(state, params);
  }
}

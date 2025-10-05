package top.ctnstudio.futurefood.common.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import top.ctnstudio.futurefood.common.block.tile.BatteryBlockEntity;
import top.ctnstudio.futurefood.common.block.tile.ModBlockEntity;
import top.ctnstudio.futurefood.core.init.ModTileEntity;

// TODO 物品破坏后保存能量
public class BatteryEntityBlock extends ModBaseEntityBlock<BatteryBlockEntity> {
  private static final MapCodec<BatteryEntityBlock> CODEC = simpleCodec(BatteryEntityBlock::new);

  public BatteryEntityBlock() {
    this(BlockBehaviour.Properties.of());
  }

  protected BatteryEntityBlock(Properties properties) {
    super(properties, ModTileEntity.BATTERY);
  }

  @Override
  protected MapCodec<? extends BaseEntityBlock> codec() {
    return CODEC;
  }

  @Override
  public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new BatteryBlockEntity(pos, state);
  }

  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
    return createTickerHelper(type, ModTileEntity.BATTERY.get(), ModBlockEntity::tick);
  }
}

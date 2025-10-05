package top.ctnstudio.futurefood.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.capabilities.Capabilities;
import org.jetbrains.annotations.NotNull;
import top.ctnstudio.futurefood.util.BlockEntyUtil;
import top.ctnstudio.futurefood.util.ItemEntityUtil;
import top.ctnstudio.futurefood.util.ItemUtil;

import java.util.function.Supplier;

public abstract class ModBaseEntityBlock<T extends BlockEntity> extends BaseEntityBlock {
  private final Supplier<BlockEntityType<T>> tileType;

  protected ModBaseEntityBlock(Properties properties, Supplier<BlockEntityType<T>> tileType) {
    super(properties);
    this.tileType = tileType;
  }

  public @NotNull T getBlockEntity(Level level, BlockPos pos) {
    return BlockEntyUtil.getBlockEntity(level, pos, tileType.get());
  }

  public Supplier<BlockEntityType<T>> getBlockEntityType() {
    return tileType;
  }

  @Override
  public boolean onDestroyedByPlayer(BlockState state, Level world, BlockPos pos, Player player,
                                     boolean willHarvest, FluidState fluid) {
    if (!world.isClientSide()) {
      var iItemHandler = world.getCapability(Capabilities.ItemHandler.BLOCK, pos, null);
      if (iItemHandler != null) {
        ItemEntityUtil.summonLootItemStacks((ServerLevel) world, pos, ItemUtil.clearContent(iItemHandler));
      }
    }
    return super.onDestroyedByPlayer(state, world, pos, player, willHarvest, fluid);
  }
}

package top.ctnstudio.futurefood.common.block;

import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import org.jetbrains.annotations.NotNull;

public abstract class DirectionalEntityBlock extends BaseEntityBlock {
  protected static final DirectionProperty FACING = BlockStateProperties.FACING;

  protected DirectionalEntityBlock(Properties properties) {
    super(properties);
    this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(FACING);
  }

  /**
   * 放置时调用
   */
  @Override
  public BlockState getStateForPlacement(BlockPlaceContext context) {
    return this.defaultBlockState().setValue(FACING,
      context.getNearestLookingDirection().getOpposite());
  }

  /**
   * Returns the blockstate with the given rotation from the passed blockstate. If inapplicable,
   * returns the passed blockstate.
   */
  @Override
  protected @NotNull BlockState rotate(BlockState state, Rotation rot) {
    return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
  }

  /**
   * Returns the blockstate with the given mirror of the passed blockstate. If inapplicable,
   * returns the passed blockstate.
   */
  @Override
  protected @NotNull BlockState mirror(BlockState state, Mirror mirror) {
    return state.rotate(mirror.getRotation(state.getValue(FACING)));
  }
}

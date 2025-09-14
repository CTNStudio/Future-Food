package ctn.futurefood.common.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class QedEntityBlock extends BaseEntityBlock  {
	private static final MapCodec<QedEntityBlock> CODEC  = simpleCodec(QedEntityBlock::new);
	public static final VoxelShape SHAPE = makeShape();
	
	public QedEntityBlock(Properties properties) {
		super(properties);
	}
	
	@Override
	protected MapCodec<? extends BaseEntityBlock> codec() {
		return CODEC;
	}
	
	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new QedBlockEntity(pos, state);
	}
	
	@Override
	protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}
	
	@SuppressWarnings("DuplicatedCode")
	private static VoxelShape makeShape(){
		VoxelShape shape = Shapes.empty();
		shape = Shapes.join(shape, Shapes.box(0.25, -0.0015625000000000083, 0.25, 0.75, 0.125, 0.75), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.25, 0, 0.25, 0.75, 0.25, 0.75), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.25, 0.8125, 0.25, 0.75, 0.875, 0.75), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.375, 0.625, 0.375, 0.625, 0.6875, 0.625), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.375, 0.125, 0.375, 0.625, 0.4375, 0.625), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.4375, 0.4375, 0.4375, 0.5625, 1.1875, 0.5625), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.375, 1, 0.375, 0.625, 1.0625, 0.625), BooleanOp.OR);
		return shape;
	}
}

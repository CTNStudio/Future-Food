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

public class QerEntityBlock extends BaseEntityBlock {
	private static final MapCodec<QerEntityBlock> CODEC  = simpleCodec(QerEntityBlock::new);
	public static final VoxelShape SHAPE = makeShape();
	
	public QerEntityBlock(Properties properties) {
		super(properties);
	}
	
	@Override
	protected MapCodec<? extends BaseEntityBlock> codec() {
		return CODEC;
	}
	
	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new QerBlockEntity(pos, state);
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
		shape = Shapes.join(shape, Shapes.box(-0.004441738241592191, -0.125, 0.3124999999999999, 0.05805826175840764, 0.3125, 0.5625), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.9419417382415922, -0.125, 0.4375, 1.0044417382415922, 0.3125, 0.6875000000000001), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.9419417382415922, -0.125, 0.3125, 1.0044417382415922, 0.3125, 0.5625000000000001), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.3124999999999999, -0.125, 0.9419417382415922, 0.5625, 0.3125, 1.0044417382415922), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.12055826175840781, 0.3125, 0.3749999999999999, 0.18305826175840764, 0.6875, 0.625), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.3749999999999999, 0.3125, 0.8169417382415923, 0.625, 0.6875, 0.8794417382415922), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.375, 0.3125, 0.12055826175840778, 0.6250000000000001, 0.6875, 0.18305826175840767), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.8169417382415923, 0.3125, 0.375, 0.8794417382415922, 0.6875, 0.6250000000000001), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.25, 0.8125, 0.25, 0.75, 0.875, 0.75), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.375, 0.625, 0.375, 0.625, 0.6875, 0.625), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.375, 0.125, 0.375, 0.625, 0.4375, 0.625), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.4375, 0.4375, 0.4375, 0.5625, 1.1875, 0.5625), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.375, 1, 0.375, 0.625, 1.0625, 0.625), BooleanOp.OR);
		return shape;
	}
}

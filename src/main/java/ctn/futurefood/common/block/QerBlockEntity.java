package ctn.futurefood.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import static ctn.futurefood.init.FfBlockEntityTypes.QER_BLOCK_ENTITY_TYPE;

public class QerBlockEntity extends BlockEntity {
	public QerBlockEntity(BlockPos pos, BlockState blockState) {
		super(QER_BLOCK_ENTITY_TYPE.get(), pos, blockState);
	}
}

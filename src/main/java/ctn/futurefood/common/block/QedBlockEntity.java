package ctn.futurefood.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import static ctn.futurefood.init.FfBlockEntityTypes.QED_BLOCK_ENTITY_TYPE;

public class QedBlockEntity extends BlockEntity {
	public QedBlockEntity(BlockPos pos, BlockState blockState) {
		super(QED_BLOCK_ENTITY_TYPE.get(), pos, blockState);
	}
}

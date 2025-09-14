package ctn.futurefood.init;

import ctn.ctntemplate.CtnTemplate;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class Blocks {
	public static final DeferredRegister.Blocks BLOCK_REGISTER = DeferredRegister.createBlocks(CtnTemplate.ID);
	
	public static DeferredBlock<Block> registerSimpleBlock(String name, BlockBehaviour.Properties props) {
		return BLOCK_REGISTER.registerSimpleBlock(name, props);
	}
}

package ctn.futurefood.init;

import ctn.futurefood.FutureFood;
import ctn.futurefood.common.block.QedEntityBlock;
import ctn.futurefood.common.block.QerEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;

public class FfBlocks {
	public static final DeferredRegister.Blocks BLOCK_REGISTER = DeferredRegister.createBlocks(FutureFood.ID);
	
	public static final DeferredBlock<Block> QED = registerBlock("quantum_energy_diffuser", QedEntityBlock::new, BlockBehaviour.Properties.of());
	public static final DeferredBlock<Block> QER = registerBlock("quantum_energy_receiver", QerEntityBlock::new, BlockBehaviour.Properties.of());
	
	public static DeferredBlock<Block> registerSimpleBlock(String name, BlockBehaviour.Properties props) {
		return BLOCK_REGISTER.registerSimpleBlock(name, props);
	}
	
	public static <B extends Block> DeferredBlock<Block> registerBlock(String name, Function<BlockBehaviour.Properties, ? extends B> func, BlockBehaviour.Properties props) {
		return BLOCK_REGISTER.registerBlock(name, func, props);
	}
}

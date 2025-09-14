package ctn.futurefood.init;

import ctn.futurefood.FutureFood;
import ctn.futurefood.common.block.QedBlockEntity;
import ctn.futurefood.common.block.QerBlockEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static ctn.futurefood.init.FfBlocks.QED;
import static ctn.futurefood.init.FfBlocks.QER;

public class FfBlockEntityTypes {
	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPE_REGISTER = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, FutureFood.ID);
	
	public static final Supplier<BlockEntityType<QedBlockEntity>> QED_BLOCK_ENTITY_TYPE = register("quantum_energy_diffuser_block_entity", QedBlockEntity::new, QED);
	public static final Supplier<BlockEntityType<QerBlockEntity>> QER_BLOCK_ENTITY_TYPE = register("quantum_energy_receiver_block_entity", QerBlockEntity::new, QER);
	
	private static <B extends BlockEntity> Supplier<BlockEntityType<B>> register(final String name, BlockEntityType.BlockEntitySupplier<B> blockEntity, Supplier<Block> blocks) {
		return registerBlockEntity(name, () -> BlockEntityType.Builder.of(blockEntity, blocks.get()).build(null));
	}
	
	private static <I extends BlockEntityType<?>> DeferredHolder<BlockEntityType<?>, I> registerBlockEntity(String name, final Supplier<? extends I> sup) {
		return BLOCK_ENTITY_TYPE_REGISTER.register(name, sup);
	}
}

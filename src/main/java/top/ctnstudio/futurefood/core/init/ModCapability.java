package top.ctnstudio.futurefood.core.init;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities.EnergyStorage;
import net.neoforged.neoforge.capabilities.Capabilities.ItemHandler;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import top.ctnstudio.futurefood.common.block.tile.EnergyStorageBlockEntity;

@EventBusSubscriber
public final class ModCapability {
  @SubscribeEvent
  public static void register(final RegisterCapabilitiesEvent event) {
    ModTileEntity.TILES.getEntries().forEach(entry -> {
      Block validBlock = entry.get().getValidBlocks().stream().iterator().next();
      BlockEntity blockEntity = entry.get().create(BlockPos.ZERO, validBlock.defaultBlockState());
      if (blockEntity instanceof EnergyStorageBlockEntity) {
        event.registerBlockEntity(EnergyStorage.BLOCK, entry.get(), (be1, d) ->
          ((EnergyStorageBlockEntity) be1).externalGetEnergyStorage(d));
        event.registerBlockEntity(ItemHandler.BLOCK, entry.get(), (be1, d) ->
          ((EnergyStorageBlockEntity) be1).externalGetItemHandler(d));
      }
    });

    event.registerBlockEntity(ModCapabilitys.Block.UNLIMITED_LINK_STORAGE, ModTileEntity.QED.get(),
      (be, v) -> be.getUnlimitedStorage());
  }
}

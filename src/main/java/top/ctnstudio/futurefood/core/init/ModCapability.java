package top.ctnstudio.futurefood.core.init;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.Capabilities.EnergyStorage;
import net.neoforged.neoforge.capabilities.Capabilities.ItemHandler;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.ctnstudio.futurefood.api.capability.IUnlimitedLinkStorage;
import top.ctnstudio.futurefood.common.block.tile.EnergyStorageBlockEntity;
import top.ctnstudio.futurefood.core.FutureFood;

@EventBusSubscriber
public final class ModCapability {
  private static <T> @NotNull BlockCapability<T, @Nullable Void> createVoid(String name, Class<T> typeClass) {
    return BlockCapability.createVoid(FutureFood.modRL(name), typeClass);
  }

  public static class ModBlockCapability {
    public static final BlockCapability<IUnlimitedLinkStorage, Void> UNLIMITED_LINK_STORAGE =
      createVoid("unlimited_link_storage", IUnlimitedLinkStorage.class);
  }
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

    event.registerBlockEntity(ModCapability.ModBlockCapability.UNLIMITED_LINK_STORAGE, ModTileEntity.QED.get(),
      (be, v) -> be.getUnlimitedStorage());
  }
}

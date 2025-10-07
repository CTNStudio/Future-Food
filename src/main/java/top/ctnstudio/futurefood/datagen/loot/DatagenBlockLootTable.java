package top.ctnstudio.futurefood.datagen.loot;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;
import top.ctnstudio.futurefood.core.init.ModBlock;

import java.util.ArrayList;
import java.util.Set;

public class DatagenBlockLootTable extends BlockLootSubProvider {
  public DatagenBlockLootTable(HolderLookup.Provider lookupProvider) {
    super(Set.of(), FeatureFlags.DEFAULT_FLAGS, lookupProvider);
  }

  @Override
  protected void generate() {
    dropSelf(ModBlock.QED.get());
    dropSelf(ModBlock.QER.get());
    dropSelf(ModBlock.PARTICLE_COLLIDER.get());
    dropSelf(ModBlock.GLUTTONY.get());
    dropSelf(ModBlock.BATTERY.get());
    dropSelf(ModBlock.INFINITE_BATTERY.get());
  }

  @Override
  protected @NotNull Iterable<Block> getKnownBlocks() {
    return new ArrayList<>(ModBlock.BLOCKS.getEntries().stream().map(DeferredHolder::value).toList());
  }
}

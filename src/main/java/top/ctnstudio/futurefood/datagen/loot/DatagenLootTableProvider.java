package top.ctnstudio.futurefood.datagen.loot;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class DatagenLootTableProvider extends LootTableProvider {
  public DatagenLootTableProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
    super(output, Collections.emptySet(), new ArrayList<>(), registries);
    addLoot(getTables());
  }

  public void addLoot(List<SubProviderEntry> tables) {
    tables.add(new SubProviderEntry(DatagenBlockLootTable::new, LootContextParamSets.BLOCK));
  }
}

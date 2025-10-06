package top.ctnstudio.futurefood.datagen.loot;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyComponentsFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;
import top.ctnstudio.futurefood.core.init.ModBlock;
import top.ctnstudio.futurefood.core.init.ModDataComponent;

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
    add(ModBlock.BATTERY.get(), this::createEnergyStorageDrop);
  }

  protected LootTable.Builder createEnergyStorageDrop(Block block) {
    return LootTable.lootTable()
      .withPool(
        applyExplosionCondition(
          block, LootPool.lootPool()
            .setRolls(ConstantValue.exactly(1.0F))
            .add(
              LootItem.lootTableItem(block)
                .apply(
                  CopyComponentsFunction.copyComponents(CopyComponentsFunction.Source.BLOCK_ENTITY)
                    .include(
                      ModDataComponent.ENERGY_STORAGE.get())))));
  }

  @Override
  protected @NotNull Iterable<Block> getKnownBlocks() {
    return new ArrayList<>(ModBlock.BLOCKS.getEntries().stream().map(DeferredHolder::value).toList());
  }
}

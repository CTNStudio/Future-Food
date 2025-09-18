package top.ctnstudio.futurefood.datagen.tag;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import top.ctnstudio.futurefood.core.FutureFood;

import javax.annotation.CheckForNull;
import java.util.concurrent.CompletableFuture;

public class FfItemTags extends ItemTagsProvider {
  public FfItemTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
                    CompletableFuture<TagLookup<Block>> blockTags,
                    @CheckForNull ExistingFileHelper existingFileHelper) {
    super(output, lookupProvider, blockTags, FutureFood.ID, existingFileHelper);
  }

  protected static TagKey<Item> createTag(String name) {
    return ItemTags.create(FutureFood.modRL(name));
  }

  @Override
  protected void addTags(HolderLookup.Provider capability) {
  }
}
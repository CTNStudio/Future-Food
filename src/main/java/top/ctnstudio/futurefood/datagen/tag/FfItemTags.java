package top.ctnstudio.futurefood.datagen.tag;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import top.ctnstudio.futurefood.core.FutureFood;

import javax.annotation.CheckForNull;
import java.util.concurrent.CompletableFuture;

import static top.ctnstudio.futurefood.core.init.ModItem.CYBER_WRENCH;

public class FfItemTags extends ItemTagsProvider {
  public FfItemTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
                    CompletableFuture<TagLookup<Block>> blockTags,
                    @CheckForNull ExistingFileHelper existingFileHelper) {
    super(output, lookupProvider, blockTags, FutureFood.ID, existingFileHelper);
  }

  protected static TagKey<Item> createTag(String name) {
    return ItemTags.create(FutureFood.modRL(name));
  }

  protected static TagKey<Item> createGeneralTag(String name) {
    return ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", name));
  }

  @Override
  protected void addTags(HolderLookup.Provider capability) {
    tag(Tags.Items.TOOLS).add(CYBER_WRENCH.get());
    tag(Tags.Items.TOOLS_WRENCH).add(CYBER_WRENCH.get());
  }
}

package top.ctnstudio.futurefood.datagen.tag;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import top.ctnstudio.futurefood.core.FutureFood;
import top.ctnstudio.futurefood.core.init.ModBlock;

import javax.annotation.CheckForNull;
import java.util.concurrent.CompletableFuture;

public class FfBlockTags extends BlockTagsProvider {

  public static final TagKey<Block> UNLIMITED_RECEPTION = createTag("unlimited_reception");

  public FfBlockTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
                     @CheckForNull ExistingFileHelper existingFileHelper) {
    super(output, lookupProvider, FutureFood.ID, existingFileHelper);
  }

  protected static TagKey<Block> createTag(String name) {
    return BlockTags.create(FutureFood.modRL(name));
  }

  @Override
  protected void addTags(HolderLookup.Provider capability) {
    tag(UNLIMITED_RECEPTION).add(ModBlock.PARTICLE_COLLIDER.value(), ModBlock.QER.value());
  }
}
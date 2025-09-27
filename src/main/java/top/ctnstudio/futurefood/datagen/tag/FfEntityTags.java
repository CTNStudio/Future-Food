package top.ctnstudio.futurefood.datagen.tag;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import top.ctnstudio.futurefood.core.FutureFood;

import javax.annotation.CheckForNull;
import java.util.concurrent.CompletableFuture;

public class FfEntityTags extends EntityTypeTagsProvider {
  public FfEntityTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
                      @CheckForNull ExistingFileHelper existingFileHelper) {
    super(output, lookupProvider, FutureFood.ID, existingFileHelper);
  }

  protected static TagKey<EntityType<?>> createTag(String name) {
    return TagKey.create(Registries.ENTITY_TYPE, FutureFood.modRL(name));
  }

  @Override
  protected void addTags(HolderLookup.Provider capability) {
  }
}

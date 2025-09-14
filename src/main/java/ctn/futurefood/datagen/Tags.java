package ctn.futurefood.datagen;

import ctn.futurefood.FutureFood;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.DamageTypeTagsProvider;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import javax.annotation.CheckForNull;
import java.util.concurrent.CompletableFuture;

public class Tags {
	public static class Block extends BlockTagsProvider {
		
		public Block(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @CheckForNull ExistingFileHelper existingFileHelper) {
			super(output, lookupProvider, FutureFood.ID, existingFileHelper);
		}
		
		protected static TagKey<net.minecraft.world.level.block.Block> createTag(String name) {
			return BlockTags.create(ResourceLocation.fromNamespaceAndPath(FutureFood.ID, name));
		}
		
		@Override
		protected void addTags(HolderLookup.Provider capability) {
		}
	}
	
	public static class Item extends ItemTagsProvider {
		public Item(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<net.minecraft.world.level.block.Block>> blockTags, @CheckForNull ExistingFileHelper existingFileHelper) {
			super(output, lookupProvider, blockTags, FutureFood.ID, existingFileHelper);
		}
		
		protected static TagKey<net.minecraft.world.item.Item> createTag(String name) {
			return ItemTags.create(ResourceLocation.fromNamespaceAndPath(FutureFood.ID, name));
		}
		
		@Override
		protected void addTags(HolderLookup.Provider capability) {
		}
	}
	
	public static class Entity extends EntityTypeTagsProvider {
		public Entity(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @CheckForNull ExistingFileHelper existingFileHelper) {
			super(output, lookupProvider, FutureFood.ID, existingFileHelper);
		}
		
		protected static TagKey<EntityType<?>> createTag(String name) {
			return TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(FutureFood.ID, name));
		}
		
		@Override
		protected void addTags(HolderLookup.Provider capability) {
		}
	}
	
	public static class DamageType extends DamageTypeTagsProvider {
		
		public DamageType(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @CheckForNull ExistingFileHelper existingFileHelper) {
			super(output, lookupProvider, FutureFood.ID, existingFileHelper);
		}
		
		protected static TagKey<net.minecraft.world.damagesource.DamageType> createTag(String name) {
			return TagKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(FutureFood.ID, name));
		}
		
		@Override
		protected void addTags(HolderLookup.Provider capability) {
		}
	}
}

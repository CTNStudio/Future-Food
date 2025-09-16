package top.ctnstudio.futurefood.common.item;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;

public class ItemBlockBase extends BlockItem {
  public ItemBlockBase(final ResourceLocation registryName) {
    super(BuiltInRegistries.BLOCK.get(registryName), new Properties());
  }
}

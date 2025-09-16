package top.ctnstudio.futurefood.core.init;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;

public final class ModItem extends AbstractObjectRegister<Item> {
  public static final ModItem INSTANCE = new ModItem();
  private ModItem() {
    super(BuiltInRegistries.ITEM, Registries.ITEM);
  }
}

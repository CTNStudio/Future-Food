package top.ctnstudio.futurefood.core.init;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import top.ctnstudio.futurefood.core.FutureFood;

import java.util.function.Supplier;

public final class ModItem {
  public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(FutureFood.ID);

  private ModItem() {}

  public static final DeferredItem<Item> QED =
    ITEMS.register("quantum_energy_diffuser", createBlockItem(ModBlock.QED));
  public static final DeferredItem<Item> QER =
    ITEMS.register("quantum_energy_receiver", createBlockItem(ModBlock.QER));
  public static final DeferredItem<Item> PARTICLE_COLLIDER =
    ITEMS.register("particle_collider", createBlockItem(ModBlock.PARTICLE_COLLIDER));

  private static Supplier<BlockItem> createBlockItem(Supplier<Block> block) {
    return () -> new BlockItem(block.get(), new Item.Properties());
  }
}

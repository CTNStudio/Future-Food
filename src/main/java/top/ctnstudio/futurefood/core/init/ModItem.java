package top.ctnstudio.futurefood.core.init;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import top.ctnstudio.futurefood.client.renderer.item.ParticleColliderBlockItemRenderer;
import top.ctnstudio.futurefood.common.item.BatteryItem;
import top.ctnstudio.futurefood.common.item.food.ModGeoBlockItem;
import top.ctnstudio.futurefood.common.item.tool.CyberWrenchItem;
import top.ctnstudio.futurefood.core.FutureFood;

import java.util.function.Function;
import java.util.function.Supplier;

public final class ModItem {
  public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(FutureFood.ID);
  public static final DeferredItem<Item> CYBER_WRENCH = ITEMS.registerItem(
    "cyber_wrench", CyberWrenchItem::new);
  public static final DeferredItem<Item> QED = ITEMS.register(
    "quantum_energy_diffuser", createBlockItem(ModBlock.QED));
  public static final DeferredItem<Item> QER = ITEMS.register(
    "quantum_energy_receiver", createBlockItem(ModBlock.QER));
  public static final DeferredItem<Item> PARTICLE_COLLIDER = ITEMS.register(
    "particle_collider", createGeoBlockItem(ModBlock.PARTICLE_COLLIDER,
      ParticleColliderBlockItemRenderer::new));
  public static final DeferredItem<Item> GLUTTONY = ITEMS.register(
    "gluttony", createBlockItem(ModBlock.GLUTTONY));
  public static final DeferredItem<Item> BATTERY = ITEMS.register(
    "battery", () -> new BatteryItem(ModBlock.BATTERY.get()));
  public static final DeferredItem<Item> INFINITE_BATTERY = ITEMS.register(
    "infinite_battery", () -> new BatteryItem(ModBlock.INFINITE_BATTERY.get()));
  public static final DeferredItem<Item> FOOD_ESSENCE = ITEMS.registerSimpleItem(
    "food_essence");

  private static Supplier<BlockItem> createBlockItem(Supplier<Block> block) {
    return () -> new BlockItem(block.get(), new Item.Properties());
  }

  private static Supplier<BlockItem> createGeoBlockItem(Supplier<Block> block) {
    return () -> new ModGeoBlockItem(block.get(), new Item.Properties());
  }

  private static Supplier<BlockItem> createGeoBlockItem(Supplier<Block> block, Function<Block,
    BlockEntityWithoutLevelRenderer> renderer) {
    return () -> {
      Block b = block.get();
      return new ModGeoBlockItem(b, new Item.Properties(), () -> renderer.apply(b));
    };
  }
}

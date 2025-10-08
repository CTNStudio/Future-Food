package top.ctnstudio.futurefood.core.init;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import top.ctnstudio.futurefood.client.renderer.item.ParticleColliderBlockItemRenderer;
import top.ctnstudio.futurefood.common.item.BatteryItem;
import top.ctnstudio.futurefood.common.item.ModGeoBlockItem;
import top.ctnstudio.futurefood.common.item.food.*;
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

  public static final DeferredItem<Item> ANTIMATTER_SNACK = ITEMS.register("antimatter_snack",
    AntimatterSnack::new);
  public static final DeferredItem<Item> STRONGLY_INTERACTING_BREAD = ITEMS.register(
    "stronglg_interacting_bread", StronglyInteractingBread::new);
  public static final DeferredItem<Item> WEAKLY_INTERACTING_WATER_BOTTLE = ITEMS.register(
    "weakly_interacting_water_bottle", WeaklyInteractingWaterBottle::new);
  public static final DeferredItem<Item> LEYDEN_JAR = ITEMS.register("leyden_jar",
    LeydenJar::new);
  public static final DeferredItem<Item> SCHRODINGERS_CAN = ITEMS.register(
    "schrodingers_can", SchrodingersCan::new);
  public static final DeferredItem<Item> WORMHOLE_COOKIE = ITEMS.register(
    "wormhole_cookie", WormholeCookie::new);
  public static final DeferredItem<Item> POWERED_MILK = ITEMS.register(
    "powered_milk", PowderedMilk::new);
  public static final DeferredItem<Item> ENTROPY_STEW = ITEMS.register(
    "entropy_stew", EntropyStew::new);
  public static final DeferredItem<Item> UNPREDICTABLE_CHORUS_FRUIT = ITEMS.register(
    "unpredictable_chorus_fruit", UnpredictableChorusFruit::new);
  public static final DeferredItem<Item> BLACK_HOLE_CAKE = ITEMS.register(
    "black_hole_cake", BlackHoleCake::new);
  public static final DeferredItem<Item> WHITE_HOLE_CAKE = ITEMS.register(
    "white_hole_cake", WhiteHoleCake::new);
  public static final DeferredItem<Item> ATOM_COLA = ITEMS.register(
    "atom_cola", AtomCola::new);

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

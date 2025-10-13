package top.ctnstudio.futurefood.core;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.ctnstudio.futurefood.api.recipe.ParticleColliderRecipeManager;
import top.ctnstudio.futurefood.core.init.*;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

@SuppressWarnings("unused")
@Mod(FutureFood.ID)
public class FutureFood {
  public static final String ID = "futurefood";
  public static final Logger LOGGER = LogManager.getLogger(ID);

  public FutureFood(IEventBus modEventBus, ModContainer modContainer) {
    LOGGER.info("Starting FutureFood");
    ModDataComponent.ITEM_COMPONENTS.register(modEventBus);
    ModMenu.MENU.register(modEventBus);
    ModItem.ITEMS.register(modEventBus);
    ModBlock.BLOCKS.register(modEventBus);
    ModTileEntity.TILES.register(modEventBus);
    ModCreativeModeTab.TABS.register(modEventBus);
    ModEffect.EFFECT.register(modEventBus);

  }

  /**
   * 从注册表提取物件。
   *
   * @param registry 由 {@code BuiltInRegistries} 提取的注册表。
   * @param name     物件名称。
   * @param <T>      物件的类型，通常会被 {@code registry} 的输入提供自动推断。
   * @return 如果物件被注册了，则不应该返回 null.
   */
  @CheckForNull
  public static <T> T getModObject(final Registry<T> registry, final String name) {
    return registry.get(modRL(name));
  }

  /**
   * 构建一个模组用 {@code ResourceLocation}。
   *
   * @param name 数据路径（名称）。
   * @return 构建一个以 ModID 为命名空间的 {@code ResourceLocation}。
   */
  @Nonnull
  public static ResourceLocation modRL(final String name) {
    return ResourceLocation.fromNamespaceAndPath(FutureFood.ID, name);
  }
}

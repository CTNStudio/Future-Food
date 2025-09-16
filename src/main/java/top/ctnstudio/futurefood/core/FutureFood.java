package top.ctnstudio.futurefood.core;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.ctnstudio.futurefood.core.init.ModBlock;
import top.ctnstudio.futurefood.core.init.ModCreativeModeTab;
import top.ctnstudio.futurefood.core.init.ModItem;
import top.ctnstudio.futurefood.core.init.ModTileEntity;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

@SuppressWarnings("unused")
@Mod(FutureFood.ID)
public class FutureFood {
  public static final String ID = "futurefood";
  public static final Logger LOGGER = LogManager.getLogger(ID);

  public FutureFood(IEventBus modEventBus, ModContainer modContainer) {
    modEventBus.addListener(this::register);
  }

  /**
   * Build the resource location with mod namespace.
   *
   * @param name the path for rl.
   * @return build a ResourceLocation data with mod id for namespace.
   */
  @Nonnull
  public static ResourceLocation modRL(final String name) {
    return ResourceLocation.fromNamespaceAndPath(FutureFood.ID, name);
  }

  /**
   * Get the object which was mod register it.
   *
   * @param registry the register, get it from {@code BuiltInRegistries}.
   * @param name     the data register name.
   * @param <T>      the type of data, it will auto input by registry.
   * @return the target data output if it had be registry.
   */
  @CheckForNull
  public static <T> T getModObject(final Registry<T> registry, final String name) {
    return registry.get(modRL(name));
  }

  /**
   * Register the objects in mod.
   */
  private void register(final RegisterEvent event) {
    ModBlock.INSTANCE.registerObject(event);
    ModTileEntity.INSTANCE.registerObject(event);
    ModItem.INSTANCE.registerObject(event);
    ModCreativeModeTab.init(event);
  }
}

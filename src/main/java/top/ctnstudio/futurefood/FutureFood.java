package top.ctnstudio.futurefood;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.ctnstudio.futurefood.init.ModBlock;
import top.ctnstudio.futurefood.init.ModTileEntity;

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
  public static ResourceLocation modRL(String name) {
    return ResourceLocation.tryBuild(FutureFood.ID, name);
  }

  public static <T> T getModObject(Registry<T> registry, String name) {
    return registry.get(modRL(name));
  }

  /**
   * Register the objects in mod.
   */
  private void register(final RegisterEvent event) {
    ModBlock.init(event);
    ModTileEntity.init(event);
  }
}

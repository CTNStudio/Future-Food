package top.ctnstudio.futurefood;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.ctnstudio.futurefood.init.ModTileEntity;

import static top.ctnstudio.futurefood.init.ModBlock.BLOCK_REGISTER;
import static top.ctnstudio.futurefood.init.FfItems.ITEM_REGISTER;

@Mod(FutureFood.ID)
public class FutureFood {
  public static final String ID = "futurefood";
  public static final Logger LOGGER = LogManager.getLogger();

  public FutureFood(IEventBus modEventBus, ModContainer modContainer) {
    NeoForge.EVENT_BUS.register(this);

    modEventBus.addListener(this::register);

    BLOCK_REGISTER.register(modEventBus);
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
    ModTileEntity.init(event);
  }
}

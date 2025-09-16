package top.ctnstudio.futurefood.api;

import com.google.common.collect.ImmutableMap;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.apache.logging.log4j.util.Lazy;
import org.jetbrains.annotations.ApiStatus.Internal;

import java.util.Map;
import java.util.function.Supplier;

public interface IObjectRegister<T> {
  /**
   * Copy the objects had be register.
   */
  ImmutableMap<ResourceLocation, Lazy<T>> copyObjects();

  /**
   * @apiNote Register an object.
   *
   * @param name the register name, it will make a ResourceLocation by mod.
   * @param supplier the object we will register. Never use an object instance without a wrapper,
   *                 too early to use the data will make registry holder crash.
   * @return the supplier who is input, or it will make a lazy wrapper.
   */
  @Internal
  Supplier<T> register(String name, Supplier<T> supplier);

  /**
   * Get data register. Instance this from {@code BuiltInRegistries}.
   */
  @Internal
  Registry<T> getRegistry();

  /**
   * Get data resource key. Instance this form {@code Registries}.
   */
  @Internal
  ResourceKey<? extends Registry<T>> getResourceKey();

  /**
   * return the raw map in register.
   */
  @Internal
  Map<ResourceLocation, Lazy<T>> getObjectMap();

  /**
   * Do something after the object had be register.
   */
  @Internal
  default void afterRegister(ResourceLocation registerName, Lazy<T> lazy) {
  }

  /**
   * @apiNote call this with register event, don't register it to listener.
   */
  @Internal
  default void registerObject(final RegisterEvent event) {
    if (!event.getRegistry().equals(this.getRegistry())) {
      return;
    }

    final var reg = this.getResourceKey();

    this.getObjectMap().forEach((key, value) -> {
      event.register(reg, key, value);
      this.afterRegister(key, value);
    });
  }
}

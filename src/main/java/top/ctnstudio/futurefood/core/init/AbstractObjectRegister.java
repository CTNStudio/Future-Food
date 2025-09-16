package top.ctnstudio.futurefood.core.init;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.util.Lazy;
import top.ctnstudio.futurefood.api.IObjectRegister;
import top.ctnstudio.futurefood.core.FutureFood;

import java.util.Map;
import java.util.function.Supplier;

public abstract class AbstractObjectRegister<T> implements IObjectRegister<T> {
  private final Map<ResourceLocation, Lazy<T>> data = Maps.newHashMap();
  private final Registry<T> registry;
  private final ResourceKey<? extends Registry<T>> registryKey;

  public AbstractObjectRegister(Registry<T> registry,
                                ResourceKey<? extends Registry<T>> registryKey) {
    this.registry = registry;
    this.registryKey = registryKey;
  }
  @Override
  public ImmutableMap<ResourceLocation, Lazy<T>> copyObjects() {
    return ImmutableMap.copyOf(this.data);
  }

  @Override
  public Supplier<T> register(String name, Supplier<T> supplier) {
    return this.data.put(FutureFood.modRL(name), Lazy.lazy(supplier));
  }

  public Supplier<T> register(ResourceLocation name, Supplier<T> supplier) {
    return this.data.put(name, Lazy.lazy(supplier));
  }

  @Override
  public Registry<T> getRegistry() {
    return this.registry;
  }

  @Override
  public ResourceKey<? extends Registry<T>> getResourceKey() {
    return this.registryKey;
  }

  @Override
  public Map<ResourceLocation, Lazy<T>> getObjectMap() {
    return this.data;
  }
}

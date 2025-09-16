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
   * 拷贝数据到不可变表。
   */
  ImmutableMap<ResourceLocation, Lazy<T>> copyObjects();

  /**
   * @apiNote 注册数据物件.
   *
   * @param name 物件注册名，最终会被处理为 {@code ResourceLocation}.
   * @param supplier 注册物件包装，切记：注册事件结束之前皆不可以对物件脱壳。
   * @return 返回输入的对象，但可能会被处理为 {@code Lazy}。
   */
  @Internal
  Supplier<T> register(String name, Supplier<T> supplier);

  /**
   * 获取数据注册器，由 {@code BuiltInRegistries} 获取。
   */
  @Internal
  Registry<T> getRegistry();

  /**
   * 获取注册资源，由 {@code Registries} 提取。
   */
  @Internal
  ResourceKey<? extends Registry<T>> getResourceKey();

  /**
   * 取得物件表。
   */
  @Internal
  Map<ResourceLocation, Lazy<T>> getObjectMap();

  /**
   * 任何需要再注册之后继续追加的任务。
   */
  @Internal
  default void afterRegister(ResourceLocation registerName, Lazy<T> lazy) {
  }

  /**
   * @apiNote 注册物件，但不要加入到监听队列。
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

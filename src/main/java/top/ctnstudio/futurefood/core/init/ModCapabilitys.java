package top.ctnstudio.futurefood.core.init;

import net.neoforged.neoforge.capabilities.BlockCapability;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.ctnstudio.futurefood.api.capability.IUnlimitedLinkStorage;
import top.ctnstudio.futurefood.core.FutureFood;

public final class ModCapabilitys {
  private static <T> @NotNull BlockCapability<T, @Nullable Void> createVoid(String name, Class<T> typeClass) {
    return BlockCapability.createVoid(FutureFood.modRL(name), typeClass);
  }

  public static class Block {
    public static final BlockCapability<IUnlimitedLinkStorage, Void> UNLIMITED_LINK_STORAGE =
      createVoid("unlimited_link_storage", IUnlimitedLinkStorage.class);
  }
}

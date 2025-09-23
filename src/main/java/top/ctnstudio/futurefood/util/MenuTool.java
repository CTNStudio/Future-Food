package top.ctnstudio.futurefood.util;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import top.ctnstudio.futurefood.common.payloads.OpenMenuData;

public class MenuTool {
  /// 打开菜单GUI
  public static void openMenu(OpenMenuData data, IPayloadContext context, MenuProvider menuProvider) {
    context.enqueueWork(() -> {
      Player player = context.player();

      if (!(player instanceof ServerPlayer serverPlayer)) {
        return;
      }
      ItemStack stack = player.isCreative() ? data.carried() : player.containerMenu.getCarried();
      player.containerMenu.setCarried(ItemStack.EMPTY);
      player.openMenu(menuProvider);

      if (stack.isEmpty()) {
        return;
      }
      player.containerMenu.setCarried(stack);
    });
  }
}

package top.ctnstudio.futurefood.util;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public class ModUtil {
  /**
   * 快速发送到UI
   */
  public static void sendOverlayMessage(String text, Object... args) {
    Minecraft.getInstance().gui.setOverlayMessage(Component.translatable(text, args), false);
  }
}

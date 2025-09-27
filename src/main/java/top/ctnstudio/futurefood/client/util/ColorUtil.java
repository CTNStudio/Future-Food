package top.ctnstudio.futurefood.client.util;

import net.minecraft.network.chat.TextColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ColorUtil {
  public static int rgbColor(String color) {
    return TextColor.parseColor(color).getOrThrow().getValue();
  }

  public static int rgbaColor(String color) {
    return parseColorTo16Bit(color);
  }

  public static int parseColorTo16Bit(String colorString) {
    if (!colorString.startsWith("#") || colorString.length() != 9) {
      throw new IllegalArgumentException("Invalid color format, expected #RRGGBBAA");
    }

    try {
      String hex = colorString.substring(1);
      int r = Integer.parseInt(hex.substring(0, 2), 16);
      int g = Integer.parseInt(hex.substring(2, 4), 16);
      int b = Integer.parseInt(hex.substring(4, 6), 16);
      int a = Integer.parseInt(hex.substring(6, 8), 16);

      // 转换为16位格式 (RGBA -> 4444)
      int r16 = (r >> 4) & 0xF;
      int g16 = (g >> 4) & 0xF;
      int b16 = (b >> 4) & 0xF;
      int a16 = (a >> 4) & 0xF;

      return (r16 << 12) | (g16 << 8) | (b16 << 4) | a16;
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("Invalid hex color format: " + colorString);
    }
  }

  /**
   * 浮点颜色值转整数颜色值
   */
  public static int colorValue(float colorValue) {
    return Math.max(0, Math.min(255, (int) (colorValue * 255.0f)));
  }

  /**
   * 整数颜色值转浮点颜色值
   */
  public static float colorValue(int colorValue) {
    return Math.max(0.0f, (Math.min(1.0f, colorValue / 255.0f)));
  }
}

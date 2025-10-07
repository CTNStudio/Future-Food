package top.ctnstudio.futurefood.util;

public class TextUtil {
  /**
   * 将长整型数值转换为带单位的字符串表示
   *
   * @param value 需要格式化的长整型数值
   * @return 格式化后的带单位字符串
   */
  public static String getDigitalText(long value) {
    int valueTextLength = String.valueOf(Math.abs(value)).length();
    if (valueTextLength >= 12) {
      return formatNumber(value / 1000000000000.0, "T");
    } else if (valueTextLength >= 10) {
      return formatNumber(value / 1000000000.0, "G");
    } else if (valueTextLength >= 7) {
      return formatNumber(value / 1000000.0, "M");
    } else if (valueTextLength >= 4) {
      return formatNumber(value / 1000.0, "K");
    }
    return String.valueOf(value);
  }

  /**
   * 格式化数字并添加单位
   *
   * @param value 需要格式化的数值
   * @param unit  要添加的单位字符串
   * @return 格式化后的带单位字符串
   */
  public static String formatNumber(double value, String unit) {
    if (Math.abs(value - Math.round(value)) < 1e-10) {
      return String.format("%.0f%s", value, unit);
    }
    String formatted = String.format("%.2f", value);
    formatted = formatted.replaceAll("\\.?0+$", "");
    return formatted + " " + unit;
  }

  /**
   * 将游戏刻度转换为时间格式
   * 每20刻度等于1秒
   *
   * @param ticks 游戏刻度数
   * @return 格式化后的时间字符串
   */
  public static String formatGameTime(long ticks) {
    long seconds = ticks / 20;
    long minutes = seconds / 60;
    long hours = minutes / 60;

    if (hours > 0) {
      return String.format("%dh %dm %ds", hours, minutes % 60, seconds % 60);
    } else if (minutes > 0) {
      return String.format("%dm %ds", minutes, seconds % 60);
    } else {
      return String.format("%ds", seconds);
    }
  }

}

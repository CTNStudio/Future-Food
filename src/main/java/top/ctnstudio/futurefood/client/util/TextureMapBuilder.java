package top.ctnstudio.futurefood.client.util;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 纹理映射构建器类，用于创建和管理纹理映射关系
 */
public class TextureMapBuilder {
  private static final String EMPTY_STRING = "";
  private static final float ROUNDING_FACTOR = 100.0f;
  private final Map<Float, String> textureMap;
  private final int maxCount;
  private final float correct;

  /**
   * 默认构造函数，创建最大数量为64的纹理映射构建器
   */
  public TextureMapBuilder() {
    this(64);
  }

  /**
   * 构造函数，创建指定最大数量的纹理映射构建器
   *
   * @param maxCount 纹理映射的最大数量，必须大于0
   */
  public TextureMapBuilder(int maxCount) {
    if (maxCount <= 0) {
      throw new IllegalArgumentException("maxCount must be greater than 0");
    }

    this.textureMap = new LinkedHashMap<>();
    float correctValue = 1.0f;
    for (int i = 0, j = String.valueOf(maxCount).length(); i < j; i++) {
      correctValue *= 0.1f;
    }
    this.correct = correctValue;

    for (int i = 0; i < maxCount; i++) {
      textureMap.put(round(i * correctValue), EMPTY_STRING);
    }
    this.maxCount = maxCount;
  }

  /**
   * 四舍五入到两位小数
   *
   * @param value 需要四舍五入的值
   * @return 四舍五入后的值
   */
  public static float round(float value) {
    return Math.round(value * ROUNDING_FACTOR) / ROUNDING_FACTOR;
  }

  /**
   * 根据名称数组均匀分配纹理映射
   *
   * @param names 名称数组，不能为空且长度必须大于0
   * @return 纹理映射构建器实例
   * @throws IllegalArgumentException 当names为空或长度为0时抛出
   */
  public TextureMapBuilder sharing(String[] names) {
    validateNamesArray(names);

    int count = (int) round((float) maxCount / names.length);
    // 防止count为0导致的除零异常
    if (count == 0) {
      count = 1;
    }

    for (int i = 0, j = 0, k = 0; i < maxCount; i++, k++) {
      String name = names[j];
      if (k == count && i > 0) {
        j = Math.min(j + 1, names.length - 1);
        k = 0;
      }
      textureMap.put(round(i * correct), name);
    }
    return this;
  }

  // 提取重复的验证逻辑
  private void validateNamesArray(String[] names) {
    if (names == null || names.length == 0) {
      throw new IllegalArgumentException("names array cannot be null or empty");
    }
  }

  /**
   * 按指定分组数量均匀分配纹理映射
   *
   * @param sharingCount 分组数量，必须大于0
   * @return 纹理映射构建器实例
   * @throws IllegalArgumentException 当sharingCount小于等于0时抛出
   */
  public TextureMapBuilder sharing(int sharingCount) {
    return sharingInternal("", sharingCount);
  }

  /**
   * 按指定分组数量均匀分配纹理映射的内部实现
   *
   * @param prefix       前缀字符串
   * @param sharingCount 分组数量，必须大于0
   * @return 纹理映射构建器实例
   * @throws IllegalArgumentException 当sharingCount小于等于0时抛出
   */
  private TextureMapBuilder sharingInternal(String prefix, int sharingCount) {
    validateSharingCount(sharingCount);

    int count = Math.max(1, Math.round((float) maxCount / sharingCount));

    for (int i = 0, j = 0, k = 0; i < maxCount; i++, k++) {
      if (k == count && i > 0) {
        j = Math.min(j + 1, sharingCount - 1);
        k = 0;
      }
      String name = j != 0 ? prefix + j : "";
      textureMap.put(round(i * correct), name);
    }
    return this;
  }

  private void validateSharingCount(int sharingCount) {
    if (sharingCount <= 0) {
      throw new IllegalArgumentException("sharingCount must be greater than 0");
    }
  }

  /**
   * 按指定分组数量均匀分配纹理映射
   *
   * @param additional   前缀字符串，会添加到分组数字前
   * @param sharingCount 分组数量，必须大于0
   * @return 纹理映射构建器实例
   * @throws IllegalArgumentException 当sharingCount小于等于0时抛出
   */
  public TextureMapBuilder sharing(String additional, int sharingCount) {
    if (additional == null) {
      additional = "";
    }
    return sharingInternal(additional, sharingCount);
  }

  /**
   * 在去除头尾的区域内将纹理均匀分配给指定的名称数组
   *
   * @param names 名称数组
   * @return 纹理映射构建器实例
   */
  public TextureMapBuilder sharingWithoutHeadAndTail(String[] names) {
    validateNamesArray(names);

    if (maxCount <= 2) {
      return this;
    }

    return sharing(1, maxCount - 2, names);
  }

  /**
   * 在指定区域内将纹理均匀分配给指定的名称数组
   *
   * @param startIndex 起始索引
   * @param endIndex   结束索引
   * @param names      名称数组
   * @return 纹理映射构建器实例
   */
  public TextureMapBuilder sharing(int startIndex, int endIndex, String[] names) {
    validateNamesArray(names);
    validateIndexRange(startIndex, endIndex);

    int range = endIndex - startIndex + 1;
    int count = Math.max(1, Math.round((float) range / names.length));

    for (int i = startIndex, j = 1, k = 0; i <= endIndex; i++, k++) {
      String name = names[j];
      if (k == count) {
        j++;
        k = 0;
      }
      textureMap.put(round(i * correct), name);
    }
    return this;
  }

  private void validateIndexRange(int startIndex, int endIndex) {
    if (startIndex < 0 || endIndex >= maxCount || startIndex > endIndex) {
      throw new IllegalArgumentException("startIndex and endIndex must be valid range");
    }
  }

  /**
   * 在去除头尾的区域内将纹理均匀分配给指定数量的数字分组
   *
   * @param sharingCount 分组数量
   * @return 纹理映射构建器实例
   */
  public TextureMapBuilder sharingWithoutHeadAndTail(int sharingCount) {
    return sharingWithoutHeadAndTail("", sharingCount);
  }

  /**
   * 在去除头尾的区域内将纹理均匀分配给指定数量的数字分组，并添加前缀
   *
   * @param additional   前缀字符串，会添加到分组数字前
   * @param sharingCount 分组数量
   * @return 纹理映射构建器实例
   */
  public TextureMapBuilder sharingWithoutHeadAndTail(String additional, int sharingCount) {
    if (additional == null) {
      additional = "";
    }

    if (maxCount <= 2) {
      return this;
    }

    return sharing(1, maxCount - 2, additional, sharingCount);
  }

  /**
   * 在指定区域内将纹理均匀分配给指定数量的数字分组，并添加前缀
   *
   * @param startIndex   起始索引
   * @param endIndex     结束索引
   * @param additional   前缀字符串，会添加到分组数字前
   * @param sharingCount 分组数量
   * @return 纹理映射构建器实例
   */
  public TextureMapBuilder sharing(int startIndex, int endIndex, String additional, int sharingCount) {
    if (additional == null) {
      additional = "";
    }
    return sharingInternal(startIndex, endIndex, additional, sharingCount);
  }

  /**
   * 在指定区域内将纹理均匀分配给指定数量的数字分组
   *
   * @param startIndex 起始索引
   * @param endIndex   结束索引
   * @param prefix     前缀字符串
   * @param groupCount 分组数量
   * @return 纹理映射构建器实例
   */
  private TextureMapBuilder sharingInternal(int startIndex, int endIndex, String prefix, int groupCount) {
    validateSharingCount(groupCount);
    validateIndexRange(startIndex, endIndex);

    int range = endIndex - startIndex + 1;
    int count = Math.max(1, Math.round((float) range / groupCount));
    for (int i = startIndex, j = 1, k = 0; i <= endIndex; i++, k++) {
      if (k == count) {
        j++;
        k = 0;
      }
      String name = prefix + j;
      textureMap.put(round(i * correct), name);
    }
    return this;
  }

  /**
   * 在指定区域内将纹理均匀分配给指定数量的数字分组
   *
   * @param startIndex   起始索引
   * @param endIndex     结束索引
   * @param sharingCount 分组数量
   * @return 纹理映射构建器实例
   */
  public TextureMapBuilder sharing(int startIndex, int endIndex, int sharingCount) {
    return sharingInternal(startIndex, endIndex, "", sharingCount);
  }

  /**
   * 设置第一个纹理映射项
   *
   * @param name 纹理名称
   * @return 纹理映射构建器实例
   */
  public TextureMapBuilder head(String name) {
    textureMap.put(0.0f, name);
    return this;
  }

  /**
   * 设置最后一个纹理映射项
   *
   * @param name 纹理名称
   * @return 纹理映射构建器实例
   */
  public TextureMapBuilder tail(String name) {
    textureMap.put(round((maxCount - 1) * correct), name);
    return this;
  }

  /**
   * 在指定索引范围内设置纹理映射
   *
   * @param index1 起始索引
   * @param index2 结束索引
   * @param name   纹理名称
   * @return 纹理映射构建器实例
   */
  public TextureMapBuilder set(float index1, float index2, String name) {
    if (index1 > index2) {
      float temp = index1;
      index1 = index2;
      index2 = temp;
    }

    // 在指定范围内设置纹理映射，避免浮点数累积误差
    int start = Math.round(index1 / correct);
    int end = Math.round(index2 / correct);
    for (int i = start; i < end; i++) {
      textureMap.put(round(i * correct), name);
    }
    return this;
  }

  /**
   * 在指定索引位置设置纹理映射
   *
   * @param index 索引位置
   * @param name  纹理名称
   * @return 纹理映射构建器实例
   */
  public TextureMapBuilder set(int index, String name) {
    if (index < 0) {
      throw new IllegalArgumentException("index must be greater than or equal to 0");
    }
    return set(round(index * correct), name);
  }

  /**
   * 在指定索引位置设置纹理映射
   *
   * @param index 索引位置
   * @param name  纹理名称
   * @return 纹理映射构建器实例
   */
  public TextureMapBuilder set(float index, String name) {
    textureMap.put(index, name);
    return this;
  }

  /**
   * 获取构建完成的纹理映射
   *
   * @return 纹理映射Map
   */
  public Map<Float, String> builder() {
    return textureMap;
  }
}

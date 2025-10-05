package top.ctnstudio.futurefood.client.gui.widget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageWidget;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

/**
 * 一个基于图像的进度条控件抽象类，支持水平和垂直方向的渲染。
 * <p>
 * 该类继承自 {@link ImageWidget.Sprite}，用于在 GUI 中显示一个可变长度的进度条，
 * 并提供工具提示功能。子类可以实现不同的方向（水平或垂直）。
 */
public abstract class ImageProgressBar extends ImageWidget.Sprite {
  public String getTooltipKey() {
    return tooltipKey;
  }

  private int value;
  private int maxValue;
  private final String tooltipKey;

  /**
   * 构造一个新的 ImageProgressBar 实例。
   *
   * @param x          控件的 X 坐标
   * @param y          控件的 Y 坐标
   * @param width      控件的宽度
   * @param height     控件的高度
   * @param value      当前进度值
   * @param maxValue   最大进度值
   * @param texture    进度条使用的纹理资源位置
   * @param tooltipKey 工具提示翻译键
   */
  private ImageProgressBar(int x, int y,
                           int width, int height,
                           int value, int maxValue,
                           ResourceLocation texture, String tooltipKey) {
    super(x, y, width, height, texture);
    this.setValue(value);
    this.setMaxValue(maxValue);
    this.tooltipKey = tooltipKey;
  }

  /**
   * 设置最大进度值。
   *
   * @param maxValue 新的最大进度值
   */
  public void setMaxValue(int maxValue) {
    this.maxValue = maxValue;
  }

  /**
   * 同时设置当前值和最大值。
   *
   * @param energy  当前进度值
   * @param maxTick 最大进度值
   */
  public void setValue(int energy, int maxTick) {
    this.setValue(energy);
    this.setMaxValue(maxTick);
  }

  /**
   * 获取当前进度值。
   *
   * @return 当前进度值
   */
  public int getValue() {
    return this.value;
  }

  /**
   * 获取最大进度值。
   *
   * @return 最大进度值
   */
  public int getMaxValue() {
    return maxValue;
  }

  /**
   * 设置当前进度值。
   *
   * @param value 新的进度值
   */
  public void setValue(int value) {
    this.value = value;
  }

  /**
   * 渲染控件及其工具提示。
   *
   * @param guiGraphics GUI 图形上下文
   * @param mouseX      鼠标 X 坐标
   * @param mouseY      鼠标 Y 坐标
   * @param partialTick 部分 tick 时间（用于动画插值）
   */
  @Override
  public void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
    if (this.getMaxValue() > 0) {
      renderTexture(guiGraphics);
    }
    if (getMaxValue() > 0 && isHovered()) {
      renderWidgetTooltip(guiGraphics, mouseX, mouseY);
    }
  }

  protected abstract void renderTexture(@NotNull GuiGraphics guiGraphics);

  /**
   * 获取要渲染的进度值，确保不小于 0。
   *
   * @return 渲染用的进度值
   */
  public int getRenderValue() {
    return Math.min(Math.max(0, this.getValue()), this.getMaxValue());
  }

  /**
   * 渲染控件的工具提示。
   *
   * @param guiGraphics GUI 图形上下文
   * @param mouseX      鼠标 X 坐标
   * @param mouseY      鼠标 Y 坐标
   */
  protected void renderWidgetTooltip(GuiGraphics guiGraphics, int mouseX, int mouseY) {
    guiGraphics.renderTooltip(Minecraft.getInstance().font, getTooltipComponent(), mouseX, mouseY);
  }

  /**
   * 获取工具提示组件。
   *
   * @return 工具提示组件
   */
  public @NotNull Component getTooltipComponent() {
    return Component.translatable(getTooltipKey(), getRenderValue(), getMaxValue());
  }

  /**
   * 获取纹理资源位置。
   *
   * @return 纹理资源位置
   */
  public ResourceLocation getTexture() {
    return this.sprite;
  }

  /**
   * 水平方向的进度条实现。
   * <p>
   * 该类重写了部分方法以适应水平方向的绘制逻辑。
   */
  public static class Horizontal extends ImageProgressBar {
    public final boolean isToLeft;
    /**
     * 构造一个新的水平进度条实例。
     *
     * @param x          控件的 X 坐标
     * @param y          控件的 Y 坐标
     * @param width      控件的宽度
     * @param height     控件的高度
     * @param value      当前进度值
     * @param maxValue   最大进度值
     * @param texture    纹理资源位置
     * @param tooltipKey 工具提示翻译键
     */
    public Horizontal(int x, int y, int width, int height, int value, int maxValue, ResourceLocation texture, String tooltipKey, boolean isToLeft) {
      super(x, y, width, height, value, maxValue, texture, tooltipKey);
      this.isToLeft = isToLeft;
    }

    @Override
    protected void renderTexture(@NotNull GuiGraphics guiGraphics) {
      int value = (int) ((getRenderValue() / (float) this.getMaxValue()) * this.getWidth());
      int uWidth = isToLeft ? value : this.getWidth() - value;
      int vHeight = getHeight();
      int xPosition = isToLeft ? 0 : uWidth;
      int yPosition = 0;
      int x = isToLeft ? this.getX() + xPosition : this.getX();
      int y = this.getY() + yPosition;

      guiGraphics.blitSprite(this.sprite,
              this.getWidth(), this.getHeight(),
              isToLeft ? xPosition : 0, 0,
              x, y,
              uWidth, vHeight);
    }
  }

  /**
   * 垂直方向的进度条实现。
   * <p>
   * 该类重写了部分方法以适应垂直方向的绘制逻辑。
   */
  public static class Vertical extends ImageProgressBar {
    public final boolean isToTop;
    /**
     * 构造一个新的垂直进度条实例。
     *
     * @param x          控件的 X 坐标
     * @param y          控件的 Y 坐标
     * @param width      控件的宽度
     * @param height     控件的高度
     * @param value      当前进度值
     * @param maxValue   最大进度值
     * @param texture    纹理资源位置
     * @param tooltipKey 工具提示翻译键
     */
    public Vertical(int x, int y, int width, int height, int value, int maxValue, ResourceLocation texture, String tooltipKey, boolean isToTop) {
      super(x, y, width, height, value, maxValue, texture, tooltipKey);
      this.isToTop = isToTop;
    }

    @Override
    protected void renderTexture(@NotNull GuiGraphics guiGraphics) {
      int uWidth = getWidth();
      int value = (int) ((getRenderValue() / (float) this.getMaxValue()) * this.getHeight());
      int vHeight = isToTop ? value : this.getHeight() - value;
      int xPosition = 0;
      int yPosition = isToTop ? getHeight() - vHeight : 0;
      int x = this.getX() + xPosition;
      int y = isToTop ? this.getY() + yPosition : this.getY();

      guiGraphics.blitSprite(this.sprite,
              this.getWidth(), this.getHeight(),
              0, yPosition,
              x, y,
              uWidth, vHeight);
    }
  }
}

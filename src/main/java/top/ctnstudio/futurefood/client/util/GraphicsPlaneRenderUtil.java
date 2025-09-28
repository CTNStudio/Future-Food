package top.ctnstudio.futurefood.client.util;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.joml.Matrix4f;

@OnlyIn(Dist.CLIENT)
public class GraphicsPlaneRenderUtil {
  /**
   * 渲染XY平面带边框的等边多边形（通过中心点和边长）
   */
  public static void renderRegularPolygonXYBySideLength(
    PoseStack poseStack, VertexConsumer vertexConsumer, Matrix4f projectionMatrix,
    float borderWidth,
    float centerX, float centerY,
    float sideLength,
    int sides,
    int red, int green, int blue, int alpha) {
    if (sides <= 2) {
      return;
    }

    // 根据边长计算外接圆半径
    // 对于等边多边形，半径 R = (s/2) / sin(π/n)
    // 其中 s 是边长，n 是边数
    float radius = (float) ((sideLength * 0.5f) / Math.sin(Math.PI / sides));

    renderRegularPolygonXYByRadius(poseStack, vertexConsumer, projectionMatrix,
      borderWidth, centerX, centerY, radius, sides, red, green, blue, alpha);
  }

  /**
   * 渲染XY平面带边框的等边多边形（通过中心点和外接圆半径）
   */
  public static void renderRegularPolygonXYByRadius(
    PoseStack poseStack, VertexConsumer vertexConsumer, Matrix4f projectionMatrix,
    float borderWidth,
    float centerX, float centerY,
    float radius,
    int sides,
    int red, int green, int blue, int alpha) {
    if (sides <= 2) {
      return;
    }

    // 计算多边形的顶点
    float[] xPoints = new float[sides];
    float[] yPoints = new float[sides];
    float[] xePoints = new float[sides];
    float[] yePoints = new float[sides];

    for (int i = 0; i < sides; i++) {
      double angle = 2 * Math.PI * i / sides - Math.PI * 0.5f; // 从顶部开始绘制
      float cos = (float) Math.cos(angle);
      float sin = (float) Math.sin(angle);
      xPoints[i] = centerX + radius * cos;
      yPoints[i] = centerY + radius * sin;

      // 使用中心扩散方式计算扩展顶点
      xePoints[i] = centerX + (radius + borderWidth) * cos;
      yePoints[i] = centerY + (radius + borderWidth) * sin;
    }

    renderRegularPolygon(poseStack, vertexConsumer, projectionMatrix,
      xPoints, yPoints,
      xePoints, yePoints,
      red, green, blue, alpha);
  }

  public static void renderRegularPolygon(
    PoseStack poseStack, VertexConsumer vertexConsumer, Matrix4f projectionMatrix,
    float[] xPoints, float[] yPoints,
    float[] xePoints, float[] yePoints,
    int red, int green, int blue, int alpha) {

    int sides = xPoints.length;
    if (sides <= 2) {
      return;
    }

    // 渲染每个边的外部边框部分
    for (int i = 0; i < sides; i++) {
      int next = (i + 1) % sides;

      renderQuadWithBorder(poseStack, vertexConsumer, projectionMatrix,
        xPoints[i], yPoints[i],
        xPoints[next], yPoints[next],
        xePoints[next], yePoints[next],
        xePoints[i], yePoints[i],
        red, green, blue, alpha);
    }
  }

  /**
   * 渲染带边框的四边形
   */
  public static void renderQuadWithBorder(
    PoseStack poseStack, VertexConsumer vertexConsumer, Matrix4f projectionMatrix,
    float v1x, float v1y,
    float v2x, float v2y,
    float v3x, float v3y,
    float v4x, float v4y,
    int red, int green, int blue, int alpha) {
    GraphicsRenderUtil.renderQuadWithBorder(poseStack, vertexConsumer, projectionMatrix,
      v1x, v1y, 0,
      v2x, v2y, 0,
      v3x, v3y, 0,
      v4x, v4y, 0,
      red, green, blue, alpha);
  }
}

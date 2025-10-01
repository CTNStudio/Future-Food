package top.ctnstudio.futurefood.client.util;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.Direction;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.joml.Matrix4f;

@OnlyIn(Dist.CLIENT)
public class GraphicsPlaneRenderUtil {

  public static void renderTextures(PoseStack pose, VertexConsumer buffer,
                                    float size,
                                    float x, float y, float z,
                                    float u, float v, float u1, float v1,
                                    int r, int g, int b, int a) {
    int fullBright = LightTexture.FULL_BRIGHT;
    PoseStack.Pose last = pose.last();
    float s = size / 2;
    renderVertex(last, buffer, x + s, y - s, z, r, g, b, a, u1, v1, fullBright);
    renderVertex(last, buffer, x + s, y + s, z, r, g, b, a, u1, v, fullBright);
    renderVertex(last, buffer, x - s, y + s, z, r, g, b, a, u, v, fullBright);
    renderVertex(last, buffer, x - s, y - s, z, r, g, b, a, u, v1, fullBright);
  }

  private static void renderVertex(PoseStack.Pose pose, VertexConsumer buffer,
                                   float x, float y, float z,
                                   int r, int g, int b, int a,
                                   float u, float v, int packedLight) {
    buffer.addVertex(pose, x, y, z)
      .setUv(u, v)
      .setColor(r, g, b, a)
      .setLight(packedLight);
  }

  public static void renderQuads(PoseStack pose, VertexConsumer vertex,
                                 boolean isReverse, float size, int[] uv, int[] uv1) {
    PoseStack.Pose last = pose.last();
    for (Direction direction : Direction.values()) {
      renderNoodles(last, vertex, direction, size, isReverse, uv[0], uv[1], uv1[0], uv1[1]);
    }
  }

  public static void renderQuads(PoseStack pose, VertexConsumer[] vertex,
                                 boolean isReverse, float size, int[][] uv, int[][] uv1) {
    PoseStack.Pose last = pose.last();
    int index = 0;
    for (Direction direction : Direction.values()) {
      renderNoodles(last, vertex[index], direction, size, isReverse, uv[index][0], uv[index][1], uv1[index][0], uv1[index][1]);
      index++;
    }
  }

  public static void renderNoodles(PoseStack.Pose last, VertexConsumer vertex, Direction direction,
                                   float size, boolean isReverse, int u, int v, int u1, int v1) {
    final float scopeSize = size / 2;
    float v1x = 0, v1y = 0, v1z = 0;
    float v2x = 0, v2y = 0, v2z = 0;
    float v3x = 0, v3y = 0, v3z = 0;
    float v4x = 0, v4y = 0, v4z = 0;
    float nX = 0, nY = 0, nZ = 0;
    switch (direction) {
      case DOWN -> {
        v1x = -scopeSize;
        v1y = -scopeSize;
        v1z = scopeSize;
        v2x = scopeSize;
        v2y = scopeSize;
        v2z = scopeSize;
        v3x = scopeSize;
        v3z = -scopeSize;
        v3y = -scopeSize;
        v4x = -scopeSize;
        v4y = -scopeSize;
        v4z = -scopeSize;
      }
      case UP -> {
        v1x = scopeSize;
        v1y = -scopeSize;
        v1z = -scopeSize;
        v2x = -scopeSize;
        v2y = -scopeSize;
        v2z = -scopeSize;
        v3x = scopeSize;
        v3z = scopeSize;
        v3y = scopeSize;
        v4x = scopeSize;
        v4y = scopeSize;
        v4z = scopeSize;
      }
      case NORTH, SOUTH, EAST, WEST -> {
        v1x = scopeSize;
        v1y = scopeSize;
        v1z = scopeSize;
        v2x = scopeSize;
        v2y = -scopeSize;
        v2z = -scopeSize;
        v3x = -scopeSize;
        v3z = -scopeSize;
        v3y = -scopeSize;
        v4x = -scopeSize;
        v4y = -scopeSize;
        v4z = scopeSize;
      }
    }
    switch (direction) {
      case DOWN -> nY = isReverse ? -1 : 1;
      case UP -> nY = isReverse ? 1 : -1;
      case NORTH -> nZ = isReverse ? -1 : 1;
      case SOUTH -> nZ = isReverse ? 1 : -1;
      case WEST -> nX = isReverse ? 1 : -1;
      case EAST -> nX = isReverse ? -1 : 1;
    }
    vertex.addVertex(last, v1x, v1y, v1z).setNormal(last, nX, nY, nZ).setUv(u, v).setUv1(u, v).setUv2(u1, v1).setColor(0xFFFFFFFF);
    vertex.addVertex(last, v2x, v2y, v2z).setNormal(last, nX, nY, nZ).setUv(u, v).setUv1(u, v).setUv2(u1, v1).setColor(0xFFFFFFFF);
    vertex.addVertex(last, v3x, v3y, v3z).setNormal(last, nX, nY, nZ).setUv(u, v).setUv1(u, v).setUv2(u1, v1).setColor(0xFFFFFFFF);
    vertex.addVertex(last, v4x, v4y, v4z).setNormal(last, nX, nY, nZ).setUv(u, v).setUv1(u, v).setUv2(u1, v1).setColor(0xFFFFFFFF);
  }

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

package top.ctnstudio.futurefood.client.util;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.joml.Matrix4f;

@OnlyIn(Dist.CLIENT)
public class GraphicsRenderUtil {
  /**
   * 渲染带边框的等边多边形（外部边框版本）
   */
  public static void renderRegularPolygonWithBorder(
    PoseStack poseStack, VertexConsumer vertexConsumer, Matrix4f projectionMatrix,
    float borderWidth,
    float[] xPoints, float[] yPoints, float[] zPoints,
    int red, int green, int blue, int alpha) {

    int sides = xPoints.length;

    // 计算多边形的法向量（基于前三个顶点）
    float edge1x = xPoints[1] - xPoints[0];
    float edge1y = yPoints[1] - yPoints[0];
    float edge1z = zPoints[1] - zPoints[0];
    float edge2x = xPoints[2] - xPoints[0];
    float edge2y = yPoints[2] - yPoints[0];
    float edge2z = zPoints[2] - zPoints[0];

    // 叉积计算法向量
    float normalX = edge1y * edge2z - edge1z * edge2y;
    float normalY = edge1z * edge2x - edge1x * edge2z;
    float normalZ = edge1x * edge2y - edge1y * edge2x;

    // 归一化法向量
    float normalLength = (float) Math.sqrt(normalX * normalX + normalY * normalY + normalZ * normalZ);
    // 避免除零错误
    if (normalLength > 1e-6f) {
      float normalInvLength = 1.0f / normalLength;
      normalX *= normalInvLength;
      normalY *= normalInvLength;
      normalZ *= normalInvLength;
    } else {
      // 默认法向量（如果多边形退化）
      normalX = 0.0f;
      normalY = 1.0f;
      normalZ = 0.0f;
    }

    // 计算扩展的顶点
    float[] xePoints = new float[sides];
    float[] yePoints = new float[sides];
    float[] zePoints = new float[sides];

    for (int i = 0; i < sides; i++) {
      xePoints[i] = xPoints[i] + borderWidth * normalX;
      yePoints[i] = yPoints[i] + borderWidth * normalY;
      zePoints[i] = zPoints[i] + borderWidth * normalZ;
    }

    renderRegularPolygon(poseStack, vertexConsumer, projectionMatrix,
      xPoints, yPoints, zPoints,
      xePoints, yePoints, zePoints,
      red, green, blue, alpha);
  }

  public static void renderRegularPolygon(
    PoseStack poseStack, VertexConsumer vertexConsumer, Matrix4f projectionMatrix,
    float[] xPoints, float[] yPoints, float[] zPoints,
    float[] xePoints, float[] yePoints, float[] zePoints,
    int red, int green, int blue, int alpha) {

    int sides = xPoints.length;

    // 渲染每个边的外部边框部分
    for (int i = 0; i < sides; i++) {
      int next = (i + 1) % sides;

      renderQuadWithBorder(poseStack, vertexConsumer, projectionMatrix,
        xPoints[i], yPoints[i], zPoints[i],
        xPoints[next], yPoints[next], zPoints[next],
        xePoints[next], yePoints[next], zePoints[next],
        xePoints[i], yePoints[i], zePoints[i],
        red, green, blue, alpha);
    }
  }

  /**
   * 渲染带边框的四边形
   */
  public static void renderQuadWithBorder(
    PoseStack poseStack, VertexConsumer vertexConsumer, Matrix4f projectionMatrix,
    float v1x, float v1y, float v1z,
    float v2x, float v2y, float v2z,
    float v3x, float v3y, float v3z,
    float v4x, float v4y, float v4z,
    int red, int green, int blue, int alpha) {
    // 添加四个顶点构成一个四边形
    poseStack.pushPose();
    vertexConsumer.addVertex(projectionMatrix, v1x, v1y, v1z).setColor(red, green, blue, alpha);
    vertexConsumer.addVertex(projectionMatrix, v2x, v2y, v2z).setColor(red, green, blue, alpha);
    vertexConsumer.addVertex(projectionMatrix, v3x, v3y, v3z).setColor(red, green, blue, alpha);
    vertexConsumer.addVertex(projectionMatrix, v4x, v4y, v4z).setColor(red, green, blue, alpha);
    poseStack.popPose();
  }

  /**
   * 渲染带边框的矩形（外部边框版本）
   */
  public static void renderSquareWithBorder(
    PoseStack poseStack, VertexConsumer vertexConsumer, Matrix4f projectionMatrix,
    float borderWidth,
    float minX, float minY, float minZ,
    float maxX, float maxY, float maxZ,
    int red, int green, int blue, int alpha) {
    // 定义矩形的四个顶点
    float v1x = minX, v1y = minY, v1z = minZ;  // 左下角
    float v2x = maxX, v2y = minY, v2z = minZ;  // 右下角
    float v3x = maxX, v3y = maxY, v3z = maxZ;  // 右上角
    float v4x = minX, v4y = maxY, v4z = maxZ;  // 左上角

    // 计算矩形的法向量（基于前三个顶点）
    float edge1x = v2x - v1x;
    float edge1y = v2y - v1y;
    float edge1z = v2z - v1z;
    float edge2x = v3x - v1x;
    float edge2y = v3y - v1y;
    float edge2z = v3z - v1z;

    // 叉积计算法向量
    float normalX = edge1y * edge2z - edge1z * edge2y;
    float normalY = edge1z * edge2x - edge1x * edge2z;
    float normalZ = edge1x * edge2y - edge1y * edge2x;

    // 归一化法向量
    float normalLength = (float) Math.sqrt(normalX * normalX + normalY * normalY + normalZ * normalZ);
    // 避免除零错误
    if (normalLength > 1e-6f) {
      float normalInvLength = 1.0f / normalLength;
      normalX *= normalInvLength;
      normalY *= normalInvLength;
      normalZ *= normalInvLength;
    } else {
      // 默认法向量（如果矩形退化）
      normalX = 0.0f;
      normalY = 1.0f;
      normalZ = 0.0f;
    }

    // 计算扩展的四个顶点
    float v1eX = v1x + borderWidth * normalX;
    float v1eY = v1y + borderWidth * normalY;
    float v1eZ = v1z + borderWidth * normalZ;
    float v2eX = v2x + borderWidth * normalX;
    float v2eY = v2y + borderWidth * normalY;
    float v2eZ = v2z + borderWidth * normalZ;
    float v3eX = v3x + borderWidth * normalX;
    float v3eY = v3y + borderWidth * normalY;
    float v3eZ = v3z + borderWidth * normalZ;
    float v4eX = v4x + borderWidth * normalX;
    float v4eY = v4y + borderWidth * normalY;
    float v4eZ = v4z + borderWidth * normalZ;

    renderSquareWithBorder(poseStack, vertexConsumer, projectionMatrix,
      v1x, v1y, v1z,
      v2x, v2y, v2z,
      v3x, v3y, v3z,
      v4x, v4y, v4z,
      v1eX, v1eY, v1eZ,
      v2eX, v2eY, v2eZ,
      v3eX, v3eY, v3eZ,
      v4eX, v4eY, v4eZ,
      red, green, blue, alpha);
  }

  private static void renderSquareWithBorder(
    PoseStack poseStack, VertexConsumer vertexConsumer, Matrix4f projectionMatrix,
    float v1x, float v1y, float v1z,
    float v2x, float v2y, float v2z,
    float v3x, float v3y, float v3z,
    float v4x, float v4y, float v4z,
    float v1eX, float v1eY, float v1eZ,
    float v2eX, float v2eY, float v2eZ,
    float v3eX, float v3eY, float v3eZ,
    float v4eX, float v4eY, float v4eZ,
    int red, int green, int blue, int alpha) {
    // 渲染四个外部边框部分

    renderQuadWithBorder(poseStack, vertexConsumer, projectionMatrix,
      v1x, v1y, v1z,
      v2x, v2y, v2z,
      v2eX, v2eY, v2eZ,
      v1eX, v1eY, v1eZ,
      red, green, blue, alpha);

    renderQuadWithBorder(poseStack, vertexConsumer, projectionMatrix,
      v2x, v2y, v2z,
      v3x, v3y, v3z,
      v3eX, v3eY, v3eZ,
      v2eX, v2eY, v2eZ,
      red, green, blue, alpha);

    renderQuadWithBorder(poseStack, vertexConsumer, projectionMatrix,
      v3x, v3y, v3z,
      v4x, v4y, v4z,
      v4eX, v4eY, v4eZ,
      v3eX, v3eY, v3eZ,
      red, green, blue, alpha);

    renderQuadWithBorder(poseStack, vertexConsumer, projectionMatrix,
      v4x, v4y, v4z,
      v1x, v1y, v1z,
      v1eX, v1eY, v1eZ,
      v4eX, v4eY, v4eZ,
      red, green, blue, alpha);
  }

  /**
   * 渲染带边框的三角形（外部边框版本）
   */
  public static void renderTriangleWithBorder(
    PoseStack poseStack, VertexConsumer vertexConsumer, Matrix4f projectionMatrix,
    float borderWidth,
    float v1x, float v1y, float v1z,
    float v2x, float v2y, float v2z,
    int red, int green, int blue, int alpha) {
    // 计算等边三角形第三个顶点（在3D空间中）
    float dx = v2x - v1x;
    float dy = v2y - v1y;
    float dz = v2z - v1z;
    float sideLengthSquared = dx * dx + dy * dy + dz * dz;
    float sideLength = (float) Math.sqrt(sideLengthSquared);
    float height = (float) (Math.sqrt(3) * 0.5f * sideLength);
    // 计算v1到v2的中点
    float midX = (v1x + v2x) * 0.5f;
    float midY = (v1y + v2y) * 0.5f;
    float midZ = (v1z + v2z) * 0.5f;

    // 在3D空间中计算垂直于v1v2边的向量
    // 首先创建一个不与v1v2向量平行的辅助向量
    float auxX, auxY, auxZ;
    if (Math.abs(dx) > Math.abs(dz)) {
      auxX = 0.0f;
      auxY = 1.0f;
      auxZ = 0.0f;
    } else {
      auxX = 1.0f;
      auxY = 0.0f;
      auxZ = 0.0f;
    }

    // 计算v1v2向量与辅助向量的叉积，得到垂直于v1v2的向量
    float perpX = dy * auxZ - dz * auxY;
    float perpY = dz * auxX - dx * auxZ;
    float perpZ = dx * auxY - dy * auxX;

    // 归一化
    float perpLength = (float) Math.sqrt(perpX * perpX + perpY * perpY + perpZ * perpZ);
    float perpInvLength = 1.0f / perpLength;
    perpX *= perpInvLength;
    perpY *= perpInvLength;
    perpZ *= perpInvLength;

    // 计算第三个顶点
    float v3x = midX + perpX * height;
    float v3y = midY + perpY * height;
    float v3z = midZ + perpZ * height;

    renderTriangleWithBorder(poseStack, vertexConsumer, projectionMatrix, borderWidth,
      v1x, v1y, v1z,
      v2x, v2y, v2z,
      v3x, v3y, v3z,
      red, green, blue, alpha);
  }

  public static void renderTriangleWithBorder(
    PoseStack poseStack, VertexConsumer vertexConsumer, Matrix4f projectionMatrix,
    float borderWidth,
    float v1x, float v1y, float v1z,
    float v2x, float v2y, float v2z,
    float v3x, float v3y, float v3z,
    int red, int green, int blue, int alpha) {
    // 计算每个边的法向量并扩展顶点
    float v1eX, v1eY, v1eZ, v2eX, v2eY, v2eZ, v3eX, v3eY, v3eZ;

    // 计算三角形法向量（右手定则）
    float edge1x = v2x - v1x;
    float edge1y = v2y - v1y;
    float edge1z = v2z - v1z;
    float edge2x = v3x - v1x;
    float edge2y = v3y - v1y;
    float edge2z = v3z - v1z;

    // 叉积计算法向量
    float normalX = edge1y * edge2z - edge1z * edge2y;
    float normalY = edge1z * edge2x - edge1x * edge2z;
    float normalZ = edge1x * edge2y - edge1y * edge2x;

    // 归一化法向量
    float normalLength = (float) Math.sqrt(normalX * normalX + normalY * normalY + normalZ * normalZ);
    float normalInvLength = 1.0f / normalLength;
    normalX *= normalInvLength;
    normalY *= normalInvLength;
    normalZ *= normalInvLength;

    // 扩展顶点（在3D空间中沿法向量方向扩展）
    v1eX = v1x + borderWidth * normalX;
    v1eY = v1y + borderWidth * normalY;
    v1eZ = v1z + borderWidth * normalZ;
    v2eX = v2x + borderWidth * normalX;
    v2eY = v2y + borderWidth * normalY;
    v2eZ = v2z + borderWidth * normalZ;
    v3eX = v3x + borderWidth * normalX;
    v3eY = v3y + borderWidth * normalY;
    v3eZ = v3z + borderWidth * normalZ;

    renderTriangleWithBorder(poseStack, vertexConsumer, projectionMatrix,
      v1x, v1y, v1z,
      v2x, v2y, v2z,
      v3x, v3y, v3z,
      v1eX, v1eY, v1eZ,
      v2eX, v2eY, v2eZ,
      v3eX, v3eY, v3eZ,
      red, green, blue, alpha);
  }

  public static void renderTriangleWithBorder(
    PoseStack poseStack, VertexConsumer vertexConsumer, Matrix4f projectionMatrix,
    float v1x, float v1y, float v1z,
    float v2x, float v2y, float v2z,
    float v3x, float v3y, float v3z,
    float v1eX, float v1eY, float v1eZ,
    float v2eX, float v2eY, float v2eZ,
    float v3eX, float v3eY, float v3eZ,
    int red, int green, int blue, int alpha) {

    renderQuadWithBorder(poseStack, vertexConsumer, projectionMatrix,
      v1x, v1y, v1z,
      v2x, v2y, v2z,
      v2eX, v2eY, v2eZ,
      v1eX, v1eY, v1eZ,
      red, green, blue, alpha);
    renderQuadWithBorder(poseStack, vertexConsumer, projectionMatrix,
      v2x, v2y, v2z,
      v3x, v3y, v3z,
      v3eX, v3eY, v3eZ,
      v2eX, v2eY, v2eZ,
      red, green, blue, alpha);
    renderQuadWithBorder(poseStack, vertexConsumer, projectionMatrix,
      v1x, v1y, v1z,
      v3x, v3y, v3z,
      v3eX, v3eY, v3eZ,
      v1eX, v1eY, v1eZ,
      red, green, blue, alpha);
  }

  /**
   * 渲染三角形
   */
  public static void renderTriangle(
    PoseStack poseStack, VertexConsumer vertexConsumer, Matrix4f projectionMatrix,
    float v1x, float v1y, float v1z,
    float v2x, float v2y, float v2z,
    float v3x, float v3y, float v3z,
    int red, int green, int blue, int alpha) {
    poseStack.pushPose();
    vertexConsumer.addVertex(projectionMatrix, v1x, v1y, v1z).setColor(red, green, blue, alpha);
    vertexConsumer.addVertex(projectionMatrix, v2x, v2y, v2z).setColor(red, green, blue, alpha);
    vertexConsumer.addVertex(projectionMatrix, v3x, v3y, v3z).setColor(red, green, blue, alpha);
    poseStack.popPose();
  }
}

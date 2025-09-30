package top.ctnstudio.futurefood.client.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.core.BlockPos;
import net.minecraft.util.FastColor.ARGB32;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.common.Tags;
import org.jetbrains.annotations.NotNull;
import top.ctnstudio.futurefood.common.block.QedEntityBlock;
import top.ctnstudio.futurefood.util.ModUtil;

import java.util.Map;
import java.util.function.Predicate;

import static top.ctnstudio.futurefood.client.core.ModRenderType.getHighlighted;
import static top.ctnstudio.futurefood.client.util.ColorUtil.colorValue;
import static top.ctnstudio.futurefood.client.util.ColorUtil.rgbColor;
import static top.ctnstudio.futurefood.client.util.GraphicsPlaneRenderUtil.renderRegularPolygonXYByRadius;
import static top.ctnstudio.futurefood.common.item.CyberWrenchItem.SCOPE;
import static top.ctnstudio.futurefood.datagen.tag.FfBlockTags.UNLIMITED_RECEIVE;

/**
 * 无线链接高亮渲染
 */
@OnlyIn(Dist.CLIENT)
public class HighlightLinksRender implements ModRender {
  private static final HighlightLinksRender INSTANCE = new HighlightLinksRender();

  public static ModRender get() {
    return INSTANCE;
  }

  @Override
  public void levelRender(Minecraft minecraft, ClientLevel level, Frustum frustum, PoseStack pose, Camera camera) {
    final LocalPlayer player = minecraft.player;
    if (level == null || player == null || !player.isAlive() || (
      !player.getMainHandItem().is(Tags.Items.TOOLS_WRENCH) &&
        !player.getItemInHand(InteractionHand.OFF_HAND).is(Tags.Items.TOOLS_WRENCH))) {
      return;
    }
    final BlockPos playerPos = player.getOnPos();

    final Map<BlockPos, BlockState> blockPosList = getBlock(level, frustum, playerPos);
    if (blockPosList.isEmpty()) {
      return;
    }
    RenderSystem.disableDepthTest();
    final var renderBuffers = minecraft.renderBuffers();
    final var buffer = renderBuffers.bufferSource();
    final var cameraPos = camera.getPosition();
    final VertexConsumer consumer = buffer.getBuffer(getHighlighted());

    pose.pushPose();
    pose.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);

    // TODO 完成高亮显示
//    var from = player.getEyePosition();
//    var playerLookAngle = player.getLookAngle();
//    double scope = from.distanceToSqr(blockCenterV3);
//    var to = from.add(playerLookAngle.x * scope,
//      playerLookAngle.y * scope,
//      playerLookAngle.z * scope);
//    getTargetBlockPos(blockPos, from, to)
    for (Map.Entry<BlockPos, BlockState> entry : blockPosList.entrySet()) {
      blockRender(entry, consumer, pose, camera, false);
    }

    pose.popPose();
    buffer.endBatch();

    RenderSystem.disableDepthTest();
  }

  private @NotNull Map<BlockPos, BlockState> getBlock(ClientLevel level, Frustum frustum, BlockPos playerPos) {
    return ModUtil.rangePos(level, playerPos, SCOPE, entry -> {
      BlockPos pos = entry.getKey();
      if (!frustum.isVisible(new AABB(pos))) {
        return false;
      }
      BlockState blockState = level.getBlockState(pos);
      if (blockState.isEmpty()) {
        return false;
      }
      return blockState.is(UNLIMITED_RECEIVE) || blockState.getBlock() instanceof QedEntityBlock;
    });
  }

  private void blockRender(Map.Entry<BlockPos, BlockState> entry, VertexConsumer consumer,
                           PoseStack pose, Camera camera, boolean isSelected) {
    final var blockPos = entry.getKey();
    final var blockState = entry.getValue();
    final var blockCenterV3 = blockPos.getCenter();
    final var x = blockCenterV3.x;
    final var y = blockCenterV3.y;
    final var z = blockCenterV3.z;
    var size = 0.1f;
    var sideLength = 0.35f;
    var alpha = 0.4f;
    var color = rgbColor("#6916af");
    if (isSelected) {
      sideLength = 0.4f;
      alpha = 0.7f;
      color = rgbColor("#a11aff");
    }
    if (blockState.is(UNLIMITED_RECEIVE)) { // 可链接方块

    }

    pose.pushPose();

    pose.translate(x, y, z);
    pose.mulPose(camera.rotation());
    var last = pose.last();
    var matrix4f = last.pose();

    pose.pushPose();

    renderRegularPolygonXYByRadius(pose, consumer, matrix4f, size, 0, 0, sideLength, 6,
      ARGB32.red(color), ARGB32.green(color), ARGB32.blue(color), colorValue(alpha));

    pose.popPose();

    pose.popPose();
  }

  public boolean getTargetBlockPos(BlockPos target, Vec3 from, Vec3 to) {
    return BlockGetter.traverseBlocks(from, to,
      (Predicate<BlockPos>) pos -> pos.equals(target),
      (posPredicate, t) -> posPredicate.test(t) ? true : null,
      a -> false);
  }
}

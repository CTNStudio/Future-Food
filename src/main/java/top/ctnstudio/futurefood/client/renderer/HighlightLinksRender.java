package top.ctnstudio.futurefood.client.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.PoseStack.Pose;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.core.BlockPos;
import net.minecraft.util.FastColor.ARGB32;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent.Stage;
import net.neoforged.neoforge.common.Tags;
import top.ctnstudio.futurefood.common.block.QedEntityBlock;
import top.ctnstudio.futurefood.common.item.CyberWrenchItem;
import top.ctnstudio.futurefood.util.ModUtil;

import java.util.Map;

import static top.ctnstudio.futurefood.client.ModRenderType.getHighlighted;
import static top.ctnstudio.futurefood.client.util.ColorUtil.colorValue;
import static top.ctnstudio.futurefood.client.util.ColorUtil.rgbColor;
import static top.ctnstudio.futurefood.client.util.GraphicsPlaneRenderUtil.renderRegularPolygonXYByRadius;
import static top.ctnstudio.futurefood.datagen.tag.FfBlockTags.UNLIMITED_RECEIVE;

/**
 * 高亮无限链接渲染
 */
@OnlyIn(Dist.CLIENT)
@EventBusSubscriber
public class HighlightLinksRender {
  @SubscribeEvent
  public static void renderLevelStageEvent(final RenderLevelStageEvent event) {
    final var stage = event.getStage();
    if (stage != Stage.AFTER_TRIPWIRE_BLOCKS) {
      return;
    }

    final Minecraft minecraft = Minecraft.getInstance();
    final ClientLevel level = minecraft.level;
    final LocalPlayer player = minecraft.player;
    if (level == null || player == null || !player.isAlive() || (
      !player.getMainHandItem().is(Tags.Items.TOOLS_WRENCH) &&
        !player.getItemInHand(InteractionHand.OFF_HAND).is(Tags.Items.TOOLS_WRENCH))) {
      return;
    }
    final BlockPos playerPos = player.getOnPos();
    final Frustum frustum = event.getFrustum();

    final Map<BlockPos, BlockState> blockPosList;
    blockPosList = ModUtil.rangePos(level, playerPos, CyberWrenchItem.SCOPE, entry -> {
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
    if (blockPosList.isEmpty()) {
      return;
    }

    PoseStack pose = event.getPoseStack();
    Camera camera = event.getCamera();

    render(pose, camera, minecraft, blockPosList);
  }

  private static void render(PoseStack pose, Camera camera, Minecraft minecraft, Map<BlockPos, BlockState> blockPosList) {
    RenderSystem.disableDepthTest();
    var renderBuffers = minecraft.renderBuffers();
    var buffer = renderBuffers.bufferSource();
    pose.pushPose();

    Vec3 cameraPos = camera.getPosition();

    pose.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);
    for (Map.Entry<BlockPos, BlockState> entry : blockPosList.entrySet()) {
      BlockPos blockPos = entry.getKey();
      BlockState blockState = entry.getValue();
      if (blockState.is(UNLIMITED_RECEIVE)) { // 可链接方块

      }
      var blockCenterV3 = blockPos.getCenter();
      pose.pushPose();
      double x = blockCenterV3.x;
      double y = blockCenterV3.y;
      double z = blockCenterV3.z;
      pose.translate(x, y, z);
      pose.mulPose(camera.rotation());
//      pose.mulPose(Axis.XP.rotationDegrees(90));
//      pose.mulPose(Axis.YN.rotationDegrees(90));

      float size = 0.1f;
      float sideLength = 0.35f;
      float alpha = 0.7f;
      var consumer = buffer.getBuffer(getHighlighted());
      Pose last = pose.last();
      var matrix4f = last.pose();

      pose.pushPose();
      {
        int color = rgbColor("#6916af");
        int red = ARGB32.red(color);
        int green = ARGB32.green(color);
        int blue = ARGB32.blue(color);
        int alphaInt = colorValue(alpha);
        renderRegularPolygonXYByRadius(pose, consumer, matrix4f,
          size, 0, 0, sideLength, 6,
          red, green, blue, alphaInt);
      }
      pose.popPose();
      pose.popPose();
    }

    pose.popPose();
    buffer.endBatch();

    RenderSystem.disableDepthTest();
  }
}
